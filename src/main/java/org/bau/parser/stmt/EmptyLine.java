package org.bau.parser.stmt;

import java.util.HashSet;

import org.bau.parser.DataType;
import org.bau.parser.FunctionContext;
import org.bau.parser.MemoryType;
import org.bau.parser.Program;
import org.bau.parser.ProgramContext;
import org.bau.parser.Section;
import org.bau.parser.expr.Expression;
import org.bau.parser.expr.Variable;
import org.bau.runtime.Memory;

public class EmptyLine implements Statement, Section {

    @Override
    public String formatSource() {
        return "\n";
    }

    @Override
    public Statement replace(Variable old, Expression with) {
        return this;
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
        return "\n";
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
