package sch.frog.kit.lang.grammar;

import java.util.List;

public interface IAstNode {

    String literal();

    List<IAstNode> getChildren();

}
