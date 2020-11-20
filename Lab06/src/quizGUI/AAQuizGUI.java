package quizGUI;

/* Amino acid quiz GUI by Brendan Kearney
 * Click "Start game" button to start the quiz
 * User is given an amino acid full name, they must submit a one-letter code for a correct answer
 * User can submit answer using the submit button or the enter key
 * Either Upper or lowercase works
 * The game ends after 30 seconds
 */

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Random;

@SuppressWarnings("serial")
public class AAQuizGUI extends JFrame
{
	public static String[] SHORT_NAMES = {"A","R","N","D","C","Q","E","G","H","I","L","K","M","F","P","S","T","W","Y","V"};
	public static String[] FULL_NAMES = {"Alanine","Arginine","Asparagine","Aspartic acid","Cysteine",
		"Glutamine","Glutamic acid","Glycine","Histidine","Isoleucine",
		"Leucine","Lysine","Methionine","Phenylalanine","Proline",
		"Serine","Threonine","Tryptophan","Tyrosine","Valine"};
	
	private JButton startQuizButton = new JButton("Start quiz");
	private JButton cancelButton = new JButton("Cancel");
	private JButton submitButton = new JButton("Submit");
	private JTextField entryField = new JTextField();
	private JTextArea infoArea = new JTextArea();
	private JTextArea questionArea = new JTextArea("Find the 1-letter code of this amino acid: ");
	private JTextArea previousAnswerArea = new JTextArea();
	private JTextField answerBar = new JTextField("Press enter key to submit answer");
	private int corrects = 0;
	private int incorrects = 0;
	private long start_time = 0;
	private long current_time = 30;
	private String previousAnswer = "";
	private String correctAnswer = "";
	private static JLabel timerLabel=new JLabel(new Date().toString());

	private JPanel getTopPanel()
	{
		// Add start and cancel buttons to gui
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		
		// Start the quiz. Also functions as a game reset.
		startQuizButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				{
					resetGame();
					infoArea.setText("");
					updateRightBar();
					
					playGame();

				}
			}
		});
		
		// Reset game stats/timer
		cancelButton.setMnemonic(KeyEvent.VK_ESCAPE);
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				resetGame();
			}
		});
		
		panel.add(startQuizButton);
		panel.add(cancelButton);
		return panel;
	}
	private JPanel getBotPanel()
	{
		// Add answer submission area and a submit answer button to bottom of gui
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		
		panel.add(answerBar);
		panel.add(submitButton);
		
		return panel;
	}
	private void playGame()
	{
		newQuestion();
		startTimer();
	}
	private void startTimer() 
	{
		// Update timer every second (counting down from 30) 
		// If timer hits zero stop updating the text. (Didn't figure out how to actually stop timer)
		start_time = System.currentTimeMillis();
		Timer timer = new Timer(1000, new ActionListener() {
			
	        @Override
	        public void actionPerformed(ActionEvent arg0) {
	        	
	        	current_time = 30-(System.currentTimeMillis()-start_time)/1000;
	            timerLabel.setText("Time left: "+String.valueOf(current_time));
	    	    if (current_time <= 0) {
	            	timerLabel.setText("TIME'S UP! Press cancel or start quiz to play again. Or just keep playing with no timer");
	            }
	        }
	    });
	    timer.start(); 
	    
	    
	    
	    
	}
	private void newQuestion()
	{
		questionArea.setText("");
		questionArea.append("\n");
		questionArea.append("\n");
		questionArea.append("\n");
		questionArea.append("\n");
		questionArea.append("\n");
		questionArea.append("\n");
		Random random1 = new Random();
		int randInt = random1.nextInt(FULL_NAMES.length);
		correctAnswer = SHORT_NAMES[randInt];
		questionArea.append(FULL_NAMES[randInt]);
	}
	
	private void resetGame()
	{
		entryField.setText("");
		corrects = 0;
		incorrects = 0;
		current_time = 0;
		previousAnswer = "";
		correctAnswer = "";
		questionArea.setText("GAME RESET!");
		start_time = System.currentTimeMillis();
	}

	private void updateRightBar()
	{
		previousAnswerArea.setText("");
		previousAnswerArea.append("Your answer: "+previousAnswer+"\n");
		previousAnswerArea.append("Correct answer: "+correctAnswer+"\n");
		previousAnswerArea.append("CORRECT: "+corrects+"\n");
		previousAnswerArea.append("WRONG: "+incorrects+"\n");
	}
	
	public AAQuizGUI() 
	{
		super("Amino Quiz GUI");
		
		setLocationRelativeTo(null);
		setSize(800,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getTopPanel(), BorderLayout.NORTH);
		getContentPane().add(infoArea, BorderLayout.LINE_START);
		getContentPane().add(questionArea, BorderLayout.CENTER);
		getContentPane().add(previousAnswerArea, BorderLayout.LINE_END);
		getContentPane().add(getBotPanel(), BorderLayout.SOUTH);
	    getContentPane().add(timerLabel, BorderLayout.LINE_START);

		infoArea.setBorder(BorderFactory.createLineBorder(Color.black));
		questionArea.setBorder(BorderFactory.createLineBorder(Color.black));
		previousAnswerArea.setBorder(BorderFactory.createLineBorder(Color.black));

		// Clear answer bar if user clicks on it (after each question)
		answerBar.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	            answerBar.setText("");
	        }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
	    });
		
		// Submit button functionality, grades the answer and gives a new question
		Action action = new AbstractAction()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {

		        previousAnswer = answerBar.getText();
		        
		        if (previousAnswer.equals(correctAnswer) || previousAnswer.equals(correctAnswer.toLowerCase())) {
					corrects++;
				} else {
					incorrects++;
				}
		        
		        updateRightBar();
		        answerBar.setText("");
		        newQuestion();
		    }
		};
		
		// allows enter to use enter key, same as submit button 
		submitButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	        // Progress to next question if clicked
	        previousAnswer = answerBar.getText();
	        
	        if (previousAnswer.equals(correctAnswer) || previousAnswer.equals(correctAnswer.toLowerCase())) {
				corrects++;
			} else {
				incorrects++;
			}
	        
	        updateRightBar();
	        newQuestion();
	      }
	    });
		
		answerBar.addActionListener( action );

		setVisible(true);
	} 
	

	public static void main(String[] args) 
	{
		new AAQuizGUI();
	}

}


