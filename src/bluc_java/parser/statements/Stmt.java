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

import java.util.ArrayList;
import lombok.AccessLevel;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a statement in the abstract syntax tree (AST).
 */
public class Stmt
{
    /**
     * The type of statement this is (method, class, etc).
     */
    @Getter
    @Setter
    private StmtType type;

    /**
     * The parent node of this statement. For instance, a "class" Stmt would
     *  be the parent of a "method".
     */
    @Getter
    @Setter
    private Stmt parent;

    /**
     * The children nodes of this statement. For instance, a "class" Stmt may have multiple
     *  "method" children. It may also have several types of children, such as "Field"
     *  statements.<br/><br/>
     * 
     * It may also have <b>no</b> children, in which case the list is still initialized,
     *  but is empty.
     */
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private ArrayList<Stmt> children;


    /**
     * Creates a new statement with the given type and parent.
     * 
     * @param type The type of statement this is (method, class, etc).
     * @param parent The parent statement of this statement.
     */
    public Stmt(StmtType type, Stmt parent)
    {
        this.type = type;
        this.parent = parent;
        this.children = new ArrayList<>();
    }
}
