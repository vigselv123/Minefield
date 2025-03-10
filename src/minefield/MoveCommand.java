package minefield;

//Command that moves player in given Heading.
public class MoveCommand extends Command {

    private Heading heading;

    public MoveCommand(Heading heading) {
        this.heading = heading;
    }

    @Override
    public void execute(MineFieldModel model) {
        if (model instanceof MineFieldModel) {
            MineFieldModel mf = (MineFieldModel) model;
            try {
                mf.move(heading);
            } catch (MineFieldException e) {
                // For a GUI, you might show a dialog or status label.
                System.out.println(e.getMessage());
            }
        }
    }
}
