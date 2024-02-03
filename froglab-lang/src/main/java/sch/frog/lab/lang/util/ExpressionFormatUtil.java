package sch.frog.lab.lang.util;

import sch.frog.lab.lang.lexical.Token;
import sch.frog.lab.lang.lexical.TokenType;

import java.util.ArrayList;
import java.util.List;

public class ExpressionFormatUtil {

    private final static String SPACE = " ";

    private final static String NEXT_LINE = "\n";

    private final static String TAB_SPACE = "    ";

    public static String pretty(List<Token> tokens) {
        if (tokens != null && !tokens.isEmpty()) {
            ExpressionBlock block = new ExpressionBlock("");
            block.type = -1;
            for (Token token : tokens) {
                if (!block.add(token)) {
                    throw new IllegalStateException("pretty failed");
                }
            }
            PrettyWriter writer = new PrettyWriter();
            writer.write(block);
            return writer.getString();
        }
        return "";
    }

    public static String compress(List<Token> tokens) {
        if (tokens != null && !tokens.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Token token : tokens) {
                if (token.type() == TokenType.COMMENT) {
                    String comment = token.literal();
                    StringBuilder c = new StringBuilder();
                    if (comment.startsWith("//")) {
                        c.append("/*").append(comment.substring(2)).append("*/");
                    } else if (comment.startsWith("/*")) {
                        for (int i = 0, len = comment.length(); i < len; i++) {
                            char ch = comment.charAt(i);
                            if (ch == '\n') {
                                c.append("\\n");
                            } else {
                                c.append(ch);
                            }
                        }
                    }
                    sb.append(c);
                } else {
                    sb.append(token.literal()).append(' ');
                }
            }
            return sb.toString();
        }
        return "";
    }

    private static class ExpressionBlock {
        private int type; // 1 - 括号, 0 - 其他, -1 - 根, 2 - 注释
        private final String literal;

        private boolean noBracketChild = true;

        public ExpressionBlock(String literal) {
            this.literal = literal;
            if ("(".equals(literal) || "{".equals(literal)) {
                type = 1;
            }else if(literal.startsWith("//") || literal.startsWith("/*")){
                type = 2;
            }
        }

        // 子级(只有type = 1时才存在)
        private final ArrayList<ExpressionBlock> children = new ArrayList<>();

        // 上一个
        private ExpressionBlock pre = null;

        // 下一个
        private ExpressionBlock next = null;

        private boolean closed = false;

        public boolean add(Token token) {
            if (closed) {
                return false;
            }
            if (type == 0 || type == 2) {
                return false;
            } else {
                int count = children.size();
                String t = token.literal();
                ExpressionBlock tail = null;
                if (count != 0) {
                    tail = children.get(count - 1);
                    if (tail.add(token)) {
                        return true;
                    }
                }
                if (
                        type != -1 &&
                                (
                                        ("(".equals(literal) && ")".equals(token.literal()))
                                                || ("{".equals(literal) && "}".equals(token.literal()))
                                )
                ) {
                    closed = true;
                } else {
                    ExpressionBlock b = new ExpressionBlock(t);
                    b.pre = tail;
                    if(tail != null){ tail.next = b; }
                    noBracketChild = noBracketChild && b.type != 1;
                    children.add(b);
                }
                return true;
            }
        }

    }

    private static class PrettyWriter {
        private int tab;

        private final StringBuilder sb = new StringBuilder();

        public void write(ExpressionBlock block) {
            if(block.type == 1){ // 括号
                char closeChar = ' ';
                switch (block.literal) {
                    case "(":
                        closeChar = ')';
                        break;
                    case "{":
                        closeChar = '}';
                        break;
                }
                if (block.children.isEmpty()) {
                    sb.append(block.literal).append(closeChar);
                } else {
                    if(block.noBracketChild){
                        sb.append(block.literal);
                        for (ExpressionBlock child : block.children) {
                            write(child);
                        }
                        sb.append(closeChar);
                    }else{
                        boolean lambdaDeclare = block.next != null && "=>".equals(block.next.literal);
                        tab++;
                        int tt = tab;
                        if(lambdaDeclare){
                            sb.append(block.literal);
                        }else{
                            sb.append(block.literal).append(NEXT_LINE).append(TAB_SPACE.repeat(tt));
                        }
                        for (ExpressionBlock innerBlock : block.children) {
                            write(innerBlock);
                        }
                        tab = tt - 1;

                        if(lambdaDeclare){
                            sb.append(closeChar);
                        }else{
                            sb.append(NEXT_LINE).append(TAB_SPACE.repeat(tab)).append(closeChar);
                        }
                    }
                }
            }else if(block.type == 0){
                sb.append(block.literal);
                if (",".equals(block.literal)) {
                    if(
                            (block.pre != null && block.pre.type == 1 && !block.pre.noBracketChild)
                            || (block.next != null && block.next.next != null && block.next.next.type == 1 && !block.next.next.noBracketChild)
                    ){
                        sb.append('\n').append(TAB_SPACE.repeat(tab));
                    }else{
                        sb.append(' ');
                    }
                }
                for (ExpressionBlock innerBlock : block.children) {
                    write(innerBlock);
                }
            }else if(block.type == 2){ // 注释
                String repeat = TAB_SPACE.repeat(tab);
                int index = sb.lastIndexOf("\n");
                index = index == -1 ? 0 : index;
                if(!sb.substring(index).isBlank()){
                    sb.append('\n').append(repeat);
                }
                String[] lines = block.literal.split("\n");
                int start = lines[0].indexOf('/');
                for (String line : lines) {
                    sb.append(StringUtils.trimLeft(line, start)).append('\n').append(repeat);
                }
            }else{
                for (ExpressionBlock innerBlock : block.children) {
                    write(innerBlock);
                }
            }
        }

        public String getString() {
            return sb.toString();
        }

    }


}
