package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R39")
@Scope("prototype")
/**
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8R39 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ440Service iJcicZ440Service;
	@Autowired
	public CdCodeService iCdCodeService;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r39 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ440 iJcicZ440 = new JcicZ440();
		iJcicZ440 = iJcicZ440Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ440 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			String jcicBankCode = "";
			CdCode tCdCode = new CdCode();

			totaVo.putParam("L8r39TranKey", iJcicZ440.getTranKey());
			totaVo.putParam("L8r39CustId", iJcicZ440.getCustId());
			totaVo.putParam("L8r39SubmitKey", iJcicZ440.getSubmitKey());
			totaVo.putParam("L8r39ApplyDate", iJcicZ440.getApplyDate());
			totaVo.putParam("L8r39CourtCode", iJcicZ440.getCourtCode());
			totaVo.putParam("L8r39AgreeDate", iJcicZ440.getAgreeDate());
			totaVo.putParam("L8r39StartDate", iJcicZ440.getStartDate());
			totaVo.putParam("L8r39RemindDate", iJcicZ440.getRemindDate());
			totaVo.putParam("L8r39ApplyType", iJcicZ440.getApplyType());
			totaVo.putParam("L8r39ReportYn", iJcicZ440.getReportYn());

			totaVo.putParam("L8r39NotBankId1", iJcicZ440.getNotBankId1());
			jcicBankCode = iJcicZ440.getNotBankId1();
			if (jcicBankCode.trim().isEmpty() || jcicBankCode.equals("")) {
				totaVo.putParam("L8r39NotBankIdX1", "");
			} else {
				tCdCode = iCdCodeService.getItemFirst(8, "JcicBankCode", jcicBankCode, titaVo);
				if (tCdCode != null) {
					totaVo.putParam("L8r39NotBankIdX1", tCdCode.getItem());
				} else {
					totaVo.putParam("L8r39NotBankIdX1", "");
				}
			}
			totaVo.putParam("L8r39NotBankId2", iJcicZ440.getNotBankId2());
			jcicBankCode = iJcicZ440.getNotBankId2();
			if (jcicBankCode.trim().isEmpty() || jcicBankCode.equals("")) {
				totaVo.putParam("L8r39NotBankIdX2", "");
			} else {
				tCdCode = iCdCodeService.getItemFirst(8, "JcicBankCode", jcicBankCode, titaVo);
				if (tCdCode != null) {
					totaVo.putParam("L8r39NotBankIdX2", tCdCode.getItem());
				} else {
					totaVo.putParam("L8r39NotBankIdX2", "");
				}
			}
			totaVo.putParam("L8r39NotBankId3", iJcicZ440.getNotBankId3());
			jcicBankCode = iJcicZ440.getNotBankId3();
			if (jcicBankCode.trim().isEmpty() || jcicBankCode.equals("")) {
				totaVo.putParam("L8r39NotBankIdX3", "");
			} else {
				tCdCode = iCdCodeService.getItemFirst(8, "JcicBankCode", jcicBankCode, titaVo);
				if (tCdCode != null) {
					totaVo.putParam("L8r39NotBankIdX3", tCdCode.getItem());
				} else {
					totaVo.putParam("L8r39NotBankIdX3", "");
				}
			}
			totaVo.putParam("L8r39NotBankId4", iJcicZ440.getNotBankId4());
			jcicBankCode = iJcicZ440.getNotBankId4();
			if (jcicBankCode.trim().isEmpty() || jcicBankCode.equals("")) {
				totaVo.putParam("L8r39NotBankIdX4", "");
			} else {
				tCdCode = iCdCodeService.getItemFirst(8, "JcicBankCode", jcicBankCode, titaVo);
				if (tCdCode != null) {
					totaVo.putParam("L8r39NotBankIdX4", tCdCode.getItem());
				} else {
					totaVo.putParam("L8r39NotBankIdX4", "");
				}
			}
			totaVo.putParam("L8r39NotBankId5", iJcicZ440.getNotBankId5());
			jcicBankCode = iJcicZ440.getNotBankId5();
			if (jcicBankCode.trim().isEmpty() || jcicBankCode.equals("")) {
				totaVo.putParam("L8r39NotBankIdX5", "");
			} else {
				tCdCode = iCdCodeService.getItemFirst(8, "JcicBankCode", jcicBankCode, titaVo);
				if (tCdCode != null) {
					totaVo.putParam("L8r39NotBankIdX5", tCdCode.getItem());
				} else {
					totaVo.putParam("L8r39NotBankIdX5", "");
				}
			}
			totaVo.putParam("L8r39NotBankId6", iJcicZ440.getNotBankId6());
			jcicBankCode = iJcicZ440.getNotBankId6();
			if (jcicBankCode.trim().isEmpty() || jcicBankCode.equals("")) {
				totaVo.putParam("L8r39NotBankIdX6", "");
			} else {
				tCdCode = iCdCodeService.getItemFirst(8, "JcicBankCode", jcicBankCode, titaVo);
				if (tCdCode != null) {
					totaVo.putParam("L8r39NotBankIdX6", tCdCode.getItem());
				} else {
					totaVo.putParam("L8r39NotBankIdX6", "");
				}
			}
			totaVo.putParam("L8r39OutJcicTxtDate", iJcicZ440.getOutJcicTxtDate());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}