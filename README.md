# SoulKnight

SoulKnight is a 2D action game developed in Java using the LibGDX framework. It features dynamic gameplay with various characters, monsters, and levels. The game supports single-player mode, a basic online multiplayer mode, and the ability to record gameplay as GIFs.

## Features

*   **Dynamic Gameplay:** Engage in fast-paced combat with player characters against diverse monsters.
*   **Multiple Levels:** Progress through different game levels (`level0.txt`, `level1.txt`, `level2.txt`) with unique layouts.
*   **Characters & Monsters:**
    *   Playable Character (Single Player): The Knight.
    *   Playable Characters (Multiplayer): Knight, Snowman, Warrior.
    *   Enemy Types: Face distinct monsters like Master, Collider, and Raider, each with unique attack patterns and behaviors.
*   **Game States:** Includes menu, gameplay, pause, win, and lose screens.
*   **Networking:**
    *   Supports basic online multiplayer functionality allowing players to join a game server.
    *   Implemented using Netty for client-server communication.
*   **GIF Recording:** Option to record gameplay sessions and save them as GIF files.
*   **Save/Load System:** Save your single-player game progress and resume later.
*   **Custom Assets:** Features custom-designed graphics for characters, bullets, environments, UI elements, as well as sound effects and background music.

## Project Structure

The repository is organized as follows:

*   `assets/`: Contains all game assets including images, fonts, maps, music, and UI skins.
    *   `basic/`: Core visual assets (buildings, bullets, doors, roles).
    *   `fonts/`: Font files used in the game.
    *   `maps/`: Text files defining game level layouts.
    *   `music/`: Sound effects and background music.
    *   `ui/`: UI skin files and related figures.
*   `core/src/com/mygdx/soulknight/`: Main game logic built with LibGDX.
    *   `Enum/`: Enumerations for game states, entity types, etc.
    *   `abstractclass/`: Base classes for game entities like `Creature`, `Monster`, `NetPlayer`.
    *   `assets/`: Classes for loading and managing game media (e.g., `MediaGamePlaying`) and global variables (`Variable.java`).
    *   `character/`: Player character (`Knight`), monster implementations (`Master`, `Collider`, `Raider`), and network player characters (`NetKnight`, `NetSnowman`, `NetWarrior`).
    *   `entity/`: Game entities like `Bullet`, `Door`, `Floor`, `Wall`.
    *   `io/`: Input/Output operations, including GIF recording (`GifRecorder`) and game data persistence (`SaveGameDataTool`, `LoadGameDataTool`).
    *   `map/`: Map loading and generation logic (`Map.java`).
    *   `network/`: Client-side (`OnlineGameScreen`) and server-side (`GameServer`) logic for multiplayer.
    *   `screen/`: Different game screens (Menu, Gameplay, Pause, Win, Lose).
    *   `thread/`: Multithreading for monster AI (`MonsterThread`) and game management.
*   `desktop/src/com/mygdx/soulknight/`: Desktop-specific launchers.
    *   `DesktopLauncher.java`: Main entry point for the game client (single-player and multiplayer).
    *   `GameServerLauncher.java`: Entry point for the multiplayer game server.
*   `gradle/`: Gradle wrapper files for building the project.
*   `tests/`: Contains unit tests for the game.

## Technologies Used

*   **Java:** Primary programming language.
*   **LibGDX:** Cross-platform game development framework.
*   **Gradle:** Build automation tool.
*   **Netty:** Framework for network application programming (used in multiplayer).

## How to Run

The project is built using Gradle. Ensure you have a JDK (e.g., Java 11 or compatible) installed.

### Single Player

1.  Clone the repository.
2.  Open the project in an IDE that supports Gradle (e.g., IntelliJ IDEA, Eclipse).
3.  Run the `DesktopLauncher` class located in `desktop/src/com/mygdx/soulknight/DesktopLauncher.java`.
    Alternatively, you can use the Gradle task from the project's root directory:
    ```bash
    ./gradlew desktop:run
    ```

### Multiplayer

1.  **Start the Server:**
    *   Run the `GameServerLauncher` class located in `desktop/src/com/mygdx/soulknight/GameServerLauncher.java`.
    *   Alternatively, use the Gradle command from the project's root directory:
        ```bash
        ./gradlew desktop:execute -PmainClass=com.mygdx.soulknight.GameServerLauncher
        ```
    The server will start listening on the port defined in `core/src/com/mygdx/soulknight/assets/Variable.java` (default is `localhost:12345`).

2.  **Start Player Clients:**
    *   Run one or more instances of the `DesktopLauncher` class as described in the Single Player section.
    *   From the main menu on each client, select "Play with Friends".
    *   Clients will attempt to connect to the server IP and port specified in `Variable.java`.

## Controls

*   **Movement:** `W` (Up), `A` (Left), `S` (Down), `D` (Right)
*   **Shoot:** `SPACE`
*   **Enter Portal/Next Level (Single Player):** `ENTER` (when all monsters are defeated and the player is on the portal)
*   **Interact/Next Wave (Multiplayer):** `ENTER` (when player is on the portal and monsters are cleared, to spawn next wave)
*   **Pause Menu / Exit Online Game:** `ESCAPE`
*   **Toggle GIF Recording:** `T` (Recording must be enabled by setting `RECORD_FUNCTION = true` in `Variable.java`)

## Configuration

Various game parameters can be configured in `core/src/com/mygdx/soulknight/assets/Variable.java`. This includes:
*   Character stats (health, attack, defense, speed) for Knight, Snowman, and Warrior.
*   Monster attributes and behavior.
*   Bullet properties (speed, size).
*   Network settings (default server IP, port).
*   GIF recording options (FPS, activation key, output directory).
*   Initial positions for characters, door location, and tile sizes.
*   Paths to game data files and level files.
