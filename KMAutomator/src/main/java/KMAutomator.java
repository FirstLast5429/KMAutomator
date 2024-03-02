//Made By ChatGPT(Entirely)

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;


public class KMAutomator {
    public static void main(String[] args) {
        // Create a JFrame (window) with title "Text Box Window"
        JFrame frame = new JFrame("Text Box Window");

        // Set the size of the window
        frame.setSize(400, 300);

        // Create a JTextArea (text area) with alignment to top-left
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true); // Enable line wrapping
        textArea.setWrapStyleWord(true); // Wrap at word boundaries
        textArea.setAlignmentX(Component.LEFT_ALIGNMENT); // Align text to the left
        textArea.setAlignmentY(Component.TOP_ALIGNMENT); // Align text to the top

        // Create a JLabel (label) for some text
        JLabel label = new JLabel("Type your script in the textfield above. Press run to execute script.");

        // Create a JButton (button) labeled "Run"
        JButton button = new JButton("Run");

        // Create a JPanel to hold the label and button horizontally
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(label);
        panel.add(button);

        // Create a JPanel to hold the text area
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Create a JPanel to hold the label and button panel at the bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(panel, BorderLayout.NORTH);

        // Add action listener to the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeScript(textArea.getText());
            }
        });

        // Add the text area and bottom panel to the window
        frame.add(textPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Make the window visible
        frame.setVisible(true);

        // Set default close operation to exit the application when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Start a thread to continuously display mouse position in console
        Thread mousePositionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
                        System.out.println("Mouse Position: " + mousePoint.x + ", " + mousePoint.y);
                        Thread.sleep(1000); // Sleep for 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mousePositionThread.start(); // Start the thread
    }

    // Method to execute the script
    private static void executeScript(String script) {
        String[] lines = script.split("\n");

        // Create Robot instance
        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            return; // Exit if Robot couldn't be initialized
        }

        // Loop through each line of the script
        for (String line : lines) {
            String[] parts = line.split(" ");
            if (parts.length < 1)
                continue;

            String command = parts[0];

            switch (command) {
                case "MouseLocation":
                    // Move mouse pointer to the specified location
                    if (parts.length >= 3) {
                        try {
                            int x = Integer.parseInt(parts[1]);
                            int y = Integer.parseInt(parts[2]);
                            robot.mouseMove(x, y);
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid coordinates provided for MouseLocation command.");
                        }
                    }
                    break;
                case "LeftMouseClick":
                    // Perform a left mouse click
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                case "LeftMouseDown":
                    // Press the left mouse button
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                case "LeftMouseUp":
                    // Release the left mouse button
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                case "RightMouseClick":
                    // Perform a right mouse click
                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    break;
                case "RightMouseDown":
                    // Press the right mouse button
                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    break;
                case "RightMouseUp":
                    // Release the right mouse button
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    break;
                case "Type":
                    // Join the parts of the command to get the text to type
                    String textToType = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                    // Simulate typing the text
                    typeString(robot, textToType);
                    break;
                case "KeyDown":
                    // Check if the parts length is sufficient
                    if (parts.length >= 2) {
                        // Parse the key code
                        int keyCode = KeyEvent.getExtendedKeyCodeForChar(parts[1].charAt(0));
                        // Press the key
                        robot.keyPress(keyCode);
                    }
                    break;
                case "KeyUp":
                    // Check if the parts length is sufficient
                    if (parts.length >= 2) {
                        // Parse the key code
                        int keyCode = KeyEvent.getExtendedKeyCodeForChar(parts[1].charAt(0));
                        // Release the key
                        robot.keyRelease(keyCode);
                    }
                    break;
                case "Delay":
                    // Check if the parts length is sufficient
                    if (parts.length >= 2) {
                        // Parse the delay time in seconds
                        try {
                            int delaySeconds = Integer.parseInt(parts[1]);
                            // Convert delay time to milliseconds
                            long delayMilliseconds = delaySeconds * 1000L;
                            // Introduce delay in script execution
                            Thread.sleep(delayMilliseconds);
                        } catch (NumberFormatException | InterruptedException ex) {
                            System.out.println("Invalid delay specified for Delay command.");
                        }
                    }
                    break;
                case "Loop":
                    // Check if the parts length is sufficient
                    if (parts.length >= 4 && parts[2].equals("(") && parts[parts.length - 1].equals(")")) {
                        // Parse the number of iterations
                        try {
                            int loopTimes = Integer.parseInt(parts[1]);
                            // Get the index of the start of the loop block
                            int startIndex = 0;
                            for (int i = 0; i < parts.length; i++) {
                                if (parts[i].equals("(")) {
                                    startIndex = i + 1;
                                    break;
                                }
                            }
                            // Get the index of the end of the loop block
                            int endIndex = 0;
                            for (int i = parts.length - 1; i >= 0; i--) {
                                if (parts[i].equals(")")) {
                                    endIndex = i;
                                    break;
                                }
                            }

                            // Extract the loop block
                            StringBuilder loopBlock = new StringBuilder();
                            for (int i = startIndex; i < endIndex; i++) {
                                loopBlock.append(parts[i]).append(" ");
                            }

                            // Execute the loop block 'loopTimes' number of times
                            for (int i = 0; i < loopTimes; i++) {
                                executeScript(loopBlock.toString());
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid number of iterations specified for Loop command.");
                        }
                    }
                    break;



                // Handle other commands...
            }
        }
    }

    // Helper method to simulate typing a string
    private static void typeString(Robot robot, String text) {
        for (char c : text.toCharArray()) {
            // Simulate typing each character
            switch (c) {
                case '\n':
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                    break;
                default:
                    // Convert the character to its corresponding key code and simulate key press/release
                    int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    break;
            }
            // Introduce a small delay between key presses to simulate typing speed
            robot.delay(50);
        }
    }
}
