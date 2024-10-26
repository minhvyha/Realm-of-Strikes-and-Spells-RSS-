package screen.menu;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import screen.SelectionListener;

public class MapSelection extends JPanel {
    // UI Components
    private JLabel titleLabel = new JLabel("Select Your Allies", SwingConstants.CENTER);
    private JPanel buttonPanel = new JPanel();
    private JLabel selectedMapLabel = new JLabel("", SwingConstants.CENTER);
    private SelectionListener listener; // Listener to handle map selection events
    private String[] battleMapNames = { // Array of map names
            "Enchanted Forest", "Frozen Tundra", "Desert Dunes", "Desert Oasis",
            "Cavern Depths", "Autumn Woods", "Mystic Grove", "Dungeon Chambers",
            "Rocky Plateau"
    };
    private int map; // Variable to store selected map
    private boolean[] mapUnlocked; // Array to track unlocked maps

    // Constructor to set up MapSelection screen
    public MapSelection(SelectionListener listener, int map) {
        this.map = map;
        this.listener = listener;
        this.mapUnlocked = new boolean[battleMapNames.length]; // Initialize map lock array
        this.mapUnlocked[0] = true; // Unlock the first map by default
        setLayout(new BorderLayout());

        configureTitleLabel(); // Configure title label
        configureButtonPanel(); // Configure map button panel
        addTitleLabel(); // Add title label to layout
        addButtonPanel(); // Add button panel to layout
        addSouthPanel(); // Add the South panel (map selection status and navigation buttons)
        addNavBar(); // Add the navigation bar

        revalidate();
        repaint();
    }

