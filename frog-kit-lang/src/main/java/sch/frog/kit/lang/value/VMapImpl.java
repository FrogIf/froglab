package sch.frog.kit.lang.value;

import sch.frog.kit.lang.util.BeanUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VMapImpl extends HashMap<String, Value> implements VMap {

    public static VMap load(Object obj) {
        VMap result = new VMapImpl();
        if(obj instanceof Map){
            Map objMap = (Map) obj;
            Set<Entry> set = objMap.entrySet();
            for (Map.Entry o : set) {
                Object key = o.getKey();
                result.put(key == null ? "null" : key.toString(), Value.of(o.getValue()));
            }
        }else{
            Map<String, Object> map = BeanUtils.getMap(obj);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                result.put(entry.getKey(), Value.of(entry.getValue()));
            }
        }
        return result;
    }

    @Override
    public Value get(String key) {
        return super.get(key);
    }

}
