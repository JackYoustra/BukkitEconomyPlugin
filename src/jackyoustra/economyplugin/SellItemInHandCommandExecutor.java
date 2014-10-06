package jackyoustra.economyplugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class SellItemInHandCommandExecutor implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Only players can use this command");
			return false;
		}
		Player vendor = (Player)sender;
		
		ItemStack handStack = vendor.getItemInHand();
		Material stackType = handStack.getType();
		int numberOfItems = handStack.getAmount();
		
		double value = Main.marketValueTable.get(stackType);
		Main.marketValueTable.put(stackType, value/(numberOfItems+1));// put market value
		value *= numberOfItems;
		Main.moneyTable.put(vendor.getUniqueId(), Main.moneyTable.get(vendor.getUniqueId())+value);
		vendor.setItemInHand(new ItemStack(Material.AIR, 0));
		DisplayManager.updateBank(vendor);
		vendor.sendMessage("Sold");
		return true;
	}
	
}
