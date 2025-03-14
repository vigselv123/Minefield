package minefield;

import java.io.File;
//Implements AppFactory for MineField game. Creates model, view, and commands.
public class MineFieldFactory implements AppFactory {

    @Override
    public MineFieldModel makeModel() {
        // Create 10x10 minefield by default
        return new MineFieldModel(10, 10);
    }

    @Override
    public MineFieldView makeView(MineFieldModel m) {
        // Cast to MineFieldModel so we can pass it to MineFieldView
        return new MineFieldView((MineFieldModel) m);
    }

    @Override
    public String getTitle() {
        return "Mine Field MVC";
    }

    @Override
    public String[] getHelp() {
        return new String[] {
                "Use directional buttons or Edit menu to move.",
                "Avoid mines. Reach bottom-right to win."
        };
    }

    @Override
    public String about() {
        return "Mine Field game using an MVC pattern.";
    }

    //Create commands for Edit menu, based on string "type".
    @Override
    public Command makeEditCommand(MineFieldModel model, String type) {
        if (type.startsWith("move")) {
            // parse heading from type
            switch (type) {
                case "moveN":  return new MoveCommand(Heading.N);
                case "moveNE": return new MoveCommand(Heading.NE);
                case "moveE":  return new MoveCommand(Heading.E);
                case "moveSE": return new MoveCommand(Heading.SE);
                case "moveS":  return new MoveCommand(Heading.S);
                case "moveSW": return new MoveCommand(Heading.SW);
                case "moveW":  return new MoveCommand(Heading.W);
                case "moveNW": return new MoveCommand(Heading.NW);
            }
        }
        return null; // unknown command
    }

    //Called by AppPanel's openFile() to load model from disk.
    public MineFieldModel open(File file) throws Exception {
        // Use static load method in MineFieldModel
        return MineFieldModel.load(file);
    }

    //Called by AppPanel's saveFile() to save current model.
    public void save(MineFieldModel m, File file) throws Exception {
        if (m instanceof MineFieldModel) {
            ((MineFieldModel) m).save(file);
        }
    }
}
