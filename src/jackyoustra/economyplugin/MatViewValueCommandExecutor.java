package jackyoustra.economyplugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MatViewValueCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Material translatedMat = Material.getMaterial(args[0].toUpperCase());
		if(translatedMat == null){
			sender.sendMessage("Invalid material name!");
			return false;
		}
		//sender.sendMessage(Main.marketValueTable.toString());
		double value = Main.marketValueTable.get(translatedMat);
		sender.sendMessage("Broker: The value for " + args[0] + " material is " + value);
		return true;
	}

}
