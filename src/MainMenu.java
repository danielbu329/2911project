import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;



public class MainMenu extends JFrame{
	JPanel cards;
	JPanel startPanel;
	JButton btnStart;
	JButton btnLoad; 
	JButton btnQuit;
	
	JPanel choosePlayersPanel;
	
	
	public MainMenu() {
		
//		CardLayout cl = new CardLayout();
		setTitle("Connect 4");
		setLocation(150,200);
		setSize(701,701);
		setVisible(true);
		setResizable(false);
//		setLayout(cl);
		createStartPanel();
		createChoosePlayersPanel();
		
		cards = new JPanel(new CardLayout());
		cards.add(startPanel, "Base");
		cards.add(choosePlayersPanel, "ChoosePlayers");
		add(cards);
		cards.setVisible(true);
		CardLayout cl = (CardLayout) cards.getLayout();
		cl.show(cards, "Base");
		this.revalidate();
		repaint();
	}

	private void createChoosePlayersPanel() {
		choosePlayersPanel = new JPanel();
		choosePlayersPanel.setVisible(true);
		
		GroupLayout gl = new GroupLayout(choosePlayersPanel);
		JButton btnOnePlayer = new JButton("One Player"); 
		JButton btnTwoPlayer = new JButton("Two Player"); 
		JButton btnBack = new JButton("Back");
		JLabel welcomeMessage = new JLabel("Choose Players");
		btnStart.setMinimumSize(new Dimension(300,40));
		btnLoad.setMinimumSize(new Dimension(300,40));
		btnQuit.setMinimumSize(new Dimension(300,40));
		welcomeMessage.setFont(new Font("Comic Sans", Font.PLAIN, 30));
//		ImageIcon image = new ImageIcon("src/menu.jpg");
//		welcomeMessage.setIcon(image);
		
		btnBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) cards.getLayout();
				cl.show(cards, "Base");
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
		btnStart = new JButton("Start"); 
		btnLoad = new JButton("Load"); 
		btnQuit = new JButton("Quit");
		JLabel welcomeMessage = new JLabel("Connect 4");
		btnStart.setMinimumSize(new Dimension(300,40));
		btnLoad.setMinimumSize(new Dimension(300,40));
		btnQuit.setMinimumSize(new Dimension(300,40));
		welcomeMessage.setFont(new Font("Comic Sans", Font.PLAIN, 30));
//		ImageIcon image = new ImageIcon("src/menu.jpg");
//		welcomeMessage.setIcon(image);
		
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
				.addGap(30)
				.addComponent(btnQuit)
				);
		startPanel.setLayout(gl);
		
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
