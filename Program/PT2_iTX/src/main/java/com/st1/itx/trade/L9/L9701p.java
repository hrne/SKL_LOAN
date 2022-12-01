package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.springjpa.cm.L9701ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L9701p")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9701p extends TradeBuffer {

	@Autowired
	L9701ServiceImpl l9701ServiceImpl;

	@Autowired
	L9701Report l9701Report;

	@Autowired
	L9701Report2 l9701Report2;

	@Autowired
	L9701Report3 l9701Report3;

	@Autowired
	CdReportService sCdReportService;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9701p ");
		this.totaVo.init(titaVo);

		String formNo = "L9701";
		CdReport tCdReport = new CdReport();
		tCdReport = sCdReportService.findById(formNo, titaVo);
		String Fg = tCdReport.getLetterFg();
		if ("Y".equals(Fg)) {
		String parentTranCode = titaVo.getTxcd();

		l9701Report.setParentTranCode(parentTranCode);
		l9701Report2.setParentTranCode(parentTranCode);
		l9701Report3.setParentTranCode(parentTranCode);

		// 讀取VAR參數
		String iReportType = titaVo.getParam("ReportType").trim();

		List<BaTxVo> listBaTxVo = new ArrayList<>();

		if (!iReportType.equals("3")) {
			listBaTxVo = runBaTxCom(titaVo);
		}
			if (iReportType.equals("1") || iReportType.equals("9")) {
				l9701Report.exec(titaVo, listBaTxVo);
			}

			if (iReportType.equals("2") || iReportType.equals("9")) {
				l9701Report2.exec(titaVo, listBaTxVo);
			}

			if (iReportType.equals("3") || iReportType.equals("9")) {
				l9701Report3.exec(titaVo);
			}
		}
		String nowBc = dDateUtil.getNowStringBc();
		String tlrNo = titaVo.getTlrNo();

		webClient.sendPost(nowBc, "1800", tlrNo, "Y", "LC009", tlrNo + "L9701", "L9701客戶往來交易明細表已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private List<BaTxVo> runBaTxCom(TitaVo titaVo) throws LogicException {
		baTxCom.setTxBuffer(this.txBuffer);

		int entDy = titaVo.getEntDyI();

		int custNo = Integer.parseInt(titaVo.get("CustNo"));

		return baTxCom.settingUnPaid(entDy, custNo, 0, 0, 1, BigDecimal.ZERO, titaVo);

	}
}