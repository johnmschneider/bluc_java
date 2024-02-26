/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package bluc_java.parser;

import bluc_java.Lexer;
import bluc_java.Token;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 *  Builder class to create a list of lexed tokens (ArrayList<Token>).
 * @author john
 */
public class LexedTokenBuilder
{
    /**
     * Gets or sets the fake name to use as the tokens' file name, for testing.
     */
    @Getter
    @Setter
    private String testFileName;
    
    /**
     * Gets or sets a list of all the tokens that have been lexed so far.
     * 
     * This should NOT include the SOF or EOF tokens, as these are added by
     *  the "build" function automatically.
     */
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private ArrayList<Token> lexedTokens;
    
    /**
     * Initializes a new instance of the LexedTokenBuilder class.
     * 
     * @param testFileName - the fake name to use as the tokens' file name,
     *  for testing
     */
    public LexedTokenBuilder(String testFileName)
    {
        this.testFileName = testFileName;
        this.lexedTokens = new ArrayList<Token>();
    }
    
    /**
     * Appends a new token to the end of the lexed tokens list.
     * 
     * @param lineNum - the line number of the new token
     * @param columnNum - the column number of the new token
     * @param text - the text of the new token
     */
    public LexedTokenBuilder addToken(int lineNum, int columnNum, String text)
    {
        var newToken
            = new Token(this.testFileName, lineNum, columnNum, text);
        
        this.lexedTokens()
            .add(newToken);
        
        return this;
    }
    
    /**
     * Appends a new token to the end of the lexed tokens list.
     * 
     * @param token - the token to append
     */
    public LexedTokenBuilder addToken(Token token)
    {
        this.lexedTokens()
            .add(token);
        
        return this;
    }
    
    /**
     * Runs the specified string through the lexer, as though it were code, and
     *  appends the resulting tokens to this builder's lexed tokens list. Each
     *  newline character encountered iterates the following tokens to the next
     *  line.
     */
    public LexedTokenBuilder addTokens(String tokensToAdd)
    {
        var unlexedLines
                = Arrays.asList(tokensToAdd.split("\n"));
        var newLexedTokens
                = new Lexer()
                .lexFile(unlexedLines);
        
        // Remove the SOF and EOF tokens, since we already add them in this
        //  builder automatically. This must be done because we may potentially
        //  be using just the "addToken" function, which doesn't include EOF/SOF
        //  tokens, so they're added in the build function.
        newLexedTokens = this.removeSofAndEof(newLexedTokens);
        newLexedTokens = this.addOffsetToLineNumbers(newLexedTokens);
        
        lexedTokens.addAll(newLexedTokens);
        
        return this;
    }
    
    /**
     * Removes the SOF and EOF tokens from the specified list of tokens.
     * 
     * @param lexedTokens - the list to remove the tokens from.
     */
    private ArrayList<Token> removeSofAndEof(ArrayList<Token> lexedTokens)
    {
        lexedTokens.remove(0);
        lexedTokens.remove(lexedTokens.size() - 1);
        
        return lexedTokens;
    }
    
    /**
     * Calculates and adds the line offset (largest line number already in
     *  the list) to the rest of the tokens that were just generated.
     * 
     * @param newLexedTokens - the tokens to add the line offset to
     */
    private ArrayList<Token> addOffsetToLineNumbers(
        ArrayList<Token> newLexedTokens)
    {
        /*
         * Not a mistake -- we want to get the previous line number of the
         *  *current* lexedTokens list, not the newLexedTokens list.
         */
        var lineOffset = this.findPreviousLineNumber(this.lexedTokens());
        
        for (var token : newLexedTokens)
        {
            var newLineNum = token.lineNum() + lineOffset;
            
            token.lineNum(newLineNum);
        }
        
        return newLexedTokens;
    }
    
    /**
     * Finds the line number of the previous token in the specified list of
     *  tokens.
     * 
     * @return the line number of the previous token, or 0 if there are no
     *  tokens in the array.
     */
    private int findPreviousLineNumber(ArrayList<Token> tokens)
    {
        if (tokens.isEmpty())
        {
            return 0;
        }
        
        // The last index of a token other than the EOF token.
        var lastTokenIndex = tokens.size() - 2;
        
        if (lastTokenIndex < 0)
        {
            lastTokenIndex = 0;
        }
        
        return tokens.get(lastTokenIndex).lineNum();
    }
    
    /**
     * Build the lexed token list.
     * 
     * @return a deep copy of the current lexed tokens ArrayList.
     */
    public ArrayList<Token> build()
    {
        var deepCopiedList = new ArrayList<Token>();
        var lexedTokens = this.lexedTokens();
        
        deepCopiedList.add(Token.BLUC_SOF);
        
        for (var token : lexedTokens)
        {
            deepCopiedList.add(token.deepCopy());
        }
        
        deepCopiedList.add(Token.BLUC_EOF);
        
        return deepCopiedList;
    }
    
    /**
     * Clears the current buffer of lexed tokens.
     */
    public void clearBuffer()
    {
        this.lexedTokens().clear();
    }
}
