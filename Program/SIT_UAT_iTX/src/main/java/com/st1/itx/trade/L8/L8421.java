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
import com.st1.itx.db.domain.JcicZ061;
import com.st1.itx.db.service.JcicZ061LogService;
import com.st1.itx.db.service.JcicZ061Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8421")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業(061)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8421 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;

	@Autowired
	public L8403File iL8403File;

	@Autowired
	public JcicZ061Service sJcicZ061Service;
	@Autowired
	public JcicZ061LogService sJcicZ061LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);

		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		long sno1 = 0;
		switch (iSubmitType) {
		case 1:
			sno1 = doFile(titaVo);
			break;
		case 2:
			doRemoveJcicDate(titaVo);
			break;
		}
		totaVo.put("ExcelSnoM", "" + sno1);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public long doFile(TitaVo titaVo) throws LogicException {

		String iSubmitKey = titaVo.getParam("SubmitKey");
		String iReportDate = titaVo.getParam("ReportDate");
		String iTranCode = StringUtils.leftPad(titaVo.getParam("TranCode"), 3, '0');

		// 檔名
		// BBBMMDDS.XXX 金融機構總行代號+月份+日期+次數.檔案類別
		String fileNname = iSubmitKey + iReportDate.substring(3) + "." + iTranCode;
		this.info("檔名=" + fileNname);

		iL8403File.exec(titaVo);
		long fileNo = iL8403File.close();
		iL8403File.toFile(fileNo, fileNname);
		return fileNo;
	}

	public void doRemoveJcicDate(TitaVo titaVo) throws LogicException {
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		this.info("ReportDate" + iJcicDate);
		int count = 0;
		Slice<JcicZ061> iJcicZ061 = null;
		JcicZ061 uJcicZ061 = new JcicZ061();
		JcicZ061 oldJcicZ061 = new JcicZ061();
		iJcicZ061 = sJcicZ061Service.findAll(this.index, this.limit, titaVo);
		for (JcicZ061 iiJcicZ061 : iJcicZ061) {
			if (iiJcicZ061.getOutJcicTxtDate() == iJcicDate) {
				count++;
				uJcicZ061 = sJcicZ061Service.holdById(iiJcicZ061.getJcicZ061Id(), titaVo);
				oldJcicZ061 = (JcicZ061) iDataLog.clone(uJcicZ061);
				uJcicZ061.setOutJcicTxtDate(0);
				try {
					sJcicZ061Service.update(uJcicZ061, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
				}
				iDataLog.setEnv(titaVo, oldJcicZ061, uJcicZ061);
				iDataLog.exec();
			}
		}
		if (count == 0) {
			throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
		}
	}
}