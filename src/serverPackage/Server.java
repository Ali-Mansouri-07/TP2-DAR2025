package serverPackage;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket socketserveur = new ServerSocket(12346);
            System.out.println("Je suis un serveur en attente de la connexion d'un client sur le port 12346...");

            Socket socket = socketserveur.accept();
            System.out.println("Un client est connecté");

            // Flux pour recevoir les données du client
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Flux pour envoyer les résultats au client
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            String operation;
            while ((operation = br.readLine()) != null) {
                System.out.println("Opération reçue: " + operation);
                
                // Si le client envoie "0", on arrête la communication
                if (operation.equals("0")) {
                    System.out.println("Fin de communication demandée par le client");
                    break;
                }
                
                // Traitement de l'opération
                String resultat = calculerOperation(operation);
                pw.println(resultat);
                System.out.println("Résultat envoyé: " + resultat);
            }

            // Fermeture des ressources
            br.close();
            pw.close();
            socket.close();
            socketserveur.close();
            System.out.println("Serveur arrêté");

        } catch (IOException e) {
            System.err.println("Erreur du serveur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour calculer une opération mathématique
     * @param operation l'opération sous forme de chaîne (ex: "55 * 25")
     * @return le résultat ou un message d'erreur
     */
    private static String calculerOperation(String operation) {
        try {
            // Séparation des éléments de l'opération
            String[] elements = operation.split(" ");
            
            // Vérification du format
            if (elements.length != 3) {
                return "ERREUR: Format incorrect. Utilisez: nombre opérateur nombre";
            }
            
            // Extraction des opérandes et de l'opérateur
            double operande1 = Double.parseDouble(elements[0]);
            String operateur = elements[1];
            double operande2 = Double.parseDouble(elements[2]);
            
            // Calcul selon l'opérateur
            double resultat;
            switch (operateur) {
                case "+":
                    resultat = operande1 + operande2;
                    break;
                case "-":
                    resultat = operande1 - operande2;
                    break;
                case "*":
                    resultat = operande1 * operande2;
                    break;
                case "/":
                    if (operande2 == 0) {
                        return "ERREUR: Division par zéro impossible";
                    }
                    resultat = operande1 / operande2;
                    break;
                default:
                    return "ERREUR: Opérateur non supporté. Utilisez +, -, *, /";
            }
            
            return String.valueOf(resultat);
            
        } catch (NumberFormatException e) {
            return "ERREUR: Les opérandes doivent être des nombres valides";
        } catch (Exception e) {
            return "ERREUR: " + e.getMessage();
        }
    }
}