package com.example.smsreceiveandsend;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.adapters.SMSListAdapter;
import com.models.SMSModel;

public class AllSmsList extends Activity implements OnItemClickListener{

	public static SMSListViewHolder smsHolder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		smsHolder = new SMSListViewHolder();
		View header = (View)getLayoutInflater().inflate(R.layout.header, null);
		System.out.println(smsHolder);
		smsHolder.getListView1().addHeaderView(header, null, false);
		ArrayList<SMSModel> stavke = new ArrayList<SMSModel>();
		stavke = fetchInboxSms();
//		SMSModel sms = new SMSModel("0642736408", "21.01.2003", "Dobar dan");
//		stavke.add(sms);
		SMSListAdapter adapter = new SMSListAdapter(AllSmsList.this, R.layout.activity_main_item, stavke);
		smsHolder.getListView1().setAdapter(adapter);
		smsHolder.getListView1().setOnItemClickListener(AllSmsList.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
	    case R.id.action_prikazi_neodgovorene:
	    	 Intent intent = new Intent(this, FilterListActivity.class);
	    	 intent.putExtra("lista", "neodgovorene");
		     this.startActivity(intent);
	        break;
	    case R.id.action_prikazi_odgovorene:
	    	 Intent intent1 = new Intent(this, FilterListActivity.class);
	    	 intent1.putExtra("lista", "odgovorene");
		     this.startActivity(intent1);
	        break;
	    case R.id.action_prikazi_sve:
	    	 Intent intent2 = new Intent(this, AllSmsList.class);
	    	 intent2.putExtra("lista", "odgovorene");
		     this.startActivity(intent2);
	        break;
	    }
	    return true;
	}
	public class SMSListViewHolder
	{
		private ListView listViewPregledSMS;
		public SMSListViewHolder() {
			this.listViewPregledSMS = (ListView)AllSmsList.this.findViewById(R.id.listViewPregledSMS);
		}
		public ListView getListView1() {
			return listViewPregledSMS;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> a, View arg1, int position, long arg3) {
		SMSModel sms = (SMSModel) a.getItemAtPosition(position);
		if(sms!=null){
			Intent i = new Intent(this, NewSMSActivity.class);
			i.putExtra("date", sms.getDate());
			i.putExtra("senderNumber", sms.getSenderNumber());
			i.putExtra("message", sms.getMessage());
			i.putExtra("_id", sms.getId());
			startActivity(i);		
		}
	}
	
	public ArrayList<SMSModel> fetchInboxSms() {
	  ArrayList<SMSModel> smsInbox = new ArrayList<SMSModel>();
	  Uri uriSms = Uri.parse("content://sms/inbox");
	  Cursor cursor = this.getContentResolver()
	          .query(uriSms, new String[] { "_id", "address", "person", "date", "body",
	                          "type", "read" }, "type="+1, null,
	                  "date" + " COLLATE LOCALIZED ASC");
	  if (cursor != null) {
	      cursor.moveToLast();
	      if (cursor.getCount() > 0) {
	          do {
	        	  if(validateMessage(cursor.getString(cursor.getColumnIndex("body")))){
		        	  SMSModel message = new SMSModel();
		        	  String person = cursor.getString(cursor.getColumnIndex("person"));
		        	  if(person != null && !person.equals("")) {
		        		  message.setSenderNumber(person);
		        	  } else {
		        		  message.setSenderNumber(cursor.getString(cursor.getColumnIndex("address")));
		        	  }
		              message.setMessage(cursor.getString(cursor.getColumnIndex("body")));
		              Date dateFromSms = new Date(new Long(cursor.getString(cursor.getColumnIndex("date"))));
		              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
		              message.setDate(sdf.format(dateFromSms));
		              message.setId(cursor.getString(cursor.getColumnIndex("_id")));
		              smsInbox.add(message);
	        	  }
	          } while (cursor.moveToPrevious());
	      }
	  }
	  return smsInbox;
	}

	private boolean validateMessage(String message) {
		boolean ok = false;
		ok=message.contains("misa") && !message.contains("Replyed");
		return ok;
	}
	
	public void brisiPoruku(View view){
		//Implementirati
		
		ImageView image = (ImageView) view;
		SMSModel model = (SMSModel) image.getTag();//ovde imate sve podatke koji vam trebaju
		System.out.println(model.getId());
		}
}
