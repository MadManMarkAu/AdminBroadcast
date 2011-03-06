package net.madmanmarkau.AdminBroadcast;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

// Permissions:
// if (iStick.Permissions.has(player, "foo.bar")) {}

public class AdminBroadcast extends JavaPlugin {
	public final Logger log = Logger.getLogger("Minecraft");
    public PermissionHandler Permissions;
	public Configuration Config;
    public PluginDescriptionFile pdfFile;

	@Override
	public void onDisable() {
	    log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " unloaded");
	}

	@Override
	public void onEnable() {
		this.pdfFile = this.getDescription();

		setupPermissions();
		
		log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " loaded");
	}

	public void setupPermissions() {
		Plugin perm = this.getServer().getPluginManager().getPlugin("Permissions");
			
		if (this.Permissions == null) {
			if (perm!= null) {
				this.getServer().getPluginManager().enablePlugin(perm);
				this.Permissions = ((Permissions) perm).getHandler();
			}
			else {
				log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + "not enabled. Permissions not detected");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
	}

    public static String join(String[] sourceArray, String delimiter, int startPos) {
        StringBuffer buffer = new StringBuffer();
        int arrayLength = sourceArray.length;
        
        for (int index = startPos; index < arrayLength; index++) {
        	buffer.append(sourceArray[index]);
        	if (index < arrayLength - 1) {
        		buffer.append(delimiter);
        	}
        }

        return buffer.toString();
    }

	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
		String source;

		if (sender instanceof Player && !Permissions.has((Player) sender, "adminbroadcast")) {
			return false;
		}
		
		if (cmd.getName().compareToIgnoreCase("say") == 0) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				source = player.getName();
				
				if (!Permissions.has(player, "adminbroadcast.say")) {
					return false;
				}
			} else {
				source = "Console";
			}
			
			Player players[] = sender.getServer().getOnlinePlayers();
			String message = join(args, " ", 0);
			
			for (Player thisPlayer : players) {
				thisPlayer.sendMessage(ChatColor.DARK_PURPLE + "[" + source + "] " + message);
			}
			
	    	return true;
		} else if (cmd.getName().compareToIgnoreCase("gmsg") == 0) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				source = player.getName();

				if (!Permissions.has(player, "adminbroadcast.gmsg")) {
					return false;
				}
			} else {
				source = "Console";
			}
			
			Player players[] = sender.getServer().getOnlinePlayers();
			String message = join(args, " ", 1);
			
			for (Player thisPlayer : players) {
				if (Permissions.inGroup(thisPlayer.getWorld().getName(), thisPlayer.getName(), args[0])) {
					thisPlayer.sendMessage(ChatColor.DARK_PURPLE + "[(G) " + source + "] " + message);
				}
			}
			
	    	return true;
		} else if (cmd.getName().compareToIgnoreCase("omsg") == 0) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				source = player.getName();

				if (!Permissions.has(player, "adminbroadcast.omsg")) {
					return false;
				}
			} else {
				source = "Console";
			}
			
			Player players[] = sender.getServer().getOnlinePlayers();
			String message = join(args, " ", 0);
			
			for (Player thisPlayer : players) {
				if (thisPlayer.isOp()) {
					thisPlayer.sendMessage(ChatColor.DARK_PURPLE + "[(O) " + source + "] " + message);
				}
			}
			
	    	return true;
		}
		return false;
    }

}
