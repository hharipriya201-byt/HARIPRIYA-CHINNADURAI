package exercise2.commands;

import exercise2.BookingManager;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

public class BookCommand implements Command {
    private static final Logger LOG = Logger.getLogger(BookCommand.class.getName());
    private final int roomId;
    private final Instant from;
    private final Duration minutes;
    public BookCommand(int roomId, Instant from, Duration minutes){
        this.roomId = roomId; this.from = from; this.minutes = minutes;
    }
    @Override
    public boolean execute(){
        try{
            return BookingManager.getInstance().book(roomId, from, minutes);
        } catch(Exception ex){
            LOG.warning("BookCommand failed: " + ex.getMessage());
            return false;
        }
    }
}
