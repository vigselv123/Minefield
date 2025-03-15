package minefield;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class AppPanel extends JPanel {

    protected MineFieldModel model;        // Current model
    protected MineFieldView view;          // Current view
    protected AppFactory factory; // Factory that builds model, view, commands
    protected JMenuBar menuBar;
    private File currentFile;// Main menu bar

    public AppPanel(AppFactory factory) {
        super();
        this.factory = factory;
        this.model = factory.makeModel();
        this.view = factory.makeView(model);

        // Layout: put view in center
        setLayout(new BorderLayout());
        add(view, BorderLayout.CENTER);

        // Create menu bar
        menuBar = createMenuBar();
    }

    //Creates basic menu bar with File and Edit menus
    protected JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> newFile());
        fileMenu.add(newItem);

        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(e -> openFile());
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> saveFile());
        fileMenu.add(saveItem);

        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.addActionListener(e ->saveAsFile());
        fileMenu.add(saveAsItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        bar.add(fileMenu);

        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem moveN = new JMenuItem("Move North");
        moveN.addActionListener(e -> edit("moveN"));
        editMenu.add(moveN);

        JMenu helpMenu = new JMenu("Help");

        JMenuItem help = new JMenuItem("Help");
        help.addActionListener(e ->help());
        helpMenu.add(help);

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> about());
        helpMenu.add(about);

        JMenuItem moveNE = new JMenuItem("Move NE");
        moveNE.addActionListener(e -> edit("moveNE"));
        editMenu.add(moveNE);

        JMenuItem moveE = new JMenuItem("Move East");
        moveE.addActionListener(e -> edit("moveE"));
        editMenu.add(moveE);

        JMenuItem moveSE = new JMenuItem("Move SE");
        moveSE.addActionListener(e -> edit("moveSE"));
        editMenu.add(moveSE);

        JMenuItem moveS = new JMenuItem("Move South");
        moveS.addActionListener(e -> edit("moveS"));
        editMenu.add(moveS);

        JMenuItem moveSW = new JMenuItem("Move SW");
        moveSW.addActionListener(e -> edit("moveSW"));
        editMenu.add(moveSW);

        JMenuItem moveW = new JMenuItem("Move West");
        moveW.addActionListener(e -> edit("moveW"));
        editMenu.add(moveW);

        JMenuItem moveNW = new JMenuItem("Move NW");
        moveNW.addActionListener(e -> edit("moveNW"));
        editMenu.add(moveNW);

        bar.add(editMenu);
        bar.add(helpMenu);

        return bar;
    }

    protected void help() {
        String[] helpItems = factory.getHelp();
        String helpText = String.join("\n", helpItems);
        JOptionPane.showMessageDialog(
                view,
                helpText,
                "Help",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    protected void about() {
        String aboutText = factory.about();
        JOptionPane.showMessageDialog(
                view,
                aboutText,
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    //Called when user selects "New" from File menu.
    protected void newFile() {
        if (confirmDiscardChanges()) {
            MineFieldModel newModel = factory.makeModel();
            setModel(newModel);
        }
    }

    //Called when user selects "Open" from File menu.
    protected void openFile() {
        if (confirmDiscardChanges()) {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    // Use factory to load model
                    MineFieldModel newModel = ((MineFieldFactory) factory).open(file);
                    setModel(newModel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Open failed: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    //Called when user selects "Save" from File menu.
    protected void saveFile() {

        if (currentFile == null) {
            saveAsFile();
        } else {
            try {
                factory.save(model, currentFile);
                model.setDirty(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        view,
                        "Save failed: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    protected void saveAsFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showSaveDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                factory.save(model, file);
                model.setDirty(false);
                this.currentFile = file;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        view,
                        "Save failed: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }


    //Called when user selects an item from Edit menu.
    protected void edit(String type) {
        Command cmd = factory.makeEditCommand(model, type);
        if (cmd != null) {
            cmd.execute(model);
        }
    }

    //Check if model is "dirty" (has unsaved changes). If yes, confirm user wants to discard
    protected boolean confirmDiscardChanges() {
        if (model instanceof MineFieldModel) {
            MineFieldModel mf = (MineFieldModel) model;
            if (mf.isDirty()) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "You have unsaved changes. Save them first?",
                        "Unsaved Changes",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // user wants to save
                    saveFile();
                    // If still dirty, user canceled or error happened
                    if (mf.isDirty()) {
                        return false;
                    }
                } else if (choice == JOptionPane.CANCEL_OPTION) {
                    return false;
                }
            }
        }
        return true;
    }

    //Replace current model with new one, update view accordingly.
    public void setModel(MineFieldModel newModel) {
        model = newModel;
        remove(view);
        view = factory.makeView(model);
        add(view, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    //Return current model.
    public MineFieldModel getModel() {
        return model;
    }

    //Return menu bar so main frame can set it.
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public MineFieldView getView() {
        return view;
    }
}
