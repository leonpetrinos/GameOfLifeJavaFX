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

