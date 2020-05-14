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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SearchCustomer {
	JFrame frame;
	JPanel cont;
	JTextArea customerArea;
	JTextField searchField;
	JButton searchBtn;
	Font font;
	Customer customer;
	SearchCustomer(){
		frame = new JFrame("Customer search");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1040,650);
		cont = new JPanel();
		cont.setLayout(null);
		customerArea = new JTextArea();
		customerArea.setEditable(false);
		searchField = new JTextField();
		searchBtn = new JButton("Search");
		font = new Font("Arial",Font.PLAIN,30);
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
		
		
		cont.add(customerArea);
		cont.add(searchField);
		cont.add(searchBtn);
		
		frame.add(cont);
		frame.setVisible(true);
		searchField.requestFocus();
	}
	private void performSearch(String search) {
		List<Customer> customers = new ArrayList<>();
		HttpURLConnection http = null;
		JSONObject obj = null;
		JSONTokener token;
		try {
			URL url = new URL("http://koistine.com/OrderApi/customers.php");
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
				if(!obj.isNull("customers")) {
					JSONArray customersArray = obj.getJSONArray("customers");
					Iterator<Object> it = customersArray.iterator();
					while(it.hasNext()) {
						JSONObject customerObject = (JSONObject)it.next();
						int cid = Integer.parseInt(customerObject.getString("customerid"));
						String cname = customerObject.getString("name");
						String cphone = customerObject.getString("phone");
						customers.add(new Customer(cid,cname,null,null,null,cphone,null));
						
					}
					customerArea.setText("");
					for(Customer c : customers) {
						customerArea.append(c.customerid + " " + c.name + " " + c.phone + "\n");
					}
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		
	}
	private void setLayout() {
		customerArea.setFont(font);
		searchField.setFont(font);
		searchBtn.setFont(font);
		
		customerArea.setBounds(5,5,1014,550);
		searchField.setBounds(5,560,814,40);
		searchBtn.setBounds(819,560,200,40);
	}
}
