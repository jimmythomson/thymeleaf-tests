/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2016, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.thymeleaf.engine;

import java.io.IOException;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Test;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.model.IModelVisitor;
import org.thymeleaf.model.IText;
import org.thymeleaf.text.ITextRepository;
import org.thymeleaf.text.TextRepositories;


public final class TextTest {


    @Test
    public void test() {

        final ITextRepository textRepository = TextRepositories.createLimitedSizeCacheRepository();

        final char[] buf1 = "hello".toCharArray();

        final Text c1 = new Text(textRepository);
        c1.reset(buf1, 0, 5, "template", 10, 3);
        Assert.assertEquals("hello", extractText(c1));
        final String c1all = c1.getText();
        Assert.assertEquals("hello", c1all);
        Assert.assertSame(c1all, c1.getText());
        Assert.assertEquals("template", c1.getTemplateName());
        Assert.assertEquals(10, c1.getLine());
        Assert.assertEquals(3, c1.getCol());

        final String c1c0 = " something\nhere ";
        c1.setText(c1c0);
        Assert.assertSame(c1c0, c1.getText());
        Assert.assertSame(textRepository.getText(" something\nhere "), c1.getText());
        Assert.assertNull(c1.getTemplateName());
        Assert.assertEquals(-1, c1.getLine());
        Assert.assertEquals(-1, c1.getCol());

        final String c1c2 = "hey!";
        c1.setText(c1c2);
        final String c1c2_2 = c1.getText();
        Assert.assertSame(c1c2, c1c2_2);
        Assert.assertSame(c1c2, c1.getText());

        c1.reset(c1c0.toCharArray(), 0, c1c0.length(), "template", 11, 4);
        final String c1c3_2 = c1.getText();
        Assert.assertEquals(c1c0, c1c3_2);
        Assert.assertSame(c1c3_2, c1.getText());
        Assert.assertEquals("template", c1.getTemplateName());
        Assert.assertEquals(11, c1.getLine());
        Assert.assertEquals(4, c1.getCol());


        final String c2c1 = "hello";
        final Text c2 = new Text(textRepository, c2c1);
        final String c2cs1_2 = c2.getText();
        Assert.assertEquals(c2c1, c2cs1_2);
        Assert.assertSame(c2cs1_2, c2.getText());
        Assert.assertNull(c2.getTemplateName());
        Assert.assertEquals(-1, c2.getLine());
        Assert.assertEquals(-1, c2.getCol());

    }


    @Test
    public void testSubsection() {

        final ITextRepository textRepository = TextRepositories.createLimitedSizeCacheRepository();

        Text c1 = new Text(textRepository);

        c1.setText("something");

        Assert.assertEquals("thing", c1.subSequence(4, 9));
        Assert.assertEquals("some", c1.subSequence(0, 4));


        c1 = new Text(textRepository);

        c1.reset("something".toCharArray(), 0, 9, "test", 1, 1);

        Assert.assertEquals("thing", c1.subSequence(4, 9));
        Assert.assertEquals("some", c1.subSequence(0, 4));

    }


