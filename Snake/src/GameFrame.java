import javax.swing.*;

public class GameFrame extends JFrame {

    GameFrame(){

        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack(); //Fit the JFrame around the components.
        this.setVisible(true);
        this.setLocationRelativeTo(null); //Center the window on middle of computer.
    }
}
