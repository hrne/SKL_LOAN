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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.domain.JcicZ571Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ571LogService;
import com.st1.itx.db.service.JcicZ571Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8435")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業(571)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8435 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ571Service sJcicZ571Service;
	@Autowired
	public JcicZ571LogService sJcicZ571LogService;

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
		Slice<JcicZ571> iJcicZ571 = null;
		JcicZ571 uJcicZ571 = new JcicZ571();
		JcicZ571 oldJcicZ571 = new JcicZ571();
		iJcicZ571 = sJcicZ571Service.findAll(0,Integer.MAX_VALUE, titaVo);
		for (JcicZ571 iiJcicZ571 : iJcicZ571) {
			if (iiJcicZ571.getOutJcicTxtDate() == iJcicDate) {
				count++;
				uJcicZ571 = sJcicZ571Service.holdById(iiJcicZ571.getJcicZ571Id(), titaVo);
				oldJcicZ571 = (JcicZ571) iDataLog.clone(uJcicZ571);
				uJcicZ571.setOutJcicTxtDate(0);
				try {
					sJcicZ571Service.update(uJcicZ571, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
				}
                JcicZ571Log iJcicZ571Log = sJcicZ571LogService.ukeyFirst(uJcicZ571.getUkey(), titaVo);
				JcicZ571 cJcicZ571 = sJcicZ571Service.ukeyFirst(uJcicZ571.getUkey(), titaVo);
				CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ571.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);			
				iDataLog.setEnv(titaVo, oldJcicZ571, uJcicZ571);
				iDataLog.exec("L8435取消報送",iJcicZ571Log.getUkey()+iJcicZ571Log.getTxSeq());
			}
		}
		if (count == 0) {
			throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
		}
	}
}