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

import bluc_java.parser.statements.StmtSubparser;
import bluc_java.parser.statements.StmtType;
import bluc_java.LogFormatter;
import bluc_java.Result;
import bluc_java.ResultType;
import bluc_java.Token;
import bluc_java.Utils;
import bluc_java.parser.statements.Stmt;
import java.util.ArrayList;
import lombok.AccessLevel;
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
    private Token currentToken;
    
    /**
     * The abstract syntax tree.
     */
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private ArrayList<Stmt> ast;
    
    /**
     * "True" if the .parse function was already called and has completed.
     * 
     * Parser objects are single-use so that we don't have to deep copy the AST.
     */
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private boolean parserAlreadyRan;
    
    /**
     * The sub-parser for statements.
     */
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private StmtSubparser stmtParser;
    
    public Parser(ArrayList<Token> lexedTokens)
    {
        this.lexedTokens        = lexedTokens;
        this.ast                = new ArrayList<>();
        this.currentTokenIndex  = 0;
        this.currentToken       = this.lexedTokens.get(0);
        this.stmtParser         = new StmtSubparser(this);
    }
    
    public String getCurrentTokenText()
    {
        return this.currentToken().text();
    }
    
    /**
     * Returns the token at curTokenIndex + offset.
     * 
     * It's valid for the offset to be negative to get previous
     *  tokens.
     * 
     * If the token specified is before the first token or after the last token,
     *  then a SOF or EOF token is returned, respectively.
     * 
     * @return The token, BLUC_SOF if curTokenIndex + offset is less than
     *  0, or BLUC_EOF if curTokenIndex + offset is greater than or equal
     *  to lexedTokens.size().
     */
    public Token peek(int offset)
    {
        var lexedTokens = this.lexedTokens();
        
        // The index of the last element in lexedTokens.
        var lexedTokensLastIndex = lexedTokens.size() - 1;
        var peekedTokenIndex = this.currentTokenIndex() + offset;
        
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
    public boolean nextTokenMatches(String textToMatch)
    {
        return this.nextTokenMatches(new String[]{textToMatch});
    }
    
    /**
     * Returns true if peek(1) matches *any* string in textsToMatch.
     */
    public boolean nextTokenMatches(String[] textsToMatch)
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
        return this.currentToken().matchesAny(textsToMatch);
    }
    
    /**
     * Returns true if the current token matches the given string.
     */
    public boolean currentTokenMatches(String textToMatch)
    {
        return this.currentToken().matches(textToMatch);
    }
    
    /**
     * Returns the current token's line number.
     */
    public int currentLineNum()
    {
        return this.currentToken().lineNum();
    }
    
    /**
     * Returns the column number of the current token.
     */
    public int currentColumnNum()
    {
        return this.currentToken().columnNum();
    }
    
    /**
     * Returns the text of the current token.
     */
    public String currentTokenText()
    {
        return this.currentToken().text();
    }
    
    /**
     * Sets the current token to the token at index, and the current index
     *  to the index specified.
     */
    public void setCurrentToken(int index)
    {
        var lexedTokens = this.lexedTokens();
        var startLineNum = this.currentToken().lineNum();
        
        index = this.wrapTokenIndex(index);
        
        this.currentTokenIndex(index);
        this.currentToken = lexedTokens.get(index);
    }
    
    /**
     * Clamps the index so that it's not less than 0 or greater than
     *  lexedTokens.size() - 1.
     */
    private int wrapTokenIndex(int index)
    {
        var lexedTokens = this.lexedTokens();
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
        var lexedTokens = this.lexedTokens();
        
        index = this.wrapTokenIndex(index);
        
        var token = lexedTokens.get(index);
        
        return token;
    }
    
    /**
     * Attempts to advance the parser to the next token, and returns the
     *   token the parser was on before advancing.
     * 
     * @param expectedToken The text of the token that the parser is expected to be on. If
     *      the parser is not on this token, an error is returned.
     * @return The token the parser was on before advancing.
     */
    public ConsumeResult consumeCurrentToken(String expectedToken)
    {
        var result
                = new ConsumeResult();
        var currentToken
                = this.currentToken();

        if (!currentToken.matches(expectedToken))
        {
            result.error(currentToken, ConsumeResultErrCode.TOKEN_DOESNT_MATCH);
            return result;
        }

        var nextTokenResult
                = this.nextToken();
        
        if (nextTokenResult.hasFailed())
        {
            var castedErrCode
                    = ConsumeResultErrCode
                    .castToConsumeError(
                        nextTokenResult.errCode());

            result.error(currentToken, castedErrCode);
        }
        else
        {
            result.data(currentToken);
        }
        
        return result;
    }

    /**
     * Attempts to advance the parser to the next token. This may fail if we're
     *  at the end of the file.
     * 
     * @return the result of advancing the parser, and an error code if any
     *  errors occurred.
     */
    public Result<NextTokenErrCode> nextToken()
    {
        return this.nextToken(1);
    }
    
    /**
     * Attempts to advance the parser `tokensToAdvance` number of tokens ahead.
     *  This may fail if we're at the end of the file.
     * 
     * @return the result of advancing the parser, and an error code if any
     *  errors occurred.
     */
    public Result<NextTokenErrCode> nextToken(int tokensToAdvance)
    {
        var result
            = new Result<NextTokenErrCode>();
        
        for (int i = 0; i < tokensToAdvance; i++)
        {
            var advanceResult
                = this.advanceParser();

            if (advanceResult.hasFailed())
            {
                result = Parser.castToNextTokenError(advanceResult);
                break;
            }
        }
        
        return result;
    }
    
    /**
     * Advances the parser to the next token, but doesn't perform any additional
     *  checks/handling that "nextToken" does.
     * 
     * This should generally not be used, except for classes closely related to
     *  the parser itself.
     * 
     * @return the result of running the method, and any error data (if an error
     *  was encountered).
     */
    public Result<AdvanceParserErrCode> advanceParser()
    {
        var result = new Result<AdvanceParserErrCode>();
        
        var newTokenIndex = this.currentTokenIndex + 1;
        
        var eofTokenIndex = this.lexedTokens.size() - 1;
        if (newTokenIndex >= eofTokenIndex)
        {
            result.error(this.currentToken(),
                        AdvanceParserErrCode.AT_EOF);
        }
        else
        {
            this.setCurrentToken(newTokenIndex);
        }
        
        return result;
    }
    
    /**
     * Returns "true" if the next token is the end of file token,
     * "false" otherwise.
     */
    public boolean atEOF()
    {
        return this.atEOF(1);
    }

    /**
     * Checks if the token at the specified offset from the current token is
     *  the end of file token.
     * 
     * @return Returns "true" if the token is the end of file token,
     *  "false" otherwise.
     */    
    public boolean atEOF(int offset)
    {
        return this.peek(offset) == Token.BLUC_EOF;
    }
    
    /**
     * Returns "true" if the current token is the last token of this line
     *  of the file, returns "false" otherwise.
     */
    public boolean atEndOfLine()
    {
        if (this.atEOF())
        {
            return true;
        }
        
        var peekedToken         = this.peek(1);
        var currentLineNumber   = this.currentLineNum();
        
        return peekedToken.lineNum() != currentLineNumber;
    }
    
    /**
     * @return "true" if the current token is the first token of the line,
     *  "false" otherwise.
     */
    public boolean atStartOfLine()
    {
        if (this.currentTokenIndex == 0)
        {
            return true;
        }
        
        var peekedToken         = this.peek(-1);
        var currentLineNumber   = this.currentLineNum();
        
        return peekedToken.lineNum() != currentLineNumber;
    }
    
    /**
     * Takes the error code of a Result<AdvanceParserErrCode> and converts it to
     *  a Result<NextTokenErrCode>.
     * 
     * @return The converted Result.
     */
    public static Result<NextTokenErrCode> castToNextTokenError(
        Result<AdvanceParserErrCode> advanceResult)
    {
        var result
                = new Result<NextTokenErrCode>();

        if (advanceResult.hasFailed())
        {
            var advanceErrCode 
                = advanceResult
                .errCode();

            var asNextTokenErrCode
                = NextTokenErrCode
                .castToNextTokenError(advanceErrCode);

            result.error(advanceResult.errToken(),
                        asNextTokenErrCode);
        }

        return result;
    }
    
    /**
     * Initiate the parsing of a file and any imported modules, imported
     *  modules of imported modules etc, recursively.
     * 
     * @return The result of parsing. <br/><br/>
     *  If a failure occurred, the error code is returned in the result.
     *      <br/><br/>
     *  If the parse succeeded, the AST is returned in the result.
     */
    public ParseResult parse()
    {
        var result = new ParseResult();
        
        if (this.parserAlreadyRan())
        {
            result.errCode(ParseResultErrCode.PARSER_ALREADY_RAN);
            
            return result;
        }
        
        // Advance off the "start of file" token
        this.nextToken();
        
        while (!this.atEOF())
        {
            var stmtResult = this.stmtParser().tryParseStmt();
            
            if (stmtResult.hasSucceeded())
            {
                this.ast.addAll(stmtResult.data());
            }
            else
            {
                // Log the error, but keep parsing to try and catch any
                //  additional errors. We don't currently expect this to fail
                //  during normal operation, so log it as a fatal error.
                
                var fatalMessage
                    = LogFormatter.formatCompilerError(
                        Utils.getCurrentMethodName(),
                        stmtResult.errCode().formattedMessage());
                
                System.err.println(fatalMessage);
                
                result.error(
                    this.currentToken(), 
                    ParseResultErrCode.FATAL_UNKNOWN_ERROR);
            }
            
            this.nextToken();
        }
        
        var debugMessage
            = LogFormatter.formatDebug(
                Utils.getCurrentMethodName(),
                "ast ==\n" + this.ast);
        
        System.out.println(debugMessage);
        
        
        result.data(this.ast);
        
        return result;
    }
    
    /**
     * Class for storing the result of the parser function. Shorthand for
     *  ResultType<ParseResultErrCode, ArrayList<Stmt>>.
     */
    public static class ParseResult
            extends ResultType<ParseResultErrCode, ArrayList<Stmt>>
    {

    }
    
    public static enum ParseResultErrCode
    {
        /**
         * Indicates that the .parse function was already called and has
         *   completed.
         * 
         * Parser objects are single-use so that we don't have to deep copy the
         *   AST.
         */
        PARSER_ALREADY_RAN,
        
        /**
         * Indicates a fatal compiler error that occurred as a result of an
         *  unknown ResultType failure code.
         */
        FATAL_UNKNOWN_ERROR,
    }
    
    public static enum NextTokenErrCode
    {
        /**
         * Indicates that the parser reached the end of the file when it was
         *  expecting more tokens.
         */
        AT_EOF,
        
        /**
         * Indicates a fatal compiler error that occurred as a result of an
         *  unknown ResultType failure code.
         */
        FATAL_UNKNOWN_ERROR;
        
        @Getter
        @Setter
        private String errorMessage;
        
        public static NextTokenErrCode castToNextTokenError(
            AdvanceParserErrCode error)
        {
            NextTokenErrCode castedError;
            
            switch (error)
            {
                case AT_EOF:
                    castedError =  NextTokenErrCode.AT_EOF;
                    break;
                    
                default:
                    // This should never happen, so embed the function name in
                    //  the error message.
                    var errorMessage = LogFormatter.formatCompilerError(
                        Utils.getCurrentMethodName(),
                                
                        "Unknown AdvancedParserError type `" + error.name()
                        + "`");
                    
                    castedError = NextTokenErrCode.FATAL_UNKNOWN_ERROR;
                    castedError.errorMessage(errorMessage);
                    
                    break;
            }
            
            return castedError;
        }
    }
    
    public enum AdvanceParserErrCode
    {
        AT_EOF;
    }

    public static enum ConsumeResultErrCode
    {
        /**
         * Indicates that the parser reached the end of the file when it was
         *  expecting more tokens.
         */
        AT_EOF,

        /**
         * Indicates that the parser's current token was not the expected token.
         */
        TOKEN_DOESNT_MATCH,

        /**
         * Indicates a fatal compiler error that occurred as a result of an
         *  unknown ResultType failure code.
         */
        FATAL_UNKNOWN_ERROR;

        @Getter
        @Setter
        private String errorMessage;

        public static ConsumeResultErrCode castToConsumeError(
            NextTokenErrCode error)
        {
            ConsumeResultErrCode castedError;
            
            switch (error)
            {       
                case AT_EOF:
                    castedError = ConsumeResultErrCode.AT_EOF;
                    castedError.errorMessage("The parser reached the end of the file when it was expecting more tokens.");
                    break;
                    
                default:
                    // This should never happen, so embed the function name in
                    //  the error message.
                    var errorMessage = LogFormatter.formatCompilerError(
                        Utils.getCurrentMethodName(),
                                
                        "Unknown NextTokenError type `" + error.name()
                        + "`");
                    
                    castedError = ConsumeResultErrCode.FATAL_UNKNOWN_ERROR;
                    castedError.errorMessage(errorMessage);
                    
                    break;
            }
            
            return castedError;
        }
    }

    /**
     * Class for storing the result of the Consume function. Shorthand for
     *  ResultType<NextTokenErrCode, Token>.
     */
    public static class ConsumeResult extends ResultType<ConsumeResultErrCode, Token>
    {

    }
}
