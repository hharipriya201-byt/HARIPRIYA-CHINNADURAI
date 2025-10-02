package exercise1.behavioural.interpreter.src;

public class Add implements Expression {
    private Expression left, right;
    public Add(Expression l, Expression r){ this.left = l; this.right = r; }
    public int interpret(){ return left.interpret() + right.interpret(); }
}
