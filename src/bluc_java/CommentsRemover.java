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

import java.util.ArrayList;
import java.util.List;

/**
 * Removes comments from a source file so we only have to process
 */
public class CommentsRemover
{
    public List<String> run(List<String> lexedLines)
    {
        var decommentedLines = new ArrayList<String>();
        
        for (var line : lexedLines)
        {
            var lineSoFar = "";
            
            for (var character : line.toCharArray())
            {
                if (character == '#')
                {
                    break;
                }
                
                lineSoFar += character;
            }
            
            decommentedLines.add(lineSoFar);
        }
        
        return decommentedLines;
    }
}
