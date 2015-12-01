package kr.co.aura.mtelo.healthcare.util;

import kr.co.aura.mtelo.healthcare.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Popup_Manager
{
	public interface TwoButton_Handle
	{
		void onPostive();
		void onNegative();
	}
	
	public interface OneButton_Handle
	{
		void onOK();
	}
	
//	나야나 적용 인터페이스 
	public interface SetSmartRing
	{
		void onSmartRing();
		void onGroupRing();
		void onCancel();
	}

	/**
	 * 
	 * 2개의 버튼을 사용하는 팝업 
	 * 
	 * @param ctx				컨텐스트
	 * @param title				팝업 제목
	 * @param msg				팝업 내용
	 * @param postive			확인 키에 적을 내용 - 예: 확인 or 나야나 설정
	 * @param negative			취소 키에 적을 내용 - 예: 취소 or 다시 만들기
	 * @param handle			리턴 받을 함수 등록
	 */
	public static void Show_Dialog(Context ctx, String title, String msg, String postive, String negative, final TwoButton_Handle handle)
	{
		try 
		{// TODO: handle exception
			AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
			
			dlg.setTitle(title);
			dlg.setMessage(msg);
			dlg.setPositiveButton(postive, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					handle.onPostive();
				}
			});
			
			dlg.setNegativeButton(negative, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					handle.onNegative();
				}
			});
			
			dlg.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				@Override
				public void onCancel(DialogInterface dialog)
				{
					handle.onNegative();
					dialog.dismiss();				
				}
			});
			
			dlg.show();
			
		}
		catch (Exception e)
		{
		}
		catch (OutOfMemoryError e)
		{
		}		
	}
	
	
	/**
	 * 
	 * 2개의 버튼을 사용하는 팝업 
	 * 
	 * @param ctx				컨텐스트
	 * @param title				팝업 제목
	 * @param msg				팝업 내용
	 * @param postive			확인 키에 적을 내용 - 예: 확인 or 나야나 설정
	 * @param negative			취소 키에 적을 내용 - 예: 취소 or 다시 만들기
	 * @param handle			리턴 받을 함수 등록
	 */
	public static void Show_Dialog(Context ctx, int title, int msg, int postive, int negative, final TwoButton_Handle handle)
	{
		{// TODO: handle exception
			AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
			if(title != 0 )
				dlg.setTitle(title);
			
			dlg.setMessage(msg);
			dlg.setPositiveButton(postive, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					handle.onPostive();
				}
			});
			
			dlg.setNegativeButton(negative, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					handle.onNegative();
				}
			});
			
			dlg.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				@Override
				public void onCancel(DialogInterface dialog)
				{
					handle.onNegative();
					dialog.dismiss();				
				}
			});
			
			dlg.show();
			
		}
	}

	/**
	 * 1개의 버튼을 사용하는 팝업
	 * 
	 * @param ctx				컨텐스트
	 * @param title				팝업 제목
	 * @param msg				팝업 내용
	 * @param btn_Confirm			확인 키에 적을 내용 - 예: 확인 or 나야나 설정
	 * @param handle			리턴 받을 함수 등록
	 */
	public static void Show_Dialog(Context ctx, String title, String msg, String btn_Confirm, final OneButton_Handle handle)
	{
		AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
		
		dlg.setTitle(title);
		dlg.setMessage(msg);
		dlg.setPositiveButton(btn_Confirm, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				handle.onOK();
			}
		});
		
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				handle.onOK();
				dialog.dismiss();
			}
		});
		
		dlg.show();
	}
	
	/**
	 * 1개의 버튼을 사용하는 팝업
	 * 
	 * @param ctx				컨텐스트
	 * @param title				팝업 제목
	 * @param msg				팝업 내용
	 * @param btnTitle			확인 키에 적을 내용 - 예: 확인 or 나야나 설정
	 * @param handle			리턴 받을 함수 등록
	 */
	public static void Show_Dialog(Context ctx, int title, int msg, int btnTitle, final OneButton_Handle handle) 
	{
		AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
		if(title != 0 )
			dlg.setTitle(title);
		dlg.setMessage(msg);
		dlg.setPositiveButton(btnTitle, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				handle.onOK();
			}
		});
		
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				handle.onOK();
				dialog.dismiss();
			}
		});
		dlg.show();
	}

	
	/**
 * 
 *  Dioaig 의 Massage 내용이 String 로 되어 있을경우
 * */
	public static AlertDialog.Builder Get_Dialog(Context ctx, int titleID, int msgID, int btnTitle, final OneButton_Handle handle)
	{
		AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
		if(titleID != 0 )
			dlg.setTitle(titleID);
		
		dlg.setMessage(msgID)
		.setPositiveButton(btnTitle, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				handle.onOK();
			}
		});
		
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				handle.onOK();
				dialog.dismiss();
			}
		});
		
		return dlg;
	}

	
	public static AlertDialog.Builder Get_Dialog(Context ctx, int titleID, String msg, int btnTitle, final OneButton_Handle handle)
	{
		String title = null;
		if(titleID > 0 )
		{
			title = ctx.getResources().getString(titleID);
		}
		
		String btn = ctx.getResources().getString(btnTitle);
		return Get_Dialog(ctx, title, msg, btn, handle);
		
	}
	
	public static AlertDialog.Builder Get_Dialog(Context ctx, String titleID, String msg, String btnTitle, final OneButton_Handle handle)
	{
		AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
		
		dlg.setTitle(titleID)
		.setMessage(msg)
		.setPositiveButton(btnTitle, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				handle.onOK();
			}
		});
		
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				handle.onOK();
				dialog.dismiss();
			}
		});
		
		return dlg;
		
	}
	
