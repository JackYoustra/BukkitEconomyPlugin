package jackyoustra.economyplugin;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	public static ConcurrentHashMap<UUID, Double> moneyTable = new ConcurrentHashMap<>();
	public static final String moneyTablePath = "moneytable.bin";
	public static ConcurrentHashMap<Material, Double> valueTable = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Material, Double> marketValueTable = new ConcurrentHashMap<>();
	public static final String marketValueTablePath = "marketvalues.bin";
	private static long blocks = 0;
	
	
	public static void addToBlocks(int adding) {
		Main.blocks += blocks;
	}

	private static Plugin instance;
	
	@Override
	public void onEnable() {
		instance = this;
		initCommands();
		loadTables(); // TODO make rarity table relative to a polling of loaded blocks (magic number?)
		initListeners();
		DisplayManager.createScoreboards(getServer().getOnlinePlayers());
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		saveTables();
		super.onDisable();
	}
	
	
	public void loadTables(){
		try {
			Main.moneyTable = SLAPI.load(moneyTablePath);
			Main.marketValueTable = SLAPI.load(marketValueTablePath);
			StockManager.stockPortfolio = SLAPI.load(StockManager.stockPortfolioPath);
			
		} catch (Exception e) {
			getServer().getConsoleSender().sendMessage("could not load tables");
			e.printStackTrace();
		}
		if(marketValueTable.isEmpty()){
			initSeedTable();
		}
	}
	
	public void saveTables(){
		try {
			
			SLAPI.save(Main.moneyTable, moneyTablePath);
			SLAPI.save(marketValueTable, marketValueTablePath);
			SLAPI.save(StockManager.stockPortfolio, StockManager.stockPortfolioPath);
		} catch (Exception e) {
			getServer().getConsoleSender().sendMessage("could not save tables");
			e.printStackTrace();
		}
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
		getCommand("viewMaterialCost").setExecutor(new MatViewValueCommandExecutor());
		getCommand("sellItemInHand").setExecutor(new SellItemInHandCommandExecutor());
	}
	
	private void initSeedTable() {
		long totalMaterials = 0;
		for(Material mat : Material.values()){
			marketValueTable.put(mat, 100.0);
		}
		for(int i = 0; i < 10; i++){
			for(World w : getServer().getWorlds()){
				final World f = w;
				short[][] generatedChunk = w.getGenerator().generateExtBlockSections(w, new Random(), 200, 200, new ChunkGenerator.BiomeGrid() {
					
					@Override
					public void setBiome(int x, int z, Biome bio) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public Biome getBiome(int x, int z) {
						// TODO Auto-generated method stub
						return f.getBiome(200, 200);
					}
				});
				
				for(short[] s : generatedChunk){
					for(short currentMat : s){
						Material mat = Material.getMaterial(currentMat);
						marketValueTable.put(mat, marketValueTable.get(mat)+1);
						totalMaterials++;
					}
				}
			}
		}
		for(Material mat : Material.values()){
			marketValueTable.put(mat, totalMaterials/marketValueTable.get(mat));
		}
	}
	// obselete
	protected void oldInitSeedTable(){/*
		final Calendar calendarInstance = Calendar.getInstance();
		long time = calendarInstance.getTimeInMillis()*-1;
		marketValueTable = new ConcurrentHashMap<>();
		List<World> loadedWorlds = getServer().getWorlds();
		for(World currentWorld : loadedWorlds){
			Chunk[] loadedChunks = currentWorld.getLoadedChunks();
			for(Chunk currentChunk : loadedChunks){
				final int currentChunkX = currentChunk.getX();
				final int currentChunkY = 0;
				final int currentChunkZ = currentChunk.getZ();
				
				for(int x = currentChunkX; x < currentChunkX+16; x++){
					for(int y = currentChunkY; y < currentChunkY+128; y++){
						for(int z = currentChunkZ; z < currentChunkZ + 16; z++){
							Block currentBlock = currentWorld.getBlockAt(currentChunkX, currentChunkY, currentChunkZ);
							Material blockMaterial = currentBlock.getType();
							Double tnum = marketValueTable.get(blockMaterial);
							if(tnum == null) tnum = 0.0;
							marketValueTable.put(blockMaterial, tnum + 1);
							if(blockMaterial != Material.AIR){
								blocks++;
							}
						}
					}
				}
			}
		}
		time += calendarInstance.getTimeInMillis();
		getServer().getConsoleSender().sendMessage("Time to seed: " + time + "\n Number of non-air blocks cleared" + blocks);
		time = calendarInstance.getTimeInMillis()*-1;

		time += calendarInstance.getTimeInMillis();
		getServer().getConsoleSender().sendMessage("Time to get rarity values " + time);
		*/
	}
		
	private void initListeners(){
		getServer().getPluginManager().registerEvents(new LoginListener(), instance);
	}

	
	
}
