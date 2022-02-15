import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import java.awt.Color;
import java.util.Random;

public class Brick extends GCompound {

    public static final int WIDTH = 44;
    public static final int HEIGHT = 20;
    public int hitp;
    public int maxhpp;
    public boolean powerup;

    public Random randy = new Random();

    public GLabel hpl = new GLabel("");



    public Brick(double x, double y, Color color, int row, int maxhp){
        GRect bricc = new GRect(WIDTH,HEIGHT);
        bricc.setFillColor(color);
        bricc.setFilled(true);

        add(bricc,x,y);
        hitp = maxhp;
        maxhpp = maxhp;

        hpl.setLabel("" + hitp);
        hpl.setColor(Color.white);
        add(hpl,x+hpl.getWidth()/2,y+hpl.getHeight());
        if (randy.nextInt(4)==0){
            bricc.setFillColor(Color.green);
            powerup = true;
        }

    }


    public int getLives(){
        return this.hitp;
    }

    public void hit(int dmg){
        hitp=hitp-1;
        hpl.setLabel("" + hitp);
    }

    public int getMaxhp(){
        return maxhpp;
    }







}