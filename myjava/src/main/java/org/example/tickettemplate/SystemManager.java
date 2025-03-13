package org.example.tickettemplate;

import java.util.HashMap;
import java.util.Map;

public class SystemManager {
    private static final SystemManager instance = new SystemManager(new DefaultSystem());
    private final Map<String, System> ticketSystems;

    private SystemManager(System... systems) {
        this.ticketSystems = new HashMap<>();
        for (System sys : systems) {
            register(sys.getName(), sys);
        }
    }

    public static SystemManager getInstance() {
        return instance;
    }

    private void register(String name, System sys) {
        ticketSystems.put(name, sys);
    }

    public TicketResponse createTicketWithConfig(String context, String name, Ticket ticketCfg) throws Exception {
        System sys = ticketSystems.getOrDefault(name, ticketSystems.get("default"));
        java.lang.System.out.printf("[SystemManager][CreateTicketWithConfig]name: %s, ticket config: %s%n", name, ticketCfg);

        return sys.createTicket(context, ticketCfg);
    }
}
