package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ObjectNode implements IExpression {

    private final List<ObjectEntry> entries = new ArrayList<>();

    public ObjectNode(Collection<ObjectEntry> entries) {
        this.entries.addAll(entries);
    }

    @Override
    public String literal() {
        return "{}";
    }

    @Override
    public List<IAstNode> getChildren() {
        return new ArrayList<>(entries);
    }

    public static class ObjectEntry implements Map.Entry<IdentifierNode, IExpression>, IAstNode{

        private final IdentifierNode key;
        private final IExpression value;

        public ObjectEntry(IdentifierNode key, IExpression value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public IdentifierNode getKey() {
            return key;
        }

        @Override
        public IExpression getValue() {
            return value;
        }

        @Override
        public IExpression setValue(IExpression value) {
            throw new RuntimeException(new IllegalAccessException("can't set value"));
        }

        @Override
        public String literal() {
            return ":";
        }

        @Override
        public List<IAstNode> getChildren() {
            return Arrays.asList(key, value);
        }
    }
}
