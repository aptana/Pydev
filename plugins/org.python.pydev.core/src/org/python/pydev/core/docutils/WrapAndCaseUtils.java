package org.python.pydev.core.docutils;

import org.python.pydev.core.structure.FastStringBuffer;

/*
 * Copyright 2002-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * <p>Operations on Strings that contain words.</p>
 * 
 * <p>This class tries to handle <code>null</code> input gracefully.
 * An exception will not be thrown for a <code>null</code> input.
 * Each method documents its behaviour in more detail.</p>
 * 
 * @author Apache Jakarta Velocity
 * @author Henri Yandell
 * @author Stephen Colebourne
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @author Gary Gregory
 * @since 2.0
 * @version $Id: WordUtils.java,v 1.10 2008/09/27 19:57:34 fabioz Exp $
 */
public class WrapAndCaseUtils {

    /**
     * <p><code>WordWrapUtils</code> instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * <code>WordWrapUtils.wrap("foo bar", 20);</code>.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public WrapAndCaseUtils() {
    }

    // Wrapping
    //--------------------------------------------------------------------------
    //    /**
    //     * <p>Wraps a block of text to a specified line length using '\n' as
    //     * a newline.</p>
    //     *
    //     * <p>This method takes a block of text, which might have long lines in it
    //     * and wraps the long lines based on the supplied lineLength parameter.</p>
    //     * 
    //     * <p>If a single word is longer than the line length (eg. a URL), it will
    //     * not be broken, and will display beyond the expected width.</p>
    //     * 
    //     * <p>If there are tabs in inString, you are going to get results that are
    //     * a bit strange. Tabs are a single character but are displayed as 4 or 8
    //     * spaces. Remove the tabs.</p>
    //     *
    //     * @param str  text which is in need of word-wrapping, may be null
    //     * @param lineLength  the column to wrap the words at
    //     * @return the text with all the long lines word-wrapped
    //     *  <code>null</code> if null string input
    //     */
    //    public static String wrapText(String str, int lineLength) {
    //        return wrap(str, null, lineLength);
    //    }

    //    /**
    //     * <p>Wraps a block of text to a specified line length.</p>
    //     *
    //     * <p>This method takes a block of text, which might have long lines in it
    //     * and wraps the long lines based on the supplied lineLength parameter.</p>
    //     * 
    //     * <p>If a single word is longer than the wrapColumn (eg. a URL), it will
    //     * not be broken, and will display beyond the expected width.</p>
    //     * 
    //     * <p>If there are tabs in inString, you are going to get results that are
    //     * a bit strange. Tabs are a single character but are displayed as 4 or 8
    //     * spaces. Remove the tabs.</p>
    //     *
    //     * @param str  text which is in need of word-wrapping, may be null
    //     * @param newLineChars  the characters that define a newline, null treated as \n
    //     * @param lineLength  the column to wrap the words at
    //     * @return the text with all the long lines word-wrapped
    //     *  <code>null</code> if null string input
    //     */
    //    public static String wrapText(String str, String newLineChars, int lineLength) {
    //        if (str == null) {
    //            return null;
    //        }
    //        if (newLineChars == null) {
    //            newLineChars = "\n";
    //        }
    //        StringTokenizer lineTokenizer = new StringTokenizer(str, newLineChars, true);
    //        StringBuffer stringBuffer = new StringBuffer();
    //
    //        while (lineTokenizer.hasMoreTokens()) {
    //            try {
    //                String nextLine = lineTokenizer.nextToken();
    //
    //                if (nextLine.length() > lineLength) {
    //                    // This line is long enough to be wrapped.
    //                    nextLine = wrapLine(nextLine, null, lineLength, false);
    //                }
    //
    //                stringBuffer.append(nextLine);
    //
    //            } catch (NoSuchElementException nsee) {
    //                // thrown by nextToken(), but I don't know why it would
    //                break;
    //            }
    //        }
    //
    //        return (stringBuffer.toString());
    //    }

    // Wrapping
    //-----------------------------------------------------------------------
    /**
     * <p>Wraps a single line of text, identifying words by <code>' '</code>.</p>
     * 
     * <p>New lines will be separated by the system property line separator.
     * Very long words, such as URLs will <i>not</i> be wrapped.</p>
     * 
     * <p>Leading spaces on a new line are stripped.
     * Trailing spaces are not stripped.</p>
     *
     * <pre>
     * WordUtils.wrap(null, *) = null
     * WordUtils.wrap("", *) = ""
     * </pre>
     *
     * @param str  the String to be word wrapped, may be null
     * @param wrapLength  the column to wrap the words at, less than 1 is treated as 1
     * @return a line with newlines inserted, <code>null</code> if null input
     */
    public static String wrap(String str, int wrapLength) {
        return wrap(str, wrapLength, null, false);
    }

