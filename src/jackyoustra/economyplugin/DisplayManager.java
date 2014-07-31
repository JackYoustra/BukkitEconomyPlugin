package jackyoustra.economyplugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class DisplayManager {

	static ScoreboardManager manager = Bukkit.getScoreboardManager();
	static Map<Player, Scoreboard> scoreboards = new ConcurrentHashMap<Player, Scoreboard>();

	public static void createScoreboard(Player player) {

		Scoreboard board = manager.getNewScoreboard();

		Objective objective = board.registerNewObjective("dtvScoreboard", "dummy");
		Score money = objective.getScore((ChatColor.GREEN + "Money:"));

		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("Account");

		money.setScore(0);

		player.setScoreboard(board);
		scoreboards.put(player, board);

	}

	public static void setBank(Player player, int money) {
		Scoreboard board = scoreboards.get(player);

		Objective objective = board.getObjective(DisplaySlot.SIDEBAR);

		Score acctDisplay = objective.getScore((ChatColor.GREEN + "Money:"));

		acctDisplay.setScore(money);

		player.setScoreboard(board);
		scoreboards.put(player, board);
	}

	public static void updateBank(Player target) {
		Map<Player, Double> moneyTable = Main.moneyTable;
		setBank(target, moneyTable.get(target).intValue());
	}
}
