package sch.frog.kit.core.util;

import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.parse.lexical.TokenType;
import sch.frog.kit.core.parse.lexical.TokenUtil;

import java.util.ArrayList;
import java.util.List;

public class ExpressionFormatUtil {

    private final static String SPACE = " ";

    private final static String NEXT_LINE = "\n";

    private final static String TAB_SPACE = "    ";

    public static String pretty(List<Token> tokens){
        if(tokens != null && !tokens.isEmpty()){
            List<StringBuilder> lines = new ArrayList<>();
            Token[] tokenArr = new Token[tokens.size()];
            int i = 0;
            for (Token token : tokens) {
                tokenArr[i] = token;
                i++;
            }

            int tab = 0;
            StringBuilder sb = new StringBuilder();
            lines.add(sb);
            int len = tokenArr.length;
            boolean newLine = false;
            for(i = 0; i < len; i++){
                Token token = tokenArr[i];
                if(i != 0){
                    if(",".equals(tokenArr[i - 1].literal())){
                        sb.append(' ');
                    }
                }
                if(TokenUtil.isConstant(token.type())){
                    sb.append(token.literal());
                }else if(token.type() == TokenType.IDENTIFIER){
                    if(i < len - 1){
                        if("(".equals(tokenArr[i + 1].literal())){
                            sb = new StringBuilder();
                            lines.add(sb);
                            newLine = true;
                            tab++;
                            sb.append(TAB_SPACE.repeat(tab));
                        }
                        sb.append(token.literal());
                    }
                }else if(token.type() == TokenType.COMMENT){

                }else if(token.type() == TokenType.STRUCT){
                    if(")".equals(token.literal())){
                        if(newLine){
                            tab--;
                            if(tab < 0){ tab = 0; }
                        }
                    }
                }else{

                }
            }
            return sb.toString();
        }
        return "";
    }

    public static String compress(List<Token> tokens){
        if(tokens != null && !tokens.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for (Token token : tokens) {
                if(token.type() == TokenType.COMMENT){
                    String comment = token.literal();
                    StringBuilder c = new StringBuilder();
                    if(comment.startsWith("//")){
                        c.append("/*").append(comment.substring(2)).append("*/");
                    }else if(comment.startsWith("/*")){
                        for(int i = 0, len = comment.length(); i < len; i++){
                            char ch = comment.charAt(i);
                            if(ch == '\n'){
                                c.append("\\n");
                            }else{
                                c.append(ch);
                            }
                        }
                    }
                    sb.append(c);
                }else{
                    sb.append(token.literal());
                }
            }
            return sb.toString();
        }
        return "";
    }

    private static class ExpressionBlock{
        private int type; // 1 - 括号, 0 - 其他, -1 - 根
        private String literal;

        public ExpressionBlock(String literal) {
            this.literal = literal;
            if("(".equals(literal)){
               type = 1;
            }
        }

        private final ArrayList<ExpressionBlock> innerBlocks = new ArrayList<>();
        private boolean closed = false;
        public boolean add(Token token){
            if(closed){ return false; }
            int count = innerBlocks.size();
            String t = token.literal();
            if (count != 0) {
                ExpressionBlock block = innerBlocks.get(count - 1);
                if (block.add(token)) {
                    return true;
                }
            }
            if(")".equals(token.literal())){
                closed = true;
            }else{
                innerBlocks.add(new ExpressionBlock(t));
            }
            return true;
        }

        public void write(PrettyContext context){

        }
    }

    private static class PrettyContext {
        private int tab;

    }


}
