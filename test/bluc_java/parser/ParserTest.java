/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package bluc_java.parser;

import bluc_java.Result;
import bluc_java.Token;
import bluc_java.parser.Parser.AdvanceParserErrCode;
import bluc_java.parser.Parser.NextTokenErrCode;
import bluc_java.parser.Parser.ParseResult;
import bluc_java.parser.Parser.ParseResultErrCode;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the parser class.
 * 
 * @author john
 */
public class ParserTest
{
    
    public ParserTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getCurrentTokenText method, of class Parser.
     */
    @Test
    public void testGetCurrentTokenText()
    {
        System.out.println("getCurrentTokenText");
        
        var testFileName    = "junit4_fake_test_file.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        var expResult       = "A";
        
        var testTokens
                = builder
                .addToken(1, 1, expResult)
                .addToken(2, 1, "b")
                .addToken(2, 2, "donut")
                .build();
        
        var instance = new Parser(testTokens);
        
        // advance off the "start of file" token
        instance.nextToken();
        
        String result = instance.getCurrentTokenText();
        assertEquals(expResult, result);
    }

    /**
     * Test that the "next" token in the parser is the expected token.
     */
    @Test
    public void testPeek_nextTokenIsCorrectToken()
    {
        System.out.println("peek");
        
        var testFileName    = "junit4_fake_test_file2.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        var expResult       = new Token(testFileName, 1, 2, "b");
        
        var testTokens
                = builder
                .addToken(1, 1, "a")
                .addToken(expResult)
                .addToken(2, 2, "aubergine")
                .build();
        
        
        var instance = new Parser(testTokens);
        
        // We start on the "start of file" token, so advance off of it
        instance.nextToken();
        
        var offset = 1;
        var result = instance.peek(offset);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test that the "next" token in the parser, when the parser is at the
     *  penultimate token, is the EOF token.
     */
    @Test
    public void testPeek_endOfFileReturnsEOFToken()
    {
        System.out.println("peek");
        
        var testFileName    = "junit4_fake_test_file3.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        var expResult       = Token.BLUC_EOF;
        
        var testTokens
                = builder
                .addToken(1, 1, "a")
                .addToken(2, 2, "aubergine")
                .build();
        
        
        var instance = new Parser(testTokens);
        
        // We start on the "start of file" token, so advance off of it
        instance.nextToken();
        instance.nextToken();
        instance.nextToken();
        
        var offset = 1;
        var result = instance.peek(offset);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test that the parser starts on the "start of file" token.
     */
    @Test
    public void testPeek_parserStartsOnSOFToken()
    {
        System.out.println("Parser(ArrayList<Token> lexedTokens)");
        
        var testFileName    = "junit4_fake_test_file3.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        var expResult       = Token.BLUC_SOF;
        
        var testTokens
                = builder
                .addToken(1, 1, "apple")
                .addToken(2, 2, "orange")
                .build();
        
        
        var instance = new Parser(testTokens);
        var result = instance.currentToken();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of nextTokenMatches method, of class Parser.
     */
    @Test
    public void testNextTokenMatches_String_FindsNothing()
    {
        System.out.println("nextTokenMatches(String textToMatch)");
        
        var testFileName    = "junit4_fake_test_file4.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        var textToMatch     = "aubergine";
        
        var testTokens
                = builder
                .addToken(1, 1, "apple")
                .addToken(2, 2, "orange")
                .build();

        var instance    = new Parser(testTokens);
        var expResult   = false;
        var result      = instance.nextTokenMatches(textToMatch);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of nextTokenMatches method, of class Parser.
     */
    @Test
    public void testNextTokenMatches_String_FindsSomething()
    {
        System.out.println("nextTokenMatches(String textToMatch)");
        
        var testFileName    = "junit4_fake_test_file5.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        var textToMatch     = "apple";
        
        var testTokens
                = builder
                .addToken(1, 1, textToMatch)
                .addToken(2, 2, "orange")
                .build();

        var instance    = new Parser(testTokens);
        var expResult   = true;
        var result      = instance.nextTokenMatches(textToMatch);
        assertEquals(expResult, result);
    }

    /**
     * Test of nextTokenMatches method, of class Parser.
     */
    @Test
    public void testNextTokenMatches_StringArr_FindsNothing()
    {
        System.out.println("nextTokenMatches");
        
        var testFileName    = "junit4_fake_test_file6.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        var textToMatch     = "peanut_butter";
        
        var testTokens
                = builder
                .addToken(1, 1, textToMatch)
                .addToken(2, 2, "orange")
                .build();
        
        var textsToMatch    = new String[]{ "apple", "aubergine" };
        var instance        = new Parser(testTokens);
        var expResult       = false;
        var result          = instance.nextTokenMatches(textsToMatch);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of nextTokenMatches method, of class Parser.
     */
    @Test
    public void testNextTokenMatches_StringArr_FindsSomething()
    {
        System.out.println("nextTokenMatches");
        
        var testFileName    = "junit4_fake_test_fileA.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        var textToMatch     = "peanut_butter";
        
        var testTokens
                = builder
                .addToken(1, 1, textToMatch)
                .addToken(2, 2, "orange")
                .build();
        
        var textsToMatch    = new String[]{ "apple", "aubergine" };
        var instance        = new Parser(testTokens);
        var expResult       = false;
        var result          = instance.nextTokenMatches(textsToMatch);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of peekMatches method, of class Parser.
     */
    @Test
    public void testPeekMatches_int_String_FindsMatch()
    {
        System.out.println("int peekMatches(String textToMatch)");
        
        var testFileName    = "junit4_fake_test_fileB.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addToken(1, 1, "lots")
                .addToken(2, 2, "of")
                .addToken(2, 5, "unit")
                .addToken(3, 1, "tests")
                .build();
        
        var offset = 0;
        var textToMatch = "of";
        var instance = new Parser(testTokens);
        
        // Advance off "start of file" token.
        instance.nextToken();
        instance.nextToken();
        
        var expResult   = true;
        var result      = instance.peekMatches(offset, textToMatch);
        assertEquals(expResult, result);
    }

    /**
     * Test of peekMatches method, of class Parser.
     */
    @Test
    public void testPeekMatches_int_String_DoesntFindMatch()
    {
        System.out.println("int peekMatches(String textToMatch)");
        
        var testFileName    = "junit4_fake_test_fileC.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addToken(1, 1, "lots")
                .addToken(2, 2, "of")
                .addToken(2, 5, "unit")
                .addToken(3, 1, "tests")
                .build();
        
        var offset = 0;
        var textToMatch = "aubergines";
        var instance = new Parser(testTokens);
        
        instance.nextToken();
        
        var expResult   = false;
        var result      = instance.peekMatches(offset, textToMatch);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of peekMatches method, of class Parser.
     */
    @Test
    public void testPeekMatches_int_StringArr_DoesntFindMatch()
    {
        System.out.println("int peekMatches(String[] textsToMatch)");
        
        var testFileName    = "junit4_fake_test_fileC.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addToken(1, 1, "many")
                .addToken(1, 6, ",")
                .addToken(2, 2, "many")
                .addToken(2, 5, "unit")
                .addToken(3, 1, "tests")
                .build();
        
        var offset          = 0;
        var textsToMatch    = new String[]{ "orange", "apple" };
        var instance        = new Parser(testTokens);
        var expResult       = false;
        var result          = instance.peekMatches(offset, textsToMatch);
        assertEquals(expResult, result);
    }

    /**
     * Test of peekMatches method, of class Parser.
     */
    @Test
    public void testPeekMatches_int_StringArr_FindsMatch()
    {
        System.out.println("int peekMatches(String[] textsToMatch)");
        
        var testFileName    = "junit4_fake_test_fileC.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addToken(1, 1, "a")
                .addToken(1, 6, "lot")
                .addToken(2, 2, "of")
                .addToken(2, 5, "testing")
                .addToken(3, 1, "units")
                .build();
        
        var offset          = 0;
        var textsToMatch    = new String[]{ "orange", "apple", "a"};
        var instance        = new Parser(testTokens);
        var expResult       = false;
        var result          = instance.peekMatches(offset, textsToMatch);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currentTokenMatches method, of class Parser.
     */
    @Test
    public void testCurrentTokenMatches_StringArr_DoesntFindMatch()
    {
        System.out.println("currentTokenMatches(String[] textsToMatch)");
        
        var testFileName    = "junit4_fake_test_fileC.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addToken(1, 1, "maybe_i_will_start")
                .addToken(2, 1, "writing_haikus_for_testing")
                .addToken(3, 1, "these_tests_are_boring")
                .build();
        
        var textsToMatch    = new String[] { "haikus", "are", "fun" };
        var instance        = new Parser(testTokens);
        var expResult       = false;
        var result          = instance.currentTokenMatches(textsToMatch);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currentTokenMatches method, of class Parser.
     */
    @Test
    public void testCurrentTokenMatches_StringArr_FindsMatch()
    {
        System.out.println("currentTokenMatches(String[] textsToMatch)");
        
        var testFileName    = "junit4_fake_test_fileC.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addToken(1, 1, "let")
                .addToken(1, 5, "me")
                .addToken(1, 9, "try")
                .addToken(2, 1, "three_by_five_haiku")
                .addToken(3, 1, "its_fun_too")
                .build();
        
        var textsToMatch    = new String[] { "me", "haikus", "are", "fun" };
        var instance        = new Parser(testTokens);
        var expResult       = true;
        
        // Don't always just test the very first token. In this case, test the
        //  second token.
        instance.nextToken();
        instance.nextToken();
        
        var result = instance.currentTokenMatches(textsToMatch);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currentTokenMatches method, of class Parser.
     */
    @Test
    public void testCurrentTokenMatches_String_DoesntFindMatch()
    {
        System.out.println("currentTokenMatches(String textToMatch)");
        
        var testFileName    = "junit4_fake_test_fileC.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addToken(1, 1, "three")
                .addToken(1, 5, "by")
                .addToken(1, 9, "five")
                .addToken(2, 1, "haiku-ing-it-up")
                .addToken(3, 1, "its-much-fun")
                .build();
        
        var textToMatch = "five";
        var instance    = new Parser(testTokens);
        var expResult   = false;
        var result      = instance.currentTokenMatches(textToMatch);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currentTokenMatches method, of class Parser.
     */
    @Test
    public void testCurrentTokenMatches_String_FindsMatch()
    {
        System.out.println("currentTokenMatches(String textToMatch)");
        
        var testFileName    = "junit4_fake_test_file55C.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addToken(1, 1, "bored")
                .addToken(1, 5, "of")
                .addToken(1, 9, "haikus")
                .addToken(1, 17, "now")
                .addToken(2, 1, "but-doing-five-by-seven")
                .addToken(3, 1, "dont-think-i-can-stop")
                .build();
        
        var textToMatch = "bored";
        var instance    = new Parser(testTokens);
        var expResult   = true;
        
        instance.nextToken();
        
        var result      = instance.currentTokenMatches(textToMatch);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currentLineNum method, of class Parser.
     */
    @Test
    public void testCurrentLineNum()
    {
        System.out.println("currentLineNum");
        
        var testFileName    = "junit4_fake_test_file55C.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addToken(1, 1, "another")
                .addToken(1, 5, "haiku")
                .addToken(2, 1, "but-not-yet-anoter-day")
                .addToken(3, 1, "tests-should-be-done-soon")
                .build();
        
        var instance    = new Parser(testTokens);
        var expResult   = 2;
        
        instance.nextToken();
        instance.nextToken();
        instance.nextToken();
        
        var result = instance.currentLineNum();
        assertEquals(expResult, result);
    }

    /**
     * Test of currentColumnNum method, of class Parser.
     */
    @Test
    public void testCurrentColumnNum()
    {
        System.out.println("currentColumnNum");
        
        var testFileName    = "junit4_fake_test_number1_the_larch.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        var expResult       = 5;
        
        var testTokens
                = builder
                .addToken(1, 1, "ive")
                .addToken(1, expResult, "haikus")
                .addTokens("for days,\n" +
                           "but this testing needs finished\n" +
                           "something something words")
                .build();
        
        var instance = new Parser(testTokens);

        instance.nextToken(2);
        
        var result = instance.currentColumnNum();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of the nextToken method, of class Parser.
     */
    @Test
    public void testNextToken_int_ReturnsSuccessResult()
    {
        System.out.println("nextToken(int tokensToAdvance)");
        
        var testFileName    = "junit4_fake_test_number2_the_larch.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addTokens("made nice tester func\n" +
                           "and im still making haikus\n" +
                            "will dream of haikus")
                .build();
        
        var instance        = new Parser(testTokens);
        var tokensToAdvance = 2;
        var expResult       = true;
        var result          = instance.nextToken(tokensToAdvance)
                                .hasSucceeded();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of the nextToken method, of class Parser.
     */
    @Test
    public void testNextToken_int_AdvancesToCorrectToken()
    {
        System.out.println("nextToken(int tokensToAdvance)");
        
        var testFileName    = "junit4_fake_test_number22_the_larch.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addTokens("made nice tester func\n" +
                           "dreaming of haikus tonight\n" +
                           "am kind of hungry")
                .build();
        
        var instance        = new Parser(testTokens);
        var tokensToAdvance = 2;
        var expResult       = "nice";
        
        instance.nextToken(tokensToAdvance);
                
        var result = instance.currentTokenText();

        assertEquals(expResult, result);
    }
    
    /**
     * Test of currentTokenText method, of class Parser.
     */
    @Test
    public void testCurrentTokenText()
    {
        System.out.println("currentTokenText");
        
        var testFileName    = "junit4_fake_test_number3_the_larch.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addTokens("how many more tests\n" +
                           "can there possibly be, huh?\n" +
                           "haikus getting old")
                .build();
        
        var instance    = new Parser(testTokens);
        var expResult   = "more";
        
        instance.nextToken(3);
        
        var result = instance.currentTokenText();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCurrentToken method, of class Parser.
     */
    @Test
    public void testSetCurrentToken()
    {
        System.out.println("setCurrentToken");
        
        var testFileName    = "junit4_fake_test_number3_the_larch.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        var expResult
                = new Token(testFileName, 1, 10, "baguette");
        
        var testTokens
                = builder
                .addTokens("1 french")
                .addToken(expResult)
                .addTokens("half cup pizza sauce\n" +
                           "two thirds cup shredded mozzarella cheese\n" +
                           "two ounces pepperoni")
                .build();
        
        var index = 2;
        var instance = new Parser(testTokens);
        
        instance.setCurrentToken(index);
        
        var result = instance.currentToken();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getTokenAt method, of class Parser.
     */
    @Test
    public void testGetTokenAt()
    {
        System.out.println("getTokenAt");
        
        var testFileName    = "junit4_fake_test.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        var expResult
                = new Token(testFileName, 1, 18, "lunch");
        
        var testTokens
                = builder
                .addTokens("I am hungry, but")
                .addToken(expResult)
                .addTokens("helps")
                .build();
        
        var index = 5;
        var instance = new Parser(testTokens);
        var result = instance.getTokenAt(index);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of nextToken() method, of class Parser.
     */
    @Test
    public void testNextToken_noArgs()
    {
        System.out.println("nextToken()");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var expResult
                = new Token(testFileName, 1, 17, "huel");
        
        var testTokens
                = builder
                .addTokens("I am hungry, but")
                .addToken(expResult)
                .addTokens("helps")
                .build();
        
        var indexBeforeToken = 4;
        var instance = new Parser(testTokens);
        
        instance.setCurrentToken(indexBeforeToken);
        
        instance.nextToken();
        
        var result = instance.currentToken();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of nextToken(int) method, of class Parser.
     */
    @Test
    public void testNextToken_intArg()
    {
        System.out.println("nextToken(int tokensToAdvance)");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var expResult
                = new Token(testFileName, 1, 18, "pizza");
        
        var testTokens
                = builder
                .addTokens("I would like some")
                .addToken(expResult)
                .addTokens("tbh")
                .build();
        
        var tokensToAdvance = 4;
        var instance = new Parser(testTokens);
        
        instance.nextToken(tokensToAdvance);
        
        var result = instance.currentToken();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of advanceParser method, of class Parser.
     */
    @Test
    public void testAdvanceParser_validIndexReturnsSuccessCode()
    {
        System.out.println("advanceParser - returns success code");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addTokens("Buffalo chicken dip sounds good right now")
                .build();
        
        Parser instance
                = new Parser(testTokens);
        var expResult
                = true;
        var result
                = instance
                .advanceParser()
                .hasSucceeded();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of advanceParser method, of class Parser.
     */
    @Test
    public void testAdvanceParser_validIndexAdvancesParser()
    {
        System.out.println("advanceParser - actually advances parser");
        
        var testFileName
                = "junit4_fake_test.txt";
        var expResult
                = new Token(testFileName, 1, 1, "Jalepeno");
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addToken(expResult)
                .addTokens("poppers sound good right now")
                .build();
        
        Parser instance
                = new Parser(testTokens);
        
        instance.advanceParser();
        
        var result
                = instance.currentToken();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of advanceParser method, of class Parser.
     */
    @Test
    public void testAdvanceParser_invalidIndexReturnsErrorCode()
    {
        System.out.println(
            "advanceParser - error code is returned when at EOF");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addTokens("Food sounds good right now")
                .build();
        
        Parser instance
                = new Parser(testTokens);
        
        var lastTokenIndex
                = 4;
        
        // add 1 to account for the "EOF" token
        instance.setCurrentToken(lastTokenIndex + 1);
        
        var expResult
                = AdvanceParserErrCode.AT_EOF;
        var result
                = instance.advanceParser();
        
        assertEquals(expResult, result.errCode());
    }
    
    /**
     * Test of atEOF method, of class Parser.
     */
    @Test
    public void testAtEOF_atEofReturnsTrue()
    {
        System.out.println("atEOF");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addTokens("testing atEOF function")
                .build();
        
        Parser instance
                = new Parser(testTokens);
        
        var lastTokenIndex
                = 2;
        instance.setCurrentToken(lastTokenIndex);
        
        var expResult
                = true;
        var result
                = instance.atEOF();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of atEOF method, of class Parser.
     */
    @Test
    public void testAtEOF_notAtEofReturnsFalse()
    {
        System.out.println("atEOF");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addTokens("testing atEOF function")
                .build();
        
        var instance
                = new Parser(testTokens);

        instance.nextToken();
        
        var expResult
                = false;
        var result
                = instance.atEOF();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of atEOF method, of class Parser.
     */
    @Test
    public void testAtEOF_offsetEofReturnsTrue()
    {
        System.out.println("atEOF");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addTokens("testing atEOF function\n" +
                           "with a multi-line test file")
                .build();
        
        var instance
                = new Parser(testTokens);
        
        // Not a mistake -- this isn't supposed to advance all the way
        //  to the EOF token.
        instance.nextToken(2);
        
        var expResult
                = true;
        var result
                = instance.atEOF(7);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of atEndOfLine method, of class Parser.
     */
    @Test
    public void testAtEndOfLine_notAtEolReturnsFalse()
    {
        System.out.println("atEndOfLine");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addTokens("testing atEOL function on a\n" +
                           "multi-line token array which\n" +
                           "spans several lines")
                .build();
        
        var instance
                = new Parser(testTokens);
        var expResult
                = false;
        
        instance.nextToken(2);
        
        var result
                = instance.atEndOfLine();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of atEndOfLine method, of class Parser.
     */
    @Test
    public void testAtEndOfLine_atEolReturnsTrue()
    {
        System.out.println("atEndOfLine");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addTokens("testing atEOL function on a\n" +
                           "multi-line token array which\n" +
                           "spans several lines")
                .build();
        
        var instance
                = new Parser(testTokens);
        var expResult
                = true;
        
        instance.nextToken(4);
        
        var result
                = instance.atEndOfLine();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of atEndOfLine method, of class Parser.
     */
    @Test
    public void testAtEndOfLine_atEofReturnsTrue()
    {
        System.out.println("atEndOfLine");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addTokens("testing atEOL function on a\n" +
                           "multi-line token array which\n" +
                           "is spanning several lines")
                .build();
        
        var instance
                = new Parser(testTokens);
        var expResult
                = true;
        
        instance.nextToken(14);
        
        var result
                = instance.atEndOfLine();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of atStartOfLine method, of class Parser.
     */
    @Test
    public void testAtStartOfLine_sofReturnsTrue()
    {
        System.out.println("atStartOfLine");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addTokens("testing atEOL function on a\n" +
                           "multi-line token array which\n" +
                           "spans several lines")
                .build();
        
        var instance
                = new Parser(testTokens);
        var expResult
                = true;
        var result
                = instance.atStartOfLine();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of atStartOfLine method, of class Parser.
     */
    @Test
    public void testAtStartOfLine_notAtSolReturnsFalse()
    {
        System.out.println("atStartOfLine");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addTokens("testing atEOL function on a\n" +
                           "multi-line token array which\n" +
                           "spans several lines")
                .build();
        
        var instance
                = new Parser(testTokens);
        var expResult
                = false;
        
        instance.nextToken(2);
        
        var result
                = instance.atStartOfLine();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of atStartOfLine method, of class Parser.
     */
    @Test
    public void testAtStartOfLine_atSolReturnsTrue()
    {
        System.out.println("atStartOfLine");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addTokens("testing atEOL function on a\n" +
                           "multi-line token array which\n" +
                           "spans several lines")
                .build();
        
        var instance
                = new Parser(testTokens);
        var expResult
                = true;
        
        instance.nextToken(5);
        
        var result
                = instance.atStartOfLine();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of castToNextTokenError method, of class Parser.
     */
    @Test
    public void testCastToNextTokenError()
    {
        System.out.println("castToNextTokenError");
        
        var advanceResult
                = new Result<AdvanceParserErrCode>();
        var expResult
                = new Result<NextTokenErrCode>();
        
        advanceResult.errCode(AdvanceParserErrCode.AT_EOF);
        expResult.errCode(NextTokenErrCode.AT_EOF);
        
        var result
                = Parser.castToNextTokenError(advanceResult);
        
        assertEquals(expResult.errCode(), result.errCode());
    }

    /**
     * Test of parse method, of class Parser.
     */
    @Test
    public void testParse()
    {
        System.out.println("parse");
        
        var testFileName
                = "junit4_fake_test.txt";
        var builder
                = new LexedTokenBuilder(testFileName);
        var testTokens
                = builder
                .addTokens("I am no longer hungry after eating lunch")
                .build();
        
        Parser instance
                = new Parser(testTokens);
        
        /*
         * Currently, we expect the parser to fail, as no sub-parsers are
         * implemented yet This will change in the near future.
         */
        var expResult
                = ParseResultErrCode.FATAL_UNKNOWN_ERROR;
        var result
                = instance.parse();
        
        assertEquals(expResult, result.errCode());
    }

    /**
     * Test of currentTokenIndex method, of class Parser.
     */
    @Test
    public void testCurrentTokenIndex()
    {
        System.out.println("currentTokenIndex");
        Parser instance = null;
        int expResult = 0;
        int result = instance.currentTokenIndex();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of currentToken method, of class Parser.
     */
    @Test
    public void testCurrentToken()
    {
        System.out.println("currentToken");
        Parser instance = null;
        Token expResult = null;
        Token result = instance.currentToken();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isMultilineStmt method, of class Parser.
     */
    @Test
    public void testIsMultilineStmt()
    {
        System.out.println("isMultilineStmt");
        Parser instance = null;
        boolean expResult = false;
        boolean result = instance.isMultilineStmt();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
