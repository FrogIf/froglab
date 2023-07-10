package sch.frog.kit.core.value;

public class Locator {

    private final String key;

    private final int index;

    private Locator next;

    public Locator(String str) {
        this.key = str;
        this.index = -1;
    }

    public Locator(int index){
        this.index = index;
        this.key = null;
    }

    public Locator(String str, Locator next){
        this(str);
        this.next = next;
    }

    public Locator(int index, Locator next){
        this(index);
        this.next = next;
    }

    public String getKey(){
        return this.key;
    }

    public int getIndex(){
        return this.index;
    }

    public boolean isIndex(){
        return this.index >= 0;
    }

    public Locator next(){
        return this.next;
    }

    @Override
    public String toString() {
        if(this.index >= 0){
            return "#" + this.index + (this.next == null ? "" : ("." + this.next));
        }else{
            return "@" + this.key + (this.next == null ? "" : ("." + this.next));
        }
    }

}
