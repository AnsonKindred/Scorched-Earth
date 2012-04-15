package scorched.earth;

import android.opengl.GLES20;

public class AimingHUD
{
	
	PowerBar powerBar;
	AnglePicker anglePicker;
	FireButton startButton;
	Drawable divider;
	
	static final float DIVIDER = .49f; // ratio of height
	static final float ANGLE_PICKER_LEFT_OFFSET = .03f; // ratio of height
	
	static AimingHUD instance = null;
	
	// apparently mutable statics are passe...so singleton it is
	public static AimingHUD getInstance()
	{
		if(instance == null) instance = new AimingHUD();
		return instance;
	}
	
	public AimingHUD()
	{
		anglePicker = new AnglePicker();
		powerBar    = new PowerBar();
		divider     = new Drawable();
		startButton = new FireButton();
		
		OpenGLRenderer renderer = OpenGLRenderer.getInstance();
		anglePicker.circle.x = -ANGLE_PICKER_LEFT_OFFSET*renderer.viewHeight;
		anglePicker.line.x = -ANGLE_PICKER_LEFT_OFFSET*renderer.viewHeight;
		
		float[] line = {
				DIVIDER*renderer.viewHeight, -renderer.viewHeight/2, 0,
				DIVIDER*renderer.viewHeight, renderer.viewHeight/2, 0
			};
		divider.baseGeometry = line; 
		divider.init();
	}
	
	public void delete()
	{
		AimingHUD.instance = null;
	}
	
	public void draw()
	{
		anglePicker.draw();
		powerBar.draw();
		divider.draw(GLES20.GL_LINES);
		startButton.draw();
	}
	
	public void touch(float x, float y)
	{
		OpenGLRenderer renderer = OpenGLRenderer.getInstance();
		if(x < -renderer.viewWidth/2+startButton.width && y < -renderer.viewHeight/2+startButton.height) {
			startButton.depress();
		}
		else if(x < DIVIDER*renderer.viewHeight) {
			anglePicker.touch(x, y);
		}
		else {
			powerBar.touch(x, y);
		}
	}
	
	public void click(float x, float y)
	{
		OpenGLRenderer renderer = OpenGLRenderer.getInstance();
		if(x < -renderer.viewWidth/2+startButton.width && y < -renderer.viewHeight/2+startButton.height) {
			startButton.click();
			ScorchedEngine.getInstance().fireShot();
		}
	}

	public float getAngle() 
	{
		return anglePicker.line.getRotation();
	}

	public float getPower() 
	{
		return powerBar.percentFull;
	}
	
}
