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
    public boolean canParseCurrentExpression()
    {
        var parser
                = this.parser();
        return parser.currentTokenMatches("(");
    }

    /**
     * Parses the current expression. <br/><br/>
     * 
     * <b>Pre-condition:</b> This method assumes that {@link #canParseCurrentExpression()} is true.
     * 
     * @return The parsed expression, or an error code if the expression could not be parsed.
     */
    @Override
    public ExprParserResult parse()
    {
        var result
                = new ExprParserResult();
        var parser
                = this.parser();

        var openParenthesis
                = parser.currentToken();

        return result;
    }

    @Override
    public ExprSubParser createNewSubParser(Parser parser, ExprParser exprParser)
    {
        return new GroupingSubParser(parser, exprParser);
    }
}