package exercise1.behavioural.command.src;

public class Main {
    public static void main(String[] args){
        Light light = new Light();
        RemoteControl rc = new RemoteControl();
        rc.setCommand(new LightOnCommand(light));
        rc.press(); // Light on
        rc.setCommand(new LightOffCommand(light));
        rc.press(); // Light off
        System.out.println("Command Pattern demo finished.");
    }
}
