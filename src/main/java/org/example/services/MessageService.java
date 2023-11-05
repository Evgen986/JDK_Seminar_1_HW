package org.example.services;

import org.example.client.Client;
import org.example.server.Server;

import java.util.List;

/**
 * Сервисный класс, осуществляющий работу с сущностями моделей.
 */
public class MessageService {
    // Поле сущности сервер
    private final Server server;

    // Поле для создания объекта сервиса в единственном экземпляре
    private static MessageService messageService;

    /**
     * Закрытый конструктор сервиса.
     */
    private MessageService() {
        this.server = Server.getServer();
    }

    /**
     * Получение объекта сервиса.
     * @return объект сервиса.
     */
    public static MessageService getMessageService(){
        if (messageService == null){
            messageService = new MessageService();
        }
        return messageService;
    }

    /**
     * Подключение клиента к серверу.
     * @param client подключающейся клиент.
     */
    public void connection(Client client){
        server.saveMessage("Server", String.format("%s подключился к беседе", client.getName()));
    }

    /**
     * Отправка сообщения на сервер.
     * @param client клиент отправляющий сообщение.
     * @param message текст сообщения.
     */
    public void sendMessage(Client client, String message){
        server.saveMessage(client.getName(), message);
        System.out.printf("Сообщение от %s с текстом: '%s' сохранено.", client.getName(), message);
    }

    /**
     * Получение сообщений клиентов.
     * @return сообщения клиентов.
     */
    public String getClientMessage(){
        StringBuilder sb = new StringBuilder();
            List<String> messages = server.getMessageClient();
            messages.forEach(message ->
                    sb.append(message).append("\n"));
        return sb.toString();
    }

    /**
     * Аутентификация пользователя на сервере.
     * @param username логин пользователя
     * @param password пароль пользователя
     * @return
     */
    public boolean authentication(String username, String password){
        Client clientFromDB = new Client(username);  // Имитация получения клиента из БД (пароль по умолчанию 123)
        return password.equals(clientFromDB.getPassword());
    }

    /**
     * Остановка сервера.
     */
    public void closeServer(){
        server.stopServer();
    }

    /**
     * Получить все сообщения хранящиеся на сервере.
     * @return все сообщения в строковом представлении.
     */
    public String getAllMessage(){
        StringBuilder sb = new StringBuilder();
        List <String> messages = server.getAllMessage();
        messages.forEach(message -> sb.append(message).append("\n"));
        return sb.toString();
    }
}
