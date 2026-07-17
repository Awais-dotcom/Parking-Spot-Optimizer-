public class Admin {

    private final String username;
    private final String password;
    ParkingLot managedLot;

    public Admin(String username, String password, ParkingLot managedLot) {
        this.username = username;
        this.password = password;
        this.managedLot = managedLot;
    }

    public boolean authenticate(String enteredPassword){
        return this.password.equals(enteredPassword);
    }

    public void displayTotalRevenue(){
        System.out.printf("Total revenue: $%.2f%n", managedLot.getRevenue());
    }

    public void displayAvailableSpots() {
        int occupied = managedLot.getAllSpots().size() - managedLot.getAvailableSpots();
        System.out.println("Total number of available spots: " + managedLot.getAvailableSpots() + ", Occupied: " +occupied);
    }
}
