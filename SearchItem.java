import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SearchItem {
	JFrame frame;
	JPanel cont;
	JTextArea itemArea;
	JTextField searchField;
	JButton searchBtn;
	Font font;
	Item item;
	
	SearchItem() {
		frame = new JFrame("Item search");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1040,650);
		cont = new JPanel();
		cont.setLayout(null);
		itemArea = new JTextArea();
		itemArea.setEditable(false);
		searchField = new JTextField();
		searchBtn = new JButton("Search");
		font = new Font("Arial",Font.PLAIN,30);
		item = null;
		setLayout();
		
		searchField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					performSearch(searchField.getText());
				}
			}
		});
		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performSearch(searchField.getText());
			}
		});
		
		
		cont.add(itemArea);
		cont.add(searchField);
		cont.add(searchBtn);
		
		frame.add(cont);
		frame.setVisible(true);
		searchField.requestFocus();
	}
	private void performSearch(String search) {
		List<Item> items = new ArrayList<>();
		HttpURLConnection http = null;
		JSONObject obj = null;
		JSONTokener token;
		try {
			URL url = new URL("http://koistine.com/OrderApi/items.php");
			URLConnection con = url.openConnection();
			http = (HttpURLConnection)con;
			http.setRequestMethod("POST");
			http.setDoOutput(true);
		}catch(Exception e) {
			
		}
		if(http != null) {
			Map<String,String> map = new HashMap<>();
			map.put("userid","80085y4y");
			map.put("search",search);
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
				try(OutputStream os = http.getOutputStream()){
					os.write(out);
				}
				token = new JSONTokener(http.getInputStream());
				obj = new JSONObject(token);
				if(!obj.isNull("items")) {
					JSONArray itemsArray = obj.getJSONArray("items");
					Iterator<Object> it = itemsArray.iterator();
					while(it.hasNext()) {
						JSONObject itemObject = (JSONObject)it.next();
						int iid = Integer.parseInt(itemObject.getString("itemid"));
						String iname = itemObject.getString("name");
						double ipriceout = itemObject.getDouble("priceout");
						double ipricein = itemObject.getDouble("pricein");
						int iamount = 1;
						items.add(new Item(iid,iname,ipriceout,ipricein,ipriceout,iamount,0));
						
					}
					itemArea.setText("");
					int hPos = 5;
					DecimalFormat df = new DecimalFormat("#.00");
					for(Item i : items) {
						itemArea.append(i.itemId + " " + i.itemName + " " + df.format(i.priceOut) + "€\n");
						JButton iSelect = new JButton("Select");
						cont.add(iSelect);
						iSelect.setBounds(824,hPos,200,35);
						cont.revalidate();
						hPos = hPos + 40;
						iSelect.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent ec) {
								item = i;
								frame.dispose();
							}
						});
					}
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	public Item getItem() {
		return item;
	}
	private void setLayout() {
		itemArea.setFont(font);
		searchField.setFont(font);
		searchBtn.setFont(font);
		
		itemArea.setBounds(5,5,814,550);
		searchField.setBounds(5,560,814,40);
		searchBtn.setBounds(819,560,200,40);
	}
}
