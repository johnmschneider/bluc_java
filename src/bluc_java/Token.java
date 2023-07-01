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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents tokens generated by the lexer, for use by the parser.
 */
@NoArgsConstructor
@AllArgsConstructor
public class Token
{

    public static final Token BLUC_SOF =
        new Token(null, -1, -1, "__BLUC_SOF__");
    
    public static final Token BLUC_EOF =
        new Token(null, -1, -1, "__BLUC_EOF__");
    
    @Getter
    @Setter
    private String filePath;
    
    @Getter
    @Setter
    private int lineNum;
    
    @Getter
    @Setter
    private int columnNum;
    
    @Getter
    @Setter
    private String text;

    /**
     * Returns true if this.text .equals any string in
     *  textsToMatch.
     */
    public boolean matchesAny(String[] textsToMatch)
    {
        var matches = false;
        var tokenText = this.getText();
        
        for (var textToMatch : textsToMatch)
        {
            if (tokenText.equals(textToMatch))
            {
                matches = true;
                break;
            }
        }
        
        return matches;
    }
    
    /**
     * Returns true if this.text .equals textToMatch.
     */
    public boolean matches(String textToMatch)
    {
        return this.matchesAny(new String[]{textToMatch});
    }
}
