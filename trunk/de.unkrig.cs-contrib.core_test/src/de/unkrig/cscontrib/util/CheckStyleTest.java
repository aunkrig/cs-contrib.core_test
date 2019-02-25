
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

// SUPPRESS CHECKSTYLE Javadoc:9999

package de.unkrig.cscontrib.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.TreeWalker;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public abstract
class CheckStyleTest {

    protected final List<String> errorMessages = new ArrayList<String>();

    public
    interface CsTest {

        CsTest addAttribute(String name, String value);
        CsTest addAttributes(String... namesAndValues);

        /**
         * Verifies that executing the checks does not issue any audit events.
         */
        void assertNoMessages();

        /**
         * Verifies that executing the checks issues a sequence of audit events, and that these events' messages
         * equal the given <var>expectedMessages</var>..
         */
        void assertMessages(String... expectedMessages);
    }

    protected CsTest
    csTest(Class<?> checkClass, final String cu) {
        
        final DefaultConfiguration dc = new DefaultConfiguration(checkClass.getName());
        
        return new CsTest() {
            
            @Override public CsTest
            addAttribute(String name, String value) { 
                dc.addAttribute(name, value);
                return this;
            } 
            
            @Override public CsTest
            addAttributes(String... namesAndValues) { 
                for (int i = 0; i < namesAndValues.length;) dc.addAttribute(namesAndValues[i++], namesAndValues[i++]);
                return this;
            } 
            
            @Override public void
            assertNoMessages() {
                CheckStyleTest.this.assertNoMessages(createTwChecker(dc), cu);
            } 
            
            @Override public void
            assertMessages(String... expectedMessages) {
                CheckStyleTest.this.assertMessages(createTwChecker(dc), cu, expectedMessages);
            }
        };
    }
    
    private Checker
    createTwChecker(Configuration configuration) {

        final DefaultConfiguration result = new DefaultConfiguration(TreeWalker.class.getName());
        result.addChild(configuration);
        
        return this.createChecker(result);
    }
    
    private Checker
    createChecker(Configuration configuration) {
        
        final Checker result = new Checker();
        
        // make sure the tests always run with english error messages
        // so the tests don't fail in supported locales like german
        final Locale locale = Locale.ENGLISH;
        result.setLocaleCountry(locale.getCountry());
        result.setLocaleLanguage(locale.getLanguage());
        
        result.setModuleClassLoader(Thread.currentThread().getContextClassLoader());
        try {
            result.configure(createCheckerConfig(configuration));
        } catch (CheckstyleException e) {
            throw new AssertionError(e);
        }
        result.addListener(new AuditListener() {

            @Override public void auditStarted(AuditEvent event)  {} 
            @Override public void auditFinished(AuditEvent event) {} 
            @Override public void fileStarted(AuditEvent event)   {} 
            @Override public void fileFinished(AuditEvent event)  {} 
            
            @Override public void
            addError(AuditEvent event) { 
                errorMessages.add(event.getLine() + "x" + event.getColumn() + ": " + event.getMessage());
            } 
            
            @Override public void
            addException(AuditEvent event, Throwable throwable) {
                errorMessages.add(event.getLine() + "x" + event.getColumn() + ": " + throwable + ": " + event.getMessage());
            }
        });
        return result;
    }
    
    protected DefaultConfiguration
    createCheckerConfig(Configuration configuration) {
        
        final DefaultConfiguration result = new DefaultConfiguration("root");
        result.addChild(configuration);
        
        return result;
    }
    
    protected void
    verify(Checker checker, File file, String[] expectedMessages) {
        verify(checker, new File[] { file }, expectedMessages);
    }

    protected void
    verify(Checker checker, File[] files, String... expectedMessages) {
        
        this.errorMessages.clear();
        
        try {
            /*final int errs =*/ checker.process(java.util.Arrays.asList(files));
        } catch (CheckstyleException e) {
            throw new AssertionError(e);
        }

        final Iterator<String> actualIt = this.errorMessages.iterator();

        for (int i = 0; i < expectedMessages.length; i++) {
            
            final String expectedMessage = expectedMessages[i];
            
            if (!actualIt.hasNext()) Assert.fail("Expected CheckStyle message \"" + expectedMessage + "\"");
            final String actual = actualIt.next();
            
            Assert.assertEquals("CheckStyle message #" + i, expectedMessage, actual);
        }
        
        if (actualIt.hasNext()) Assert.fail("Unexpected CheckStyle message \"" + actualIt.next() + "\"");

        checker.destroy();
    }
    
    protected void
    assertNoMessages(Checker checker, String code) {
        assertMessages(checker, code, new String[0]);
    }

    protected void
    assertMessages(Checker checker, String code, String... expectedMessages) {
        
        try {
            File tempFile = File.createTempFile("checkstyle-test", ".java");
            
            try (Writer w = new FileWriter(tempFile)) {
                w.write(code);
                w.close();
                verify(checker, tempFile, expectedMessages);
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
