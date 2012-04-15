package scorched.earth;

import java.util.ArrayList;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class GLMatrix 
{
    private static float[] mvpMatrix = new float[16];
    private static float[] mvMatrix = new float[16];
    private static float[] projMatrix = new float[16];
    
    private static ArrayList<Float[]> matrixStack = new ArrayList<Float[]>();
    
    public static void mvIdentity()
    {
    	Matrix.setIdentityM(mvMatrix, 0);
    }
	
    public static void ortho(int something, float left, float right, float bottom, float top, float near, float far)
    {
    	Matrix.orthoM(projMatrix, something, left, right, bottom, top, near, far);
    }
    
    public static void applyTransforms(int shaderProperty)
    {
    	Matrix.multiplyMM(mvpMatrix, 0, projMatrix, 0, mvMatrix, 0);
        GLES20.glUniformMatrix4fv(shaderProperty, 1, false, mvpMatrix, 0);
    }
    
    public static void translate(float x, float y, float z)
	{
		Matrix.translateM(mvMatrix, 0, x, y, z);
	}
	
	public static void rotate(float deg, float x, float y, float z)
	{
		Matrix.rotateM(mvMatrix, 0, deg, x, y, z);
	}
	
	public static void pushMatrix()
	{
		matrixStack.add(Util.boxFloats(mvMatrix));
	}
	
	public static void popMatrix()
	{
		Float[] bla = matrixStack.remove(matrixStack.size()-1);
		mvMatrix = Util.unboxFloats(bla);
	}
}
