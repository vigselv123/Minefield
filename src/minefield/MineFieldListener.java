package minefield;

public interface MineFieldListener {
    //Called by model whenever model state changes, so views can refresh display.
    void modelChanged();
}
