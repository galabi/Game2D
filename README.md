# Game2D

Game2D is a top-down, 2D sandbox game developed in Java. It features a tile-based world where players can explore, gather resources, craft items, and interact with various creatures. The game includes both single-player and multiplayer capabilities through a client-server architecture.

## Features

-   **Dynamic World:** Explore a tile-based world with different ground types and a separate cave map.
-   **Resource Gathering:** Chop trees, mine rocks, and gather resources from the environment. Certain resources like trees and rocks will naturally regenerate over time.
-   **Crafting System:** Combine resources in a 2x2 crafting grid to create new tools and items like axes, pickaxes, and fire pits.
-   **Inventory Management:** A full inventory system with a hotbar, drag-and-drop functionality, and item stacking.
-   **Interactive Objects:** Interact with the world by placing blocks, cooking fish on a fire pit, and more.
-   **Fishing:** A fishing mini-game allows you to catch fish from bodies of water.
-   **Creatures:** The world is populated with passive creatures such as chickens, cows, sheep, and hostile slimes.
-   **Save/Load System:** Player progress, inventory, and the world state are saved to text files, allowing you to continue your adventure later.
-   **Multiplayer:** Features a client-server model for multiplayer gameplay. Host a world for your friends to join or connect to an existing server.

## Getting Started

To run the game, you will need to have the Java Development Kit (JDK) installed on your system.

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/galabi/Game2D.git
    cd Game2D
    ```
2.  **Compile the source code:**
    Use your preferred Java IDE (like Eclipse or IntelliJ IDEA) to open the project and compile the source files located in the `src` directory.
3.  **Run the game:**
    Execute the `main` method in the `src/MainPackage/Main.java` file to start the game.

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
-   **Start a New Game:** From the main menu, click "Start" to generate a new single-player world. The game will automatically save every 10 seconds.
-   **Multiplayer:**
    -   To host a game, go to the pause menu (`Escape`) and select "Open-lan".
    -   To join a game, click "Multiplayer" on the main menu. The game will scan for available servers on the local network, which you can then select to join.
-   **Crafting:** Open your inventory (`E`) to access the 2x2 crafting grid. Place items in the grid to discover recipes.
-   **Fishing:** Equip a fishing rod, right-click on a body of water to cast your line, and right-click again when you see bubbles to reel in a fish.

## Project Structure

-   `src/`: Contains all Java source code, organized into the following packages:
    -   `Creature/`: Defines the behavior and properties of in-game creatures (Slime, Cow, etc.).
    -   `Entity/`: Contains base classes for all game objects, tiles, particles, and other entities.
    -   `MainPackage/`: Manages the core game loop, window, and main classes.
    -   `mutiplayer/`: Implements the client-server architecture for multiplayer functionality.
    -   `playerPackage/`: Handles the player character, controls, animations, and interactions.
    -   `Regeneration/`: Manages the system for regenerating resources like trees and rocks over time.
    -   `Storage/`: Implements the inventory, item, and crafting systems.
-   `saves/`: Stores game save data, including map layouts and player inventory.
-   `Creature_image_file/`, `itemicon/`, `playerIcons/`, `tiles_source/`: Directories containing all the graphical assets for the game.
-   `fonts/`: Contains the pixel-art fonts used for the UI.
