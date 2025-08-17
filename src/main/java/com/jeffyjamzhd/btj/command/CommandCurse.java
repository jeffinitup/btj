package com.jeffyjamzhd.btj.command;

import com.jeffyjamzhd.btj.api.CurseManager;
import com.jeffyjamzhd.btj.api.CurseRegistry;
import com.jeffyjamzhd.btj.api.curse.ICurse;
import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;

import java.util.List;

public class CommandCurse extends CommandBase {
    @Override
    public String getCommandName() {
        return "curse";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/curse <add/remove> [identifier]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] strings) {
        if (strings.length < 2) {
            getCommandUsage(sender);
            return;
        }

        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        CurseManager man = player.btj$getCurseManager();

        if (strings[0].equals("add")) {
            boolean curse = man.applyCurse(strings[1], player.getEntityWorld(), player);
            if (!curse)
                sender.sendChatToPlayer(ChatMessageComponent.createFromText("Curse already exists or identifier is malformed."));
        } else if (strings[0].equals("remove")) {
            boolean curse = man.removeCurse(strings[1], player.getEntityWorld(), player);
            if (!curse)
                sender.sendChatToPlayer(ChatMessageComponent.createFromText("Curse isn't in player or identifier is malformed."));
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] str) {
        return str.length > 1 ?
                getListOfStringsMatchingLastWord(str, CurseRegistry.INSTANCE.getCurseNames().toArray(String[]::new)) : null;
    }
}
