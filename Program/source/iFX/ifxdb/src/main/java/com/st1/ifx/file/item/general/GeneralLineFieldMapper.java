package com.st1.ifx.file.item.general;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class GeneralLineFieldMapper implements FieldSetMapper<GeneralLine> {

	@Override
	public GeneralLine mapFieldSet(FieldSet fieldSet) throws BindException {
		GeneralLine g = new GeneralLine();
		g.setId(fieldSet.readString("id"));
		g.setOpfg(fieldSet.readString("opfg"));
		g.setKi(fieldSet.readString("key"));
		g.setContent(fieldSet.readString("content"));
		return g;
	}

}
