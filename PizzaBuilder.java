import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class PizzaBuilder extends JFrame {

    private PizzaPanel pizzaPanel;
    private JComboBox<String> toppingBox;
    private JButton clearButton;
    private JButton randomButton;
    private JLabel infoLabel;

    /*
     * Constructor that creates the main application window.
     * It initializes all Swing components and connects them
     * to the pizza panel so the user can interact with it.
     */
    public PizzaBuilder() {
        super("Chiaras Italian Pizza Builder");

        pizzaPanel = new PizzaPanel();

        toppingBox = new JComboBox<>(new String[]{"Pepperoni", "Mushroom", "Olive"});
        clearButton = new JButton("Clear Pizza");
        randomButton = new JButton("Random Pizza");
        infoLabel = new JLabel("Choose a topping, then click on the pizza.");

        JPanel controls = new JPanel();
        controls.add(new JLabel("Topping:"));
        controls.add(toppingBox);
        controls.add(clearButton);
        controls.add(randomButton);
        controls.add(infoLabel);

        // Updates the current topping when user selects from dropdown
        toppingBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) toppingBox.getSelectedItem();
                pizzaPanel.setCurrentTopping(selected);
                infoLabel.setText("Current topping: " + selected);
            }
        });

        // Clears all toppings from the pizza
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pizzaPanel.clearToppings();
                infoLabel.setText("Pizza cleared.");
            }
        });

        // Generates a new pizza with randomly placed toppings
        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pizzaPanel.makeRandomPizza();
                infoLabel.setText("Random pizza created.");
            }
        });

        setLayout(new BorderLayout());
        add(pizzaPanel, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        pizzaPanel.setCurrentTopping((String) toppingBox.getSelectedItem());
    }
}

class PizzaPanel extends JPanel implements MouseListener {

    private ArrayList<Topping> toppings;
    private String currentTopping;

    private int centerX;
    private int centerY;
    private int crustRadius;
    private int sauceRadius;
    private int cheeseRadius;

    private Random random;

    /*
     * Initializes the pizza panel and sets up default values
     * such as the pizza size, position, and starting topping.
     * Also enables mouse interaction for placing toppings.
     */
    public PizzaPanel() {
        toppings = new ArrayList<>();
        currentTopping = "Mushroom";

        centerX = 390;
        centerY = 380;
        crustRadius = 230;
        sauceRadius = 205;
        cheeseRadius = 190;

        random = new Random();

        setBackground(new Color(245, 235, 220));
        addMouseListener(this);
    }

    /*
     * Sets the current topping that will be placed when the user clicks.
     */
    public void setCurrentTopping(String topping) {
        currentTopping = topping;
    }

    /*
     * Removes all toppings from the list and refreshes the display.
     */
    public void clearToppings() {
        toppings.clear();
        repaint();
    }

    /*
     * Generates a pizza with 18 toppings placed randomly inside
     * the pizza area. Ensures toppings only appear within bounds.
     */
    public void makeRandomPizza() {
        toppings.clear();

        String[] toppingNames = {"Pepperoni", "Mushroom", "Olive"};

        for (int i = 0; i < 18; i++) {
            int x;
            int y;

            // Keeps generating positions until inside pizza
            do {
                x = random.nextInt(320) + 230;
                y = random.nextInt(320) + 220;
            } while (!isInsidePizza(x, y));

            String topping = toppingNames[random.nextInt(toppingNames.length)];
            toppings.add(new Topping(x, y, topping));
        }

        repaint();
    }

    /*
     * Called automatically when the panel needs to be redrawn.
     * Draws the pizza base and all toppings currently stored.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawTitle(g);
        drawPizzaBase(g);

        for (Topping topping : toppings) {
            drawTopping(g, topping);
        }
    }

    /*
     * Draws the title and instructions at the top of the panel.
     */
    private void drawTitle(Graphics g) {
        g.setColor(new Color(80, 40, 20));
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("Build Your Pizza", 285, 60);

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Click inside the pizza to add toppings", 265, 88);
    }

    /*
     * Draws the pizza base including crust, sauce, and cheese layers.
     */
    private void drawPizzaBase(Graphics g) {
        g.setColor(new Color(194, 132, 67));
        g.fillOval(centerX - crustRadius, centerY - crustRadius, crustRadius * 2, crustRadius * 2);

        g.setColor(new Color(200, 55, 40));
        g.fillOval(centerX - sauceRadius, centerY - sauceRadius, sauceRadius * 2, sauceRadius * 2);

        g.setColor(new Color(248, 220, 105));
        g.fillOval(centerX - cheeseRadius, centerY - cheeseRadius, cheeseRadius * 2, cheeseRadius * 2);
    }

    /*
     * Determines which topping drawing method to call
     * based on the topping type.
     */
    private void drawTopping(Graphics g, Topping topping) {
        if (topping.type.equals("Pepperoni")) {
            drawPepperoni(g, topping.x, topping.y);
        } else if (topping.type.equals("Mushroom")) {
            drawMushroom(g, topping.x, topping.y);
        } else if (topping.type.equals("Olive")) {
            drawOlive(g, topping.x, topping.y);
        }
    }

    /*
     * Draws a pepperoni slice using circles and small details.
     */
    private void drawPepperoni(Graphics g, int x, int y) {
        g.setColor(new Color(170, 30, 30));
        g.fillOval(x - 12, y - 12, 24, 24);

        g.setColor(new Color(120, 15, 15));
        g.drawOval(x - 12, y - 12, 24, 24);

        g.fillOval(x - 5, y - 6, 4, 4);
        g.fillOval(x + 2, y - 2, 4, 4);
        g.fillOval(x - 1, y + 4, 4, 4);
    }

    /*
     * Draws a mushroom using an arc for the cap and rectangle for the stem.
     */
    private void drawMushroom(Graphics g, int x, int y) {
        g.setColor(new Color(210, 190, 150));
        g.fillArc(x - 12, y - 10, 24, 20, 0, 180);

        g.setColor(new Color(190, 170, 130));
        g.fillRect(x - 4, y - 1, 8, 12);

        g.setColor(new Color(130, 110, 80));
        g.drawArc(x - 12, y - 10, 24, 20, 0, 180);
        g.drawRect(x - 4, y - 1, 8, 12);
    }

    /*
     * Draws an olive as two circles to create a ring effect.
     */
    private void drawOlive(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.drawOval(x - 7, y - 7, 14, 14);
        g.drawOval(x - 5, y - 5, 10, 10);
    }

    /*
     * Checks if a point is inside the pizza area
     * so toppings cannot be placed outside.
     */
    private boolean isInsidePizza(int x, int y) {
        int dx = x - centerX;
        int dy = y - centerY;
        return dx * dx + dy * dy <= cheeseRadius * cheeseRadius;
    }

    /*
     * Handles mouse clicks and places a topping at the clicked location
     * if the click is inside the pizza.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (isInsidePizza(x, y)) {
            toppings.add(new Topping(x, y, currentTopping));
            repaint();
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    private class Topping {
        int x;
        int y;
        String type;

        /*
         * Represents a single topping with position and type.
         */
        public Topping(int x, int y, String type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }
}