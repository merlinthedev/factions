package me.merlin.balance;

import me.merlin.Factions;
import me.merlin.balance.commands.BalanceCommands;

public class BalanceHandler {

    public BalanceHandler() {
        Factions.getCommandFramework().registerCommands(new BalanceCommands());
    }
}
