package org.bau.parser.stmt;

import java.util.ArrayList;
import java.util.HashSet;

import org.bau.parser.DataType;
import org.bau.parser.FunctionContext;
import org.bau.parser.MemoryType;
import org.bau.parser.Program;
import org.bau.parser.ProgramContext;
import org.bau.parser.expr.Expression;
import org.bau.parser.expr.Variable;
import org.bau.runtime.Memory;

public class Switch implements Statement {

    public Expression expression;
    public ArrayList<ArrayList<Expression>> cases = new ArrayList<>();
    public ArrayList<ArrayList<Statement>> lists = new ArrayList<>();
    public ArrayList<Statement> elseList = new ArrayList<>();

    @Override
    public Statement replace(Variable old, Expression with) {
        Switch s = new Switch();
        s.expression = expression.replace(old, with);
        for (int i = 0; i < cases.size(); i++) {
            ArrayList<Expression> l = cases.get(i);
            ArrayList<Expression> l2 = new ArrayList<>();
            for (int j = 0; j < l.size(); j++) {
                l2.add(l.get(j).replace(old, with));
            }
            s.cases.add(l2);
        }
        for (int i = 0; i < lists.size(); i++) {
            ArrayList<Statement> l = lists.get(i);
            ArrayList<Statement> l2 = new ArrayList<>();
            for (int j = 0; j < l.size(); j++) {
                l2.add(l.get(j).replace(old, with));
            }
            s.lists.add(l2);
        }
        for (int j = 0; j < elseList.size(); j++) {
            s.elseList.add(elseList.get(j).replace(old, with));
        }
        return s;
    }

    @Override
    public StatementResult run(Memory m) {
        throw new IllegalStateException();
    }

    @Override
    public void optimize(ProgramContext context) {
        throw new IllegalStateException();
    }

    @Override
    public String toC() {
        throw new IllegalStateException();
    }

    @Override
    public String format() {
        StringBuilder buff = new StringBuilder();
        buff.append("switch ");
        buff.append(expression.format()).append("\n");
        for (int i = 0; i < cases.size(); i++) {
            buff.append("case ");
            ArrayList<Expression> l = cases.get(i);
            for (int j = 0; j < l.size(); j++) {
                if (j > 0) {
                    buff.append(", ");
                }
                buff.append(l.get(j).format());
            }
            buff.append("\n");
            ArrayList<Statement> ls = lists.get(i);
            for (int j = 0; j < ls.size(); j++) {
                buff.append(Statement.indent(ls.get(j).format()));
            }
        }
        if (elseList != null && !elseList.isEmpty()) {
            buff.append("else\n");
            for (Statement s : elseList) {
                buff.append(Statement.indent(s.format()));
            }
        }
        return buff.toString();
    }

    @Override
    public void collectTypes(HashSet<DataType> set, MemoryType memoryType) {
        throw new IllegalStateException();
    }

    @Override
    public void used(Program program) {
        throw new IllegalStateException();
    }

    @Override
    public DataType canThrowException() {
        throw new IllegalStateException();
    }

    @Override
    public void setVariableVersions(String name, int oldVersion, int newVersion) {
        throw new IllegalStateException();
    }

    @Override
    public void resolveTypesForStatement(FunctionContext context) {
        throw new IllegalStateException();
    }

}
