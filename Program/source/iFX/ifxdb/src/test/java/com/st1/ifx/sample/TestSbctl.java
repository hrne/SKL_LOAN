package com.st1.ifx.sample;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.st1.ifx.domain.Sbctl;
import com.st1.ifx.service.SbctlService;
import com.st1.util.cbl.CobolProcessor;

public class TestSbctl {
	static GenericXmlApplicationContext ctx;
	static String line1 = "SBCTL   0050500000I    20110104150505050150500$";
	static String line2 = "SBCTL   0050500000J    20110104150505050150500$";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		loadContext();

		Sbctl sbctl = parse(line1);
		save(sbctl);
	}

	private static void save(Sbctl sbctl) {
		System.out.println("IN  Sbctl save!");
		SbctlService service = ctx.getBean("sbctlService", SbctlService.class);
		sbctl =  service.save(sbctl);
		Sbctl sb2 = service.findById(sbctl.getType(), sbctl.getBrno(), sbctl.getTlrno(), sbctl.getSbtyp());
		System.out.println(sb2);

	}

	private static void loadContext() {
		String[] xmls = { "classpath:ifx-database-jpa.xml",
				"classpath:ifx-batch-context.xml" };
		ctx = new GenericXmlApplicationContext();
		ctx.load(xmls);

		ctx.refresh();
	}

	private static Sbctl parse(String line) {
		System.out.println("IN  Sbctl parse!");
		line = line.substring(9);
		Sbctl sbctl = new Sbctl();
		try {
			CobolProcessor.parse(line, sbctl);
			System.out.println(sbctl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sbctl;

	}

}
