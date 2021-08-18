package com.st1.itx.trade.L8;
import java.util.ArrayList;
import java.util.UUID;

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
import com.st1.itx.db.domain.JcicZ060;
import com.st1.itx.db.domain.JcicZ060Id;
import com.st1.itx.db.domain.JcicZ060Log;
import com.st1.itx.db.service.JcicZ060LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ060Service;
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

@Service("L8318")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8318 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ060Service sJcicZ060Service;
	@Autowired
	public JcicZ060LogService sJcicZ060LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8318 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iChangePayDate = Integer.valueOf(titaVo.getParam("ChangePayDate"));
		int iYM = Integer.valueOf(titaVo.getParam("YM"));
		String iKey = "";
		//JcicZ060
		JcicZ060 iJcicZ060 = new JcicZ060();
		JcicZ060Id iJcicZ060Id = new JcicZ060Id();
		iJcicZ060Id.setSubmitKey(iSubmitKey);
		iJcicZ060Id.setCustId(iCustId);		
		iJcicZ060Id.setRcDate(iRcDate);
		iJcicZ060Id.setChangePayDate(iChangePayDate);
		JcicZ060 chJcicZ060 = new JcicZ060();
		
		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複
			chJcicZ060 = sJcicZ060Service.findById(iJcicZ060Id, titaVo);
			if (chJcicZ060!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ060.setJcicZ060Id(iJcicZ060Id);
			iJcicZ060.setUkey(iKey);
			iJcicZ060.setRcDate(iRcDate);
			iJcicZ060.setTranKey(iTranKey);
			iJcicZ060.setSubmitKey(iSubmitKey);
			iJcicZ060.setCustId(iCustId);
			iJcicZ060.setChangePayDate(iChangePayDate);
			iJcicZ060.setYM(iYM);
			try {
				sJcicZ060Service.insert(iJcicZ060, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ060 = sJcicZ060Service.ukeyFirst(iKey, titaVo);
			JcicZ060 uJcicZ060 = new JcicZ060();
			uJcicZ060 = sJcicZ060Service.holdById(iJcicZ060.getJcicZ060Id(), titaVo);
			if (uJcicZ060 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ060.setTranKey(iTranKey);
			uJcicZ060.setYM(iYM);
			uJcicZ060.setOutJcicTxtDate(0);
			JcicZ060 oldJcicZ060 = (JcicZ060) iDataLog.clone(uJcicZ060);
			try {
				sJcicZ060Service.update(uJcicZ060, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ060, uJcicZ060);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ060 = sJcicZ060Service.findById(iJcicZ060Id);
			if (iJcicZ060 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ060Log> dJcicLogZ060 = null;
			dJcicLogZ060 = sJcicZ060LogService.ukeyEq(iJcicZ060.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ060 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ060Service.delete(iJcicZ060, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ060Log iJcicZ060Log = dJcicLogZ060.getContent().get(0);				
				iJcicZ060.setYM(iJcicZ060Log.getYM());
				iJcicZ060.setTranKey(iJcicZ060Log.getTranKey());
				iJcicZ060.setOutJcicTxtDate(iJcicZ060Log.getOutJcicTxtDate());
				try {
					sJcicZ060Service.update(iJcicZ060, titaVo);
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