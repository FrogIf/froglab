package sch.frog.kit.lang.parse.exception;

public class GrammarException extends Exception {

    private final int pos;

    private final String message;

    public GrammarException(int pos, String message) {
        this.pos = pos;
        this.message = message;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
