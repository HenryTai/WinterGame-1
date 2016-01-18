
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Main extends Canvas implements Runnable{

	public static final long serialVersionUID = 1L;
	
	//Size of the Window
	public static final int WIDTH = 450, HEIGHT = WIDTH * 12 / 9;
	
	public GameState state = GameState.Menu;
	
	private Thread thread;
	
	private boolean isRunning = false;
	
	private GameObjectHandler handler;
	private Menu menu;
	private HUD hud;
	
	public Main(){
		handler = new GameObjectHandler();
		menu = new Menu(this, handler);
		
		this.addKeyListener(new Controller(handler));
		this.addMouseListener(menu);
		
		new Window(WIDTH, HEIGHT, "Skip the ball!", this);
		
		hud = new HUD();

		// create object 
		handler.addObject(new Player(Main.WIDTH/2, Main.HEIGHT - 100, GameObjectID.Player, handler));
		handler.addObject(new Enemy(Main.WIDTH/2, 0, GameObjectID.Player));
	}
	
	//Starting the content of game inside the window
	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		isRunning = true;
	}
	
	//Stop the content of the game inside the window
	public synchronized void stop(){
		try{
			thread.join();
			isRunning = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//Game loop that a lot people use it to maintain the animation of the game
	@Override
	public void run() {
		this.requestFocus();
		long lastLoopTime = System.nanoTime();
		final double targetFPS = 60.0;
		final double optimalTime = 1000000000 / targetFPS;
		double delta = 0;
		
		//used to track FPS, no effect to the functionality of game loop
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while(isRunning){
			long now = System.nanoTime();
			delta += (now - lastLoopTime) / optimalTime;
			lastLoopTime = now;
			
			//update the content/logic of the game
			while(delta >= 1){
				updateGameLogic();
				delta--;
			}
			
			//draw everything of the game at that time
			if(isRunning)	updateGameGraphic();
			
			frames++;
			
			//Used to keep track with FPS, and display in console
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		
		stop();
		
	}
	
	public void updateGameLogic(){
		if(HUD.HEALTH <= 0){
			System.out.println("GAME OVER");
			// later will be switch to a screen for Game Over 
			System.exit(1);
		}

		if(state == GameState.Game){
			handler.updateGameObjectsLogic();
			hud.updateGameObjectsLogic();
		}else if(state == GameState.Menu){
			
		}
		
	}
	
	//draw everything function
	public void updateGameGraphic(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		//screen color
		g.setColor(Color.black); 
		g.fillRect(0, 0, WIDTH,HEIGHT);
		
		
		if(state == GameState.Game){
			handler.updateGameObjectsGraphic(g);
			hud.updateGameObjectsGraphic(g);
		}else{
			menu.updateMenuGraphic(g);
		}
		
		g.dispose();
		bs.show();
	}
	
	//Help functions:
	
	//constrain a variable within a boundaries
	public static int constrain(int var, int min, int max){
		if(var <= min) return var = min;
		else if(var >= max) return var = max;
		else return var;
	}
	
	//check if the mouse is clicked in desired region
	public static boolean isMouseOver(int mx, int my, int x, int y, int width, int height){
		if((mx > x && mx < x + width)
			&&(my > y && my < y + height)){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
}