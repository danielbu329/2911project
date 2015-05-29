package connect4;

import javax.swing.*;

import java.net.URL;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
    private JMenu hvcDiff;
    private JMenu cvcDiff;
    private JMenu timeLeft;
    private Mode currMode;
    private String compDifficulty1;
    private String compDifficulty2;
    
    
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
        currMode = Mode.NORMAL;	
        compDifficulty1 = "Easy";
        compDifficulty2 = "Hard";
        
        items = new HashMap<>(20);
        choices = "Human/Easy/Medium/Hard".split("/");
        JMenuBar menubar = new JMenuBar();      //Create a menu bar
     //   JMenu menu = addMenu(menubar, "Game", "Restart Game/Normal/Speed/Endless/--/+Sound/--/Exit".split("/"));
        JMenu menu = addMenu(menubar, "Game", "Restart Game/--/!New Game/Normal/Speed/Endless/--/+Sound/--/Exit".split("/"));
        menu.setMnemonic(KeyEvent.VK_G);
        menu.setIcon(getIcon("iconmenu.png"));
//        menu = addMenu(menubar, "Player 1", "Human/--/!Computer/Easy/Medium/Hard".split("/"));
//        menu.setIcon(player1);
//        menu = addMenu(menubar, "Player 2", "Human/--/!Computer/Easy/Medium/Hard".split("/"));
//        menu.setIcon(player2);
        
        menu = addMenu(menubar, "Mode", "-Modes".split("/"));
        menu.setIcon(getIcon("icontinyboard.png"));
        
        menu = addMenu(menubar, "Players", "-Human vs Human/-Human vs Computer/-Computer vs Computer".split("/"));
        menu.setIcon(getIcon("iconhumanvscomputer.png"));        
        
        menu = addMenu(menubar, "Difficulty", "!Singleplayer/-SinglePlayerDifficulties".split("/"));
        hvcDiff = menu;
        hvcDiff.setVisible(true);
       	menu.setIcon(getIcon("icondifficulty.png"));
       	
       	menu = addMenu(menubar, "Ai Difficulty", "!AI vs AI/-Yellow/-Red".split("/"));
       	menu.setIcon(getIcon("icondifficulty.png"));
       	cvcDiff = menu;
       	cvcDiff.setVisible(false);
        
        menu = addMenu(menubar, "Help", "Tutorial".split("/"));
        menu.setMnemonic(KeyEvent.VK_H);
        menu.setIcon(getIcon("iconhelp.png"));
        
        
        menubar.add(Box.createHorizontalGlue());
        timeLeft = new JMenu("Time Left:");
        menubar.add(timeLeft);
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
    	System.out.println(menu);
        int slash = menu.indexOf('/');
        String title = menu.substring(0, slash);
        String item = menu.substring(slash+1);
        if (title.startsWith("Player"))
        {
        	System.out.println("wtf");
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
//                JMenuItem soundItem = items.get(menu);
                //soundItem.setIcon(getIcon("iconsound.png"));
                sound = !sound;
//                soundItem.setSelected(sound);
                return;
            }
            
            if (item.equals("New Game"))
            {
            	//restart game
            }

            for (Mode mode : Mode.values())
            {
                String modeName = mode.toString();
                modeName = modeName.charAt(0) + modeName.substring(1).toLowerCase();
                String menuItem = title + '/' + modeName;
//                JMenuItem selected = items.get(menuItem);
//                if (selected != null)
//                    selected.setSelected(false);
            }
