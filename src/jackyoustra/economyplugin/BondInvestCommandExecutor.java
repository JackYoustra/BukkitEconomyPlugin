package jackyoustra.economyplugin;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BondInvestCommandExecutor implements CommandExecutor {

	public static boolean argLengthCheck(CommandSender sender, String[] args,
			int number) {
		if (args.length > number) {
			sender.sendMessage("Too many arguments");
			return false;
		} else if (args.length < number) {
			sender.sendMessage("Too few arguments");
			return false;
		}
		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command fcommand,
			String label, String[] args) {
		final Map<Player, Double> moneyTable = Main.moneyTable;
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (argLengthCheck(sender, args, 3)) {
				String index = args[1];
				String type = args[2];
				final double amount = Double.parseDouble(args[0]);
				if (moneyTable.get(player) < amount) {
					sender.sendMessage("not enough money to complete transaction");
					return false;
				}
				double interestRate = 0;
				long timeInSeconds = 0;
				// checking done
				switch (index.toLowerCase()) {
				case "ust":
					switch (type.toLowerCase()) {
					case "2sec": // experimental
						interestRate = 0.01;
						timeInSeconds = 2;
					case "2m": // 2 minute united states treasury bond, 00.05%
								// interest rate
						interestRate = 0.05;
						timeInSeconds = 2 * 60;
						break;
					case "5m":
						interestRate = 0.15;
						timeInSeconds = 5 * 60;
						break;
					case "10m":
						interestRate = 0.4;
						timeInSeconds = 10 * 60;
						break;
					case "ust 12hr":
						interestRate = 30.0; // slight correction, probably want
												// to
												// shift everything below
						timeInSeconds = 12 * 60 * 60;
						break;
					case "24hr":
						interestRate = 65.0;
						timeInSeconds = 24 * 60 * 60;
						break;
					default:
						sender.sendMessage("Not a valid bond index!");
						return false;
					}
					break;
				default:
					sender.sendMessage("Not a valid exchange!");
					return false;
				}
				final double innerInterestRate = interestRate; // final allows
																// for use
																// inside inner
																// loop

				moneyTable.put(player, moneyTable.get(player) - amount); // withdrawal
				DisplayManager.updateBank(player);
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() { // at end of time, runs
						moneyTable.put(player, amount + amount
								* (innerInterestRate / 100));
						DisplayManager.updateBank(player);
						player.sendMessage("repaid");
					}
				}, timeInSeconds * 1000);
			}
		}

		return false;
	}
	
}
