package com.musselwhizzle.tapcounter.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class PixelUtil {
	
	public static int dpToPx(Context context, int dp) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float scale = metrics.density;
		
		return (int)(scale * dp);
		
	}

}
