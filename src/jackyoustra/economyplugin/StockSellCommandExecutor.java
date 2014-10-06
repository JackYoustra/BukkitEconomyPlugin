package jackyoustra.economyplugin;

import java.util.InputMismatchException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StockSellCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Only a player can use this command");
			return false;
		}
		if(!BondInvestCommandExecutor.argLengthCheck(sender, args, 2)){
			return false;
		}
		Player player = (Player)sender;
		int amount;
		try{
			amount = Integer.parseInt(args[0]);
		}catch(InputMismatchException e){
			player.sendMessage("Invalid amount input");
			return false;
		}catch (NumberFormatException e) {
			player.sendMessage("Error: misplaced ticker symbol");
			return false;
		}
		try {
			StockManager.sellStockForPlayer(player, args[1], amount);
		} catch (StockTransactionException e) {
			if(e.isTickerNotFound()){
				player.sendMessage("Ticker not owned");
			}
			if(e.isInsufficientCurrency()){
				player.sendMessage("You do not have that many stocks to sell!");
			}
			return false;
		}
		return true;
	}

}
