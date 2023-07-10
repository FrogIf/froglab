package sch.frog.kit.core.json;

import sch.frog.kit.core.util.BeanUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonObject implements JsonElement {

    private final LinkedHashMap<String, JsonValue<?>> kvMap = new LinkedHashMap<>();

    public static JsonObject load(Object obj) {
        JsonObject jsonObject = new JsonObject();
        if(obj instanceof Map){
            Map objMap = (Map) obj;
            Set<Map.Entry> set = objMap.entrySet();
            for (Map.Entry o : set) {
                Object key = o.getKey();
                jsonObject.put(key == null ? "null" : key.toString(), o.getValue());
            }
        }else{
            Map<String, Object> map = BeanUtils.getMap(obj);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        }
        return jsonObject;
    }

    void putJsonValue(String key, JsonValue<?> jsonValue){
        kvMap.put(key, jsonValue);
    }

    void putObject(String key, JsonObject jsonObject) {
        kvMap.put(key, new ObjectJsonValue(jsonObject));
    }

    void putArray(String key, JsonArray jsonArray) {
        kvMap.put(key, new ArrayJsonValue(jsonArray));
    }

    public Iterator<Map.Entry<String, JsonValue<?>>> getIterator() {
        Set<Map.Entry<String, JsonValue<?>>> entries = kvMap.entrySet();
        Iterator<Map.Entry<String, JsonValue<?>>> iterator = entries.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Map.Entry<String, JsonValue<?>> next() {
                Map.Entry<String, JsonValue<?>> entry = iterator.next();
                return new Map.Entry<>() {
                    @Override
                    public String getKey() {
                        return entry.getKey();
                    }

                    @Override
                    public JsonValue<?> getValue() {
                        return entry.getValue();
                    }

                    @Override
                    public JsonValue<?> setValue(JsonValue<?> value) {
                        throw new UnsupportedOperationException("setValue");
                    }
                };
            }
        };
    }

    @Override
    public String toCompressString() {
        CompactJsonWriter writer = new CompactJsonWriter();
        writer.writeObject(this);
        return writer.toJson();
    }

    @Override
    public String toPrettyString() {
        PrettyJsonWriter writer = new PrettyJsonWriter();
        writer.writeObject(this);
        return writer.toJson();
    }

    @Override
    public String toString(){
        return toCompressString();
    }

    public void put(String key, Object value) {
        this.putJsonValue(key, JsonValueConvertor.convert(value));
    }

    public Object get(String key){
        JsonValue<?> jsonValue = this.kvMap.get(key);
        return jsonValue == null ? null : jsonValue.getValue();
    }

    public List<String> keys(){
        Set<Map.Entry<String, JsonValue<?>>> entries = kvMap.entrySet();
        ArrayList<String> keyList = new ArrayList<>(entries.size());
        for (Map.Entry<String, JsonValue<?>> entry : entries) {
            keyList.add(entry.getKey());
        }
        return keyList;
    }

    public boolean containsKey(String key){
        return this.kvMap.containsKey(key);
    }
}
