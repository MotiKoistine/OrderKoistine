import java.util.List;

public class InsertOrder {
	List<Item> items;
	int customerId,deliveryId,userId;
	String itemString;
	
	InsertOrder(List<Item> items, int customerId,int deliveryId,int userId){
		this.items = items;
		this.customerId = customerId;
		this.deliveryId = deliveryId;
		this.userId = userId;
		createItemString();
		
	}
	private void createItemString() {
		itemString = "";
		for(Item i : items) {
			itemString = itemString + "(" + i.itemId + ")-" + i.itemName + "-" + i.amount + "x" + i.priceEdit + "; ";
		}
		System.out.println(itemString);
	}
}
