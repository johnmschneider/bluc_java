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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Lexes a source file into tokens, for use by the parser.
 */
public class Lexer
{
    @Getter
    @Setter
    private LexerState state;
    
    public Lexer()
    {
        this.state = new LexerState();
    }
    
    public ArrayList<Token> lexFile(String filePath)
    {
        var lexedTokens = new ArrayList<Token>();
        
        try
        {
            var allLinesOfFile = 
                    Files.readAllLines(Paths.get(filePath));
            lexedTokens = this.lex(allLinesOfFile, this.state);
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
        
        return lexedTokens;
    }
    
    private ArrayList<Token> lex(
        List<String> allLinesOfFile,
        LexerState state)
    {
        var commentsRemover = new CommentsRemover();
        
        allLinesOfFile = commentsRemover.run(allLinesOfFile);
        
        state.addLexedToken(Token.BLUC_SOF);
        
        for (var line : allLinesOfFile)
        {
            this.lexLine(this.state);
        }
        
        return state.getLexedTokens();
    }
    
    private void lexLine(LexerState state)
    {
        state.setColumn(1);
        
        for (var curChar : state.getLine().toCharArray())
        {
            state.setCurChar(curChar);
            
            if (curChar == '"')
            {
                this.lexCharWhenOnAQuote(state);
            }
            else
            {
                this.lexCharWhenNotOnAQuote(state);
            }
        }
    }
    
    private void lexCharWhenOnAQuote(LexerState state)
    {
        if (!state.isInString())
        {
            state.appendTokenIfNotWhitespace();

            state.setWordSoFarToCurChar();
            state.appendTokenIfNotWhitespace();
            
            state.prepareForNextToken(
                true,
                false,
                false,
                false);
        }
        else if (!state.isLastCharWasEscape())
        {
            state.appendTokenIfNotWhitespace();

            state.setWordSoFarToCurChar();
            state.appendTokenIfNotWhitespace();
            
            state.prepareForNextToken(
                false,
                false,
                false,
                true);
        }
    }
    
    private void lexCharWhenNotOnAQuote(LexerState state)
    {
        state.setLastCharWasEscape(false);
        
        if (state.isInString())
        {
            this.lexWhenInsideString(state);
        }
        else
        {
            this.lexWhenNotInString(state);
        }
    }
    
    private void lexWhenInsideString(LexerState state)
    {
        if (state.getCurChar() == '\\')
        {
            state.setLastCharWasEscape(true);
        }
        else
        {
            state.appendCurCharToWordSoFar();
        }
    }
    
    private void lexWhenNotInString(LexerState state)
    {
        if (state.curCharIsWhitespace())
        {
            state.appendTokenIfNotWhitespace();
            
            state.resetWordSoFar();
            state.setCheckNextToken(false);
        }
        else if (state.curCharMatchesAny(
                    new char[]{'(', ')', '{', '}', '[', ']'}))
        {
            state.appendTokenIfNotWhitespace();
            
            state.setWordSoFarToCurChar();
            state.appendTokenIfNotWhitespace();
            
            state.resetWordSoFar();
            state.setCheckNextToken(false);
        }
    }
    

}
