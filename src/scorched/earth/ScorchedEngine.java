package scorched.earth;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class ScorchedEngine
{

	static final int NUM_PLAYERS = 2;
	static final int STATE_CHOOSE_ANGLE = 0;
	static final int STATE_FIRING_SHOT = 1;
	
	public Player players[] = new Player[NUM_PLAYERS];
	int gameState = STATE_CHOOSE_ANGLE;
	public int currentPlayer = 0;
	AimingHUD angleHUD;
	ArrayList<Shot> shots = new ArrayList<Shot>();
	public Point2D gravity = new Point2D(0, -.0003f);
	
	static ScorchedEngine instance = null;
	
	public static ScorchedEngine getInstance()
	{
		if(instance == null) instance = new ScorchedEngine();
		return instance;
	}

	public ScorchedEngine()
	{
	}
	
	// Called when the gl context is first made available
	public void init()
	{
		Terrain.init();
		angleHUD = AimingHUD.getInstance();
		
		for(int p = 0; p < NUM_PLAYERS; p++) players[p] = new Player();
	}
	
	public void delete()
	{
		angleHUD.delete();
		ScorchedEngine.instance = null;
	}
	
	public void draw()
	{
		Terrain.draw();
		
		for(int p = 0; p < NUM_PLAYERS; p++) players[p].draw();
		
		if(gameState == STATE_CHOOSE_ANGLE) {
			angleHUD.draw();
		}
		OpenGLRenderer renderer = OpenGLRenderer.getInstance();
		for(int i = 0; i < shots.size(); i++) {
			Shot shot = shots.get(i);
			shot.draw();
			if(shot.shotX > renderer.viewWidth/2 || 
					shot.shotX < -renderer.viewWidth/2 ||
					shot.shotY > renderer.viewHeight/2 ||
					shot.shotY < -renderer.viewHeight/2)
			{
				gameState = STATE_CHOOSE_ANGLE;
				shots.remove(i);
				i--;
			}
		}			
	}
	
	public void touch(float x, float y)
	{
		float real_xy[] = {x, y};
		OpenGLRenderer.getInstance().screenToViewCoords(real_xy);
		if(gameState == STATE_CHOOSE_ANGLE) {
			angleHUD.touch(real_xy[0], real_xy[1]);
		}
	}
	
	public void click(float x, float y)
	{
		float real_xy[] = {x, y};
		OpenGLRenderer.getInstance().screenToViewCoords(real_xy);
		if(gameState == STATE_CHOOSE_ANGLE) {
			angleHUD.click(real_xy[0], real_xy[1]);
		}
	}
	
	public void setCurrentTankAngle(float rotation)
	{
		players[currentPlayer].setAngle(rotation);
	}

	public void fireShot() 
	{
		gameState = STATE_FIRING_SHOT;
		Shot shot = new Shot(players[currentPlayer], 0, angleHUD.getAngle(), angleHUD.getPower());
		players[currentPlayer].fireShot(shot);
		shots.add(shot);
	}
}
