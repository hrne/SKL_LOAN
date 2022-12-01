package com.st1.itx.util.common;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("reportCom")
@Scope("prototype")
/**
 * 報表執行程式<BR>
 * executeReports(titaVo) called by L9801, L9802, L9803, L9804, L9805, L9806<BR>
 * 
 * 讀取 TitaVo 後，依序執行在前端勾選的所有報表程式<BR>
 * 如程式的 bean scope 為 prototype，跳出新頁籤要求使用者輸入參數<BR>
 * 其餘者，直接設為 BatchJob 執行。<BR>
 * 
 * @author Xiang Wei Huang
 *
 */
public class ReportCom extends CommBuffer {

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	CdReportService cdReportService;

	@Autowired
	public WebClient webClient;

	@Autowired
	ConfigurableApplicationContext applicationContext;

	@Autowired
	Parse parse;


	public BeanDefinition getBean(String beanName) {
		try {
			return applicationContext.getBeanFactory().getBeanDefinition(beanName);
		} catch (Exception e) {
			this.error("ReportCom.getBean() - Bean not found: " + beanName);
			return null;
		}
	}

	private Boolean needInput(String beanName) {
		try {
			// scope為prototype時, 需要輸入參數
			return "prototype".equals(getBean(beanName).getScope());
		} catch (Exception e) {
			this.error("ReportCom.needInput() - " + beanName + " getScope() failed, return true");
			this.error(e.toString());

			// false會直接進batchjob
			// 丟true, 讓Webpost照常跳通知, 但點擊後會跳出查無該程式
			return true;
		}
	}

	private class NeedInputJob {
		String code = "";
		String name = "";

		public NeedInputJob(String code, String name) {
			this.code = code;
			this.name = name;
		}
	}

	/**
	 * 批量執行報表程式<BR>
	 * 參考：L9801, L9802, L9803, L9804, L9805, L9806
	 * 
	 * @param titaVo titaVo
	 * @param txcd   呼叫此函數的報表代號（顯示用）
	 * @throws LogicException ..
	 */
	public void executeReports(TitaVo titaVo, String txcd) throws LogicException {
		this.info("ReportCom: activated by " + txcd);

		StringBuilder backgroundJobs = new StringBuilder();
		Queue<NeedInputJob> needInputJobs = new LinkedList<NeedInputJob>();

		int totalItem = parse.stringToInteger(titaVo.getParam("TotalItem"));

		for (int i = 1; i <= totalItem; i++) {

			// Put jobs into related job queue
			if (!titaVo.getParam("BtnShell" + i).trim().isEmpty()) {
				String tradeCode = titaVo.getParam("TradeCode" + i);
				String tradeName = "";

				// get tradeName
				CdReport form = cdReportService.FormNoFirst(tradeCode, titaVo);
				if (form != null) {
					tradeName = cdReportService.FormNoFirst(tradeCode, titaVo).getFormName();
				}

				if (needInput(tradeCode)) {
					// send notice through website
					this.info("ReportCom: adding NeedInputJob " + tradeCode + "(" + txcd + ")");
					needInputJobs.add(new NeedInputJob(tradeCode, tradeName));
				} else {
					// add into batch job
					// batchJob format: j[BEANNAME];j[BEANNAME];...;j[BEANNAME]
					this.info("ReportCom: adding BatchJob j" + tradeCode + "(" + txcd + ")");
					if(!backgroundJobs.toString().contains(tradeCode)){
						backgroundJobs.append("j" + tradeCode + ";");
					}

				}
			}
		}

		// run batchJob
		if (backgroundJobs.length() > 0) {
			this.info("ReportCom: executing BatchJobs (" + txcd + ")");

			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), backgroundJobs.length() / 7 + " 支報表正在背景產製，完成後可於＂報表及製檔＂存取", titaVo);
			// jL0001;jL0002;jL0003...
			// each job is 7 chars long, hence /7

			backgroundJobs.setLength(backgroundJobs.length() - 1); // delete out the last ; symbol.
			titaVo.setBatchJobId(backgroundJobs.toString());
		}

		if (needInputJobs.size() > 0) {
			this.info("ReportCom: sending popups about NeedInputJobs (" + txcd + ")");

			// send popup about reports that need further input
			// after batchJob to make sure that users click through jobs that need inputs
			// first
			for (NeedInputJob j : needInputJobs) {
				webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", j.code, titaVo.getParam("TLRNO"), j.code + j.name + "須填寫查詢條件", titaVo);
			}
		}

		this.info("ReportCom: we're done here! (" + txcd + ")");
	}

	@Override
	public void exec() throws LogicException {
		// call methods above instead.
	}
}