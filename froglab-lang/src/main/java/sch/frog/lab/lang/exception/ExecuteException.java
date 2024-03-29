package sch.frog.lab.lang.exception;

public class ExecuteException extends Exception {

    public static final int CODE_NULL_POINTER = 0;

    public static final int CODE_UNASSIGNABLE = 1;

    private final int exCode;

    public ExecuteException(String message) {
        super(message);
        this.exCode = -1;
    }

    public ExecuteException(int exCode, String message) {
        this.exCode = exCode;
    }
}
