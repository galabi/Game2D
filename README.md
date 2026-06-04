# Game2D

Game2D is a top-down, 2D sandbox game developed in Java. It features a tile-based world where players can explore, gather resources, craft items, and interact with various creatures. The game includes both single-player and multiplayer capabilities through a client-server architecture.

## Features

-   **Dynamic World:** Explore a tile-based world with different ground types and a separate cave map.
-   **Resource Gathering:** Chop trees, mine rocks, and gather resources from the environment. Resources like trees and rocks regenerate naturally over time.
-   **Crafting System:** Combine resources in a 2x2 crafting grid to create tools and items like axes, pickaxes, and fire pits.
-   **Inventory Management:** A full inventory system with a hotbar, drag-and-drop functionality, and item stacking.
-   **Chest Storage:** Place chests in the world to store items. Supports full drag-and-drop between your inventory and the chest. Breaking a chest drops all its contents.
-   **Interactive Objects:** Place blocks, cook fish on a fire pit, and more.
-   **Fishing:** A fishing mini-game lets you catch fish from bodies of water.
-   **Combat System:** Attack creatures with melee weapons. Enemies deal damage with knockback, and your health regenerates slowly over time.
-   **Health & Hunger:** A survival system with heart and hunger slot UI. Hunger decays over time and causes starvation damage when depleted. Eat food to restore hunger.
-   **Creatures:** The world is populated with passive creatures (chickens, cows, sheep) and hostile slimes that chase and attack the player. Creatures spawn automatically with population caps based on tile type.
-   **Game Over Screen:** Dying sends you to a game over screen where you can respawn or return to the main menu.
-   **Minimap:** A minimap overlay in the top-right corner shows the surrounding terrain and your current position.
-   **Save/Load System:** Player progress, inventory, world state, and creature positions are saved in a binary format, allowing you to continue your adventure later.
-   **Multiplayer:** Features a client-server model for multiplayer gameplay. Host a world for friends to join or connect to an existing server. Other players are shown with name tags above their heads.

## Getting Started

To run the game, you will need to have the Java Development Kit (JDK) installed on your system.

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/galabi/Game2D.git
    cd Game2D
    ```
2.  **Compile the source code:**
    Open the project in your preferred Java IDE (IntelliJ IDEA or Eclipse) and compile the source files in the `src` directory.
3.  **Run the game:**
    Execute the `main` method in `src/MainPackage/Main.java`.

## How to Play

### Controls
-   **Movement:** `W` (Up), `A` (Left), `S` (Down), `D` (Right)
-   **Sprint:** Hold `Space` while moving.
-   **Open/Close Inventory:** `E`
-   **Pause Game / Close UI:** `Escape`
-   **Select Hotbar Slot:** `1` - `5`
-   **Break/Attack:** Hold `Left Mouse Button`
-   **Use Item/Place Block:** `Right Mouse Button`
-   **Toggle Dev Mode:** `Q` (Displays collision boxes and other debug info)

### Gameplay
-   **Start a New Game:** From the main menu, click "Start" to generate a new single-player world. The game auto-saves every 10 seconds.
-   **Multiplayer:**
    -   To host a game, open the pause menu (`Escape`) and select "Open-lan".
    -   To join a game, click "Multiplayer" on the main menu. The game scans for available servers on the local network, which you can select to join.
-   **Crafting:** Open your inventory (`E`) to access the 2x2 crafting grid. Place items in the grid to discover recipes.
-   **Chests:** Craft a chest and place it in the world. Right-click to open it and drag items between your inventory and the chest.
-   **Fishing:** Equip a fishing rod, right-click on water to cast, then right-click again when you see bubbles to reel in a fish.
-   **Hunger:** Keep your hunger topped up by eating food. If hunger runs out, you will take starvation damage over time.

## Project Structure

-   `src/`: Contains all Java source code, organized into the following packages:
    -   `creature/`: Defines the behavior and properties of in-game creatures (Slime, Cow, Sheep, Chicken) and manages automatic spawning.
    -   `entity/`: Base classes for all game objects, tiles, particles, and other entities.
    -   `MainPackage/`: Core game loop, window, main menu, pause screen, game over screen, minimap renderer, and tile management.
    -   `mapRender/`: Map tile and object rendering, including tile/object property managers.
    -   `multiplayer/`: Client-server architecture for multiplayer functionality.
    -   `playerPackage/`: Player character, controls, animations, block breaking, and interactions.
    -   `regeneration/`: System for regenerating resources like trees and rocks over time.
    -   `storage/`: Inventory, item, crafting, chest storage, and save/load systems.
-   `saves/`: Stores game save data in binary format, including map layouts, player inventory, and creature positions.
-   `Creature_image_file/`, `itemicon/`, `playerIcons/`, `tiles_source/`: Graphical assets for the game.
-   `fonts/`: Pixel-art fonts used for the UI.
