package kr.co.aura.mtelo.healthcare.network;

import java.util.Timer;
import java.util.TimerTask;


public class LTimeOut_Kepper
{
	
	private Timer m_Timer;
	
	private Kepper m_Kepper;
	
	private Kepper_CallBack m_Kepper_CallBack;
	
	
	public interface Kepper_CallBack
	{
		void onTimeOut();
	}
	
	
	public void Start_Kepper(int start_time , long delay_time, Kepper_CallBack kepper_callBack)
	{
		Stop_Kepper();
		
		m_Kepper_CallBack = kepper_callBack;
		
		m_Timer = new Timer();
		m_Kepper = new Kepper();
		
		if(delay_time == 0) //딜레이 타일을 주지않았을경우 1회실행
			m_Timer.schedule(m_Kepper, start_time);
		else  				//딜레이 타일을 주지않았을경우 연속실행
			m_Timer.schedule(m_Kepper, start_time, delay_time);
	}
	
	
	public void Start_Kepper(int time_out, Kepper_CallBack kepper_callBack)
	{
		Start_Kepper(time_out, 0, kepper_callBack);
	}
	
	
	
	public void Stop_Kepper()
	{
		if ( m_Timer != null )
		{
			m_Timer.cancel();
			m_Timer = null;
		}
		
		if ( m_Kepper != null )
		{
			m_Kepper.cancel();
			m_Kepper = null;
		}
	}


	
	private class Kepper extends TimerTask
	{
		@Override
		public void run()
		{
			Stop_Kepper();
			m_Kepper_CallBack.onTimeOut();
		}
	}

}
