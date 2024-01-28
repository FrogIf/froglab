package sch.frog.lab.win.editor;

public class HistoryChain {

    private HistoryNode first = null;

    private HistoryNode tail = null;

    private int count = 0;

    private final int maxCount;

    private HistoryNode cursor = null;

    public HistoryChain(int maxCount) {
        this.maxCount = maxCount;
    }

    public void add(String value){
        HistoryNode node = new HistoryNode(value);
        if(first == null){
            first = node;
        }else{
            if(tail.value.equals(value)){   // 去除重复命令
                return;
            }
            tail.next = node;
            node.pre = tail;
        }
        tail = node;
        count++;
        if(count > maxCount){
            removeFirst();
        }
    }

    private void removeFirst(){
        if(first != null){
            first = first.next;
            if(first != null){ first.pre = null; }
            count--;
        }
    }

    public String up(){
        if(cursor == null){
            cursor = tail;
        }else{
            if(cursor.pre != null){
                cursor = cursor.pre;
            }
        }
        if(cursor != null){
            return cursor.value;
        }else{
            return null;
        }
    }

    public String down(){
        if(cursor == null){
            return null;
        }
        cursor = cursor.next;
        if(cursor != null){
            return cursor.value;
        }else{
            return null;
        }
    }

    public void reset(){
        cursor = null;
    }

    private static class HistoryNode{

        private final String value;

        public HistoryNode(String value) {
            this.value = value;
        }

        private HistoryNode pre;

        private HistoryNode next;

        public String getValue() {
            return value;
        }

    }

}
