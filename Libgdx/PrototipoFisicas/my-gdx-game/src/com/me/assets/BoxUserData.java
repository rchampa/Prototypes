package com.me.assets;

public class BoxUserData {

	int collisionGroup;
	int boxId;

	public BoxUserData(int boxid, int collisiongroup) {
		this.boxId = boxid;
		this.collisionGroup = collisiongroup;
	}

	public void set(int boxid, int collisiongroup) {
		this.boxId = boxid;
		this.collisionGroup = collisiongroup;
	}

	public int getBoxId() {
		return this.boxId;
	}

	public int getCollisionGroup() {
		return this.collisionGroup;
	}
	
}
