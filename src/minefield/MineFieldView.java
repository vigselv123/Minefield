package minefield;
import javax.swing.*;
import java.awt.*;

//Displays minefield grid. Implements MineFieldListener so it can update whenever model changes.
public class MineFieldView extends JPanel implements MineFieldListener {
    // reference to model so we can query its state.
    private MineFieldModel model;

    // 2D array of labels to visually represent cells.
    private JLabel[][] cells;

    //Sets up GUI components for displaying grid.
    public MineFieldView(MineFieldModel model) {
        this.model = model;

        // Set grid layout to display rows x cols labels.
        setLayout(new GridLayout(model.getRows(), model.getCols()));

        // Create 2D array of labels.
        cells = new JLabel[model.getRows()][model.getCols()];

        // Initialize each label and add it to panel.
        for (int r = 0; r < model.getRows(); r++) {
            for (int c = 0; c < model.getCols(); c++) {
                JLabel label = new JLabel("?", SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                cells[r][c] = label;
                add(label);
            }
        }
    }

    //method called whenever model state changes. Refresh display to show visited cells, neighbor counts.
    @Override
    public void modelChanged() {
        // Loop through all cells in grid.
        for (int r = 0; r < model.getRows(); r++) {
            for (int c = 0; c < model.getCols(); c++) {

                // If cell has been visited...
                if (model.isVisited(r, c)) {
                    // Check if player's current position.
                    if (r == model.getPlayerRow() && c == model.getPlayerCol()) {
                        // Highlight player's position in white (or another color).
                        cells[r][c].setOpaque(true);
                        cells[r][c].setBackground(Color.WHITE);

                        // Show how many mines are around player.
                        int neighborMines = model.getNeighborMineCount(r, c);
                        cells[r][c].setText(String.valueOf(neighborMines));

                    } else {
                        // visited path cell, not player's position.
                        cells[r][c].setOpaque(true);
                        cells[r][c].setBackground(Color.LIGHT_GRAY);

                        // Show neighbor mine count here as well.
                        int neighborMines = model.getNeighborMineCount(r, c);
                        cells[r][c].setText(String.valueOf(neighborMines));
                    }

                } else {
                    // If cell not visited, display question mark.
                    cells[r][c].setOpaque(true);
                    cells[r][c].setBackground(Color.DARK_GRAY);
                    cells[r][c].setText("?");
                }
            }
        }
        // Force panel to repaint itself with new data.
        repaint();
    }
    //Allows resetting the view when new model is set.
    public void setModel(MineFieldModel newModel) { 
        this.model = newModel; 
        removeAll(); 
        //Reset layout with new model dimensions.
        setLayout(new GridLayout(model.getRows(), model.getCols())); 
        cells = new JLabel[model.getRows()][model.getCols()]; 
        //Reinitialize each cell label.
        for (int r = 0; r < model.getRows(); r++) { 
            for (int c = 0; c < model.getCols(); c++) { 
                JLabel label = new JLabel("?", SwingConstants.CENTER); 
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY)); 
                cells[r][c] = label; 
                add(label); 
            } 
        } 
        revalidate(); 
        repaint(); 
    } 
}
