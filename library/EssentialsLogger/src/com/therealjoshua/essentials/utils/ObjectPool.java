package com.therealjoshua.essentials.utils;

import java.util.ArrayList;
import java.util.List;

public class ObjectPool<T> {
	
	private ObjectFactory<T> factory;
	private int poolSize = 10;
	
	private List<T> pool;
	
	public ObjectPool(ObjectFactory<T> factory, int poolSize) {
		this.factory = factory;
		this.poolSize = poolSize;
		this.pool = new ArrayList<T>();
	}
	
	public T getObject() {
		if (pool.size() > 0) return pool.remove(0);
		return factory.create();
	}
	
	public void recycle(T object) {
		if (pool.size() >= poolSize) {
			factory.recycle(object);
		} else {
			pool.add(object);
		}
	}	
}