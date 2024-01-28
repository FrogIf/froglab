package sch.frog.kit.lang.value;

import java.util.ArrayList;

public class VListImpl extends ArrayList<Value> implements VList {

    public static VList load(Iterable<?> obj) {
        VList list = new VListImpl();
        for (Object o : obj) {
            list.add(Value.of(o));
        }
        return list;
    }

}
