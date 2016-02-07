
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
 *    3. The name of the author may not be used to endorse or promote products derived from this software without
 *       specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package de.unkrig.cscontrib.checks;

import junit.framework.TestCase;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import de.unkrig.junitcs.CheckTest;

/**
 * Test case for the {@link WrapMethodCheck}. Requires the <a href="http://junit-cs.unkrig.de">junit-cs</a> framework.
 */
public
class WrapMethodCheckTest extends TestCase {

    /**
     * A simple positive test.
     */
    @Test public void
    testSimple() throws CheckstyleException {

        WrapMethodCheck wmc = new WrapMethodCheck();

        CheckTest.assertNoWarnings(
            (
                ""
                + "class Foo {\n"
                + "    int\n"
                + "    method() { System.out.println(\"HELLO\");\n}\n"
                + "}"
            ),
            wmc
        );
    }

    /**
     * Test for the checking of the vertical alignment of a method declaration.
     */
    @Test public void
    testAlignment() throws CheckstyleException {

        // The alignment check is not configurable, so only one (negative) check.

        CheckTest.assertWarning(
            "'method' must appear in column 5, not 6",
            (
                ""
                + "class Foo {\n"
                + "    int\n"
                + "     method() { System.out.println(\"HELLO\");\n}\n"
                + "}"
            ),
            new WrapMethodCheck()
        );
    }

    /**
     * Test for {@link WrapMethodCheck#setAllowMultipleArgsPerLine(boolean)}.
     */
    @Test public void
    testAllowMultipleArgsPerLine() throws CheckstyleException {

        String cuWithMultipleArgumentsPerLine = (
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
        
        String cuWithoutMultipleArgumentsPerLine = (
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
        
        WrapMethodCheck wmc = new WrapMethodCheck();
        
        // Default is "false".
        CheckTest.assertWarning("Must wrap line before '\"HELLO\"'", cuWithMultipleArgumentsPerLine, wmc);
        CheckTest.assertNoWarnings(cuWithoutMultipleArgumentsPerLine, wmc);
        
        wmc.setAllowMultipleArgsPerLine(false);
        CheckTest.assertWarning("Must wrap line before '\"HELLO\"'", cuWithMultipleArgumentsPerLine, wmc);
        CheckTest.assertNoWarnings(cuWithoutMultipleArgumentsPerLine, wmc);

        wmc.setAllowMultipleArgsPerLine(true);
        CheckTest.assertNoWarnings(cuWithMultipleArgumentsPerLine, wmc);
        CheckTest.assertNoWarnings(cuWithoutMultipleArgumentsPerLine, wmc);
    }

    /**
     * Test for {@link WrapMethodCheck#setAllowMultipleParametersPerLine(boolean)}.
     */
    @Test public void
    testAllowMultipleParametersPerLine() throws CheckstyleException {
        
        String allParametersInOneLine = (
            ""
            + "class Foo {\n"
            + "    int\n"
            + "    method(int a, int b) {}\n"
            + "}"
        );
        
        String oneLinePerParameter = (
            ""
            + "class Foo {\n"
            + "    int\n"
            + "    method(\n"
            + "        int a,\n"
            + "        int b\n"
            + "    ) {}\n"
            + "}"
        );
        
        String multipleParametersInOneLine = (
            ""
            + "class Foo {\n"
            + "    int\n"
            + "    method(\n"
            + "        int a, int b\n"
            + "    ) {}\n"
            + "}"
        );
        
        WrapMethodCheck wmc = new WrapMethodCheck();
        
        // Default is "false".
        CheckTest.assertNoWarnings(allParametersInOneLine, wmc);
        CheckTest.assertNoWarnings(oneLinePerParameter, wmc);
        CheckTest.assertWarning("Must wrap line before 'int'", multipleParametersInOneLine, wmc);
        
        wmc.setAllowMultipleParametersPerLine(false);
        CheckTest.assertNoWarnings(allParametersInOneLine, wmc);
        CheckTest.assertNoWarnings(oneLinePerParameter, wmc);
        CheckTest.assertWarning("Must wrap line before 'int'", multipleParametersInOneLine, wmc);
        
        wmc.setAllowMultipleParametersPerLine(true);
        CheckTest.assertNoWarnings(allParametersInOneLine, wmc);
        CheckTest.assertNoWarnings(oneLinePerParameter, wmc);
        CheckTest.assertNoWarnings(multipleParametersInOneLine, wmc);
    }

    /**
     * Test for {@link WrapMethodCheck#setAllowOneLineDecl(boolean)}.
     */
    @Test public void
    testAllowOneLineDecl() throws CheckstyleException {
        
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
        
        
        WrapMethodCheck wmc = new WrapMethodCheck();
        
        // Default is "true".
        CheckTest.assertNoWarnings(oneLineDecl, wmc);
        CheckTest.assertNoWarnings(multiLineDecl, wmc);
        
        wmc.setAllowOneLineDecl(false);
        CheckTest.assertWarning("Must wrap line before 'method'", oneLineDecl, wmc);
        CheckTest.assertNoWarnings(multiLineDecl, wmc);
        
        wmc.setAllowOneLineDecl(true);
        CheckTest.assertNoWarnings(oneLineDecl, wmc);
        CheckTest.assertNoWarnings(multiLineDecl, wmc);
    }

    /**
     * Test for {@link WrapMethodCheck#setWrapDeclBeforeName(String)}.
     */
    @Test public void
    testWrapDeclBeforeName() throws CheckstyleException {
        
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
        
        WrapMethodCheck wmc = new WrapMethodCheck();

        // Default is "always".
        CheckTest.assertWarning("Must wrap line before 'method'", notWrappedBeforeMethodName, wmc);
        CheckTest.assertNoWarnings(wrappedBeforeMethodName, wmc);

        wmc.setWrapDeclBeforeName("never");
        CheckTest.assertNoWarnings(notWrappedBeforeMethodName, wmc);
        CheckTest.assertWarning("'method' must appear on same line as 'int'", wrappedBeforeMethodName, wmc);
        
        wmc.setWrapDeclBeforeName("optional");
        CheckTest.assertNoWarnings(notWrappedBeforeMethodName, wmc);
        CheckTest.assertNoWarnings(wrappedBeforeMethodName, wmc);

        wmc.setWrapDeclBeforeName("always");
        CheckTest.assertWarning("Must wrap line before 'method'", notWrappedBeforeMethodName, wmc);
        CheckTest.assertNoWarnings(wrappedBeforeMethodName, wmc);
    }
}
