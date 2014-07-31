package jackyoustra.economyplugin;

import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewPortfolioCommandExecutor implements CommandExecutor {

	// TODO make a synonym map that allows ordering of ticker keys to full company name synonyms
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("only a player can have a portfolio");
			return false;
		}
		Player player = (Player)sender;
		Map<String, Integer> folio = StockManager.stockPortfolio.get(player); // rets individual stock portfolio
		String[] keys = folio.keySet().toArray(new String[0]);
		Integer[] values = folio.values().toArray(new Integer[0]);
		
		player.sendMessage("Stock Portfolio:");
		if(values.length != 0){
			for(int i = 0; i < keys.length && i < values.length; i++){
				player.sendMessage(values[i] + " share of " + keys[i]);
			}
		}
		else{
			player.sendMessage("You have yet to buy any stocks");
		}
		return true;
	}

}
