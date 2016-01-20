
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
 * Test case for the {@link WrapMethodCheck}.
 */
public
class AlignmentCheckTest extends TestCase {

    private static final String PROPERLY_ALIGNED = (
        ""
        + "public class Main {\n"
        + "\n"
        + "     int    field1 = 7;\n"
        + "     double field2 = 7.0;             // Aligned field names and initializers\n"
        + "\n"
        + "     public static void meth1(\n"
        + "         String[] param1,\n"
        + "         int      param2              // Aligned parameter names\n"
        + "     ) {\n"
        + "\n"
        + "         int    locvar1 = 7;\n"
        + "         double locvar2 = 7.0;        // Aligned local variable names and initializers\n"
        + "\n"
        + "         y   = 8;\n"
        + "         yyy = 8.0;                   // Aligned assignments\n"
        + "\n"
        + "         switch (x) {\n"
        + "         case 1:  break;\n"
        + "         default: x++; return;        // Aligned case groups statements\n"
        + "         }\n"
        + "     }\n"
        + "\n"
        + "     public static void meth2()  {}\n"
        + "     public void        meth33() {}   // Aligned method names and bodies\n"
        + " }"
    );
    
    private static final String MISALIGNED = (
        ""
        + "public class Main {\n"
        + "\n"
        + "     int field1    = 7;\n"
        + "     double field2 = 7.0;              // Misaligned field names\n"
        + "\n"
        + "     int    field3 = 7;\n"
        + "     double field4  = 7.0;             // Misaligned field initializers\n"
        + "\n"
        + "     public static void meth1(\n"
        + "         String[] param1,\n"
        + "         int param2                    // Misaligned parameter names\n"
        + "     ) {\n"
        + "\n"
        + "         int locvar1    = 7;\n"
        + "         double locvar2 = 7.0;         // Misaligned local variable names\n"
        + "\n"
        + "         int    locvar1 = 7;\n"
        + "         double locvar2  = 7.0;        // Misaligned local variable initializers\n"
        + "\n"
        + "         y = 8;\n"
        + "         yyy = 8.0;                    // Misaligned assignments\n"
        + "\n"
        + "         switch (x) {\n"
        + "         case 1: break;\n"
        + "         default: x++; return;         // Misaligned case groups statements\n"
        + "         }\n"
        + "     }\n"
        + "\n"
        + "     public static void meth1() {}\n"
        + "     public void meth2()        {}     // Misaligned method names\n"
        + "\n"
        + "     public static void meth3() {}\n"
        + "     public void        meth4()  {}    // Misaligned method bodies\n"
        + " }"
    );

    /**
     * Test for the "{@code applyToAssignments}" property.
     */
    @Test public void
    testApplyToAssignments() throws CheckstyleException {

        Alignment ac = newAlignmentCheck();
        ac.setApplyToAssignments(true);

        CheckTest.assertNoWarnings(PROPERLY_ALIGNED, ac);
        CheckTest.assertWarning("21x14: '=' should be aligned with '=' in line 20", MISALIGNED, ac);
    }

    /**
     * Test for the "{@code applyToCaseGroupStatements}" property.
     */
    @Test public void
    testApplyToCaseGroupStatements() throws CheckstyleException {
        
        Alignment ac = newAlignmentCheck();
        ac.setApplyToCaseGroupStatements(true);

        CheckTest.assertNoWarnings(PROPERLY_ALIGNED, ac);
        CheckTest.assertWarning("25x19: 'x' should be aligned with 'break' in line 24", MISALIGNED, ac);
    }

    /**
     * Test for the "{@code applyToFieldInitializer}" property.
     */
    @Test public void
    testApplyToFieldInitializer() throws CheckstyleException {
        
        Alignment ac = newAlignmentCheck();
        ac.setApplyToFieldInitializer(true);

        CheckTest.assertNoWarnings(PROPERLY_ALIGNED, ac);
        CheckTest.assertWarning("7x21: '=' should be aligned with '=' in line 6", MISALIGNED, ac);
    }

    /**
     * Test for the "{@code applyToFieldName}" property.
     */
    @Test public void
    testApplyToFieldName() throws CheckstyleException {
        
        Alignment ac = newAlignmentCheck();
        ac.setApplyToFieldName(true);
        
        CheckTest.assertNoWarnings(PROPERLY_ALIGNED, ac);
        CheckTest.assertWarning("4x13: 'field2' should be aligned with 'field1' in line 3", MISALIGNED, ac);
    }

    /**
     * Test for the "{@code applyToLocalVariableInitializer}" property.
     */
    @Test public void
    testApplyToLocalVariableInitializer() throws CheckstyleException {
        
        Alignment ac = newAlignmentCheck();
        ac.setApplyToLocalVariableInitializer(true);
        
        CheckTest.assertNoWarnings(PROPERLY_ALIGNED, ac);
        CheckTest.assertWarning("18x26: '=' should be aligned with '=' in line 17", MISALIGNED, ac);
    }
    
    /**
     * Test for the "{@code applyToLocalVariableName}" property.
     */
    @Test public void
    testApplyToLocalVariableName() throws CheckstyleException {
        
        Alignment ac = newAlignmentCheck();
        ac.setApplyToLocalVariableName(true);
        
        CheckTest.assertNoWarnings(PROPERLY_ALIGNED, ac);
        CheckTest.assertWarning("15x17: 'locvar2' should be aligned with 'locvar1' in line 14", MISALIGNED, ac);
    }
    
    /**
     * Test for the "{@code applyToMethodBody}" property.
     */
    @Test public void
    testApplyToMethodBody() throws CheckstyleException {
        
        Alignment ac = newAlignmentCheck();
        ac.setApplyToMethodBody(true);
        
        CheckTest.assertNoWarnings(PROPERLY_ALIGNED, ac);
        CheckTest.assertWarning("33x34: '{' should be aligned with '{' in line 32", MISALIGNED, ac);
    }
    
    /**
     * Test for the "{@code applyToMethodName}" property.
     */
    @Test public void
    testApplyToMethodName() throws CheckstyleException {
        
        Alignment ac = newAlignmentCheck();
        ac.setApplyToMethodName(true);
        
        CheckTest.assertNoWarnings(PROPERLY_ALIGNED, ac);
        CheckTest.assertWarning("30x18: 'meth2' should be aligned with 'meth1' in line 29", MISALIGNED, ac);
    }
    
    /**
     * Test for the "{@code applyToParameterName}" property.
     */
    @Test public void
    testApplyToParameterName() throws CheckstyleException {
        
        Alignment ac = newAlignmentCheck();
        ac.setApplyToParameterName(true);
        
        CheckTest.assertNoWarnings(PROPERLY_ALIGNED, ac);
        CheckTest.assertWarning("11x14: 'param2' should be aligned with 'param1' in line 10", MISALIGNED, ac);
    }

    /**
     * @return An AlignmentCheckTest with all "{@code applyTo...}" properties set to {@code false}
     */
    private static Alignment
    newAlignmentCheck() {

        Alignment ac = new Alignment();
        ac.setApplyToAssignments(false);
        ac.setApplyToCaseGroupStatements(false);
        ac.setApplyToFieldInitializer(false);
        ac.setApplyToFieldName(false);
        ac.setApplyToLocalVariableInitializer(false);
        ac.setApplyToLocalVariableName(false);
        ac.setApplyToMethodBody(false);
        ac.setApplyToMethodName(false);
        ac.setApplyToParameterName(false);

        return ac;
    }
}
