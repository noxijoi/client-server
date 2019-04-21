package serverapp.communication.clientcommands;

import java.io.Serializable;

public abstract class BaseCommand implements Serializable, Command {
    protected String commandDescription;

    public String getCommandDescription() {
        return commandDescription;
    }


}
