package org.bau.parser;

import static org.junit.Assert.assertEquals;

import org.bau.parser.expr.StringLiteral;
import org.junit.Test;

public class StringLiteralTest {

    @Test
    public void quote() {
        assertEquals("'test'", StringLiteral.quote("test"));
        assertEquals("'tab \\t neline \\n double quote \\\" quote \\' backslash \\\\'", StringLiteral.quote("tab \t neline \n double quote \" quote ' backslash \\"));
        assertEquals("'test \\x00\\x00x1f \\xc3\\xa4\\xc3\\xb6\\xc3\\xbc'", StringLiteral.quote("test \0\0x1f äöü"));
    }

    @Test
    public void quoteRaw() {
        assertEquals("`\n"
                + "    hello\n"
                + "    world\n"
                + "    `", StringLiteral.quoteRaw("hello\nworld"));
        assertEquals("`test`", StringLiteral.quoteRaw("test"));
        assertEquals("``hello`world``", StringLiteral.quoteRaw("hello`world"));
        assertEquals("```hello``world```", StringLiteral.quoteRaw("hello``world"));
        assertEquals("``\n"
                + "    `hello\n"
                + "    ``", StringLiteral.quoteRaw("`hello"));
        assertEquals("``\n"
                + "    hello`\n"
                + "    ``", StringLiteral.quoteRaw("hello`"));
    }

}
