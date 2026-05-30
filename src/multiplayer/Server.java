package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import MainPackage.Main;
import playerPackage.Player;

public class Server implements Runnable {

    private ServerSocket ss;
    private int port = 1000;
    private boolean serverUP = false;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public void run() {
        serverUP = true;
        openServerSocket();
        System.out.println("[server] Listening on port " + port);
        try {
            while (true) {
                try {
                    Socket s = ss.accept();
                    int id = nextId.getAndIncrement();
                    ClientHandler ch = new ClientHandler(s, id);
                    clients.add(ch);
                    System.out.println("[server] Client " + id + " connected: " + s.getInetAddress());
                    // Send initial world state
                    ch.send(Main.tilesManager.toString());
                    ch.send(Main.tilesManager.ObjectsToString());
                    ch.send(Main.tilesManager.DropsToString());
                    ch.send(Main.chestStorage.toSyncString());
                    // Send positions of all currently connected players
                    ch.send(Main.player.toString());
                    ch.send("player_name 0 " + Main.playerName);
                    for (ClientHandler other : clients) {
                        if (other == ch) continue;
                        Player rp = Main.remotePlayers.get(other.id);
                        if (rp != null)
                            ch.send(rp.toString().replace("player:", "player_" + other.id + ":"));
                        ch.send("player_name " + other.id + " " + other.playerName);
                    }
                    new Thread(ch).start();
                } catch (Exception e) {
                    if (!"Socket closed".equals(e.getMessage())) e.printStackTrace();
                }
            }
        } finally {
            try { if (ss != null && !ss.isClosed()) ss.close(); } catch (IOException ignored) {}
        }
    }

    private void openServerSocket() {
        while (ss == null) {
            try {
                ss = new ServerSocket(port);
            } catch (Exception e) {
                port++;
                if (!"Address already in use".equals(e.getLocalizedMessage())) e.printStackTrace();
            }
        }
    }

    /** Broadcast to all clients except the sender (null sender = broadcast to all). */
    public void broadcast(String msg, ClientHandler sender) {
        for (ClientHandler c : clients) {
            if (c != sender) c.send(msg);
        }
    }

    /** Send to all clients (host → everyone). */
    public void sendToClient(String msg) {
        for (ClientHandler c : clients) c.send(msg);
    }

    public int getPort() { return port; }
    public boolean isServerUp() { return serverUP; }
    public List<ClientHandler> getClients() { return clients; }

    // -------------------------------------------------------------------------
    class ClientHandler implements Runnable {

        final int id;
        final Socket socket;
        private final PrintWriter out;
        private final BufferedReader in;
        final Queue<String> queue = new ConcurrentLinkedQueue<>();
        String playerName = "Player";

        ClientHandler(Socket s, int id) throws IOException {
            this.id     = id;
            this.socket = s;
            this.out    = new PrintWriter(s.getOutputStream(), true);
            this.in     = new BufferedReader(new InputStreamReader(s.getInputStream()));
        }

        void send(String msg) {
            queue.add(msg);
        }

        @Override
        public void run() {
            new Thread(new ClientOut()).start();
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    if ("stop".equals(line)) break;
                    ServerClientHandler.responseHandler(line, this);
                }
            } catch (Exception e) {
                if (!"Connection reset".equals(e.getLocalizedMessage())) e.printStackTrace();
            } finally {
                clients.remove(this);
                Main.remotePlayers.remove(id);
                broadcast("player_left " + id, null);
                System.out.println("[server] Client " + id + " disconnected.");
                try { socket.close(); } catch (IOException ignored) {}
            }
        }

        private class ClientOut implements Runnable {
            @Override
            public void run() {
                while (!socket.isClosed()) {
                    if (!queue.isEmpty()) out.println(queue.poll());
                    try { Thread.sleep(5); } catch (InterruptedException ignored) {}
                }
            }
        }
    }
}
