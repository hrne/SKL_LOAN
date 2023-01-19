package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.codehaus.jettison.json.JSONString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * JsonDoubleCom
 *
 * @author Wei
 * @version 1.0.0
 */
@Service
@Scope("prototype")
public class JsonDoubleCom implements JSONString {
	private double value;

	public JsonDoubleCom(double value) {
		this.value = value;
	}

	public JsonDoubleCom(BigDecimal value) {
		this.value = value.doubleValue();
	}

	public String toJSONString() {
		DecimalFormat df = new DecimalFormat("0.####");
		return df.format(value);
	}
}