    // Configure title label appearance
    private void configureTitleLabel() {
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setOpaque(true);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));
    }

    // Add title label to the top (North) of the layout
    private void addTitleLabel() {
        add(titleLabel, BorderLayout.NORTH);
    }

    // Configure the panel for map selection buttons
    private void configureButtonPanel() {
        buttonPanel.setLayout(new GridLayout(3, 3, 40, 40));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Dimension buttonSize = new Dimension(230, 200);
        for (int i = 1; i <= 9; i++) {
            JLayeredPane mapButton = createMapButton(i, buttonSize);
            buttonPanel.add(mapButton);
        }
    }

    // Add button panel to the center of the layout
    private void addButtonPanel() {
        add(buttonPanel, BorderLayout.CENTER);
    }

    // Add a panel at the bottom (South) for map status and navigation buttons
    private void addSouthPanel() {
        JPanel wrapper = new JPanel(new GridLayout(2, 1, 20, 0));
        wrapper.setBackground(Color.BLACK);

        configureSelectedMapLabel(); // Configure map status label
        wrapper.add(selectedMapLabel);
        wrapper.add(createButtonPanel()); // Add navigation buttons

        add(wrapper, BorderLayout.SOUTH);
    }

    // Set up the label that shows the selected map name
    private void configureSelectedMapLabel() {
        selectedMapLabel.setFont(new Font("Arial", Font.BOLD, 14));
        selectedMapLabel.setForeground(Color.WHITE);
        selectedMapLabel.setOpaque(true);
        selectedMapLabel.setBackground(Color.BLACK);
        selectedMapLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));
        selectedMapLabel.setText("Selected Map: " + battleMapNames[map - 1]);
    }

    // Create navigation bar
    private void addNavBar() {
        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbar.setBackground(Color.DARK_GRAY);

        String[] navItems = { "Home", "Map", "Characters", "Exit" };
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setForeground(Color.WHITE);
            navButton.setBackground(Color.DARK_GRAY);
            navButton.setBorderPainted(false);
            navButton.setFocusPainted(false);
            navButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            navbar.add(navButton);

            // Capture the current item in a final variable for the action listener
            final String currentItem = item;

            // Adding an action for each navigation item
            navButton.addActionListener(e -> {
                switch (currentItem) {
                    case "Home":
                        listener.onGuideBack();
                        break;
                    case "Map":
                        listener.onMenuMapSelected();
                        break;
                    case "Characters":
                        listener.onMenuCharacterSelected();
                        break;
                    case "Exit":
                        ImageIcon originalIcon = new ImageIcon(
                                getClass().getResource("/assets/logo.png"));

                        // Scale the image to 50x50 pixels
                        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50,
                                Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(scaledImage);

                        int confirm = JOptionPane.showOptionDialog(
                                null, // Parent component
                                "Are you sure you want to quit?", // Message
                                "Quit Confirmation", // Title
                                JOptionPane.YES_NO_OPTION, // Option type (Yes/No)
                                JOptionPane.QUESTION_MESSAGE, // Message type
                                scaledIcon, // Custom icon
                                null, // No custom button options
                                null); // Default button option
                        if (confirm == JOptionPane.YES_OPTION) {
                            System.exit(0);
                        }
                        break;
                }
            });
        }
        add(navbar, BorderLayout.NORTH);
    }

    // Create navigation buttons for Next and Back
    private JPanel createButtonPanel() {
        JPanel buttonPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanelWrapper.setBackground(Color.BLACK);

        JButton nextButton = createButton("Next");
        JButton backButton = createButton("Back");
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanelWrapper.add(backButton);
        buttonPanelWrapper.add(nextButton);

        return buttonPanelWrapper;
    }

    // Create a map selection button with overlay and label
    private JLayeredPane createMapButton(int mapIndex, Dimension buttonSize) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(buttonSize);

        JButton button = createMapImageButton(mapIndex, buttonSize);
        JPanel overlayPanel = createOverlayPanel(buttonSize);
        JLabel textLabel = createMapLabel(mapIndex, buttonSize);

        layeredPane.add(button, Integer.valueOf(0));
        layeredPane.add(overlayPanel, Integer.valueOf(1));
        layeredPane.add(textLabel, Integer.valueOf(2));

        layeredPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return layeredPane;
    }

    // Create an image button for each map
    private JButton createMapImageButton(int mapIndex, Dimension buttonSize) {
        JButton button = new JButton();
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBounds(30, 0, buttonSize.width, buttonSize.height);
        button.setHorizontalAlignment(SwingConstants.CENTER);

        button.addActionListener(e -> { // Update map selection on click
            this.map = mapIndex;
            selectedMapLabel.setText("Selected Map: " + battleMapNames[mapIndex - 1]);
        });

        String imagePath = "/assets/background/battleback" + mapIndex + ".png";
        URL resource = getClass().getResource(imagePath);
        if (resource != null) {
            ImageIcon originalIcon = new ImageIcon(resource);
            Image scaledImage = originalIcon.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } else {
            button.setText("New Option " + mapIndex);
            button.setBackground(Color.WHITE);
        }

        return button;
    }

    // Create an overlay panel for semi-transparent dark overlay effect
    private JPanel createOverlayPanel(Dimension buttonSize) {
        JPanel overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        overlayPanel.setBounds(30, 0, buttonSize.width, buttonSize.height);
        overlayPanel.setOpaque(false);
        return overlayPanel;
    }
    
    // Create a label with map name for each button
    private JLabel createMapLabel(int mapIndex, Dimension buttonSize) {
        JLabel textLabel = new JLabel(battleMapNames[mapIndex - 1], SwingConstants.CENTER);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Arial", Font.BOLD, 18));
        textLabel.setBounds(30, -40, buttonSize.width, buttonSize.height);
        return textLabel;
    }

    // Create navigation button for Next/Back functionality
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(100, 30));

        // Define button action
        button.addActionListener(e -> {
            if (text.equals("Next")) {
                if (mapUnlocked[map - 1]) { // Check if the current map is unlocked
                    listener.onMenuCharacterSelected();
                } else {
                    JOptionPane.showMessageDialog(this, "You need to complete the previous map to unlock the next one.", "Map Locked", JOptionPane.WARNING_MESSAGE);
                }
            } else if (text.equals("Back")) {
                listener.onMapSelected(-1);
            }
        });
        

        return button;
    }

    // Call this method when a map is completed to unlock the next one
    public void unlockNextMap(int completedMapIndex) {
        if (completedMapIndex < mapUnlocked.length - 1) {
            mapUnlocked[completedMapIndex + 1] = true; // Unlock the next map
        }
    }
}
