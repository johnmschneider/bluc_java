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
package bluc_java.parser.expressions;

import bluc_java.IUnitTest;
import bluc_java.parser.LexedTokenBuilder;
import bluc_java.parser.Parser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the GroupingSubParser class, of the bluc_java.parser.expressions package.
 * @author John
 */
public class GroupingSubParserTest implements IUnitTest
{
    public GroupingSubParserTest()
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
     * Test of canStartParsingOnThisToken method, of class GroupingSubParser.
     */
    @Test
    public void testCanStartParsingOnThisToken_validTokenReturnsTrue()
    {
        System.out.println("canStartParsingOnThisToken - valid token returns true");

        var testFileName    = "junit4_fake_test_file.txt";
        var builder         = new LexedTokenBuilder(testFileName);
        
        var testTokens
                = builder
                .addTokens("( a )")
                .build();

        var parser      = new Parser(testTokens);
        var exprParser  = new ExprParser(parser);
        var instance    = new GroupingSubParser(parser, exprParser);
        var expResult   = true;

        parser.
        // Move off of the SOF token.
        parser.nextToken();

        var result = instance.canStartParsingOnThisToken();

        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class GroupingSubParser.
     */
    @Test
    public void testParse()
    {
        System.out.println("parse");
        GroupingSubParser instance = null;
        ExprSubParser.ExprParserResult expResult = null;
        ExprSubParser.ExprParserResult result = instance.parse();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createNewSubParser method, of class GroupingSubParser.
     */
    @Test
    public void testCreateNewSubParser()
    {
        System.out.println("createNewSubParser");
        Parser parser = null;
        ExprParser exprParser = null;
        GroupingSubParser instance = null;
        ExprSubParser expResult = null;
        ExprSubParser result = instance.createNewSubParser(parser, exprParser);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
