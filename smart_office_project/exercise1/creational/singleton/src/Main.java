package exercise1.creational.singleton.src;

public class Main {
    public static void main(String[] args){
        LoggerSingleton s1 = LoggerSingleton.getInstance();
        LoggerSingleton s2 = LoggerSingleton.getInstance();
        System.out.println("Same instance? " + (s1==s2));
        System.out.println("Singleton Pattern demo finished.");
    }
}
