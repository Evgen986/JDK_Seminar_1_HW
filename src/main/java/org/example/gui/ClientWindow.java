package org.example.gui;

import org.example.client.Client;
import org.example.services.MessageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientWindow extends JFrame {
    // Положение и размеры окна
    private static final int POS_X = 100;
    private static final int POS_Y = 100;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 400;

    private JPanel connectionPanel = new JPanel(new GridLayout(1,2));
    private JPanel settingsConnectionPanel = new JPanel(new GridLayout(2,2));
    private JTextField ipAddressField = new JTextField();
    private JTextField portField = new JTextField();
    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton loginButton = new JButton("login");
    private JTextArea logArea = new JTextArea();
    private JScrollPane scrollPaneLog = new JScrollPane(logArea);
    private JPanel sendMessagePanel = new JPanel(new GridLayout(1, 2));
    private JTextField messageField = new JTextField();
    private JButton sendButton = new JButton("send");

    private final ServerWindow serverWindow;
    private final MessageService messageService = MessageService.getMessageService();
    private Client client;
    private boolean isAuthorize = false;

    /**
     * Конструктор класса. Задает настройки отображения окна и поведения при взаимодействии.
     * @param serverWindow серверное окно.
     */
    public ClientWindow(ServerWindow serverWindow){
        windowSettings();
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (serverWindow.isServerWorking())
                    authorize();
                if (isAuthorize) {
                    clientMessages(messageService.getClientMessage());
                }
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (serverWindow.isServerWorking()) {
                    String message = sendMessage();
                    serverWindow.newMessage(message);
                }
            }
        });
        this.serverWindow = serverWindow;
        serverWindow.addClientWindow(this);
    }

    /**
     * Общие настройки окна клиента.
     */
    private void windowSettings(){
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocation(POS_X,POS_Y);
        setSize( WIDTH, HEIGHT);
        logMessageSettings();
        sendMessageSettings();
        connectionSettings();
        add(connectionPanel, BorderLayout.NORTH);
        add(scrollPaneLog);
        add(sendMessagePanel, BorderLayout.SOUTH);
        setTitle("Chat client");
        setResizable(false);
        setVisible(true);
    }

    /**
     * Настройка блока подключения и авторизации пользователя.
     */
    private void connectionSettings(){
        settingConnectionSettings();
        connectionPanel.add(settingsConnectionPanel);
        JPanel buttonLogin = new JPanel(new GridLayout(1,2));
        buttonLogin.add(loginButton);
        connectionPanel.add(buttonLogin);
    }

    /**
     * Настройки блока авторизации.
     */
    private void settingConnectionSettings(){
        ipAddressField.setText("127.0.0.1");
        settingsConnectionPanel.add(ipAddressField);
        portField.setText("8090");
        settingsConnectionPanel.add(portField);
        settingsConnectionPanel.add(usernameField);
        passwordField.setText("123");
        settingsConnectionPanel.add(passwordField);
    }

    /**
     * Настройка блока с историей сообщений.
     */
    private void logMessageSettings(){
        logArea.setEditable(false);
    }

    /**
     * Настройка блока отправки сообщений.
     */
    private void sendMessageSettings(){
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (serverWindow.isServerWorking()) {
                    String message = sendMessage();
                    serverWindow.newMessage(message);
                }
            }
        });
        sendMessagePanel.add(messageField);
        sendMessagePanel.add(sendButton);
    }

    /**
     * Метод авторизации пользователя.
     */
    private void authorize(){
        String login = usernameField.getText();
        String password = passwordField.getText();
        if (messageService.authentication(login, String.valueOf(password.hashCode()))){
            isAuthorize = true;
            client = new Client(login);
            messageService.connection(client);
        }
        // Если авторизация не пройдена, выводится диалоговое окно
        else{
            JOptionPane.showMessageDialog(
                    this,
                    "Неверный логин или пароль!",
                    "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Метод отправки сообщений.
     * @return отправленное сообщение.
     */
    private String sendMessage(){
        String message = "";
        if (isAuthorize) {
            message = messageField.getText();
            messageService.sendMessage(client, message);
            return client.getName() + ": " + message + "\n";
        }
        return message;
    }

    /**
     * Вывод сообщения в блоке истории сообщений.
     * @param message сообщение для отображения.
     */
    public void clientMessages(String message){
        logArea.append(message);
    }

    /**
     * Проверка авторизации пользователя.
     * @return true - при авторизации, иначе false.
     */
    public boolean isAuthorize(){
        return isAuthorize;
    }
}
