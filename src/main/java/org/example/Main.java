import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Kleurentabel Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        // Invoerveld voor basis kleur
        JTextField colorInputField = new JTextField("120, 200, 160"); // Voorbeeld: lichtgroen
        inputPanel.add(colorInputField, BorderLayout.NORTH);

        // Knop om de kleurentabel te genereren
        JButton generateButton = new JButton("Genereer Kleurentabel");
        inputPanel.add(generateButton, BorderLayout.CENTER);

        // Knop om een willekeurige basis kleur te genereren
        JButton randomColorButton = new JButton("Willekeurige Basis Kleur");
        inputPanel.add(randomColorButton, BorderLayout.SOUTH);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        JPanel colorAndButtonPanel = new JPanel();
        colorAndButtonPanel.setLayout(new BorderLayout());

        JPanel colorContainer = new JPanel(); // Container voor de kleurpanelen
        colorContainer.setLayout(new GridLayout(5, 1)); // Plaats de kleurpanelen in een rij

        // Voeg een scheiding (margin) toe tussen de kleurpanelen
        int marginSize = 10;
        colorContainer.setBorder(BorderFactory.createEmptyBorder(marginSize, marginSize, marginSize, marginSize));

        colorAndButtonPanel.add(colorContainer, BorderLayout.CENTER);

        mainPanel.add(colorAndButtonPanel, BorderLayout.CENTER);

        // Luisteraar voor de knop om kleurentabel te genereren
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = colorInputField.getText();
                Color baseColor = parseColor(inputText);
                if (baseColor != null) {
                    generateColorTable(colorContainer, baseColor);
                } else {
                    JOptionPane.showMessageDialog(frame, "Ongeldige kleurinvoer. Gebruik het formaat 'R, G, B'.", "Fout", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Luisteraar voor de knop om een willekeurige basis kleur te genereren
        randomColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color randomColor = generateRandomColor();
                colorInputField.setText(randomColor.getRed() + ", " + randomColor.getGreen() + ", " + randomColor.getBlue());
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JPanel createColorPanel(String label, Color color) {
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(color);

        JLabel labelComponent = new JLabel(label + " (R: " + color.getRed() + ", G: " + color.getGreen() + ", B: " + color.getBlue() + ")");
        labelComponent.setHorizontalAlignment(JLabel.CENTER);
        colorPanel.add(labelComponent);
        labelComponent.setForeground(new Color(255, 243, 243, 169)); // Grijs met transparantie

        return colorPanel;
    }

    private static Color getOppositeColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float oppositeHue = (hsb[0] + 0.5f) % 1.0f;
        return Color.getHSBColor(oppositeHue, hsb[1], hsb[2]);
    }

    private static Color darkenColor(Color color, double factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float newBrightness = Math.max(hsb[2] - (float) factor, 0.0f);
        return Color.getHSBColor(hsb[0], hsb[1], newBrightness);
    }

    private static Color lightenColor(Color color, double factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float newBrightness = Math.min(hsb[2] + (float) factor, 1.0f);
        return Color.getHSBColor(hsb[0], hsb[1], newBrightness);
    }

    private static void generateColorTable(JPanel panel, Color baseColor) {
        // Verwijder alle componenten uit het paneel
        panel.removeAll();

        // Voeg de kleurpanelen opnieuw toe
        panel.add(createColorPanel("Basis Kleur", baseColor));

        Color darkerColor = darkenColor(baseColor, 0.1);
        panel.add(createColorPanel("Donkerder dan Basis", darkerColor));

        Color lighterColor = lightenColor(baseColor, 0.1);
        panel.add(createColorPanel("Lichter dan Basis", lighterColor));

        Color oppositeColor = getOppositeColor(baseColor);
        panel.add(createColorPanel("Tegenovergestelde Kleur", oppositeColor));

        Color lighterOppositeColor = lightenColor(oppositeColor, 0.1);
        panel.add(createColorPanel("Lichter dan Tegenovergestelde", lighterOppositeColor));

        // Herlaad het paneel
        panel.revalidate();
        panel.repaint();
    }

    private static Color parseColor(String input) {
        try {
            String[] components = input.split(",");
            int red = Integer.parseInt(components[0].trim());
            int green = Integer.parseInt(components[1].trim());
            int blue = Integer.parseInt(components[2].trim());

            return new Color(red, green, blue);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private static Color generateRandomColor() {
        Random rand = new Random();
        int red = rand.nextInt(256); // Willekeurige waarde tussen 0 en 255
        int green = rand.nextInt(256);
        int blue = rand.nextInt(256);
        return new Color(red, green, blue);
    }
}
