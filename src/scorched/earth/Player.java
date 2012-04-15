package scorched.earth;

import java.io.IOException;


public class Player
{
	
	Tank tank;
	float angle, power;

	public Player()
	{
		tank = new Tank();
	}
	
	public void draw()
	{
		tank.draw();
	}

	public void fireShot(Shot shot)
	{
		this.angle = shot.angle;
		this.power = shot.power;
		tank.turret.setRotation(angle);
		String msg = "{" +
					"x:"+shot.shotX+","+
					"y:"+shot.shotY+","+
					"vx:"+shot.shotVelocity.x+","+
					"vy:"+shot.shotVelocity.y+
				"}";
		try {
			SocketUtil.sendMessage(msg);
		} catch (IOException e) {}
	}
	
	public void setAngle(float angle)
	{
		this.angle = angle;
		tank.turret.setRotation(angle);
	}

	
}
