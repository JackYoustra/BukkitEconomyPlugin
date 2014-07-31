package jackyoustra.economyplugin;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class LoginListener implements Listener {
	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(PlayerJoinEvent event){
		Map<Player, Double> mt = Main.moneyTable;
		Player cPlayer = event.getPlayer();
		DisplayManager.createScoreboard(cPlayer);
		if(!mt.containsKey(cPlayer)){
			mt.put(cPlayer, 5000.0); // Starting cash value
			cPlayer.sendMessage("CONSOLE: Welcome to my server. You start with $5k to invest or spend. Use it wisely!" + ChatColor.GRAY);
		}
		else{
			cPlayer.sendMessage("CONSOLE: Welcome Back!!!" + ChatColor.GRAY);
		}
		DisplayManager.updateBank(cPlayer);
		StockManager.initializePortfolio(cPlayer);
	}
	
	
}
