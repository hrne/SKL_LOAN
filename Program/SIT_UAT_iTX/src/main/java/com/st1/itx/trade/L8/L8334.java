package com.st1.itx.trade.L8;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

/* log */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;


import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.domain.JcicZ572Id;
import com.st1.itx.db.domain.JcicZ572Log;
import com.st1.itx.db.service.JcicZ572LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ572Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;


@Service("L8334")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8334 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8334.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ572Service sJcicZ572Service;
	@Autowired
	public JcicZ572LogService sJcicZ572LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8334 ");
		this.totaVo.init(titaVo);
			
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		int iStartDate = Integer.valueOf(titaVo.getParam("StartDate"));
		int iPayDate = Integer.valueOf(titaVo.getParam("PayDate"));
		String iBankId = titaVo.getParam("BankId");
		int iAllotAmt = Integer.valueOf(titaVo.getParam("AllotAmt"));
		BigDecimal iOwnPercentage = new BigDecimal(titaVo.getParam("OwnPercentage"));	
		String iKey = "";
		//JcicZ572
		JcicZ572 iJcicZ572 = new JcicZ572();
		JcicZ572Id iJcicZ572Id = new JcicZ572Id();
		iJcicZ572Id.setApplyDate(iApplyDate);
		iJcicZ572Id.setBankId(iBankId);
		iJcicZ572Id.setCustId(iCustId);
		iJcicZ572Id.setSubmitKey(iSubmitKey);
		iJcicZ572Id.setPayDate(iPayDate);
		JcicZ572 chJcicZ572 = new JcicZ572();
		
		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ572
			chJcicZ572 = sJcicZ572Service.findById(iJcicZ572Id, titaVo);
			if (chJcicZ572 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ572.setJcicZ572Id(iJcicZ572Id);
			iJcicZ572.setTranKey(iTranKey);
			iJcicZ572.setStartDate(iStartDate);
			iJcicZ572.setAllotAmt(iAllotAmt);
			iJcicZ572.setOwnPercentage(iOwnPercentage);
			iJcicZ572.setUkey(iKey);
			try {
				sJcicZ572Service.insert(iJcicZ572, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ572 = sJcicZ572Service.ukeyFirst(iKey, titaVo);
			JcicZ572 uJcicZ572 = new JcicZ572();
			uJcicZ572 = sJcicZ572Service.holdById(iJcicZ572.getJcicZ572Id(), titaVo);
			if (uJcicZ572 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ572.setStartDate(iStartDate);
			uJcicZ572.setAllotAmt(iAllotAmt);
			uJcicZ572.setOwnPercentage(iOwnPercentage);
			uJcicZ572.setTranKey(iTranKey);
			uJcicZ572.setOutJcicTxtDate(0);
			JcicZ572 oldJcicZ572 = (JcicZ572) iDataLog.clone(uJcicZ572);
			try {
				sJcicZ572Service.update(uJcicZ572, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ572, uJcicZ572);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ572 = sJcicZ572Service.findById(iJcicZ572Id);
			if (iJcicZ572 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ572Log> dJcicLogZ572 = null;
			dJcicLogZ572 = sJcicZ572LogService.ukeyEq(iJcicZ572.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ572 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ572Service.delete(iJcicZ572, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ572Log iJcicZ572Log = dJcicLogZ572.getContent().get(0);				
				iJcicZ572.setStartDate(iJcicZ572Log.getStartDate());
				iJcicZ572.setAllotAmt(iJcicZ572Log.getAllotAmt());
				iJcicZ572.setOwnPercentage(iJcicZ572Log.getOwnPercentage());
				iJcicZ572.setTranKey(iJcicZ572Log.getTranKey());
				iJcicZ572.setOutJcicTxtDate(iJcicZ572Log.getOutJcicTxtDate());
				try {
					sJcicZ572Service.update(iJcicZ572, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
		default:
			break;
		}	
		this.addList(this.totaVo);
		return this.sendList();
	}
}