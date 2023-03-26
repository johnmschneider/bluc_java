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
package bluc_java.parser.expressions;

import bluc_java.Token;
import bluc_java.parser.ParserState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an expression in the abstract syntax tree (AST).
 */
public abstract class Expr
{
    @AllArgsConstructor
    public class Binary
    {
        @Getter
        @Setter
        private Expr left;
        
        @Getter
        @Setter
        private Token operator;
        
        @Getter
        @Setter
        private Expr right;
    }
    
    @AllArgsConstructor
    public class Grouping
    {
        @Getter
        @Setter
        private Token openParenthesis;
        
        @Getter
        @Setter
        private Expr innerExpr;
        
        @Getter
        @Setter
        private Expr closeParenthesis;
    }
    
    @AllArgsConstructor
    public class Literal
    {
        @Getter
        @Setter
        private Token value;
    }
    
    @AllArgsConstructor
    public class Unary
    {
        @Getter
        @Setter
        private Token operator;
        
        @Getter
        @Setter
        private Expr right;
    }
    
    public interface Visitor<T>
    {
        T visitBinaryExpr(Binary expr);
        T visitGroupingExpr(Grouping expr);
        T visitLiteralExpr(Literal expr);
        T visitUnaryExpr(Unary expr);
    }
    
    abstract <T> T accept(Visitor<T> visitor);
}
