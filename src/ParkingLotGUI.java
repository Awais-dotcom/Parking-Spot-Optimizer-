import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParkingLotGUI {
    ParkingLot parkingLot = new ParkingLot(10, 20, 10, 3, 5, 4);
    Admin systemAdmin = new Admin("admin", "123", parkingLot);

    JButton parkButton;
    JPanel spotMapContainer;

    // --- DARK MODE COLOR PALETTE ---
    Color bgDark = new Color(43, 43, 43);
    Color fgLight = new Color(187, 187, 187);

    // Spot Map Colors (Darker variants for Dark Mode)
    Color freeStandard = new Color(50, 110, 50);    // Deep Green
    Color freeEV = new Color(40, 100, 140);         // Deep Blue
    Color occStandard = new Color(150, 50, 50);     // Deep Red
    Color occEV = new Color(110, 50, 130);          // Deep Purple

    public ParkingLotGUI() {
        JFrame frame = new JFrame();
        frame.setTitle("Parking Management System");
        frame.setSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(bgDark);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel parkTab     = new JPanel();   // Tab 1: Park Vehicle
        JPanel checkoutTab = new JPanel();   // Tab 2: Checkout Vehicle
        JPanel adminTab    = new JPanel();   // Tab 3: Admin Dashboard
        JPanel mapTab        = new JPanel(new BorderLayout()); // Tab 4: Spot Map

        tabbedPane.add("Park Vehicle",      parkTab);
        tabbedPane.add("Checkout Vehicle", checkoutTab);
        tabbedPane.add("Admin Dashboard",   adminTab);
        tabbedPane.add("Spot Map",          mapTab);

        JTextArea statusConsole = new JTextArea(4, 50);
        statusConsole.setEditable(false);
        statusConsole.setFont(new Font("Monospaced", Font.BOLD, 18));
        statusConsole.setBackground(new Color(30, 30, 30)); // Extra dark for console
        statusConsole.setForeground(new Color(0, 255, 100)); // Hacker green text
        statusConsole.setText("System Initialized. Welcome to Parking Management System");
        JScrollPane scrollPane = new JScrollPane(statusConsole);

        // ── Tab 1: Park Vehicle ────────────────────────────────────────────────
        parkTab.setLayout(new GridBagLayout());

        JPanel arrivalPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        // FIX: Force white title border
        javax.swing.border.TitledBorder arriveBorder = BorderFactory.createTitledBorder("Arrive and Park");
        arriveBorder.setTitleColor(Color.WHITE);
        arrivalPanel.setBorder(arriveBorder);

        JLabel parkPlateLabel = new JLabel("Number Plate:");
        parkPlateLabel.setForeground(Color.WHITE);
        arrivalPanel.add(parkPlateLabel);

        JTextField plateField = new JTextField();
        arrivalPanel.add(plateField);

        JLabel typeLabel = new JLabel("Vehicle Type:");
        typeLabel.setForeground(Color.WHITE);
        arrivalPanel.add(typeLabel);

        String[] types = {"Motorcycle", "Car", "SUV"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        arrivalPanel.add(typeCombo);

        JLabel powerLabel = new JLabel("Power Source:");
        powerLabel.setForeground(Color.WHITE);
        arrivalPanel.add(powerLabel);

        String[] sources = {"Combustion", "Electric"};
        JComboBox<String> sourcesCombo = new JComboBox<>(sources);
        arrivalPanel.add(sourcesCombo);

        arrivalPanel.add(new JLabel("")); // Empty spacer is fine as is
        parkButton = new JButton("Park Vehicle");

        parkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numberPlate = plateField.getText().trim();

                if (numberPlate.isEmpty()) {
                    statusConsole.setText("Error: Number plate cannot be empty");
                    return;
                }
                if (parkingLot.isPlateParked(numberPlate)) {
                    statusConsole.setText("Security Alert: A vehicle with number plate [" + numberPlate + "] is already parked in the lot!");
                    return;
                }

                int typeIndex  = typeCombo.getSelectedIndex();
                int powerIndex = sourcesCombo.getSelectedIndex();

                Vehicle.PowerSource powerEnum = (powerIndex == 0)
                        ? Vehicle.PowerSource.COMBUSTION
                        : Vehicle.PowerSource.ELECTRIC;

                Vehicle newVehicle = null;
                if (typeIndex == 0)      newVehicle = new Motorcycle(numberPlate, powerEnum);
                else if (typeIndex == 1) newVehicle = new Car(numberPlate, powerEnum);
                else if (typeIndex == 2) newVehicle = new SUV(numberPlate, powerEnum);

                if (newVehicle != null) {
                    parkingLot.routeVehicle(newVehicle);
                    statusConsole.setText("System: Processed arrival for " + newVehicle.getSize() + " [" + numberPlate + "].\n");
                    refreshSpotMap();
                }
                plateField.setText("");
            }
        });
        arrivalPanel.add(parkButton);
        parkTab.add(arrivalPanel);

        // ── Tab 2: Checkout Vehicle ────────────────────────────────────────────
        checkoutTab.setLayout(new GridBagLayout());
        JPanel checkoutFormPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        // FIX: Force white title border
        javax.swing.border.TitledBorder checkoutBorder = BorderFactory.createTitledBorder("Checkout and Pay");
        checkoutBorder.setTitleColor(Color.WHITE);
        checkoutFormPanel.setBorder(checkoutBorder);

        JLabel checkoutLabel = new JLabel("Number Plate to checkout:");
        checkoutLabel.setForeground(Color.WHITE);
        checkoutFormPanel.add(checkoutLabel);

        JTextField checkoutPlateField = new JTextField();
        checkoutFormPanel.add(checkoutPlateField);

        checkoutFormPanel.add(new JLabel("")); // Empty spacer
        JButton checkoutButton = new JButton("Process Checkout");

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String plate = checkoutPlateField.getText().trim();

                if (plate.isEmpty()) {
                    statusConsole.setText("Error: Number plate cannot be empty");
                    return;
                }
                if (!parkingLot.isPlateParked(plate)) {
                    statusConsole.setText("Error: Vehicle [" + plate + "] is not currently in the parking lot!");
                    return;
                }

                parkingLot.removeVehicleByPlate(plate);
                statusConsole.setText("System: Checkout successful for [" + plate + "].\n(Note: Full detailed receipt printed to your IDE background console).");
                checkoutPlateField.setText("");
                refreshSpotMap();
            }
        });
        checkoutFormPanel.add(checkoutButton);
        checkoutTab.add(checkoutFormPanel);

        // ── Tab 3: Admin Dashboard  ─────────────
        CardLayout adminCardLayout = new CardLayout();
        adminTab.setLayout(adminCardLayout);

        JPanel adminLoginPanel = new JPanel(new GridBagLayout());

        JPanel loginBox = new JPanel(new GridLayout(3, 1, 10, 15));

        // FIX: Force white title border for Auth Box
        javax.swing.border.TitledBorder authBorder = BorderFactory.createTitledBorder("System Authentication");
        authBorder.setTitleColor(Color.WHITE);
        loginBox.setBorder(BorderFactory.createCompoundBorder(
                authBorder,
                BorderFactory.createEmptyBorder(20, 40, 20, 40)
        ));

        // FIX: Force white label text
        JLabel passLabel = new JLabel("Enter Admin Password:", SwingConstants.CENTER);
        passLabel.setForeground(Color.WHITE);
        loginBox.add(passLabel);

        JPasswordField passwordField = new JPasswordField(15);
        loginBox.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginBox.add(loginButton);

        adminLoginPanel.add(loginBox);

        JPanel adminDashboardPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // FIX: Force white giant label text
        JLabel revenueLabel = new JLabel("Total Revenue: $0.00", SwingConstants.CENTER);
        revenueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        revenueLabel.setForeground(Color.WHITE);

        // FIX: Force white giant label text
        JLabel spotsLabel = new JLabel("Available Spots: " + parkingLot.getAvailableSpots(), SwingConstants.CENTER);
        spotsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        spotsLabel.setForeground(Color.WHITE);

        JPanel logoutWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton logoutButton = new JButton("Logout & Secure System");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminCardLayout.show(adminTab, "LOGIN");
                statusConsole.setText("System: Admin dashboard secured and logged out.");
            }
        });
        logoutWrapper.add(logoutButton);

        adminDashboardPanel.add(revenueLabel);
        adminDashboardPanel.add(spotsLabel);
        adminDashboardPanel.add(logoutWrapper);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String( passwordField.getPassword());

                if (systemAdmin.authenticate(password)){
                    revenueLabel.setText("Total Revenue: $" + parkingLot.getRevenue());
                    spotsLabel.setText("Available Spots: " + parkingLot.getAvailableSpots());
                    adminCardLayout.show(adminTab, "DASHBOARD");

                    passwordField.setText("");
                    statusConsole.setText("System: Admin login successful.");
                } else {
                    statusConsole.setText("Security Alert: Incorrect Admin Password!");
                }
            }
        });


        adminTab.add(adminLoginPanel,     "LOGIN");
        adminTab.add(adminDashboardPanel, "DASHBOARD");

        // ── Tab 4: Spot Map ────────────────────────────────────────────────────
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 6));

        // FIX: Force white title border for Legend
        javax.swing.border.TitledBorder legendBorder = BorderFactory.createTitledBorder("Legend");
        legendBorder.setTitleColor(Color.WHITE);
        legendPanel.setBorder(legendBorder);

        legendPanel.add(colorBox(freeStandard)); legendPanel.add(new JLabel("Free (Standard)"));
        legendPanel.add(colorBox(freeEV));       legendPanel.add(new JLabel("Free (EV Charger)"));
        legendPanel.add(colorBox(occStandard));  legendPanel.add(new JLabel("Occupied (Standard)"));
        legendPanel.add(colorBox(occEV));        legendPanel.add(new JLabel("Occupied (EV)"));

        JButton refreshMapBtn = new JButton("Refresh Map");
        refreshMapBtn.addActionListener(e -> refreshSpotMap());
        legendPanel.add(refreshMapBtn);

        mapTab.add(legendPanel, BorderLayout.NORTH);

        spotMapContainer = new JPanel();
        spotMapContainer.setLayout(new BoxLayout(spotMapContainer, BoxLayout.Y_AXIS));
        JScrollPane mapScroll = new JScrollPane(spotMapContainer);
        mapTab.add(mapScroll, BorderLayout.CENTER);

        buildSpotMap();

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 3) refreshSpotMap();
        });

        // ── Assemble frame ─────────────────────────────────────────────────────
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // ── Spot Map helpers ───────────────────────────────────────────────────────
    private void buildSpotMap() {
        spotMapContainer.removeAll();

        java.util.ArrayList<ParkingSpot> spots = parkingLot.getAllSpots();

        addSectionHeader("Motorcycle Spots (M1 – M10)");
        addSpotsRow(spots, 0, 10);

        addSectionHeader("Compact / Car Spots (C1 – C20)");
        addSpotsRow(spots, 10, 20);
        addSpotsRow(spots, 20, 30);

        addSectionHeader("Large / SUV Spots (L1 – L10)");
        addSpotsRow(spots, 30, 40);

        spotMapContainer.revalidate();
        spotMapContainer.repaint();
    }

    private void refreshSpotMap() {
        buildSpotMap();
    }

    private void addSectionHeader(String title) {
        JLabel label = new JLabel("  " + title);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(Color.WHITE); // FIX: Ensure headers stand out
        label.setBorder(BorderFactory.createEmptyBorder(10, 5, 2, 5));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        spotMapContainer.add(label);
    }

    private void addSpotsRow(java.util.ArrayList<ParkingSpot> spots, int from, int to) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = from; i < to && i < spots.size(); i++) {
            row.add(makeSpotButton(spots.get(i)));
        }
        spotMapContainer.add(row);
    }

    private JButton makeSpotButton(ParkingSpot spot) {
        String bottomLine;
        if (spot.isAvailable()) {
            bottomLine = spot.hasEV_Charging() ? "EV FREE" : "FREE";
        } else {
            Vehicle v = spot.getCurrentOccupant();
            String plate = (v != null) ? v.getNumberPlate() : "???";
            if (plate.length() > 7) plate = plate.substring(0, 7);
            bottomLine = plate;
        }

        // FIX: Inject <font color='white'> directly into the HTML
        String label = "<html><center><font color='white'>" + spot.getSpotID()
                + "<br><font size='2'>" + bottomLine + "</font></font>"
                + "</center></html>";

        JButton btn = new JButton(label);
        btn.setPreferredSize(new Dimension(75, 50));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setFocusPainted(false);

        // Force text color to white for readability on dark backgrounds
        btn.setForeground(Color.WHITE);

        if (spot.isAvailable()) {
            btn.setBackground(spot.hasEV_Charging() ? freeEV : freeStandard);
        } else {
            btn.setBackground(spot.hasEV_Charging() ? occEV : occStandard);
        }

        return btn;
    }

    private JPanel colorBox(Color color) {
        JPanel box = new JPanel();
        box.setBackground(color);
        box.setPreferredSize(new Dimension(16, 16));
        box.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return box;
    }

    // ── Entry point ────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        // --- 1. SET GLOBAL DARK MODE PROPERTIES ---
        setupDarkMode();

        // --- 2. LAUNCH GUI ---
        SwingUtilities.invokeLater(() -> new ParkingLotGUI());
    }

    // ── DARK MODE CONFIGURATION ────────────────────────────────────────────────
    private static void setupDarkMode() {
        try {
            // Forces the UI to use a standard cross-platform look so our colors apply consistently
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Define our master colors
        Color bgDark = new Color(43, 43, 43);         // Main background (Dark Grey)
        Color fgLight = new Color(187, 187, 187);     // Main text (Light Grey)
        Color fieldBg = new Color(69, 73, 74);        // Text input background
        Color buttonBg = new Color(75, 110, 175);     // Nice muted blue for buttons
        Color buttonText = Color.WHITE;

        // Apply background/foreground to global components
        UIManager.put("Panel.background", bgDark);
        UIManager.put("Label.foreground", fgLight);

        // Text Fields
        UIManager.put("TextField.background", fieldBg);
        UIManager.put("TextField.foreground", fgLight);
        UIManager.put("TextField.caretForeground", fgLight); // Cursor color

        UIManager.put("PasswordField.background", fieldBg);
        UIManager.put("PasswordField.foreground", fgLight);
        UIManager.put("PasswordField.caretForeground", fgLight);

        // Combo Boxes (Dropdowns)
        UIManager.put("ComboBox.background", fieldBg);
        UIManager.put("ComboBox.foreground", fgLight);
        UIManager.put("ComboBox.selectionBackground", buttonBg);
        UIManager.put("ComboBox.selectionForeground", buttonText);

        // Buttons
        UIManager.put("Button.background", buttonBg);
        UIManager.put("Button.foreground", buttonText);

        // Tabbed Pane
        UIManager.put("TabbedPane.background", bgDark);
        UIManager.put("TabbedPane.foreground", fgLight);
        UIManager.put("TabbedPane.selected", fieldBg); // Highlights the active tab
        UIManager.put("TabbedPane.contentAreaColor", bgDark);

        // Borders and ScrollPanes
        UIManager.put("TitledBorder.titleColor", fgLight);
        UIManager.put("Viewport.background", bgDark);
        UIManager.put("ScrollPane.background", bgDark);
    }
}