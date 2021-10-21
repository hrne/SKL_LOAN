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
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ040Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ041;
import com.st1.itx.db.domain.JcicZ041Id;
import com.st1.itx.db.domain.JcicZ041Log;
/*DB服務*/
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.service.JcicZ041Service;
import com.st1.itx.db.service.JcicZ041LogService;

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

@Service("L8302")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8302 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ041Service sJcicZ041Service;
	@Autowired
	public JcicZ041LogService sJcicZ041LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8302 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey"); // 交易代碼
		String iCustId = titaVo.getParam("CustId");// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey");// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iScDate = Integer.valueOf(titaVo.getParam("ScDate"));
		int iNegoStartDate = Integer.valueOf(titaVo.getParam("NegoStartDate"));
		int iNonFinClaimAmt = Integer.valueOf(titaVo.getParam("NonFinClaimAmt"));
		String iKey = "";
		// JcicZ041, JcicZ040
		JcicZ041 iJcicZ041 = new JcicZ041();
		JcicZ041Id iJcicZ041Id = new JcicZ041Id();
		iJcicZ041Id.setCustId(iCustId);// 債務人IDN
		iJcicZ041Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ041Id.setRcDate(iRcDate);
		JcicZ041 chJcicZ041 = new JcicZ041();
		JcicZ040 iJcicZ040 = new JcicZ040();
		JcicZ040Id iJcicZ040Id = new JcicZ040Id();
		iJcicZ040Id.setCustId(iCustId);// 債務人IDN
		iJcicZ040Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ040Id.setRcDate(iRcDate);

		// 檢核項目(D-4)
		if (!"4".equals(iTranKey_Tmp)) {

			// 2 start 完整key值未曾報送過'40':前置協商受理申請暨請求回報債權通知則予以剔退
			iJcicZ040 = sJcicZ040Service.findById(iJcicZ040Id, titaVo);
			if (iJcicZ040 == null) {
				if ("A".equals(iTranKey)) {
					throw new LogicException("E0005", "未曾報送過(40)前置協商受理申請暨請求回報債權通知資料.");
				} else {
					throw new LogicException("E0007", "未曾報送過(40)前置協商受理申請暨請求回報債權通知資料.");
				}
			}
			// 2 end

			// 3 --->1014會議通知不需檢核，只要把[協商開始日]欄位改為預設值"協商申請日+25"，並設為只讀
			// 最大債權金融機構應於七項文件齊全後報送本檔案格式，此時需報送第9欄停催日(第7欄協商開始日可為空白)，並且於實際協商開始(最晚收件後第25日)時再度報送異動本檔案格式，此時異動，協商開始日iNegoStartDate和停催日iScDate必須有值)

			// 4 停催日大於協商開始日則予以剔退--->(前端檢核)

			// 檢核項目 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ041
			chJcicZ041 = sJcicZ041Service.findById(iJcicZ041Id, titaVo);
			if (chJcicZ041 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ041.setJcicZ041Id(iJcicZ041Id);
			iJcicZ041.setTranKey(iTranKey);
			iJcicZ041.setScDate(iScDate);
			iJcicZ041.setNegoStartDate(iNegoStartDate);
			iJcicZ041.setNonFinClaimAmt(iNonFinClaimAmt);
			iJcicZ041.setUkey(iKey);
			try {
				sJcicZ041Service.insert(iJcicZ041, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ041 = sJcicZ041Service.ukeyFirst(iKey, titaVo);
			JcicZ041 uJcicZ041 = new JcicZ041();
			uJcicZ041 = sJcicZ041Service.holdById(iJcicZ041.getJcicZ041Id(), titaVo);
			if (uJcicZ041 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ041.setTranKey(iTranKey);
			uJcicZ041.setScDate(iScDate);
			uJcicZ041.setNegoStartDate(iNegoStartDate);
			uJcicZ041.setNonFinClaimAmt(iNonFinClaimAmt);
			uJcicZ041.setOutJcicTxtDate(0);
			JcicZ041 oldJcicZ041 = (JcicZ041) iDataLog.clone(uJcicZ041);
			try {
				sJcicZ041Service.update(uJcicZ041, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ041, uJcicZ041);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ041 = sJcicZ041Service.findById(iJcicZ041Id);
			if (iJcicZ041 == null) {
				throw new LogicException("E0006", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ041Log> dJcicLogZ041 = null;
			dJcicLogZ041 = sJcicZ041LogService.ukeyEq(iJcicZ041.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ041 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ041Service.delete(iJcicZ041, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ041Log iJcicZ041Log = dJcicLogZ041.getContent().get(0);
				iJcicZ041.setScDate(iJcicZ041Log.getScDate());
				iJcicZ041.setNegoStartDate(iJcicZ041Log.getNegoStartDate());
				iJcicZ041.setNonFinClaimAmt(iJcicZ041Log.getNonFinClaimAmt());
				iJcicZ041.setTranKey(iJcicZ041Log.getTranKey());
				iJcicZ041.setOutJcicTxtDate(iJcicZ041Log.getOutJcicTxtDate());
				try {
					sJcicZ041Service.update(iJcicZ041, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			}
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}
