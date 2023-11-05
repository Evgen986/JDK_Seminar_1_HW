package org.example.gui;

import org.example.services.MessageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame {
    // Позиционирование и размер окна
    private static final int POS_X = 700;
    private static final int POS_Y = 500;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    // Панель кнопок
    private JPanel buttons = new JPanel(new GridLayout(1,2));
    // Кнопка запуска сервера
    private final JButton btnStart = new JButton("Start");
    // Кнопка остановки сервера
    private final JButton btnStop = new JButton("Stop");
    // История сообщений
    private final JTextArea log = new JTextArea();
    // Прокрутка истории сообщений
    private final JScrollPane scrollPaneLog = new JScrollPane(log);
    // Запущен ли сервер
    private boolean isServerWorking;
    // Объект сервиса с бизнес-логикой.
    private MessageService messageService;
    // Коллекция окон клиентов.
    private List<ClientWindow> clientsWindow = new ArrayList<>();

    /**
     * Конструктор класса.
     */
    public ServerWindow(){
        isServerWorking = false;
        settingsWindow();

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStart();
                log.append(messageService.getAllMessage());
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStop();
            }
        });
    }

    /**
     * Настройки окна.
     */
    public void settingsWindow(){
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CustomCloseAction());
        setVisible(true);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);

        // Добавление окна сообщений
        add(scrollPaneLog);

        // Добавление кнопок нижней панели
        buttons.add(btnStart);
        buttons.add(btnStop);
        add(buttons, BorderLayout.SOUTH);
    }

    /**
     * Поведение при нажатии кнопки Start.
     */
    private void btnStart(){
        if(!isServerWorking) {
            isServerWorking = true;
            messageService = MessageService.getMessageService();
        }
    }

    /**
     * Поведение при нажатии кнопки Stop.
     */
    private void btnStop(){
        if (isServerWorking){
            isServerWorking = false;
            messageService.closeServer();
            System.exit(0);
        }
    }
    /**
     * Класс реализующий поведение при закрытии окна.
     */
    private class CustomCloseAction extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            if (messageService != null) {
                messageService.closeServer();
            }
            System.exit(0);
        }
    }

    /**
     * Метод проверки, запущен ли сервер.
     * @return true - если сервер запущен, иначе false.
     */
    public boolean isServerWorking(){
        return isServerWorking;
    }

    /**
     * Вывод в окно нового сообщения и рассылка клиентам для вывода в их окнах.
     * @param message сообщение клиента.
     */
    public void newMessage(String message){
        log.append(message);
        for (ClientWindow client : clientsWindow){
            if (client.isAuthorize()) {
                client.clientMessages(message);
            }
        }
    }

    /**
     * Добавление нового окна клиента в коллекцию.
     * @param clientWindow окно клиента.
     */
    public void addClientWindow(ClientWindow clientWindow){
        clientsWindow.add(clientWindow);
    }
}
