package creature;

import java.util.Random;

import MainPackage.Main;
import MainPackage.TilesManager;
import entity.GameObject;
import entity.Tile;
import mapRender.MapTile;
import mapRender.ObjectPropertiesManager;
import mapRender.TilePropertiesManager;
import playerPackage.Player;

public class SpawnManager {

	private static final int MAX_SLIMES   = 3;
	private static final int MAX_COWS     = 2;
	private static final int MAX_SHEEP    = 2;
	private static final int MAX_CHICKENS = 2;
	private static final long SPAWN_INTERVAL_MS = 60_000;
	private static final int MIN_SPAWN_TILES = 8;
	private static final int MAX_SPAWN_TILES = 14;
	private static final int MAX_TRIES = 15;
	private static final int MAP_SIZE = 50;

	private static final Random RANDOM = new Random();
	private static long lastSpawnTime = 0;

	public static void tick() {
		if (System.currentTimeMillis() - lastSpawnTime < SPAWN_INTERVAL_MS) return;
		lastSpawnTime = System.currentTimeMillis();

		boolean inCave = Main.tilesManager != null && "cave".equals(Main.tilesManager.getMap());

		if (inCave) {
			if (countByType(Slime.class) < MAX_SLIMES) trySpawn("slime", false);
		} else {
			if (countByType(Cow.class)     < MAX_COWS)     trySpawn("cow",     true);
			if (countByType(Sheep.class)   < MAX_SHEEP)    trySpawn("sheep",   true);
			if (countByType(Chicken.class) < MAX_CHICKENS) trySpawn("chicken", true);
		}
	}

	public static void reset() {
		lastSpawnTime = 0;
	}

	private static void trySpawn(String type, boolean grassOnly) {
		if (Main.player == null || Main.tilesManager == null) return;
		int playerTileJ = (Main.player.getX() + Player.playerCollisionBoxX) / TilesManager.tileSize;
		int playerTileI = (Main.player.getY() + Player.playerCollisionBoxY) / TilesManager.tileSize;

		for (int attempt = 0; attempt < MAX_TRIES; attempt++) {
			double angle = RANDOM.nextDouble() * 2 * Math.PI;
			int dist = MIN_SPAWN_TILES + RANDOM.nextInt(MAX_SPAWN_TILES - MIN_SPAWN_TILES + 1);
			int ti = playerTileI + (int)(Math.sin(angle) * dist);
			int tj = playerTileJ + (int)(Math.cos(angle) * dist);

			if (ti < 0 || ti >= MAP_SIZE || tj < 0 || tj >= MAP_SIZE) continue;

			Tile tile = Main.tilesManager.getTiles(ti, tj);
			GameObject obj = Main.tilesManager.getObjects(ti, tj);
			MapTile mapTile = TilePropertiesManager.getTile(tile.getId());

			if (tile.isSolid(0, 0, 64, 64)) continue;
			if (grassOnly && mapTile.isWater()) continue;
			if (obj.getId() != 0 && ObjectPropertiesManager.getObject(obj.getId()).isSolid()) continue;

			CreatureManager.createCreature(tj * TilesManager.tileSize, ti * TilesManager.tileSize, type);
			return;
		}
	}

	private static long countByType(Class<?> cls) {
		return CreatureManager.getCreatures().stream()
				.filter(c -> c.getClass() == cls)
				.count();
	}
}
