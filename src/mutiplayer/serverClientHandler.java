package mutiplayer;

import Entity.Object;
import MainPackage.Main;
import Storage.Item;
import playerPackage.Player;

public class serverClientHandler {

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
		if(server != null) {
			server.sendToClient(data);
		}
		if(client != null) {
			client.sendToServer(data);
		}
	}
	
	public static void responseHandeler(String response) {
		String[] responseArr = response.split(" ");
		switch (responseArr[0]) {
		case "player:":
            if (Main.player2 == null) {
                Main.player2 = new Player(Integer.parseInt(responseArr[1]), Integer.parseInt(responseArr[2]), Main.player.getSizeX());
            }
            Main.player2.setX(Integer.parseInt(responseArr[1]));
            Main.player2.setY(Integer.parseInt(responseArr[2]));
            Main.player2.setImageDirection(Integer.parseInt(responseArr[3]));
            Main.player2.setImagePosture(Integer.parseInt(responseArr[4]));			
            Main.player2.setSpeed(Integer.parseInt(responseArr[5]), Integer.parseInt(responseArr[6]));	
            Main.player2.playerI = Integer.parseInt(responseArr[7]);
            Main.player2.playerJ = Integer.parseInt(responseArr[8]);

            break;
		case "map:":
			Main.tilesManager.setMapFromMultiplayer(responseArr);
			break;
		case "update_block":
			((Object) Main.tilesManager.getObjects()[Integer.parseInt(responseArr[1])][Integer.parseInt(responseArr[2])]).setType(Integer.parseInt(responseArr[3]));
			break;
		case "items:":
			Main.tilesManager.setItemsOnTilesFromMultiplayer(responseArr);
			break;
		case "drops:":
			Main.tilesManager.setDropsFromMultiplayer(responseArr);
			break;
		case "remove_drop":
			Main.tilesManager.removeDrop(Integer.parseInt(responseArr[1]), Integer.parseInt(responseArr[2]));
			break;
		case "add_drop":
			Item temp = new Item(Integer.parseInt(responseArr[1]));
			temp.setQuantity(Integer.parseInt(responseArr[2]));
			Main.tilesManager.addItemDrop(new Item(Integer.parseInt(responseArr[1])),Integer.parseInt(responseArr[3]) , Integer.parseInt(responseArr[4]));
			break;
		}
		
	}
	
}
