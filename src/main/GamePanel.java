package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import manager.MouseManager;
import manager.KeyManager;
import manager.StateManager;


/**
 * GamePanel class is the panel (screen) that will be drawn on
 * @author Vachia Thoj
 *
 */
public class GamePanel extends JPanel implements Runnable, KeyListener
{
	private static final long serialVersionUID = 1L;
	
	//Width and Height of gamePanel
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	//Thread to run the game
	private Thread thread;
	private boolean running;
	
	//To render graphics
	private BufferedImage image;
	private Graphics2D g;
	
	//Game framerate
	private static final int FPS = 120;
	private static final int TARGET_TIME = 1000 / FPS;
	
	//Manage different states
	private StateManager stateManager;
	
	//Manage mouse events
	private MouseManager mouseManager;
	
	//Manager keyboard events
	private KeyManager keyManager;
	
	/**
	 * Constructor
	 */
	public GamePanel()
	{
		super();
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setFocusable(true);
		this.requestFocus();
		
		stateManager = StateManager.instance();
		mouseManager = MouseManager.instance();
		keyManager = KeyManager.instance();
	}
	
	public void addNotify()
	{
		super.addNotify();
		
		if(thread == null)
		{
			//Add a MouseListener
        	addMouseListener(new MouseAdapter(){
        		public void mousePressed(MouseEvent e)
        		{
        			mouseManager.setPressedPoint(e.getX(), e.getY());
        			mouseManager.setMousePressed(true);
        			mouseManager.setMouseReleased(false);
        		}
        		
        		public void mouseReleased(MouseEvent e)
        		{
        			mouseManager.setReleasedPoint(e.getX(), e.getY());
        			mouseManager.setMouseReleased(true);
        			mouseManager.setMousePressed(false);
        		}
        	});
        	
        	//Add a MouseMotionListener
        	addMouseMotionListener(new MouseAdapter() {
        		public void mouseMoved(MouseEvent e)
        		{
        			mouseManager.setCurrentPoint(e.getX(), e.getY());
        		}
        	});
        	
        	//Add KeyListener
        	addKeyListener(this);
			
			//Create thread and start thread
			thread = new Thread(this);
			thread.start();
		}
	}
	
	/**
	 * Run method
	 */
	public void run()
	{
		running = true;
        
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        
        //Add anti-aliasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        long start;
        long elapsed;
        long wait;
        
        //Running thread
        while(running == true)
        {
            start = System.nanoTime();
            
            update();
            draw();
            drawToScreen();
            
            //******* (START) Frame counting *******
            
            elapsed = System.nanoTime() - start;
            wait = (TARGET_TIME - elapsed) / 1000000;
            if(wait < 0)
            {
            	wait = TARGET_TIME;
            }
            
            
            try{
            	Thread.sleep(wait);
            }catch(Exception e){
                
            }
            //******* (END) Frame counting *******
        }
	}
	
	/**
	 * Method that updates GamePanel
	 */
	private void update()
	{
		stateManager.update();
	}
	
	/**
	 * Method that draws on GamePanel
	 */
	private void draw()
	{
		stateManager.draw(g);
	}
	
	/**
	 * Method for double buffering
	 */
	private void drawToScreen()
	{
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0,  null);
		g2.dispose();
	}
	
	public void keyPressed(KeyEvent k) {keyManager.setKey(k.getKeyCode(), true);}
	public void keyReleased(KeyEvent k) {keyManager.setKey(k.getKeyCode(), false);}
	public void keyTyped(KeyEvent k) {}
}