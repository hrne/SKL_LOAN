package com.st1.itx.trade.LC;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.*;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.db.service.TxHolidayService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.filter.FilterUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

import com.st1.itx.util.data.DataLog;

import com.st1.itx.db.service.springjpa.cm.TableColumnServiceImpl;

@Service("LC998")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC998 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(LC998.class);

	@Autowired
	public TableColumnServiceImpl tableColumnServiceImpl;

	@Autowired
	public TxHolidayService sTxHolidayService;

	@Autowired
	public TxTranCodeService txTranCodeService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC998 ");
		this.totaVo.init(titaVo);

		try {
			if ("0".equals(titaVo.getParam("LOGFG"))) {
				if (System.getProperty("iTXPort") != null && System.getProperty("iTXPort").equals("7005"))
					this.load(System.getProperty("itx_Config") + "/logback0_7005.xml");
				else
					this.load(System.getProperty("itx_Config") + "/logback0.xml");
			} else if ("1".equals(titaVo.getParam("LOGFG"))) {
				if (System.getProperty("iTXPort") != null && System.getProperty("iTXPort").equals("7005"))
					this.load(System.getProperty("itx_Config") + "/logback_7005.xml");
				else
					this.load(System.getProperty("itx_Config") + "/logback.xml");
			}
		} catch (Throwable t) {
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throw new LogicException("CE000", "Log設定失敗!!");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void load(String externalConfigFileLocation) throws IOException, JoranException {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		File externalConfigFile = new File(FilterUtils.filter(externalConfigFileLocation));
		if (!externalConfigFile.exists()) {
			throw new IOException("Logback External Config File Parameter does not reference a file that exists");
		} else {
			if (!externalConfigFile.isFile()) {
				throw new IOException("Logback External Config File Parameter exists, but does not reference a file");
			} else {
				if (!externalConfigFile.canRead()) {
					throw new IOException("Logback External Config File exists and is a file, but cannot be read.");
				} else {
					JoranConfigurator configurator = new JoranConfigurator();
					configurator.setContext(lc);
					lc.reset();
					configurator.doConfigure(externalConfigFileLocation);
					StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
				}
			}
		}
	}

}