package flatset.gui;

import flatset.auth.AuthManager;
import flatset.auth.User;

import javax.swing.*;
import java.awt.*;

public class AuthFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public AuthFrame() {
        setTitle("Authorization");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 180);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> handleAuth(false));
        registerButton.addActionListener(e -> handleAuth(true));
    }

    private void handleAuth(boolean isRegister) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password must not be empty.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = isRegister ? AuthManager.register(username, password)
                : AuthManager.login(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    (isRegister ? "Registration" : "Login") + " successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // закрыть окно авторизации

            // запустить главное окно
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame(user); // передаём авторизованного пользователя
                mainFrame.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this,
                    (isRegister ? "Registration" : "Login") + " failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
