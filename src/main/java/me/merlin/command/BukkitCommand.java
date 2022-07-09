package me.merlin.command;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommand extends org.bukkit.command.Command {

    private final Plugin owningPlugin;
    protected BukkitCompleter completer;
    private CommandExecutor executor;


    protected BukkitCommand(String name, CommandExecutor executor, Plugin owner) {
        super(name);
        this.executor = executor;
        this.owningPlugin = owner;
        this.usageMessage = "";
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        boolean success = false;
        if (!owningPlugin.isEnabled()) {
            return false;
        }

        if (!testPermission(commandSender)) {
            return true;
        }

        try {
            success = executor.onCommand(commandSender, this, s, strings);
        } catch (Throwable throwable) {
            throw new CommandException("Unhandled exception executing command '" + s + "' in plugin " + owningPlugin.getDescription().getFullName(), throwable);
        }

        if (!success && usageMessage.length() > 0) {
            for (String line : usageMessage.replace("<command>", s).split("\n")) {
                commandSender.sendMessage(line);
            }
        }

        return success;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args)
            throws CommandException, IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        List<String> completions = new ArrayList<>();

        int currentArg = args.length - 1;
        Bukkit.getOnlinePlayers().stream().filter(p -> !completions.contains(p.getName()) && p.getName().startsWith(args[currentArg])).forEach(p -> completions.add(p.getName()));

        return completions;
    }

}
