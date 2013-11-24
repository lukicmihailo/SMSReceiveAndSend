package com.example.smsreceiveandsend;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewSMSActivity extends Activity {
	private String senderNumber = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		String number = getIntent().getStringExtra("senderNumber").toString();
		senderNumber = number;
		String date = getIntent().getStringExtra("date").toString();
		String message = getIntent().getStringExtra("message").toString();
		SMSActivityHolder activityHolder = new SMSActivityHolder();
		activityHolder.getTxtNumber().setText(number);
		activityHolder.getTxtDate().setText(date);
		activityHolder.getTxtMessage().setText(message);
		activityHolder.getBtnRespond().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				respond();
			}
		});
		
		activityHolder.getBtnDecline().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		activityHolder.getBtnRespondLater().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public void respond(){
	       SmsManager sms = SmsManager.getDefault();
	       sms.sendTextMessage(senderNumber, null, "test", null, null);
	}
	
	public void decline(){
	       SmsManager sms = SmsManager.getDefault();
	       sms.sendTextMessage(senderNumber, null, "Neces dobiti, nesto si zajebó", null, null);
	}

	public void respondLater(){
		
	}
	
	public class SMSActivityHolder{
		private EditText txtNumber;
		private EditText txtDate;
		private EditText txtMessage;
		private Button btnRespond;
		private Button btnDecline;
		private Button btnRespondLater;
		public SMSActivityHolder() {
			this.txtNumber = (EditText)NewSMSActivity.this.findViewById(R.id.txtNumber);
			this.txtDate = (EditText)NewSMSActivity.this.findViewById(R.id.txtDate);
			this.txtMessage = (EditText)NewSMSActivity.this.findViewById(R.id.txtMessage);
			this.btnRespond = (Button)NewSMSActivity.this.findViewById(R.id.btnRespond);
			this.btnRespondLater = (Button)NewSMSActivity.this.findViewById(R.id.btnRespondLater);
			this.btnDecline = (Button)NewSMSActivity.this.findViewById(R.id.btnDecline);
		}
		public EditText getTxtNumber() {
			return txtNumber;
		}
		public EditText getTxtDate() {
			return txtDate;
		}
		public EditText getTxtMessage() {
			return txtMessage;
		}
		public Button getBtnRespond() {
			return btnRespond;
		}
		public Button getBtnDecline() {
			return btnDecline;
		}
		public Button getBtnRespondLater() {
			return btnRespondLater;
		}
	}
}
