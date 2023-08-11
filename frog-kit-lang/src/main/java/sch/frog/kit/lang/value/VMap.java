package sch.frog.kit.lang.value;

import sch.frog.kit.lang.util.BeanUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VMap extends HashMap<String, Value> {
    public static VMap load(Object obj) {
        VMap result = new VMap();
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
}
