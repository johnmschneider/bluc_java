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

import bluc_java.parser.Parser;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a sub-parser of the overall expression parser.
 */
public abstract class ExprSubParser
{
    @Getter
    @Setter
    private Parser parser;
    
    @Getter
    @Setter
    private ExprParser exprParser;
    
    /**
     * The precedence of the sub-parser. Lower precedences are processed first.
     */
    @Getter
    private int precedence;
    
    public ExprSubParser(Parser parser, ExprParser exprParser)
    {
        this.parser = parser;
        this.exprParser = exprParser;
        ExprParserRegistry.registerTypeIfNotRegisteredAlready(this);
    }
    
    /**
     * Creates a new, blank sub parser.
     */
    public abstract ExprSubParser createNewSubParser(
        Parser parser, ExprParser exprParser);
    
    /**
     * Tries to parse the current tokens with the sub-parsers implementation of
     *  the parse function. Returns null if it's not successful.
     */
    public abstract Expr parse();
}
