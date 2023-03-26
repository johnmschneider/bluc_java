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

import java.io.PrintStream;

/**
 *
 * @author john
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
        
        var equalsIndex = args[0].indexOf("=");
        var filePath = args[0].substring(equalsIndex + 1);
        var lexer = new Lexer();
        
        var tokens = lexer.lexFile(filePath);
        for (var token : tokens)
        {
            System.out.print(token.getText() + ", ");
        }
    }
    
    private static void printHelp(PrintStream out)
    {
        out.println("-f= flag: \t\t-f=fileNameHere\t\twhere fileNameHere is " +
                "the file to compile");
    }
}
