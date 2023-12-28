package sch.frog.kit.lang.parse.lexical;

public interface ITokenStream {

    Token current();

    Token peek();

    Token next();

}
