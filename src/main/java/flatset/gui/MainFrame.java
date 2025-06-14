package flatset.gui;

import flatset.auth.User;
import flatset.commands.CommandManager;
import flatset.Flat;

import javax.swing.*;
import java.awt.*;
import java.util.Set;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class MainFrame extends JFrame {
    private final User currentUser;
    private final Set<Flat> flatSet;
    private final CommandManager commandManager;

    private JTextArea outputArea;
    private JTextField commandField;
    private JButton executeButton;

    public MainFrame(User user) {
        this.currentUser = user;
        this.flatSet = ConcurrentHashMap.newKeySet(); // пустой, пока без загрузки
        this.commandManager = new CommandManager(flatSet, currentUser, new Scanner(System.in));

        setTitle("Flat Manager - User: " + user.getUsername());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        commandField = new JTextField();
        executeButton = new JButton("Execute");

        JPanel commandPanel = new JPanel(new BorderLayout());
        commandPanel.add(commandField, BorderLayout.CENTER);
        commandPanel.add(executeButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(commandPanel, BorderLayout.SOUTH);

        // Обработка команды
        executeButton.addActionListener(e -> executeCommand());
        commandField.addActionListener(e -> executeCommand()); // по Enter
    }

    private void executeCommand() {
        String commandText = commandField.getText().trim();
        if (commandText.isEmpty()) return;

        appendOutput("> " + commandText);
        commandManager.setOutputHandler(this::appendOutput);
        commandManager.executeCommand(commandText);

        commandField.setText("");
    }

    private void appendOutput(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }
}
