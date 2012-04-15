package scorched.earth;

import android.opengl.GLES20;

public class Tank extends Drawable
{
	static final float TURRENT_LENGTH = .045f; // ratio of screen height
	static final float WIDTH = .1f; // ratio of screen height
	static final float HEIGHT = .07f; // ratio of screen height

	float baseGeometry[];
	
	public Drawable turret = new Drawable();
	float turretLength = 5;
	
	public Tank()
	{
		OpenGLRenderer renderer = OpenGLRenderer.getInstance();
		x = (float) (Math.random()*renderer.viewWidth)-renderer.viewWidth/2;
		y = (float) (Math.random()*renderer.viewHeight)-renderer.viewHeight/2;
		width = WIDTH*renderer.viewHeight;
		height = HEIGHT*renderer.viewHeight;
		float[] baseGeometry = {
		            // X, Y
					0, 0, 0,
					width, 0f,0,
					width/2, height,0
		        };
		super.baseGeometry = baseGeometry;
		init();
		
		turretLength = TURRENT_LENGTH*renderer.viewHeight;
		float turretGeometry[] = {
				0, 0, 0,
				0, turretLength, 0
			};
		turret.baseGeometry = turretGeometry;
		turret.x = x+width/2;
		turret.y = y+height;
		turret.init();
	}
	
	public void draw()
	{
		super.draw();
		turret.draw(GLES20.GL_LINES);
	}
	
}
