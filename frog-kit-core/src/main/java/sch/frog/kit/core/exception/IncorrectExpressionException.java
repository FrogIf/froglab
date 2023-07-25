package sch.frog.kit.core.exception;

/**
 * 不正确的表达式异常
 */
public class IncorrectExpressionException extends Exception{

    private int pos;

    public IncorrectExpressionException(String message, int pos){
        super(message);
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
