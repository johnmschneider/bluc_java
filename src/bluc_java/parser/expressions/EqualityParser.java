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
import bluc_java.parser.expressions.Expr.Binary;

/**
 * Parses "==".
 */
public class EqualityParser extends ExprSubParser
{
    public EqualityParser(Parser parser, ExprParser exprParser)
    {
        super(parser, exprParser, 7);
    }

    @Override
    public ExprSubParser createNewSubParser(
        Parser parser,
        ExprParser exprParser)
    {
        return new EqualityParser(parser, exprParser);
    }

    @Override
    public ExprParserResult parse()
    {
        var parser
                = this.parser();
        ExprParserResult result
                = null;
        Binary data
                = null;

        if (parser.currentTokenMatches("=="))
        {
            
        }
        
        return result;
    }

    @Override
    public boolean canStartParsingOnThisToken() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
