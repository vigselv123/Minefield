package minefield;

//simple custom exception to signal game-related errors, like moving off grid or stepping on a mine.
public class MineFieldException extends Exception {
    // Constructor that takes a message describing what went wrong.
    public MineFieldException(String message) {
        super(message);
    }
}