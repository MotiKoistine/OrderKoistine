public class Item{
	int itemId;
	String itemName;
	double priceOut;
	double priceIn;
	double priceEdit;
	int amount;
	int pos;
	Item(int itemId, String itemName, double priceOut, double priceIn,double priceEdit,int amount,int pos){
		this.itemId = itemId;
		this.itemName = itemName;
		this.priceOut = priceOut;
		this.priceIn = priceIn;
		this.priceEdit = priceEdit;
		this.amount = amount;
		this.pos = pos;
	}
}