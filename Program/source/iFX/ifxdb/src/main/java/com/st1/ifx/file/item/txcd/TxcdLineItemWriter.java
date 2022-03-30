package com.st1.ifx.file.item.txcd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.st1.ifx.file.item.CUDItemWriter;
import com.st1.ifx.file.item.LineHeader;
import com.st1.ifx.service.TxcdService;

@Component
public class TxcdLineItemWriter implements CUDItemWriter<TxcdLine> {
	static final Logger logger = LoggerFactory.getLogger(TxcdLineItemWriter.class);
	private TxcdService txcdService;

	@Autowired
	public void setTxcdService(@Qualifier("txcdService") TxcdService txcdService) {
		this.txcdService = txcdService;
	}

	@Override
	public void write(TxcdLine line) {
		logger.info("## write one");
		logger.info(line.toString());
		switch (line.getHeader().getAction()) {
		case LineHeader.ACTION_MERGE:
			txcdService.save(line.getTxcd());
			break;
		case LineHeader.ACTION_DELETE:
			txcdService.delete(line.getTxcd());
			break;
		case LineHeader.ACTION_REMOVE_ALL:
			txcdService.removeAll();
			break;
		default:
			break;
		}
	}

}
