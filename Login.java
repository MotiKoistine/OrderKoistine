import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import javax.swing.*;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Login{
	JFrame frame;
	JPanel cont;
	JLabel unLabel, pwLabel, resultLabel;
	JTextField unField;
	JPasswordField pwField;
	JButton loginBtn;
	Font font;
	
	Login(){
		frame = new JFrame("Login");
		frame.setSize(300,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		font = new Font("Arial",Font.PLAIN,20);
		cont = new JPanel();
		cont.setLayout(null);
		unLabel = new JLabel("Username:");
		pwLabel = new JLabel("Password:");
		unField = new JTextField(120);
		pwField = new JPasswordField(120);
		loginBtn = new JButton("Login");
		resultLabel = new JLabel(" ");
		setLayout();
		
		loginBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				checkLogin(unField.getText(),pwField.getPassword());
			}
		});
		frame.getRootPane().setDefaultButton(loginBtn);
		cont.add(unLabel);
		cont.add(unField);
		cont.add(pwLabel);
		cont.add(pwField);
		cont.add(loginBtn);
		cont.add(resultLabel);
		
		frame.add(cont);
		frame.setVisible(true);
	}
	
	private void checkLogin(String un,char[] pwC){
		User user = new User();
		try {
			URL url = new URL("http://koistine.com/OrderKoistine/v1/login.php?");
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			Map<String, String> arg = new HashMap<>();
			String pw = "";
			for(char p : pwC) {
				pw = pw + p;
			}
			arg.put("userid","80085y4y");
			arg.put("username",un);
			arg.put("pw",pw);
			StringJoiner sj = new StringJoiner("&");
			for(Map.Entry<String,String> entry : arg.entrySet()) {
			    sj.add(entry.getKey() + "="  + entry.getValue());
			}
			byte[] out = sj.toString().getBytes();
			int length = out.length;
			http.setFixedLengthStreamingMode(length);
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			http.connect();
			try(OutputStream os = http.getOutputStream()) {
			    os.write(out);
			}
			InputStream is = http.getInputStream();
			JSONTokener token = null;
			JSONObject obj = null;
			try {
				token = new JSONTokener(is);
				obj = new JSONObject(token);
			}catch(Exception ex) {
				
			}
			if(obj == null) {
				resultLabel.setText("Incorrect username/password");
			}
			else if(obj.getString("login").equals("true")) {
				user.username = obj.getString("username");
				user.userId = obj.getInt("userid");
				new Menu(user);
				frame.dispose();
			}
			http.disconnect();
		}catch(IOException e) {
			
		}
	}
	
	
	private void setLayout(){
		unLabel.setFont(font);
		pwLabel.setFont(font);
		unField.setFont(font);
		pwField.setFont(font);
		loginBtn.setFont(font);
		resultLabel.setFont(font);
		
		unLabel.setBounds(5,10,140,30);
		unField.setBounds(120,10,140,30);
		pwLabel.setBounds(5,45,140,30);
		pwField.setBounds(120,45,140,30);
		loginBtn.setBounds(158,80,100,30);
		resultLabel.setBounds(5,115,300,30);
	}
}