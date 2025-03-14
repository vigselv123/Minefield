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
        
    }
}
