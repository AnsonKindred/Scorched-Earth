package scorched.earth;

import java.io.IOException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class ScorchedEarthActivity extends Activity 
{
	private ScorchedEarthSurfaceView view;
	public ScorchedEngine gameEngine;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);


		try {
			SocketUtil.connectToIP("68.205.215.105");
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally { SocketUtil.closeConnection(); }
		
		// Create the GLSurfaceView.
		view = new ScorchedEarthSurfaceView(this);
		gameEngine = ScorchedEngine.getInstance();
		
		// Set the GLSurfaceView to be shown.
		setContentView(view);
	}
	
	@Override
    protected void onPause() 
	{
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        view.onPause();
        //gameEngine.onPause();
    }
    
    @Override
    protected void onResume() 
    {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        view.onResume();
        //gameEngine.onResume();
    }
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	view.delete();
    	gameEngine.delete();
    }
}

class ScorchedEarthSurfaceView extends GLSurfaceView
{
	public OpenGLRenderer renderer;
	private static final int CLICK_THRESHOLD = 10;
	
	//private float lastX, lastY;
	private float clickStartX, clickStartY;
	
    public ScorchedEarthSurfaceView(ScorchedEarthActivity activity)
    {
        super(activity);
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        renderer = OpenGLRenderer.getInstance(activity);
        this.setRenderer(renderer);
    }
    
    public void delete()
    {
    	renderer.delete();
    }
    
    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float x = e.getX();
        float y = e.getY();
        float x2 = 0;
        float y2 = 0;
        if(e.getPointerCount() > 1) {
        	x2 = e.getX(1);
            y2 = e.getY(1);
        }
        ScorchedEngine gameEngine = ((ScorchedEarthActivity)this.getContext()).gameEngine;
        
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                gameEngine.touch(x, y);
                if(e.getPointerCount() > 1) {
                	gameEngine.touch(x2, y2);
                }
                break;
            case MotionEvent.ACTION_DOWN:
            	clickStartX = x;
            	clickStartY = y;
            	break;
            case MotionEvent.ACTION_UP:
            	if(Math.abs(clickStartX - x) < CLICK_THRESHOLD && Math.abs(clickStartY - y) < CLICK_THRESHOLD)
            	{
            		gameEngine.click(x, y);
            	}
            	break;
        }
        
        return true;
    }
}