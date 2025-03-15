package minefield;

import javax.swing.*;

public class MineFieldMain {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            //Create the model
            MineFieldModel model = new MineFieldModel(20,20);

            //Create the view, passing the model.
            MineFieldView view = new MineFieldView(model);

            //Register the view as a listener to the model.
            model.addListener(view);
            MineFieldFactory factory = new MineFieldFactory();
            MineFieldController controller = new MineFieldController(model, view, factory);

            view.modelChanged();
        });
    }
}

