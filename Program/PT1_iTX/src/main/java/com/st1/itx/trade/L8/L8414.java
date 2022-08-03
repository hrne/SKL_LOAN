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
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.domain.JcicZ051Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ051LogService;
import com.st1.itx.db.service.JcicZ051Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8414")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(051)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8414 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;

	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ051Service sJcicZ051Service;
	@Autowired
	public JcicZ051LogService sJcicZ051LogService;

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
		Slice<JcicZ051> iJcicZ051 = null;
		JcicZ051 uJcicZ051 = new JcicZ051();
		JcicZ051 oldJcicZ051 = new JcicZ051();
		iJcicZ051 = sJcicZ051Service.findAll(0,Integer.MAX_VALUE, titaVo);
		for (JcicZ051 iiJcicZ051 : iJcicZ051) {
			if (iiJcicZ051.getOutJcicTxtDate() == iJcicDate) {
				count++;
				uJcicZ051 = sJcicZ051Service.holdById(iiJcicZ051.getJcicZ051Id(), titaVo);
				oldJcicZ051 = (JcicZ051) iDataLog.clone(uJcicZ051);
				uJcicZ051.setOutJcicTxtDate(0);
				try {
					sJcicZ051Service.update(uJcicZ051, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
				}
                JcicZ051Log iJcicZ051Log = sJcicZ051LogService.ukeyFirst(uJcicZ051.getUkey(), titaVo);
				JcicZ051 cJcicZ051 = sJcicZ051Service.ukeyFirst(uJcicZ051.getUkey(), titaVo);
				CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ051.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);			
				iDataLog.setEnv(titaVo, oldJcicZ051, uJcicZ051);
				iDataLog.exec("L8414取消報送",iJcicZ051Log.getUkey()+iJcicZ051Log.getTxSeq());

			}
		}
		if (count == 0) {
			throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
		}
	}
}