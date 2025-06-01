# Java Multiplayer Chess Game

A complete chess game written in Java with support for multiplayer networking over a local or remote server. Built using a modular design with Swing GUI, TCP sockets, and Maven for build management.

## Project Context
- Final year academic project
- Year: 2024–2025
- Developed in collaboration with ChatGPT (as assistant)

## Features
- Chess game with full rules implementation
- Local GUI with player switch
- Multiplayer support via TCP (client-server)
- Clean modular structure (Board, Game, Network, UI)
- Built with Maven

## Tech Stack
- Java 8+
- Maven
- Java Swing
- TCP Sockets

## How to Run
1. Clone the repo
2. Open in IDE or use terminal:
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.chessgame.Main"
   ```

Make sure server is running before connecting clients.

## Project Structure
com.chessgame/
|
├── Board/       # Game board and moves
├── Frame/       # UI and panels
├── Game/        # Game logic
├── Network/     # Client-server communication
├── Main.java    # Entry point

## Report
See `report_2221251373.pdf` for full documentation and explanation.

Developed as an educational project with ChatGPT assistance for logic, networking, and documentation.
