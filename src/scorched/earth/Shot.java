package scorched.earth;

import java.util.ArrayList;

import android.opengl.GLES20;


public class Shot 
{

	static final float MAX_START_VELOCITY = .003f; // ratio of screen height
	
	static final float POINT_RADIUS = .02f; // ratio of screen height
	
	ArrayList<Point2D> path;
	int weaponType;
	float shotX, shotY;
	float pointRadius;
	float maxStartVelocity;
	float angle, power;
	Point2D shotVelocity = new Point2D();
	
	Drawable shotPoint = new Drawable();
	
	public Shot()
	{
		this.weaponType = 0;
	}
	
	public Shot(Player player, int weaponType, float angle, float power)
	{
		OpenGLRenderer renderer = OpenGLRenderer.getInstance();
		this.weaponType = weaponType;
		this.angle = angle;
		this.power = power;
		maxStartVelocity = MAX_START_VELOCITY * renderer.viewHeight;
		
		// starting point of shot, set to the top of the tank
		shotX = player.tank.x+player.tank.width/2;
		shotY = player.tank.y+player.tank.height;
		
		// Get a normalized velocity vector from the turret angle
		int flip = player.tank.turret.getRotation() < 0 ? 1 : -1;
		shotVelocity.x = flip;
		shotVelocity.y = flip*player.tank.turret.getSlope();
		VectorUtil.normalize2D(shotVelocity);
		
		// Adjust the starting position so that it is at the end of the turret
		shotX += shotVelocity.x*player.tank.turretLength;
		shotY += shotVelocity.y*player.tank.turretLength;
		
		// Magnify the velocity vector by power
		VectorUtil.mult2D(shotVelocity, power*maxStartVelocity);

		pointRadius = POINT_RADIUS * renderer.viewHeight;
		float[] pointGeometry = {-pointRadius/2, -pointRadius/2, 0,
								-pointRadius/2, pointRadius/2, 0,
								pointRadius/2, -pointRadius/2, 0,
								pointRadius/2, pointRadius/2, 0};
		
		shotPoint.baseGeometry = pointGeometry;
		shotPoint.x = shotX;
		shotPoint.y = shotY;
		shotPoint.init();
	}
	
	public void draw()
	{
		OpenGLRenderer renderer = OpenGLRenderer.getInstance();
		ScorchedEngine engine = ScorchedEngine.getInstance();

		shotPoint.draw(GLES20.GL_TRIANGLE_STRIP);
		
		long dTime = renderer.timeSinceLastDraw;
		
		shotX += shotVelocity.x*dTime;
		shotY += shotVelocity.y*dTime;
		shotVelocity.x += engine.gravity.x*dTime;
		shotVelocity.y += engine.gravity.y*dTime;
		
		shotPoint.x = shotX;
		shotPoint.y = shotY;
	}
	
}
