package com.st1.itx.util.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

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
	public WebClient webClient;

	@Autowired
	ConfigurableApplicationContext applicationContext;

	private Boolean needInput(String beanName) {
		try {
			// scope為prototype時, 需要輸入參數
			return "prototype".equals(applicationContext.getBeanFactory().getBeanDefinition(beanName).getScope());
		} catch (Exception e) {
			this.error("Bean not found: " + beanName);
			this.error(e.toString());

			// false會直接進batchjob
			// 丟true, 讓Webpost照常跳通知, 但點擊後會跳出查無該程式
			return true;
		}
	}

	public void executeReports(TitaVo titaVo, String txcd) throws LogicException {
		this.info("ReportCom: active " + txcd);

		String job = "";
		int totalItem = Integer.parseInt(titaVo.getParam("TotalItem"));

		for (int i = 1; i <= totalItem; i++) {

			// if it's checked
			if (titaVo.getParam("BtnShell" + i).equals("V")) {
				String tradeCode = titaVo.getParam("TradeCode" + i);
				String tradeName = titaVo.getParam("TradeName" + i);

				if (needInput(tradeCode)) {
					// send notice through website
					webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", tradeCode,
							titaVo.getParam("TLRNO"), tradeCode + tradeName + "須填寫查詢條件", titaVo);
				} else {
					// add into batch job
					this.info(txcd + " adds job j" + tradeCode);

					job += ";j" + tradeCode;

				}
			}
		}

		// run batchJob
		if (!job.equals("")) {
			titaVo.setBatchJobId(job.substring(1));

			// ;jL1111;jL2222...
			// hence 7
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), job.length() / 7 + " 支報表正在背景產製，完成後可於＂報表及製檔＂存取", titaVo);
		}
	}

	@Override
	public void exec() throws LogicException {
		// call methods above instead.
	}
}