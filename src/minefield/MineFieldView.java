package minefield;
import javax.swing.*;
import java.awt.*;

//Displays minefield grid. Implements MineFieldListener so it can update whenever model changes.
public class MineFieldView extends JPanel implements MineFieldListener {
    // reference to model so we can query its state.
    private MineFieldModel model;

    // 2D array of labels to visually represent cells.
    private JLabel[][] cells;
    private JPanel buttons;
    private JPanel grid;
    //Sets up GUI components for displaying grid.
    public MineFieldView(MineFieldModel model) {
        this.model = model;
        JButton N, NW, NE, S, SW, SE, E, W;
        JMenuBar menuBar;
        JMenuItem newItm, save, saveAs, open, quit, about, helpItm, editN, editNW, editNE, editW, editS, editSE, editSW, editE;
        JMenu help, edit;
        // Set grid layout to display rows x cols labels.
        setLayout(new GridLayout(1,2));

        // Create 2D array of labels.
         grid = new JPanel(new GridLayout(model.getRows(), model.getCols()));
        cells = new JLabel[model.getRows()][model.getCols()];

        // Initialize each label and add it to panel.
        for (int r = 0; r < model.getRows(); r++) {
            for (int c = 0; c < model.getCols(); c++) {
                JLabel label = new JLabel("?", SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                cells[r][c] = label;
                grid.add(label);
            }
        }


        //button JPanel and add buttons to panel
        buttons = new JPanel();
        buttons.setLayout(null);

        NW = new JButton("NW");
        NW.setBounds(100, 10, 75, 30);
        buttons.add(NW);

        N = new JButton("N");
        N.setBounds(300, 10, 75, 30);
        buttons.add(N);

        NE = new JButton("NE");
        NE.setBounds(100, 130, 75, 30);
        buttons.add(NE);

        W = new JButton("W");
        W.setBounds(300, 130, 75, 30);
        buttons.add(W);

        E = new JButton("E");
        E.setBounds(100, 250, 75, 30);
        buttons.add(E);

        SW = new JButton("SW");
        SW.setBounds(300, 250, 75, 30);
        buttons.add(SW);

        S = new JButton("S");
        S.setBounds(100, 370, 75, 30);
        buttons.add(S);

        SE = new JButton("SE");
        SE.setBounds(300, 370, 75, 30);
        buttons.add(SE);

        menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        menuBar.add(file);

        newItm = new JMenuItem("New");
        file.add(newItm);

        save = new JMenuItem("Save");
        file.add(save);

        saveAs = new JMenuItem("SaveAs");
        file.add(saveAs);

        open = new JMenuItem("Open");
        file.add(open);

        quit = new JMenuItem("Quit");
        file.add(quit);

        edit = new JMenu("Edit");
        menuBar.add(edit);

        help = new JMenu("Help");
        menuBar.add(help);

        about = new JMenuItem("About");
        help.add(about);

        helpItm = new JMenuItem("Help");
        help.add(helpItm);

        editN = new JMenuItem("N");
        edit.add(editN);

        editNW = new JMenuItem("NW");
        edit.add(editNW);

        editNE = new JMenuItem("NE");
        edit.add(editNE);

        editW = new JMenuItem("W");
        edit.add(editW);

        editE = new JMenuItem("E");
        edit.add(editE);

        editS = new JMenuItem("S");
        edit.add(editS);

        editSW = new JMenuItem("SW");
        edit.add(editSW);

        editSE = new JMenuItem("SE");
        edit.add(editSE);



        //add grid and buttons
        add(buttons, BorderLayout.WEST);
        add(grid, BorderLayout.CENTER);

        //create and initialize frame
        JFrame frame = new JFrame("Mine Field Game");
        frame.setJMenuBar(menuBar);
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(this);    // 'this' is our MineFieldView panel
        frame.setVisible(true);


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
