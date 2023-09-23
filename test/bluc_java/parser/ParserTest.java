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
        Parser instance = null;
        String expResult = "";
        String result = instance.getCurrentTokenText();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of peek method, of class Parser.
     */
    @Test
    public void testPeek()
    {
        System.out.println("peek");
        int offset = 0;
        Parser instance = null;
        Token expResult = null;
        Token result = instance.peek(offset);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of nextTokenMatches method, of class Parser.
     */
    @Test
    public void testNextTokenMatches_String()
    {
        System.out.println("nextTokenMatches");
        String textToMatch = "";
        Parser instance = null;
        boolean expResult = false;
        boolean result = instance.nextTokenMatches(textToMatch);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of nextTokenMatches method, of class Parser.
     */
    @Test
    public void testNextTokenMatches_StringArr()
    {
        System.out.println("nextTokenMatches");
        String[] textsToMatch = null;
        Parser instance = null;
        boolean expResult = false;
        boolean result = instance.nextTokenMatches(textsToMatch);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of peekMatches method, of class Parser.
     */
    @Test
    public void testPeekMatches_int_String()
    {
        System.out.println("peekMatches");
        int offset = 0;
        String textToMatch = "";
        Parser instance = null;
        boolean expResult = false;
        boolean result = instance.peekMatches(offset, textToMatch);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of peekMatches method, of class Parser.
     */
    @Test
    public void testPeekMatches_int_StringArr()
    {
        System.out.println("peekMatches");
        int offset = 0;
        String[] textsToMatch = null;
        Parser instance = null;
        boolean expResult = false;
        boolean result = instance.peekMatches(offset, textsToMatch);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of currentTokenMatches method, of class Parser.
     */
    @Test
    public void testCurrentTokenMatches_StringArr()
    {
        System.out.println("currentTokenMatches");
        String[] textsToMatch = null;
        Parser instance = null;
        boolean expResult = false;
        boolean result = instance.currentTokenMatches(textsToMatch);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of currentTokenMatches method, of class Parser.
     */
    @Test
    public void testCurrentTokenMatches_String()
    {
        System.out.println("currentTokenMatches");
        String textToMatch = "";
        Parser instance = null;
        boolean expResult = false;
        boolean result = instance.currentTokenMatches(textToMatch);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of currentLineNum method, of class Parser.
     */
    @Test
    public void testCurrentLineNum()
    {
        System.out.println("currentLineNum");
        Parser instance = null;
        int expResult = 0;
        int result = instance.currentLineNum();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of currentColumnNum method, of class Parser.
     */
    @Test
    public void testCurrentColumnNum()
    {
        System.out.println("currentColumnNum");
        Parser instance = null;
        int expResult = 0;
        int result = instance.currentColumnNum();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of currentTokenText method, of class Parser.
     */
    @Test
    public void testCurrentTokenText()
    {
        System.out.println("currentTokenText");
        Parser instance = null;
        String expResult = "";
        String result = instance.currentTokenText();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCurrentToken method, of class Parser.
     */
    @Test
    public void testSetCurrentToken()
    {
        System.out.println("setCurrentToken");
        int index = 0;
        Parser instance = null;
        instance.setCurrentToken(index);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTokenAt method, of class Parser.
     */
    @Test
    public void testGetTokenAt()
    {
        System.out.println("getTokenAt");
        int index = 0;
        Parser instance = null;
        Token expResult = null;
        Token result = instance.getTokenAt(index);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of nextToken method, of class Parser.
     */
    @Test
    public void testNextToken()
    {
        System.out.println("nextToken");
        Parser instance = null;
        Result<Parser.NextTokenErrCode> expResult = null;
        Result<Parser.NextTokenErrCode> result = instance.nextToken();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of advanceParser method, of class Parser.
     */
    @Test
    public void testAdvanceParser()
    {
        System.out.println("advanceParser");
        Parser instance = null;
        Result<Parser.AdvanceParserErrCode> expResult = null;
        Result<Parser.AdvanceParserErrCode> result = instance.advanceParser();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of atEOF method, of class Parser.
     */
    @Test
    public void testAtEOF()
    {
        System.out.println("atEOF");
        Parser instance = null;
        boolean expResult = false;
        boolean result = instance.atEOF();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of atEndOfLine method, of class Parser.
     */
    @Test
    public void testAtEndOfLine()
    {
        System.out.println("atEndOfLine");
        Parser instance = null;
        boolean expResult = false;
        boolean result = instance.atEndOfLine();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of atStartOfLine method, of class Parser.
     */
    @Test
    public void testAtStartOfLine()
    {
        System.out.println("atStartOfLine");
        Parser instance = null;
        boolean expResult = false;
        boolean result = instance.atStartOfLine();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of castToNextTokenError method, of class Parser.
     */
    @Test
    public void testCastToNextTokenError()
    {
        System.out.println("castToNextTokenError");
        Result<Parser.AdvanceParserErrCode> advanceResult = null;
        Result<Parser.NextTokenErrCode> expResult = null;
        Result<Parser.NextTokenErrCode> result = Parser.castToNextTokenError(advanceResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parse method, of class Parser.
     */
    @Test
    public void testParse()
    {
        System.out.println("parse");
        Parser instance = null;
        Parser.ParseResult expResult = null;
        Parser.ParseResult result = instance.parse();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of lexedTokens method, of class Parser.
     */
    @Test
    public void testLexedTokens_0args()
    {
        System.out.println("lexedTokens");
        Parser instance = null;
        ArrayList<Token> expResult = null;
        ArrayList<Token> result = instance.lexedTokens();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of lexedTokens method, of class Parser.
     */
    @Test
    public void testLexedTokens_ArrayList()
    {
        System.out.println("lexedTokens");
        ArrayList<Token> lexedTokens = null;
        Parser instance = null;
        Parser expResult = null;
        Parser result = instance.lexedTokens(lexedTokens);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
