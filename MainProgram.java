import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MainProgram{
	JFrame frame,errorFrame;
	JPanel cont;
	Font font;
	JTextArea itemArea,priceArea,amountArea,totalPriceArea,customerArea;
	JTextField codeField;
	JButton customerBtn,itemBtn,deliveryBtn,exitBtn,errorBtn;
	JLabel infoArea,timeArea,totalPriceLabel,errorLabel;
	Item item;
	EditRow editRow;
	List<Item> items;
	Customer customer;
	SearchCustomer searchCustomer;
	SearchItem searchItem;
	DecimalFormatSymbols formatSymbols;
	int pos;
	
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
		formatSymbols = new DecimalFormatSymbols(Locale.US);
		
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
		totalPriceLabel = new JLabel("Total: 0.00€");
		
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
		DecimalFormat df = new DecimalFormat("#.00",formatSymbols);
		double finalPrice = 0;
		Iterator<Item> i = items.iterator();
		codeField.setText("");
		itemArea.setText("");
		priceArea.setText("");
		amountArea.setText("");
		totalPriceArea.setText("");
		int hPos = 45;
		pos = 0;
		while(i.hasNext()) {
			JButton editBtn = new JButton("Edit");
			Item row = i.next();
			row.pos = pos;
			editBtn.setBounds(5,hPos,100,32);
			hPos = hPos + 35;
			cont.add(editBtn);
			itemArea.append(row.itemName + "\n");
			priceArea.append(df.format(row.priceEdit) + "€\n");
			amountArea.append(row.amount + "pcs\n");
			double totalPrice = row.amount * row.priceEdit;
			finalPrice = finalPrice + totalPrice;
			totalPriceArea.append(df.format(totalPrice) + "€\n");
			editBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editRow = new EditRow(row);
					getRow(row.pos);
				}
			});
			frame.repaint();
			pos++;
		}
		if(finalPrice != 0) {
			totalPriceLabel.setText("Total: " + df.format(finalPrice) + "€");
		}
	}
	private void getRow(int rowPos) {
		Thread t = new Thread() {
			public void run() {
				while(editRow.getRow() == null) {
					try {
						Thread.sleep(1000);
						Item row = editRow.getRow();
						if(row != null) {
							items.set(rowPos, row);
							updateItems();
						}
					} catch (InterruptedException e) {
						
					}
				}
			}
		};
		t.start();
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
		int windowW = frame.getWidth();
		int windowH = frame.getHeight();
		windowW = 1920 - windowW;
		windowH = 1080 - windowH;
		customerArea.setBounds(5,5,1905-windowW,35);
		itemArea.setBounds(105,45,1305-windowW,760-windowH);
		priceArea.setBounds(1410-windowW,45,200,760-windowH);
		amountArea.setBounds(1610-windowW,45,100,760-windowH);
		totalPriceArea.setBounds(1710-windowW,45,200,760-windowH);
		codeField.setBounds(5,810-windowH,1600-windowW,40);
		customerBtn.setBounds(5,850-windowH,250,140);
		itemBtn.setBounds(260,850-windowH,250,140);
		deliveryBtn.setBounds(1410-windowW,850-windowH,250,140);
		exitBtn.setBounds(1665-windowW,850-windowH,250,140);
		totalPriceLabel.setBounds(1610-windowW,810-windowH,300,40);
		infoArea.setBounds(515,860-windowH,885-windowW,70);
		infoArea.setHorizontalAlignment(SwingConstants.CENTER);
		timeArea.setBounds(515,900-windowH,885-windowW,70);
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