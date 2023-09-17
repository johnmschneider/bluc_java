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

import java.io.File;
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
        var absoluteFilePath = new File(filePath).getAbsolutePath();
        
        this.state.filePath(absoluteFilePath);
        
        try
        {
            var allLinesOfFile = 
                    Files.readAllLines(Paths.get(absoluteFilePath));
            
            allLinesOfFile = this.addNewlinesBackToFileContents(allLinesOfFile);
            lexedTokens = this.lex(allLinesOfFile, this.state);
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
        
        return lexedTokens;
    }
    
    /**
     * Adds newlines back to the contents of the file. This simplifies the
     *  lexing of the file, so that we can just split on white space.
     * 
     * @return The contents of the file with \n added at the
     *  end of each line.
     */
    private ArrayList<String> addNewlinesBackToFileContents(
        List<String> currentContentsOfFile)
    {
        var contentsWithNewline = new ArrayList<String>();
        
        for (var line : currentContentsOfFile)
        {
            var lineWithNewline = line + "\n";
            contentsWithNewline.add(lineWithNewline);
        }
        
        return contentsWithNewline;
    }
    
    private ArrayList<Token> lex(
        List<String> allLinesOfFile,
        LexerState state)
    {
        var commentsRemover = new CommentsRemover();
        var linesOfFile = commentsRemover.run(allLinesOfFile);
        
        state.appendLexedToken(Token.BLUC_SOF);
        
        for (var line : linesOfFile)
        {
            state.line(line);
            
            this.lexLine(this.state);
            
            state.incrementLineNum();
        }
        
        state.appendLexedToken(Token.BLUC_EOF);
        
        return state.lexedTokens();
    }
    

    
    private void lexLine(LexerState state)
    {
        state.column(1);
        
        var lineAsArray = state.line().toCharArray();
        
        for (int column = 1; column <= lineAsArray.length; column++)
        {
            var curChar = lineAsArray[column - 1];
            
            state.curChar(curChar);
            state.column(column);
            
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
        if (!state.inString())
        {
            state.appendCurCharToWordSoFar();
            state.prepareForNextToken(
                true,
                false,
                false,
                false);
        }
        else if (!state.lastCharWasEscape())
        {
            state.appendCurCharToWordSoFar();
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
        state.lastCharWasEscape(false);
        
        if (state.inString())
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
        if (state.curChar() == '\\')
        {
            state.lastCharWasEscape(true);
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
            state.checkNextToken(false);
        }
        else if (state.curCharMatchesAny(
                    new char[]{'(', ')', '{', '}', '[', ']'}))
        {
            state.appendTokenIfNotWhitespace();
            
            state.setWordSoFarToCurChar();
            state.appendTokenIfNotWhitespace();
            
            state.resetWordSoFar();
            state.checkNextToken(false);
        }
        else if (state.curCharMatchesAny(
                    new char[]{'+', '-', '*', '/', '%', '=', '!',
                        '<', '>', '|', '&', '^'}))
        {
            if (state.checkNextToken())
            {
                state.appendCurCharToWordSoFar();
                state.appendTokenIfNotWhitespace();
                
                state.resetWordSoFar();
                state.checkNextToken(false);
            }
            else
            {
                state.appendTokenIfNotWhitespace();
                
                state.setWordSoFarToCurChar();
                state.checkNextToken(true);
            }
        }
        else
        {
            state.appendCurCharToWordSoFar();
        }
    }
}
