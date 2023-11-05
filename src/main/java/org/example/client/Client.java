package org.example.client;

/**
 * Класс клиента.
 */
public class Client {
    // Имя клиента
    private final String name;
    // Пароль клиента (для упрощения установлен по умолчанию 123)
    private final String password;

    /**
     * Конструктор класса клиента.
     * @param name имя клиента.
     */
    public Client(String name) {
        this.name = name;
        this.password = String.valueOf("123".hashCode());
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
