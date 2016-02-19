
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
 * Test case for the {@link CppCommentAlignment} check.
 */
public
class CppCommentAlignmentCheckTest extends TestCase {

    /**
     * Test that C++-style coments are properly aligned.
     */
    @Test public void
    testCppComments() throws CheckstyleException {

        String compilationUnit = (
            ""
            + "class            //\n"
            + "Foo              //\n"
            + "{                //\n"
            + "    void            //\n"
            + "    method          // 5\n"
            + "    (               //\n"
            + "        int            //\n"
            + "        a              //\n"
            + "        ,              //\n"
            + "        int b,         // 10\n"
            + "        int c          //\n"
            + "    ) {             //\n"
            + "        if (a) throw b;      //\n"
            + "        a = (                //\n"
            + "            b                  // 15\n"
            + "            + c                //\n"
            + "            + d                //\n"
            + "        )                    //\n"
            + "        ;                    //\n"
            + "        a                            // 20\n"
            + "        =                    //\n"
            + "        b                            //\n"
            + "        + c                          //\n"
            + "        + d                          //\n"
            + "        ;                    // 25\n"
            + "        a                                  //\n"
            + "        =                    //\n"
            + "        (                                  //\n"
            + "        b                                     //\n"
            + "        +                                      // 30\n"
            + "        c                                     //\n"
            + "        +                                  //\n"
            + "        d                                     //\n"
            + "        )                                  //\n"
            + "        ;                    // 35\n"
            + "    }                                                //\n"
            + "}                                                     //\n"
        );

        CheckTest.assertNoWarnings(compilationUnit, new CppCommentAlignment());
        
//        int lineno = 1;
//        for (int i = compilationUnit.indexOf('\n'); i != -1; i = compilationUnit.indexOf('\n', i + 2), lineno++) {
//            int j = compilationUnit.lastIndexOf("//", i);
//            String cu2 = compilationUnit.substring(0, i) + ' ' + compilationUnit.substring(i);
//            CheckTest.assertWarning(
//                "Line " + lineno + ": " + cu2,
//                "C++ comment must appear on column",
//                cu2,
//                new CppCommentAlignment()
//            );
//        }
    }
}
