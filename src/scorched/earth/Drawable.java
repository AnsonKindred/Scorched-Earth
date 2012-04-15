package scorched.earth;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


import android.opengl.GLES20;

public class Drawable 
{
	float x, y;
	// 0-1
	private float rotation = 0;
	private float slope = 0;
	public float width = 0;
	float height = 0;
	
	public FloatBuffer geometryBuffer;
	public float[] baseGeometry = null;
	public int texture_id = 0;
	
	float[] color = {.3f, .4f, .5f, 1};
	float[] vertexColors = null;
	public FloatBuffer colorBuffer;
	
	float[] textureCoords = null;
	public FloatBuffer textureBuffer;
	
	public void init()
	{
		if(baseGeometry == null) return;
        vertexColors = new float[baseGeometry.length/3 * 4];
        for(int c = 0; c < baseGeometry.length/3; c++)
        {
        	int i = c * 4;
        	vertexColors[i] = color[0];
        	vertexColors[i+1] = color[1];
        	vertexColors[i+2] = color[2];
        	vertexColors[i+3] = color[3];
        }
        
        textureCoords = new float[baseGeometry.length/3 * 2];
        float factor = height;
        if(width > height) factor = width;
        for(int c = 0; c < baseGeometry.length/3; c++)
        {
        	textureCoords[c*2] = baseGeometry[c*3]/factor;
        	textureCoords[c*2+1] = 1-baseGeometry[c*3+1]/factor;
        }
        
        // initialize color Buffer  
        ByteBuffer vbb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                vertexColors.length * 4
            ).order(ByteOrder.nativeOrder());
        colorBuffer = vbb.asFloatBuffer();
        colorBuffer.put(vertexColors);    // add the coordinates to the FloatBuffer
        colorBuffer.position(0);          // set the buffer to read the first coordinate
        
        // initialize vertex Buffer  
        vbb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                baseGeometry.length * 4
            ).order(ByteOrder.nativeOrder());
        geometryBuffer = vbb.asFloatBuffer();
        geometryBuffer.put(baseGeometry);    // add the coordinates to the FloatBuffer
        geometryBuffer.position(0);          // set the buffer to read the first coordinate
        
        // initialize texture Buffer  
        vbb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                textureCoords.length * 4
            ).order(ByteOrder.nativeOrder());
        textureBuffer = vbb.asFloatBuffer();
        textureBuffer.put(textureCoords);    // add the coordinates to the FloatBuffer
        textureBuffer.position(0);          // set the buffer to read the first coordinate
	}
	
	public void draw()
	{
		this.draw(GLES20.GL_TRIANGLES);
	}
	
	public void draw(int draw_mode)
	{
		if(baseGeometry == null) return;
		
		OpenGLRenderer renderer = OpenGLRenderer.getInstance();
		
		GLMatrix.pushMatrix();
		GLMatrix.translate(x, y, 0);
		if(rotation != 0) {
			GLMatrix.rotate(this.rotation, 0, 0, 1);
		}

		renderer.render(draw_mode, this);
		GLMatrix.popMatrix();
	}
	
	public float getRotation()
	{
		return rotation;
	}
	
	public void setRotation(float x, float y)
	{
		float theta = (float) Math.atan(y/x);
		rotation = (float) Math.toDegrees(theta)-90;
		slope = y/x;
		if(x < 0) rotation += 180;
	}
	
	public void setRotation(float deg)
	{
		rotation = deg;
		slope = (float) Math.tan(Math.toRadians(deg+90));
	}

	public float getSlope()
	{
		return slope;
	}
}
