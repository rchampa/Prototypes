package com.example.intenciones;

import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void pgWeb(View view) {
	       Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.androidcurso.com/"));
	       startActivity(intent);
	}
	 
	public void llamadaTelefono(View view) {
		/*
	       Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:962849347"));
	       startActivity(intent);
	       */
		
		tweetPanico();
	}
	
	public void marcarTelefono(View view) {
	       Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:962849347"));
	       startActivity(intent);
	}
	 
	public void googleMaps(View view) {
	       Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:41.656313,-0.877351"));
	       startActivity(intent);
	}
	 
	public void tomarFoto(View view) {
	       Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	       startActivity(intent);
	}
	 
	public void mandarCorreo(View view) {
		
	       Intent intent = new Intent(Intent.ACTION_SEND);
	       intent.setType("message/rfc822");
	       intent.putExtra(Intent.EXTRA_SUBJECT, "asunto");
	       intent.putExtra(Intent.EXTRA_TEXT, "texto del correo");
	       intent.putExtra(Intent.EXTRA_EMAIL, new String[] {" jtomas@upv.es" });
	       startActivity(intent);
		
		/*
		Intent gmail = new Intent(Intent.ACTION_VIEW);
        gmail.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
        gmail.putExtra(Intent.EXTRA_EMAIL, new String[] { "jckdsilva@gmail.com" });
        gmail.setData(Uri.parse("jckdsilva@gmail.com"));
        gmail.putExtra(Intent.EXTRA_SUBJECT, "enter something");
        gmail.setType("plain/text");
        gmail.putExtra(Intent.EXTRA_TEXT, "hi android jack!");
        startActivity(gmail);
        */
	}
	
	public void tweetPanico(){
		Intent shareIntent = findTwitterClient(); 
        shareIntent.putExtra(Intent.EXTRA_TEXT, "test");
        startActivity(Intent.createChooser(shareIntent, "Share"));
        
	}
	
	public Intent findTwitterClient() {
	    final String[] twitterApps = {
	            // package // name - nb installs (thousands)
	            "com.twitter.android", // official - 10 000
	            "com.twidroid", // twidroid - 5 000
	            "com.handmark.tweetcaster", // Tweecaster - 5 000
	            "com.thedeck.android" }; // TweetDeck - 5 000 };
	    Intent tweetIntent = new Intent();
	    tweetIntent.setType("text/plain");
	    final PackageManager packageManager = getPackageManager();
	    List<ResolveInfo> list = packageManager.queryIntentActivities(
	            tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

	    for (int i = 0; i < twitterApps.length; i++) {
	        for (ResolveInfo resolveInfo : list) {
	            String p = resolveInfo.activityInfo.packageName;
	            if (p != null && p.startsWith(twitterApps[i])) {
	                tweetIntent.setPackage(p);
	                return tweetIntent;
	            }
	        }
	    }
	    return null;

	}

}
