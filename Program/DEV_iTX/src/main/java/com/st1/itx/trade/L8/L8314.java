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
import com.st1.itx.db.domain.JcicZ052;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ053;
import com.st1.itx.db.domain.JcicZ053Id;
import com.st1.itx.db.domain.JcicZ053Log;
import com.st1.itx.db.service.JcicZ052Service;
import com.st1.itx.db.service.JcicZ053LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ053Service;

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

@Service("L8314")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8314 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ052Service sJcicZ052Service;
	@Autowired
	public JcicZ053Service sJcicZ053Service;
	@Autowired
	public JcicZ053LogService sJcicZ053LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8314 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey"); // 交易代碼
		String iCustId = titaVo.getParam("CustId");// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey");// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		String iMaxMainCode = titaVo.getParam("MaxMainCode");
		String iAgreeSend = titaVo.getParam("AgreeSend");
		String iAgreeSendData1 = titaVo.getParam("AgreeSendData1");
		String iAgreeSendData2 = titaVo.getParam("AgreeSendData2");
		int iChangePayDate = Integer.valueOf(titaVo.getParam("ChangePayDate"));
		String iKey = "";
		

		// JcicZ053
		JcicZ053 iJcicZ053 = new JcicZ053();
		JcicZ053Id iJcicZ053Id = new JcicZ053Id();
		iJcicZ053Id.setCustId(iCustId);// 債務人IDN
		iJcicZ053Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ053Id.setRcDate(iRcDate);
		iJcicZ053Id.setMaxMainCode(iMaxMainCode);
		JcicZ053 chJcicZ053 = new JcicZ053();

		// 檢核項目(D-29)
		if (!"4".equals(iTranKey_Tmp)) {
			// 2 start KEY值(IDN+報送單位代號+協商申請日)未曾報送過'52':前置協商相關資料報送例外處理則予以剔退
			Slice<JcicZ052> sJcicZ052 = sJcicZ052Service.otherEq(iSubmitKey, iCustId, iRcDate + 19110000, 0,
						Integer.MAX_VALUE, titaVo);
				if (sJcicZ052 == null) {
					throw new LogicException("E0005", "未曾報送過(52)前置協商相關資料報送例外處理.");
				}
			 // 2 end

			// 3
			// 後續需檢核補報送金融機構需於最大債權金融機構報送'52':前置協商相關資料報送例外處理檔案格式後3個營業日內回報送本檔案格式並補送相關資料，否則予以剔退處理.***J

			// 4
			// 檢核第8欄「是否同意報送例外處理檔案格式」填報'Y'者，方可補報送'42'或'43'檔案格式，否則予以剔退處理.***J

			// 5 檢核第9，10欄「同意補報送資料檔案格式之資料別」僅限於"42", "43", "61"，其餘予以剔退處理--->前端處理
				
			// 6 本中心將以第8欄「是否同意報送例外處理檔案格式」填報'Y'之筆數，計算該債權金融機構應支付最大債權金融機構之服務費.***J

			// 檢核項目 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ053
			chJcicZ053 = sJcicZ053Service.findById(iJcicZ053Id, titaVo);
			if (chJcicZ053 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ053.setJcicZ053Id(iJcicZ053Id);
			iJcicZ053.setTranKey(iTranKey);
			iJcicZ053.setAgreeSend(iAgreeSend);
			iJcicZ053.setAgreeSendData1(iAgreeSendData1);
			iJcicZ053.setAgreeSendData2(iAgreeSendData2);
			iJcicZ053.setChangePayDate(iChangePayDate);
			iJcicZ053.setUkey(iKey);
			try {
				sJcicZ053Service.insert(iJcicZ053, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ053 = sJcicZ053Service.ukeyFirst(iKey, titaVo);
			JcicZ053 uJcicZ053 = new JcicZ053();
			uJcicZ053 = sJcicZ053Service.holdById(iJcicZ053.getJcicZ053Id(), titaVo);
			if (uJcicZ053 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ053.setTranKey(iTranKey);
			uJcicZ053.setAgreeSend(iAgreeSend);
			uJcicZ053.setAgreeSendData1(iAgreeSendData1);
			uJcicZ053.setAgreeSendData2(iAgreeSendData2);
			uJcicZ053.setChangePayDate(iChangePayDate);
			uJcicZ053.setOutJcicTxtDate(0);
			JcicZ053 oldJcicZ053 = (JcicZ053) iDataLog.clone(uJcicZ053);
			try {
				sJcicZ053Service.update(uJcicZ053, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ053, uJcicZ053);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ053 = sJcicZ053Service.findById(iJcicZ053Id);
			if (iJcicZ053 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ053Log> dJcicLogZ053 = null;
			dJcicLogZ053 = sJcicZ053LogService.ukeyEq(iJcicZ053.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ053 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ053Service.delete(iJcicZ053, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ053Log iJcicZ053Log = dJcicLogZ053.getContent().get(0);
				iJcicZ053.setAgreeSend(iJcicZ053Log.getAgreeSend());
				iJcicZ053.setAgreeSendData1(iJcicZ053Log.getAgreeSendData1());
				iJcicZ053.setAgreeSendData2(iJcicZ053Log.getAgreeSendData2());
				iJcicZ053.setChangePayDate(iJcicZ053Log.getChangePayDate());
				iJcicZ053.setTranKey(iJcicZ053Log.getTranKey());
				iJcicZ053.setOutJcicTxtDate(iJcicZ053Log.getOutJcicTxtDate());
				try {
					sJcicZ053Service.update(iJcicZ053, titaVo);
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
