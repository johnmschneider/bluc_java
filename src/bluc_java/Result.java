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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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
    private boolean successful;
    
    /**
     * The token that caused the error (if an error occurred).
     */
    @Getter
    @Setter
    private Token errToken;
    
    @Getter
    private ErrorType errCode;
    
    public Result()
    {
        this.setSuccessful(true);
    }
    
    public void setErrCode(ErrorType errCode)
    {
        this.setSuccessful(false);
        this.setErrCode(errCode);
    }
    
    /**
     * Set both the error token and error code.
     */
    public void setError(Token errToken, ErrorType errCode)
    {
        this.setErrToken(errToken);
    }
}
