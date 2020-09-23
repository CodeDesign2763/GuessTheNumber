//This project was created with the aim of learning the Java language.

//"Guess the number" game
//Игра "Угадай число"
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.Color;


class MyForm extends JFrame implements ActionListener 
{
	final private int max_number_of_attempts=5;
	private int randomNumber;
	private JPanel centralpanel;
	private JLabel rules = new JLabel("<html><font color=red>Guess the Number<font color=green> <br> Game rules:<br> You need to guess a natural number from 1 to 9 in " +String.valueOf(max_number_of_attempts)+" attemts </html>",SwingConstants.CENTER);
	//Special class to display the number of remaining attempts
	class Attempts_counter extends JLabel
	{
		private int current_number_of_attempts;
		private void show_CNA()
		{
			this.setText("Attempts left: "+ String.valueOf(max_number_of_attempts-current_number_of_attempts));
		}
		public void inc() {
			current_number_of_attempts++;
			this.show_CNA();
		}
		public void dec() {
			current_number_of_attempts--;
			this.show_CNA();
		}
		public void set(int new_CNA) {
			current_number_of_attempts=new_CNA;
			this.show_CNA();
		}
		public int get() {
			return current_number_of_attempts;
		}
		public Attempts_counter(String initial_value,int aligment)
		{
			super(initial_value,aligment);
		}
	}
	private Attempts_counter cna=new Attempts_counter("0",SwingConstants.CENTER);
	class TextFieldWithBackgroundText extends JTextField implements FocusListener
	{
		private String hint;
		private boolean first_click;
		public void reset()
		{
			setForeground(Color.LIGHT_GRAY);
			setText(hint);
			this.first_click=true;
		}
		public TextFieldWithBackgroundText(int h,String hint) {
			super(h);
			this.hint = hint;
			this.reset();
			addFocusListener(this);
		}

		@Override
		public void focusGained(FocusEvent e) {
			if(this.first_click==true) 
			{
				setForeground(Color.BLACK);
				this.setText("");
				this.first_click=false;
			}
		}
		@Override
		public void focusLost(FocusEvent e) {
		}
	}
		
	private TextFieldWithBackgroundText answerfield=new TextFieldWithBackgroundText(20,"Enter your answer");
	private JButton checkanswer = new JButton("Check answer!");
	private ImageIcon win_icon;
	
	private void set_new_game()
	{
		randomNumber=new Random().nextInt(9)+1;
		answerfield.setEnabled(true);
		checkanswer.setEnabled(true);
		checkanswer.setText("Check answer!");
		cna.set(0);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Check_answer_signal")) 
			{
			cna.inc();
			try {
				if (Integer.valueOf(answerfield.getText())==randomNumber) 
				{
					//Number is correct. Player won.
					JOptionPane.showMessageDialog(this,"Congratulations, you won the game!","Victory",JOptionPane.INFORMATION_MESSAGE,win_icon);
					checkanswer.setEnabled(false);
					checkanswer.setText("You won!");
					answerfield.setEnabled(false);
					//Creating an event for starting a new game manually
					//This interface can be used for another button.
					this.actionPerformed(new ActionEvent(this,1,"New_game_signal"));
				}
				else
				{
					if (cna.get()<5)
					{
						//The player gave an incorrect answer, but attempts have not yet been used up
						JOptionPane.showMessageDialog(this,"You've made a mistake! Try again!","Wrong answer",JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						//The player gave an incorrect answer, and all attempts have been exhausted.
						JOptionPane.showMessageDialog(this,"<html>Unfortunately you just lost the game.<br>The correct answer was "+String.valueOf(randomNumber)+".</html>","Defeat",JOptionPane.INFORMATION_MESSAGE);
						checkanswer.setEnabled(false);
						checkanswer.setText("You lost the game");
						answerfield.setEnabled(false);
						this.actionPerformed(new ActionEvent(this,1,"New_game_signal"));
					}
				}
			}
			catch (NumberFormatException x)
			{
				//Error while entering a number
				cna.dec();
				JOptionPane.showMessageDialog(this,"Error! Please enter only natural numbers!","Error",JOptionPane.ERROR_MESSAGE);
			}
			answerfield.reset();
		}
		
		else	
		//Start a new game?
		if (e.getActionCommand().equals("New_game_signal"))
		{
		    if (JOptionPane.showConfirmDialog(this,"Do you want to start a new game?","GAME OVER",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) 
				set_new_game();
		    else
				java.lang.System.exit(0);
		}
	}
	
	public MyForm(String title)
	{
		//Configuring the graphical interface
		setTitle(title);
		centralpanel=new JPanel(new GridLayout(4,1,10,10));
		centralpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		centralpanel.add(rules);
		centralpanel.add(cna);
		centralpanel.add(answerfield);
		centralpanel.add(checkanswer);
		checkanswer.addActionListener(this);
		checkanswer.setActionCommand("Check_answer_signal");
		add(centralpanel);
		//Setting values ​​for a new game
		set_new_game();
		//Picture for the dialog box
		win_icon = new ImageIcon("win.png");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
		checkanswer.requestFocus();
		this.requestFocusInWindow();
	}
}
class GN {
	public static void main(String args[])
	{
		MyForm form1 = new MyForm("Guess the Number by Alexander Ch.");
	}
}
