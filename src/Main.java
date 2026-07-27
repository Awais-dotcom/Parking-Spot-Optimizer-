void main() {
    Scanner input = new Scanner(System.in);

    ParkingLot parkingLot = new ParkingLot(10, 20, 10, 3, 5, 4);
    Admin systemAdmin = new Admin("admin", "123", parkingLot);

    while (true) {
        try {
            IO.println("=================================");
            IO.println("    PARKING MANAGEMENT SYSTEM    ");
            IO.println("=================================");
            IO.println("1. Customer: Arrive & Park");
            IO.println("2. Customer: Checkout & Pay");
            IO.println("3. Admin: System Dashboard");
            IO.println("4. Exit the System");
            IO.print("Select an option (1-4): ");

            int choice = input.nextInt();
            input.nextLine(); // Consume the leftover newline character

            switch (choice) {
                case 1:
                    IO.print("Enter the Number Plate: ");
                    String numberPlate = input.nextLine();
                    if (parkingLot.isPlateParked(numberPlate)) {
                        IO.println("Security Alert: A vehicle with number plate [" + numberPlate + "] is already parked in the lot!");
                        break;
                    }
                    IO.println("Select vehicle Type: ");
                    IO.println("1. Motorcycle");
                    IO.println("2. Car");
                    IO.println("3. SUV");
                    IO.print("Enter your choice: ");
                    int typeChoice = input.nextInt();

                    IO.println("Select Power Source:");
                    IO.println("1. Standard Combustion (Gas/Diesel)");
                    IO.println("2. Electric Vehicle (EV)");
                    IO.print("Choice (1-2): ");
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
                            IO.println("Error: Invalid vehicle type selected. Arrival cancelled.");
                            break;
                    }

                    if (newVehicle != null)
                        parkingLot.routeVehicle(newVehicle);
                    break;

                case 2:
                    IO.print("Enter Number plate: ");
                    String plate = input.nextLine();
                    parkingLot.removeVehicleByPlate(plate);
                    break;

                case 3:
                    boolean isAuthenticated = false;
                    String adminPassword;

                    //The Authentication Loop
                    while (!isAuthenticated) {
                        IO.print("Enter admin password (or type 'cancel' to go back): ");
                        adminPassword = input.nextLine();

                        if (adminPassword.equalsIgnoreCase("cancel")) {
                            IO.println("Returning to Main Menu...");
                            break;
                        }
                        if (systemAdmin.authenticate(adminPassword)) {
                            isAuthenticated = true;
                        } else {
                            IO.println("Wrong Password. Try again!\n");
                        }
                    }
                    if (isAuthenticated) {
                        boolean adminMenuOpen = true;

                        while (adminMenuOpen) {
                            IO.println("---------------------------------");
                            IO.println("         ADMIN DASHBOARD        ");
                            IO.println("-----------------------------------");
                            IO.println("1. Display Total Revenue");
                            IO.println("2. Display Available Spots");
                            IO.println("3. Force Checkout");
                            IO.println("4. Go back to main menu");
                            IO.println("5. Exit the System");
                            IO.print("Enter your choice(1-3): ");
                            int adminChoice = input.nextInt();
                            input.nextLine();
                            switch (adminChoice) {
                                case 1:
                                    systemAdmin.displayTotalRevenue();
                                    break;

                                case 2:
                                    systemAdmin.displayAvailableSpots();
                                    break;

                                case 3:
                                    IO.print("Enter the exact spot ID: ");
                                    String spotID = input.nextLine();
                                    parkingLot.removeVehicle(spotID);
                                    break;

                                case 4:
                                    adminMenuOpen = false;
                                    break;

                                case 5:
                                    IO.println("Exiting the system!");
                                    System.exit(0);

                                default:
                                    IO.println("Error: Invalid choice. Try again!");
                                    break;
                            }
                        }
                    }
                    break;

                case 4:
                    IO.println("Exiting the system!");
                    System.exit(0);

                default:
                    IO.println("\nError: Invalid selection. Please try again.");
            }
        } catch (InputMismatchException e) {
            IO.println("Error: Invalid Input Type. Try again!");
            input.nextLine();
        }
    }
}