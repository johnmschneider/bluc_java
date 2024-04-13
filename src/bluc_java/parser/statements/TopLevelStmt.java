package bluc_java.parser.statements;

/**
 * Represents a top-level statement in the abstract syntax tree (AST). The root node of each
 *  translation unit is a top-level statement.
 */
public class TopLevelStmt extends Stmt
{
    public TopLevelStmt()
    {
        super(StmtType.TOP_LEVEL, null);

        // The top-level statement is its own parent, to simplify the AST traversal process.
        //  This way, we don't have to check if the parent is null when traversing the AST.
        this.parent(this);
    }
}