// Context대신 	Activity 를 인자로 받는다 
	public static AlertDialog.Builder Get_Dialog(Activity act, int titleID, int msgID, int btnTitle, final OneButton_Handle handle)
	{
		return Get_Dialog(act.getApplicationContext(), titleID, msgID, btnTitle, handle);
	}
	
/**
/**
 * 
 *  Dioaig 의 Massage 내용이 int 로 되어 있을경우
 * @param btnCancle 
 * */
	public static AlertDialog.Builder Get_Dialog(Context ctx, int titleID, int msgID, int btnTitle, int btnCancle, final OneButton_Handle handle)
	{
		
		AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
		
		dlg.setTitle(titleID)
		.setMessage(msgID)
		.setPositiveButton(btnTitle, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				handle.onOK();
			}
		});
		
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				handle.onOK();
				dialog.dismiss();
			}
		});
		dlg.setCancelable(false);
		
		return dlg;
		
//		return Get_Dialog(ctx, titleID, ctx.getResources().getString(msgID) , btnTitle, handle);
	}
	
	
	
	public static AlertDialog.Builder Get_Dialog(Context ctx, int titleID, int msgID, String postive, String negative, final TwoButton_Handle handle)
	{
		AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
		if(titleID !=0)
			dlg.setTitle(titleID);
		
		dlg.setMessage(msgID)
		.setPositiveButton(postive, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				handle.onPostive();
			}
		})
		.setNegativeButton(negative, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				handle.onNegative();
			}
		})
		.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				handle.onNegative();
				dialog.dismiss();
			}
		});
		
		return dlg;
	}
	
	
	
	static ProgressDialog progress;
	//	SetSmartRingDialog 실행후 작업이 졸료시에 호출하여야 한다 
	public static void CloseProgressDialog()
	{
		if(progress.isShowing()) progress.dismiss();
	}


	public static AlertDialog.Builder Get_Dialog(Context ctx, int titleID, int msgID, int postive, int negative, final TwoButton_Handle handle)
	{
		AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
		if(titleID !=0)
			dlg.setTitle(titleID);
		
		dlg.setMessage(msgID)
		.setPositiveButton(postive, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				handle.onPostive();
			}
		})
		.setNegativeButton(negative, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				handle.onNegative();
			}
		})
		.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				handle.onNegative();
				dialog.dismiss();
			}
		});
		
		return dlg;
	}

	public static void Show_Dialog(Context mContext, String title, String msg,	int btnConfirm,  OneButton_Handle handle) {
		String btn_Confirm = mContext.getResources().getString(btnConfirm);
		Show_Dialog(mContext, title, msg, btn_Confirm,  handle);
	}
	
	public static void Show_Dialog(Context mContext, String title, String msg,	int btnConfirm, int btnCancle, TwoButton_Handle handle) {
		String btn_Confirm = mContext.getResources().getString(btnConfirm);
		String btn_Cancle =  mContext.getResources().getString(btnCancle);
		Show_Dialog(mContext, title, msg, btn_Confirm , btn_Cancle, handle);
	}

	public static void SHow_Unused_Functions(Context ctx, String msg, String positiveMsg)
	{
		AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
//		dlg.setMessage(R.string.dialog_unuseed_functions);
//		dlg.setPositiveButton(R.string.btn_Confirm, new DialogInterface.OnClickListener()
		dlg.setMessage(msg);
		dlg.setPositiveButton(positiveMsg, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();			
			}
		});
		
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				dialog.dismiss();
			}
		});
		
		dlg.show();
		
	}


	public static void Show_Dialog(Context mContext, int title, String msg,	int btnConfirm, OneButton_Handle handle) {
		
		Show_Dialog(mContext, null , msg, btnConfirm, handle);
	}
	
		
	//에러 메세지 표시 1
	public static void Show_Error_Dialog(Context ctx, int code, final OneButton_Handle one)
	{
		AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
		dlg.setTitle(R.string.error_popup_title);
		String msg = ctx.getResources().getString(code) + ctx.getResources().getString(R.string.error_popup_msg);
		dlg.setMessage(msg);
		dlg.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				one.onOK();
				dialog.dismiss();
			}
		});
		dlg.show();
	}
	
	//에러 메세지 표시 2
	public static void Show_Error_Dialog(Context ctx, String code, final OneButton_Handle one)
	{
		AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
		dlg.setTitle(R.string.error_popup_title);
		dlg.setMessage(code);
		dlg.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				one.onOK();
				dialog.dismiss();
			}
		});
		dlg.show();
	}
}
