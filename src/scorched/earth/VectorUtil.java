package scorched.earth;

class VectorUtil
{

    public static void normalize2D(Point2D p)
    {
        float total = mag2D(p);
		if(total != 0)
		{
			p.x /= total;
			p.y /= total;
		}
    }
    
    public static float mag2D(Point2D p)
	{
		return (float) Math.sqrt(Math.pow(p.x, 2)+Math.pow(p.y, 2));
	}

	public static void scaleTo2D(Point2D p, float mag)
	{
		if(mag != 0)
		{
			float total = mag2D(p);
			if(total != 0)
			{
				total *= mag;
				p.x /= total;
				p.y /= total;
			}
		}
	}

	static void mult2D(Point2D p, float vm)
	{
		p.x *= vm;
		p.y *= vm;
	}
	
	/*static void translate2D(float[] out, float[] in, float x, float y)
	{
		for(int i = 0; i < in.length; i+=2)
		{
			out[i] = in[i] + x;
			out[i+1] = in[i+1] + y;
		}
	}
	
	static void translate2D(double[] out, double[] in, double x, double y)
	{
		for(int i = 0; i < in.length; i+=2)
		{
			out[i] = in[i] + x;
			out[i+1] = in[i+1] + y;
		}
	}
	
	// Theta is from 0-1. Deal with it.
	static void rotate2D(float[] out, float[] in, double theta)
	{
		double real_theta = Math.toRadians(theta);
		for(int i = 0; i < in.length; i+=2)
		{
			out[i] = (float) (in[i]*Math.cos(real_theta) - in[i+1]*Math.sin(real_theta));
			out[i+1] = (float) (in[i]*Math.sin(real_theta) + in[i+1]*Math.cos(real_theta));
		}
	}
	
	// Theta is from 0-1. Deal with it.
	static void rotate2D(double[] out, double[] in, double theta)
	{
		double real_theta = theta*2.0*Math.PI;
		for(int i = 0; i < in.length; i+=2)
		{
			out[i] = in[i]*Math.cos(real_theta) - in[i+1]*Math.sin(real_theta);
			out[i+1] = in[i]*Math.sin(real_theta) + in[i+1]*Math.cos(real_theta);
		}
	}*/

}