//            items.get(menu).setSelected(true);
            Mode mode = gui.mode(item);
            currMode = mode;
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
            timeLeft.setVisible(mode == Mode.SPEED);
            score1.setVisible(mode == Mode.ENDLESS);
            score2.setVisible(mode == Mode.ENDLESS);
        }
        
	    if (title.equals("P1")) {
	    	select("Player 1", item);
	    } else if (title.equals("P2")) {
	    	select("Player 2", item);
	    }
	    
	    if (compDifficulty2.equals("Easy")) {
			JMenu myMenu= (JMenu) cvcDiff.getMenuComponent(2);
			JRadioButtonMenuItem rad= (JRadioButtonMenuItem) myMenu.getMenuComponent(0);
			System.out.println(rad.getText());
			rad.setSelected(true);
		} 
		if (compDifficulty2.equals("Medium")) {
			JMenu myMenu= (JMenu) cvcDiff.getMenuComponent(2);
			JRadioButtonMenuItem rad= (JRadioButtonMenuItem) myMenu.getMenuComponent(1);
			rad.setSelected(true);
		}
		if (compDifficulty2.equals("Hard")) {
			JMenu myMenu= (JMenu) cvcDiff.getMenuComponent(2);
			JRadioButtonMenuItem rad= (JRadioButtonMenuItem) myMenu.getMenuComponent(2);
			rad.setSelected(true);
		}
		if (compDifficulty1.equals("Easy")) {
			JMenu myMenu= (JMenu) cvcDiff.getMenuComponent(1);
			JRadioButtonMenuItem rad= (JRadioButtonMenuItem) myMenu.getMenuComponent(0);
			System.out.println(rad.getText());
			rad.setSelected(true);
		} 
		if (compDifficulty1.equals("Medium")) {
			JMenu myMenu= (JMenu) cvcDiff.getMenuComponent(1);
			JRadioButtonMenuItem rad= (JRadioButtonMenuItem) myMenu.getMenuComponent(1);
			rad.setSelected(true);
		}
		if (compDifficulty1.equals("Hard")) {
			JMenu myMenu= (JMenu) cvcDiff.getMenuComponent(1);
			JRadioButtonMenuItem rad= (JRadioButtonMenuItem) myMenu.getMenuComponent(2);
			rad.setSelected(true);
		}
		if (compDifficulty2.equals("Easy")) {
			JRadioButtonMenuItem rad= (JRadioButtonMenuItem) hvcDiff.getMenuComponent(1);
			rad.setSelected(true);
		} 
		if (compDifficulty2.equals("Medium")) {
			JRadioButtonMenuItem rad= (JRadioButtonMenuItem) hvcDiff.getMenuComponent(2);
			rad.setSelected(true);
		}
		if (compDifficulty2.equals("Hard")) {
			JRadioButtonMenuItem rad= (JRadioButtonMenuItem) hvcDiff.getMenuComponent(3);
			rad.setSelected(true);
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
                	text = text.substring(1);
                	JMenuItem item = new JMenuItem(text);
                	if (text.equals("AI vs AI")) {
                		item.setIcon(getIcon("iconcomputervscomputer.png"));
                	}
                		
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
                        	// add sub menus for Sound
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
                    else if (ch == '-') 
                    {
                    	text = text.substring(1);
                    	if (text.equals("Modes")) {
                        	ImageIcon icon = getIcon("iconnormal.png");
                        	ButtonGroup bg = new ButtonGroup();
                        	item = new JRadioButtonMenuItem("Normal", icon);
                        	item.setActionCommand("Game/Normal");
                        	item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									if (!checkDiscardGame())
										return;
									menuAction(e.getActionCommand());
								}
                        	});
                        	items.put("Game/Normal", item);
                        	bg.add(item);
                        	menu.add(item);
                        	item.setSelected(true);
                        	
                        	icon = getIcon("iconspeed.png");
                         	item = new JRadioButtonMenuItem("Speed", icon);
                         	item.setActionCommand("Game/Speed");
                         	item.addActionListener(new ActionListener() {
 								@Override
 								public void actionPerformed(ActionEvent e) {
 									if (!checkDiscardGame())
 										return;
 									menuAction(e.getActionCommand());
 								}
                         	});
                         	items.put("Game/Speed", item);
                         	bg.add(item);
                         	menu.add(item);
                         	
                         	
                         	icon = getIcon("iconendless.png");
                        	item = new JRadioButtonMenuItem("Endless", icon);
                        	item.setActionCommand("Game/Endless");
                        	item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									if (!checkDiscardGame())
										return;
									menuAction(e.getActionCommand());
								}
                        	});
                        	items.put("Game/Endless", item);
                        	bg.add(item);
                        	
                        } else if (text.equals("Human vs Human")) {
                        	ImageIcon icon = getIcon("iconhumanvshuman.png");
                    		item = new JMenuItem(text, icon);
                    		String cmd;
                    		title = "Game";
                    		if (currMode == Mode.NORMAL || currMode == Mode.TUTORIAL) {
                    			 cmd = title + "/" + "Normal";
                    		} else if (currMode == Mode.SPEED) {
                    			cmd = title + "/" + "Speed";
                    		} else {
                    			cmd = title + "/" + "Endless";
                    		}
                    		item.setActionCommand(cmd);
                    		menu.add(item);
                    		item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									if (!checkDiscardGame())
										return;
									
									select("Player 1", "Human");
									select("Player 2", "Human");
									cvcDiff.setVisible(false);
									hvcDiff.setVisible(false);
									menuAction(e.getActionCommand());
									
								}
                    		});
