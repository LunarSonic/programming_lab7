package commands;
import java.io.Serial;
import java.io.Serializable;

/**
 * Enum из названий команд (используем его экземпляры для передачи на сервер)
 */
public enum CommandName implements Serializable {
    add("add"),
    add_if_max("add_if_max"),
    add_if_min("add_if_min"),
    clear("clear"),
    execute_script("execute_script"),
    exit("exit"),
    help("help"),
    history("history"),
    info("info"),
    max_by_postal_address("max_by_postal_address"),
    remove_all_by_annual_turnover("remove_all_by_annual_turnover"),
    remove_by_id("remove_by_id"),
    save("save"),
    show("show"),
    sum_of_annual_turnover("sum_of_annual_turnover"),
    update("update");
    
    @Serial
    private static final long serialVersionUID = 2L;
    private final String name;

    CommandName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
