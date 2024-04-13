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

import bluc_java.parser.Parser;
import bluc_java.parser.statements.StmtType;
import static bluc_java.parser.expressions.Expr.Grouping;

/**
 * A sub-parser for parsing "grouping" (parenthesis) expressions.
 */
public class GroupingSubParser extends ExprSubParser
{
    public GroupingSubParser(Parser parser, ExprParser exprParser)
    {
        super(parser, exprParser, 1);
    }
    
    @Override
    public boolean canStartParsingOnThisToken()
    {
        var parser  = this.parser();

        if (parser.currentTokenMatches("("))
        {
            return false;
        }

        var parentNodeType
                = parser
                .getNodeThatsBeingProcessed()
                .type();

        // checks if the parenthesis is in a valid location
        // for this symbol to be a grouping expression
        var symbolIsInValidLocation = false;
        switch (parentNodeType)
        {
            case CLASS_FIELD:

            // methods / method-like things
            case METHOD_BLOCK:
            case LAMBDA_BLOCK:

            // constructors
            case CONSTRUCTOR_BLOCK:
            case STATIC_CONSTRUCTOR_BLOCK:
            
            // error type blocks
            case ATTEMPT_BLOCK:
            case CATCH_BLOCK:

            // branches
            case IF_CONDITION:
            case IF_BLOCK:
            case ELSE_BLOCK:
            case ELSE_IF_CONDITION:
            case ELSE_IF_BLOCK:

            // loops
            case WHILE_CONDITION:
            case WHILE_BLOCK:
            case FOR_CONDITION:
            case FOR_BLOCK:
                symbolIsInValidLocation = true;
                break;


            default:
                symbolIsInValidLocation = false;
                break;
        }

        return symbolIsInValidLocation;
    }

    /**
     * Parses the current expression. <br/><br/>
     * 
     * <b>Pre-conditions:</b> This method assumes that {@link #canStartParsingOnThisToken()} is true. <br/><br/>
     * 
     * <b>Post-conditions:</b> The parser will be on the next token after the
     *  statement IFF the statement was successfully parsed.
     * 
     * @return The parsed expression, or an error code if the expression could not be parsed.
     */
    @Override
    public ExprParserResult parse()
    {
        var result      = new ExprParserResult();
        var parser      = this.parser();
        var exprParser  = this.exprParser();

        // Assume this token is valid, as "canStartParsingOnThisToken" should have been called first.
        var openParenthesis = parser.consumeCurrentToken("(").data();

        // Parse the expression inside the parenthesis
        var innerExprResult = exprParser.tryParseExpr();
        var innerExprHasParseError = innerExprResult.hasFailed();

        if (innerExprHasParseError)
        {
            result.errCode(innerExprResult.errCode());
            return result;
        }

        var closingParenthesisResult = parser.consumeCurrentToken(")");
        var closingParenthesisNotFound = closingParenthesisResult.hasFailed();

        if (closingParenthesisNotFound)
        {
            result.errCode(ExprSubParserErrCode.MISSING_CLOSING_PARENTHESIS);
            return result;
        }
        
        result.data(new Grouping(
                        openParenthesis,
                        innerExprResult.data(),
                        closingParenthesisResult.data()));
        
        return result;
    }


    @Override
    public ExprSubParser createNewSubParser(Parser parser, ExprParser exprParser)
    {
        return new GroupingSubParser(parser, exprParser);
    }
}