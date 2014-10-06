package jackyoustra.economyplugin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

public class StockManager {
	public static ConcurrentHashMap<UUID, ConcurrentHashMap<String, Integer>> stockPortfolio = new ConcurrentHashMap<>(); // player to number of stocks
	public static final String stockPortfolioPath = "ubiquitousstockportfolio.bin";
	
	public static void buyStock(Player buyer, int amount, String ticker) throws StockTransactionException{ 
		double money = Main.moneyTable.get(buyer.getUniqueId());
		double price;
		try {
			price = getStockPrice(ticker);
		} catch (Exception e) {
			throw new StockTransactionException(false, true);
		}
		price *= amount; // total price
		if(money < price){
			throw new StockTransactionException(true, false);
		}
		
		
		money-=price;
		
		Main.moneyTable.put(buyer.getUniqueId(), money);
		// get stocks
		Map<String, Integer> personalPortfolio = stockPortfolio.get(buyer.getUniqueId());
		int numberOfStocks = amount;
		if(mapContainsStringKey(personalPortfolio, ticker)){
			numberOfStocks += personalPortfolio.get(ticker);
		}
		personalPortfolio.put(ticker, numberOfStocks);
	}
	
	public static boolean mapContainsStringKey(Map<String, Integer> map, String checkingKey){
		for(String currentString : map.keySet().toArray(new String[0])){
			if(currentString.equals(checkingKey)) return true;
		}
		return false;
	}
	
	public static void initializePortfolio(Player player){
		if(!stockPortfolio.containsKey(player.getUniqueId())){
			stockPortfolio.put(player.getUniqueId(), new ConcurrentHashMap<String, Integer>());
		}
	}
	
	public static void addStockForPlayer(Player player, String stock, int amount){
		Map<String, Integer> individualPortfolio = stockPortfolio.get(player.getUniqueId());
		individualPortfolio.put(stock, individualPortfolio.get(stock)+amount);
	}
	
	public static void sellStockForPlayer(Player player, String stock, int amount) throws StockTransactionException{ // return false if failed (would be negative, not found, etc)
		Map<String, Integer> individualPortfolio = stockPortfolio.get(player.getUniqueId());
		String[] keys = individualPortfolio.keySet().toArray(new String[0]);
		int currentLocation;
		for(currentLocation = 0; currentLocation < keys.length; currentLocation++){
			String currentKey = keys[currentLocation];
			if(currentKey.equalsIgnoreCase(stock)){
				// can do work
				Integer currentTotal = individualPortfolio.get(currentKey);
				if(currentTotal - amount < 0){
					throw new StockTransactionException(true, false);
				}
				// guarunteed to work
				currentTotal-=amount;
				
				// update money in bank
				double capitalGain;
				try {
					capitalGain = amount * getStockPrice(stock);
				} catch (Exception e) {
					throw new StockTransactionException(false, true);
				}
				if(currentTotal != 0){
					individualPortfolio.put(currentKey, currentTotal); // reset number of holdings
				}
				else{
					individualPortfolio.remove(currentKey);
				}
				Main.moneyTable.put(player.getUniqueId(), Main.moneyTable.get(player.getUniqueId())+capitalGain);
				DisplayManager.updateBank(player);
				return;
			}
		}
		player.sendMessage("not found");
		throw new StockTransactionException(false, true);
	}
	
	public static double getStockPrice(String ticker) throws Exception{
		// construct URL eg http://finance.yahoo.com/d/quotes.csv?s=GOOG+AAPL&f=snl1
		StringBuilder builder = new StringBuilder("http://finance.yahoo.com/d/quotes.csv?s=");
		builder.append(ticker);
		builder.append("&f=snl1");
		String response = executePost(builder.toString(), "");
		String[] elements = response.split(",");
		double price = Double.parseDouble(elements[elements.length-1]);
		if(price == 0.0) throw new Exception("Did not get ticker");
		return price;
		
	}
	private static String executePost(String targetURL, String urlParameters){
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
				
	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {
	    	
	      e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	  }
}
