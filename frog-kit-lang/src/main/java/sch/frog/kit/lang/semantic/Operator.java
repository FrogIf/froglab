package sch.frog.kit.lang.semantic;

import io.github.frogif.calculator.number.impl.RationalNumber;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.grammar.IExpression;
import sch.frog.kit.lang.grammar.node.IdentifierNode;
import sch.frog.kit.lang.lexical.TokenConstant;
import sch.frog.kit.lang.value.VList;
import sch.frog.kit.lang.value.VMap;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

public class Operator {

    public static Value prefixEvaluate(String prefix, IExpression right, IExecuteContext context) throws ExecuteException {
        Value val = null;
        switch (prefix){
            case TokenConstant.MINUS:
                val = right.evaluate(context);
                RationalNumber num = val.cast(RationalNumber.class);
                return Value.of(num.not());
            case TokenConstant.PLUS:
                val = right.evaluate(context);
                if(val.getType() != ValueType.NUMBER){
                    throw new ExecuteException("prefix '+' only use for number, but : " + val.getType());
                }
                return val;
            case TokenConstant.BANG:
                val = right.evaluate(context);
                boolean b = val.cast(boolean.class);
                return Value.of(!b);
            case TokenConstant.REFERENCE:
                // TODO reference
        }
        throw new ExecuteException("unsupported prefix : " + prefix);
    }

    public static Value infixEvaluate(IExpression left, String infix, IExpression right, IExecuteContext context) throws ExecuteException {
        if(TokenConstant.ASSIGN.equals(infix)){
            if(!(left instanceof IdentifierNode)){
                throw new ExecuteException("assign left must identifier");
            }
            Value val = right.evaluate(context);
            context.setVariable(((IdentifierNode) left).identifier(), val);
            return val;
        }else{
            switch (infix){
                case TokenConstant.OR:
                    return Value.of(left.evaluate(context).cast(boolean.class) | right.evaluate(context).cast(boolean.class));
                case TokenConstant.AND:
                    return Value.of(left.evaluate(context).cast(boolean.class) & right.evaluate(context).cast(boolean.class));
                case TokenConstant.SHORT_CIRCLE_AND:
                    return Value.of(left.evaluate(context).cast(boolean.class) && right.evaluate(context).cast(boolean.class));
                case TokenConstant.SHORT_CIRCLE_OR:
                    return Value.of(left.evaluate(context).cast(boolean.class) || right.evaluate(context).cast(boolean.class));
                case TokenConstant.EQ:
                    return Value.of(equals(left, right, context));
                case TokenConstant.NOT_EQ:
                    return Value.of(!equals(left, right, context));
                case TokenConstant.GT:
                    return Value.of(compare(left, right, context) > 0);
                case TokenConstant.GTE:
                    return Value.of(compare(left, right, context) >= 0);
                case TokenConstant.LT:
                    return Value.of(compare(left, right, context) < 0);
                case TokenConstant.LTE:
                    return Value.of(compare(left, right, context) <= 0);
                case TokenConstant.PLUS:
                    return baseOperation(left, right, context, RationalNumber::add);
                case TokenConstant.MINUS:
                    return baseOperation(left, right, context, RationalNumber::sub);
                case TokenConstant.STAR:
                    return baseOperation(left, right, context, RationalNumber::mult);
                case TokenConstant.SLASH:
                    return baseOperation(left, right, context, RationalNumber::div);
            }
        }
        throw new ExecuteException("unsupported infix for : " + infix);
    }

    private static Value baseOperation(IExpression left, IExpression right, IExecuteContext context, Operation operation) throws ExecuteException {
        Value leftV = left.evaluate(context);
        Value rightV = right.evaluate(context);
        if (leftV.getType() != ValueType.NUMBER || rightV.getType() != ValueType.NUMBER){
            throw new ExecuteException("unsupported compare for " + leftV.getType() + " and " + rightV.getType());
        }
        return Value.of(operation.operate(leftV.cast(RationalNumber.class), rightV.cast(RationalNumber.class)));
    }

    private static int compare(IExpression left, IExpression right, IExecuteContext context) throws ExecuteException {
        Value leftV = left.evaluate(context);
        Value rightV = right.evaluate(context);
        if (leftV.getType() != ValueType.NUMBER || rightV.getType() != ValueType.NUMBER){
            throw new ExecuteException("unsupported compare for " + leftV.getType() + " and " + rightV.getType());
        }
        return leftV.cast(RationalNumber.class).compareTo(rightV.cast(RationalNumber.class));
    }

    private static boolean equals(IExpression left, IExpression right, IExecuteContext context) throws ExecuteException {
        Value leftV = left.evaluate(context);
        Value rightV = right.evaluate(context);
        if(leftV.getType() != rightV.getType()){ return false; }
        ValueType type = leftV.getType();
        switch (type){
            case NULL: return true;
            case VOID: throw new ExecuteException("unsupported for void");
            case BOOL: return leftV.cast(boolean.class).equals(rightV.cast(boolean.class));
            case NUMBER: return leftV.cast(RationalNumber.class).equals(rightV.cast(RationalNumber.class));
            case STRING: return leftV.cast(String.class).equals(rightV.cast(String.class));
            case OBJECT: return leftV.cast(VMap.class).equals(rightV.cast(VMap.class));
            case LIST: return leftV.cast(VList.class).equals(rightV.cast(VList.class));
            case SYMBOL: throw new ExecuteException("unsupported");
            case FUNCTION: return leftV.cast(IFunction.class).equals(rightV.cast(IFunction.class));
        }
        throw new ExecuteException("unsupported");
    }

    private interface Operation{
        RationalNumber operate(RationalNumber l, RationalNumber r);
    }

}
