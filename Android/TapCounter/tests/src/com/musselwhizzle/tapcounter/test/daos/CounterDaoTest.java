package com.musselwhizzle.tapcounter.test.daos;

import android.test.AndroidTestCase;

import com.musselwhizzle.tapcounter.daos.CounterDao;
import com.musselwhizzle.tapcounter.vos.CounterVo;

public class CounterDaoTest extends AndroidTestCase {
	
	public CounterDaoTest() {
	}
	
	@Override
	protected void setUp() throws Exception {
		CounterVo vo1 = new CounterVo();
		vo1.setId(44);
		vo1.setLabel("SetUp Label");
		vo1.setCount(1000);
		vo1.setLocked(false);
		new CounterDao().insert(vo1);
		
		CounterVo vo2 = new CounterVo();
		vo2.setId(45);
		vo2.setLabel("SetUp Label2");
		vo2.setCount(5000);
		vo2.setLocked(true);
		new CounterDao().insert(vo2);
		
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		new CounterDao().deleteAll();
		super.tearDown();
	}
	
	
	public void testGet() {
		CounterVo vo = new CounterDao().get(44);
		assertNotNull(vo);
		assertEquals(44, vo.getId());
		assertEquals("SetUp Label", vo.getLabel());
		assertEquals(1000, vo.getCount());
		assertEquals(false, vo.isLocked());
	}
	
	public void testGet2() {
		CounterVo vo = new CounterDao().get(45);
		assertNotNull(vo);
		assertEquals(45, vo.getId());
		assertEquals("SetUp Label2", vo.getLabel());
		assertEquals(5000, vo.getCount());
		assertEquals(true, vo.isLocked());
	}
	
	public void testUpdate() {
		CounterVo vo1 = new CounterVo();
		vo1.setId(44);
		vo1.setLabel("SetUp Label Update");
		vo1.setCount(1001);
		vo1.setLocked(true);
		new CounterDao().update(vo1);
		
		CounterVo vo = new CounterDao().get(44);
		assertNotNull(vo);
		assertEquals(44, vo.getId());
		assertEquals("SetUp Label Update", vo.getLabel());
		assertEquals(1001, vo.getCount());
		assertEquals(true, vo.isLocked());
	}
	
	public void testInsert() {
		CounterVo vo1 = new CounterVo();
		vo1.setId(23);
		vo1.setLabel("Insert Label");
		vo1.setCount(500);
		vo1.setLocked(false);
		new CounterDao().insert(vo1);
		
		CounterVo vo = new CounterDao().get(vo1.getId());
		
		assertEquals(23, vo.getId());
		assertEquals("Insert Label", vo.getLabel());
		assertEquals(500, vo.getCount());
		assertEquals(false, vo.isLocked());
	}
	
	public void testInsertDuplicateId() {
		CounterVo vo1 = new CounterVo();
		vo1.setId(44);
		vo1.setLabel("Insert Label");
		vo1.setCount(500);
		vo1.setLocked(false);
		long result = new CounterDao().insert(vo1);
		assertEquals(-1, result);
		
		
		CounterVo vo = new CounterDao().get(vo1.getId());
		assertEquals(44, vo.getId());
		assertEquals("SetUp Label", vo.getLabel());
		assertEquals(1000, vo.getCount());
		assertEquals(false, vo.isLocked());
	}
	
	public void testDeleteById() {
		
		new CounterDao().delete(44);
		CounterVo vo = new CounterDao().get(44);
		assertNull(vo);
	}
}
