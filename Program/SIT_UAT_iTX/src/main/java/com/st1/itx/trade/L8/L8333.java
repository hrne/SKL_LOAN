package com.st1.itx.trade.L8;

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
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.domain.JcicZ571Id;
import com.st1.itx.db.domain.JcicZ571Log;
import com.st1.itx.db.service.JcicZ571LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ571Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;


/**
 * Tita<br>
* TranKey=X,1<br>
* CustId=X,10<br>
* SubmitKey=X,10<br>
* RcDate=9,7<br>
* ChangePayDate=9,7<br>
* ClosedDate=9,7<br>
* ClosedResult=9,1<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8333")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8333 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8333.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ571Service sJcicZ571Service;
	@Autowired
	public JcicZ571LogService sJcicZ571LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8333 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iBankId = titaVo.getParam("BankId");
		String iOwnerYn = titaVo.getParam("OwnerYn");
		String iPayYn = titaVo.getParam("PayYn");
		int iOwnerAmt = Integer.valueOf(titaVo.getParam("OwnerAmt"));
		int iAllotAmt = Integer.valueOf(titaVo.getParam("AllotAmt"));
		int iUnallotAmt = Integer.valueOf(titaVo.getParam("UnallotAmt"));
		String iKey = "";
		//JcicZ571
		JcicZ571 iJcicZ571 = new JcicZ571();
		JcicZ571Id iJcicZ571Id = new JcicZ571Id();
		iJcicZ571Id.setApplyDate(iApplyDate);
		iJcicZ571Id.setBankId(iBankId);
		iJcicZ571Id.setCustId(iCustId);
		iJcicZ571Id.setSubmitKey(iSubmitKey);
		JcicZ571 chJcicZ571 = new JcicZ571();
		
		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ571
			chJcicZ571 = sJcicZ571Service.findById(iJcicZ571Id, titaVo);
			if (chJcicZ571!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");          
            iJcicZ571.setJcicZ571Id(iJcicZ571Id);
			iJcicZ571.setTranKey(iTranKey);
			iJcicZ571.setOwnerYn(iOwnerYn);
			iJcicZ571.setPayYn(iPayYn);
			iJcicZ571.setOwnerAmt(iOwnerAmt);
			iJcicZ571.setAllotAmt(iAllotAmt);
			iJcicZ571.setUnallotAmt(iUnallotAmt);
			iJcicZ571.setUkey(iKey);
			try {
				sJcicZ571Service.insert(iJcicZ571, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ571 = sJcicZ571Service.ukeyFirst(iKey, titaVo);
			JcicZ571 uJcicZ571 = new JcicZ571();
			uJcicZ571 = sJcicZ571Service.holdById(iJcicZ571.getJcicZ571Id(), titaVo);
			if (uJcicZ571 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}			
			uJcicZ571.setTranKey(iTranKey);
			uJcicZ571.setOwnerYn(iOwnerYn);
			uJcicZ571.setPayYn(iPayYn);
			uJcicZ571.setOwnerAmt(iOwnerAmt);
			uJcicZ571.setAllotAmt(iAllotAmt);
			uJcicZ571.setUnallotAmt(iUnallotAmt);
			uJcicZ571.setOutJcicTxtDate(0);
			JcicZ571 oldJcicZ571 = (JcicZ571) iDataLog.clone(uJcicZ571);
			try {
				sJcicZ571Service.update(uJcicZ571, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ571, uJcicZ571);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ571 = sJcicZ571Service.findById(iJcicZ571Id);
			if (iJcicZ571 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ571Log> dJcicLogZ571 = null;
			dJcicLogZ571 = sJcicZ571LogService.ukeyEq(iJcicZ571.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ571 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
			try {
				sJcicZ571Service.delete(iJcicZ571, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ571Log iJcicZ571Log = dJcicLogZ571.getContent().get(0);
				iJcicZ571.setOwnerYn(iJcicZ571Log.getOwnerYn());
				iJcicZ571.setPayYn(iJcicZ571Log.getPayYn());
				iJcicZ571.setOwnerAmt(iJcicZ571Log.getOwnerAmt());
				iJcicZ571.setAllotAmt(iJcicZ571Log.getAllotAmt());
				iJcicZ571.setUnallotAmt(iJcicZ571Log.getUnallotAmt());
				iJcicZ571.setTranKey(iJcicZ571Log.getTranKey());
				iJcicZ571.setOutJcicTxtDate(iJcicZ571Log.getOutJcicTxtDate());
				try {
					sJcicZ571Service.update(iJcicZ571, titaVo);
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
