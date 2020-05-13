import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainProgram{
	JFrame frame;
	JPanel cont;
	Font font;
	JTextArea itemArea,priceArea,amountArea,totalPriceArea;
	JTextField codeField;
	JButton customerBtn,itemBtn,deliveryBtn,exitBtn;
	JLabel infoArea,timeArea,totalPriceLabel;
	List<Item> items;
	Customer customer;
	
	MainProgram(User user){
		frame = new JFrame("OrderKoistine");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width,screenSize.height);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		font = new Font("Arial",Font.PLAIN,30);
		cont = new JPanel();
		cont.setLayout(null);
		items = new ArrayList<Item>();
		
		itemArea = new JTextArea();
		priceArea = new JTextArea();
		amountArea = new JTextArea();
		totalPriceArea = new JTextArea();
		codeField = new JTextField();
		customerBtn = new JButton("Select customer");
		itemBtn = new JButton("Search item");
		deliveryBtn = new JButton("Select delivery");
		exitBtn = new JButton("Exit");
		infoArea = new JLabel(user.username + " (" + user.userId + ")");
		timeArea = new JLabel("");
		totalPriceLabel = new JLabel("Total: 0,00€");
		
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
		customerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SearchCustomer();
			}
		});
		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispatchEvent(new WindowEvent(frame,WindowEvent.WINDOW_CLOSING));
			}
		});
		frame.add(cont);
		frame.setVisible(true);
		
		codeField.requestFocus();
	}
	private void addItem(){
		SearchItem search = new SearchItem();
		Item item = search.performSearch(codeField.getText());
		DecimalFormat df = new DecimalFormat("#.00");
		double finalPrice = 0;
		if(item != null) {
			items.add(item);
		}
		Iterator<Item> i = items.iterator();
		codeField.setText("");
		itemArea.setText("");
		priceArea.setText("");
		amountArea.setText("");
		totalPriceArea.setText("");
		while(i.hasNext()) {
			Item row = i.next();
			itemArea.append(row.itemName + "\n");
			priceArea.append(df.format(row.priceOut) + "€\n");
			amountArea.append(row.amount + "pcs\n");
			double totalPrice = row.amount * row.priceOut;
			finalPrice = finalPrice + totalPrice;
			totalPriceArea.append(df.format(totalPrice) + "€\n");
		}
		if(finalPrice != 0) {
			totalPriceLabel.setText("Total: " + df.format(finalPrice) + "€");
		}
	}
	private void setLayout(){
		//Set fonts
		codeField.setFont(font);
		customerBtn.setFont(font);
		itemArea.setFont(font);
		priceArea.setFont(font);
		amountArea.setFont(font);
		totalPriceArea.setFont(font);
		itemBtn.setFont(font);
		deliveryBtn.setFont(font);
		exitBtn.setFont(font);
		infoArea.setFont(font);
		timeArea.setFont(font);
		totalPriceLabel.setFont(font);
		
		
		//Set sizes
		itemArea.setBounds(5,5,1405,800);
		priceArea.setBounds(1410,5,200,800);
		amountArea.setBounds(1610,5,100,800);
		totalPriceArea.setBounds(1710,5,200,800);
		codeField.setBounds(5,810,1600,40);
		customerBtn.setBounds(5,850,250,140);
		itemBtn.setBounds(260,850,250,140);
		deliveryBtn.setBounds(1410,850,250,140);
		exitBtn.setBounds(1665,850,250,140);
		totalPriceLabel.setBounds(1610,810,200,40);
		infoArea.setBounds(515,860,885,70);
		infoArea.setHorizontalAlignment(SwingConstants.CENTER);
		timeArea.setBounds(515,900,885,70);
		timeArea.setHorizontalAlignment(SwingConstants.CENTER);
		itemArea.setEditable(false);
		
		//Add elements
		cont.add(itemArea);
		cont.add(priceArea);
		cont.add(amountArea);
		cont.add(totalPriceArea);
		cont.add(codeField);
		cont.add(customerBtn);
		cont.add(itemBtn);
		cont.add(deliveryBtn);
		cont.add(exitBtn);
		cont.add(infoArea);
		cont.add(timeArea);
		cont.add(totalPriceLabel);
	}
	private void displayInfo(){
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		DateTimeFormatter dfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
		ActionListener updateClockAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalDate date = LocalDate.now();
				LocalTime time = LocalTime.now();
				String timeNow = date.format(df) + " " + time.format(dfTime);
				timeArea.setText(timeNow);
			}
		};
		Timer t = new Timer(100, updateClockAction);
		t.start();
	}
}