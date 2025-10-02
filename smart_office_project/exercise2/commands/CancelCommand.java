package exercise2.commands;

import exercise2.BookingManager;
import java.util.logging.Logger;

public class CancelCommand implements Command {
    private static final Logger LOG = Logger.getLogger(CancelCommand.class.getName());
    private final int roomId;
    public CancelCommand(int roomId){ this.roomId = roomId; }
    @Override
    public boolean execute(){
        try{
            return BookingManager.getInstance().cancel(roomId);
        } catch(Exception ex){
            LOG.warning("CancelCommand failed: " + ex.getMessage());
            return false;
        }
    }
}
