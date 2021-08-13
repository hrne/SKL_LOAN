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
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.service.JcicZ572LogService;
import com.st1.itx.db.service.JcicZ572Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8436")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業(572)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8436 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8436.class);
	@Autowired
	public DataLog iDataLog;

	@Autowired
	public L8403File iL8403File;
	
	@Autowired
	public JcicZ572Service sJcicZ572Service; 
	@Autowired
	public JcicZ572LogService sJcicZ572LogService; 

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
		this.info("檔名=" + fileNname);

		iL8403File.exec(titaVo);
		long fileNo = iL8403File.close();
		iL8403File.toFile(fileNo, fileNname);
	}
	
	public void doRemoveJcicDate(TitaVo titaVo) throws LogicException {
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int count = 0;
		Slice<JcicZ572> iJcicZ572 = null;
		JcicZ572 uJcicZ572 = new JcicZ572();
		JcicZ572 oldJcicZ572 = new JcicZ572();
		iJcicZ572 = sJcicZ572Service.findAll(this.index, this.limit, titaVo);
		for (JcicZ572 iiJcicZ572: iJcicZ572) {
			if (iiJcicZ572.getOutJcicTxtDate() == iJcicDate) {
				count ++;
				uJcicZ572 = sJcicZ572Service.holdById(iiJcicZ572.getJcicZ572Id(), titaVo);
				oldJcicZ572 = (JcicZ572) iDataLog.clone(uJcicZ572);
				uJcicZ572.setOutJcicTxtDate(0);
				try {
					sJcicZ572Service.update(uJcicZ572, titaVo);
				}catch(DBException e) {
					throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
				}
				iDataLog.setEnv(titaVo, oldJcicZ572, uJcicZ572);
				iDataLog.exec();
			}
		}
		if (count == 0) {
			throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
		}
	}
}