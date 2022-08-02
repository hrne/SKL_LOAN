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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ046;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.domain.JcicZ051Id;
import com.st1.itx.db.domain.JcicZ051Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.JcicZ051LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ051Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;

@Service("L8312")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8312 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	@Autowired
	public JcicZ051Service sJcicZ051Service;
	@Autowired
	public JcicZ051LogService sJcicZ051LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public DateUtil iDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8312 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim(); // 交易代碼
		String iCustId = titaVo.getParam("CustId").trim();// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate").trim());
		int iDelayYM = Integer.valueOf(titaVo.getParam("DelayYM").trim())+191100;
		String iDelayCode = titaVo.getParam("DelayCode").trim();
		String iDelayDesc = titaVo.getParam("DelayDesc").trim();
		String iKey = "";
		
		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);
		
		// JcicZ051, JcicZ046
		JcicZ051 iJcicZ051 = new JcicZ051();
		JcicZ051Id iJcicZ051Id = new JcicZ051Id();
		iJcicZ051Id.setCustId(iCustId);// 債務人IDN
		iJcicZ051Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ051Id.setRcDate(iRcDate);
		iJcicZ051Id.setDelayYM(iDelayYM);
		JcicZ051 chJcicZ051 = new JcicZ051();

		// Date計算
		int iaDelayYM = DealMon(iDelayYM, 1);// 延期繳款年月後1月
		int imDelayYM = DealMon(iDelayYM, -1);// 延期繳款年月前1月
		int sCovDelayYM = 0;// 延期繳款累計期數(「延期繳款原因」為'L:受嚴重特殊傳染性肺炎疫情影響繳款')
		int sDelayYM = 0;// 延期繳款累計期數(「延期繳款原因」為非'L')

		// 檢核項目(D-25)
		if (!"4".equals(iTranKey_Tmp)) {

			// 2.1 KEY值（CustId+SubmitKey+RcDate）不存在則予以剔退***
			// 2.2 start 完整key值已報送結案則予以剔退
			if ("A".equals(iTranKey)) {
				Slice<JcicZ046> sJcicZ046 = sJcicZ046Service.hadZ046(iCustId, iRcDate + 19110000, iSubmitKey, 0, Integer.MAX_VALUE, titaVo);
				if (sJcicZ046 != null) {
					int sTranKey = 0;
					for (JcicZ046 xJcicZ046 : sJcicZ046) {
						if (!"D".equals(xJcicZ046.getTranKey())) {
							sTranKey = 1;
						}
					}
					if (sTranKey == 1) {
						throw new LogicException("E0005", "Key值(IDN+報送單位代號+協商申請日)已報送(46)結案通知資料.");
					}
				}
			} // 2.2 end

			// 3 最大債權金融機構於核準債務人申請喘息期後，最慢需於次月10日前報送此檔案，逾期將無法直接報送，需以公文方式行文本中心處理資料進檔.***J
			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {
				// 4 start 若延期繳款原因為D'繳稅'者，延期繳款年月不能連續兩期
				// 5 start 延期繳款累計期數(月份)不得超過6期，超過者以剔退處理.
				// 7.2 第7欄「延期繳款原因」為'L:受嚴重特殊傳染性肺炎疫情影響繳款'【限累計申請最多6期】，則不受上述檢核5的限制.
				if ("L".equals(iDelayCode)) {
					sCovDelayYM = 1;
				} else {
					sDelayYM = 1;
				}
				Slice<JcicZ051> sJcicZ051 = sJcicZ051Service.SubCustRcEq(iCustId, iRcDate + 19110000, iSubmitKey, 0, Integer.MAX_VALUE, titaVo);
				if (sJcicZ051 != null) {
					for (JcicZ051 xJcicZ051 : sJcicZ051) {
						if (!"D".equals(xJcicZ051.getTranKey()) && !titaVo.getParam("Ukey").equals(xJcicZ051.getUkey())) {
							if (("D".equals(iDelayCode) && "D".equals(xJcicZ051.getDelayCode())) && (iaDelayYM == xJcicZ051.getDelayYM() || imDelayYM == xJcicZ051.getDelayYM())) {
								if ("A".equals(iTranKey)) {
									throw new LogicException("E0005", "延期繳款原因為'D繳稅'者，延期繳款年月不能連續兩期.");
								} else {
									throw new LogicException("E0007", "延期繳款原因為'D繳稅'者，延期繳款年月不能連續兩期.");
								}
							}
							if ("L".equals(xJcicZ051.getDelayCode())) {
								sCovDelayYM++;
							} else {
								sDelayYM++;
							}
						}
					}
					if (sDelayYM > 6) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005", "延期繳款累計期數(月份)不得超過6期.");
						} else {
							throw new LogicException("E0007", "延期繳款累計期數(月份)不得超過6期.");
						}
					} else if (sCovDelayYM > 6) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005", "「延期繳款原因」為'L:受嚴重特殊傳染性肺炎疫情影響繳款'【限累計申請最多6期】.");
						} else {
							throw new LogicException("E0007", "「延期繳款原因」為'L:受嚴重特殊傳染性肺炎疫情影響繳款'【限累計申請最多6期】.");
						}
					}
				} // 4, 5, 7.2 end
			}

			// 6 若第7欄「延期繳款原因」為'F:放無薪假或減薪'，則檢核於98/06/30前僅可申請'一期喘息'.***J

			// 7.1 第7欄「延期繳款原因」為'L:受嚴重特殊傳染性肺炎疫情影響繳款'【限累計申請最多6期】，則不受上述檢核3的限制.***J

			// 檢核項目 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ051
			chJcicZ051 = sJcicZ051Service.findById(iJcicZ051Id, titaVo);
			if (chJcicZ051 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ051.setJcicZ051Id(iJcicZ051Id);
			iJcicZ051.setTranKey(iTranKey);
			iJcicZ051.setDelayCode(iDelayCode);
			iJcicZ051.setDelayDesc(iDelayDesc);
			iJcicZ051.setUkey(iKey);
			try {
				sJcicZ051Service.insert(iJcicZ051, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ051 = sJcicZ051Service.ukeyFirst(iKey, titaVo);
			JcicZ051 uJcicZ051 = new JcicZ051();
			uJcicZ051 = sJcicZ051Service.holdById(iJcicZ051.getJcicZ051Id(), titaVo);
			if (uJcicZ051 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ051 oldJcicZ051 = (JcicZ051) iDataLog.clone(uJcicZ051);
			uJcicZ051.setTranKey(iTranKey);
			uJcicZ051.setDelayCode(iDelayCode);
			uJcicZ051.setDelayDesc(iDelayDesc);
			uJcicZ051.setOutJcicTxtDate(0);
			try {
				sJcicZ051Service.update(uJcicZ051, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ051, uJcicZ051);
			iDataLog.exec("L8312異動", uJcicZ051.getSubmitKey()+uJcicZ051.getCustId()+uJcicZ051.getRcDate());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ051 = sJcicZ051Service.ukeyFirst(iKey, titaVo);
			JcicZ051 uJcicZ0512 = new JcicZ051();
			uJcicZ0512 = sJcicZ051Service.holdById(iJcicZ051.getJcicZ051Id(), titaVo);
			iJcicZ051 = sJcicZ051Service.findById(iJcicZ051Id);
			if (iJcicZ051 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			JcicZ051 oldJcicZ0512 = (JcicZ051) iDataLog.clone(uJcicZ0512);
			uJcicZ0512.setTranKey(iTranKey);
			uJcicZ0512.setDelayCode(iDelayCode);
			uJcicZ0512.setDelayDesc(iDelayDesc);
			uJcicZ0512.setOutJcicTxtDate(0);
			
			Slice<JcicZ051Log> dJcicLogZ051 = null;
			dJcicLogZ051 = sJcicZ051LogService.ukeyEq(iJcicZ051.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ051 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ051Service.delete(iJcicZ051, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ051Log iJcicZ051Log = dJcicLogZ051.getContent().get(0);
				iJcicZ051.setDelayCode(iJcicZ051Log.getDelayCode());
				iJcicZ051.setDelayDesc(iJcicZ051Log.getDelayDesc());
				iJcicZ051.setTranKey(iJcicZ051Log.getTranKey());
				iJcicZ051.setOutJcicTxtDate(iJcicZ051Log.getOutJcicTxtDate());
				try {
					sJcicZ051Service.update(iJcicZ051, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ0512, uJcicZ0512);
			iDataLog.exec("L8312刪除", uJcicZ0512.getSubmitKey()+uJcicZ0512.getCustId()+uJcicZ0512.getRcDate());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private int DealMon(int txMonth, int iMonth) throws LogicException {
		String txMonStr = String.valueOf(txMonth);
		int txDate = Integer.valueOf(txMonStr + "10");
		int retxdate = 0;
		iDateUtil.init();
		iDateUtil.setDate_1(txDate);
		iDateUtil.setMons(iMonth);
		retxdate = iDateUtil.getCalenderDay();
		String retxdateStr = String.valueOf(retxdate);
		String retxMonStr = retxdateStr.substring(0, retxdateStr.length() - 2);

		return Integer.valueOf(retxMonStr);
	}
}
