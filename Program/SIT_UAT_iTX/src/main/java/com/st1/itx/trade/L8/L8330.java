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
import com.st1.itx.db.domain.JcicZ451;
import com.st1.itx.db.domain.JcicZ451Id;
import com.st1.itx.db.domain.JcicZ451Log;
import com.st1.itx.db.service.JcicZ451LogService;

/*DB服務*/
import com.st1.itx.db.service.JcicZ451Service;


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

@Service("L8330")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie/ Mata
 * @version 1.0.0
 */
public class L8330 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ451Service sJcicZ451Service;
	@Autowired
	public JcicZ451LogService sJcicZ451LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8330 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey=titaVo.getParam("TranKey").trim();
		String iCustId=titaVo.getParam("CustId").trim();
		String iSubmitKey=titaVo.getParam("SubmitKey").trim();
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iCourtCode=titaVo.getParam("CourtCode").trim();
		int iDelayYM=Integer.valueOf(titaVo.getParam("DelayYM"));
		String iDelayCode = titaVo.getParam("DelayCode");
		String iKey = "";
		//JcicZ451
		JcicZ451 iJcicZ451 = new JcicZ451();
		JcicZ451Id iJcicZ451Id = new JcicZ451Id();
		iJcicZ451Id.setApplyDate(iApplyDate);
		iJcicZ451Id.setCourtCode(iCourtCode);
		iJcicZ451Id.setCustId(iCustId);
		iJcicZ451Id.setSubmitKey(iSubmitKey); 
		iJcicZ451Id.setDelayYM(iDelayYM);
		JcicZ451 chJcicZ451 = new JcicZ451();
		
		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ451
			chJcicZ451 = sJcicZ451Service.findById(iJcicZ451Id, titaVo);
			if (chJcicZ451 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ451.setJcicZ451Id(iJcicZ451Id);
			iJcicZ451.setTranKey(iTranKey);
			iJcicZ451.setDelayCode(iDelayCode);
			iJcicZ451.setUkey(iKey);
			try {
				sJcicZ451Service.insert(iJcicZ451, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ451 = sJcicZ451Service.ukeyFirst(iKey, titaVo);
			JcicZ451 uJcicZ451 = new JcicZ451();
			uJcicZ451 = sJcicZ451Service.holdById(iJcicZ451.getJcicZ451Id(), titaVo);
			if (uJcicZ451 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ451.setDelayCode(iDelayCode);
			uJcicZ451.setTranKey(iTranKey);
			uJcicZ451.setOutJcicTxtDate(0);
			JcicZ451 oldJcicZ451 = (JcicZ451) iDataLog.clone(uJcicZ451);
			try {
				sJcicZ451Service.update(uJcicZ451, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ451, uJcicZ451);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ451 = sJcicZ451Service.findById(iJcicZ451Id);
			if (iJcicZ451 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ451Log> dJcicLogZ451 = null;
			dJcicLogZ451 = sJcicZ451LogService.ukeyEq(iJcicZ451.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ451 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ451Service.delete(iJcicZ451, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ451Log iJcicZ451Log = dJcicLogZ451.getContent().get(0);
				iJcicZ451.setDelayCode(iJcicZ451Log.getDelayCode());
				iJcicZ451.setTranKey(iJcicZ451Log.getTranKey());
				iJcicZ451.setOutJcicTxtDate(iJcicZ451Log.getOutJcicTxtDate());
				try {
					sJcicZ451Service.update(iJcicZ451, titaVo);
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