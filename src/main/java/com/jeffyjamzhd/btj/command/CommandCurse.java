package com.jeffyjamzhd.btj.command;

import com.jeffyjamzhd.btj.api.CurseManager;
import com.jeffyjamzhd.btj.api.CurseRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

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
        return "/curse <add/remove> [player] [identifier]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] strings) {
        // Wrong use
        if (strings.length < 3)
            throw new WrongUsageException(getCommandUsage(sender), strings[1]);

        // Get player
        EntityPlayerMP player = getPlayer(sender, strings[1]);
        if (player == null) {
            throw new WrongUsageException("Could not find player %s", strings[1]);
        }

        // Get curse manager and add/remove curse
        CurseManager man = player.btj$getCurseManager();
        if (strings[0].equals("add")) {
            boolean curse = man.applyCurse(strings[2], player.getEntityWorld(), player);
            if (!curse) {
                throw new WrongUsageException("Curse already exists or identifier is malformed.");
            } else {
                notifyAdmins(sender, "Curse %s given to %s.".formatted(strings[2], strings[1]));
            }
        } else if (strings[0].equals("remove")) {
            boolean curse = man.removeCurse(strings[2], player.getEntityWorld(), player);
            if (!curse) {
                throw new WrongUsageException("Curse isnt in player or identifier is malformed.");
            } else {
                notifyAdmins(sender, "Curse %s removed from %s.".formatted(strings[2], strings[1]));
            }
        } else {
            throw new WrongUsageException(getCommandUsage(sender), strings[1]);
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] str) {
        return switch (str.length) {
            case 1 ->
                    getListOfStringsMatchingLastWord(str, "add", "remove");
            case 2 ->
                    getListOfStringsMatchingLastWord(str, MinecraftServer.getServer().getAllUsernames());
            case 3 ->
                    getListOfStringsMatchingLastWord(str, CurseRegistry.INSTANCE.getCurseNames().toArray(String[]::new));
            default -> null;
        };
    }
}
