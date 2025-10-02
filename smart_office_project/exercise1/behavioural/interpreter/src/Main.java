package exercise1.behavioural.interpreter.src;

public class Main {
    public static void main(String[] args){
        Expression five = new Number(5);
        Expression three = new Number(3);
        Expression add = new Add(five, three);
        System.out.println("5 + 3 = " + add.interpret());
        System.out.println("Interpreter Pattern demo finished.");
    }
}
