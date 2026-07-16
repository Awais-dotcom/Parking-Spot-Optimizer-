import java.io.Serializable;
import java.time.LocalDateTime;

interface Billable {

    double calculateFee(long minutesParked);
}


public abstract class Vehicle implements Billable, Serializable {

    public enum VehicleSize {
        MOTORCYCLE, COMPACT, LARGE;
    }

    public enum PowerSource {
        COMBUSTION, ELECTRIC;
    }

    private String numberPlate;
    private VehicleSize size;
    private PowerSource powerSource;
    private LocalDateTime entryTime;

    // Overloaded Constructor 1: Defaults to Combustion
    public Vehicle(String numberPlate, VehicleSize size) {
        this.numberPlate = numberPlate;
        this.size = size;
        this.powerSource = PowerSource.COMBUSTION;
        this.entryTime = LocalDateTime.now();
    }

    // Overloaded Constructor 2: Specifies Power Source
    public Vehicle(String numberPlate, VehicleSize size, PowerSource powerSource) {
        this.numberPlate = numberPlate;
        this.size = size;
        this.powerSource = powerSource;
        this.entryTime = LocalDateTime.now();
    }

    // GETTERS
    public String getNumberPlate() {
        return numberPlate;
    }

    public VehicleSize getSize() {
        return size;
    }

    public PowerSource getPowerSource() {
        return powerSource;
    }

    public  LocalDateTime getEntryTime() {
        return entryTime;
    }


    // We override the default Object.toString() method to provide a clean description.
    @Override
    public String toString() {
        return this.powerSource + " " +  this.size + " [" + this.numberPlate + "]";
    }
}

// Sub-Classes
class Motorcycle extends Vehicle {

    public Motorcycle(String numberPlate, PowerSource powerSource) {
        super(numberPlate, VehicleSize.MOTORCYCLE, powerSource);
    }

    @Override
    public double calculateFee(long minutesParked) {
        return 0.05 * minutesParked;
    }
}

class Car extends Vehicle {

    public Car(String numberPlate, PowerSource powerSource) {
        super(numberPlate, VehicleSize.COMPACT, powerSource);
    }

    @Override
    public double calculateFee(long minutesParked) {
        return 0.10 * minutesParked;
    }
}

class SUV extends Vehicle {

    public SUV(String numberPlate, PowerSource powerSource) {
        super(numberPlate, VehicleSize.LARGE, powerSource);
    }

    @Override
    public double calculateFee(long minutesParked) {
        return 0.25 * minutesParked;
    }
}

