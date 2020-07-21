import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;


public class DisplayTotal {
	List<Item> items;
	DeliveryOption deliveryOption;
	User user;
	Customer customer;
	JFrame frame;
	JPanel cont;
	Font font;
	JTextArea totalAreaItem,totalAreaPrice,totalAreaAmount,totalAreaTotalPrice;
	JButton submit;
	JLabel totalLabel;
	JLabel totalField;
	DecimalFormat dF;
	DecimalFormatSymbols formatSymbols;
	int height;
	
	
	DisplayTotal(List<Item> items, DeliveryOption deliveryOption, User user, Customer customer){
		this.items = items;
		this.deliveryOption = deliveryOption;
		this.user = user;
		this.customer = customer;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		height = (int)screenSize.getHeight();
		frame = new JFrame("Total");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1000,height);
		cont = new JPanel();
		cont.setLayout(null);
		font = new Font("Arial",Font.PLAIN,30);
		totalAreaItem = new JTextArea();
		totalAreaItem.setEditable(false);
		totalAreaPrice = new JTextArea();
		totalAreaPrice.setEditable(false);
		totalAreaAmount = new JTextArea();
		totalAreaAmount.setEditable(false);
		totalAreaTotalPrice = new JTextArea();
		totalAreaTotalPrice.setEditable(false);
		submit = new JButton("Submit order");
		totalLabel = new JLabel("Total: ");
		totalField = new JLabel("0");
		formatSymbols = new DecimalFormatSymbols(Locale.US);
		dF = new DecimalFormat("0.00",formatSymbols);
		setLayout();
		displayTotal();
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new InsertOrder(items,customer.customerid,deliveryOption.id,user.userId);
			}
		});
		
		frame.setVisible(true);
	}
	private void displayTotal() {
		double totalPrice = deliveryOption.price;
		Iterator<Item> it = items.iterator();
		while(it.hasNext()) {
			Item item = (Item)it.next();
			totalPrice+= item.priceEdit * item.amount;
			totalAreaItem.append(item.itemName + "\n");
			totalAreaPrice.append(dF.format(item.priceEdit) + "€\n");
			totalAreaAmount.append(item.amount + "pcs\n");
			totalAreaTotalPrice.append(dF.format(item.priceEdit * item.amount) + "€\n");
		}
		totalAreaItem.append(deliveryOption.name + "\n");
		totalAreaPrice.append(dF.format(deliveryOption.price) + "€\n");
		totalAreaAmount.append("1pcs\n");
		totalAreaTotalPrice.append(dF.format(deliveryOption.price) + "€\n");
		totalField.setText(dF.format(totalPrice) + "€");
	}
	
	private void setLayout() {
		totalAreaItem.setFont(font);
		totalAreaPrice.setFont(font);
		totalAreaAmount.setFont(font);
		totalAreaTotalPrice.setFont(font);
		submit.setFont(font);
		totalLabel.setFont(font);
		totalField.setFont(font);
		
		totalAreaItem.setBounds(5,5,580,height-150);
		totalAreaPrice.setBounds(585,5,150,height-150);
		totalAreaAmount.setBounds(735,5,90,height-150);
		totalAreaTotalPrice.setBounds(825,5,150,height-150);
		submit.setBounds(20,height-120,250,50);
		totalLabel.setBounds(750,height-120,100,50);
		totalField.setBounds(820,height-120,150,50);
		
		totalField.setHorizontalAlignment(SwingConstants.RIGHT);
		
		cont.add(totalAreaItem);
		cont.add(totalAreaPrice);
		cont.add(totalAreaAmount);
		cont.add(totalAreaTotalPrice);
		cont.add(submit);
		cont.add(totalLabel);
		cont.add(totalField);
		
		frame.add(cont);
	}

}
