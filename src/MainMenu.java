import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



public class MainMenu extends JFrame{
	JPanel cards;
	JPanel loadingScreen;
	JProgressBar load;
	JPanel startPanel;
	JPanel choosePlayersPanel;
	JPanel onePlayerPanel;
	JPanel twoPlayerPanel;
	// The Image to store the background image in.
    Image img;
	
	public MainMenu() {
		img = Toolkit.getDefaultToolkit().createImage("background.png");
//		CardLayout cl = new CardLayout();
		setTitle("Connect 4");
		setLocation(150,200);
		setSize(701,701);
		setVisible(true);
		setResizable(false);

		cards = new JPanel(new CardLayout());
		cards.setVisible(true);
		CardLayout cl = (CardLayout) cards.getLayout();
		createLoadingScreen();
		cards.add(loadingScreen, "load");
		cl.show(cards, "load");
		
		add(cards);
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
		createStartPanel();
		incrementSmoothly(load, 20);
		createChoosePlayersPanel();
		incrementSmoothly(load, 20);
		createOnePlayerPanel();
		incrementSmoothly(load, 20);
		createTwoPlayerPanel();
		incrementSmoothly(load,20);
		cards.add(startPanel, "Base");
		cards.add(choosePlayersPanel, "ChoosePlayers");
		cards.add(onePlayerPanel, "OnePlayer");
		cards.add(twoPlayerPanel, "TwoPlayer");
		incrementSmoothly(load, 20);
		add(cards);
		load.setVisible(false);
		cl.show(cards, "Base");
		revalidate();
		repaint();
	}
	
	private void incrementSmoothly(JProgressBar p, int val) {
		int pValue = p.getValue();
		for (int i = 0; i < val; i++) {
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			p.setValue(pValue+i);
		}
	}

	private void createLoadingScreen() {
		loadingScreen = new JPanel();
		loadingScreen.setVisible(true);
		
		GroupLayout gl = new GroupLayout(loadingScreen);
		JLabel welcomeMessage = new JLabel("Loading...");
		load = new JProgressBar();
		load.setMaximumSize(new Dimension(400,25));
		load.setStringPainted(true);
		welcomeMessage.setFont(new Font("Arial", Font.PLAIN, 30));
		
		gl.setAutoCreateContainerGaps(true);
//		gl.setAutoCreateGaps(isEnabled());
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGap(150)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
//				.addComponent(welcomeMessage)
				.addComponent(load)
				)
		);
		
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGap(240)
//				.addComponent(welcomeMessage)
//				.addGap(30)
				.addComponent(load)
				);
		loadingScreen.setLayout(gl);
	}

	private void createChoosePlayersPanel() {
		choosePlayersPanel = new JPanel();
		choosePlayersPanel.setVisible(true);
		
		GroupLayout gl = new GroupLayout(choosePlayersPanel);
		JButton btnOnePlayer = new JButton("One Player"); 
		JButton btnTwoPlayer = new JButton("Two Player"); 
		JButton btnBack = new JButton("Back");
		JLabel welcomeMessage = new JLabel("Choose Players");
		btnOnePlayer.setMinimumSize(new Dimension(300,40));
		btnTwoPlayer.setMinimumSize(new Dimension(300,40));
		btnBack.setMinimumSize(new Dimension(300,40));
		welcomeMessage.setFont(new Font("Comic Sans", Font.PLAIN, 30));
		
		btnBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) cards.getLayout();
				cl.show(cards, "Base");
				revalidate();
				repaint();
			}
			
		});
		btnOnePlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) cards.getLayout();
				cl.show(cards, "OnePlayer");
				revalidate();
				repaint();
			}
			
		});
		btnTwoPlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) cards.getLayout();
				cl.show(cards, "TwoPlayer");
				revalidate();
				repaint();
			}
			
		});

		gl.setAutoCreateContainerGaps(true);
//		gl.setAutoCreateGaps(isEnabled());
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGap(210)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(welcomeMessage)
				.addComponent(btnOnePlayer)
				.addComponent(btnTwoPlayer)
				.addComponent(btnBack)
				)
		);
		
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGap(200)
				.addComponent(welcomeMessage)
				.addGap(30)
				.addComponent(btnOnePlayer)
				.addGap(30)
				.addComponent(btnTwoPlayer)
				.addGap(30)
				.addComponent(btnBack)
				);
		choosePlayersPanel.setLayout(gl);
	}

	private void createStartPanel() {
		startPanel = new JPanel();
		startPanel.setVisible(true);
		
		GroupLayout gl = new GroupLayout(startPanel);
		JButton btnStart = new JButton("Start"); 
		JButton btnLoad = new JButton("Load"); 
		JButton btnQuit = new JButton("Quit");
		JLabel welcomeMessage = new JLabel("Connect 4");
		btnStart.setMinimumSize(new Dimension(300,40));
		btnLoad.setMinimumSize(new Dimension(300,40));
		btnQuit.setMinimumSize(new Dimension(300,40));
		welcomeMessage.setFont(new Font("Comic Sans", Font.PLAIN, 30));
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) cards.getLayout();
				cl.show(cards, "ChoosePlayers");
				revalidate();
				repaint();
			}
			
		});
		btnQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		gl.setAutoCreateContainerGaps(true);
