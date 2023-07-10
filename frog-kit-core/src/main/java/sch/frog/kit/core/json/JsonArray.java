package sch.frog.kit.core.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonArray implements JsonElement, Iterable<Object> {

    private final ArrayList<JsonValue<?>> list = new ArrayList<>();

    public static JsonArray load(Iterable<?> obj) {
        JsonArray jsonArray = new JsonArray();
        for(Object o : obj){
            jsonArray.add(o);
        }
        return jsonArray;
    }

    void addJsonValue(JsonValue<?> jsonValue){
        list.add(jsonValue);
    }

    void addObject(JsonObject jsonObject)
    {
        list.add(new ObjectJsonValue(jsonObject));
    }

    void addArray(JsonArray jsonArray)
    {
        list.add(new ArrayJsonValue(jsonArray));
    }

    public Iterator<JsonValue<?>> getIterator(){
        Iterator<JsonValue<?>> iterator = list.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonValue<?> next() {
                return iterator.next();
            }
        };
    }

    @Override
    public String toCompressString() {
        CompactJsonWriter writer = new CompactJsonWriter();
        writer.writeArray(this);
        return writer.toJson();
    }

    @Override
    public String toPrettyString() {
        PrettyJsonWriter writer = new PrettyJsonWriter();
        writer.writeArray(this);
        return writer.toJson();
    }

    @Override
    public String toString(){
        return toCompressString();
    }

    public void add(Object value) {
        this.addJsonValue(JsonValueConvertor.convert(value));
    }

    public Object get(int index){
        return list.get(index).getValue();
    }

    public int size(){ return list.size(); }

    @Override
    public Iterator<Object> iterator() {
        return new JsonArrayIterator(list);
    }

    public void set(int index, Object val) {
        list.set(index, JsonValueConvertor.convert(val));
    }

    private static class JsonArrayIterator implements Iterator<Object>{

        private final Iterator<JsonValue<?>> iterator;

        public JsonArrayIterator(List<JsonValue<?>> list) {
            iterator = list.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Object next() {
            JsonValue<?> next = iterator.next();
            return next == null ? null : next.getValue();
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }
}
