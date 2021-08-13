package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM028;
import com.st1.itx.db.service.MonthlyLM028Service;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LM028Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM028Report.class);

	@Autowired
	MonthlyLM028Service sMonthlyLM028Service;

	@Autowired
	MakeExcel makeExcel;

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LM028Report exec start ...");
		this.info("LM028Report exec DB = " + titaVo.getParam(ContentName.dataBase));

		int iTbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		this.info("LM028Report exec iTbsdyf = " + iTbsdyf);

		this.info("LM028Report exec Usp begin.");
		try {
		sMonthlyLM028Service.Usp_L9_MonthlyLM028_Upd(iTbsdyf, titaVo.getEmpNos(), titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("sMonthlyLM028Service Usp_L9_MonthlyLM028_Upd error = " + errors.toString());
		}
		this.info("LM028Report exec Usp end.");

		Slice<MonthlyLM028> sMonthlyLM028 = null;

		try {
			sMonthlyLM028 = sMonthlyLM028Service.findAll(0, Integer.MAX_VALUE, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("sMonthlyLM028Service findAll error = " + errors.toString());
		}

		List<MonthlyLM028> lMonthlyLM028 = sMonthlyLM028 == null ? null : sMonthlyLM028.getContent();

		exportExcel(titaVo, lMonthlyLM028);
	}

	private void exportExcel(TitaVo titaVo, List<MonthlyLM028> lMonthlyLM028) throws LogicException {
		this.info("LM028Report exportExcel start ...");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM028", "預估現金流量", "LM028預估現金流量", "預估現金流量.xlsx",
				"DEL5");
		if (lMonthlyLM028 == null || lMonthlyLM028.size() == 0) {
			makeExcel.setValue(2, 1, "本日無資料");
		}
		else {
		// 有標題列,從第2列開始塞值
		int row = 2;

		for (MonthlyLM028 tMonthlyLM028 : lMonthlyLM028) {

			makeExcel.setValue(row, 1, tMonthlyLM028.getLMSSTS());
			makeExcel.setValue(row, 2, tMonthlyLM028.getCUSENT());
			makeExcel.setValue(row, 3, tMonthlyLM028.getCUSBRH());
			makeExcel.setValue(row, 4, tMonthlyLM028.getLMSACN());
			makeExcel.setValue(row, 5, tMonthlyLM028.getLMSAPN());
			makeExcel.setValue(row, 6, tMonthlyLM028.getLMSASQ());
			makeExcel.setValue(row, 7, tMonthlyLM028.getIRTRAT(), "###0.0000");
			makeExcel.setValue(row, 8, tMonthlyLM028.getLMSISC());
			makeExcel.setValue(row, 9, tMonthlyLM028.getLMSPBK());
			makeExcel.setValue(row, 10, tMonthlyLM028.getAPLMON());
			makeExcel.setValue(row, 11, tMonthlyLM028.getAPLDAY());
			makeExcel.setValue(row, 12, tMonthlyLM028.getLMSLBL(), "###0");
			makeExcel.setValue(row, 13, tMonthlyLM028.getAILIRT());
			makeExcel.setValue(row, 14, tMonthlyLM028.getPOSCDE());
			makeExcel.setValue(row, 15, tMonthlyLM028.getLMSPDY());
			makeExcel.setValue(row, 16, tMonthlyLM028.getIRTFSC());
			makeExcel.setValue(row, 17, tMonthlyLM028.getIRTBCD());
			makeExcel.setValue(row, 18, tMonthlyLM028.getIRTRATYR1(), "###0.0000");
			makeExcel.setValue(row, 19, tMonthlyLM028.getIRTRATYR2(), "###0.0000");
			makeExcel.setValue(row, 20, tMonthlyLM028.getIRTRATYR3(), "###0.0000");
			makeExcel.setValue(row, 21, tMonthlyLM028.getIRTRATYR4(), "###0.0000");
			makeExcel.setValue(row, 22, tMonthlyLM028.getIRTRATYR5(), "###0.0000");
			makeExcel.setValue(row, 23, tMonthlyLM028.getGDRID1());
			makeExcel.setValue(row, 24, tMonthlyLM028.getGDRID2());
			makeExcel.setValue(row, 25, tMonthlyLM028.getYYYY());
			makeExcel.setValue(row, 26, tMonthlyLM028.getMONTH());
			makeExcel.setValue(row, 27, tMonthlyLM028.getDAY());
			makeExcel.setValue(row, 28, tMonthlyLM028.getW08CDE());
			makeExcel.setValue(row, 29, tMonthlyLM028.getRELATION());
			makeExcel.setValue(row, 30, tMonthlyLM028.getDPTLVL());
			makeExcel.setValue(row, 31, tMonthlyLM028.getACTFSC());

			row++;
		}
		}
		for (int i = 1; i <= 31; i++) {
			makeExcel.setWidth(i, 15);
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
