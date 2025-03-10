package minefield;

public interface AppFactory {
    // Create new model.
    MineFieldModel makeModel();

    // Create new view, given model.
    MineFieldView makeView(MineFieldModel m);

    // Title for the application window.
    String getTitle();

    // Help messages for user.
    String[] getHelp();

    // About info.
    String about();

    // Create edit command based on "type" string.
    Command makeEditCommand(MineFieldModel model, String type);
}

