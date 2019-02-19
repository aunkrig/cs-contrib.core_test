
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

// SUPPRESS CHECKSTYLE LineLength:9999

package de.unkrig.cscontrib.checks;

import org.junit.Test;

import de.unkrig.cscontrib.util.CheckStyleTest;

/**
 * Test case for the {@link WrapMethodCheck}.
 */
public
class AlignmentCheckTest extends CheckStyleTest {

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
        + "public class Main {\n"                                                                    // 1
        + "\n"
        + "     int field1    = 7;\n"
        + "     double field2 = 7.0;              // Misaligned field names\n"
        + "\n"                                                                                       // 5
        + "     int    field3 = 7;\n"
        + "     double field4  = 7.0;             // Misaligned field initializers\n"
        + "\n"
        + "     public static void meth1(\n"
        + "         String[] param1,\n"                                                              // 10
        + "         int param2                    // Misaligned parameter names\n"
        + "     ) {\n"
        + "\n"
        + "         int locvar1    = 7;\n"
        + "         double locvar2 = 7.0;         // Misaligned local variable names\n"              // 15
        + "\n"
        + "         int    locvar1 = 7;\n"
        + "         double locvar2  = 7.0;        // Misaligned local variable initializers\n"
        + "\n"
        + "         y = 8;\n"                                                                        // 20
        + "         yyy = 8.0;                    // Misaligned assignments\n"
        + "\n"
        + "         switch (x) {\n"
        + "         case 1: break;\n"
        + "         default: x++; return;         // Misaligned case groups statements\n"            // 25
        + "         }\n"
        + "     }\n"
        + "\n"
        + "     public static void meth1() {}\n"
        + "     public void meth2()        {}     // Misaligned method names\n"                      // 30
        + "\n"
        + "     public static void meth3() { }\n"
        + "     public void        meth4()  {}    // Misaligned method bodies\n"
        + "\n"
        + "     public static void meth5() { }\n"                                                    // 35
        + "     public void        meth6() {}    // Misaligned method bodies\n"
        + " }"
    );

    private static final String[] APPLY_TO_ATTRIBUTE_NAMES = {
        "applyToAssignments",
        "applyToCaseGroupStatements",
        "applyToFieldInitializer",
        "applyToFieldName",
        "applyToLocalVariableInitializer",
        "applyToLocalVariableName",
        "applyToMethodBody",
        "applyToMethodName",
        "applyToParameterName",
    };

    /**
     * Test for the "{@code applyToAssignments}" property.
     */
    @Test public void
    testApplyToAssignments() {
        csTest(Alignment.class, PROPERLY_ALIGNED).addAttributes(only("applyToAssignments")).assertNoMessages();
        csTest(Alignment.class, MISALIGNED).addAttributes(only("applyToAssignments")).assertMessages("21x14: '=' should be aligned with '=' in line 20");
    }

    /**
     * Test for the "{@code applyToCaseGroupStatements}" property.
     */
    @Test public void
    testApplyToCaseGroupStatements() {
        csTest(Alignment.class, PROPERLY_ALIGNED).addAttributes(only("applyToCaseGroupStatements")).assertNoMessages();
        csTest(Alignment.class, MISALIGNED).addAttributes(only("applyToCaseGroupStatements")).assertMessages("25x19: 'x' should be aligned with 'break' in line 24");
    }

    /**
     * Test for the "{@code applyToFieldInitializer}" property.
     */
    @Test public void
    testApplyToFieldInitializer() {
        csTest(Alignment.class, PROPERLY_ALIGNED).addAttributes(only("applyToFieldInitializer")).assertNoMessages();
        csTest(Alignment.class, MISALIGNED).addAttributes(only("applyToFieldInitializer")).assertMessages("7x21: '=' should be aligned with '=' in line 6");
    }

    /**
     * Test for the "{@code applyToFieldName}" property.
     */
    @Test public void
    testApplyToFieldName() {
        csTest(Alignment.class, PROPERLY_ALIGNED).addAttributes(only("applyToFieldName")).assertNoMessages();
        csTest(Alignment.class, MISALIGNED).addAttributes(only("applyToFieldName")).assertMessages("4x13: 'field2' should be aligned with 'field1' in line 3");
    }

    /**
     * Test for the "{@code applyToLocalVariableInitializer}" property.
     */
    @Test public void
    testApplyToLocalVariableInitializer() {
        csTest(Alignment.class, PROPERLY_ALIGNED).addAttributes(only("applyToLocalVariableInitializer")).assertNoMessages();
        csTest(Alignment.class, MISALIGNED).addAttributes(only("applyToLocalVariableInitializer")).assertMessages("18x26: '=' should be aligned with '=' in line 17");
    }
    
    /**
     * Test for the "{@code applyToLocalVariableName}" property.
     */
    @Test public void
    testApplyToLocalVariableName() {
        csTest(Alignment.class, PROPERLY_ALIGNED).addAttributes(only("applyToLocalVariableName")).assertNoMessages();
        csTest(Alignment.class, MISALIGNED).addAttributes(only("applyToLocalVariableName")).assertMessages("15x17: 'locvar2' should be aligned with 'locvar1' in line 14");
    }
    
    /**
     * Test for the "{@code applyToMethodBody}" property.
     */
    @Test public void
    testApplyToMethodBody() {
        csTest(Alignment.class, PROPERLY_ALIGNED).addAttributes(only("applyToMethodBody")).assertNoMessages();
        csTest(Alignment.class, MISALIGNED).addAttributes(only("applyToMethodBody")).assertMessages(
            "33x34: '{' should be aligned with '{' in line 32",
            "36x34: '}' should be aligned with '}' in line 35"
        );
    }
    
    /**
     * Test for the "{@code applyToMethodName}" property.
     */
    @Test public void
    testApplyToMethodName() {
        csTest(Alignment.class, PROPERLY_ALIGNED).addAttributes(only("applyToMethodName")).assertNoMessages();
        csTest(Alignment.class, MISALIGNED).addAttributes(only("applyToMethodName")).assertMessages("30x18: 'meth2' should be aligned with 'meth1' in line 29");
    }
    
    /**
     * Test for the "{@code applyToParameterName}" property.
     */
    @Test public void
    testApplyToParameterName() {
        csTest(Alignment.class, PROPERLY_ALIGNED).addAttributes(only("applyToParameterName")).assertNoMessages();
        csTest(Alignment.class, MISALIGNED).addAttributes(only("applyToParameterName")).assertMessages("11x14: 'param2' should be aligned with 'param1' in line 10");
    }

    private String[]
    only(String attributeName) {
        
        String[] result = new String[APPLY_TO_ATTRIBUTE_NAMES.length * 2];
        
        for (int i = 0; i < APPLY_TO_ATTRIBUTE_NAMES.length; i++) {
            String an = APPLY_TO_ATTRIBUTE_NAMES[i];
            
            result[2 * i]     = an;
            result[2 * i + 1] = String.valueOf(attributeName.equals(an));
        }
        
        return result;
    }
}
