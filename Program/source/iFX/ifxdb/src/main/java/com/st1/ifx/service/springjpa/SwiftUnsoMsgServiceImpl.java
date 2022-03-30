package com.st1.ifx.service.springjpa;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

import com.st1.ifx.domain.SwiftPrinter;
import com.st1.ifx.domain.SwiftUnsolicitedMsg;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.repository.SwiftPrinterRepository;
import com.st1.ifx.repository.SwiftUnsoMsgRepository;
import com.st1.ifx.service.SwiftUnsoMsgService;

@Service("swiftUnsoMsgService")
@Repository
@Transactional
public class SwiftUnsoMsgServiceImpl implements SwiftUnsoMsgService, InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(SwiftUnsoMsgServiceImpl.class);

	@Autowired
	private SwiftUnsoMsgRepository unsoRepos;

	@Autowired
	private SwiftPrinterRepository printerRepos;

	@PersistenceContext
	private EntityManager em;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
		Assert.assertNotNull(unsoRepos);
		Assert.assertNotNull(printerRepos);
	}

	@Override
	public void save(SwiftUnsolicitedMsg unso) {
		log.info(unso.toString());
		try {
			unsoRepos.save(unso);
		} catch (Exception ex) {
			log.warn("SwiftUnsolicitedMsg save :" + ex.getMessage());
		}

	}

	@Override
	public List<SwiftUnsolicitedMsg> get(String brno, String date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SwiftPrinter> getPrinters(String brn) {
		log.info("getPrinters :" + brn);
		List<SwiftPrinter> printList = null;
		try {
			printList = printerRepos.findByBrn(brn);
		} catch (Exception ex) {
			log.error("getPrinters error:" + ex.getMessage());
		}
		return printList;
	}

	// 更新列印次數
	@Override
	public void updataPrintTime(String brndept, String dt, String filename) {
		log.info(FilterUtils.escape("updataPrintTime:" + brndept + "," + dt + "," + filename));
		SwiftUnsolicitedMsg b = null;
		try {
			b = unsoRepos.findByBrndeptAndSrhdayAndFileName(brndept, dt, filename);
			// 更新列印的日期.時間和列印次數
			b.printTouch();
			this.unsoRepos.saveAndFlush(b);
		} catch (Exception ex) {
			log.error("updataPrintTime error:" + ex.getMessage());
		}
	}

	// 移動除了該日期之其他的未列印的電文稿
	@Override
	public void updateNotprintWithoutdt(String brndept, String dt) {
		log.info("updateNotprintWithoutdt:" + brndept + "," + dt);
		List<SwiftUnsolicitedMsg> list = null;
		try {
			list = unsoRepos.findByBrndeptAndPrintTimesAndSrhdayNot(brndept, 0, dt);
			for (SwiftUnsolicitedMsg b : list) {
				b.movetouch();
				log.info(FilterUtils.escape("updateNotprintWithoutdt movetouch:" + b.getBrndept() + "," + b.getSrhday()
						+ "," + b.getFileName()));
				this.unsoRepos.saveAndFlush(b);
			}
		} catch (Exception ex) {
			log.error("updateNotprintWithoutdt error :" + ex.getMessage());
		}
	}

	// 移動全部未列印的電文稿
	@Override
	public void updateNotprint() {
		log.info("updateNotprint!");
		List<SwiftUnsolicitedMsg> list = null;
		java.util.Date today = new java.util.Date();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		String stoday = sdf2.format(today);
		try {
			// 20180111 調整
			list = unsoRepos.findByPrintTimesAndSrhdayNot(0, stoday);
			for (SwiftUnsolicitedMsg b : list) {
				b.movetouch();
				log.info(FilterUtils.escape(
						"updateNotprint movetouch:" + b.getBrndept() + "," + b.getSrhday() + "," + b.getFileName()));
				log.info(FilterUtils.escape(b.toString()));
				this.unsoRepos.saveAndFlush(b);
			}
		} catch (Exception ex) {
			log.error("updateNotprint error :" + ex.getMessage());
		}
	}

	// Find swift lsit
	@Override
	public List<SwiftUnsolicitedMsg> findSwiftListbybrndttime(String brndept, String dt, int printTimes) {
		log.info(FilterUtils.escape("findSwiftListbybrndttime:" + brndept + "," + dt + "," + printTimes));
		List<SwiftUnsolicitedMsg> list = null;
		try {
			// list = unsoRepos.findByBrndeptAndSrhdayAndPrintTimes(brndept, dt,
			// printTimes);
			list = unsoRepos.findByBrndeptAndSrhdayAndPrintTimesOrderByIdAsc(brndept, dt, printTimes);// 潘加orderbyId
		} catch (Exception ex) {
			log.error("findSwiftListbybrndttime error :" + ex.getMessage());
		}
		return list;
	}

	@Override
	public List<SwiftUnsolicitedMsg> findSwiftListbybrndt(String brndept, String dt) {
		log.info(FilterUtils.escape("findSwiftListbybrndt:" + brndept + "," + dt));
		List<SwiftUnsolicitedMsg> list = null;
		try {
			list = unsoRepos.findByBrndeptAndSrhday(brndept, dt);
		} catch (Exception ex) {
			log.error("findSwiftListbybrndt error :" + ex.getMessage());
		}
		return list;
	}

	// 取得該列印位置
	@Override
	public String findPathByBrndeptSrhdayFilename(String brndept, String dt, String filename) {

		SwiftUnsolicitedMsg b = null;
		try {
			b = unsoRepos.findByBrndeptAndSrhdayAndFileName(brndept, dt, filename);
		} catch (Exception ex) {
			log.error(FilterUtils.escape("findPathByBrndeptSrhdayFilename error:" + ex.getMessage()));
		}
		if (b != null) {
			log.info(FilterUtils.escape(
					"findPathByBrndeptSrhdayFilename:" + brndept + "," + dt + "," + filename + "," + b.getFilePath()));
			return b.getFilePath();
		} else {
			log.warn(FilterUtils
					.escape("[NOT EXIST!]findPathByBrndeptSrhdayFilename:" + brndept + "," + dt + "," + filename));
			return null;
		}
	}
}
