package minefield;

import javax.swing.*;
import java.awt.*;

public class MineFieldMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //Create the factory
            MineFieldFactory factory = new MineFieldFactory();

            //Create the controller
            MineFieldController controller = new MineFieldController(factory);

            //Get the model from the controller
            MineFieldModel model = controller.getModel();

            //Get the view from the controller
            MineFieldView view = controller.getView();

            //Register the view as a listener to the model
            model.addListener(view);

            //Create and configure the main frame
            JFrame frame = new JFrame(factory.getTitle());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setJMenuBar(controller.getMenuBar());
            frame.setContentPane(controller);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);

            //Update the view to reflect initial model state
            view.modelChanged();

            //Display the frame
            frame.setVisible(true);
        });
    }
}