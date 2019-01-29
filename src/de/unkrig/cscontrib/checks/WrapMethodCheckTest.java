
/*
 * de.unkrig.cs-contrib - Additional checks, filters and quickfixes for CheckStyle and Eclipse-CS
 *
 * Copyright (c) 2016, Arno Unkrig
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

// SUPPRESS CHECKSTYLE Javadoc|LineLength:9999

package de.unkrig.cscontrib.checks;

import org.junit.Test;

import de.unkrig.cscontrib.util.CheckStyleTest;

/**
 * Test case for the {@link WrapMethodCheck}. Requires the <a href="http://junit-cs.unkrig.de">junit-cs</a> framework.
 */
public
class WrapMethodCheckTest extends CheckStyleTest {

    /**
     * A simple positive test.
     */
    @Test public void
    testSimple() {

        csTest(WrapMethodCheck.class, (
            ""
            + "class Foo {\n"
            + "    int\n"
            + "    method() { System.out.println(\"HELLO\");\n}\n"
            + "}"
        )).assertNoMessages();
    }

    /**
     * Test for the checking of the vertical alignment of a method declaration.
     */
    @Test public void
    testAlignment() {

        // The alignment check is not configurable, so only one (negative) check.

        csTest(WrapMethodCheck.class, (
            ""
            + "class Foo {\n"
            + "    int\n"
            + "     method() { System.out.println(\"HELLO\"); }\n" // <= Indentation is one SPACE too deep.
            + "}"
        )).assertMessages("3x6: 'method' must appear in column 5, not 6");
    }

    /**
     * Test for {@link WrapMethodCheck#setAllowMultipleArgsPerLine(boolean)}.
     */
    @Test public void
    testAllowMultipleArgsPerLine() {

        String multipleArgumentsPerLine = (
            ""
            + "class Foo {\n"
            + "    int\n"
            + "    method() {\n"
            + "        System.out.printf(\n"
            + "            \"%s%n\", \"HELLO\"\n"
            + "        );\n"
            + "    }\n"
            + "}"
        );
        
        String noMultipleArgumentsPerLine = (
            ""
            + "class Foo {\n"
            + "    int\n"
            + "    method() {\n"
            + "        System.out.printf(\n"
            + "            \"%s%n\",\n"
            + "            \"HELLO\"\n"
            + "        );\n"
            + "    }\n"
            + "}"
        );
        
        
        // Default is "false".
        csTest(WrapMethodCheck.class, multipleArgumentsPerLine).assertMessages("5x21: Must wrap line before '\"HELLO\"'");
        csTest(WrapMethodCheck.class, noMultipleArgumentsPerLine).assertNoMessages();
        
        csTest(WrapMethodCheck.class, multipleArgumentsPerLine).addAttribute("allowMultipleArgsPerLine", "false").assertMessages("5x21: Must wrap line before '\"HELLO\"'");
        csTest(WrapMethodCheck.class, noMultipleArgumentsPerLine).addAttribute("allowMultipleArgsPerLine", "false").assertNoMessages();

        csTest(WrapMethodCheck.class, multipleArgumentsPerLine).addAttribute("allowMultipleArgsPerLine", "true").assertNoMessages();
        csTest(WrapMethodCheck.class, noMultipleArgumentsPerLine).addAttribute("allowMultipleArgsPerLine", "true").assertNoMessages();
    }

