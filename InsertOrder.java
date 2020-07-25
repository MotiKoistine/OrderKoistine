import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class InsertOrder {
	List<Item> items;
	int customerId,deliveryId,userId;
	String itemString;
	boolean orderDone;
	CheckOrder checkOrder;
	
	InsertOrder(List<Item> items, int customerId,int deliveryId,int userId,CheckOrder checkOrder){
		this.items = items;
		this.customerId = customerId;
		this.deliveryId = deliveryId;
		this.userId = userId;
		this.checkOrder = checkOrder;
		orderDone = false;
		createItemString();
		
	}
	private void createItemString() {
		itemString = "";
		for(Item i : items) {
			itemString = itemString + "(" + i.itemId + ")-" + i.itemName + "-" + i.amount + "x" + i.priceEdit + "; ";
		}
		sendOrder();
	}
	private void sendOrder() {
		if(!itemString.isEmpty()) {
			HttpURLConnection http = null;
			try {
				URL url = new URL("http://koistine.com/OrderKoistine/v1/insert.php");
				URLConnection con = url.openConnection();
				http = (HttpURLConnection) con;
				http.setRequestMethod("POST");
				http.setDoOutput(true);
			}catch(Exception e1) {
				
			}
			if(http != null) {
				LocalDate date = LocalDate.now();
				Map<String,String> map = new HashMap<>();
				map.put("userid","80085y4y");
				map.put("customer_id","" + customerId);
				map.put("delivery_id","" + deliveryId);
				map.put("seller_id","" + userId);
				map.put("date","" + date);
				map.put("items",itemString);
				StringJoiner sj = new StringJoiner("&");
				for(Map.Entry<String, String> entry : map.entrySet()) {
					sj.add(entry.getKey() + "=" + entry.getValue());
				}
				byte[] out = sj.toString().getBytes();
				int length = out.length;
				try {
					http.setFixedLengthStreamingMode(length);
					http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
					http.connect();
					try (OutputStream os = http.getOutputStream()){
						os.write(out);
					}
					InputStream is = new BufferedInputStream(http.getInputStream());
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String result = br.readLine();
					if(result.equals("Success")) {
						displayResult("Order succesfully created!");
					}else {
						displayResult("Error creating order!");
					}
					
				}catch(Exception e2) {
					
				}
			}
		}
	}
	private void displayResult(String result) {
		JFrame resultFrame = new JFrame("Result");
		JLabel resultLabel = new JLabel(result);
		JButton resultBtn = new JButton("Ok");
		Font font = new Font("Arial",Font.PLAIN,30);
		resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		resultFrame.setResizable(false);
		resultFrame.setSize(400,150);
		resultFrame.setLayout(null);
		resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		resultLabel.setBounds(5,5,390,40);
		resultBtn.setBounds(150,60,100,40);
		resultLabel.setFont(font);
		resultBtn.setFont(font);
		resultFrame.add(resultLabel);
		resultFrame.add(resultBtn);
		resultFrame.setVisible(true);
		resultFrame.getRootPane().setDefaultButton(resultBtn);
		resultBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resultFrame.dispose();
				checkOrder.setState();
			}
		});
	}
}
