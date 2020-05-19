public class Item{
	int itemId;
	String itemName;
	double priceOut;
	double priceIn;
	double priceEdit;
	int amount;
	Item(int itemId, String itemName, double priceOut, double priceIn,double priceEdit,int amount){
		this.itemId = itemId;
		this.itemName = itemName;
		this.priceOut = priceOut;
		this.priceIn = priceIn;
		this.priceEdit = priceEdit;
		this.amount = amount;
	}
}