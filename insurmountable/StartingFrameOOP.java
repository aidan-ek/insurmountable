//this template can be used for a start screen
package insurmountable;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
//import javax.swing.SwingUtilities;

class StartingFrameOOP extends JFrame implements GameConstants{ 
    
    JFrame thisFrame;
    
    //constructor - this runs first
    StartingFrameOOP() { 
        super("Start Screen");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(START_W, START_H);
        this.setResizable(false); 
        this.setLocationRelativeTo(null); //start the frame in the center of the screen
        
        //create the start panel (where intro graphics or GUI elements are drawn)
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());
        
        //create a JButton for the panel
        JButton startButton = new JButton("START");
        //attach a listener to the button
        startButton.addActionListener(new StartButtonListener(this));
        
        //create a label for the panel
        JLabel startLabel = new JLabel("<HTML><H1>Basic OOP Game Example</H1></HTML>",JLabel.CENTER);
        
        //add all to the panel according to border layout
        startPanel.add(startButton,BorderLayout.SOUTH);
        startPanel.add(startLabel,BorderLayout.CENTER);
        
        //add the panel to the frame
        this.add(startPanel);
        this.requestFocusInWindow();      //make this window the active window
        this.setVisible(true);        
    }

    //the main method  - starts this application
    public static void main(String[] args) { 
        new StartingFrameOOP();
    }
}