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
package bluc_java;

import javax.lang.model.type.ErrorType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Represents a result after performing an operation that may fail.
 * 
 * Base class of all ResultType's.
 */
public class Result<ErrorType>
{
    /**
     * Whether or not the operation was successful.
     */
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private boolean hasSucceeded;
    
    /**
     * The token that caused the error (if an error occurred). Null otherwise.
     */
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Token errToken;
    
    /**
     * The error code of the error (if an error occurred). Null otherwise.
     */
    @Getter
    private ErrorType errCode;
    
    public Result()
    {
        this.hasSucceeded(true);
    }
    
    /**
     * @return "true" if the result has failed,
     *  "false" otherwise.
     */
    public boolean hasFailed()
    {
        return !this.hasSucceeded();
    }
    
    /**
     * Set the error code for this result.
     * 
     * If this method is called, the success flag is also set to false.
     * 
     * @param errCode - The error code to set.
     */
    public void errCode(ErrorType errCode)
    {
        this.hasSucceeded(false);
        this.errCode = errCode;
    }
    
    /**
     * Set both the error token and error code.
     * 
     * @param errToken - The token that the error occurred on.
     * @Param errCode - The error code to set.
     */
    public void error(Token errToken, ErrorType errCode)
    {
        this.errToken(errToken);
        this.errCode(errCode);
    }
}
