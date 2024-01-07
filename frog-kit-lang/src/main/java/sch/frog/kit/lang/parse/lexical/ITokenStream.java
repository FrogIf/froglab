package sch.frog.kit.lang.parse.lexical;

public interface ITokenStream {

    /**
     * 当读取到末尾, 则返回Token.EOF
     * @return not null
     */
    Token current();

    /**
     * 预读下一个
     * @return not null
     */
    Token peek();

    /**
     * 当读取到末尾, 则返回Token.EOF
     * @return not null
     */
    Token next();

}
