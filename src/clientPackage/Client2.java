package clientPackage;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import common.Operation;

public class Client2 {
    public static void main(String[] args) {
        try {
            System.out.println("Client démarrage...");
            
            Socket socket = new Socket("localhost", 12346);
            System.out.println("Connecté au serveur");
            
            // Flux pour l'échange d'objets
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("=== CALCULATICE CLIENT-SERVEUR ===");
            System.out.println("Format: nombre opérateur nombre (ex: 55 * 25)");
            System.out.println("Opérateurs supportés: +, -, *, /");
            System.out.println("Tapez 'quit' pour quitter\n");

            while (true) {
                System.out.print("Entrez une opération: ");
                String input = scanner.nextLine().trim();
                
                if (input.equalsIgnoreCase("quit")) {
                    break;
                }
                
                // Valider et parser l'opération
                Operation operation = parserOperation(input);
                if (operation == null) {
                    System.out.println("Erreur: Format invalide. Utilisez: nombre opérateur nombre\n");
                    continue;
                }
                
                // Envoyer l'objet Operation au serveur
                oos.writeObject(operation);
                oos.flush();
                System.out.println("Opération envoyée au serveur...");
                
                // Recevoir le résultat du serveur
                Operation resultat = (Operation) ois.readObject();
                
                // Afficher le résultat
                if (resultat.getErreur() != null) {
                    System.out.println("❌ " + resultat.getErreur() + "\n");
                } else {
                    System.out.println("✅ Résultat: " + resultat.getOperande1() + " " + 
                                     resultat.getOperateur() + " " + resultat.getOperande2() + 
                                     " = " + resultat.getResultat() + "\n");
                }
            }

            System.out.println("Déconnexion...");
            
            // Fermeture des ressources
            scanner.close();
            ois.close();
            oos.close();
            socket.close();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour parser une chaîne en objet Operation
     */
    private static Operation parserOperation(String input) {
        try {
            // Nettoyer et formater l'input
            input = input.replaceAll("([+\\-*/])", " $1 ")
                        .replaceAll("\\s+", " ")
                        .trim();
            
            String[] elements = input.split(" ");
            
            if (elements.length != 3) {
                return null;
            }
            
            // Extraire les opérandes
            double operande1 = Double.parseDouble(elements[0]);
            String operateur = elements[1];
            double operande2 = Double.parseDouble(elements[2]);
            
            // Valider l'opérateur
            if (!operateur.equals("+") && !operateur.equals("-") && 
                !operateur.equals("*") && !operateur.equals("/")) {
                return null;
            }
            
            // Créer et retourner l'objet Operation
            return new Operation(operande1, operateur, operande2);
            
        } catch (NumberFormatException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}