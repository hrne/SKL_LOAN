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
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.domain.JcicZ044Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ044LogService;
import com.st1.itx.db.service.JcicZ044Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8407")
@Scope("prototype")
/**
 * 請求同意債務清償方案通知資料(044)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8407 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;

	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public JcicZ044Service sJcicZ044Service;
	@Autowired
	public JcicZ044LogService sJcicZ044LogService;

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

		iL8403File.exec(titaVo);
		long fileNo = iL8403File.close();
		iL8403File.toFile(fileNo, fileNname);
		return fileNo;
	}

	public void doRemoveJcicDate(TitaVo titaVo) throws LogicException {
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int count = 0;
		Slice<JcicZ044> iJcicZ044 = null;
		JcicZ044 uJcicZ044 = new JcicZ044();
		JcicZ044 oldJcicZ044 = new JcicZ044();
		iJcicZ044 = sJcicZ044Service.findAll(0,Integer.MAX_VALUE, titaVo);
		String iCustId = titaVo.getParam("CustId");// 債務人IDN
		for (JcicZ044 iiJcicZ044 : iJcicZ044) {
			if (iiJcicZ044.getOutJcicTxtDate() == iJcicDate) {
				count++;
				uJcicZ044 = sJcicZ044Service.holdById(iiJcicZ044.getJcicZ044Id(), titaVo);
				oldJcicZ044 = (JcicZ044) iDataLog.clone(uJcicZ044);
				uJcicZ044.setOutJcicTxtDate(0);
				try {
					sJcicZ044Service.update(uJcicZ044, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
				}
				CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				JcicZ044Log iJcicZ044Log = sJcicZ044LogService.ukeyFirst(uJcicZ044.getUkey(), titaVo);
				iDataLog.setEnv(titaVo, oldJcicZ044, uJcicZ044);
				iDataLog.exec("L8407取消報送",iJcicZ044Log.getUkey()+iJcicZ044Log.getTxSeq());
			}
		}
		if (count == 0) {
			throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
		}
	}
}