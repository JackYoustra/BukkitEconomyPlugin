package jackyoustra.economyplugin;

public class StockTransactionException extends Exception {
	private static final long serialVersionUID = -314350176611476681L;
	
	private boolean insufficientCurrency, tickerNotFound;
	public StockTransactionException(boolean insufficientCurrency, boolean tickerNotFound){
		this.insufficientCurrency = insufficientCurrency;
		this.tickerNotFound = tickerNotFound;
	}
	public boolean isInsufficientCurrency() {
		return insufficientCurrency;
	}
	public boolean isTickerNotFound() {
		return tickerNotFound;
	}
}
