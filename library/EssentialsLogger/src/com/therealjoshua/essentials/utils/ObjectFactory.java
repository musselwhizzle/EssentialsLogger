package com.therealjoshua.essentials.utils;

public interface ObjectFactory<T> {
	
	public T create();
	public void recycle(T object);
	
}