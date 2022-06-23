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

/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ048Id;
import com.st1.itx.db.domain.JcicZ048Log;
import com.st1.itx.db.service.JcicZ048LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ048Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

/**
 * Tita<br>
 * TranKey=X,1<br>
 * CustId=X,10<br>
 * SubmitKey=X,10<br>
 * CaseStatus=X,1<br>
 * ClaimDate=9,7<br>
 * CourtCode=X,3<br>
 * Year=9,3<br>
 * CourtDiv=X,8<br>
 * CourtCaseNo=X,80<br>
 * Approve=X,1<br>
 * OutstandAmt=9,9<br>
 * ClaimStatus1=X,1<br>
 * SaveDate=9,7<br>
 * ClaimStatus2=X,1<br>
 * SaveEndDate=9,7<br>
 * SubAmt=9,9<br>
 * AdminName=X,20<br>
 * OutJcicTxtDate=9,7<br>
 */

@Service("L8309")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8309 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ048Service sJcicZ048Service;
	@Autowired
	public JcicZ048LogService sJcicZ048LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8309 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey"); // 交易代碼
		String iCustId = titaVo.getParam("CustId");// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey");// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		String iCustRegAddr = titaVo.getParam("CustRegAddr");
		String iCustComAddr = titaVo.getParam("CustComAddr");
		String iCustRegTelNo = titaVo.getParam("CustRegTelNo");
		String iCustComTelNo = titaVo.getParam("CustComTelNo");
		String iCustMobilNo = titaVo.getParam("CustMobilNo");

		String iKey = "";
		// JcicZ048
		JcicZ048 iJcicZ048 = new JcicZ048();
		JcicZ048Id iJcicZ048Id = new JcicZ048Id();
		iJcicZ048Id.setCustId(iCustId);// 債務人IDN
		iJcicZ048Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ048Id.setRcDate(iRcDate);
		JcicZ048 chJcicZ048 = new JcicZ048();

		// 無檢核項目 removed by Fegie 2021/10/25

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ048
			chJcicZ048 = sJcicZ048Service.findById(iJcicZ048Id, titaVo);
			if (chJcicZ048 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ048.setJcicZ048Id(iJcicZ048Id);
			iJcicZ048.setTranKey(iTranKey);
			iJcicZ048.setCustRegAddr(iCustRegAddr);
			iJcicZ048.setCustComAddr(iCustComAddr);
			iJcicZ048.setCustRegTelNo(iCustRegTelNo);
			iJcicZ048.setCustComTelNo(iCustComTelNo);
			iJcicZ048.setCustMobilNo(iCustMobilNo);
			iJcicZ048.setUkey(iKey);
			try {
				sJcicZ048Service.insert(iJcicZ048, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ048 = sJcicZ048Service.ukeyFirst(iKey, titaVo);
			JcicZ048 uJcicZ048 = new JcicZ048();
			uJcicZ048 = sJcicZ048Service.holdById(iJcicZ048.getJcicZ048Id(), titaVo);
			if (uJcicZ048 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ048 oldJcicZ048 = (JcicZ048) iDataLog.clone(uJcicZ048);
			uJcicZ048.setTranKey(iTranKey);
			uJcicZ048.setCustRegAddr(iCustRegAddr);
			uJcicZ048.setCustComAddr(iCustComAddr);
			uJcicZ048.setCustRegTelNo(iCustRegTelNo);
			uJcicZ048.setCustComTelNo(iCustComTelNo);
			uJcicZ048.setCustMobilNo(iCustMobilNo);
			uJcicZ048.setOutJcicTxtDate(0);
			try {
				sJcicZ048Service.update(uJcicZ048, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ048, uJcicZ048);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ048 = sJcicZ048Service.findById(iJcicZ048Id);
			if (iJcicZ048 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ048Log> dJcicLogZ048 = null;
			dJcicLogZ048 = sJcicZ048LogService.ukeyEq(iJcicZ048.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ048 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ048Service.delete(iJcicZ048, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ048Log iJcicZ048Log = dJcicLogZ048.getContent().get(0);
				iJcicZ048.setCustRegAddr(iJcicZ048Log.getCustRegAddr());
				iJcicZ048.setCustComAddr(iJcicZ048Log.getCustComAddr());
				iJcicZ048.setCustRegTelNo(iJcicZ048Log.getCustRegTelNo());
				iJcicZ048.setCustComTelNo(iJcicZ048Log.getCustComTelNo());
				iJcicZ048.setCustMobilNo(iJcicZ048Log.getCustMobilNo());
				iJcicZ048.setTranKey(iJcicZ048Log.getTranKey());
				iJcicZ048.setOutJcicTxtDate(iJcicZ048Log.getOutJcicTxtDate());
				try {
					sJcicZ048Service.update(iJcicZ048, titaVo);
				} catch (DBException e) {
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
