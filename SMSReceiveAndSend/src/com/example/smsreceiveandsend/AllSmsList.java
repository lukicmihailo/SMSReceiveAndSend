package com.example.smsreceiveandsend;

import java.util.ArrayList;
import java.util.Date;

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
		smsHolder.getListView1().setSelector( R.drawable.list_selector);
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
		              message.setDate(dateFromSms.toString());
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
		ok=message.contains("misa")&&!message.contains("Replyed") ;
		return ok;
	}
	
	
	
	
	
	
	
//    public class MainActivity extends Activity {
//private static final int TYPE_INCOMING_MESSAGE = 1;
//private ListView messageList;
//private MessageListAdapter messageListAdapter;
//private ArrayList<Message> recordsStored;
//private ArrayList<Message> listInboxMessages;
//private ProgressDialog progressDialogInbox;
//private CustomHandler customHandler;
//
//@Override
//public void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_main);
//    initViews();
//
//}
//
//@Override
//public void onResume() {
//    super.onResume();
//    populateMessageList();
//}
//
//private void initViews() {
//    customHandler = new CustomHandler(this);
//    progressDialogInbox = new ProgressDialog(this);
//
//    recordsStored = new ArrayList<Message>();
//
//    messageList = (ListView) findViewById(R.id.messageList);
//    populateMessageList();
//}
//
//public void populateMessageList() {
//    fetchInboxMessages();
//
//    messageListAdapter = new MessageListAdapter(this,
//            R.layout.message_list_item, recordsStored);
//    messageList.setAdapter(messageListAdapter);
//}
//
//private void showProgressDialog(String message) {
//    progressDialogInbox.setMessage(message);
//    progressDialogInbox.setIndeterminate(true);
//    progressDialogInbox.setCancelable(true);
//    progressDialogInbox.show();
//}
//
//private void fetchInboxMessages() {
//    if (listInboxMessages == null) {
//        showProgressDialog("Fetching Inbox Messages...");
//        startThread();
//    } else {
//        // messageType = TYPE_INCOMING_MESSAGE;
//        recordsStored = listInboxMessages;
//        messageListAdapter.setArrayList(recordsStored);
//    }
//}
//
//public class FetchMessageThread extends Thread {
//
//    public int tag = -1;
//
//    public FetchMessageThread(int tag) {
//        this.tag = tag;
//    }
//
//    @Override
//    public void run() {
//
//        recordsStored = fetchInboxSms(TYPE_INCOMING_MESSAGE);
//        listInboxMessages = recordsStored;
//        customHandler.sendEmptyMessage(0);
//
//    }
//
//}
//
//public ArrayList<Message> fetchInboxSms(int type) {
//    ArrayList<Message> smsInbox = new ArrayList<Message>();
//
//    Uri uriSms = Uri.parse("content://sms");
//
//    Cursor cursor = this.getContentResolver()
//            .query(uriSms,
//                    new String[] { "_id", "address", "date", "body",
//                            "type", "read" }, "type=" + type, null,
//                    "date" + " COLLATE LOCALIZED ASC");
//    if (cursor != null) {
//        cursor.moveToLast();
//        if (cursor.getCount() > 0) {
//
//            do {
//
//                Message message = new Message();
//                message.messageNumber = cursor.getString(cursor
//                        .getColumnIndex("address"));
//                message.messageContent = cursor.getString(cursor
//                        .getColumnIndex("body"));
//                smsInbox.add(message);
//            } while (cursor.moveToPrevious());
//        }
//    }
//
//    return smsInbox;
//
//}
//
//private FetchMessageThread fetchMessageThread;
//
//private int currentCount = 0;
//
//public synchronized void startThread() {
//
//    if (fetchMessageThread == null) {
//        fetchMessageThread = new FetchMessageThread(currentCount);
//        fetchMessageThread.start();
//    }
//}
//
//public synchronized void stopThread() {
//    if (fetchMessageThread != null) {
//        Log.i("Cancel thread", "stop thread");
//        FetchMessageThread moribund = fetchMessageThread;
//        currentCount = fetchMessageThread.tag == 0 ? 1 : 0;
//        fetchMessageThread = null;
//        moribund.interrupt();
//    }
//}
//
//static class CustomHandler extends Handler {
//    private final WeakReference<MainActivity> activityHolder;
//
//    CustomHandler(MainActivity inboxListActivity) {
//        activityHolder = new WeakReference<MainActivity>(inboxListActivity);
//    }
//
//    @Override
//    public void handleMessage(android.os.Message msg) {
//
//        MainActivity inboxListActivity = activityHolder.get();
//        if (inboxListActivity.fetchMessageThread != null
//                && inboxListActivity.currentCount == inboxListActivity.fetchMessageThread.tag) {
//            Log.i("received result", "received result");
//            inboxListActivity.fetchMessageThread = null;
//
//            inboxListActivity.messageListAdapter
//                    .setArrayList(inboxListActivity.recordsStored);
//            inboxListActivity.progressDialogInbox.dismiss();
//        }
//    }
//}
//
//private OnCancelListener dialogCancelListener = new OnCancelListener() {
//
//    @Override
//    public void onCancel(DialogInterface dialog) {
//        stopThread();
//    }
//
//};

}
