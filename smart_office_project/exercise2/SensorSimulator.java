package exercise2;

import java.util.logging.Logger;

/**
 * Simulates sensor events by calling Room.setOccupants().
 * In real system sensors would call this via network/hardware events.
 */
public class SensorSimulator {
    private static final Logger LOG = Logger.getLogger(SensorSimulator.class.getName());
    public void setOccupants(int roomId, int occupants){
        OfficeConfig cfg = OfficeConfig.getInstance();
        cfg.getRoom(roomId).ifPresentOrElse(r -> {
            try {
                r.setOccupants(occupants);
            } catch(Exception ex){
                LOG.warning("Sensor failed: " + ex.getMessage());
            }
        }, () -> LOG.warning("Room " + roomId + " does not exist (sensor)"));
    }
}
