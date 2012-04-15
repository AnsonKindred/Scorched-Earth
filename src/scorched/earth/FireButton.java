package scorched.earth;

import android.opengl.GLES20;

public class FireButton extends Drawable 
{
	static final float WIDTH = 38;
	static final float HEIGHT = 21;
	
	private Drawable text;

	float baseGeometry[] = {
			0, 0, 0,
			0, HEIGHT, 0,
			WIDTH, 0, 0,
			WIDTH, HEIGHT, 0
        };
	
	public FireButton()
	{
		OpenGLRenderer renderer = OpenGLRenderer.getInstance();
		super.baseGeometry = this.baseGeometry;
		init();
		
		text = new Drawable();
		text.baseGeometry = this.baseGeometry;
		text.texture_id = TextUtil.generateTextTexture("Fire", 42, 29, 19);
		text.width = this.width = WIDTH;
		text.height = this.height = HEIGHT;
		text.x = this.x = -renderer.viewWidth/2;
		text.y = this.y = -renderer.viewHeight/2;
		text.init();
	}
	
	public void draw()
	{
		super.draw(GLES20.GL_TRIANGLE_STRIP);
		text.draw(GLES20.GL_TRIANGLE_STRIP);
	}

	public void click() 
	{
		
	}

	public void depress() 
	{
		
	}
}
