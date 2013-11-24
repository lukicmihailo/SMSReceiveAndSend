package com.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.smsreceiveandsend.R;
import com.models.SMSModel;



public class SMSListAdapter extends ArrayAdapter<SMSModel> {

	Context context;
	int layoutResourceId;
	ArrayList<SMSModel> data = null;

	// ispod je konstruktor ove klase koji ima tri parametra
	// prvi je Context i mozemo proslediti referencu aktivitija u kom cemo
	// koristiti ovu klasu
	// drugi parametar je resource id layout fajla koji zelimo da koristimo za
	// prikaz svake stavke
	// kod nas je to resource id od list_item.xml
	// treci parametar je lista objekata klase SMSModel koji ce biti korisceni
	// od strane adaptera da bi prikazao podatke

	public SMSListAdapter(Context context, int layoutResourceId,
			ArrayList<SMSModel> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		// koristi privremenu holder klasu koja je deklarisana u klasi
		// SMSListAdapter
		// holder se koristi da se 'uhvate' textViewovi da mogu biti ponovo
		// korisceni za
		// svaki red u ListView i omogucavaju povecanje performanse
		PregledSMSHolder holder = null;
		if (row == null) {
			// ovaj inflator se koristi kao da se parsira nas xml file, tj
			// list_item.xml
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new PregledSMSHolder();
			holder.sender = (TextView) row.findViewById(R.id.sender);
			holder.date = (TextView) row.findViewById(R.id.date);
			row.setTag(holder);
		} 
		else {
			holder = (PregledSMSHolder) row.getTag();
		}
		SMSModel smsModel = (SMSModel) data.get(position);
		holder.sender.setText(smsModel.getSenderNumber());
		holder.date.setText(smsModel.getDate());
		return row;
	}

	// definisanje klase holdera
	static class PregledSMSHolder {
		TextView sender;
		TextView date;
	}
}
