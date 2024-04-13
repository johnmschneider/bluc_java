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
package bluc_java;

import bluc_java.parser.Parser;
import bluc_java.parser.expressions.Expr;
import bluc_java.parser.expressions.ExprPrinter;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Main class of the program.
 */
public class Bluc
{
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            Bluc.printHelp(System.out);
            return;
        }
        
        Bluc.runParserTests();
        
        var equalsIndex = args[0].indexOf("=");
        var filePath = args[0].substring(equalsIndex + 1);
        var lexer = new Lexer();
        
        var lexResult = lexer.lexFile(filePath);
        
        if (lexResult.hasFailed())
        {
            return;
        }
        
        var tokens = lexResult.data();
        
        Bluc.printLexerOutput(tokens);
        
        var parser = new Parser(tokens);

        parser.parse();
    }
    
    private static void runParserTests()
    {
        System.out.println("==== Parser tests ====");
        
        var expression = new Expr.Binary(
            new Expr.Unary(
                new Token(null, -1, -1, "-"),
                new Expr.Literal(
                    new Token(null, -1, -1, "123"))),
            new Token(null, -1, -1, "*"),
            new Expr.Grouping(
                null,
                new Expr.Literal(
                    new Token(null, -1, 01, "45.67")),
                null));
        
        var astAsString = new ExprPrinter().printToString(expression);
        System.out.println("\n\nAST:\n" + astAsString + "\n\n");

        System.out.println("==== end of Parser tests ====\n");
    }
    
    private static void printLexerOutput(ArrayList<Token> tokens)
    {
        var output = "Lexer output is ...< ";
        
        for (var token : tokens)
        {
            output += "`" + token.text() + "`, ";
        }
        
        output += ">...";
        
        System.out.println(output);
    }
    
    
    private static void printHelp(PrintStream out)
    {
        out.println("-f= flag: \t\t-f=fileNameHere\t\twhere `fileNameHere` " +
                "is the file to compile");
    }
}
