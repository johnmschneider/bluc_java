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

/**
 * Pretty printer for expressions.
 */
public class ExprPrinter implements Expr.Visitor<String>
{

    @Override
    public String visitBinaryExpr(Expr.Binary expr)
    {
        return parenthesize(expr.getOperator().getText(),
                        expr.getLeft(), expr.getRight());
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr)
    {
        return parenthesize("group", expr.getInnerExpr());
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr)
    {
        // Not a typo, we want to literally print "null" if this is a null
        //  literal.
        var output = "null";
        
        if (expr.getValue() != null)
        {
            return expr.getValue().getText();
        }
        
        return output;
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr)
    {
        return parenthesize(
            expr.getOperator().getText(), expr.getRight());
    }

    private String parenthesize(String name, Expr... exprs)
    {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs)
        {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
    
    public String print(Expr expr)
    {
        return expr.accept(this);
    }
}
