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
import java.util.ArrayList;
import java.util.List;

/**
 * Container for storing an instance of each expression sub-parser. Used
 *  for instantiating one of each type of sub-parser.
 */
public class ExprSubparserContainer
{
    /**
     * All registered sub-parsers.
     */
    private static ArrayList<ExprSubParser> allSubParsers;
    
    static
    {
        ExprSubparserContainer.allSubParsers = new ArrayList<>();

        // Register all sub-parsers here
        var subparsers = List.of(
            new EqualityParser(null, null),
            new UnarySubParser(null, null),
            new GroupingSubParser(null, null));

        ExprSubparserContainer
            .allSubParsers
            .addAll(subparsers);
    }
    
    /**
     * Creates an ArrayList with new sub-parsers for all sub-parser types.
     */
    public ArrayList<ExprSubParser> createDefaultSubparsers(
        Parser parser,
        ExprParser exprParser)
    {
        var newSubParsers = new ArrayList<ExprSubParser>();
                
        for (var subParser : ExprSubparserContainer.allSubParsers)
        {
            newSubParsers.add(
                subParser.createNewSubParser(parser, exprParser));
        }
        
        return newSubParsers;
    }
    
    /**
     * Registers the type of the specified sub-parser (only if it hasn't been
     *  registered previously)
     */
    public static void registerTypeIfNotRegisteredAlready(
        ExprSubParser subParser)
    {
        var allSubParsers = ExprSubparserContainer.allSubParsers;
        
        if (!allSubParsers.contains(subParser))
        {
            allSubParsers.add(subParser);
        }
    }
}
