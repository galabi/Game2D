package creature;

import MainPackage.Main;
import MainPackage.TilesManager;
import playerPackage.Player;
import storage.ItemIds;

public class Slime extends Creature{


	public Slime(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		image = CreatureImage.getSlimeImage();
		damage = 1;
		health = 5;
		CollisionBoxX = 12;
		CollisionBoxY = 32;
		CollisionBoxWidth = 40;
		CollisionBoxHeight = 32;
		lootIds.add(ItemIds.STONE);
	}

	public void setNextLocation() {
		if (Main.player == null) { super.setNextLocation(); return; }

		int playerCX = Main.player.getX() + Player.playerCollisionBoxX + Player.playerCollisionBoxWidth / 2;
		int playerCY = Main.player.getY() + Player.playerCollisionBoxY + Player.playerCollisionBoxHeight / 2;
		int myCX = x + CollisionBoxX + CollisionBoxWidth / 2;
		int myCY = y + CollisionBoxY + CollisionBoxHeight / 2;
		int dist = Math.abs(playerCX - myCX) + Math.abs(playerCY - myCY);

		if (dist > TilesManager.tileSize * 5) { super.setNextLocation(); return; }

		nextMoveTime = 0;
		if (Math.abs(playerCX - myCX) >= Math.abs(playerCY - myCY)) {
			targetX = Main.player.getX();
			targetY = y;
			speed = playerCX > myCX ? Math.abs(speed) : -Math.abs(speed);
			creatureDirection = playerCX > myCX ? 2 : 1;
		} else {
			targetX = x;
			targetY = Main.player.getY();
			speed = playerCY > myCY ? Math.abs(speed) : -Math.abs(speed);
			creatureDirection = playerCY > myCY ? 0 : 3;
		}
	}

}
