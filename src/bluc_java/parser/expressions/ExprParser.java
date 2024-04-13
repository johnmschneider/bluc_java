/*
 * Copyright 2023 John Schneider.
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
package bluc_java.parser.expressions;

import bluc_java.ResultType;
import bluc_java.parser.Parser;
import bluc_java.parser.expressions.ExprSubParser.ExprSubParserErrCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Logic for parsing expressions.
 */
@AllArgsConstructor
public class ExprParser
{
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Parser parser;

    /**
     * Tries to parse the current expression that the parser is on. <br/><br/>
     * 
     * <b>Post-conditions:</b> The parser will be on the next token after the
     *  statement if the statement was successfully parsed.
     */
    public ExprParserResult tryParseExpr()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Class for storing the result of parsing an expression.
     * 
     * Short-hand for {@link ResultType} with the error code {@link ExprSubParserErrCode} and the result type {@link Expr}.
     */
    public static class ExprParserResult extends ResultType<ExprSubParserErrCode, Expr>
    {

    }
}
