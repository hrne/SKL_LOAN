package com.st1.ifx.file.item.txcd;

import com.st1.ifx.batch.item.MyCobolMapper;
import com.st1.util.cbl.CobolProcessor;

public class TxcdLineMapper implements MyCobolMapper<TxcdLine> {

	@Override
	public TxcdLine map(String line) throws Exception {
		TxcdLine txcdLine = new TxcdLine();
		txcdLine.getTxcd().initOccurs();
		CobolProcessor.parse(line, txcdLine);
		txcdLine.getTxcd().occurs2List();
		return txcdLine;
	}

}
