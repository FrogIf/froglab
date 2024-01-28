package sch.frog.lab.lang.grammar.util;

import sch.frog.lab.lang.grammar.IAstNode;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AstUtil {

    /**
     * 将树转为可视化字符串形式输出
     * @param node 树根
     * @return 字符串
     */
    public static String generateTree(IAstNode node){
        StringBuilder sb = new StringBuilder();
        sb.append(toString(node)).append('\n');
        buildTree(node, sb, 0, new int[0]);
        return sb.toString();
    }

    /**
     * @param parent 父节点
     * @param sb stringBuilder
     * @param level 树层级
     * @param markArr 每一个int标记该层级是否有后继兄弟节点: 0 - 没有; 1 - 有
     */
    private static void buildTree(IAstNode parent, StringBuilder sb, int level, int[] markArr){
        List<IAstNode> children = parent.getChildren();
        if(children == null || children.isEmpty()){ return; }
        Iterator<IAstNode> itr = children.iterator();
        int[] arr = Arrays.copyOf(markArr, level + 1);
        while(itr.hasNext()){
            IAstNode child = itr.next();
            boolean hn = itr.hasNext(); // 是否有后继兄弟节点
            for(int i = 0; i <= level; i++){
                if(i == level){
                    if(hn){
                        sb.append("+- ");
                    }else{
                        sb.append("\\- ");
                    }
                }else{
                    if(arr[i] == 0){
                        sb.append("   ");
                    }else{
                        sb.append("|  ");
                    }
                }
            }
            if(child == null){
                sb.append("??null").append('\n');
            }else{
                sb.append(toString(child)).append('\n');
                arr[level] = hn ? 1 : 0;
                buildTree(child, sb, level + 1, arr);
            }
        }
    }

    private static String toString(IAstNode node){
        return node.literal();
    }

}
