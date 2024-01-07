package sch.frog.kit.lang.parse.grammar0;

import java.util.List;

public interface IAstNode {

    String literal();

    List<IAstNode> getChildren();

}
