// ==========================================
// PARKING SPOT HIERARCHY
// ==========================================

import java.io.Serializable;

public abstract class ParkingSpot implements Serializable {

    private String spotID;
    private Vehicle.VehicleSize spotSize;
    private boolean availabilityStatus;
    private int distanceFromEntrance;
    private boolean EV_ChargingStatus;
    private Vehicle currentOccupant;

    public ParkingSpot(String spotID, Vehicle.VehicleSize spotSize, int distanceFromEntrance, boolean EV_ChargingStatus) {
        this.spotID = spotID;
        this.spotSize = spotSize;
        this.availabilityStatus = true; // All spots start empty
        this.distanceFromEntrance = distanceFromEntrance;
        this.EV_ChargingStatus = EV_ChargingStatus;
        this.currentOccupant = null;
    }

    /**
     * Determines if a vehicle is physically and legally allowed in this spot.
     * Implements Graceful Degradation: EVs can use standard spots as a fallback.
     */
    public  boolean canFit(Vehicle vehicle) {
        // Fail fast: Sizes must strictly match
        if (this.spotSize != vehicle.getSize())
            return false ;

        // Enforce Power rules
        if (vehicle.getPowerSource() == Vehicle.PowerSource.ELECTRIC)
            return true; // Fallback: EVs allowed everywhere (if size matches)
        else
            return !this.hasEV_Charging(); // Gas cars forbidden from chargers
    }

    // Assigns a vehicle to the spot and updates availability
    public  void parkVehicle(Vehicle vehicle) {

        if (this.isAvailable()) {
            this.availabilityStatus = false;
            this.currentOccupant = vehicle;
        }
    }

    // Clears the spot when a vehicle leaves
    public  void freeSpot() {

        if (!this.isAvailable()) {
            this.availabilityStatus = true;
            this.currentOccupant = null;
        }
    }

    // GETTERS
    public String getSpotID() {
        return spotID;
    }

    public Vehicle.VehicleSize getSpotSize() {
        return spotSize;
    }

    public boolean isAvailable() {
        return availabilityStatus;
    }

    public int getDistanceFromEntrance() {
        return distanceFromEntrance;
    }

    public boolean hasEV_Charging() {
        return EV_ChargingStatus;
    }

    public Vehicle getCurrentOccupant() {
        return currentOccupant;
    }

    // Method Overriding
    @Override
    public String toString() {
        String evTag = this.EV_ChargingStatus ? " (EV Charger)" : "";
        return "Spot " + this.spotID + evTag;
    }
}

// Sub-Classes
class MotorcycleSpot extends ParkingSpot{
    public MotorcycleSpot(String spotID, int distanceFromEntrance, boolean EV_ChargingStatus) {
        super(spotID, Vehicle.VehicleSize.MOTORCYCLE, distanceFromEntrance, EV_ChargingStatus);
    }
}

class CompactSpot extends ParkingSpot {
    public CompactSpot(String spotID, int distanceFromEntrance, boolean EV_ChargingStatus) {
        super(spotID, Vehicle.VehicleSize.COMPACT, distanceFromEntrance, EV_ChargingStatus);
    }
}

class LargeSpot extends ParkingSpot {
    public LargeSpot(String spotID, int distanceFromEntrance, boolean EV_ChargingStatus) {
        super(spotID, Vehicle.VehicleSize.LARGE, distanceFromEntrance, EV_ChargingStatus);
    }
}
