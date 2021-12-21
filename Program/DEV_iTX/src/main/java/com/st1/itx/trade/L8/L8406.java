package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.service.JcicZ043LogService;
import com.st1.itx.db.service.JcicZ043Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8406")
@Scope("prototype")
/**
 * 同意報送例外處裡檔案(043)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8406 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;

	@Autowired
	public L8403File iL8403File;

	@Autowired
	public JcicZ043Service sJcicZ043Service;
	@Autowired
	public JcicZ043LogService sJcicZ043LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);

		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));

		switch (iSubmitType) {
		case 1:
			doFile(titaVo);
			break;
		case 2:
			doRemoveJcicDate(titaVo);
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void doFile(TitaVo titaVo) throws LogicException {

		String iSubmitKey = titaVo.getParam("SubmitKey");
		String iReportDate = titaVo.getParam("ReportDate");
		String iTranCode = StringUtils.leftPad(titaVo.getParam("TranCode"), 3, '0');

		// 檔名
		// BBBMMDDS.XXX 金融機構總行代號+月份+日期+次數.檔案類別
		String fileNname = iSubmitKey + iReportDate.substring(3) + "." + iTranCode;

		iL8403File.exec(titaVo);
		long fileNo = iL8403File.close();
		iL8403File.toFile(fileNo, fileNname);

	}

	public void doRemoveJcicDate(TitaVo titaVo) throws LogicException {
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int count = 0;
		Slice<JcicZ043> iJcicZ043 = null;
		JcicZ043 uJcicZ043 = new JcicZ043();
		JcicZ043 oldJcicZ043 = new JcicZ043();
		iJcicZ043 = sJcicZ043Service.findAll(this.index, this.limit, titaVo);
		for (JcicZ043 iiJcicZ043 : iJcicZ043) {
			if (iiJcicZ043.getOutJcicTxtDate() == iJcicDate) {
				count++;
				uJcicZ043 = sJcicZ043Service.holdById(iiJcicZ043.getJcicZ043Id(), titaVo);
				oldJcicZ043 = (JcicZ043) iDataLog.clone(uJcicZ043);
				uJcicZ043.setOutJcicTxtDate(0);
				try {
					sJcicZ043Service.update(uJcicZ043, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
				}
				iDataLog.setEnv(titaVo, oldJcicZ043, uJcicZ043);
				iDataLog.exec();
			}
		}
		if (count == 0) {
			throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
		}
	}
}