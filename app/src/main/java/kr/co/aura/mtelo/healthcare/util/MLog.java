package kr.co.aura.mtelo.healthcare.util;

import android.util.Log;


public class MLog 
{
	private static int mLogCount = 0;
	private static boolean mEnableLog = true;
	
	private static String mFilter = "";
	
	public static void set_LogEnable(boolean b, String filter)
	{
		mEnableLog = b;
		mFilter = filter;
	}
	
	public static void write(int priority, String tag, String msg)
	{
		if (mEnableLog)
		{
			Log.println(priority, tag, ++mLogCount + " : " + mFilter + " | " + msg);
		}
		else
		{
			if ( priority == Log.ERROR )
				Log.println(priority, tag, ++mLogCount + " : " + mFilter + " | " + msg);
		}
	}

	public static void write(String tag, String msg)
	{
		if (mEnableLog)
		{
			Log.println(Log.DEBUG, tag, ++mLogCount + " : " + mFilter + " | " + msg);
		}
	}


	public static void write(Object obj, String msg)
	{
		if (mEnableLog)
		{
			Log.println(Log.DEBUG, obj.getClass().getSimpleName(), ++mLogCount + " : " + mFilter + " | " + msg);
		}
	}
}
