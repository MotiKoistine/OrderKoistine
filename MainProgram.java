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
	JFrame frame,errorFrame;
	JPanel cont;
	Font font;
	JTextArea itemArea,priceArea,amountArea,totalPriceArea,customerArea;
	JTextField codeField;
	JButton customerBtn,itemBtn,deliveryBtn,exitBtn,errorBtn;
	JLabel infoArea,timeArea,totalPriceLabel,errorLabel;
	Item item;
	List<Item> items;
	Customer customer;
	SearchCustomer searchCustomer;
	SearchItem searchItem;
	
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
		customerArea = new JTextArea("Customer: Not selected");
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
				searchCustomer = new SearchCustomer();
				getCustomer();
			}
		});
		itemBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchItem = new SearchItem();
				getItem();
			}
		});
		deliveryBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getCustomer();
				if(customer == null) {
					displayError("Customer not selected");
				}
				else {
					
				}
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
	public void getCustomer() {
		Thread t = new Thread() {
			public void run() {
				boolean isCustomer = false;
				while(isCustomer == false) {
					try {
						customer = searchCustomer.getCustomer();
						if(customer != null) {
							isCustomer = true;
							customerArea.setText("Customer: " + customer.name);
						}
						Thread.sleep(1000);
					}catch(Exception e1) {
					}
				}
			}
		};
		t.start();
	}
	public void getItem() {
		Thread t = new Thread() {
			public void run() {
				boolean isItem = false;
				while(isItem == false) {
					try {
						item = searchItem.getItem();
						if(item != null) {
							isItem = true;
							items.add(item);
							updateItems();
						}
						Thread.sleep(1000);
					}catch(Exception e1) {
					}
				}
				item = null;
			}
		};
		t.start();
	}
	private void addItem(){
		QuickSearch search = new QuickSearch();
		Item item = search.performSearch(codeField.getText());
		if(item != null) {
			items.add(item);
		}
		updateItems();
	}
	private void updateItems() {
		DecimalFormat df = new DecimalFormat("#.00");
		double finalPrice = 0;
		Iterator<Item> i = items.iterator();
		codeField.setText("");
		itemArea.setText("");
		priceArea.setText("");
		amountArea.setText("");
		totalPriceArea.setText("");
		int hPos = 45;
		while(i.hasNext()) {
			JButton editBtn = new JButton("Edit");
			Item row = i.next();
			editBtn.setBounds(5,hPos,100,32);
			hPos = hPos + 35;
			cont.add(editBtn);
			itemArea.append(row.itemName + "\n");
			priceArea.append(df.format(row.priceOut) + "€\n");
			amountArea.append(row.amount + "pcs\n");
			double totalPrice = row.amount * row.priceOut;
			finalPrice = finalPrice + totalPrice;
			totalPriceArea.append(df.format(totalPrice) + "€\n");
			editBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editRow(row);
				}
			});
			frame.repaint();
		}
		if(finalPrice != 0) {
			totalPriceLabel.setText("Total: " + df.format(finalPrice) + "€");
		}
	}
	private void editRow(Item item) {
		JFrame editFrame = new JFrame("Edit row");
		JPanel editCont = new JPanel();
		JLabel lName,lPriceOut,lPriceIn,lAmount,lProfit,lDiscount,lDiscountP;
		JTextField tPriceOut,tAmount,tDiscount;
		JButton editBtn;
		double profit = item.priceOut - item.priceIn;
		int[] numbers = {KeyEvent.VK_NUMPAD0,KeyEvent.VK_NUMPAD1,KeyEvent.VK_NUMPAD2,KeyEvent.VK_NUMPAD3,KeyEvent.VK_NUMPAD4,
				KeyEvent.VK_NUMPAD5,KeyEvent.VK_NUMPAD6,KeyEvent.VK_NUMPAD7,KeyEvent.VK_NUMPAD8,KeyEvent.VK_NUMPAD9,KeyEvent.VK_BACK_SPACE};
		Font editFont = new Font("Arial",Font.PLAIN,20);
		editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		editFrame.setResizable(false);
		editFrame.setSize(400,170);
		lName = new JLabel(item.itemName);
		lPriceOut = new JLabel("€");
		lPriceIn = new JLabel(item.priceIn + "€");
		lAmount = new JLabel("pcs.");
		lProfit = new JLabel("Profit:" + profit + "€");
		tDiscount = new JTextField("0,00");
		lDiscount = new JLabel("Discount:");
		lDiscountP = new JLabel("%");
		tPriceOut = new JTextField("" + item.priceOut);
		tAmount = new JTextField("" + item.amount);
		editBtn = new JButton("OK");
		editCont.setLayout(null);
		tPriceOut.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
				for(int i : numbers) {
					if(e.getKeyCode() == i) {
						if(!tPriceOut.getText().isEmpty()) {
							DecimalFormat df = new DecimalFormat("0.00");
							double discount = item.priceOut;
							try{
								discount = (item.priceOut - Double.parseDouble(tPriceOut.getText())) / item.priceOut * 100;
							}catch(Exception e1) {}
							tDiscount.setText(""+df.format(discount));
							double profitChange = Double.parseDouble(tPriceOut.getText()) - item.priceIn;
							lProfit.setText("Profit:" + profitChange + "€");
						}
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_COMMA || e.getKeyCode() == KeyEvent.VK_DECIMAL) {
					String field = tPriceOut.getText();
					field = field.replace(',', '.');
					tPriceOut.setText(field);
				}
			}
		});
		
		lName.setBounds(5,5,400,30);
		tPriceOut.setBounds(5,40,80,30);
		lPriceOut.setBounds(85,40,20,30);
		lDiscount.setBounds(110,40,100,30);
		tDiscount.setBounds(195,40,60,30);
		lDiscountP.setBounds(255,40,20,30);
		tAmount.setBounds(305,40,40,30);
		lAmount.setBounds(350,40,50,30);
		lPriceIn.setBounds(5,80,100,30);
		lProfit.setBounds(110,80,200,30);
		editBtn.setBounds(300,80,70,30);
		
		lName.setFont(editFont);
		lPriceOut.setFont(editFont);
		lPriceIn.setFont(editFont);
		lAmount.setFont(editFont);
		lProfit.setFont(editFont);
		lDiscount.setFont(editFont);
		lDiscountP.setFont(editFont);
		tPriceOut.setFont(editFont);
		tAmount.setFont(editFont);
		tDiscount.setFont(editFont);
		editBtn.setFont(editFont);
		
		editCont.add(lName);
		editCont.add(lPriceOut);
		editCont.add(lPriceIn);
		editCont.add(lAmount);
		editCont.add(lProfit);
		editCont.add(lDiscount);
		editCont.add(lDiscountP);
		editCont.add(tPriceOut);
		editCont.add(tAmount);
		editCont.add(tDiscount);
		editCont.add(editBtn);
		editFrame.add(editCont);
		editFrame.setVisible(true);
	}
	private void displayError(String err) {
		errorFrame = new JFrame("Error");
		errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		errorFrame.setResizable(false);
		errorFrame.setSize(400,150);
		errorFrame.setLayout(null);
		errorLabel = new JLabel(err);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorBtn = new JButton("Ok");
		errorLabel.setBounds(5,5,390,40);
		errorBtn.setBounds(150,60,100,40);
		errorLabel.setFont(font);
		errorBtn.setFont(font);
		errorFrame.add(errorLabel);
		errorFrame.add(errorBtn);
		errorFrame.setVisible(true);
		errorFrame.getRootPane().setDefaultButton(errorBtn);
		errorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				errorFrame.dispose();
			}
		});
	}
	private void setLayout(){
		//Set fonts
		codeField.setFont(font);
		customerBtn.setFont(font);
		itemArea.setFont(font);
		priceArea.setFont(font);
		amountArea.setFont(font);
		totalPriceArea.setFont(font);
		customerArea.setFont(new Font("Arial",Font.ITALIC,30));
		itemBtn.setFont(font);
		deliveryBtn.setFont(font);
		exitBtn.setFont(font);
		infoArea.setFont(font);
		timeArea.setFont(font);
		totalPriceLabel.setFont(font);
		
		//Set sizes
		customerArea.setBounds(5,5,1905,35);
		itemArea.setBounds(105,45,1305,760);
		priceArea.setBounds(1410,45,200,760);
		amountArea.setBounds(1610,45,100,760);
		totalPriceArea.setBounds(1710,45,300,760);
		codeField.setBounds(5,810,1600,40);
		customerBtn.setBounds(5,850,250,140);
		itemBtn.setBounds(260,850,250,140);
		deliveryBtn.setBounds(1410,850,250,140);
		exitBtn.setBounds(1665,850,250,140);
		totalPriceLabel.setBounds(1610,810,300,40);
		infoArea.setBounds(515,860,885,70);
		infoArea.setHorizontalAlignment(SwingConstants.CENTER);
		timeArea.setBounds(515,900,885,70);
		timeArea.setHorizontalAlignment(SwingConstants.CENTER);
		itemArea.setEditable(false);
		customerArea.setEditable(false);
		
		
		//Add elements
		cont.add(customerArea);
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