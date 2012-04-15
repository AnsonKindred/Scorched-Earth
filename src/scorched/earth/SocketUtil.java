package scorched.earth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.os.AsyncTask;

public class SocketUtil 
{

	static Socket socket = null;
	static DataOutputStream dataOutputStream = null;
	static DataInputStream dataInputStream = null;
	
	static void connectToIP(String ip) throws IOException
	{
		new OpenConnectionTask().execute(ip);
	}
	
	public static void closeConnection()
	{
		new CloseConnectionTask().execute();
	}
	
	public static void sendMessage(String msg) throws IOException
	{
		new SendMessageTask().execute(msg);
	}

}

class OpenConnectionTask extends AsyncTask<String, Void, String> 
{
	private Exception exception;

    @Override
    protected String doInBackground(String... ips) 
    {
        try {
        	SocketUtil.socket = new Socket(ips[0], 8888);
        	SocketUtil.dataOutputStream = new DataOutputStream(SocketUtil.socket.getOutputStream());
        	SocketUtil.dataInputStream = new DataInputStream(SocketUtil.socket.getInputStream());
            return null;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(String obj) 
    {
        // TODO: check this.exception 
        // TODO: do something with the msg
    }
}

class CloseConnectionTask extends AsyncTask 
{
	private Exception exception;

    @Override
    protected Object doInBackground(Object... not_used) 
	{
		if (SocketUtil.socket != null) {
			try {
				SocketUtil.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (SocketUtil.dataOutputStream != null) {
			try {
				SocketUtil.dataOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (SocketUtil.dataInputStream != null) {
			try {
				SocketUtil.dataInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
    }
    
    protected void onPostExecute(String obj) 
    {
        // TODO: check this.exception 
        // TODO: do something with the msg
    }
}

class SendMessageTask extends AsyncTask<String, Void, String> 
{
	private Exception exception;

    @Override
    protected String doInBackground(String... msgs) 
    {
        try {
        	SocketUtil.dataOutputStream.writeUTF(msgs[0]);
            return null;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(String obj) 
    {
        // TODO: check this.exception 
        // TODO: do something with the msg
    }
}
