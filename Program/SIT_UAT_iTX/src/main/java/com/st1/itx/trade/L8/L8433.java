package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
/* log */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.st1.itx.db.domain.JcicZ454;
import com.st1.itx.db.service.JcicZ454LogService;
import com.st1.itx.db.service.JcicZ454Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8433")
@Scope("prototype")
/**
 * 前置調解單獨全數受清償資料(454)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8433 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8433.class);
	@Autowired
	public DataLog iDataLog;

	@Autowired
	public L8403File iL8403File;
	
	@Autowired
	public JcicZ454Service sJcicZ454Service; 
	@Autowired
	public JcicZ454LogService sJcicZ454LogService; 

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
		logger.info("檔名=" + fileNname);

		iL8403File.exec(titaVo);
		long fileNo = iL8403File.close();
		iL8403File.toFile(fileNo, fileNname);

	}
	
	public void doRemoveJcicDate(TitaVo titaVo) throws LogicException {
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int count = 0;
		Slice<JcicZ454> iJcicZ454 = null;
		JcicZ454 uJcicZ454 = new JcicZ454();
		JcicZ454 oldJcicZ454 = new JcicZ454();
		iJcicZ454 = sJcicZ454Service.findAll(this.index, this.limit, titaVo);
		for (JcicZ454 iiJcicZ454: iJcicZ454) {
			if (iiJcicZ454.getOutJcicTxtDate() == iJcicDate) {
				count ++;
				uJcicZ454 = sJcicZ454Service.holdById(iiJcicZ454.getJcicZ454Id(), titaVo);
				oldJcicZ454 = (JcicZ454) iDataLog.clone(uJcicZ454);
				uJcicZ454.setOutJcicTxtDate(0);
				try {
					sJcicZ454Service.update(uJcicZ454, titaVo);
				}catch(DBException e) {
					throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
				}
				iDataLog.setEnv(titaVo, oldJcicZ454, uJcicZ454);
				iDataLog.exec();
			}
		}
		if (count == 0) {
			throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
		}
	}
}