package jackyoustra.economyplugin;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	public static ConcurrentHashMap<Player, Double> moneyTable = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Material, Double> valueTable = new ConcurrentHashMap<>();
	
	private static Plugin instance;
	
	@Override
	public void onEnable() {
		instance = this;
		initCommands();
		initValueTable();
		initListeners();
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		
		super.onDisable();
	}
	
	public static Plugin getInstance() {
		return instance;
	}

	private void initCommands(){
		getCommand("investBond").setExecutor(new BondInvestCommandExecutor());
		getCommand("investStock").setExecutor(new StockInvestCommandExecutor());
		getCommand("balance").setExecutor(new BalanceCommandExecutor());
		getCommand("sellStock").setExecutor(new StockSellCommandExecutor());
		getCommand("viewPortfolio").setExecutor(new ViewPortfolioCommandExecutor());
	}
	
	private void initValueTable(){
		valueTable.put(Material.COBBLESTONE, 5.0);
		valueTable.put(Material.DIAMOND_BLOCK, 5000.0);
		valueTable.put(Material.DIRT, 1.0);
		valueTable.put(Material.REDSTONE, 200.0);
	}
	
	private void initListeners(){
		getServer().getPluginManager().registerEvents(new LoginListener(), instance);
	}

	
	
}
