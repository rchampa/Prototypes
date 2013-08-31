package com.musselwhizzle.tapcounter.daos;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.musselwhizzle.tapcounter.TapCounterApp;

final class DatabaseHelper extends SQLiteOpenHelper {

	@SuppressWarnings("unused")
	private static final String TAG = DatabaseHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "TapCounter";
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper() {
		super(TapCounterApp.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		final String counter = "CREATE TABLE " + CounterDao.TABLE + "(" +
			CounterDao._ID + " integer primary key, " +
			CounterDao.LABEL + " text, " +
			CounterDao.COUNT + " int, " +
			CounterDao.LOCKED + " int)"; 
		database.execSQL(counter);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// first iteration. do nothing.
	}

}
