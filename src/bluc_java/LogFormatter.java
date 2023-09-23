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

/**
 * Class for formatting log strings, in preparation for printing to the console.
 * 
 * @author john
 */
public class LogFormatter
{
    /**
     * Formats a string for when the compiler is in an invalid state (i.e., for
     *  logging compiler bugs / 'impossible' states).
     * 
     * All compiler errors are fatal errors.
     */
    public static String formatCompilerError(String methodName, String message)
    {
        return "{" + methodName + "} - FATAL COMPILER ERROR:\t" + message;
    }
    
    public static String formatDebug(String methodName, String message)
    {
        return "{" + methodName + "} - DEBUG:\t" + message;
    }
}
