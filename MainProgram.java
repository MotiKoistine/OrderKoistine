import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

public class MainProgram{
	JFrame frame;
	JPanel cont;
	Font font;
	JTextArea itemArea;
	JTextField codeField;
	JButton customerBtn,itemBtn,deliveryBtn,exitBtn;
	JLabel infoArea;
	List<Item> items;
	
	MainProgram(User user){
		frame = new JFrame("OrderKoistine");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width,screenSize.height);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		font = new Font("Arial",Font.PLAIN,30);
		cont = new JPanel();
		cont.setLayout(null);
		items = new ArrayList<Item>();
		
		itemArea = new JTextArea();
		codeField = new JTextField();
		customerBtn = new JButton("Select customer");
		itemBtn = new JButton("Search item");
		deliveryBtn = new JButton("Select delivery");
		exitBtn = new JButton("Exit");
		infoArea = new JLabel(user.username + " (" + user.userId + ")");
		
		setLayout();
		displayInfo();
		
		codeField.addKeyListener(new KeyListener(){
			@Override
			public void keyTyped(KeyEvent e){}
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					addItem();
				}
			}
			@Override
			public void keyReleased(KeyEvent e){}
		});
		
		frame.add(cont);
		frame.setVisible(true);
		
		codeField.requestFocus();
	}
	private void addItem(){
		itemArea.append(codeField.getText() + "\n");
		codeField.setText("");
		items.add(new Item(codeField.getText()));
	}
	private void setLayout(){
		//Set fonts
		codeField.setFont(font);
		customerBtn.setFont(font);
		itemArea.setFont(font);
		itemBtn.setFont(font);
		deliveryBtn.setFont(font);
		exitBtn.setFont(font);
		infoArea.setFont(font);
		
		
		//Set sizes
		itemArea.setBounds(5,5,1910,800);
		codeField.setBounds(5,805,1910,40);
		customerBtn.setBounds(5,850,250,140);
		itemBtn.setBounds(260,850,250,140);
		deliveryBtn.setBounds(1410,850,250,140);
		exitBtn.setBounds(1665,850,250,140);
		infoArea.setBounds(515,850,885,140);
		infoArea.setHorizontalAlignment(SwingConstants.CENTER);
		itemArea.setEditable(false);
		
		//Add elements
		cont.add(itemArea);
		cont.add(codeField);
		cont.add(customerBtn);
		cont.add(itemBtn);
		cont.add(deliveryBtn);
		cont.add(exitBtn);
		cont.add(infoArea);
		
	}
	private void displayInfo(){
		String un = infoArea.getText();
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		LocalDate date = LocalDate.now();
		infoArea.setText("<html>" + un + "<br/" + date.format(dateFormat) + "</html>");
	}
}