package exercise1.creational.factory.src;

public class VehicleFactory {
    public static Vehicle getVehicle(String type){
        if(type==null) throw new IllegalArgumentException("type required");
        switch(type.toLowerCase()){
            case "car": return new Car();
            case "bike": return new Bike();
            default: throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }
}
