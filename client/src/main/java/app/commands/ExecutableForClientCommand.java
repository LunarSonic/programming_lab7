package app.commands;
import network.ExecutionResponse;
import utility.User;

public interface ExecutableForClientCommand {
    ExecutionResponse execute(String[] args, User user);
}
