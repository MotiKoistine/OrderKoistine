import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
				checkLogin(unField.getText(),pwField.getText());
			}
		});
		
		cont.add(unLabel);
		cont.add(unField);
		cont.add(pwLabel);
		cont.add(pwField);
		cont.add(loginBtn);
		cont.add(resultLabel);
		
		frame.add(cont);
		frame.setVisible(true);
	}
	
	private void checkLogin(String un,String pw){
		User user = new User();
		user.username = un;
		user.userId = 1235;
		new MainProgram(user);
		frame.dispose();
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
		resultLabel.setBounds(5,115,160,30);
	}
}