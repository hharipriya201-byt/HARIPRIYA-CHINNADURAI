package exercise1.behavioural.interpreter.src;

public class Number implements Expression {
    private int number;
    public Number(int n){ this.number = n; }
    public int interpret(){ return number; }
}