//                    		items.put(cmd, item);
                    	} else if (text.equals("Human vs Computer")) {
                    		ImageIcon icon = getIcon("iconhumanvscomputer.png");
                    		item = new JMenuItem(text, icon);
                    		String cmd;
                    		title = "Game";
                    		if (currMode == Mode.NORMAL || currMode == Mode.TUTORIAL) {
                    			 cmd = title + "/" + "Normal";
                    		} else if (currMode == Mode.SPEED) {
                    			cmd = title + "/" + "Speed";
                    		} else {
                    			cmd = title + "/" + "Endless";
                    		}
                    		item.setActionCommand(cmd);
                    		menu.add(item);
                    		item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									if (!checkDiscardGame())
										return;
									select("Player 1", "Human");
									select("Player 2", compDifficulty2);
									cvcDiff.setVisible(false);
									hvcDiff.setVisible(true);
									menuAction(e.getActionCommand());

								}
								
                    		});
                    	} else if (text.equals("Computer vs Computer")) {
                    		ImageIcon icon = getIcon("iconcomputervscomputer.png");
                    		item = new JMenuItem(text, icon);
                    		String cmd;
                    		title = "Game";
                    		if (currMode == Mode.NORMAL || currMode == Mode.TUTORIAL) {
                    			 cmd = title + "/" + "Normal";
                    		} else if (currMode == Mode.SPEED) {
                    			cmd = title + "/" + "Speed";
                    		} else {
                    			cmd = title + "/" + "Endless";
                    		}
                    		item.setActionCommand(cmd);
                    		menu.add(item);
                    		item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									if (!checkDiscardGame())
										return;
									select("Player 1", compDifficulty1);
									select("Player 2", compDifficulty2);
									cvcDiff.setVisible(true);
									hvcDiff.setVisible(false);
									menuAction(e.getActionCommand());
								}
                    		});
                    	} else if (text.equals("SinglePlayerDifficulties")) {
                    		String cmd;
                    		
                    		ButtonGroup bg = new ButtonGroup();
                    	    ImageIcon icon = getIcon("iconeasy.png");
                    		item = new JRadioButtonMenuItem("Easy", icon);
                    		bg.add(item);
                    		System.out.println(title);
                    		cmd = "Player 2" + "/" + "Easy";
                    		item.setActionCommand(cmd);
                    		item.addActionListener(menuSelected);
                    		menu.add(item);
                    		
                    		icon = getIcon("iconmedium.png");
                    		item = new JRadioButtonMenuItem("Medium", icon);
                    		bg.add(item);
                    		cmd = "Player 2"  + "/" + "Medium";
                    		item.setActionCommand(cmd);
                    		item.addActionListener(menuSelected);
                    		menu.add(item);
                    		
                    		icon = getIcon("iconhard.png");
                    		item = new JRadioButtonMenuItem("Hard", icon);
                    		bg.add(item);
                    		cmd = "Player 2"  + "/" + "Hard";
                    		item.setSelected(true);
                    		item.setActionCommand(cmd);
                    		item.addActionListener(menuSelected);
                    		
                    	} else if (text.equals("Yellow") || text.equals("Red")){
                    		String cmd;
                    		if (text.equals("Yellow")) {
                    			cmd = "P1/";
                    		} else {
                    			cmd = "P2/";
                    		}
                    		ButtonGroup bg = new ButtonGroup();
                    		JRadioButtonMenuItem easy = new JRadioButtonMenuItem("Easy");
                    		easy.setIcon(getIcon("iconeasy.png"));
                    		easy.setActionCommand(cmd + "Easy");
                    		easy.addActionListener(menuSelected);
                    		JRadioButtonMenuItem medium = new JRadioButtonMenuItem("Medium");
                    		medium.setIcon(getIcon("iconmedium.png"));
                    		medium.setActionCommand(cmd + "Medium");
                    		medium.addActionListener(menuSelected);
                    		JRadioButtonMenuItem hard = new JRadioButtonMenuItem("Hard");
                    		hard.setIcon(getIcon("iconhard.png"));
                    		hard.setActionCommand(cmd + "Hard");
                    		hard.addActionListener(menuSelected);
                    		bg.add(easy);
                    		bg.add(medium);
                    		bg.add(hard);
                    		
                    		if (text.equals("Yellow"))
                    			easy.setSelected(true);
                    		else
                    			hard.setSelected(true);
                    
                    		if (text.equals("Yellow")) {
	                    		item = new JMenu("Yellow");
	                    		item.add(easy);
	                    		item.add(medium);
	                    		item.add(hard);
                    		} else {
                    			item = new JMenu("Red");
                    			item.add(easy);
	                    		item.add(medium);
	                    		item.add(hard);
                    		}
                    	} else {
                    		item = new JMenuItem(text);   //New item to add
                        }
                        menu.add(item); //add it to the menu
                    }
                    else
                    {
                    	if (text.equals("Exit")) {
                    		//System.out.println("adding exit...");
                    	    ImageIcon icon = getIcon("iconexit.png");
                    		item = new JMenuItem(text, icon);
                    		item.setMnemonic(KeyEvent.VK_Q);
                    	} else if (text.equals("Normal") || text.equals("Speed") || text.equals("Endless")) {
                    		// Add submenus for Normal, Speed, and Endless gamemodes
                        	item = new JMenu(text);
                        	if (text.equals("Normal"))
                        		item.setMnemonic(KeyEvent.VK_N);
                        	String cmd = title + "/" + text;
                        	JMenuItem hvh = new JMenuItem("Human vs Human");
                        	hvh.setActionCommand(cmd);
                        	hvh.setIcon(getIcon("iconhumanvshuman.png"));
                        	hvh.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									if (!checkDiscardGame())
										return;
									select("Player 1", "Human");
									select("Player 2", "Human");
									menuAction(e.getActionCommand()); 
									cvcDiff.setVisible(false);
									hvcDiff.setVisible(false);
								}
                        		
                        	});
                        	JMenuItem hvc = new JMenuItem("Human vs Computer");
                        	hvc.setActionCommand(cmd);
                        	hvc.setIcon(getIcon("iconhumanvscomputer.png"));
                        	hvc.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									if (!checkDiscardGame())
										return;
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
									cvcDiff.setVisible(false);
									hvcDiff.setVisible(true);

								}
                        		
                        	});
                        	
                        	JMenuItem cvc = new JMenuItem("Computer vs Computer");
                        	cvc.setActionCommand(cmd);
                        	cvc.setIcon(getIcon("iconcomputervscomputer.png"));
                        	cvc.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									if (!checkDiscardGame())
										return;
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
								cvcDiff.setVisible(true);
								hvcDiff.setVisible(false);
								}
                        	});
                        	item.add(hvh);
                        	item.add(hvc);
                        	item.add(cvc);
                        	
                    	} else if (text.equals("Restart Game")) {
                    		ImageIcon icon = getIcon("iconrestart.png");
                    		item = new JMenuItem(text, icon);
                    		item.setMnemonic(KeyEvent.VK_R);
                    		String cmd;
                    		if (currMode == Mode.NORMAL || currMode == Mode.TUTORIAL) {
                    			 cmd = title + "/" + "Normal";
                    		} else if (currMode == Mode.SPEED) {
                    			cmd = title + "/" + "Speed";
                    		} else {
                    			cmd = title + "/" + "Endless";
                    		}
                    		item.setActionCommand(cmd);
                    		menu.add(item);
                    		item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									if (!checkDiscardGame())
										return;
									menuAction(e.getActionCommand());
								}
                    		});
                        } else {
                    		item = new JMenuItem(text);   //New item to add
                    		if (text.equals("Tutorial"))
                    			item.setIcon(getIcon("icontutorial.png"));
                    	}
                    } 
                    
                    if (!text.equals("Restart Game") && ch != '-') {
	                    String cmd = title + "/" + text;
	                    items.put(cmd, item);
	                    menu.add(item);                         //Add it to the root
	                    item.setActionCommand(cmd);
	                    item.addActionListener(menuSelected);   //When selected, call the menuSelected listener
                    }
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
    
    /**
     * Dialogue to confirm with the user if they want to discard the current game
     * @return true or false
     */
    private boolean checkDiscardGame() {
    	int n;
    	if (gui.getPlaying() >= 0 && gui.getPlaying() <= 2) {
	    	n = JOptionPane.YES_OPTION;
    	} else {
    		n = JOptionPane.showConfirmDialog(
	    		    gui,
	    		    "Leave Current Game?",
	    		    "",
	    		    JOptionPane.YES_NO_OPTION);
    	}
    	if (n == JOptionPane.YES_OPTION)
    			return true;
    	else
    		return false;
    }
    
    /**
     * Retreives the icon requested
     * @param filename the name of the icon file
     * @return the image
     */
    private ImageIcon getIcon(String filename)
    {
        try
        {
            URL imgURL = getClass().getResource(filename);
            return new ImageIcon(imgURL);
        }
        catch (Exception err)
        {
        }

        try
        {
            return new ImageIcon("src/connect4/" + filename);
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
//            items.get(menu).setIcon(null);
        }
//        String menu = player + "/" + difficulty;
        int which = 0;
        PieceIcon icon = player1;
        char symbol = 'O';
        if (player.equals("Player 2"))
        {
            icon = player2;
            which = 1;
            symbol = 'X';
           
        }
        if (player.equals("Player 1") && !difficulty.equals("Human")) {
        	compDifficulty1 = difficulty;
        } else if (player.equals("Player 2") && !difficulty.equals("Human")) {
        	compDifficulty2 = difficulty;
        }
        	
//        items.get(menu).setIcon(icon);
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
