package com.musselwhizzle.tapcounter.vos;


public class CounterVo extends SimpleObservable<CounterVo> {
	
	private int id = -1;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
		notifyObservers(this);
	}
	
	private int count = 0;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
		notifyObservers(this);
	}
	
	private String label = "";
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
		notifyObservers(this);
	}
	
	private boolean locked = false;
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
		notifyObservers(this);
	}
	
	@Override
	synchronized public CounterVo clone() {
		CounterVo vo = new CounterVo();
		vo.setId(id);
		vo.setLabel(label);
		vo.setCount(count);
		vo.setLocked(locked);
		return vo;
	}
	
	synchronized public void consume(CounterVo vo) {
		this.id = vo.getId();
		this.label = vo.getLabel();
		this.count = vo.getCount();
		this.locked = vo.isLocked();
		notifyObservers(this);
	}
}
