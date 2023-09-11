package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdComm;
import com.st1.itx.db.domain.CdCommId;
import com.st1.itx.db.service.CdCommService;
import com.st1.itx.db.service.springjpa.cm.LM002ServiceImpl;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("LM002")
@Scope("step")
/**
 * LM002-房貸專案放款
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM002 extends BatchBase implements Tasklet, InitializingBean {

	/* DB服務注入 */
	@Autowired
	private CdCommService sCdCommService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Autowired
	LM002Report lm002report;

	@Autowired
	LM002ServiceImpl lM002ServiceImpl;

	TempVo tTempVo = new TempVo();

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return this.exec(contribution, "M");
	}

	public void run() throws LogicException {
		this.info("active LM002 ");
		String parentTranCode = titaVo.getTxcd();
		lm002report.setTxBuffer(this.getTxBuffer());
		lm002report.setParentTranCode(parentTranCode);
		checkProjectLoan("02");//專案放款產表時計算
		lm002report.exec(titaVo);
	}

	/**
	 * 檢查雜項代碼檔(CdComm) 是否有當月專案放款資料，沒有則系統query合計後新增進去。
	 */
	private void checkProjectLoan(String CdItem) throws LogicException {
		// 帳務日(西元)
		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日(西元)
		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();

		int iDate01 = mfbsdy / 100 * 100 + 1;

		// 判斷帳務日與月底日是否同一天
		if (tbsdy == mfbsdy) {

			CdCommId tCdCommId = new CdCommId();

			tCdCommId.setCdType("02");
			tCdCommId.setCdItem(CdItem);
			// 固定設定年月的1日(日期無作用，僅使用道年月)
			tCdCommId.setEffectDate(iDate01);

			CdComm sCdComm = sCdCommService.findById(tCdCommId, titaVo);

			// 資料庫是否已存在
			if (sCdComm != null) {

			} else {

				sCdComm = new CdComm();
				sCdComm.setCdCommId(tCdCommId);
				sCdComm = setCdcomField(sCdComm, iDate01 / 100, titaVo);
				sCdComm.setEnable("Y");
				sCdComm.setRemark("專案放款餘額");

				titaVo.keepOrgDataBase();// 保留原本記號

				try {

					sCdCommService.insert(sCdComm, titaVo);

				} catch (DBException e) {
					if (e.getErrorId() == 2) {
						throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
					} else {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				}

				titaVo.setDataBaseOnLine();// 指定連線環境

				try {

					sCdCommService.insert(sCdComm, titaVo);

				} catch (DBException e) {
					if (e.getErrorId() == 2) {
						throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
					} else {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				}

				titaVo.setDataBaseOnOrg();// 還原原本的環境
			}
		}

	}

	private CdComm setCdcomField(CdComm t, int yearMonth, TitaVo titaVo) throws LogicException {

		List<Map<String, String>> result = new ArrayList<>();
		try {
			result = lM002ServiceImpl.tmpProjectLoan(yearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM002ServiceImpl.findAll error = " + errors.toString());
		}

		tTempVo.putParam("YearMonth", yearMonth);
		//原始
		tTempVo.putParam("o340LoanBal", "0");
		tTempVo.putParam("oIALoanBal", "0");
		tTempVo.putParam("oIBLoanBal", "0");
		tTempVo.putParam("oICLoanBal", "0");
		tTempVo.putParam("oIDLoanBal", "0");
		tTempVo.putParam("oIELoanBal", "0");
		tTempVo.putParam("oIFLoanBal", "0");
		tTempVo.putParam("oIGLoanBal", "0");
		tTempVo.putParam("oIHLoanBal", "0");
		tTempVo.putParam("oIILoanBal", "0");
		tTempVo.putParam("o921LoanBal", "0");
		tTempVo.putParam("o990LoanBal", "0");
		tTempVo.putParam("oLoanBal", "0");

		//調整後
		tTempVo.putParam("340LoanBal", "0");
		tTempVo.putParam("IALoanBal", "0");
		tTempVo.putParam("IBLoanBal", "0");
		tTempVo.putParam("ICLoanBal", "0");
		tTempVo.putParam("IDLoanBal", "0");
		tTempVo.putParam("IELoanBal", "0");
		tTempVo.putParam("IFLoanBal", "0");
		tTempVo.putParam("IGLoanBal", "0");
		tTempVo.putParam("IHLoanBal", "0");
		tTempVo.putParam("IILoanBal", "0");
		tTempVo.putParam("921LoanBal", "0");
		tTempVo.putParam("990LoanBal", "0");
		tTempVo.putParam("88LoanBal", "0");//88風災另外寫
		tTempVo.putParam("LoanBal", "0");
		
		for (Map<String, String> r : result) {

			tTempVo.putParam("o"+r.get("Type") + "LoanBal", r.get("LoanBal"));
			tTempVo.putParam(r.get("Type") + "LoanBal", r.get("LoanBal"));
		}

		t.setJsonFields(tTempVo.getJsonString());

		return t;
	}

}
