package exercise1.behavioural.command.src;

public class LightOnCommand implements Command {
    private Light light;
    public LightOnCommand(Light l){ this.light = l; }
    @Override
    public void execute(){ light.on(); }
}
