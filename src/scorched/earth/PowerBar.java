package scorched.earth;

import android.opengl.GLES20;

public class PowerBar 
{
	static final float INNER_PADDING = .01f; // ratio of screen height
	static final float RIGHT_PADDING = .090f; // ratio of screen height
	static final float HEIGHT = .8f; // ratio of screen height
	static final float WIDTH = .15f; // ratio of screen height
	
	Drawable outer;
	Drawable inner;
	
	float percentFull;
	float padding;
	
	public PowerBar()
	{
		outer = new Drawable();
		inner = new Drawable();
		OpenGLRenderer renderer = OpenGLRenderer.getInstance();

		percentFull = .5f;
		
		outer.height  = renderer.viewHeight*HEIGHT;
		outer.width   = renderer.viewHeight*WIDTH;
		padding = renderer.viewHeight*INNER_PADDING;
		inner.height  = outer.height - padding;
		inner.width   = outer.width - padding;
		
		float[] outer_geometry = {
				0, 0, 0,
				outer.width, 0, 0,
				outer.width, outer.height, 0,
				0, outer.height, 0
			};
		
		outer.baseGeometry = outer_geometry;
		outer.init();
		_compileInnerBar();
		
		outer.x = renderer.viewWidth/2-renderer.viewHeight*RIGHT_PADDING-outer.width;
		outer.y = -outer.height/2f;
		inner.x = renderer.viewWidth/2-renderer.viewHeight*RIGHT_PADDING-inner.width-padding/2;
		inner.y = -inner.height/2f;
	}
	
	public void draw()
	{
		outer.draw(GLES20.GL_LINE_LOOP);
		inner.draw(GLES20.GL_TRIANGLE_STRIP);
	}
	
	private void _compileInnerBar()
	{
		float[] inner_geometry = {
				0, 0, 0,
				0, inner.height*percentFull, 0,
				inner.width, 0, 0,
				inner.width, inner.height*percentFull, 0
			};
		
		inner.baseGeometry = inner_geometry;
		inner.init();
	}
	
	public void touch(float x, float y)
	{
		// distance from base of power bar
		percentFull = ((y + inner.height/2) / inner.height);
		percentFull = Math.max(Math.min(percentFull, 1), 0);
		_compileInnerBar();
	}
}
