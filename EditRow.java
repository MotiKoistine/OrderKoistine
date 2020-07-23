import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditRow {
	DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.US);
	DecimalFormat df;
	JFrame editFrame;
	JPanel editCont;
	JLabel lName,lPriceOut,lPriceIn,lAmount,lProfit,lDiscount,lDiscountP;
	JTextField tPriceOut,tAmount,tDiscount;
	JButton editBtn;
	Font editFont;
	Item item;
	double discount;
	double profit;
	boolean edited;
	EditRow(Item item){
		this.item = item;
		editFrame = new JFrame("Edit row");
		editCont = new JPanel();
		profit = item.priceEdit - item.priceIn;
		edited = false;
		discount = (item.priceOut - item.priceEdit) / item.priceOut * 100;
		int[] numbers = {KeyEvent.VK_NUMPAD0,KeyEvent.VK_NUMPAD1,KeyEvent.VK_NUMPAD2,KeyEvent.VK_NUMPAD3,KeyEvent.VK_NUMPAD4,
				KeyEvent.VK_NUMPAD5,KeyEvent.VK_NUMPAD6,KeyEvent.VK_NUMPAD7,KeyEvent.VK_NUMPAD8,KeyEvent.VK_NUMPAD9,KeyEvent.VK_BACK_SPACE};
		df = new DecimalFormat("0.00",formatSymbols);
		editFont = new Font("Arial",Font.PLAIN,20);
		editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		editFrame.setResizable(false);
		editFrame.setSize(400,170);
		setLayout();
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
							discount = item.priceOut;
							try{
								discount = (item.priceOut - Double.parseDouble(tPriceOut.getText())) / item.priceOut * 100;
							}catch(Exception e1) {}
							tDiscount.setText(""+df.format(discount));
							int amount = Integer.parseInt(tAmount.getText());
							double profitChange = 0;
							try{
								profitChange = (Double.parseDouble(tPriceOut.getText()) - item.priceIn) * amount;
							}catch(Exception e1) {}
							lProfit.setText("Profit:" + df.format(profitChange) + "€");
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
		tDiscount.addKeyListener(new KeyListener() {
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
						if(!tDiscount.getText().isEmpty()) {
							try{
								discount = Double.parseDouble(tDiscount.getText());
								double price = item.priceOut * ((100 - discount) / 100);
								tPriceOut.setText(""+df.format(price));
							}catch(Exception e1) {
								
							}
							int amount = Integer.parseInt(tAmount.getText());
							double profitChange = (Double.parseDouble(tPriceOut.getText()) - item.priceIn) * amount;
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
		tAmount.addKeyListener(new KeyListener() {
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
						if(!tAmount.getText().isEmpty()) {
							int amount = Integer.parseInt(tAmount.getText());
							double profitChange = (Double.parseDouble(tPriceOut.getText()) - item.priceIn) * amount;
							lProfit.setText("Profit:" + profitChange + "€");
						}
					}
				}
			}
		});
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				item.priceEdit = Double.parseDouble(tPriceOut.getText());
				item.amount = Integer.parseInt(tAmount.getText());
				edited = true;
				editFrame.dispose();
			}
		});
		editFrame.setVisible(true);
	}
	public Item getRow() {
		if(edited == true) {
			return item;
		}
		return null;
	}
	private void setLayout() {
		lName = new JLabel(item.itemName);
		lPriceOut = new JLabel("€");
		lPriceIn = new JLabel(item.priceIn + "€");
		lAmount = new JLabel("pcs.");
		lProfit = new JLabel("Profit:" + profit + "€");
		tDiscount = new JTextField("" + df.format(discount));
		lDiscount = new JLabel("Discount:");
		lDiscountP = new JLabel("%");
		tPriceOut = new JTextField("" + item.priceEdit);
		tAmount = new JTextField("" + item.amount);
		editBtn = new JButton("OK");
		editCont.setLayout(null);
		
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
	}
}
