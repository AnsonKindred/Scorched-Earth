package scorched.earth;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

public class OpenGLRenderer implements GLSurfaceView.Renderer 
{
	private int shaderProgram;
	public int shaderPosition;
	private int shaderMVP;
	private int shaderColor;
	private int shaderTextureCoord;
	private int shaderHasTexture;
	private int shaderTextureID;
	
	public float viewWidth, viewHeight;
	public float screenWidth, screenHeight;
	
	private static final String vertexShaderCode = 
			"attribute vec4 vPosition; \n" +
			"uniform mat4 uMVPMatrix;   \n" +
			"attribute vec4 colorMatrix; \n" +
			"attribute vec2 textureCoord; \n" +
			"varying vec4 color; \n" +
			"varying vec2 texture_coord; \n" +
	        "void main(){              \n" +
	        " gl_Position = uMVPMatrix * vPosition; \n" +
	        " color = colorMatrix; \n" +
			" texture_coord = textureCoord; \n" +
	        "}                         \n";
	    
	private static final String fragmentShaderCode = 
			"precision mediump float;  \n" +
			"uniform sampler2D textureID; \n" +
	        "uniform float hasTexture; \n" +
	        "varying vec2 texture_coord; \n" +
			"varying vec4 color; \n" +
	        "void main(){              \n" +
			" if(hasTexture >= 1.0) { \n" +
			//"   gl_FragColor = vec4(0, 0, 0, 0); \n" +
	        "   gl_FragColor = texture2D(textureID, texture_coord); \n" +
			" } \n" +
	        " else { \n" +
			"   gl_FragColor = color; \n" +
	        " } \n " +
	        "}                         \n";
    
    private boolean didInit = false;
    
    private static OpenGLRenderer instance = null;
    
    ScorchedEarthActivity activity;
	public long timeSinceLastDraw;
	private long lastDrawTime;
    
    public static OpenGLRenderer getInstance(ScorchedEarthActivity activity)
    {
    	if(instance == null) instance = new OpenGLRenderer(activity);
    	return instance;
    }
    
    public static OpenGLRenderer getInstance()
    {
    	return instance;
    }
    
    public OpenGLRenderer(ScorchedEarthActivity activity)
    {
    	this.activity = activity;
    }

	public void onSurfaceCreated(GL10 unused, EGLConfig config) 
	{
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		
		GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GL10.GL_BLEND);
		
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        
        shaderProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(shaderProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(shaderProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(shaderProgram);                  // creates OpenGL program executables
        
        // grab handles for later
        shaderPosition = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        shaderMVP = GLES20.glGetUniformLocation(shaderProgram, "uMVPMatrix");
        shaderColor = GLES20.glGetAttribLocation(shaderProgram, "colorMatrix");
        shaderTextureCoord = GLES20.glGetAttribLocation(shaderProgram, "textureCoord");
    	shaderHasTexture = GLES20.glGetUniformLocation(shaderProgram, "hasTexture");
    	shaderTextureID = GLES20.glGetUniformLocation(shaderProgram, "textureID");
	}
	
	public void delete()
	{
	}

	public void onDrawFrame(GL10 unused) 
	{
		timeSinceLastDraw = SystemClock.uptimeMillis()-lastDrawTime;
        GLES20.glUseProgram(shaderProgram);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		// Add program to OpenGL environment
        
        GLMatrix.mvIdentity();

		ScorchedEngine.getInstance().draw();
		lastDrawTime = SystemClock.uptimeMillis();
	}
	
	public void onSurfaceChanged(GL10 unused, int width, int height) 
	{
	    GLES20.glViewport(0, 0, width, height);
	    float ratio = (float) width / height;
	    
	    screenWidth = width;
	    screenHeight = height;
        viewHeight = 100;
        viewWidth = ratio*viewHeight;
	    
        GLMatrix.ortho(0, -viewWidth/2, viewWidth/2, -viewHeight/2, viewHeight/2, -1, 1);
	    if(!didInit) ScorchedEngine.getInstance().init();
	}
	
	private int loadShader(int type, String shaderCode)
	{		    
		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type); 
	
		// add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);
	    
	    return shader;
	}
	
	public void render(int draw_mode, Drawable thing)
	{
	    GLES20.glVertexAttribPointer(shaderPosition, 3, GLES20.GL_FLOAT, false, 0, thing.geometryBuffer);
        GLES20.glEnableVertexAttribArray(shaderPosition);
	    GLES20.glVertexAttribPointer(shaderColor, 4, GLES20.GL_FLOAT, false, 0, thing.colorBuffer);
        GLES20.glEnableVertexAttribArray(shaderColor);
        if(thing.texture_id != 0) bindTexture(thing);
	    
	    // Draw the triangle
		GLMatrix.applyTransforms(shaderMVP);
	    GLES20.glDrawArrays(draw_mode, 0, thing.baseGeometry.length/3);
	    GLES20.glUniform1f(shaderHasTexture, 0);
	}
	
	public void screenToViewCoords(float[] xy)
	{
		float viewX = (xy[0]/screenWidth)*viewWidth-viewWidth/2;
		float viewY = -(xy[1]/screenHeight)*viewHeight+viewHeight/2;
		xy[0] = viewX;
		xy[1] = viewY;
	}
	
	public void bindTexture(Drawable thing)
	{
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, thing.texture_id);
		// pretty sure setting shaderTextureID to 0 gets it to use the bound texture..maybe
		GLES20.glUniform1i(shaderTextureID, 0);
		GLES20.glUniform1f(shaderHasTexture, 1);
		GLES20.glVertexAttribPointer(shaderTextureCoord, 2, GLES20.GL_FLOAT, false, 0, thing.textureBuffer);
        GLES20.glEnableVertexAttribArray(shaderTextureCoord);
	}
}