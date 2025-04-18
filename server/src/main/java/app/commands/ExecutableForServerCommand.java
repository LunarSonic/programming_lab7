package app.commands;
import network.ExecutionResponse;
import utility.User;
import java.io.Serializable;

/**
 * Интерфейс для выполнения серверных команд
 */
public interface ExecutableForServerCommand {
    ExecutionResponse execute(String[] args, Serializable objectArg, User user);
}

