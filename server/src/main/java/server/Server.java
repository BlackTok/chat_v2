package server;

import commands.Command;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


public class Server {
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;

    public Server() {
        clients = new CopyOnWriteArrayList<>();
        authService = new SimpleAuthService();
        try {
            server = new ServerSocket(PORT);
            System.out.println("server started");

            while (true) {
                socket = server.accept();
                System.out.println("client connected" + socket.getRemoteSocketAddress());
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(ClientHandler sender, String msg){
        String message = String.format("[ %s ] : %s", sender.getNickname(), msg);
        for (ClientHandler c : clients) {
            c.sendMsg(message);
        }
    }
    
    public void broadcastPrivateMsg (ClientHandler sender, String recipientName, String msg) {
        String message = String.format("[ %s ] : %s", sender.getNickname(), msg);
        ClientHandler recipient = null;

        for (ClientHandler client : clients) {
            if (client.getNickname().equals(recipientName)) {
                recipient = client;
                break;
            }
        }

        if (recipient == null) {
            sender.sendMsg(Command.ERROR_MSG_UNKNOWN_USER);
        } else if (recipient.equals(sender)) {
            sender.sendMsg(Command.ERROR_MSG_SEND_YOURSELF);
        } else {
            sender.sendMsg(message);
            recipient.sendMsg(message);
        }
    }

    public void subscribe(ClientHandler clientHandler){
        clients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler){
        clients.remove(clientHandler);
    }

    public AuthService getAuthService() {
        return authService;
    }
}
