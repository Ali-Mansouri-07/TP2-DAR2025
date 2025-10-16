package clientPackage;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            System.out.println("Je suis un client pas encore connecté...");
            
            Socket socket = new Socket("localhost", 12346);
            System.out.println("Un client est connecté au serveur");
            
            // Flux pour envoyer les opérations au serveur
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            // Flux pour recevoir les résultats du serveur
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            Scanner scanner = new Scanner(System.in);
            String operation;

            System.out.println("Calculatrice Client-Serveur");
            System.out.println("Format: nombre opérateur nombre (ex: 55 * 25)");
            System.out.println("Opérateurs supportés: +, -, *, /");
            System.out.println("Tapez '0' pour quitter\n");

            do {
                System.out.print("Entrez une opération: ");
                operation = scanner.nextLine().trim();
                
                // Validation de l'opération avant envoi
                if (!operation.equals("0")) {
                    // Formater l'opération avant validation
                    operation = operation.replaceAll("([+\\-*/])", " $1 ").replaceAll("\\s+", " ").trim();
                    
                    if (!validerOperation(operation)) {
                        System.out.println("Erreur: Opération invalide. Format: nombre opérateur nombre\n");
                        continue;
                    }
                }
                
                // Envoi de l'opération au serveur
                pw.println(operation);
                
                if (!operation.equals("0")) {
                    // Réception et affichage du résultat
                    String resultat = br.readLine();
                    System.out.println("Résultat reçu: " + resultat + "\n");
                }
                
            } while (!operation.equals("0"));

            System.out.println("Fin de communication");
            
            // Fermeture des ressources
            scanner.close();
            br.close();
            pw.close();
            socket.close();

        } catch (UnknownHostException e) {
            System.err.println("Hôte inconnu: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Erreur de connexion: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour valider la syntaxe d'une opération avant envoi au serveur
     * @param operation l'opération à valider
     * @return true si l'opération est valide, false sinon
     */
    private static boolean validerOperation(String operation) {
        try {
            String[] elements = operation.split(" ");
            
            // Vérification du nombre d'éléments
            if (elements.length != 3) {
                return false;
            }
            
            // Vérification des opérandes (doivent être des nombres)
            Double.parseDouble(elements[0]);
            Double.parseDouble(elements[2]);
            
            // Vérification de l'opérateur
            String operateur = elements[1];
            if (!operateur.equals("+") && !operateur.equals("-") && 
                !operateur.equals("*") && !operateur.equals("/")) {
                return false;
            }
            
            return true;
            
        } catch (NumberFormatException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}