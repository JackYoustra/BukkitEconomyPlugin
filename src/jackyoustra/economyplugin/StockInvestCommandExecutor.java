package jackyoustra.economyplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StockInvestCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// taking out failure cases
		if(!(sender instanceof Player)){
			sender.sendMessage("Only a player can use this command");
			return false;
		}
		if(!BondInvestCommandExecutor.argLengthCheck(sender, args, 2)){
			return false;
		}
		
		
		Player investor = (Player)sender;
		int amount;
		try{
			amount = Integer.parseInt(args[0]);
		}catch(NumberFormatException e){
			investor.sendMessage("Improper format for amount of stocks you want to buy");
			return false;
		}
		try {
			StockManager.buyStock(investor, amount, args[1]);
			DisplayManager.updateBank(investor);
			return true;
		} catch (StockTransactionException e) {
			if(e.isTickerNotFound()){
				investor.sendMessage("Ticker not found!");
				return false;
			}
			if(e.isInsufficientCurrency()){
				investor.sendMessage("Insufficient currency to complete transaction!");
				return false;
			}
		}
		return false;
	}

}
