import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SelectDelivery {
	JFrame frame;
	JPanel cont;
	JTextArea deliveryArea;
	JTextField searchField;
	JButton searchBtn;
	Font font;
	DeliveryOption delivery;
	List<DeliveryOption> deliveryOptions;
	List<Item> items;
	DecimalFormatSymbols formatSymbols;
	
	SelectDelivery(List<Item> items){
		this.items = items;
		frame = new JFrame("Select delivery");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1040,650);
		cont = new JPanel();
		cont.setLayout(null);
		deliveryArea = new JTextArea();
		deliveryArea.setEditable(false);
		font = new Font("Arial",Font.PLAIN,30);
		formatSymbols = new DecimalFormatSymbols(Locale.US);
		setLayout();
		getOptions();
		
		frame.add(cont);
		frame.setVisible(true);
	}
	private void getOptions() {
		deliveryOptions = new ArrayList<DeliveryOption>();
		HttpURLConnection http = null;
		JSONTokener token;
		JSONObject obj = null;
		try {
			URL url = new URL("http://koistine.com/OrderKoistine/v1/");
			URLConnection con = url.openConnection();
			http = (HttpURLConnection)con;
			http.setRequestMethod("POST");
			http.setDoOutput(true);
		}catch(Exception e1) {
			
		}
		if(http != null) {
			Map<String,String> map = new HashMap<>();
			map.put("userid","80085y4y");
			map.put("search_type","delivery");
			StringJoiner sj = new StringJoiner("&");
			for(Map.Entry<String,String> entry : map.entrySet()) {
				sj.add(entry.getKey() + "=" + entry.getValue());
			}
			byte[] out = sj.toString().getBytes();
			int length = out.length;
			try {
				http.setFixedLengthStreamingMode(length);
				http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				http.connect();
				try (OutputStream os =  http.getOutputStream()){
					os.write(out);
				}
				token = new JSONTokener(http.getInputStream());
				obj = new JSONObject(token);
				if(!obj.isNull("delivery")) {
					JSONArray arr = obj.getJSONArray("delivery");
					Iterator<Object> it = arr.iterator();
					while(it.hasNext()) {
						JSONObject next = (JSONObject)it.next();
						int id = next.getInt("delivery_id");
						String name = next.getString("name");
						double price = next.getDouble("price");
						deliveryOptions.add(new DeliveryOption(id,name,price));
					}
					displayOptions();
				}
			}
			catch(Exception e1) {
				
			}
		}
	}
	private void displayOptions() {
		int hPos = 5;
		DecimalFormat dF = new DecimalFormat("0.00",formatSymbols);
		for(DeliveryOption dP : deliveryOptions) {
			deliveryArea.append(dP.name + " " + dF.format(dP.price) + "€\n");
			JButton dSelect = new JButton("Select");
			dSelect.setFont(font);
			dSelect.setBounds(824,hPos,200,35);
			hPos = hPos + 40;
			cont.add(dSelect);
			dSelect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					delivery = dP;
				}
			});
		}
	}
	private void setLayout() {
		deliveryArea.setFont(font);
		
		deliveryArea.setBounds(5,5,814,550);
		
		cont.add(deliveryArea);
	}
}
