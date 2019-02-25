
/*
 * de.unkrig.cs-contrib - Additional checks, filters and quickfixes for CheckStyle and Eclipse-CS
 *
 * Copyright (c) 2019, Arno Unkrig
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *       following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *       following disclaimer in the documentation and/or other materials provided with the distribution.
 *    3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote
 *       products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

// SUPPRESS CHECKSTYLE LineLength|Javadoc:9999

package de.unkrig.cscontrib.checks;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import de.unkrig.cscontrib.util.CheckStyleTest;

/**
 * Tests for the {@link Whitespace} check.
 */
public
class WhitespaceCheckTest extends CheckStyleTest {
    
    @Test public void
    testNewInClassInstanceCreationExpression() throws CheckstyleException {

        csTest(Whitespace.class, (
            ""
            + "public class Foo {\n"
            + "    void method() {\n"
            + "        new java.util.ArrayList();\n"
            + "        new/**/java.util.ArrayList();\n"
            + "        /**/new java.util.ArrayList();\n"
            + "        /**/new/**/java.util.ArrayList();\n"
            + "    }\n"
            + "}\n"
        )).assertMessages(
            "4x12: 'new' is not followed by whitespace (option 'NEW')",
            "6x16: 'new' is not followed by whitespace (option 'NEW')"
        );
    }
    
    @Test public void
    testNewInArrayCreationExpression() throws CheckstyleException {

        csTest(Whitespace.class, (
            ""
            + "public class Foo {\n"
            + "    void method() {\n"
            + "        new int[10];\n"
            + "        new/**/int[10];\n"
            + "        /**/new int[10];\n"
            + "        /**/new/**/int[10];\n"
            + "    }\n"
            + "}\n"
        )).assertMessages(
            "4x12: 'new' is not followed by whitespace (option 'NEW')",
            "6x16: 'new' is not followed by whitespace (option 'NEW')"
        );
    }

    @Test public void
    testNewInMethodReference() throws CheckstyleException {

        CsTest csTest = csTest(Whitespace.class, (
            ""
            + "public class Foo {\n"
            + "    void method() {\n"
            + "        run(ArrayList::new);\n"
            + "        run(ArrayList::new );\n"
            + "        run(ArrayList:: new);\n"
            + "        run(ArrayList:: new );\n"
            + "    }\n"
            + "}\n"
        ));
        
        csTest.addAttribute("whitespaceBefore",   "");
        csTest.addAttribute("noWhitespaceBefore", "");
        csTest.addAttribute("whitespaceAfter",    "");
        csTest.addAttribute("noWhitespaceAfter",  "");
        csTest.assertNoMessages();
        
        csTest.addAttribute("whitespaceBefore",   "new__meth_ref");
        csTest.addAttribute("noWhitespaceBefore", "new__meth_ref");
        csTest.addAttribute("whitespaceAfter",    "new__meth_ref");
        csTest.addAttribute("noWhitespaceAfter",  "new__meth_ref");
        csTest.assertMessages(
            "3x24: 'new' is not preceded with whitespace (option 'NEW__METH_REF')",
            "3x27: 'new' is not followed by whitespace (option 'NEW__METH_REF')",
            "4x24: 'new' is not preceded with whitespace (option 'NEW__METH_REF')",
            "4x27: 'new' is followed by whitespace (option 'NEW__METH_REF')",
            "5x25: 'new' is preceded with whitespace (option 'NEW__METH_REF')",
            "5x28: 'new' is not followed by whitespace (option 'NEW__METH_REF')",
            "6x25: 'new' is preceded with whitespace (option 'NEW__METH_REF')",
            "6x28: 'new' is followed by whitespace (option 'NEW__METH_REF')"
        );
    }
    
    @Test public void
    testDoubleColon() throws CheckstyleException {
        
        CsTest csTest = csTest(Whitespace.class, (
            ""
            + "public class Foo {\n"
            + "    void method() {\n"
            + "        run(System.out::println);\n"
            + "        run(String::length);\n"
            + "        run(super::toString);\n"
            + "        run(ArrayList<String>::new);\n"
            + "        run(int[]::new);\n"
            + "    }\n"
            + "}\n"
        ));
        
        csTest.assertMessages(
        );
        
        csTest.addAttribute("whitespaceBefore", "meth_ref");
        csTest.addAttribute("whitespaceAfter",  "meth_ref");
        csTest.assertMessages(
            "3x23: '::' is not preceded with whitespace (option 'METH_REF')",
            "3x25: '::' is not followed by whitespace (option 'METH_REF')",
            "4x19: '::' is not preceded with whitespace (option 'METH_REF')",
            "4x21: '::' is not followed by whitespace (option 'METH_REF')",
            "5x18: '::' is not preceded with whitespace (option 'METH_REF')",
            "5x20: '::' is not followed by whitespace (option 'METH_REF')",
            "6x30: '::' is not preceded with whitespace (option 'METH_REF')",
            "6x32: '::' is not followed by whitespace (option 'METH_REF')",
            "7x18: '::' is not preceded with whitespace (option 'METH_REF')",
            "7x20: '::' is not followed by whitespace (option 'METH_REF')"
        );
    }
}
