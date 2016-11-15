
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

package de.unkrig.cscontrib.checks;

import junit.framework.TestCase;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import de.unkrig.junitcs.CheckTest;

/**
 * Test all kinds of JAVA constructs
 */
public
class JavaTest extends TestCase {
    
    @Test public void
    testSwitch() throws CheckstyleException {
        
        Whitespace check = new Whitespace();
        
        CheckTest.assertNoWarnings((
            ""
            + "public class Foo {\n"
            + "    void method() {\n"
            + "        switch (1) {\n"
            + "        case 1:\n"
            + "            break;\n"
            + "        default:\n"
            + "            break;\n"
            + "        }\n"
            + "    }\n"
            + "}\n"
        ), check);
    }

    @Test public void
    testInterfaceDefaultMethod() throws CheckstyleException {

        Whitespace check = new Whitespace();

        CheckTest.assertNoWarnings((
            ""
            + "public interface MyInterface {\n"
            + "    default void method() {}\n"
            + "}\n"
        ), check);
    }
}
