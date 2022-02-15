import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.program.GraphicsProgram;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import svu.csc213.Dialog;

public class Breakout extends GraphicsProgram {

    /*
    we dont run outa lives //yeah we do
    all the bricks only have 1 hit //done
    what happens if i run outa lives //i mean technically
    how do I know how many lives I have left //done
    how many bricks broke

    how could I make some bricks contain be power ups
    how could I make this game have more than one level
     */

    private Ball ball;
    private Paddle paddle;
    private int score=0;

    public int lives=3;



    public Random randy = new Random();

    private int numBricksInRow;

    private GLabel lifeLabel= new GLabel("lives left = " + String.valueOf(lives));

    private GLabel scoreLabel = new GLabel("Points = "+score);

    public Color[] rowColors = {Color.black,Color.black,Color.darkGray,Color.darkGray,Color.red,Color.red,Color.pink, Color.pink,Color.orange,Color.orange};

    @Override
    public void init(){


        add(scoreLabel,scoreLabel.getWidth()/10,getHeight()-scoreLabel.getHeight()*2);

        numBricksInRow = (int) (getWidth() / (Brick.WIDTH + 5.0));

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < numBricksInRow; col++) {
                    double brickX = 10 + col * (Brick.WIDTH + 5);
                    double brickY = 1 * Brick.HEIGHT + row * (Brick.HEIGHT + 2);

                    Brick brick = new Brick(brickX, brickY, rowColors[row], row, 5 - row/2);
                    add(brick);
                }
        }

        ball = new Ball(getWidth()/2, 250, 10, this.getGCanvas());
        add(ball);

        paddle = new Paddle(230, 430, 50 ,10);
        add(paddle);

        add(lifeLabel,getWidth()-lifeLabel.getWidth()-lifeLabel.getWidth()/10,getHeight()-lifeLabel.getHeight()*2);
        lifeLabel.sendToFront();
    }

    @Override
    public void run(){
        addMouseListeners();
        waitForClick();
        gameLoop();
    }

    @Override
    public void mouseMoved(MouseEvent me){
        // make sure that the paddle doesn't go offscreen
        if((me.getX() < getWidth() - paddle.getWidth()/2)&&(me.getX() > paddle.getWidth() / 2)){
            paddle.setLocation(me.getX() - paddle.getWidth()/2, paddle.getY());
        }
    }

    private void gameLoop(){
        while(true){
            // move the ball
            ball.handleMove();

            // handle collisions
            handleCollisions();

            // handle losing the ball
            if(ball.lost){
                handleLoss();
            }



            pause(5);
        }
    }


    private void handleCollisions(){
        // obj can store what we hit
        GObject obj = null;

        // check to see if the ball is about to hit something

        if(obj == null){
            // check the top right corner
            obj = this.getElementAt(ball.getX()+ball.getWidth(), ball.getY());
        }

        if(obj == null){
            // check the top left corner
            obj = this.getElementAt(ball.getX(), ball.getY());
        }

        //check the bottom right corner for collision
        if (obj == null) {
            obj = this.getElementAt(ball.getX() + ball.getWidth(), ball.getY() + ball.getHeight());
        }
        //check the bottom left corner for collision
        if (obj == null) {
            obj = this.getElementAt(ball.getX(), ball.getY() + ball.getHeight());
        }

        // see if we hit something
        if(obj != null){

            // lets see what we hit!
            if(obj instanceof Paddle){

                if(ball.getX() < (paddle.getX() + (paddle.getWidth() * .2))){
                    // did I hit the left side of the paddle?
                    ball.bounceLeft();
                } else if(ball.getX() > (paddle.getX() + (paddle.getWidth() * .8))) {
                    // did I hit the right side of the paddle?
                    ball.bounceRight();
                } else {
                    // did I hit the middle of the paddle?
                    ball.bounce();
                }

            }

            if (obj instanceof Brick){
                // bounce the ball
                ball.bounce();
                // destroy the brick
                 ((Brick) obj).hit(1);

                if(((Brick) obj).getLives()==0){
                    score+=((Brick) obj).getMaxhp();
                    scoreLabel.setLabel("Points = "+score);
                    if (((Brick) obj).powerup==true){
                        handlePowerup();
                    }
                    this.remove(obj);
                }else{

                }



            }

        }

        // if by the end of the method obj is still null, we hit nothing
    }

    private void handleLoss(){
        ball.lost = false;
        lives--;
        if (lives==0){
        pause(500);
        Dialog.showMessage("u loose u suck");
        exit();
        }else{
            lifeLabel.setLabel("lives left = "+String.valueOf(lives));
        }
        reset();
    }

    private void handlePowerup(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                int chance = randy.nextInt(1);
                if (chance==0){
                    double change = paddle.getWidth();
                    paddle.setSize(paddle.getWidth()+change,10);
                    try {
                        Thread.currentThread().sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                    paddle.setSize(paddle.getWidth()-change,10);
                }
                if (chance==1){
                    lives++;
                }
                if (chance==2){
                    Ball aball = new Ball;
                    aball = new Ball(getWidth()/2, 250, 10, aball.getGCanvas());
                    add(aball);
                    aball.handleMove();
                }
            }
        });
        t.start();

    }




    private void reset(){
        ball.setLocation(getWidth()/2, 250);
        paddle.setLocation(230, 430);
        waitForClick();
    }

    public static void main(String[] args) {
        new Breakout().start();
    }

}