package com.st1.ifx.batch.item;

public interface MyCobolMapper<T> {
	public T map(String line) throws Exception;
}
