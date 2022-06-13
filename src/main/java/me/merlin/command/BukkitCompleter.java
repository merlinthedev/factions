package me.merlin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BukkitCompleter implements TabCompleter {

    private Map<String, Map.Entry<Method, Object>> completers = new HashMap<>();

    public void addCompleter(String label, Method method, Object instance) {
        completers.put(label, new AbstractMap.SimpleEntry<>(method, instance));
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        for(int i = args.length; i >= 0; i--) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(label.toLowerCase());
            for(int x = 0; x < i; x++) {
                if(!args[x].equals("") && !args[x].equals(" ")) {
                    buffer.append("." + args[x].toLowerCase());
                }
            }
            String cmdLabel = buffer.toString();
            if(completers.containsKey(cmdLabel)) {
                Map.Entry<Method, Object> entry = completers.get(cmdLabel);
                try {
                    return (List<String>) entry.getKey().invoke(entry.getValue(),
                            new CommandArgs(sender, command, label, args, cmdLabel.split("\\.").length - 1));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