//		gl.setAutoCreateGaps(isEnabled());
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGap(210)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(welcomeMessage)
				.addComponent(btnStart)
				.addComponent(btnLoad)
				.addComponent(btnQuit)
				)
		);
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGap(200)
				.addComponent(welcomeMessage)
				.addGap(30)
				.addComponent(btnStart)
				.addGap(30)
				.addComponent(btnLoad)
				.addGap(80)
				.addComponent(btnQuit)
				);
		startPanel.setLayout(gl);
		
	}
	
	private void createOnePlayerPanel() {
		onePlayerPanel = new JPanel();
		onePlayerPanel.setVisible(true);
		
		GroupLayout gl = new GroupLayout(onePlayerPanel);
		JLabel welcomeMessage = new JLabel("Creating Single Player Game...");
		JLabel labelName = new JLabel("Enter your name:");
		JTextField textName = new JTextField();
		JLabel labelAi = new JLabel("Choose Ai Difficulty:");
		JRadioButton radioEasy = new JRadioButton("Easy");
		JRadioButton radioMedium = new JRadioButton("Medium");
		JRadioButton radioHard = new JRadioButton("Hard");
		JButton btnStartGame = new JButton("Start Game");
		JButton btnBack = new JButton("Cancel Game");
	
//		JButton btnMainMenu = new JButton("Main Menu");
		
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(radioEasy);
		radioGroup.add(radioMedium);
		radioGroup.add(radioHard);
		radioEasy.setSelected(true);
		
		textName.setMinimumSize(new Dimension(300,25));
		textName.setMaximumSize(new Dimension(300,25));
		btnStartGame.setMinimumSize(new Dimension(300,40));
		btnBack.setMinimumSize(new Dimension(200,40));
		welcomeMessage.setFont(new Font("Serif", Font.ITALIC, 30));
		
		btnBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) cards.getLayout();
				cl.show(cards, "ChoosePlayers");
				revalidate();
				repaint();
			}
			
		});

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(isEnabled());
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGap(115)
				
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(welcomeMessage)
				.addGroup(gl.createSequentialGroup()
					.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(labelName)
						.addComponent(labelAi)
					 )
					 .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(textName)
						.addComponent(radioEasy)
						.addComponent(radioMedium)
						.addComponent(radioHard)
					)
				)
				.addComponent(btnStartGame)
				.addComponent(btnBack)
//				.addComponent(btnMainMenu)
				)
				
		);
		
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGap(200)
				.addComponent(welcomeMessage)
//				.addGap(30)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(labelName)
				.addComponent(textName)
				)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(labelAi)
					.addComponent(radioEasy)
					
				)
				.addComponent(radioMedium)
				.addComponent(radioHard)
				.addComponent(btnStartGame)
				.addGap(25)
				.addComponent(btnBack)
//				.addComponent(btnMainMenu)
				);
		onePlayerPanel.setLayout(gl);
	}
	
	private void createTwoPlayerPanel() {
		twoPlayerPanel = new JPanel();
		twoPlayerPanel.setVisible(true);
		
		GroupLayout gl = new GroupLayout(twoPlayerPanel);
		JLabel welcomeMessage = new JLabel("Creating Two Player Game...");
		JLabel labelName1 = new JLabel("Enter Player 1 name:");
		JTextField textName1 = new JTextField();
		JLabel labelName2 = new JLabel("Enter Player 2 name:");
		JTextField textName2 = new JTextField();
		//possibly add choice on color?
		JButton btnStartGame = new JButton("Start Game");
		JButton btnBack = new JButton("Cancel Game");
	
		
		textName1.setMinimumSize(new Dimension(300,25));
		textName1.setMaximumSize(new Dimension(300,25));
		textName2.setMinimumSize(new Dimension(300,25));
		textName2.setMaximumSize(new Dimension(300,25));
		btnStartGame.setMinimumSize(new Dimension(300,40));
		btnBack.setMinimumSize(new Dimension(200,40));
		welcomeMessage.setFont(new Font("Serif", Font.ITALIC, 30));
		
		btnBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) cards.getLayout();
				cl.show(cards, "ChoosePlayers");
				revalidate();
				repaint();
			}
			
		});

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(isEnabled());
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGap(115)
				
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(welcomeMessage)
				.addGroup(gl.createSequentialGroup()
					.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(labelName1)
						.addComponent(labelName2)
					 )
					 .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(textName1)
						.addComponent(textName2)
					)
				)
				.addComponent(btnStartGame)
				.addComponent(btnBack)
//				.addComponent(btnMainMenu)
				)
				
		);
		
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGap(200)
				.addComponent(welcomeMessage)
//				.addGap(30)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(labelName1)
				.addComponent(textName1)
				)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(labelName2)
					.addComponent(textName2)
					
				)
				.addComponent(btnStartGame)
				.addGap(75)
				.addComponent(btnBack)
//				.addComponent(btnMainMenu)
				);
		twoPlayerPanel.setLayout(gl);
	}
	
	public static void main(String[] args) {
		MainMenu a = new MainMenu();
	}
	
	
//	private ImageIcon getImage(String filename)
//    {
	
	//
//            ImageIcon image = new ImageIcon("src/menu.jpg");
//            return image;
//    }
}
