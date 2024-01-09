import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;

    //Setting x and y coordinates for the snake.
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6; //Begin with 6 body parts on the snake.

    int applesEaten;
    int appleX; //X-coordinates of where the apple is located.
    int appleY;
    char direction = 'R'; //Snake will begin going to direction of right.
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {

        if(running) { //If the game is running.
            //Draw lines across the game panel to get grids.
            /*for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }*/
            //Draw the apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //Set the size of the apple.

            //Draw the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) { //The head of the snake.
                    g.setColor(new Color(1, 50, 32));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else { //If not == 0 then its the body of the snake.
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            //Display the current score.
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        }
        else{ //Else call gamve over method.
            gameOver(g);
        }
    }
    public void newApple(){
        //Calculate so that the apple can be places evenly in one square.
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        //Moving the snake by shifting the body parts coordinates.
        for(int i = bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        //Change the direction of where the snake is going.
        switch(direction){ //4 directions and thus 4 cases.
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++; //Increment the body parts.
            applesEaten++; //Increment the score.
            newApple(); //Generate new apple.
        }
    }
    public void checkCollisions(){
        //Check if head collided with the body.
        for(int i=bodyParts; i>0;i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false; //Running is false, and trigger Game Over method.
            }
        }

        //Check if Head collides with the left border
        if(x[0] < 0){
            running = false;
        }
        //Check if Head collides with the right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        //Check if Head collides with the top border
        if(y[0] < 0){
            running = false;
        }
        //Check if Head collides with the bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop(); //Stop timer if not running.
        }

    }
    public void gameOver(Graphics g){
        //display the score
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        FontMetrics metrics1 = getFontMetrics(g.getFont()); //Get information about the font.
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

        //Game over text.
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 80));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT / 2); //Gives game over in the center of the screen.

    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) { //If the game is running.
            move(); //Move the snake.
            checkApple(); //Check if run into an apple.
            checkCollisions();
        }
        repaint(); //If game not running, repaint.
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){ //Control the snake.
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
