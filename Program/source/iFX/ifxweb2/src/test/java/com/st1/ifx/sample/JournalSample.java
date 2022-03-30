package com.st1.ifx.sample;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.st1.ifx.domain.Journal;
import com.st1.ifx.service.JournalService;
import com.st1.util.MySpring;

public class JournalSample {
    static GenericXmlApplicationContext ctx;

    /**
     * @param args
     */
    public static void main(String[] args) {
	ctx = new GenericXmlApplicationContext();
	ctx.load("classpath:app-context.xml");
	ctx.refresh();

	testWriteJournal();
    }

    private static void testWriteJournal() {
	String busDate = "20100103";
	String brn = "5050";
	String tlrno = "00201";
	String txcode = "G0777";
	JournalService jnlService = MySpring.getJournalService();
	int lastSeq = jnlService.getLastSeq(busDate, brn, tlrno);
	Journal jnl = makeJournal(busDate, brn, tlrno, txcode, lastSeq);

	jnl = jnlService.save(jnl);
	System.out.println("jnl id:" + jnl.getId());

    }

    static Journal makeJournal(String busDate, String brn, String tlrno, String txcode, int lastSeq) {

	Journal jnl = new Journal();

//		System.out.println("lastSeq:"+lastSeq);
//		jnl.setJnlSeq(lastSeq + 1);
//		jnl.setBrn(brn);
//		jnl.setBusDate(busDate);
//		jnl.setTlrno(tlrno);
//		jnl.setTxcode(txcode);
//		jnl.setTita("tita tita tita");
//		jnl.setFields("fields fields fields".getBytes());
	return jnl;
    }

}
