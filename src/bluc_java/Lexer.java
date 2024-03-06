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

import lombok.AllArgsConstructor;
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
    
    /**
     * Reads the file at the specified file path, then lexes the file.
     * 
     * @param filePath - the file to read and lex
     */
    public LexResult lexFile(String filePath)
    {
        var result = new LexResult();
        var lexedTokens = new ArrayList<Token>();
        var absoluteFilePath = new File(filePath).getAbsolutePath();
        
        this.state().filePath(absoluteFilePath);
        
        try
        {
            var allLinesOfFile
                    = Files.readAllLines(Paths.get(absoluteFilePath));

            var lexResult
                    = this.lexString(allLinesOfFile, this.state());

            if (lexResult.hasFailed())
            {
                System.out.println(lexResult.errCode().errorMessage());
                
                result.errCode(lexResult.errCode());

                return result;
            }

            lexedTokens
                    = lexResult.data();
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
        
        result.data(lexedTokens);

        return result;
    }
    
    /**
     * Returns the lexed tokens given the specified file contents.<br/><br/>
     * 
     * <b>Remarks:</b><br/>&#9;
     *      This is method is currently only intended for use in
     *  unit testing.
     * 
     * @param allLinesOfFile - the file contents to lex. Each new index
     *  represents a new line in the file.
     */
    public LexResult lexString(List<String> allLinesOfFile)
    {
        return this.lexString(allLinesOfFile, this.state());
    }
    
    private LexResult lexString(List<String> allLinesOfFile, LexerState state)
    {
        var result = new LexResult();
        var commentsRemover = new CommentsRemover();
        var linesOfFile = commentsRemover.run(allLinesOfFile);
        
        state.appendLexedToken(Token.BLUC_SOF);
        
        state.totalLineCount(linesOfFile.size());

        for (var line : linesOfFile)
        {
            state.line(line);
            
            var lexLineResult = this.lexLine(this.state());
            
            if (lexLineResult.hasFailed())
            {
                var errCode = result.errCode();
                System.out.println(errCode.errorMessage());
                
                result.errCode(errCode);

                // A lexer error is a critical error, we can't continue lexing.
                return result;
            }

            state.incrementLineNum();
        }
        
        state.appendLexedToken(Token.BLUC_EOF);
        
        result.data(state.lexedTokens());

        return result;
    }
    
    private Result<LexErrCode> lexLine(LexerState state)
    {
        var result = new Result<LexErrCode>();
        var lineAsArray = state.line().toCharArray();

        state.column(1);
        
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
                result = this.lexCharWhenNotOnAQuote(state);
            }
        }

        return result;
    }
    
    private void lexCharWhenOnAQuote(LexerState state)
    {
        if (!state.isInString())
        {
            state.appendCurCharToWordSoFar();
            state.prepareForNextToken(
                true,
                false,
                false,
                false);
        }
        else if (!state.wasLastCharEscape())
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
    
    private Result<LexErrCode> lexCharWhenNotOnAQuote(LexerState state)
    {
        var result = new Result<LexErrCode>();

        if (state.isInString())
        {
            result = this.lexWhenInsideString(state);
        }
        else
        {
            this.lexWhenNotInString(state);
        }

        return result;
    }
    
    private Result<LexErrCode> lexWhenInsideString(LexerState state)
    {
        var result = new Result<LexErrCode>();

        if (state.curChar() == '\\')
        {
            state.wasLastCharEscape(true);
        }
        else
        {
            state.appendCurCharToWordSoFar();
            state.wasLastCharEscape(false);

            if (state.isAtEOF())
            {
                result.errCode(
                    new LexErrCode(
                        LexErrCode.UNEXPECTED_EOF,
                        state.line(),
                        state.column()));
            }
        }

        return result;
    }
    
    /**
     * Lexes the current token, under the assumption that the lexer is not
     *  currently inside of a string literal.
     */
    private void lexWhenNotInString(LexerState state)
    {
        if (state.curCharIsWhitespace())
        {
            state.appendTokenIfNotWhitespace();
            
            state.resetWordSoFar();
            state.doCheckNextToken(false);
        }
        else if (state.curCharMatchesAny(
                    new char[]{'(', ')', '{', '}', '[', ']', ','}))
        {
            state.appendTokenIfNotWhitespace();
            
            state.setWordSoFarToCurChar();
            state.appendTokenIfNotWhitespace();
            
            state.resetWordSoFar();
            state.doCheckNextToken(false);
        }
        else if (state.curCharMatchesAny(
                    new char[]{'+', '-', '*', '/', '%', '=', '!',
                        '<', '>', '|', '&', '^'}))
        {
            if (state.doCheckNextToken())
            {
                state.appendCurCharToWordSoFar();
                state.appendTokenIfNotWhitespace();
                
                state.resetWordSoFar();
                state.doCheckNextToken(false);
            }
            else
            {
                state.appendTokenIfNotWhitespace();
                
                state.setWordSoFarToCurChar();
                state.doCheckNextToken(true);
            }
        }
        else
        {
            state.appendCurCharToWordSoFar();

            if (state.isAtEOF())
            {
                state.appendTokenIfNotWhitespace();
            }
        }
    }

    @AllArgsConstructor
    public static class LexErrCode
    {
        public static final int UNEXPECTED_EOF = 0;

        @Getter
        @Setter
        private int errorCode;

        @Getter
        @Setter
        private String errorLine;

        @Getter
        @Setter
        private int errorColumn;

        public String errorMessage()
        {
            // I didn't really think these few error types needed to be 
            //  inherited classes. I'm trying to favor composition over
            //  inheritance.
            switch (this.errorCode())
            {
                case UNEXPECTED_EOF:
                    return this.getEofErrorMessage();

                default:
                    return "Unknown error code.";
            }
        }

        private String getEofErrorMessage()
        {
            var lookbackCharIndex = Math.max(0, this.errorColumn() - 10);

            var surroundingTokens
                    = this
                    .errorLine()
                    .substring(
                        lookbackCharIndex,
                        this.errorColumn());

            return String.format(
                "[LEXER ERROR, line %s, col %s]: Unexpected EOF while inside a string. Expected string terminator near:\n" +
                "\t`%s`." +
                "",
                this.errorLine(),
                this.errorColumn(),
                surroundingTokens);
        }
    }
    
    /**
     * Shorthand alias for ResultType<LexErrCode, ArrayList<Token>>
     */
    public static class LexResult extends ResultType<LexErrCode, ArrayList<Token>>
    {
    }
}