    @Test
    public void testContentFlags() {
        testFlags("", false, false);
        testFlags(" ", true, false);
        testFlags("   ", true, false);
        testFlags("\n", true, false);
        testFlags("\n  \t", true, false);
        testFlags("\n  [asd]", false, false);
        testFlags("\n  asdasdasd 23123 [ [asd ]]", false, false);
        testFlags("\n  asdasdasd 23123 [[asd ]]", false, true);
        testFlags("\n  asdasdasd 23123 [[asd ]]    [[asd]]", false, true);
        testFlags("\n  asdasdasd 23123  [ [asd ]]    [[asd] ]", false, false);
        testFlags("[[asd]]", false, true);
        testFlags("[[asd]", false, false);
        testFlags("[asd]]", false, false);
        testFlags("]]", false, false);
        testFlags("[[", false, false);
        testFlags("[[asd]]asd", false, true);
        testFlags("asd[[asd]]", false, true);
        testFlags("asd[[asd]]asd", false, true);
        testFlags("\n  (asd)", false, false);
        testFlags("\n  asdasdasd 23123 [ (asd )]", false, false);
        testFlags("\n  asdasdasd 23123 [(asd )]", false, true);
        testFlags("\n  asdasdasd 23123 [(asd )]    [(asd)]", false, true);
        testFlags("\n  asdasdasd 23123  [ (asd )]    [(asd) ]", false, false);
        testFlags("[(asd)]", false, true);
        testFlags("[(asd)", false, false);
        testFlags("[asd)]", false, false);
        testFlags(")]", false, false);
        testFlags("[(", false, false);
        testFlags("[(asd)]asd", false, true);
        testFlags("asd[(asd)]", false, true);
        testFlags("asd[(asd)]asd", false, true);
        testFlags("\n  (asd)", false, false);
        testFlags("\n  asdasdasd 23123 [ (asd ]]", false, false);
        testFlags("\n  asdasdasd 23123 [[asd )]", false, false);
        testFlags("\n  asdasdasd 23123 [(asd ]]    [[asd)]", false, false); // Intertwined inlines are not supported
        testFlags("\n  asdasdasd 23123  [ (asd ]]    [(asd) ]", false, false);
        testFlags("[(asd]]", false, false);
        testFlags("[(asd]", false, false);
        testFlags("(asd)]", false, false);
        testFlags("[(asd]]asd", false, false);
        testFlags("asd[[asd)]", false, false);
        testFlags("asd[(asd]])asd", false, false);
    }


