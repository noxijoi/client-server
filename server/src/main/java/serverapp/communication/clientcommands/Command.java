package serverapp.communication.clientcommands;

import serverapp.communication.cervercommands.Message;
import serverapp.managedb.DataBaseController;

public interface Command {
    Message execute(DataBaseController controller);
}
