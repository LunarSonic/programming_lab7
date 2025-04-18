package app.commands;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Абстрактный класс, классы клиентских команд наследуются от него и реализуют метод execute()
 */
public abstract class ClientCommand implements ExecutableForClientCommand, Serializable {
    @Serial
    private static final long serialVersionUID = 41L;
    private final String commandName;
    private final String description;

    /**
     * Конструктор класса Command
     * @param name название команды
     * @param description описание команды
     */
    public ClientCommand(String name, String description) {
        this.commandName = name;
        this.description = description;
    }

    /**
     * Геттер для получения описания
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Геттер для получения названия команды
     * @return commandName
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Переопределённый метод toString()
     * @return строка в определённом виде
     */
    @Override
    public String toString() {
        return "Команда - " + "название: " +  getCommandName() + ", описание: " + getDescription();
    }

    /**
     * Переопределённый метод hashCode()
     * @return хэш код, который основан на названии и описании команды
     */
    @Override
    public int hashCode() {
        return Objects.hash(getCommandName(), getDescription());
    }

    /**
     * Переопределённый метод equals() для сравнения двух команд
     * @param obj объект, с которым происходит сравнение
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        ClientCommand other = (ClientCommand) obj;
        return Objects.equals(getCommandName(), other.getCommandName()) && Objects.equals(getDescription(), other.getDescription());
    }
}
