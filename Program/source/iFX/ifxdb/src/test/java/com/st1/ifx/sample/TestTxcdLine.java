package com.st1.ifx.sample;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.st1.ifx.domain.Txcd;
import com.st1.ifx.file.item.txcd.TxcdLine;
import com.st1.ifx.service.TxcdService;
import com.st1.util.cbl.CobolProcessor;

public class TestTxcdLine {
	static GenericXmlApplicationContext ctx;
	static TxcdService txcdService;
	static String line1 = "TXCD    0G0210201101012G2        目前牌告利率查詢                                                                                                        0000100000010000001000000100000010000001000000100000000000000000000000000011111000000$";

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		initSpring();
		
		Txcd txcd = extractLine(line1);
//		store(txcd);
//		readOne("A0020");
	}

	private static void readOne(String txcode) {
		Txcd txcd = txcdService.findById(txcode);
		//System.out.println(txcd);
		
	}

	private static Txcd extractLine(String line) throws Exception {
		TxcdLine txcdFile = new TxcdLine();
		Txcd txcd = txcdFile.getTxcd();
		txcd.initOccurs();
		CobolProcessor.parse(line, txcdFile);
		txcd.occurs2List();
		System.out.println("IN Txcd extractLine!");
		System.out.println(txcd.getGrbrfgList().get(0));
		System.out.println(txcd.getGrbrfgList().get(9));
		return txcd;
	}

	private static void initSpring() {
		ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:ifx-all-context.xml");
		ctx.refresh();
		txcdService = ctx.getBean("txcdService", TxcdService.class);
	}
	
	private static void store(Txcd txcd) {
		txcdService.save(txcd);
		
	}

}
