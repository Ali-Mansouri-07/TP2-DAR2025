package serverPackage;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import common.Operation;

public class Server2 {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12346);
            System.out.println("Serveur démarré sur le port 12346...");
            System.out.println("En attente de connexions clients...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("\nNouveau client connecté: " + socket.getInetAddress());
                
                // Créer un thread pour gérer chaque client
                new ClientHandler(socket).start();
            }

        } catch (IOException e) {
            System.err.println("Erreur du serveur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Flux d'objets initialisés pour le client");

            Operation operation;
            while ((operation = (Operation) ois.readObject()) != null) {
                System.out.println("Opération reçue: " + 
                    operation.getOperande1() + " " + 
                    operation.getOperateur() + " " + 
                    operation.getOperande2());

                // Calculer l'opération
                Operation resultat = calculerOperation(operation);
                
                // Envoyer le résultat au client
                oos.writeObject(resultat);
                oos.flush();
                System.out.println("Résultat envoyé: " + resultat.getResultat());
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client déconnecté: " + socket.getInetAddress());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Méthode pour calculer une opération mathématique
     */
    private Operation calculerOperation(Operation operation) {
        try {
            double operande1 = operation.getOperande1();
            String operateur = operation.getOperateur();
            double operande2 = operation.getOperande2();
            
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
                        operation.setErreur("Division par zéro impossible");
                        return operation;
                    }
                    resultat = operande1 / operande2;
                    break;
                default:
                    operation.setErreur("Opérateur non supporté. Utilisez +, -, *, /");
                    return operation;
            }
            
            operation.setResultat(resultat);
            return operation;
            
        } catch (Exception e) {
            operation.setErreur("Erreur lors du calcul: " + e.getMessage());
            return operation;
        }
    }
}