import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import org.json.JSONObject;
import org.json.JSONTokener;

public class QuickSearch {
	JSONTokener token;
	JSONObject obj;
	URL url;
	URLConnection con;
	HttpURLConnection http;
	QuickSearch(){
		try {
			url = new URL("http://koistine.com/OrderApi/items.php");
			con = url.openConnection();
			http = (HttpURLConnection)con;
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			
		}catch(Exception e) {
			
		}
		
	}
	public Item performSearch(String search){
		Item item = null;
		Map<String, String> arg = new HashMap<>();
		arg.put("userid","80085y4y");
		arg.put("code",search);
		StringJoiner sj = new StringJoiner("&");
		for(Map.Entry<String, String> entry : arg.entrySet()) {
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
			InputStream is = http.getInputStream();
			token = new JSONTokener(is);
			obj = new JSONObject(token);
		}catch(IOException e) {
			
		}
		if(!obj.isNull("itemid")) {
			item = new Item(Integer.parseInt(obj.getString("itemid")),obj.getString("item"),obj.getDouble("priceout"),obj.getDouble("pricein"),obj.getDouble("priceout"),1);
		}
		http.disconnect();
		return item;
	}
}
