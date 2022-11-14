import java.awt.Color;
import javax.swing.JFrame;

public class Wordle {

    public static void main(String[] args) {

        try {
            WordleGUI gui = new WordleGUI();

            // Setup of the frame containing the game
            JFrame frame = new JFrame();
            frame.setTitle("Worlde");
            frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setBackground(Color.black);
            frame.getContentPane().add(gui);            

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            
            frame.setVisible(true);
            
        } catch (Exception e) {
            System.out.println("Could not make the WordleGUI frame: " + e);
        }

    }    
}
