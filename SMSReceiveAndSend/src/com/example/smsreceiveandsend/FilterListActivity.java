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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.adapters.SMSListAdapter;
import com.models.SMSModel;

public class FilterListActivity extends Activity implements OnItemClickListener {
	public static SMSListViewHolder smsHolder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_list);
		smsHolder = new SMSListViewHolder();
		View header = (View)getLayoutInflater().inflate(R.layout.header, null);
		System.out.println(smsHolder);
		smsHolder.getListView1().addHeaderView(header, null, false);
		ArrayList<SMSModel> stavke = new ArrayList<SMSModel>();
		stavke = fetchInboxSms();
		SMSListAdapter adapter = new SMSListAdapter(FilterListActivity.this, R.layout.activity_main_item, stavke);
		smsHolder.getListView1().setAdapter(adapter);
		smsHolder.getListView1().setOnItemClickListener(FilterListActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter_list, menu);
		return true;
	}
	
	public class SMSListViewHolder
	{
		private ListView listViewPregledSMS;
		public SMSListViewHolder() {
			this.listViewPregledSMS = (ListView)FilterListActivity.this.findViewById(R.id.listViewPregledSMS);
		}
		public ListView getListView1() {
			return listViewPregledSMS;
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
		        	  String body = cursor.getString(cursor.getColumnIndex("body"));
		        	  if(validateMessage(body)){
			        	  SMSModel message = new SMSModel();
			        	  String person = cursor.getString(cursor.getColumnIndex("person"));
			        	  if(person != null && !person.equals("")) {
			        		  message.setSenderNumber(person);
			        	  } else {
			        		  message.setSenderNumber(cursor.getString(cursor.getColumnIndex("address")));
			        	  }
			        	  body = srediText(body);
			              message.setMessage(body);
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
	
	private String srediText(String body) {
		if(body.contains("Replyed")) {
			body = body.substring(0,body.length()-7);
			return body;
		} else {
			return body;
		}
	}

	private boolean validateMessage(String message) {
		boolean ok = false;
		String tipPoruka = getIntent().getStringExtra("lista");
		if(tipPoruka.equals("neodgovorene")) {
			ok=message.contains("misa") && !message.contains("Replyed");
		} else if (tipPoruka.equals("odgovorene")) {
			ok=message.contains("misa") && message.contains("Replyed");
		}
		
		return ok;
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
}
