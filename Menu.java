import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Menu {
	User user;
	JFrame frame;
	JPanel cont;
	JLabel menuLabel;
	JButton newOrders,manageOrders,exit;
	Font font;
	
	Menu(User user){
		this.user = user;
		frame = new JFrame("Menu");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,500);
		font = new Font("Arial",Font.PLAIN,30);
		cont = new JPanel();
		cont.setLayout(null);
		menuLabel = new JLabel("Menu");
		newOrders = new JButton("New orders");
		manageOrders = new JButton("Manage orders");
		exit = new JButton("Exit");
		setLayout();
		
		newOrders.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MainProgram(user);
				frame.dispose();
			}
		});
		manageOrders.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		frame.add(cont);
		frame.setVisible(true);
	}
	
	private void setLayout() {
		menuLabel.setFont(font);
		newOrders.setFont(font);
		manageOrders.setFont(font);
		exit.setFont(font);
		
		menuLabel.setBounds(150,5,100,30);
		newOrders.setBounds(50,50,300,40);
		manageOrders.setBounds(50,105,300,40);
		exit.setBounds(50,400,300,40);
		
		
		
		
		cont.add(menuLabel);
		cont.add(newOrders);
		cont.add(manageOrders);
		cont.add(exit);
	}
}
