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
import com.st1.itx.db.domain.JcicZ041;
import com.st1.itx.db.domain.JcicZ041Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ041LogService;
import com.st1.itx.db.service.JcicZ041Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8404")
@Scope("prototype")
/**
 * 同意報送例外處裡檔案(041)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8404 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ041Service sJcicZ041Service;
	@Autowired
	public JcicZ041LogService sJcicZ041LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8404 ");
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
		Slice<JcicZ041> iJcicZ041 = null;
		JcicZ041 uJcicZ041 = new JcicZ041();
		JcicZ041 oldJcicZ041 = new JcicZ041();
		iJcicZ041 = sJcicZ041Service.findAll(0,Integer.MAX_VALUE, titaVo);
		for (JcicZ041 iiJcicZ041 : iJcicZ041) {
			if (iiJcicZ041.getOutJcicTxtDate() == iJcicDate) {
				count++;
				uJcicZ041 = sJcicZ041Service.holdById(iiJcicZ041.getJcicZ041Id(), titaVo);
				oldJcicZ041 = (JcicZ041) iDataLog.clone(uJcicZ041);
				uJcicZ041.setOutJcicTxtDate(0);
				try {
					sJcicZ041Service.update(uJcicZ041, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
				}
                JcicZ041Log iJcicZ041Log = sJcicZ041LogService.ukeyFirst(uJcicZ041.getUkey(), titaVo);
				JcicZ041 cJcicZ041 = sJcicZ041Service.ukeyFirst(uJcicZ041.getUkey(), titaVo);
				CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ041.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);			
				iDataLog.setEnv(titaVo, oldJcicZ041, uJcicZ041);
				iDataLog.exec("L8404取消報送",iJcicZ041Log.getUkey()+iJcicZ041Log.getTxSeq());

			}
		}
		if (count == 0) {
			throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
		}
	}
}