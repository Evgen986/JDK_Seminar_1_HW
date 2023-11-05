package org.example.server;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс сервера. Создается в единственном экземпляре.
 */
public class Server {
    /**
     * Поле с объектом сервера.
     */
    private static Server server;
    /**
     * Константа с путем до файла имитирующего БД.
     */
    private final String DB_MESSAGE_PATH = "./src/main/java/org/example/data_base/db_message.txt";

    /**
     * Поле файла БД.
     */
    private File file;
    /**
     * Поле с коллекцией хранящей данные переписки пользователей.
     */
    private Map<String, List<String>> historyMessage;

    public static final String NAME = "Server";

    /**
     * Закрытый конструктор класса.
     */
    private Server() {
        initialize();
    }

    /**
     * Метод получения объекта сервера.
     * @return объект сервера.
     */
    public static Server getServer(){
        if (server == null){
            server = new Server();
        }
        return server;
    }

    /**
     * Метод остановки сервера.
     */
    public void stopServer(){
        saveMessage(NAME, "Сервер остановлен! " + LocalDateTime.now());
        System.out.println(historyMessage.toString());
        writeDataBase();
        server = null;
    }

    /**
     * Инициализация сервера.
     */
    private void initialize(){
        historyMessage = new HashMap<>();
        file = new File(DB_MESSAGE_PATH);
        readDataBase();
        saveMessage(NAME, "Сервер запущен! " + LocalDateTime.now());
    }

    /**
     * Чтение из данных из БД.
     */
    private void readDataBase() {
        try (FileReader reader = new FileReader(file)){
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null){
                int separator = line.indexOf(':');
                String sender = line.substring(0, separator);
                String message = line.substring(separator + 1);
                if (historyMessage.containsKey(sender)){
                    historyMessage.get(sender).add(message);
                }else{
                    historyMessage.put(sender, new ArrayList<>(Arrays.asList(message)));
                }
                line = br.readLine();
            }
        }catch (IOException e){
            System.out.println("Ошибка соединения с базой данных");
        }
    }

    /**
     * Сохранение данных в БД.
     */
    private void writeDataBase(){
        try (FileWriter writer = new FileWriter(file, false)) {
            for(Map.Entry <String, List<String>> el : historyMessage.entrySet()){
                for (String message : el.getValue()){
                    writer.write(el.getKey() + ":" + message + "\n");
                }
            }
        }catch (IOException e){
            System.out.println("Ошибка соединения с базой данных");
        }
    }

    /**
     * Метод сохранения сообщений на сервере.
     * @param name имя пользователя отправившего сообщение.
     * @param message текст сообщения
     */
    public void saveMessage(String name, String message){
        if (historyMessage.containsKey(name)){
            historyMessage.get(name).add(message);
        }
        else {
            historyMessage.put(name, new ArrayList<>(Arrays.asList(message)));
        }
    }

    /**
     * Получение сообщений пользователей.
     * @return коллекцию сообщений пользователей.
     */
    public List<String> getMessageClient(){
        List<String> messages = new ArrayList<>();
        for(Map.Entry<String, List<String>> el : historyMessage.entrySet()){
            if (!el.getKey().equals(NAME)) {
                for (String message : el.getValue()) {
                    messages.add(el.getKey() + ": " + message);
                }
            }
        }
        return messages;
    }

    /**
     * Получение всех сообщений хранящихся на сервере.
     * @return коллекцию сообщений.
     */
    public List<String> getAllMessage(){
        List <String> allMessage = new ArrayList<>();
        for(Map.Entry<String, List<String>> el : historyMessage.entrySet()){
            for(String message : el.getValue()){
                allMessage.add(el.getKey() + ": " + message);
            }
        }
        return allMessage;
    }
}
