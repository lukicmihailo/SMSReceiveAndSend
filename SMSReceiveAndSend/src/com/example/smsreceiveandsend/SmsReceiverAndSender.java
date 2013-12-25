package com.example.smsreceiveandsend;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiverAndSender extends BroadcastReceiver {
	
	@Override
    public void onReceive(Context context, Intent intent){
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String messageReceived = "";            
        if (bundle != null){
            //---retrieve the SMS message received---
           Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
                messageReceived += msgs[i].getMessageBody().toString();
                messageReceived += "\n";        
            }

            //---display the new SMS message---
            Toast.makeText(context, messageReceived, Toast.LENGTH_SHORT).show();

            // Get the Sender Phone Number

           String senderPhoneNumber=msgs[0].getOriginatingAddress();   
           Date date=new Date(msgs[0].getTimestampMillis());   
           Toast.makeText(context, senderPhoneNumber, Toast.LENGTH_LONG).show();
           
           if(messageReceived.contains("misa")){
        	   Intent newIntent = new Intent(context,NewSMSActivity.class);
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        	   newIntent.putExtra("date", sdf.format(date));
        	   newIntent.putExtra("senderNumber", senderPhoneNumber);
        	   newIntent.putExtra("message", messageReceived);
        	   newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	   context.startActivity(newIntent);
           }
       }                         
    }
}
