import javax.swing.*;


public class App {
    public static void main(String[] args) throws Exception {
        
        int boardWidth = 366;
        int boardHeight = 640;
        //dimensions of the window

        JFrame frame = new JFrame("Jumpy Monkey"); //Titelbar is called "Jumpy Monkey"
        frame.setSize(boardWidth, boardHeight); //Tab resolution has been set
        frame.setLocationRelativeTo(null); //Frame is centred in the middle
        frame.setResizable(false); //Frame cannot be resized
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Hitting 'x'on the titlebar will terminate the prorgram
   
        ImageIcon icon = new ImageIcon(App.class.getResource("./mon2key.png")); // Load the icon
        frame.setIconImage(icon.getImage()); // Set it as the frame icon

        JumpyMonkey  jumpymonkey = new JumpyMonkey(); //Creation of JumpyMonkey instance
        frame.add(jumpymonkey); //adds the new instance into the frame
        frame.pack(); //ensures the 366 x 640 resolution doesn't include the titlebar.
        jumpymonkey.requestFocus();
        frame.setVisible(true); //Frame is visible
    }
}