    private static String extractText(final Text text) {

        final StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            strBuilder.append(text.charAt(i));
        }
        return strBuilder.toString();

    }


    private static void testFlags(final String text, final boolean whitespace, final boolean inlineable) {

        final ITextRepository textRepository = TextRepositories.createLimitedSizeCacheRepository();

        Text t1 = new Text(textRepository);
        t1.reset(text.toCharArray(), 0, text.length(), "test", 1, 1);
        t1.computeContentFlags();

        if (whitespace) {
            Assert.assertTrue(t1.isWhitespace());
        } else {
            Assert.assertFalse(t1.isWhitespace());
        }
        if (inlineable) {
            Assert.assertTrue(t1.isInlineable());
        } else {
            Assert.assertFalse(t1.isInlineable());
        }

        t1 = new Text(textRepository);
        t1.reset(("----[[...]]" + text + "[[...]]----").toCharArray(), 11, text.length(), "test", 1, 1);
        t1.computeContentFlags();

        if (whitespace) {
            Assert.assertTrue(t1.isWhitespace());
        } else {
            Assert.assertFalse(t1.isWhitespace());
        }
        if (inlineable) {
            Assert.assertTrue(t1.isInlineable());
        } else {
            Assert.assertFalse(t1.isInlineable());
        }

        t1 = new Text(textRepository);
        t1.reset(("----" + text + "----").toCharArray(), 4, text.length(), "test", 1, 1);
        t1.computeContentFlags();

        if (whitespace) {
            Assert.assertTrue(t1.isWhitespace());
        } else {
            Assert.assertFalse(t1.isWhitespace());
        }
        if (inlineable) {
            Assert.assertTrue(t1.isInlineable());
        } else {
            Assert.assertFalse(t1.isInlineable());
        }

        t1.setText(text);
        t1.computeContentFlags();

        if (whitespace) {
            Assert.assertTrue(t1.isWhitespace());
        } else {
            Assert.assertFalse(t1.isWhitespace());
        }
        if (inlineable) {
            Assert.assertTrue(t1.isInlineable());
        } else {
            Assert.assertFalse(t1.isInlineable());
        }

        t1 = new Text(textRepository);
        t1.reset(text.toCharArray(), 0, text.length(), "test", 1, 1);
        t1.computeContentFlags();

        Text t2 = t1.cloneEvent();

        if (whitespace) {
            Assert.assertTrue(t2.isWhitespace());
        } else {
            Assert.assertFalse(t2.isWhitespace());
        }
        if (inlineable) {
            Assert.assertTrue(t2.isInlineable());
        } else {
            Assert.assertFalse(t2.isInlineable());
        }

        t1 = new Text(textRepository);
        t1.setText(text);
        t1.computeContentFlags();

        t2 = t1.cloneEvent();

        if (whitespace) {
            Assert.assertTrue(t2.isWhitespace());
        } else {
            Assert.assertFalse(t2.isWhitespace());
        }
        if (inlineable) {
            Assert.assertTrue(t2.isInlineable());
        } else {
            Assert.assertFalse(t2.isInlineable());
        }

        t1 = new Text(textRepository);
        t1.reset(text.toCharArray(), 0, text.length(), "test", 1, 1);

        if (whitespace) {
            Assert.assertTrue(t1.isWhitespace());
        } else {
            Assert.assertFalse(t1.isWhitespace());
        }
        if (inlineable) {
            Assert.assertTrue(t1.isInlineable());
        } else {
            Assert.assertFalse(t1.isInlineable());
        }

        t1 = new Text(textRepository);
        t1.setText(text);

        if (whitespace) {
            Assert.assertTrue(t1.isWhitespace());
        } else {
            Assert.assertFalse(t1.isWhitespace());
        }
        if (inlineable) {
            Assert.assertTrue(t1.isInlineable());
        } else {
            Assert.assertFalse(t1.isInlineable());
        }


        t1 = new Text(textRepository);
        t1.setText(text);
        // By using the wrappers we avoid the utils methods calling the engine implementations (which are already tested above)
        boolean bWhitespace1 = EngineEventUtils.isWhitespace(new TextWrapper(t1));
        boolean bInlineable1 = EngineEventUtils.isInlineable(new TextWrapper(t1));
        if (whitespace) {
            Assert.assertTrue(bWhitespace1);
        } else {
            Assert.assertFalse(bWhitespace1);
        }
        if (inlineable) {
            Assert.assertTrue(bInlineable1);
        } else {
            Assert.assertFalse(bInlineable1);
        }

    }


    private static final class TextWrapper implements IText {

        private final Text delegate;

        TextWrapper(final Text delegate) {
            super();
            this.delegate = delegate;
        }

        public void resetTemplateEvent(final String templateName, final int line, final int col) {
            delegate.resetTemplateEvent(templateName, line, col);
        }

        public boolean isWhitespace() {
            return delegate.isWhitespace();
        }

        public void computeContentFlags() {
            delegate.computeContentFlags();
        }

        public void resetAsCloneOf(final Text original) {
            delegate.resetAsCloneOf(original);
        }

        public void resetAsCloneOfTemplateEvent(final AbstractTemplateEvent original) {
            delegate.resetAsCloneOfTemplateEvent(original);
        }

        public static Text asEngineText(final IEngineConfiguration configuration, final IText text, final boolean cloneAlways) {
            return Text.asEngineText(configuration, text, cloneAlways);
        }

        public boolean isInlineable() {
            return delegate.isInlineable();
        }

        public String getText() {
            return delegate.getText();
        }

        public int length() {
            return delegate.length();
        }

        public char charAt(final int index) {
            return delegate.charAt(index);
        }

        public CharSequence subSequence(final int start, final int end) {
            return delegate.subSequence(start, end);
        }

        public void reset(final char[] buffer, final int offset, final int len, final String templateName, final int line, final int col) {
            delegate.reset(buffer, offset, len, templateName, line, col);
        }

        public void setText(final CharSequence text) {
            delegate.setText(text);
        }

        public void accept(final IModelVisitor visitor) {
            delegate.accept(visitor);
        }

        public void write(final Writer writer) throws IOException {
            delegate.write(writer);
        }

        public Text cloneEvent() {
            return delegate.cloneEvent();
        }

        public String getTemplateName() {
            return delegate.getTemplateName();
        }

        public int getLine() {
            return delegate.getLine();
        }

        public int getCol() {
            return delegate.getCol();
        }

        public boolean hasLocation() {
            return delegate.hasLocation();
        }

        @Override
        public String toString() {
            return delegate.toString();
        }
    }


}