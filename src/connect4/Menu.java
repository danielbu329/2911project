package connect4;

import sun.security.provider.PolicySpiFile;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Menu
{
    //Menu Bar
    //Game - Normal / Speed / Endless / -- / Exit
    //Player 1 - Human / Computer Easy / Computer Medium / Computer Hard
    //Player 2 - Human / Computer Easy / Computer Medium / Computer Hard

    private GUI gui;
    private Player players[];
    private HashMap<String, JMenuItem> items;
    private String[] choices;
    private PieceIcon player1;
    private PieceIcon player2;
    private JMenu timer;
    private TimerIcon timerIcon;
    private JMenu score1;
    private JMenu score2;

    
    public boolean playSounds()
    {
        return sound;
    }

    private boolean sound;

    public Menu(GUI main, Image yellow, Image red)
    {
        String empty[] = new String[0];
        player1 = new PieceIcon(yellow);
        player2 = new PieceIcon(red);
        players = new Player[GUI.NUM_PLAYERS * 2];
        gui = main;
        Font scoreFont = new Font("Times", Font.BOLD, 12);

        items = new HashMap<>(20);
        choices = "Human/Easy/Medium/Hard".split("/");
        JMenuBar menubar = new JMenuBar();      //Create a menu bar
        JMenu menu = addMenu(menubar, "Game", "!<New Game>/Normal/Speed/Endless/--/+Sound/--/Exit".split("/"));
        menu.setMnemonic(KeyEvent.VK_G);
        menu = addMenu(menubar, "Player 1", "Human/--/!Computer/Easy/Medium/Hard".split("/"));
        menu.setIcon(player1);
        menu = addMenu(menubar, "Player 2", "Human/--/!Computer/Easy/Medium/Hard".split("/"));
        menu.setIcon(player2);
        menu = addMenu(menubar, "Help", "Tutorial".split("/"));
        timer = addMenu(menubar, "", empty);
        timerIcon = new TimerIcon();
        timer.setIcon(timerIcon);
        score1 = addMenu(menubar, "000000", empty);
        score1.setIcon(player1);
        score1.setFont(scoreFont);
        score2 = addMenu(menubar, "000000", empty);
        score2.setIcon(player2);
        score2.setFont(scoreFont);
        


        select("Player 1", "Human");
        //select("Player 2", "Medium");
        //select("Player 1", "Hard");
        select("Player 2", "Hard");
        main.setJMenuBar(menubar);                   //Set the menu bar as active
        menuAction("Game/Normal");
        //menuAction("Game/Endless");
        menuAction("Game/Sound");
    }

    void updateTimer(int value)
    {
        timerIcon.setValue(value);
        timer.setIcon(timerIcon);
        timer.repaint();
    }

    /**
     * Called whenever a menu item is selected
     * @param menu - Text
     */
    void menuAction(String menu)         //Called each time menu item is selected
    {
        int slash = menu.indexOf('/');
        String title = menu.substring(0, slash);
        String item = menu.substring(slash+1);
        if (title.startsWith("Player"))
        {
            select(title, item);
        }

        if (title.equals("Game") || title.equals("Help"))
        {
            if (item.equals("Exit"))
            {
                System.exit(0);                 //Exit the program
            }
            if (item.equals("Sound"))
            {
                JMenuItem soundItem = items.get(menu);
                sound = !sound;
                soundItem.setSelected(sound);
                return;
            }

            for (Mode mode : Mode.values())
            {
                String modeName = mode.toString();
                modeName = modeName.charAt(0) + modeName.substring(1).toLowerCase();
                String menuItem = title + '/' + modeName;
                JMenuItem selected = items.get(menuItem);
                if (selected != null)
                    selected.setSelected(false);
            }
            items.get(menu).setSelected(true);

            Mode mode = gui.mode(item);
            if (mode.tutorial())
            {
                players[0] = new Tutorial('O');
                players[1] = new Tutorial('X');
            }
            else
            {
                players[0] = players[0 + GUI.NUM_PLAYERS];
                players[1] = players[1 + GUI.NUM_PLAYERS];
            }
            timer.setVisible(mode == Mode.SPEED);
            score1.setVisible(mode == Mode.ENDLESS);
            score2.setVisible(mode == Mode.ENDLESS);
        }
    }

    /**
     * Add a menu to the bar
     * @param menubar - The menu bar
     * @param title - Title of the menu
     * @param entries - List of menu items
     */
    JMenu addMenu(JMenuBar menubar, String title, String[] entries)
    {
        //Whenever menu is selected
        ActionListener menuSelected = new ActionListener()        //Whenever it is chosen
        {
            @Override
            public void actionPerformed(ActionEvent e)  //The actionPerformed is call
            {
                menuAction(e.getActionCommand());    //Call with the menu item name
            }
        };
        JMenu menu = new JMenu(title);				//Make top line
        for (String text : entries)					//With the following entries
        {
            if (text.equals("--"))
            {
                menu.addSeparator();
            }
            else
            {
                char ch = text.charAt(0);
                if (ch == '!')
                {
                	JMenuItem item = new JMenuItem(text.substring(1));
                    menu.add(item);                         //Add it to the root
                    item.setEnabled(false);
                }
                else
                {
                    JMenuItem item;
                    if (ch == '+')
                    {
                        text = text.substring(1);
                        if (text.equals("Sound")) {
                        	String cmd = title + "/" + text;
                        	item = new JMenu(text);   //New item to add
                        	ButtonGroup group = new ButtonGroup();
                        	final JMenuItem on = new JRadioButtonMenuItem("On");
                        	final JMenuItem off = new JRadioButtonMenuItem("Off");
                        	group.add(on);
                        	group.add(off);
                        	group.setSelected(on.getModel(), true);
                        	on.setActionCommand(cmd);
                        	off.setActionCommand(cmd);
                        	on.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									on.setEnabled(true);
									menuAction(e.getActionCommand());
								}
                        		
                        	});
                        	
                        	off.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									off.setEnabled(true);
									menuAction(e.getActionCommand());
								}
                        		
                        	});
                        	item.add(on);
                        	item.add(off);
                        } else {
                        	item = new JMenuItem(text);   //New item to add
                        }
  
                    } 
                    else
                    {
                        //item = new JMenuItem(text);   //New item to add
                    	//System.out.println(text);
                    	if (text.equals("Exit")) {
                    		//System.out.println("adding exit...");
                    	    ImageIcon icon = getIcon("iconexit.png");
                    		item = new JMenuItem(text, icon);
                    		item.setMnemonic(KeyEvent.VK_Q);
                    	} else if (text.equals("Easy")) {
                    	    ImageIcon icon = getIcon("iconeasy.png");
                    		item = new JMenuItem(text, icon);
                    	} else if (text.equals("Medium")) {
                    	    ImageIcon icon = getIcon("iconmedium.png");
                    		item = new JMenuItem(text, icon);
                    	} else if (text.equals("Hard")) {
                    	    ImageIcon icon = getIcon("iconhard.png");
                    		item = new JMenuItem(text, icon);
                    	}  else if (text.equals("Normal") || text.equals("Speed") || text.equals("Endless")) {
                        	item = new JMenu(text);
                        	String cmd = title + "/" + text;
                        	JMenuItem hvh = new JMenuItem("Human vs Human");
                        	hvh.setActionCommand(cmd);
                        	hvh.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									select("Player 1", "Human");
									select("Player 2", "Human");
									menuAction(e.getActionCommand()); 
								}
                        		
                        	});
                        	JMenuItem hvc = new JMenuItem("Human vs Computer");
                        	hvc.setActionCommand(cmd);
                        	hvc.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									Object[] difficulties =  {"Easy", "Medium", "Hard"};
									String difficulty = null;
									difficulty = (String)JOptionPane.showInputDialog(
								                  gui,
								                  "Select the difficulty of Computer",
								                  e.getActionCommand().substring(e.getActionCommand().indexOf('/')+1) +" Mode",
								                  JOptionPane.PLAIN_MESSAGE,
								                  getIcon(null),
								                  difficulties,
								                  "Easy");
									select("Player 1", "Human");
									if (difficulty != null)
										select("Player 2", difficulty);
									else 
										select("Player 2", "Hard");
									menuAction(e.getActionCommand()); 
								}
                        		
                        	});
                        	
                        	JMenuItem cvc = new JMenuItem("Computer vs Computer");
                        	cvc.setActionCommand(cmd);
                        	cvc.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									Object[] difficulties =  {"Easy", "Medium", "Hard"};
									String computerA = null;
									String computerB = null;

									computerA = (String)JOptionPane.showInputDialog(
							                  gui,
							                  "Select the difficulty of Computer 1",
							                  e.getActionCommand().substring(e.getActionCommand().indexOf('/')+1) +" Mode",
							                  JOptionPane.PLAIN_MESSAGE,
							                  getIcon(null),
							                  difficulties,
							                  "Easy");
									
									computerB = (String)JOptionPane.showInputDialog(
							                  gui,
							                  "Select the difficulty of Computer 2",
							                  e.getActionCommand().substring(e.getActionCommand().indexOf('/')+1) +" Mode",
							                  JOptionPane.PLAIN_MESSAGE,
							                  getIcon(null),
							                  difficulties,
							                  "Easy");
									if (computerA != null)
										select("Player 1", computerA);
									else 
										select("Player 1", "Hard");
									if (computerB != null)
										select("Player 2", computerB);
									else 
										select("Player 2", "Hard");
								menuAction(e.getActionCommand()); 

								}
                        	});
                        	
                        	item.add(hvc);
                        	item.add(cvc);
                        	item.add(hvh);
                        } else {
//                    		System.out.println("adding ("+text+")");
                    		item = new JMenuItem(text);   //New item to add
                    	}
                    }
                    String cmd = title + "/" + text;
                    items.put(cmd, item);
                    menu.add(item);                         //Add it to the root
                    item.setActionCommand(cmd);
                    item.addActionListener(menuSelected);   //When selected, call the menuSelected listener
                }
            }
        }
        if (entries.length == 0)
        {
            menu.setFocusable(false);
        }
        menubar.add(menu);							//Add it to the menubar
        return menu;
    }
    
    private ImageIcon getIcon(String fileName) {
    	 try
         {
    		 ImageIcon icon = new ImageIcon("src/connect4/"+fileName);
    		 return icon;
         }
         catch (Exception err)
         {
             System.out.println("Exception:" + err);
         }
    	 return null;
    }

    public void select(String player, String difficulty)
    {
        if (gui.getMode().tutorial()) return;

        for (String choice : choices)
        {
            String menu = player + "/" + choice;
            items.get(menu).setIcon(null);
        }
        String menu = player + "/" + difficulty;
        int which = 0;
        PieceIcon icon = player1;
        char symbol = 'O';
        if (player.equals("Player 2"))
        {
            icon = player2;
            which = 1;
            symbol = 'X';
        }
        items.get(menu).setIcon(icon);
        //names
        if (difficulty.equals("Human"))
        {
            players[which] = new Human("Human", symbol);
            players[which + GUI.NUM_PLAYERS] = new Human("Human", symbol);
        }
        if (difficulty.equals("Easy"))
        {
            players[which] = new Computer(0, symbol);
            players[which + GUI.NUM_PLAYERS] = new Computer(0, symbol);
        }
        if (difficulty.equals("Medium"))
        {
            players[which] = new Computer(1, symbol);
            players[which + GUI.NUM_PLAYERS] = new Computer(1, symbol);
        }
        if (difficulty.equals("Hard"))
        {
            players[which] = new Computer(2, symbol);
            players[which + GUI.NUM_PLAYERS] = new Computer(2, symbol);
        }
    }


    public Player[] getPlayers()
    {
        return players;
    }

    public void setScore(int score, int player)
    {
        String text = "000000" + Integer.toString(score);
        text = text.substring(text.length()-6);
        switch (player)
        {
            case 0:
                score1.setText(text);
                break;
            case 1:
                score2.setText(text);
                break;
        }
    }
}
