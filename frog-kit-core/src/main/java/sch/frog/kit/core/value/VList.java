package sch.frog.kit.core.value;

import java.util.ArrayList;

public class VList extends ArrayList<Value> {
    public static VList load(Iterable<?> obj) {
        VList list = new VList();
        for (Object o : obj) {
            list.add(Value.of(o));
        }
        return list;
    }
}
