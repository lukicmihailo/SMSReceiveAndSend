package com.example.smsreceiveandsend;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FilterListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter_list, menu);
		return true;
	}

}
