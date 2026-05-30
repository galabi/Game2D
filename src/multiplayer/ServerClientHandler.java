package multiplayer;

import MainPackage.Main;
import creature.CreatureManager;
import playerPackage.Player;
import storage.Item;
import entity.GameObject;

public class ServerClientHandler {

    public static Server server;
    public static Client client;

    public static void openServer() {
        server = new Server();
        new Thread(server).start();
    }

    public static void openClient(int port) {
        client = new Client(port);
        new Thread(client).start();
    }

    public static void sendDataToServer(String data) {
        if (server != null) server.sendToClient(data);
        if (client != null) client.sendToServer(data);
    }

    // Called server-side for each message received from a specific client
    public static void responseHandler(String response, Server.ClientHandler sender) {
        String[] arr = response.split(" ");
        switch (arr[0]) {
        case "join":
            if (arr.length > 1) {
                sender.playerName = arr[1];
                server.broadcast("player_name " + sender.id + " " + sender.playerName, sender);
                Main.remotePlayerNames.put(sender.id, sender.playerName);
                Player rp = Main.remotePlayers.get(sender.id);
                if (rp != null) rp.setName(sender.playerName);
            }
            break;
        case "player:":
            // Re-broadcast with sender's ID so all other clients and host can track this player
            String tagged = "player_" + sender.id + ": " + arr[1] + " " + arr[2] + " " + arr[3]
                    + " " + arr[4] + " " + arr[5] + " " + arr[6] + " " + arr[7] + " " + arr[8];
            server.broadcast(tagged, sender);
            // Also update on host side
            updateRemotePlayer(sender.id, arr, 1);
            break;
        case "attack_creature":
            if (Main.host) {
                int strength = Integer.parseInt(arr[1]);
                Player rp = Main.remotePlayers.get(sender.id);
                if (rp != null) {
                    int px = rp.getX() + Player.playerCollisionBoxX;
                    int py = rp.getY() + Player.playerCollisionBoxY;
                    CreatureManager.attackCreatureInRange(px, py, strength);
                }
            }
            break;
        default:
            // All other messages (update_block, add_drop, remove_drop, etc.) broadcast as-is
            server.broadcast(response, sender);
            responseHandler(response); // also apply locally on host
            break;
        }
    }

    // Called client-side (no sender)
    public static void responseHandler(String response) {
        String[] arr = response.split(" ");
        switch (arr[0]) {
        case "player:":
            // Host position → remote player 0
            updateRemotePlayer(0, arr, 1);
            break;
        case "update_block":
            ((GameObject) Main.tilesManager.getObjects()
                [Integer.parseInt(arr[1])][Integer.parseInt(arr[2])]).setId(Integer.parseInt(arr[3]));
            break;
        case "map:":
            Main.tilesManager.setMapFromMultiplayer(arr);
            break;
        case "items:":
            Main.tilesManager.setItemsOnTilesFromMultiplayer(arr);
            break;
        case "drops:":
            Main.tilesManager.setDropsFromMultiplayer(arr);
            break;
        case "remove_drop":
            Main.tilesManager.removeDrop(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
            break;
        case "add_drop":
            Item temp = new Item(Integer.parseInt(arr[1]));
            temp.setQuantity(Integer.parseInt(arr[2]));
            Main.tilesManager.addItemDrop(temp, Integer.parseInt(arr[3]), Integer.parseInt(arr[4]));
            break;
        case "creatures:":
            CreatureManager.syncFromNetwork(arr);
            break;
        case "chests:":
            Main.chestStorage.applyFromNetwork(arr);
            break;
        case "chest_update":
            Main.chestStorage.applyChestUpdate(arr);
            break;
        case "player_name":
            if (arr.length > 2) {
                int nameId = Integer.parseInt(arr[1]);
                String pname = arr[2];
                Main.remotePlayerNames.put(nameId, pname);
                Player named = Main.remotePlayers.get(nameId);
                if (named != null) named.setName(pname);
            }
            break;
        case "player_left":
            int leftId = Integer.parseInt(arr[1]);
            Main.remotePlayers.remove(leftId);
            Main.remotePlayerNames.remove(leftId);
            break;
        default:
            // Handle "player_N:" messages for other remote players
            if (arr[0].startsWith("player_") && arr[0].endsWith(":")) {
                String idStr = arr[0].substring(7, arr[0].length() - 1);
                try {
                    int id = Integer.parseInt(idStr);
                    updateRemotePlayer(id, arr, 1);
                } catch (NumberFormatException ignored) {}
            }
            break;
        }
    }

    private static void updateRemotePlayer(int id, String[] arr, int offset) {
        Player rp = Main.remotePlayers.get(id);
        if (rp == null) {
            rp = new Player(Integer.parseInt(arr[offset]), Integer.parseInt(arr[offset + 1]),
                    Main.player.getSizeX());
            Main.remotePlayers.put(id, rp);
        }
        String pendingName = Main.remotePlayerNames.get(id);
        if (pendingName != null) rp.setName(pendingName);
        rp.setX(Integer.parseInt(arr[offset]));
        rp.setY(Integer.parseInt(arr[offset + 1]));
        rp.setImageDirection(Integer.parseInt(arr[offset + 2]));
        rp.setImagePosture(Integer.parseInt(arr[offset + 3]));
        rp.setSpeed(Integer.parseInt(arr[offset + 4]), Integer.parseInt(arr[offset + 5]));
        rp.playerI = Integer.parseInt(arr[offset + 6]);
        rp.playerJ = Integer.parseInt(arr[offset + 7]);
    }
}
