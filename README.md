# Conway's Game of Life - JavaFX Application

[Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) is a captivating [cellular automaton](https://en.wikipedia.org/wiki/Cellular_automaton) that exhibits intriguing patterns. A cellular automaton consists of a grid of cells, where each cell undergoes state changes based on a predefined set of rules over time.

## Rules of the Game
The Game of Life follows a few fundamental rules:

- **Survival**: Any live cell with 2 or 3 live neighbours persists to the next generation.
- **Underpopulation**: Any live cell with fewer than 2 live neighbours dies due to underpopulation.
- **Overpopulation**: Any live cell with more than 3 live neighbours dies due to overpopulation.
- **Reproduction**: Any dead cell with exactly 3 live neighbours comes to life, simulating reproduction.

## JavaFX Game of Life Simulation

This JavaFX application provides a visually engaging simulation of Conway's Game of Life. The user-friendly graphical interface enables users to interact with the cellular automaton, observe its dynamic evolution, and explore emergent patterns.

### Key Features:

- **Random Configuration:** Begin the simulation with a dynamically generated random configuration of living cells.

- **Manual Initialization:** Take control and manually set your preferred initial cell configuration using your mouse.

- **Basic Configurations:** Explore basic configurations, including classics like the [Gosper Glider Gun](https://conwaylife.com/wiki/Gosper_glider_gun) or the [Simkin glider gun](https://conwaylife.com/wiki/Simkin_glider_gun).

### Preview:

Below is a glimpse of the simulation featuring a randomly generated configuration:

<p align="center">
  <img src="src/GameOfLifeRecording.gif" alt="Random Configuration Demo">
</p>

## Prerequisites

### Java Version:

This project was run on Java 17; however, it can likely be run on later versions of Java. If you don't have Java installed, you can download and install it from the official [Java Download Page](https://www.oracle.com/java/technologies/javase-downloads.html).

### JavaFX Version and Configuration in IntelliJ:

This application runs on JavaFX version 20. To install JavaFX, visit the [OpenJFX download page](https://gluonhq.com/products/javafx/). Once JavaFX is installed, configure it correctly in your IDE. If you're using IntelliJ, follow these steps:

1. Unzip the JavaFX archive to create a folder named `javafx-sdk-20` with subfolders `legal` and `lib`.
2. In IntelliJ, open settings (`Appearance and Behavior` > `Path Variables`).
3. Click on the `+` button. Under **Name**, write `JFX_PATH` and under **Value**, write the path to the `lib` subfolder of the unzipped JavaFX archive.
4. Open IntelliJ IDEA and go to the `File` menu.
5. Choose `New Project Setup` and then select `Structure`.
6. In the left sidebar, click on `Global Libraries` under the `Platform Settings` section.
7. In the central section, click the `+` button at the top and select `Java` from the `New Global Library` menu.
8. Navigate to the `lib` folder created during the archive decompression of OpenJFX.
9. Select all files in the `lib` folder and click `Open`.
10. Change the name of the library to "OpenJFX 20" by modifying the field next to the `Name:` label.
11. Click the `+` button under the `Name:` label.
12. Select the `src.zip` file located in the parent folder of the `lib` folder and click `Open`.
14. Click `OK` in the dialog box that opens.

### Using JavaFX in a Project:

Now that the JavaFX library is correctly installed in your IDE, here is the way to use it in your project: 

1. In the `File` menu, select `Project Structure`.
2. Click on `Modules` in the `Project Settings` section.
3. Select the `Dependencies` tab.
4. Click on the `+` button, then choose `Library` from the menu that opens.
5. In the window that opens, select `OpenJFX 20`, then click on `Add Selected`.

If you encounter issues, try the following:

1. In the `Run` menu, choose `Edit Configurations`.
2. Click on `Modify options`, then choose `Add VM options`.
3. In the field entitled `VM options`, add the following line (replace `$JFX_PATH$` with your JavaFX Path): `--module-path $JFX_PATH$ --add-modules javafx.controls`

This setup ensures that the OpenJFX 20 library is properly configured in IntelliJ IDEA, allowing you to seamlessly integrate it into your JavaFX project.


