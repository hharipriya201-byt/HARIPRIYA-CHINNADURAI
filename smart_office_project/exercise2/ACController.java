package exercise2;

import java.util.logging.Logger;

public class ACController implements RoomObserver {
    private static final Logger LOG = Logger.getLogger(ACController.class.getName());
    @Override
    public void update(Room room){
        if(room.isOccupied()){
            LOG.info("Room " + room.id() + ": AC ON");
        } else {
            LOG.info("Room " + room.id() + ": AC OFF");
        }
    }
}
