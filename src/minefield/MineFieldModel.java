package minefield;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;          
import java.io.FileWriter;    
import java.io.BufferedReader;
import java.io.FileReader;    
import java.io.IOException;   
import java.io.PrintWriter;   

//Model class holds state and logic of Mine Field game. Notifies listeners whenever its state changes.
public class MineFieldModel {
    // Percentage of cells will contain mines.
    public static final int PERCENT_MINED = 5;

    // Dimensions of grid 10x10.
    private int rows;
    private int cols;

    // 2D array indicating which cells have mines (true = mine present).
    private boolean[][] mines;

    // 2D array indicating which cells have been visited or revealed.
    private boolean[][] visited;

    // Player's current position in grid.
    private int playerRow;
    private int playerCol;

    // Flag indicating if game over (won or lost).
    private boolean gameOver;

    // List of listeners (usually views) want to be notified of changes.
    private List<MineFieldListener> listeners;

    //Flag to track unsaved changes in  model
    private boolean dirty; 

    //Constructor creates minefield with given number of rows and columns.
    public MineFieldModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.listeners = new ArrayList<>();
        initGame();
    }

    //Initializes or resets game state.
    private void initGame() {
        // Create 2D arrays.
        mines = new boolean[rows][cols];
        visited = new boolean[rows][cols];

        // Mark game as not over yet.
        gameOver = false;

        //When starting fresh game, it's not "dirty" yet
        dirty = false; 

        // Randomly place mines in grid based on PERCENT_MINED.
        Random rand = new Random();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // If random number < PERCENT_MINED, place mine.
                if (rand.nextInt(100) < PERCENT_MINED) {
                    mines[r][c] = true;
                } else {
                    mines[r][c] = false;
                }
                //no cell is visited.
                visited[r][c] = false;
            }
        }

        // Start player in top-left corner (0,0).
        playerRow = 0;
        playerCol = 0;
        visited[playerRow][playerCol] = true;  // Mark starting cell as visited.

        // Notify listeners model changed.
        notifyListeners();
    }

    //Adds listener will be notified when this model changes.
    public void addListener(MineFieldListener listener) {
        listeners.add(listener);
    }

    //Notifies all listeners model state has changed.
    private void notifyListeners() {
        for (MineFieldListener l : listeners) {
            l.modelChanged();
        }
    }

    //Moves player in given heading (direction).
    public void move(Heading heading) throws MineFieldException {
        // If game over, don't allow moves.
        if (gameOver) {
            throw new MineFieldException("Game has ended. No more moves allowed.");
        }


        // Compute new row and column based on heading.
        int newRow = playerRow;
        int newCol = playerCol;

        switch (heading) {
            case N:
                newRow -= 1;
                break;
            case NE:
                newRow -= 1;
                newCol += 1;
                break;
            case E:
                newCol += 1;
                break;
            case SE:
                newRow += 1;
                newCol += 1;
                break;
            case S:
                newRow += 1;
                break;
            case SW:
                newRow += 1;
                newCol -= 1;
                break;
            case W:
                newCol -= 1;
                break;
            case NW:
                newRow -= 1;
                newCol -= 1;
                break;
        }

        // Check if new position is still within grid boundaries.
        if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
            throw new MineFieldException("Cannot move off the grid.");
        }

        // Update player's position.
        playerRow = newRow;
        playerCol = newCol;

        // Mark new position as visited.
        visited[playerRow][playerCol] = true;

        // Check if there is mine at new position.
        if (mines[playerRow][playerCol]) {
            // Game over: player stepped on mine.
            gameOver = true;
            notifyListeners();
            throw new MineFieldException("You stepped on a mine.");
        }

        // Check if player reached bottom-right corner.
        if (playerRow == rows - 1 && playerCol == cols - 1) {
            // Game over: player reached goal.
            gameOver = true;
            notifyListeners();
            throw new MineFieldException("You win! You reached the goal.");
        }

        // If none of above conditions triggered, move is valid.

        //Mark model as "dirty"; new state that hasn't been saved
        dirty = true; 

        notifyListeners();
    }

    //Returns number of rows in grid.
    public int getRows() {
        return rows;
    }

    //Returns number of columns in grid.
    public int getCols() {
        return cols;
    }

    // Returns true if given cell is visited, false otherwise.
    public boolean isVisited(int r, int c) {
        return visited[r][c];
    }

    //Returns true if cell at (r, c) contains mine.
    public boolean isMined(int r, int c) {
        return mines[r][c];
    }

    //Returns player's current row.
    public int getPlayerRow() {
        return playerRow;
    }

    //Returns player's current column.
    public int getPlayerCol() {
        return playerCol;
    }

    //Returns true if game over, either by winning or losing.
    public boolean isGameOver() {
        return gameOver;
    }

    //Counts how many neighboring cells around (r, c) have mines.
    public int getNeighborMineCount(int r, int c) {
        int count = 0;
        // Check all 8 possible neighbors around (r,c).
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                // Skip (0,0) offset because that's cell itself.
                if (dr == 0 && dc == 0) continue;
                int rr = r + dr;
                int cc = c + dc;
                // Check if rr, cc is within grid.
                if (rr >= 0 && rr < rows && cc >= 0 && cc < cols) {
                    if (mines[rr][cc]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    //Returns true if model has unsaved changes.
    public boolean isDirty() { 
        return dirty;          
    }                          

    //Allows other classes to set or clear dirty flag.
    public void setDirty(boolean d) { 
        dirty = d;                  
    }                               

    //Save the model to a text file.
    public void save(File file) throws IOException { 
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) { 
            // Write rows, cols, playerRow, playerCol, gameOver
            pw.println(rows);     
            pw.println(cols);     
            pw.println(playerRow);
            pw.println(playerCol);
            pw.println(gameOver); 

            // Save mines grid
            for (int r = 0; r < rows; r++) {               
                for (int c = 0; c < cols; c++) {           
                    pw.print(mines[r][c] ? "1" : "0");     
                }
                pw.println();                              
            }

            // Save visited grid
            for (int r = 0; r < rows; r++) {               
                for (int c = 0; c < cols; c++) {           
                    pw.print(visited[r][c] ? "1" : "0");   
                }
                pw.println();                              
            }
        }
        // After successful save, it's no longer dirty
        dirty = false; 
    } 

    //Load MineFieldModel from text file.
    public static MineFieldModel load(File file) throws IOException { 
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { 
            int r = Integer.parseInt(br.readLine()); 
            int c = Integer.parseInt(br.readLine()); 
            MineFieldModel model = new MineFieldModel(r, c); 

            model.playerRow = Integer.parseInt(br.readLine()); 
            model.playerCol = Integer.parseInt(br.readLine()); 
            model.gameOver  = Boolean.parseBoolean(br.readLine()); 

            // Read mines
            for (int rr = 0; rr < r; rr++) {                    
                String line = br.readLine();                    
                for (int cc = 0; cc < c; cc++) {                
                    model.mines[rr][cc] = (line.charAt(cc) == '1'); 
                }
            }

            // Read visited
            for (int rr = 0; rr < r; rr++) {                    
                String line = br.readLine();                    
                for (int cc = 0; cc < c; cc++) {                
                    model.visited[rr][cc] = (line.charAt(cc) == '1'); 
                }
            }

            // After loading, it's not dirty
            model.dirty = false; 

            // Notify listeners (if any) that loaded new data
            model.notifyListeners(); 

            return model; 
        }
    } 
}
