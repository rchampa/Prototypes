package es.rczone.tutoriales.gmaps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddressActivity extends Activity{

	
	private EditText etAddress;
	private ListView listView;
	private final int MAX_RESULTS = 10;
	private ArrayList<Address> addressList;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_address);
            
            addressList = new ArrayList<Address>();
            
            etAddress = (EditText)findViewById(R.id.et_address);
            listView = (ListView)findViewById(R.id.address_list);
            MyAdapter adapter = new MyAdapter(this, addressList);
            listView.setAdapter(adapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            
            
    }
	
	
	public void onClick(View v){
		
		switch(v.getId()){
			
			case R.id.but_search:
				searchForAddress();
				break;
				
			case R.id.but_show_location:
				int pos = listView.getCheckedItemPosition();
				Address a= (Address)listView.getAdapter().getItem(pos);
				Toast.makeText(this, a.getLocality(), Toast.LENGTH_SHORT).show();
				break;
		
		}
		
		
	}


	private void searchForAddress() {
		
		addressList.clear();
		MyAdapter adapter = (MyAdapter) listView.getAdapter();
		adapter.clear();
			
		String address = etAddress.getText().toString();
		
		if(address!=null){
			try {
				Geocoder geocoder = new Geocoder(this);  
				List<Address> addresses = geocoder.getFromLocationName(address, MAX_RESULTS);
//				if(addresses.size() > 0) {
//				    double latitude= addresses.get(0).getLatitude();
//				    double longitude= addresses.get(0).getLongitude();
//				}
				
				for (Address a : addresses) {
					
					adapter.add(a);
					
				}
				
				adapter.notifyDataSetChanged();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
