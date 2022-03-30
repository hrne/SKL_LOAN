package com.st1.ifx.file.item.sbctl;

import com.st1.ifx.batch.item.MyCobolMapper;
import com.st1.util.cbl.CobolProcessor;

public class SbctlLineMapper implements MyCobolMapper<SbctlLine> {

	@Override
	public SbctlLine map(String line) throws Exception {
		SbctlLine sbctlLine = new SbctlLine();
		CobolProcessor.parse(line, sbctlLine);
		return sbctlLine;
	}

}
