
public class CheckOrder {
	boolean isReady;
	CheckOrder(){
		isReady = false;
	}
	public boolean checkState() {
		return isReady;
	}
	public void setState() {
		isReady = true;
	}
}
