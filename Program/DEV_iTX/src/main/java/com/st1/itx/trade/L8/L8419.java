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
import com.st1.itx.db.domain.JcicZ056;
import com.st1.itx.db.service.JcicZ056LogService;
import com.st1.itx.db.service.JcicZ056Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8419")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(056)
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8419 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;

	@Autowired
	public L8403File iL8403File;

	@Autowired
	public JcicZ056Service sJcicZ056Service;
	@Autowired
	public JcicZ056LogService sJcicZ056LogService;

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
		Slice<JcicZ056> iJcicZ056 = null;
		JcicZ056 uJcicZ056 = new JcicZ056();
		JcicZ056 oldJcicZ056 = new JcicZ056();
		iJcicZ056 = sJcicZ056Service.findAll(this.index, this.limit, titaVo);
		for (JcicZ056 iiJcicZ056 : iJcicZ056) {
			if (iiJcicZ056.getOutJcicTxtDate() == iJcicDate) {
				count++;
				uJcicZ056 = sJcicZ056Service.holdById(iiJcicZ056.getJcicZ056Id(), titaVo);
				oldJcicZ056 = (JcicZ056) iDataLog.clone(uJcicZ056);
				uJcicZ056.setOutJcicTxtDate(0);
				try {
					sJcicZ056Service.update(uJcicZ056, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
				}
				iDataLog.setEnv(titaVo, oldJcicZ056, uJcicZ056);
				iDataLog.exec();
			}
		}
		if (count == 0) {
			throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
		}
	}
}