package jackyoustra.economyplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg2,
			String[] arg3) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage("CONSOLE: You have $" + Main.moneyTable.get(player.getUniqueId()) + ChatColor.GRAY);
			return true;
		}
		else{
			sender.sendMessage("Only the player can use this command!");
			return false;
		}
		
	}

}
