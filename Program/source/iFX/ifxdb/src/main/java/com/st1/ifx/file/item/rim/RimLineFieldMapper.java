package com.st1.ifx.file.item.rim;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class RimLineFieldMapper implements FieldSetMapper<RimLine> {

	@Override
	public RimLine mapFieldSet(FieldSet fieldSet) throws BindException {
		RimLine r = new RimLine();
		r.setTableName(fieldSet.readString("tableName"));
		r.setUpdateFlag(fieldSet.readString("updateFlag"));
		r.setKey(fieldSet.readString("key"));
		r.setData(fieldSet.readString("data"));
		return r;

	}

}