    /**
     * Test for {@link WrapMethodCheck#setAllowMultipleParametersPerLine(boolean)}.
     */
    @Test public void
    testAllowMultipleParametersPerLine() {
        
        final String allParametersInOneLine = (
            ""
            + "class Foo {\n"
            + "    int\n"
            + "    method(int a, int b) {}\n"
            + "}"
        );
        
        final String oneLinePerParameter = (
            ""
            + "class Foo {\n"
            + "    int\n"
            + "    method(\n"
            + "        int a,\n"
            + "        int b\n"
            + "    ) {}\n"
            + "}"
        );
        
        final String multipleParametersInOneLine = (
            ""
            + "class Foo {\n"
            + "    int\n"
            + "    method(\n"
            + "        int a, int b\n"
            + "    ) {}\n"
            + "}"
        );
        
        // Default for "allowMultipleParametersPerLine" is "false".
        csTest(WrapMethodCheck.class, allParametersInOneLine).assertNoMessages();
        csTest(WrapMethodCheck.class, oneLinePerParameter).assertNoMessages();
        csTest(WrapMethodCheck.class, multipleParametersInOneLine).assertMessages("4x16: Must wrap line before 'int'");
        
        csTest(WrapMethodCheck.class, allParametersInOneLine).addAttribute("allowMultipleParametersPerLine", "false").assertNoMessages();
        csTest(WrapMethodCheck.class, oneLinePerParameter).addAttribute("allowMultipleParametersPerLine", "false").assertNoMessages();
        csTest(WrapMethodCheck.class, multipleParametersInOneLine).addAttribute("allowMultipleParametersPerLine", "false").assertMessages("4x16: Must wrap line before 'int'");
        
        csTest(WrapMethodCheck.class, allParametersInOneLine).addAttribute("allowMultipleParametersPerLine", "true").assertNoMessages();
        csTest(WrapMethodCheck.class, oneLinePerParameter).addAttribute("allowMultipleParametersPerLine", "true").assertNoMessages();
        csTest(WrapMethodCheck.class, multipleParametersInOneLine).addAttribute("allowMultipleParametersPerLine", "true").assertNoMessages();
    }

    /**
     * Test for {@link WrapMethodCheck#setAllowOneLineDecl(boolean)}.
     */
    @Test public void
    testAllowOneLineDecl() {
        
        String oneLineDecl = (
            ""
            + "class Foo {\n"
            + "    int method(int a, int b) {}\n"
            + "}"
        );
        
        String multiLineDecl = (
            ""
            + "class Foo {\n"
            + "    int\n"
            + "    method(int a, int b) {}\n"
            + "}"
        );
        
        // Default is "true".
        csTest(WrapMethodCheck.class, oneLineDecl).assertNoMessages();
        csTest(WrapMethodCheck.class, multiLineDecl).assertNoMessages();
        
        csTest(WrapMethodCheck.class, oneLineDecl).addAttribute("allowOneLineDecl", "false").assertMessages("2x9: Must wrap line before 'method'");
        csTest(WrapMethodCheck.class, multiLineDecl).addAttribute("allowOneLineDecl", "false").assertNoMessages();
        
        csTest(WrapMethodCheck.class, oneLineDecl).addAttribute("allowOneLineDecl", "true").assertNoMessages();
        csTest(WrapMethodCheck.class, multiLineDecl).addAttribute("allowOneLineDecl", "true").assertNoMessages();
    }

    /**
     * Test for {@link WrapMethodCheck#setWrapDeclBeforeName(String)}.
     */
    @Test public void
    testWrapDeclBeforeName() {
        
        String wrappedBeforeMethodName = (
            ""
            + "class Foo {\n"
            + "    int\n"
            + "    method() { System.out.println(\"HELLO\");\n}\n"
            + "}"
        );
        
        String notWrappedBeforeMethodName = (
            ""
            + "class Foo {\n"
            + "    int method() { System.out.println(\"HELLO\");\n}\n"
            + "}"
        );
        
        // Default is "always".
        csTest(WrapMethodCheck.class, notWrappedBeforeMethodName).assertMessages("2x9: Must wrap line before 'method'");
        csTest(WrapMethodCheck.class, wrappedBeforeMethodName).assertNoMessages();

        csTest(WrapMethodCheck.class, notWrappedBeforeMethodName).addAttribute("wrapDeclBeforeName", "never").assertNoMessages();
        csTest(WrapMethodCheck.class, wrappedBeforeMethodName).addAttribute("wrapDeclBeforeName", "never").assertMessages("3x5: 'method' must appear on same line as 'int'");
        
        csTest(WrapMethodCheck.class, notWrappedBeforeMethodName).addAttribute("wrapDeclBeforeName", "optional").assertNoMessages();
        csTest(WrapMethodCheck.class, wrappedBeforeMethodName).addAttribute("wrapDeclBeforeName", "optional").assertNoMessages();

        csTest(WrapMethodCheck.class, notWrappedBeforeMethodName).addAttribute("wrapDeclBeforeName", "always").assertMessages("2x9: Must wrap line before 'method'");
        csTest(WrapMethodCheck.class, wrappedBeforeMethodName).addAttribute("wrapDeclBeforeName", "always").assertNoMessages();
    }
}
