package sch.frog.kit.core.common;

import sch.frog.kit.core.exception.WordDuplicateError;

import java.util.ArrayList;

public class SearchTree<D>{

    private Node<D> root = new Node<>();


    private static class Node<D>{
        private char ch;

        private Word<D> word;

        private final ArrayList<Node<D>> children = new ArrayList<>();

        private void add(String word, D data){
            int index = 0;
            ArrayList<Node<D>> cursorChildren = children;
            Node<D> targetNode = null;
            int len = word.length();
            while(true){
                char ch = word.charAt(index);
                for (Node<D> child : cursorChildren) {
                    if(child.ch == ch){
                        targetNode = child;
                        break;
                    }
                }
                if(targetNode == null){
                    targetNode = new Node<D>();
                    targetNode.ch = ch;
                    cursorChildren.add(targetNode);
                }
                cursorChildren = targetNode.children;
                index++;
                if(index == len){
                    if(targetNode.word != null){
                        throw new WordDuplicateError(word);
                    }
                    targetNode.word = new Word<>(word, data);
                    break;
                }
                targetNode = null;
            }
        }

        public Word<D> get(String literal){
            Word<D> word = match(0, literal);
            if(word != null && word.literal.equals(literal)){
                return word;
            }
            return null;
        }

        public Word<D> match(int start, String expression){
            if(start >= expression.length()){ return null; }
            char c = expression.charAt(start);
            Node<D> cursor = null;
            for (Node<D> n : children) {
                if(n.ch == c){
                    cursor = n;
                    break;
                }
            }
            if(cursor == null){
                return null;
            }else{
                Word<D> word = cursor.match(start + 1, expression);
                if(word == null){
                    return cursor.word;
                }else{
                    return word;
                }
            }
        }
    }

    private static class Word<D>{

        private final String literal;

        private final D data;

        public Word(String literal, D data) {
            this.literal = literal;
            this.data = data;
        }

        public int length(){
            return this.literal.length();
        }
    }

}
