package kr.co.aura.mtelo.healthcare.network;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import kr.co.aura.mtelo.healthcare.Define;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.MLog;
import android.content.Context;
import android.util.Log;

public class NetWork
{
	
	private Call_Back mCall_Back;
	
	private LTimeOut_Kepper mKepper;
	
	
	
	public interface Call_Back
	{
		void onError(String error);
		void onGetResponsData(byte[] data);
		void onGetResponsString(String data);
		void onRoaming(String message);
		//void onPercent(int nPercent);
	}
	
	
	
	public void set_Call_Back(Call_Back obj)
	{
		MLog.write(this, "set_Call_Back("+obj+")");
		mCall_Back = obj;
	}
	
	
	
	private String mPacketUrl; 
	
	
	private Context mContext;



	private HttpURLConnection mHttpcon;

	private InputStream mInputstream;
	
	
	
	public NetWork(Context context)
	{
		mContext = context;
	}
	
	
	
	public void make_Request_Url(String packeturl)
	{
		MLog.write(this, "make_Request_Packet("+packeturl+")");
		
		mPacketUrl = "";
		mPacketUrl = packeturl;
	}

	
	private String mNetworkErrorMsg;
	public void start_Request(int nTimeOut, String name, String errmsg)
	{
		mNetworkErrorMsg = errmsg;
		
			
		MLog.write(this, "start_Request()");
		new Thread(new request_Data_Runnable()).start();
		
		if ( mKepper == null )
			mKepper = new LTimeOut_Kepper();
		
		mKepper.Start_Kepper(nTimeOut, new LTimeOut_Kepper.Kepper_CallBack()
		{
			@Override
			public void onTimeOut()
			{
				MLog.write(this, "onTimeOut()");
				
				if (mHttpcon != null)
					mHttpcon.disconnect();
				if (mInputstream != null)
				{
					try
					{
						mInputstream.close();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	
	
	private class request_Data_Runnable implements Runnable
	{
		@Override
		public void run()
		{
			MLog.write(this, "request_Data_Runnable(). run()");
			
			request_Data();
		}
	}

	
	
	private void request_Data()
	{
		MLog.write(this, "request_Data(), mPacketUrl = " + mPacketUrl);
		
		int ContentLen = 0;
		int TotalReadLen = 0;
		
		mHttpcon = null;
		mInputstream = null;
		
		byte Total_Buff[] = null;
		
		try
		{
			mHttpcon = (HttpURLConnection) new URL(mPacketUrl).openConnection();
			mHttpcon.setConnectTimeout(Define.SOCKET_TIMEOUT );
			
			if (mHttpcon.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				mInputstream = mHttpcon.getInputStream();
				ContentLen = mHttpcon.getContentLength();
				
				mHttpcon.setReadTimeout(Define.SOCKET_TIMEOUT);

				int nReadLen = 0;
				int navailable = mInputstream.available();
				
				Total_Buff = new byte[ContentLen];

				if (navailable > 0)
				{
					byte[] buff = new byte[navailable];
					
					while ( true )
					{
						nReadLen = mInputstream.read(buff);
						
						if (nReadLen > 0)
						{
							System.arraycopy(buff, 0, Total_Buff, TotalReadLen, nReadLen);
							TotalReadLen += nReadLen;
							
							
							//if(ContentLen > 0) // �ٿ�ε� �ۼ�Ʈ ���ϱ�
							//{
							//	float Percent = (float)((TotalReadLen / ContentLen) * 100); 
							//}
							//mCall_Back.onPercent(TotalReadLen);
						}
						else
						{
							MLog.write(this, "End Download");
							break;
						}
					}

				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (mHttpcon != null)
			{
				mHttpcon.disconnect();
				Log.e("", "NetWork DisConnect()");
			}
			if (mInputstream != null)
				try
				{
					mInputstream.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
		}
		
		if ( TotalReadLen == ContentLen )
		{
			mCall_Back.onGetResponsData(Total_Buff);
		}
		else 
		{
			mCall_Back.onError( mNetworkErrorMsg /*mContext.getResources().getString(R.string.network_failed)*/ );
		}
		
		if (mKepper != null)
			mKepper.Stop_Kepper();
	}
}
