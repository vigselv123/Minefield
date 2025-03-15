package minefield;

import javax.swing.*;
import java.io.File;

public class MineFieldController {


    private MineFieldModel model;
    private MineFieldView view;
    private MineFieldFactory factory;
    private File currentFile;


    public MineFieldController(MineFieldModel model, MineFieldView view, MineFieldFactory factory) {
        this.model = model;
        this.view = view;
        this.factory = factory;
        this.currentFile=null;


        //Action events for all the buttons
        view.getButtonN().addActionListener(e -> handleMove("moveN"));
        view.getButtonNE().addActionListener(e -> handleMove("moveNE"));
        view.getButtonE().addActionListener(e -> handleMove("moveE"));
        view.getButtonSE().addActionListener(e -> handleMove("moveSE"));
        view.getButtonS().addActionListener(e -> handleMove("moveS"));
        view.getButtonSW().addActionListener(e -> handleMove("moveSW"));
        view.getButtonW().addActionListener(e -> handleMove("moveW"));
        view.getButtonNW().addActionListener(e -> handleMove("moveNW"));

        view.getEditN().addActionListener(e -> handleMove("moveN"));
        view.getEditNE().addActionListener(e -> handleMove("moveNE"));
        view.getEditE().addActionListener(e -> handleMove("moveE"));
        view.getEditSE().addActionListener(e -> handleMove("moveSE"));
        view.getEditS().addActionListener(e -> handleMove("moveS"));
        view.getEditSW().addActionListener(e -> handleMove("moveSW"));
        view.getEditW().addActionListener(e -> handleMove("moveW"));
        view.getEditNW().addActionListener(e -> handleMove("moveNW"));

        view.getMenuItemNew().addActionListener(e -> doNew());
        view.getMenuItemOpen().addActionListener(e -> doOpen());
        view.getMenuItemSave().addActionListener(e -> doSave());
        view.getMenuItemQuit().addActionListener(e -> System.exit(0));

        view.getAbout().addActionListener(e -> doAbout());
        view.getHelpItm().addActionListener(e -> doHelp());
        view.getSaveAs().addActionListener(e -> doSaveAs());
    }


    private void doAbout() {
        //Get the about text
        String aboutText = factory.about();
        //Display text
        JOptionPane.showMessageDialog(view, aboutText, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void doHelp() {
        //Get the help text
        String[] helpItems = factory.getHelp();
        String helpText = String.join("\n", helpItems);
        //Display the help text
        JOptionPane.showMessageDialog(view, helpText, "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    //Handle player movement
    private void handleMove(String commandType) {
        Command cmd = factory.makeEditCommand(model, commandType);
        if (cmd != null) {
            cmd.execute(model);
        }
    }


    private void doNew() {
        //prompt user file is unsaved
        if (!unsaved()) {
            return;
        }

        //close old window
        java.awt.Window oldWindow = SwingUtilities.getWindowAncestor(view);
        if (oldWindow != null) {
            oldWindow.dispose();
        }

        //create new window
        MineFieldModel model = new MineFieldModel(20,20);
        MineFieldView view = new MineFieldView(model);
        model.addListener(view);
        MineFieldFactory factory = new MineFieldFactory();
        MineFieldController controller = new MineFieldController(model, view, factory);

        view.modelChanged();

    }


    private void doOpen() {
        //prompt user that file is unsaved
        if (!unsaved()) {
            return;
        }
        //Choose a file
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {

                MineFieldModel newModel = factory.open(file);
                newModel.addListener(view);

                this.model = newModel;

                this.currentFile = file;
                view.modelChanged();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        view,
                        "Open failed: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void doSaveAs() {
        //choose where to save
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showSaveDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                factory.save(model, file);
                model.setDirty(false);
                currentFile = file;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view,
                        "Save failed: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //save if saveAs has been run
    private void doSave() {
        if (currentFile == null) {
            doSaveAs();
        } else {
            try {
                factory.save(model, currentFile);
                model.setDirty(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view,
                        "Save failed: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //if file is not saved, warn user
    private boolean unsaved() {
        if (model.isDirty()) {
            int choice = JOptionPane.showConfirmDialog(view,
                    "You have unsaved changes. Start a new game?",
                    "Unsaved Changes", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        return true;
    }
}
