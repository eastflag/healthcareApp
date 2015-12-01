package kr.co.aura.mtelo.healthcare.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class HCDialog extends SherlockDialogFragment {
	private AlertDialog.Builder mBuilder;
	
	public static final int DIALOG_DEFAULT = 100;
	
	
//				    ((Terms)getActivity()8).doPositiveClick();
	 public static DialogFragment newInstace(int layout_id) {
	        DialogFragment dialogFragment = new HCDialog();
	        Bundle args = new Bundle();
	        args.putInt("layout_id", layout_id);
	        dialogFragment.setArguments(args);
	        return dialogFragment;
	    }
	 public static DialogFragment newInstace(int layout_id, int title, int msg) {
	        DialogFragment dialogFragment = new HCDialog();
	        Bundle args = new Bundle();
	        args.putInt("layout_id", layout_id);
	        args.putInt("title", title);
	        args.putInt("msg", msg);
	        dialogFragment.setArguments(args);
	        return dialogFragment;
	    }
	 
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
//	    int layout = getArguments().getInt("layout_id");
//	    mBuilder = new AlertDialog.Builder(getActivity()) ;
//	    mBuilder.setView(LayoutInflater.from(getActivity()).inflate(layout, null));
		return makeDialog(getArguments());
	}
	
	
	private Dialog makeDialog( Bundle b)
	{
		mBuilder = new AlertDialog.Builder(getActivity()) ;
		int layout =b.getInt("layout_id");
		if(layout == DIALOG_DEFAULT ) //디폴트 다이얼로그
		{
			mBuilder.setTitle(b.getInt("title"));
			mBuilder.setMessage(b.getInt("msg"));
			mBuilder.setNeutralButton(android.R.string.ok, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
					Log.e("" , "getTargetRequestCode() "+getTargetRequestCode());
				}
			});
				
		}
	    return mBuilder.create();
	}
	
	
	//원버튼 리스너
	public void setOneButton(int textId, OnClickListener listener )
	{
		mBuilder.setNeutralButton(textId, listener);
	}
	public void setOneButton(String text, OnClickListener listener )
	{
		mBuilder.setNeutralButton(text, listener);
	}
	
	//투버튼 리스너  
	public void setOneButton(int p_Id, OnClickListener p_listener, int n_Id, OnClickListener n_listener )
	{
		mBuilder.setPositiveButton(p_Id, p_listener);
		mBuilder.setNeutralButton(n_Id, n_listener);
	}
	public void setOneButton(String p_Id, OnClickListener p_listener, String n_Id, OnClickListener n_listener )
	{
		mBuilder.setPositiveButton(p_Id, p_listener);
		mBuilder.setNeutralButton(n_Id, n_listener);
	}

	public void show(FragmentManager fragmentManager, String string) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}

