package com.example.prototipogcmandroid;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddContactActivity extends Activity{

	
	 AsyncTask<Void, Void, Void> mRegisterTask;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_addcontact);
	        
	        
	 }
	 
	 public void addContact(View view){
		 
		 final EditText et = (EditText)findViewById(R.id.newContactET);
		 
		 final Context context = this;
		 mRegisterTask = new AsyncTask<Void, Void, Void>() {

             @Override
             protected Void doInBackground(Void... params) {
                 // Register on our server
                 // On server creates a new user
                 ServerUtilities.addContacts(context, MainActivity.name, et.getText().toString());
                 return null;
             }

             @Override
             protected void onPostExecute(Void result) {
                 mRegisterTask = null;
             }

         };
         mRegisterTask.execute(null, null, null);
	 } 
	 
	 
}
