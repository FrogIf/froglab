package sch.frog.lab.lang.value;

/**
 * 句柄
 */
public abstract class Handle implements VMap {

    @Override
    public Value put(String key, Value value) {
        throw new IllegalStateException("unsupported method");
    }

    public abstract String[] keys();

    @Override
    public Value keyList() {
        String[] keys = keys();
        VList list = new VListImpl();
        for (String key : keys) {
            list.add(Value.of(key));
        }
        return Value.of(list);
    }

}
