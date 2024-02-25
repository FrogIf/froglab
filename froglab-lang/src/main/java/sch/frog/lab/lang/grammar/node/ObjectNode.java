package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.VMap;
import sch.frog.lab.lang.value.VMapImpl;
import sch.frog.lab.lang.value.Value;

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

    @Override
    public Value evaluate(IExecuteContext context) throws ExecuteException {
        VMap map = new VMapImpl();
        for (ObjectEntry entry : entries) {
            String key = entry.getKey().key();
            Value value = entry.getValue().evaluate(context);
            map.put(key, value);
        }
        return Value.of(map);
    }

    public static class ObjectEntry implements Map.Entry<EntryKey, IExpression>, IAstNode{

        private final EntryKey key;
        private final IExpression value;

        public ObjectEntry(EntryKey key, IExpression value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public EntryKey getKey() {
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
