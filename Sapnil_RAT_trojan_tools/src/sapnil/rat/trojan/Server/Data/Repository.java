package sapnil.rat.trojan.Server.Data;

import sapnil.rat.trojan.Server.ClientObject;

import java.util.HashMap;

public interface Repository {
    /* Store client connections for use of checking what connections are active. */
    HashMap<String, ClientObject> CONNECTIONS = new HashMap<>();
}

