package minefield;

import javax.swing.*;
import java.awt.*;

//simple controller panel that extends AppPanel.
//It adds directional buttons to left side, and uses AppPanel's menu for File/Edit operations.
public class MineFieldController extends AppPanel {

    public MineFieldController(AppFactory factory) {
        super(factory);
        layoutView();
    }

    //Creates panel of directional buttons on the west side and puts view in center
    protected void layoutView() {
        // Since the MineFieldView already creates directional buttons,
        // we need to wire them up to our controller's actions

        MineFieldView mineFieldView =  view;

        //Wire up directional buttons with action listeners
        mineFieldView.getButtonNW().addActionListener(e -> edit("moveNW"));
        mineFieldView.getButtonN().addActionListener(e -> edit("moveN"));
        mineFieldView.getButtonNE().addActionListener(e -> edit("moveNE"));
        mineFieldView.getButtonW().addActionListener(e -> edit("moveW"));
        mineFieldView.getButtonE().addActionListener(e -> edit("moveE"));
        mineFieldView.getButtonSW().addActionListener(e -> edit("moveSW"));
        mineFieldView.getButtonS().addActionListener(e -> edit("moveS"));
        mineFieldView.getButtonSE().addActionListener(e -> edit("moveSE"));

        //Wire up menu items
        mineFieldView.getMenuItemNew().addActionListener(e -> newFile());
        mineFieldView.getMenuItemSave().addActionListener(e -> saveFile());
        mineFieldView.getSaveAs().addActionListener(e -> saveFile());
        mineFieldView.getMenuItemOpen().addActionListener(e -> openFile());
        mineFieldView.getMenuItemQuit().addActionListener(e -> System.exit(0));

        //Wire up edit menu items
        mineFieldView.getEditNW().addActionListener(e -> edit("moveNW"));
        mineFieldView.getEditN().addActionListener(e -> edit("moveN"));
        mineFieldView.getEditNE().addActionListener(e -> edit("moveNE"));
        mineFieldView.getEditW().addActionListener(e -> edit("moveW"));
        mineFieldView.getEditE().addActionListener(e -> edit("moveE"));
        mineFieldView.getEditSW().addActionListener(e -> edit("moveSW"));
        mineFieldView.getEditS().addActionListener(e -> edit("moveS"));
        mineFieldView.getEditSE().addActionListener(e -> edit("moveSE"));
        mineFieldView.getAbout().addActionListener(e -> about());
        mineFieldView.getHelpItm().addActionListener(e -> help());

    }

    @Override
    public void setModel(MineFieldModel newModel) {
        model = newModel;
        if (view != null) {
            ((MineFieldView)view).setModel(newModel);
        }
    }
}