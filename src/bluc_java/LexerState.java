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
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the current state of the lexer.
 */
public class LexerState
{
    /**
     * The tokens that we have lexed so far.
     */
    @Getter
    private ArrayList<Token> lexedTokens;
    
    /**
     * The current line that the lexer is parsing.
     */
    @Getter
    @Setter
    private String line;
    
    /**
     * The file path of the file that the lexer is parsing.
     */
    @Getter
    @Setter
    private String filePath;
    
    /**
     * The current line number that the lexer is on.
     */
    @Getter
    @Setter
    private int lineNum;
    
    /**
     * The current column that the lexer is on.
     */
    @Getter
    @Setter
    private int column;
    
    /**
     * The current character that the lexer is parsing.
     */
    @Getter
    @Setter
    private char curChar;
    
    /**
     * Whether or not the lexer is currently in a string.
     */
    @Getter
    @Setter
    private boolean isInString;
    
    /**
     * Whether or not the last character was an escape sequence for a string.
     */
    @Getter
    @Setter
    private boolean wasLastCharEscape;
    
    /**
     * Whether we are on the first char of a potentially multi-char token, and
     *  should check the next token to see if it's a multi-char token.
     */
    @Getter
    @Setter
    private boolean doCheckNextToken;
    
    /**
     * The current text of the token we've parsed so far.
     */
    @Getter
    @Setter
    private String wordSoFar;
    
    public LexerState()
    {
        this.lexedTokens = new ArrayList<>();
        this.lineNum = 1;
        this.doCheckNextToken = false;
        this.resetWordSoFar();
    }
    
    /**
     * Adds a token to <see cref="lexedTokens/>
     */
    public void appendLexedToken(Token token)
    {
        this.lexedTokens.add(token);
    }
    
    /**
     * Appends LexerState.curChar to LexerState.wordSoFar.
     */
    public void appendCurCharToWordSoFar()
    {
        this.wordSoFar(this.wordSoFar() + this.curChar());
    }
    
    /**
     * Returns true if curChar is white space, false otherwise.
     */
    public boolean curCharIsWhitespace()
    {
        return Character.isWhitespace(this.curChar());
    }
    
    /**
     * Returns whether curChar matches any chars specified in charsToMatch.
     */
    public boolean curCharMatchesAny(char[] charsToMatch)
    {
        var curChar = this.curChar();
        var charFound = false;
        
        for (var charToSearch : charsToMatch)
        {
            if (curChar == charToSearch)
            {
                charFound = true;
                break;
            }
        }
        
        return charFound;
    }
    
    /**
     * Replaces the wordSoFar with the curChar.
     */
    public void setWordSoFarToCurChar()
    {
        this.wordSoFar("" + this.curChar());
    }
    
    /**
     * Resets the wordSoFar back to "".
     */
    public void resetWordSoFar()
    {
        this.wordSoFar("");
    }
    
    /**
     * Appends the wordSoFar to lexedTokens if wordSoFar isn't just
     *  white space.
     */
    public void appendTokenIfNotWhitespace()
    {
        var wordSoFar = this.wordSoFar();
        
        if (!wordSoFar.isBlank())
        {
            var token = new Token();
            token.filePath(this.filePath());
            token.lineNum(this.lineNum());
            token.columnNum(this.column());
            token.text(wordSoFar);
            
            this.appendLexedToken(token);
        }
    }
    
    /**
     * Prepares the lexer state to lex a new token after it's just finished
     *  lexing a token.
     * 
     * It's not a requirement to call this method specifically, this is only
     *  a utility method. You can call the setters of the individual booleans
     *  if you need to.
     * @param inString - true if the lexer is currently inside of a string
     *  literal.
     * @param doCheckNextToken - true if, during lexing, we should check the
     *  following token after this one. This is in-cas we have a multi-glyph
     *  lexeme.
     * @param wasLastCharEscape - true if the last char was the "escape" char
     *  for a string (i.e., the equivalent of "\" in Java).
     * @param doResetWordSoFar - true if this function should reset the state's
     *  "wordSoFar".
     */
    public void prepareForNextToken(
        boolean inString,
        boolean doCheckNextToken,
        boolean wasLastCharEscape,
        boolean doResetWordSoFar)
    {
        this.isInString(inString);
        this.doCheckNextToken(doCheckNextToken);
        this.wasLastCharEscape(wasLastCharEscape);
        
        if (doResetWordSoFar)
        {
            this.resetWordSoFar();
        }
    }
    
    /**
     * Adds 1 to the line number.
     */
    public void incrementLineNum()
    {
        this.lineNum(this.lineNum() + 1);
    }
}