    /**
     * <p>Wraps a single line of text, identifying words by <code>' '</code>.</p>
     * 
     * <p>Leading spaces on a new line are stripped.
     * Trailing spaces are not stripped.</p>
     * 
     * <pre>
     * WordUtils.wrap(null, *, *, *) = null
     * WordUtils.wrap("", *, *, *) = ""
     * </pre>
     *
     * @param str  the String to be word wrapped, may be null
     * @param wrapLength  the column to wrap the words at, less than 1 is treated as 1
     * @param newLineStr  the string to insert for a new line, 
     *  <code>null</code> uses the system property line separator
     * @param wrapLongWords  true if long words (such as URLs) should be wrapped
     * @return a line with newlines inserted, <code>null</code> if null input
     */
    public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
        if (str == null) {
            return null;
        }
        if (newLineStr == null) {
            newLineStr = "\n";
        }
        if (wrapLength < 1) {
            wrapLength = 1;
        }
        int inputLineLength = str.length();
        int offset = 0;
        FastStringBuffer wrappedLine = new FastStringBuffer(inputLineLength + 32);

        while ((inputLineLength - offset) > wrapLength) {
            if (str.charAt(offset) == ' ') {
                offset++;
                continue;
            }
            int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);

            if (spaceToWrapAt >= offset) {
                // normal case
                wrappedLine.append(str.substring(offset, spaceToWrapAt));
                wrappedLine.append(newLineStr);
                offset = spaceToWrapAt + 1;

            } else {
                // really long word or URL
                if (wrapLongWords) {
                    // wrap really long word one line at a time
                    wrappedLine.append(str.substring(offset, wrapLength + offset));
                    wrappedLine.append(newLineStr);
                    offset += wrapLength;
                } else {
                    // do not wrap really long word, just extend beyond limit
                    spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
                    if (spaceToWrapAt >= 0) {
                        wrappedLine.append(str.substring(offset, spaceToWrapAt));
                        wrappedLine.append(newLineStr);
                        offset = spaceToWrapAt + 1;
                    } else {
                        wrappedLine.append(str.substring(offset));
                        offset = inputLineLength;
                    }
                }
            }
        }

        // Whatever is left in line is short enough to just pass through
        wrappedLine.append(str.substring(offset));

        return wrappedLine.toString();
    }

    // Capitalizing
    //-----------------------------------------------------------------------
    /**
     * <p>Capitalizes all the whitespace separated words in a String.
     * Only the first letter of each word is changed. To change all letters to
     * the capitalized case, use {@link #capitalizeFully(String)}.</p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
     * A <code>null</code> input String returns <code>null</code>.
     * Capitalization uses the unicode title case, normally equivalent to
     * upper case.</p>
     *
     * <pre>
     * WordUtils.capitalize(null)        = null
     * WordUtils.capitalize("")          = ""
     * WordUtils.capitalize("i am FINE") = "I Am FINE"
     * </pre>
     * 
     * @param str  the String to capitalize, may be null
     * @return capitalized String, <code>null</code> if null String input
     * @see #uncapitalize(String)
     * @see #capitalizeFully(String)
     */
    public static String capitalize(String str) {
        return capitalize(str, null);
    }

    /**
     * <p>Capitalizes all the delimiter separated words in a String.
     * Only the first letter of each word is changed. To change all letters to
     * the capitalized case, use {@link #capitalizeFully(String)}.</p>
     *
     * <p>The delimiters represent a set of characters understood to separate words.
     * The first string character and the first non-delimiter character after a
     * delimiter will be capitalized. </p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * Capitalization uses the unicode title case, normally equivalent to
     * upper case.</p>
     *
     * <pre>
     * WordUtils.capitalize(null)        = null
     * WordUtils.capitalize("")          = ""
     * WordUtils.capitalize("i am FINE") = "I Am FINE"
     * </pre>
     * 
     * @param str  the String to capitalize, may be null
     * @param delimiters  set of characters to determine capitalization
     * @return capitalized String, <code>null</code> if null String input
     * @see #uncapitalize(String)
     * @see #capitalizeFully(String)
     */
    public static String capitalize(String str, char[] delimiters) {
        if (str == null || str.length() == 0) {
            return str;
        }
        int strLen = str.length();
        StringBuffer buffer = new StringBuffer(strLen);

        int delimitersLen = 0;
        if (delimiters != null) {
            delimitersLen = delimiters.length;
        }

        boolean capitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);

            boolean isDelimiter = false;
            if (delimiters == null) {
                isDelimiter = Character.isWhitespace(ch);
            } else {
                for (int j = 0; j < delimitersLen; j++) {
                    if (ch == delimiters[j]) {
                        isDelimiter = true;
                        break;
                    }
                }
            }

            if (isDelimiter) {
                buffer.append(ch);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer.append(Character.toTitleCase(ch));
                capitalizeNext = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    /**
     * <p>Capitalizes all the whitespace separated words in a String.
     * All letters are changed, so the resulting string will be fully changed.</p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
     * A <code>null</code> input String returns <code>null</code>.
     * Capitalization uses the unicode title case, normally equivalent to
     * upper case.</p>
     *
     * <pre>
     * WordUtils.capitalize(null)        = null
     * WordUtils.capitalize("")          = ""
     * WordUtils.capitalize("i am FINE") = "I Am Fine"
     * </pre>
     * 
     * @param str  the String to capitalize, may be null
     * @return capitalized String, <code>null</code> if null String input
     */
    public static String capitalizeFully(String str) {
        return capitalizeFully(str, null);
    }

    /**
     * <p>Capitalizes all the delimiter separated words in a String.
     * All letters are changed, so the resulting string will be fully changed.</p>
     *
     * <p>The delimiters represent a set of characters understood to separate words.
     * The first string character and the first non-delimiter character after a
     * delimiter will be capitalized. </p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * Capitalization uses the unicode title case, normally equivalent to
     * upper case.</p>
     *
     * <pre>
     * WordUtils.capitalize(null)        = null
     * WordUtils.capitalize("")          = ""
     * WordUtils.capitalize("i am FINE") = "I Am Fine"
     * </pre>
     * 
     * @param str  the String to capitalize, may be null
     * @param delimiters  set of characters to determine capitalization
     * @return capitalized String, <code>null</code> if null String input
     */
    public static String capitalizeFully(String str, char[] delimiters) {
        if (str == null || str.length() == 0) {
            return str;
        }
        str = str.toLowerCase();
        return capitalize(str, delimiters);
    }

    /**
     * <p>Uncapitalizes all the whitespace separated words in a String.
     * Only the first letter of each word is changed.</p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
     * A <code>null</code> input String returns <code>null</code>.</p>
     *
     * <pre>
     * WordUtils.uncapitalize(null)        = null
     * WordUtils.uncapitalize("")          = ""
     * WordUtils.uncapitalize("I Am FINE") = "i am fINE"
     * </pre>
     * 
     * @param str  the String to uncapitalize, may be null
     * @return uncapitalized String, <code>null</code> if null String input
     * @see #capitalize(String)
     */
    public static String uncapitalize(String str) {
        return uncapitalize(str, null);
    }

    /**
     * <p>Uncapitalizes all the whitespace separated words in a String.
     * Only the first letter of each word is changed.</p>
     *
     * <p>The delimiters represent a set of characters understood to separate words.
     * The first string character and the first non-delimiter character after a
     * delimiter will be uncapitalized. </p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
     * A <code>null</code> input String returns <code>null</code>.</p>
     *
     * <pre>
     * WordUtils.uncapitalize(null)        = null
     * WordUtils.uncapitalize("")          = ""
     * WordUtils.uncapitalize("I Am FINE") = "i am fINE"
     * </pre>
     * 
     * @param str  the String to uncapitalize, may be null
     * @param delimiters  set of characters to determine uncapitalization
     * @return uncapitalized String, <code>null</code> if null String input
     * @see #capitalize(String)
     */
    public static String uncapitalize(String str, char[] delimiters) {
        if (str == null || str.length() == 0) {
            return str;
        }
        int strLen = str.length();

        int delimitersLen = 0;
        if (delimiters != null) {
            delimitersLen = delimiters.length;
        }

        StringBuffer buffer = new StringBuffer(strLen);
        boolean uncapitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);

            boolean isDelimiter = false;
            if (delimiters == null) {
                isDelimiter = Character.isWhitespace(ch);
            } else {
                for (int j = 0; j < delimitersLen; j++) {
                    if (ch == delimiters[j]) {
                        isDelimiter = true;
                        break;
                    }
                }
            }

            if (isDelimiter) {
                buffer.append(ch);
                uncapitalizeNext = true;
            } else if (uncapitalizeNext) {
                buffer.append(Character.toLowerCase(ch));
                uncapitalizeNext = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    /**
     * <p>Swaps the case of a String using a word based algorithm.</p>
     * 
     * <ul>
     *  <li>Upper case character converts to Lower case</li>
     *  <li>Title case character converts to Lower case</li>
     *  <li>Lower case character after Whitespace or at start converts to Title case</li>
     *  <li>Other Lower case character converts to Upper case</li>
     * </ul>
     * 
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
     * A <code>null</code> input String returns <code>null</code>.</p>
     * 
     * <pre>
     * StringUtils.swapCase(null)                 = null
     * StringUtils.swapCase("")                   = ""
     * StringUtils.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
     * </pre>
     * 
     * @param str  the String to swap case, may be null
     * @return the changed String, <code>null</code> if null String input
     */
    public static String swapCase(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        StringBuffer buffer = new StringBuffer(strLen);

        boolean whitespace = true;
        char ch = 0;
        char tmp = 0;

        for (int i = 0; i < strLen; i++) {
            ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                tmp = Character.toLowerCase(ch);
            } else if (Character.isTitleCase(ch)) {
                tmp = Character.toLowerCase(ch);
            } else if (Character.isLowerCase(ch)) {
                if (whitespace) {
                    tmp = Character.toTitleCase(ch);
                } else {
                    tmp = Character.toUpperCase(ch);
                }
            } else {
                tmp = ch;
            }
            buffer.append(tmp);
            whitespace = Character.isWhitespace(ch);
        }
        return buffer.toString();
    }

}
