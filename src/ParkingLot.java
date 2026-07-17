// ==========================================
// MANAGER CLASS
// ==========================================
import java.io.*;
import java.time.*;
import java.util.*;


public class ParkingLot implements Serializable {
    // Composition: The Lot owns the spots
    private ArrayList <ParkingSpot> allSpots;
    private double revenue;

    public ParkingLot(int numOfMotorcycleSpots, int numOfCompactSpots, int numOfLargeSpots, int numMotorcycleEV, int numCompactEV, int numLargeEV) {
        allSpots = new ArrayList<>();
        this.revenue = 0.0;
        for (int i = 1; i <= numOfMotorcycleSpots; i++) {
            String id = "M" + i;
            int distance = i * 2;
            boolean hasEv = i <= numMotorcycleEV;

            allSpots.add(new MotorcycleSpot(id,distance,hasEv));
        }

        for (int i = 1; i <= numOfCompactSpots; i++) {
            String id = "C" + i;
            int distance = i * 3;
            boolean hasEv = i <= numCompactEV;

            allSpots.add(new CompactSpot(id,distance,hasEv));
        }

        for (int i = 1; i <= numOfLargeSpots; i++) {
            String id = "L" + i;
            int distance = i * 4;
            boolean hasEv = i <= numLargeEV;

            allSpots.add(new LargeSpot(id,distance,hasEv));
        }
        loadDataFromFile();
    }

    public double getRevenue() {
        return revenue;
    }

    public ArrayList<ParkingSpot> getAllSpots() {
        return allSpots;
    }

    @SuppressWarnings("unchecked")
    private void loadDataFromFile(){
        try {
            FileInputStream fis = new FileInputStream("Parking_Data.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            allSpots = (ArrayList<ParkingSpot>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            System.out.println("Starting the system....");
        } catch (IOException e) {
            System.out.println("Error: Could not load file!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Class not found!"+e);
        }
    }

    /**
     * Two-Pass Algorithm to find the optimal parking spot.
     * Enforces EV priority but allows fallback to standard spots.
     */
    public void routeVehicle(Vehicle vehicle){

        // PASS 1: The VIP Search (For EVs Only)
        if (vehicle.getPowerSource() == Vehicle.PowerSource.ELECTRIC){

            for (ParkingSpot parkingSpot: allSpots){

                if (parkingSpot.isAvailable() && parkingSpot.canFit(vehicle) && parkingSpot.hasEV_Charging()){
                    parkingSpot.parkVehicle(vehicle);
                    saveLot();
                    // Utilizing the overridden toString() from Vehicle and ParkingSpot
                    System.out.println("Priority Success: Routed " + vehicle + " to " + parkingSpot);
                    return;
                }
            }
            System.out.println("Notice: All EV chargers are full. Attempting fallback to standard spots...");
        }

        // PASS 2: Standard Search (Gas cars, or EV Fallback)
        for (ParkingSpot parkingSpot: allSpots){

            if (parkingSpot.isAvailable() && parkingSpot.canFit(vehicle)) {
                parkingSpot.parkVehicle(vehicle);
                saveLot();
                System.out.println("Standard Success: Routed " + vehicle + " to Spot " + parkingSpot.getSpotID());
                return;
            }
        }
        System.out.println("Failed: No available spots for " + vehicle.getSize() + ".");
    }

    /**
     * Searches the roster by ID to trigger the checkout process.
     */
    public void removeVehicle(String spotID){

        for (ParkingSpot parkingSpot: allSpots) {

            if (parkingSpot.getSpotID().equals(spotID)) {

                if (!parkingSpot.isAvailable()){

                    Vehicle departingVehicle = parkingSpot.getCurrentOccupant();
                    parkingSpot.freeSpot();
                    System.out.println("Checkout Successful: Spot " + spotID + " is now empty and available.");
                    showReceipt(departingVehicle,spotID);
                    saveLot();
                    return;
                }
                else
                    System.out.println("Error: Spot " + spotID + " is already empty!");
            }
        }
        System.out.println("Error: Spot " + spotID + " not found.");
    }

    public void removeVehicleByPlate(String numberPlate) {
        for (ParkingSpot spot : allSpots) {

            if (!spot.isAvailable()) {

                Vehicle vehicle = spot.getCurrentOccupant();
                if (vehicle.getNumberPlate().equals(numberPlate)){
                    spot.freeSpot();
                    System.out.println("Checkout Successful: Spot [" + spot.getSpotID() + "] is now empty and available.");
                    showReceipt(vehicle,spot.getSpotID());
                    saveLot();
                    return;
                }
            }
        }
        System.out.println("Error: Vehicle with number plate ["+numberPlate+"] not found!");
    }

    private void showReceipt(Vehicle vehicle, String spotID) {
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(vehicle.getEntryTime(),endTime);
        long minutesParked = duration.toMinutes();
        double totalCost = vehicle.calculateFee(minutesParked);
        revenue += totalCost;
        System.out.println("\n--- CHECKOUT RECEIPT ---");
        System.out.println("Spot ID: " + spotID);
        System.out.println("Vehicle: " + vehicle);
        System.out.println("Time Parked: " + minutesParked + " minutes");
        System.out.println("Total Fee: $" + String.format("%.2f", totalCost));
        System.out.println("------------------------\n");
    }

    public int getAvailableSpots(){
        int count = 0;
        for (ParkingSpot parkingSpot: allSpots){
            if (parkingSpot.isAvailable())
                count++;
        }
        return count;
    }

    public boolean isPlateParked(String numberPlate) {
        for (ParkingSpot spot : allSpots){
            if (!spot.isAvailable()){
                Vehicle vehicle = spot.getCurrentOccupant();
                if (vehicle.getNumberPlate().equals(numberPlate)){
                    return true;
                }
            }
        }
        return false;
    }

    private void saveLot(){

        try {
            FileOutputStream fos = new FileOutputStream("Parking_Data.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(allSpots);
            oos.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: File not found!");
        } catch (IOException e) {
            System.out.println("Error: Could not load file!");
        }
    }
}
