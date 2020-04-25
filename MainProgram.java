import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainProgram{
	JFrame frame;
	JPanel cont;
	Font font;
	JTextArea itemArea,infoArea;
	JTextField codeField;
	JButton customerBtn,itemBtn,deliveryBtn,exitBtn;
	
	MainProgram(User user){
		frame = new JFrame("OrderKoistine");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width,screenSize.height);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		font = new Font("Arial",Font.PLAIN,20);
		cont = new JPanel();
		cont.setLayout(null);
		
		itemArea = new JTextArea();
		codeField = new JTextField(600);
		customerBtn = new JButton("Select customer");
		itemBtn = new JButton("Search item");
		deliveryBtn = new JButton("Configure delivery");
		exitBtn = new JButton("Exit");
		
		setLayout();
		
		frame.setVisible(true);
	}
	private void setLayout(){
		codeField.setFont(font);
		customerBtn.setFont(font);
		itemArea.setFont(font);
		itemBtn.setFont(font);
		deliveryBtn.setFont(font);
		exitBtn.setFont(font);
	}
}