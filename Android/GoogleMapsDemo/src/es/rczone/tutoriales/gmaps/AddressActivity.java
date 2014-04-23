package es.rczone.tutoriales.gmaps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

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
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result",a);
				setResult(RESULT_OK,returnIntent);     
				finish();
				break;
		
		}
		
		
	}
	
	@Override
	public void onBackPressed() {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);        
		finish();
	}


	private void searchForAddress() {
		
		AsyncTask<Void, Void, Void> searching = new AsyncTask<Void, Void, Void>() {
			
			ProgressDialog progressDialog;
			MyAdapter adapter;
			String address;
			List<Address> addresses;
			
			
			@Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        progressDialog = new ProgressDialog(AddressActivity.this);
		        progressDialog.setMessage(AddressActivity.this.getString(R.string.loading_message));
		        progressDialog.setIndeterminate(true);
		        progressDialog.setCancelable(false);
		        progressDialog.show();
		        addressList.clear();
				adapter = (MyAdapter) listView.getAdapter();
				adapter.clear();
				adapter.notifyDataSetChanged();
				address = etAddress.getText().toString();
		    }
			
			@Override
			protected Void doInBackground(Void... params) {
				
				if(address!=null){
					try {
						Geocoder geocoder = new Geocoder(AddressActivity.this);  
						addresses = geocoder.getFromLocationName(address, MAX_RESULTS);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}
			
			@Override
		    protected void onPostExecute(Void result) {
		        super.onPostExecute(result);   
		        
		        for (Address a : addresses) {
					adapter.add(a);
				}
		        
		        adapter.notifyDataSetChanged();
		        
		        progressDialog.hide();        
		        
		        
		    }
		};
		
		searching.execute();
		
	}
}
