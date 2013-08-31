package com.musselwhizzle.tapcounter.daos;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.musselwhizzle.tapcounter.vos.CounterVo;

public class CounterDao {
	
	protected static final String TABLE = "Counter";
	protected static final String _ID = "_id";
	protected static final String LABEL = "label";
	protected static final String COUNT = "count";
	protected static final String LOCKED = "locked";
	
	
	public CounterDao() {
		//
	}
	
	public ArrayList<CounterVo> getAll() {
		ArrayList<CounterVo> list = new ArrayList<CounterVo>();
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
		while(cursor.moveToNext()) {
			CounterVo vo = new CounterVo();
			vo.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
			vo.setLabel(cursor.getString(cursor.getColumnIndex(LABEL)));
			vo.setCount(cursor.getInt(cursor.getColumnIndex(COUNT)));
			vo.setLocked(cursor.getInt(cursor.getColumnIndex(LOCKED)) == 1);
			list.add(vo);
		}
		
		cursor.close();
		db.close();
		return list;
	}
	
	public CounterVo get(int id) {
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		Cursor cursor = db.query(TABLE, null, _ID+"=?", new String[] {Integer.toString(id)}, null, null, null);
		CounterVo vo = null;
		if (cursor.moveToFirst()) {
			vo = new CounterVo();
			vo.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
			vo.setLabel(cursor.getString(cursor.getColumnIndex(LABEL)));
			vo.setCount(cursor.getInt(cursor.getColumnIndex(COUNT)));
			vo.setLocked(cursor.getInt(cursor.getColumnIndex(LOCKED)) == 1);
		}
		
		cursor.close();
		db.close();
		return vo;
	}
	
	
	public long insert(CounterVo counterVo) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		ContentValues values = new ContentValues();
		if (counterVo.getId() > 0) values.put(_ID, counterVo.getId());
		values.put(LABEL, counterVo.getLabel());
		values.put(COUNT, counterVo.getCount());
		values.put(LOCKED, counterVo.isLocked());
		
		long num = db.insert(TABLE, null, values);
		db.close();
		return num;
	}
	
	public int update(CounterVo counterVo) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(_ID, counterVo.getId());
		values.put(LABEL, counterVo.getLabel());
		values.put(COUNT, counterVo.getCount());
		values.put(LOCKED, counterVo.isLocked());
		
		int num = db.update(TABLE, values, _ID + "=?", new String[]{Integer.toString(counterVo.getId())});
		db.close();
		return num;
	}
	
	public void delete(int id) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		db.delete(TABLE, _ID+"=?", new String[]{Integer.toString(id)});
		db.close();
	}
	
	public void delete(CounterVo counterVo) {
		delete(counterVo.getId());
	}
	
	public void deleteAll() {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		db.delete(TABLE, null, null);
		db.close();
	}

}
