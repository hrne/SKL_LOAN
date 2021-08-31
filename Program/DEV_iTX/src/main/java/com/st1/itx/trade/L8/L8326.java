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
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.domain.JcicZ446Log;
import com.st1.itx.db.service.JcicZ446LogService;

/*DB服務*/
import com.st1.itx.db.service.JcicZ446Service;

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

@Service("L8326")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8326 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ446LogService sJcicZ446LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8326 ");
		this.totaVo.init(titaVo);
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iCourtCode = titaVo.getParam("CourtCode");
		String iCloseCode = titaVo.getParam("CloseCode");
		int iCloseDate = Integer.valueOf(titaVo.getParam("CloseDate"));
		String iKey = "";
		//JcicZ446
		JcicZ446 iJcicZ446 = new JcicZ446();
		JcicZ446Id iJcicZ446Id = new JcicZ446Id();
		iJcicZ446Id.setSubmitKey(iSubmitKey);
		iJcicZ446Id.setCustId(iCustId);		
		iJcicZ446Id.setApplyDate(iApplyDate);
		iJcicZ446Id.setCourtCode(iCourtCode);
		JcicZ446 chJcicZ446 = new JcicZ446();

		switch(iTranKey_Tmp) {
		case "1":
		    //檢核是否重複
		    chJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id, titaVo);
		    this.info("TEST==="+chJcicZ446);
		    if (chJcicZ446!=null) {
		        throw new LogicException("E0005", "已有相同資料");
		    }
		    
		    iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		    iJcicZ446.setJcicZ446Id(iJcicZ446Id);
		    iJcicZ446.setTranKey(iTranKey);
		    iJcicZ446.setUkey(iKey);
		    iJcicZ446.setCloseCode(iCloseCode);
		    iJcicZ446.setCloseDate(iCloseDate);
		    try {
		        sJcicZ446Service.insert(iJcicZ446, titaVo);
		    }catch (DBException e) {
		        throw new LogicException("E0005", "更生債權金額異動通知資料");
		    }
		    break;
		case "2":
		    iKey = titaVo.getParam("Ukey");
		    iJcicZ446 = sJcicZ446Service.ukeyFirst(iKey, titaVo);
		    JcicZ446 uJcicZ446 = new JcicZ446();
		    uJcicZ446 = sJcicZ446Service.holdById(iJcicZ446.getJcicZ446Id(), titaVo);
		    if (uJcicZ446 == null) {
		        throw new LogicException("E0007", "無此更新資料");
		    }
		    uJcicZ446.setTranKey(iTranKey);
		    uJcicZ446.setCloseCode(iCloseCode);
		    uJcicZ446.setCloseDate(iCloseDate);
		    uJcicZ446.setOutJcicTxtDate(0);
		    JcicZ446 oldJcicZ446 = (JcicZ446) iDataLog.clone(uJcicZ446);
		    try {
		        sJcicZ446Service.update(uJcicZ446, titaVo);
		    }catch (DBException e) {
		        throw new LogicException("E0005", "更生債權金額異動通知資料");
		    }
		    iDataLog.setEnv(titaVo, oldJcicZ446, uJcicZ446);
		    iDataLog.exec();
		    break;
		case "4": //需刷主管卡
		    iJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id);
		    if (iJcicZ446 == null) {
		        throw new LogicException("E0008", "");
		    }
		    if (!titaVo.getHsupCode().equals("1")) {
		        iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
		    }
		    Slice<JcicZ446Log> dJcicLogZ446 = null;
		    dJcicLogZ446 = sJcicZ446LogService.ukeyEq(iJcicZ446.getUkey(), 0, Integer.MAX_VALUE, titaVo);
		    if (dJcicLogZ446 == null) {
		        //尚未開始寫入log檔之資料，主檔資料可刪除
		        try {
		            sJcicZ446Service.delete(iJcicZ446, titaVo);
		        }catch (DBException e) {
		            throw new LogicException("E0008", "更生債權金額異動通知資料");
		        }
		    }else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
		        //最近一筆之資料
		        JcicZ446Log iJcicZ446Log = dJcicLogZ446.getContent().get(0);				
			    iJcicZ446.setCloseCode(iJcicZ446Log.getCloseCode());
			    iJcicZ446.setCloseDate(iJcicZ446Log.getCloseDate());
		        iJcicZ446.setOutJcicTxtDate(iJcicZ446Log.getOutJcicTxtDate());
		        try {
		            sJcicZ446Service.update(iJcicZ446, titaVo);
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

