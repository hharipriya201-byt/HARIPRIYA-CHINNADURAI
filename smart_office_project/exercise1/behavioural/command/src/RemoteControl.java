package exercise1.behavioural.command.src;
import java.util.logging.Logger;

public class RemoteControl {
    private static final Logger LOG = Logger.getLogger(RemoteControl.class.getName());
    private Command slot;
    public void setCommand(Command c){ slot = c; LOG.info("Command set"); }
    public void press(){ 
        if(slot==null){ LOG.warning("No command set"); return; }
        slot.execute();
    }
}
