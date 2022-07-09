package me.merlin.chat;

import me.merlin.Cardinal.Cardinal;
import me.merlin.Cardinal.chat.ChatFormatListener;
import me.merlin.Factions;

public class ChatHandler {

    public ChatHandler() {
        Factions.getInstance().getServer().getPluginManager().registerEvents(new LocalChatListener(), Factions.getInstance());
    }
}
