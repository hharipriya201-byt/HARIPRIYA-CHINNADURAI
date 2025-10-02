package exercise1.behavioural.command.src;

public class LightOffCommand implements Command {
    private Light light;
    public LightOffCommand(Light l){ this.light = l; }
    @Override
    public void execute(){ light.off(); }
}
