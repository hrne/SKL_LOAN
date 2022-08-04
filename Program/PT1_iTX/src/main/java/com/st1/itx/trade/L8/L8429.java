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
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.domain.JcicZ447Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ447LogService;
import com.st1.itx.db.service.JcicZ447Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8429")
@Scope("prototype")
/**
 * 前置調解單獨全數受清償資料(447)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8429 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ447Service sJcicZ447Service;
	@Autowired
	public JcicZ447LogService sJcicZ447LogService;

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
		int count = 0;
		Slice<JcicZ447> iJcicZ447 = null;
		JcicZ447 uJcicZ447 = new JcicZ447();
		JcicZ447 oldJcicZ447 = new JcicZ447();
		iJcicZ447 = sJcicZ447Service.findAll(0,Integer.MAX_VALUE, titaVo);
		for (JcicZ447 iiJcicZ447 : iJcicZ447) {
			if (iiJcicZ447.getOutJcicTxtDate() == iJcicDate) {
				count++;
				uJcicZ447 = sJcicZ447Service.holdById(iiJcicZ447.getJcicZ447Id(), titaVo);
				oldJcicZ447 = (JcicZ447) iDataLog.clone(uJcicZ447);
				uJcicZ447.setOutJcicTxtDate(0);
				try {
					sJcicZ447Service.update(uJcicZ447, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
				}
                JcicZ447Log iJcicZ447Log = sJcicZ447LogService.ukeyFirst(uJcicZ447.getUkey(), titaVo);
				JcicZ447 cJcicZ447 = sJcicZ447Service.ukeyFirst(uJcicZ447.getUkey(), titaVo);
				CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ447.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);			
				iDataLog.setEnv(titaVo, oldJcicZ447, uJcicZ447);
				iDataLog.exec("L8429取消報送",iJcicZ447Log.getUkey()+iJcicZ447Log.getTxSeq());
			}
		}
		if (count == 0) {
			throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
		}
	}
}