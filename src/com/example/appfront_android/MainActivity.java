package com.example.appfront_android;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
public class MainActivity extends Wifip2pSearchActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Called when a user starts the search for a tag
	 * @param view
	 */
	public void startSearch(View view){
		final EditText editTag 		= (EditText)findViewById(R.id.txtTag);
		final String tag			= editTag.getText().toString();
		// only start search if tag is not empty
		if(tag.length()>0){
			startSearch(tag);
		}
	}

	@Override
	protected void toogleCheck(boolean enable) {
		final CheckBox chkFound 		= (CheckBox)findViewById(R.id.chkFound);
		chkFound.setChecked(enable);
	}
}
