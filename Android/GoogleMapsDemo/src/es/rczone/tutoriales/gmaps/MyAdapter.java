package es.rczone.tutoriales.gmaps;

import java.util.ArrayList;

import android.app.Activity;
import android.location.Address;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<Address> {
	
	private ArrayList<Address> list;

	public MyAdapter(Activity context, ArrayList<Address> list) {
		super(context, android.R.layout.simple_list_item_checked, list);
		this.list = list;
	}
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View view = super.getView(position, convertView, parent);
	    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
	    text1.setTextSize(12f);

	    Address a = list.get(position);
	    text1.setText(a.getThoroughfare()+" "+a.getSubThoroughfare()+", "+a.getLocality()+", "+a.getCountryName());
	    
	    return view;
	  }
}
