package exercise2;

import java.util.logging.Logger;

public class LightController implements RoomObserver {
    private static final Logger LOG = Logger.getLogger(LightController.class.getName());
    @Override
    public void update(Room room){
        if(room.isOccupied()){
            LOG.info("Room " + room.id() + ": lights ON");
        } else {
            LOG.info("Room " + room.id() + ": lights OFF");
        }
    }
}
