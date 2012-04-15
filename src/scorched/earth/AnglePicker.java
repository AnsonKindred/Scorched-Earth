package scorched.earth;

import android.opengl.GLES20;

public class AnglePicker 
{
	Drawable circle;
	Drawable line;
	
	static final float LINE_LENGTH = 1.15f; // ratio of radius
	static final float RADIUS = .4f; // ratio of height
	static final int CIRCLE_RES = 50;

	private float radius;
	private float lineLength; // the actual line length calculated from radius*LINE_LENGTH
	
	public AnglePicker()
	{
		circle = new Drawable();
		line = new Drawable();
		radius = OpenGLRenderer.getInstance().viewHeight*RADIUS;
		lineLength = LINE_LENGTH*radius;
		
		float[] circle_geometry = new float[CIRCLE_RES*3];
		for(int i=0; i < CIRCLE_RES*3; i+=3) {
			int c_i = i/3;
			circle_geometry[i] = ((float) Math.sin(((1.f*c_i)/CIRCLE_RES)*2*Math.PI))*radius;
			circle_geometry[i+1] = (float) Math.cos(((1.f*c_i)/CIRCLE_RES)*2*Math.PI)*radius;
			circle_geometry[i+2] = 0;
		}
		
		circle.baseGeometry = circle_geometry;
		circle.init();

		float[] line_geometry = {0, 0, 0, 0, lineLength, 0};
		line.baseGeometry = line_geometry;
		line.init();
	}
	
	public void draw()
	{
		circle.draw(GLES20.GL_LINE_LOOP);
		line.draw(GLES20.GL_LINES);
	}
	
	public void touch(float x, float y)
	{
		line.setRotation(x, y);
		ScorchedEngine.getInstance().setCurrentTankAngle(line.getRotation());
	}
}
