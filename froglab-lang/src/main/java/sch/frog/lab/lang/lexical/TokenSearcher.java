package sch.frog.lab.lang.lexical;

import sch.frog.lab.lang.io.IScriptStream;
import sch.frog.lab.lang.io.StringScriptStream;

import java.util.ArrayList;

public class TokenSearcher<V>{

    private final Node<V> root = new Node<>();

    public void put(String key, V value){
        root.add(key, value);
    }

    public V get(String key){
        Entry<V> entry = root.get(key);
        if(entry != null){
            return entry.value;
        }
        return null;
    }

    public Entry<V> match(IScriptStream inputStream){
        return root.match(inputStream);
    }


    private static class Node<V>{
        private char ch;

        private Entry<V> entry;

        private final ArrayList<Node<V>> children = new ArrayList<>();

        private void add(String key, V data){
            int index = 0;
            ArrayList<Node<V>> cursorChildren = children;
            Node<V> targetNode = null;
            int len = key.length();
            while(true){
                char ch = key.charAt(index);
                for (Node<V> child : cursorChildren) {
                    if(child.ch == ch){
                        targetNode = child;
                        break;
                    }
                }
                if(targetNode == null){
                    targetNode = new Node<V>();
                    targetNode.ch = ch;
                    cursorChildren.add(targetNode);
                }
                cursorChildren = targetNode.children;
                index++;
                if(index == len){
                    targetNode.entry = new Entry<>(key, data);
                    break;
                }
                targetNode = null;
            }
        }

        public Entry<V> get(String literal){
            Entry<V> entry = match(new StringScriptStream(literal));
            if(entry != null && entry.key.equals(literal)){
                return entry;
            }
            return null;
        }

        public Entry<V> match(IScriptStream inputStream){
            char c = inputStream.current();
            Node<V> cursor = null;
            for (Node<V> n : children) {
                if(n.ch == c){
                    cursor = n;
                    break;
                }
            }
            if(cursor == null){
                return null;
            }else{
                if(inputStream.next() == IScriptStream.EOF){
                    return cursor.entry;
                }
                Entry<V> entry = cursor.match(inputStream);
                if(entry == null){
                    return cursor.entry;
                }else{
                    return entry;
                }
            }
        }
    }

    public static class Entry<V>{

        private final String key;

        private final V value;

        public Entry(String key, V value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

}
