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

/**
 * An extensive enumeration containing all of the possible types of parser nodes that can be parsed.
 *
 * This should NOT take inheritance into account. If we're in a lambda definition block,
 *  a node's NodeType should NOT be BLOCK. It should be LAMBDA_BLOCK. <br/><br/>
 * 
 * <b>Remarks:</b><br/>
 *  We're using an enumeration instead of a class hierarchy for several reasons, in order of importance:<br/>
 * <p>
 *      1) if a sub-parser wants to check if we're on a certain type of node, then we don't
 *          have to actually implement the node yet. We can just check the generic node's StmtType. <br/><br/>
 * 
 *          This will help significantly when developing large sections of the compiler at once, as
 *          now we're free to defer the implementation of said nodes into another ticket. <br/><br/>
 * 
 *      2) because we want to keep the parser as simple as possible. We don't want to have to deal with
 *          inheritance and all the complexities that come with it.
 * </p>
 * @author John
 */
public enum StmtType
{
    /**
     * Whether or not we are currently parsing the top level of the program (i.e., outside of any class, method, or other symbol).
     */
    TOP_LEVEL,

    /**
     * Whether or not we are currently parsing the inside of a class.
     */
    CLASS,

    /**
     * Whether or not we are currently parsing the inside of an interface.
     */
    INTERFACE,

    /**
     * Whether or not we are currently parsing the inside of an enum.
     */
    ENUM,

    /**
     * Whether or not we are currently parsing the inside of an annotation.
     */
    ANNOTATION,

    /**
     * Whether or not we are currently parsing the inside of a field of a class.
     */
    CLASS_FIELD,

    /**
     * Whether or not we are currently parsing the parameter list declaration of a constructor.
     */
    CONSTRUCTOR_PARAMETERS,

    /**
     * Whether or not we are currently parsing the argument list of a constructor.
     */
    CONSTRUCTOR_ARGUMENTS,

    /**
     * Whether or not we are currently parsing the inside of a constructor call.
     */
    CONSTRUCTOR_CALL,

    /**
     * Whether or not we are currently parsing the inside of a constructor of a class.
     */
    CONSTRUCTOR_BLOCK,

    /**
     * Whether or not we are currently parsing the inside of a static block constructor of a class.
     */
    STATIC_CONSTRUCTOR_BLOCK,

    /**
     * Whether or not we are currently parsing the inside of an initializer block of a variable.
     */
    INITIALIZER_BLOCK,

    /**
     * Whether or not we are currently parsing the inside of an "attempt" (try) block.
     */
    ATTMEPT_BLOCK,

    /**
     * Whether or not we are currently parsing the inside of a "catch" parameter list.
     */
    CATCH_PARAMETERS,

    /**
     * Whether or not we are currently parsing the inside of a "catch" block.
     */
    CATCH_BLOCK,

    /**
     * Whether or not we are currently parsing the inside of a method call.
     */
    METHOD_CALL,

    /**
     * Whether or not we are currently parsing the inside of a method parameter list.
     */
    METHOD_PARAMETERS,

    /**
     * Whether or not we are currently parsing the inside of a method call/argument list.
     */
    METHOD_ARGUMENTS,

    /**
     * Whether or not we are currently parsing the inside of a method declaration (the block part).
     */
    METHOD_BLOCK,

    /**
     * Whether or not we are currently parsing the inside of a method's return statement.
     */
    RETURN_STATEMENT,

    /**
     * Whether or not we are currently parsing the inside of a lambda's parameter list.
     */
    LAMBDA_PARAMETERS,

    /**
     * Whether or not we are currently parsing the inside of a lambda's arguments.
     */
    LAMBDA_ARGUMENTS,

    /**
     * Whether or not we are currently parsing the inside of a lambda definition (the block).
     */
    LAMBDA_BLOCK,

    /**
     * Whether or not we are currently parsing the inside of a block.<br/><br/>
     * 
     * <b>Remarks:</b><br/>
     *  This applies ONLY to a regular block, and not to any other type
     *      of block, such as a method block.
     */
    BLOCK,

    /**
     * Whether or not we are currently parsing the inside of a switch block parameter list.
     */
    SWITCH_PARAMETERS,

    /**
     * Whether or not we are currently parsing the inside of a switch block.
     */
    SWITCH_BLOCK,

    /**
     * Whether or not we are currently parsing the inside of a while block parameter list.
     */
    WHILE_PARAMETERS,

    /**
     * Whether or not we are currently parsing the inside of a while loop.
     */
    WHILE_BLOCK,

    /**
     * Whether or not we are currently parsing the inside of a for loop parameter list.
     */
    FOR_BLOCK_PARAMETERS,

    /**
     * Whether or not we are currently parsing the inside of a for loop.
     */
    FOR_BLOCK,
}
