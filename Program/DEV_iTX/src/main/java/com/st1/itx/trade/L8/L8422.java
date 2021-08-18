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
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.service.JcicZ062LogService;
import com.st1.itx.db.service.JcicZ062Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8422")
@Scope("prototype")
/**
 * 金融機構無擔保債務變更還款條件協議資料(062)
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8422 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8422.class);
	@Autowired
	public DataLog iDataLog;

	@Autowired
	public L8403File iL8403File;
	
	@Autowired
	public JcicZ062Service sJcicZ062Service; 
	@Autowired
	public JcicZ062LogService sJcicZ062LogService; 

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
		Slice<JcicZ062> iJcicZ062 = null;
		JcicZ062 uJcicZ062 = new JcicZ062();
		JcicZ062 oldJcicZ062 = new JcicZ062();
		iJcicZ062 = sJcicZ062Service.findAll(this.index, this.limit, titaVo);
		for (JcicZ062 iiJcicZ062: iJcicZ062) {
			if (iiJcicZ062.getOutJcicTxtDate() == iJcicDate) {
				count ++;
				uJcicZ062 = sJcicZ062Service.holdById(iiJcicZ062.getJcicZ062Id(), titaVo);
				oldJcicZ062 = (JcicZ062) iDataLog.clone(uJcicZ062);
				uJcicZ062.setOutJcicTxtDate(0);
				try {
					sJcicZ062Service.update(uJcicZ062, titaVo);
				}catch(DBException e) {
					throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
				}
				iDataLog.setEnv(titaVo, oldJcicZ062, uJcicZ062);
				iDataLog.exec();
			}
		}
		if (count == 0) {
			throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
		}
	}
}