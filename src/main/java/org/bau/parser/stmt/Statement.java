package org.bau.parser.stmt;

import java.util.HashSet;
import java.util.List;

import org.bau.parser.BasicBlock;
import org.bau.parser.DataType;
import org.bau.parser.FunctionContext;
import org.bau.parser.MemoryType;
import org.bau.parser.Program;
import org.bau.parser.ProgramContext;
import org.bau.parser.Solver;
import org.bau.parser.expr.Expression;
import org.bau.parser.expr.Variable;
import org.bau.runtime.Memory;

public interface Statement {

    enum StatementResult {
        OK,
        BREAK,
        RETURN,
        CONTINUE,
        THROW,
        PANIC,
        TIMEOUT
    }

    public static String indent(String s) {
        if (s.isEmpty()) {
            return s;
        }
        boolean nl = s.endsWith("\n");
        String[] lines = s.split("\n");
        StringBuilder buff = new StringBuilder();
        boolean blockComment = false;
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                buff.append("\n");
            }
            String l = lines[i];

            int todoMatchingBlockComments;
            // we have to match the number of opening # with the number of closing #

            if (blockComment) {
                // comments retain the indentation
                buff.append(l);
                if (l.trim().endsWith("##")) {
                    blockComment = false;
                }
            } else if (l.trim().startsWith("##")) {
                // block comment retain indentation until end of block comment
                buff.append(l);
                if (l.trim().equals("##") || !l.trim().endsWith("##")) {
                    blockComment = true;
                }
            } else if (l.trim().startsWith("#")) {
                // comments retain the indentation
                buff.append(l);
            } else if (l.isEmpty()) {
                // empty line
            } else {
                // regular line
                buff.append("    ");
                buff.append(l);
            }
        }
        if (nl) {
            buff.append("\n");
        }
        int todo;
        return buff.toString();
        /*
        s = s.replace("\n", "\n    ");
        if (nl) {
            s += "\n";
        }
        return "    " + s;
        */
    }

    Statement replace(Variable old, Expression with);

    StatementResult run(Memory m);

    void optimize(ProgramContext context);

    String toC();

    String format();

    default void setBounds(Solver solver, int depth, boolean loop) {

    }

    void collectTypes(HashSet<DataType> set, MemoryType memoryType);

    void used(Program program);

    DataType canThrowException();

    default BasicBlock linkBasicBlocks(FunctionContext functionContext, BasicBlock current, BasicBlock breakTarget,
            BasicBlock continueTarget) {
        if (canThrowException() != null) {
            BasicBlock next = functionContext.newBasicBlock();
            functionContext.addCatchPredecessor(next);
            current.addSuccessor(next);
            current = next;
            functionContext.linkBasicBlocks(this, current);
            return next;
        } else {
            return functionContext.linkBasicBlocks(this, current);
        }
    }

    default void setVariableVersions(FunctionContext functionContext, BasicBlock basicBlock) {
        // ignore
    }

    default void skipIncrementDecrementRefCount(String varName) {
        // ignore
    }

    void setVariableVersions(String name, int oldVersion, int newVersion);

    default List<Variable> getDeclaredVariables() {
        return List.of();
    }

    void resolveTypesForStatement(FunctionContext context);
}
