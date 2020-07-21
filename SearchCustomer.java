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
	JTextArea customerArea,customerIdArea,customerPhoneArea;
	JTextField searchField;
	JButton searchBtn;
	Font font;
	Customer customer;
	
	SearchCustomer() {
		frame = new JFrame("Customer search");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1040,650);
		cont = new JPanel();
		cont.setLayout(null);
		customerArea = new JTextArea();
		customerArea.setEditable(false);
		customerIdArea = new JTextArea();
		customerIdArea.setEditable(false);
		customerPhoneArea = new JTextArea();
		customerPhoneArea.setEditable(false);
		searchField = new JTextField();
		searchBtn = new JButton("Search");
		font = new Font("Arial",Font.PLAIN,30);
		customer = null;
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
		
		frame.add(cont);
		frame.setVisible(true);
		searchField.requestFocus();
	}
	private void performSearch(String search) {
		customerIdArea.setText("");
		customerArea.setText("");
		customerPhoneArea.setText("");
		List<Customer> customers = new ArrayList<>();
		HttpURLConnection http = null;
		JSONObject obj = null;
		JSONTokener token;
		try {
			URL url = new URL("http://koistine.com/OrderKoistine/v1/");
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
			map.put("search_type","customer");
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
						String caddress = customerObject.getString("address");
						String ccity = customerObject.getString("city");
						String cpostcode = customerObject.getString("postcode");
						String cemail = customerObject.getString("email");
						String cphone = customerObject.getString("phone");
						customers.add(new Customer(cid,cname,caddress,cpostcode,ccity,cphone,cemail));
						
					}
					customerArea.setText("");
					int hPos = 5;
					for(Customer c : customers) {
						customerIdArea.append(c.customerid + "\n");
						customerArea.append(c.name + "\n");
						customerPhoneArea.append(c.phone + "\n");
						JButton cSelect = new JButton("Select");
						cont.add(cSelect);
						cSelect.setFont(font);
						cSelect.setBounds(824,hPos,200,35);
						hPos = hPos + 40;
						cSelect.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent ec) {
								customer = c;
								frame.dispose();
							}
						});
					}
				}
			}catch(Exception e) {
			}
		}
		
		
	}
	public Customer getCustomer() {
		return customer;
	}
	private void setLayout() {
		customerArea.setFont(font);
		customerIdArea.setFont(font);
		customerPhoneArea.setFont(font);
		searchField.setFont(font);
		searchBtn.setFont(font);
		
		customerIdArea.setBounds(5,5,100,550);
		customerArea.setBounds(105,5,300,550);
		customerPhoneArea.setBounds(405,5,409,550);
		searchField.setBounds(5,560,814,40);
		searchBtn.setBounds(819,560,200,40);
		
		cont.add(customerIdArea);
		cont.add(customerArea);
		cont.add(customerPhoneArea);
		cont.add(searchField);
		cont.add(searchBtn);
	}
}
