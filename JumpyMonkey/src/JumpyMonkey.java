import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class JumpyMonkey extends JPanel implements  ActionListener, KeyListener{ //This JumpyMonkey class inherits the Jpanel class

    int boardWidth = 350;
    int boardHeight = 640;
    //Resolution

    Image backgroundImg;
    Image monkeyImg;
    Image topmonkeyImg;
    Image bottomtreeImg;
    Image panelIconImg;
    //Images

    int monkeyX = boardWidth/8; //The x position of the monkey will be 1/8 of the left side from the screen
    int monkeyY = boardHeight/2; //The y position of the monkey will be half way from the top of the screen
    int monkeyWidth = 30;
    int monkeyHeight = 30;

    class Monkey{
        int x = monkeyX;
        int y = monkeyY;
        int width = monkeyWidth;
        int height = monkeyHeight;
        Image img;

        Monkey(Image img){
            this.img = img;
        }
        //defines constructor
    }
    //Monkey

    int obstX = boardWidth;
    int obstY = 0;
    int obstWidth = 64;
    int obstHeight = 512;

    class Obstacle{

        int x = obstX;
        int y = obstY;
        int width = obstWidth;
        int height = obstHeight;
        Image img;
        boolean passed = false; //checks whether the monkey has passed the obstacle

        Obstacle(Image img){
            this.img = img;
        }
    }
    //Obstacles

    Monkey monkey;
    int velocityX = -4; //Moves the obstacles to the left at 4 pixels (simulates the mnonkey moving right)
    int velocityY = 0; //Game will start with the monkey falling down
    int gravity = 1; //Adds a downward force onto the monkey. Every frame, monkey will slow down by 1 pixel

    ArrayList<Obstacle> obstacles;
    Random random = new Random();

    Timer gameLoop; //Variable for game loop
    Timer placeObstaclesTimer; //Time for obstacles

    double score = 0; //initial score

    boolean gameOver = false;
    //game logic

    JumpyMonkey(){ 

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true); //Ensures that the Jumpy Monkey class (the Jpanel) takes in the key events
        addKeyListener(this);

        backgroundImg = new ImageIcon(getClass().getResource("./backgroun1d.png")).getImage();
        monkeyImg = new ImageIcon(getClass().getResource("./mon1key.png")).getImage();
        topmonkeyImg = new ImageIcon(getClass().getResource("./topmonkey.png")).getImage();
        bottomtreeImg = new ImageIcon(getClass().getResource("./bottomtree.png")).getImage();
        panelIconImg = new ImageIcon(getClass().getResource("./mon1key.png")).getImage();
        //assigns the images into the variables

        monkey = new Monkey(monkeyImg); //Creates the Monkey image and passes on the image
        obstacles = new ArrayList<Obstacle>();

        placeObstaclesTimer = new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placeObstacles();
            }
        }); //Place obstacles every 1500    
        placeObstaclesTimer.start();

        gameLoop = new Timer(1999/60, this); //Creates game timer object. Repeated at 60 fps.
        gameLoop.start(); //Game loop is important as it continuosly draws the frames for the game

    }
    //Creation of constructor

    public void placeObstacles(){

        int randomObstacleY = (int) (obstY - obstHeight/4 - Math.random()*(obstHeight/2));
        int openingSpace = boardHeight/4;

        Obstacle topObstacle = new Obstacle(topmonkeyImg);
        topObstacle.y = randomObstacleY;
        obstacles.add(topObstacle);


        Obstacle bottomObstacle = new Obstacle(bottomtreeImg);
        bottomObstacle.y = topObstacle.y + obstHeight + openingSpace;
        obstacles.add(bottomObstacle);
    }

    public void paintComponent(Graphics g){ 
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        g.drawImage(panelIconImg, 0, 0, boardWidth, boardHeight, null); //icon
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null); //background
        g.drawImage(monkey.img, monkey.x, monkey.y, monkey.width, monkey.height, null); //monkey
        for (int i = 0; i < obstacles.size(); i++)
        {
            Obstacle obstacle = obstacles.get(i);
            g.drawImage(obstacle.img, obstacle.x, obstacle.y, obstacle.width, obstacle.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 40));

        if (gameOver)
        {
            g.drawString("Game Over: " +String.valueOf((int) score), 10, 35);
        }
        else
        {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }
    /*
        Draws the images. X & Y is the starting position (top left of the corner),
        boardWidth and boardHeight specifies how big u want the image.

    */

    public void move(){
        velocityY += gravity;
        monkey.y += velocityY;
        monkey.y = Math.max(monkey.y, 0); //Prevents the monkey from gooing up beyong the screen. Y position is capped at 0
    //monkey
    
    for (int i = 0; i < obstacles.size(); i++)
    {
        Obstacle obstacle = obstacles.get(i);
        obstacle.x += velocityX;

        if (!obstacle.passed && monkey.x > obstacle.x + obstacle.width)
        {
            obstacle.passed = true;
            score += 0.5; //0.5 because there are 2 obstacles. 0.5 * 2 = 1, 1 for each set of obstacles
        }

        if (collision(monkey, obstacle))
        {
            gameOver = true;
        }
    }

    if (monkey.y > boardHeight)
    {
        gameOver = true;
    }
    //obstacles
    }   

    public boolean collision (Monkey a, Obstacle b){
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
        a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
        a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
        a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();  //peforms the move method at 60fps
        repaint(); //Calls paint component
        if (gameOver)
        {
            placeObstaclesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            velocityY = -9;

            if (gameOver)
            {
                monkey.y = monkeyY;
                velocityY = 0;
                obstacles.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeObstaclesTimer.start();
                //restart the game by resetting the conditions
            }
        }
        // If they space bar key is pressed, the velocity will be set to -9

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {  
    }


    
}
