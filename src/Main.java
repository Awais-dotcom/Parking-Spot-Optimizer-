import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        ParkingLot parkingLot = new ParkingLot(10, 20, 10, 3, 5, 4);
        Admin systemAdmin = new Admin("admin", "123", parkingLot);

        while (true) {
            try {
                System.out.println("=================================");
                System.out.println("    PARKING MANAGEMENT SYSTEM    ");
                System.out.println("=================================");
                System.out.println("1. Customer: Arrive & Park");
                System.out.println("2. Customer: Checkout & Pay");
                System.out.println("3. Admin: System Dashboard");
                System.out.println("4. Exit the System");
                System.out.print("Select an option (1-4): ");

                int choice = input.nextInt();
                input.nextLine(); // Consume the leftover newline character

                switch (choice) {
                    case 1:
                        System.out.print("Enter the Number Plate: ");
                        String numberPlate = input.nextLine();
                        if (parkingLot.isPlateParked(numberPlate)){
                            System.out.println("Security Alert: A vehicle with number plate ["+numberPlate+"] is already parked in the lot!");
                            break;
                        }
                        System.out.println("Select vehicle Type: ");
                        System.out.println("1. Motorcycle");
                        System.out.println("2. Car");
                        System.out.println("3. SUV");
                        System.out.print("Enter your choice: ");
                        int typeChoice = input.nextInt();

                        System.out.println("Select Power Source:");
                        System.out.println("1. Standard Combustion (Gas/Diesel)");
                        System.out.println("2. Electric Vehicle (EV)");
                        System.out.print("Choice (1-2): ");
                        int powerChoice = input.nextInt();

                        input.nextLine();
                        Vehicle.PowerSource powerEnum;
                        if (powerChoice == 2) {
                            powerEnum = Vehicle.PowerSource.ELECTRIC;
                        } else {
                            powerEnum = Vehicle.PowerSource.COMBUSTION; // Default fallback
                        }

                        Vehicle newVehicle = null;
                        switch (typeChoice) {
                            case 1:
                                newVehicle = new Motorcycle(numberPlate, powerEnum);
                                break;
                            case 2:
                                newVehicle = new Car(numberPlate, powerEnum);
                                break;
                            case 3:
                                newVehicle = new SUV(numberPlate, powerEnum);
                                break;
                            default:
                                System.out.println("Error: Invalid vehicle type selected. Arrival cancelled.");
                                break;
                        }

                        if (newVehicle != null)
                            parkingLot.routeVehicle(newVehicle);
                        break;

                    case 2:
                        System.out.print("Enter Number plate: ");
                        String plate = input.nextLine();
                        parkingLot.removeVehicleByPlate(plate);
                        break;

                    case 3:
                        boolean isAuthenticated = false;
                        String adminPassword = "";

                        //The Authentication Loop
                        while (!isAuthenticated) {
                            System.out.print("Enter admin password (or type 'cancel' to go back): ");
                            adminPassword = input.nextLine();

                            if (adminPassword.equalsIgnoreCase("cancel")) {
                                System.out.println("Returning to Main Menu...");
                                break;
                            }
                            if (systemAdmin.authenticate(adminPassword)) {
                                isAuthenticated = true;
                            } else {
                                System.out.println("Wrong Password. Try again!\n");
                            }
                        }
                            if (isAuthenticated){
                                boolean adminMenuOpen = true;

                                while (adminMenuOpen){
                                    System.out.println("---------------------------------");
                                    System.out.println("         ADMIN DASHBOARD        ");
                                    System.out.println("-----------------------------------");
                                    System.out.println("1. Display Total Revenue");
                                    System.out.println("2. Display Available Spots");
                                    System.out.println("3. Force Checkout");
                                    System.out.println("4. Go back to main menu");
                                    System.out.println("5. Exit the System");
                                    System.out.print("Enter your choice(1-3): ");
                                    int adminChoice = input.nextInt();
                                    input.nextLine();
                                    switch (adminChoice){
                                        case 1:
                                            systemAdmin.displayTotalRevenue();
                                            break;

                                        case 2:
                                            systemAdmin.displayAvailableSpots();
                                            break;

                                        case 3:
                                            System.out.print("Enter the exact spot ID: ");
                                            String spotID = input.nextLine();
                                            parkingLot.removeVehicle(spotID);
                                            break;

                                        case 4:
                                            adminMenuOpen = false;
                                            break;

                                        case 5:
                                            System.out.println("Exiting the system!");
                                            System.exit(0);

                                        default:
                                            System.out.println("Error: Invalid choice. Try again!");
                                            break;
                                    }
                                }
                            }
                            break;
                            
                    case 4:
                        System.out.println("Exiting the system!");
                        System.exit(0);

                    default:
                        System.out.println("\nError: Invalid selection. Please try again.");
                }
            }catch (InputMismatchException e){
                System.out.println("Error: Invalid Input Type. Try again!");
                input.nextLine();
            }
        }
    }
}