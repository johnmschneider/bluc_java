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
package bluc_java.parser;

import bluc_java.Result;
import bluc_java.Token;
import bluc_java.parser.statements.Stmt;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * Takes a lexed token list and parses it into an abstract syntax tree (AST).
 */
public class Parser
{
    /**
     * The token list retrieved from the lexer.
     */
    @Getter
    @Setter
    private ArrayList<Token> lexedTokens;
    
    /**
     * The index (in lexedTokens) of the current token.
     */
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private int currentTokenIndex;
            
    /**
     * The current token that the parser is processing.
     */
    @Getter
    @Setter
    private Token currentToken;
    
    /**
     * The abstract syntax tree.
     */
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private ArrayList<Stmt> ast;
    
    /**
     * True if the current token is on a line which is part of
     *  a multi-line statement.
     */
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private boolean isMultilineStmt;
            
    public Parser(ArrayList<Token> lexedTokens)
    {
        this.lexedTokens = lexedTokens;
        this.ast = new ArrayList<>();
    }
    
    public String getCurTokenText()
    {
        return this.getCurrentToken().getText();
    }
    
    public boolean curTokTextEquals(String textToMatch)
    {
        return this.getCurTokenText().equals(textToMatch);
    }
    
    /**
     * Returns the token at curTokenIndex + offset.
     * 
     * It's valid for offset to be negative to get previous
     *  tokens.
     * 
     * @return The token, BLUC_SOF if curTokenIndex + offset is less than
     *  0, or BLUC_EOF if curTokenIndex + offset is greater than or equal
     *  to lexedTokens.size().
     */
    public Token peek(int offset)
    {
        var lexedTokens = this.getLexedTokens();
        
        // The index of the last element in lexedTokens.
        var lexedTokensLastIndex = lexedTokens.size() - 1;
        var peekedTokenIndex = this.getCurrentTokenIndex() + offset;
        
        if (peekedTokenIndex < 0)
        {
            peekedTokenIndex = 0;
        }
        else if (peekedTokenIndex >= lexedTokensLastIndex)
        {
            peekedTokenIndex = lexedTokensLastIndex;
        }
        
        return lexedTokens.get(peekedTokenIndex);
    }
    
    /**
     * Returns true if peek(1) matches textsToMatch.
     */
    public boolean peekMatches(String textToMatch)
    {
        return this.peekMatches(new String[]{textToMatch});
    }
    
    /**
     * Returns true if peek(1) matches *any* string in textsToMatch.
     */
    public boolean peekMatches(String[] textsToMatch)
    {
        return this.peekMatches(1, textsToMatch);
    }
    
    /**
     * Returns true if peek(offset) matches textToMatch.
     */
    public boolean peekMatches(int offset, String textToMatch)
    {
        return this.peekMatches(offset, new String[]{textToMatch});
    }
    
    /**
     * Returns true if peek(offset) matches *any* string in textsToMatch.
     */
    public boolean peekMatches(int offset, String[] textsToMatch)
    {
        var tokenToCheck = this.peek(offset);
        return tokenToCheck.matchesAny(textsToMatch);
    }
    
    /**
     * Returns true if the current token matches *any* string in textsToMatch.
     */
    public boolean currentTokenMatches(String[] textsToMatch)
    {
        return this.getCurrentToken().matchesAny(textsToMatch);
    }
    
    /**
     * Returns the current token's line number.
     */
    public int currentLineNum()
    {
        return this.getCurrentToken().getLineNum();
    }
    
    /**
     * Returns the column number of the current token.
     */
    public int currentColumnNum()
    {
        return this.getCurrentToken().getColumnNum();
    }
    
    /**
     * Returns the text of the current token.
     */
    public String currentTokenText()
    {
        return this.getCurrentToken().getText();
    }
    
    /**
     * Sets the current token to the token at index, and the current index
     *  to the index specified.
     */
    public void setCurrentToken(int index)
    {
        var lexedTokens = this.getLexedTokens();
        var startLineNum = this.getCurrentToken().getLineNum();
        
        index = this.wrapTokenIndex(index);
        
        this.setCurrentTokenIndex(index);
        this.currentToken = lexedTokens.get(index);
        
        if (this.currentToken.getLineNum() != startLineNum)
        {
            // Reset our multi-line statement flag as we're on a
            //  different line now
            this.setMultilineStmt(false);
        }
    }
    
    /**
     * Clamps the index so that it's not less than 0 or greater than
     *  lexedTokens.size() - 1.
     */
    private int wrapTokenIndex(int index)
    {
        var lexedTokens = this.getLexedTokens();
        var endIndex = lexedTokens.size() - 1;
        
        if (index < 0)
        {
            index = 0;
        }
        else if (index > endIndex)
        {
            index = endIndex;
        }
        
        return index;
    }
    
    /**
     * Returns the token at lexedTokens.get(index).
     */
    public Token getTokenAt(int index)
    {
        var lexedTokens = this.getLexedTokens();
        
        index = this.wrapTokenIndex(index);
        
        var token = lexedTokens.get(index);
        
        return token;
    }
    
    public Result nextToken()
    {
        var outcome = new Result<String>();
        
        this.setCurrentToken(this.getCurrentTokenIndex() + 1);
        
        if (this.isMultilineStmt())
        {
            if (!this.currentTokenText().isBlank())
            {
                var errorMessage =
                    "Expected nothing or whitespace after " +
                    "multi-line separator, found `" + this.currentTokenText() +
                    "` instead.";
                
                outcome.setError(
                    this.getCurrentToken(),
                    errorMessage);
            }
            else
            {
                // TODO - continue converting the py implementation
                //  starting at line 133 of parser.py
                if (this.atEOL())
                {
                    
                }
            }
        }
    }
    
    /**
     * Returns "true" if the current token is the end of file token,
     * "false" otherwise.
     */
    public boolean atEOF()
    {
        return this.getCurrentToken() == Token.BLUC_EOF;
    }
    
    /**
     * Returns "true" if the current token is the last token of this line
     *  of the file, returns "false" otherwise.
     */
    public boolean atEOL()
    {
        if (this.atEOF())
        {
            return true;
        }
        
        var peekedToken         = this.peek(1);
        var currentLineNumber   = this.currentLineNum();
        
        return peekedToken.getLineNum() != currentLineNumber;
    }
}
