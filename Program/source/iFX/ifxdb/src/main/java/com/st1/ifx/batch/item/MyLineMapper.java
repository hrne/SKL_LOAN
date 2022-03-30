package com.st1.ifx.batch.item;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.InitializingBean;
import org.junit.Assert;

public class MyLineMapper<T> implements LineMapper<T>, InitializingBean, InitialContextFactory {
	private MyCobolMapper<T> mapper;

	@SuppressWarnings("unchecked")
	public void setMapper(MyCobolMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull("The Mapper must be set", mapper);

	}

	@Override
	public T mapLine(String line, int lineNumber) throws Exception {
		try {
			return mapper.map(line);
		} catch (Exception ex) {
			throw new FlatFileParseException("Parsing error at line: " + lineNumber + ", input=[" + line + "]", ex, line, lineNumber);
		}
	}

}
