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
package bluc_java.parser.statements;

import bluc_java.ResultType;
import bluc_java.parser.Parser;
import bluc_java.parser.statements.Stmt;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Sub-parser for parsing statements.
 * @author john
 */
@AllArgsConstructor
public class StmtSubparser
{
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Parser parser;
    
    /**
     * Tries to parse the current statement that the parser is on. <br/><br/>
     * 
     * <b>Post-condition:</b> The parser will be on the next token after the
     *  statement if the statement was successfully parsed.
     */
    public StmtParseResult tryParseStmt()
    {
        var result = new StmtParseResult();
        
        result.errCode(StmtResultErrCode.FUNCTION_NOT_IMPLEMENTED_YET);
        
        return result;
    }
    
    /**
     * Class for restoring the result of the tryParseStmt function. Shorthand
     *  for ResultType<StmtResultErrCode, ArrayList<Stmt>>.
     */
    public class StmtParseResult
            extends ResultType<StmtResultErrCode, ArrayList<Stmt>>
    {
        
    }
    
    /**
     * Stores error codes for the "tryParseStmt" function.
     * @see StmtSubparser.tryParseStmt
     */
    @AllArgsConstructor
    public enum StmtResultErrCode
    {
        /**
         * This is a temporary error code that should be returned until the
         *  "tryParseStmt" function is implemented.
         */
        FUNCTION_NOT_IMPLEMENTED_YET(
            "Function `StmtParseResult.tryParseStmt` not yet implemented.");
        
        @Getter
        @Setter(AccessLevel.PRIVATE)
        public String formattedMessage;
    }
}
