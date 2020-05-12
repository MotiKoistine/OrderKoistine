public class Item{
	int itemId;
	String itemName;
	double priceOut;
	double priceIn;
	int amount;
	Item(int itemId, String itemName, double priceOut, double priceIn,int amount){
		this.itemId = itemId;
		this.itemName = itemName;
		this.priceOut = priceOut;
		this.priceIn = priceIn;
		this.amount = amount;
	}
}