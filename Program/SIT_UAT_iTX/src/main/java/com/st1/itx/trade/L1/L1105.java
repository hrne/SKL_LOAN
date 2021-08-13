package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L1105")
@Scope("prototype")
public class L1105 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L1105.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public CustTelNoService sCustTelNoService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	private boolean isEloan = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1105 ");
		this.totaVo.init(titaVo);

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		this.totaVo.putParam("OResult", "N");
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		String iCustId = titaVo.getParam("CustId");
		String iCustUKey = "";
		int count = 1;
		CustMain iCustMain = new CustMain();
		// check if input id or no exist in custmain
		if (iCustNo == 0) {
			// check custid
			iCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		} else {
			// check custno
			iCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		}

		if (iCustMain == null) {
			throw new LogicException("E1003", "客戶資料主檔");
		} else {
			iCustUKey = iCustMain.getCustUKey();
		}

		// start deal with data
		CustTelNo tCustTelNo = new CustTelNo();
//		CustTelNo hCustTelNo = new CustTelNo();
//		Slice<CustTelNo> xCustTelNo = null;
		String iFuncFg = titaVo.getParam("FunCd");
		String iTelTypeCode = titaVo.getParam("TelTypeCode");
		String iTelArea = titaVo.getParam("TelArea");
		String iTelNo = titaVo.getParam("TelNo");
		String iTelExt = titaVo.getParam("TelExt");
		String iTelOther = StringUtils.rightPad(titaVo.getParam("TelOther"), 20," ");
		String iTelChgRsnCode = titaVo.getParam("TelChgRsnCode");
		String iRelationCode = titaVo.getParam("RelationCode");
		String iLiaisonName = titaVo.getParam("LiaisonName");
		String iRmk = titaVo.getParam("Rmk");
		String iStopReason = titaVo.getParam("StopReason");
		String iEnable = titaVo.getParam("Enable");
		String tita_TelNoUKey = titaVo.getParam("TelNoUKey");
//		int iCustTelSeq = 1;

		// 處理電話格式(若有需要)
		if (iTelTypeCode.equals("09")) {
			iTelArea = iTelOther.substring(0, 5);
			iTelNo = iTelOther.substring(5, 15);
			iTelExt = iTelOther.substring(15);
		}
		
		if (isEloan && "1".equals(iFuncFg)) {
			String telNoUKey = uniqueEloan(iCustUKey,iTelTypeCode,iTelArea,iTelNo,iTelExt);
			if (!"".equals(telNoUKey)) {
				iFuncFg = "2";
				tita_TelNoUKey = telNoUKey;
			}
		}
		
		if (iFuncFg.equals("1")) {
			String new_telNoUKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			tCustTelNo.setTelNoUKey(new_telNoUKey);
			tCustTelNo.setCustUKey(iCustUKey);
//			tCustTelNo.setCustTelSeq(iCustTelSeq);
			tCustTelNo.setTelTypeCode(iTelTypeCode);
			tCustTelNo.setTelArea(iTelArea);
			tCustTelNo.setTelNo(iTelNo);
			tCustTelNo.setTelExt(iTelExt);
			tCustTelNo.setTelChgRsnCode(iTelChgRsnCode);
			tCustTelNo.setRelationCode(iRelationCode);
			tCustTelNo.setLiaisonName(iLiaisonName);
			tCustTelNo.setRmk(iRmk);
			tCustTelNo.setStopReason(iStopReason);
			tCustTelNo.setEnable(iEnable);
//			tCustTelNo.setCreateEmpNo(titaVo.getTlrNo());
			try {
				sCustTelNoService.insert(tCustTelNo, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "客戶聯絡電話檔");
			}
		} else {
			tCustTelNo = sCustTelNoService.holdById(tita_TelNoUKey, titaVo);
			
			if (tCustTelNo == null) {
				throw new LogicException("E0003", "客戶聯絡電話檔");
			}
//			hCustTelNo = sCustTelNoService.findById(tita_TelNoUKey, titaVo);
			// 變更前
			CustTelNo beforeCustTelNo = (CustTelNo) dataLog.clone(tCustTelNo);
			tCustTelNo.setTelTypeCode(iTelTypeCode);
			tCustTelNo.setTelArea(iTelArea);
			tCustTelNo.setTelNo(iTelNo);
			tCustTelNo.setTelExt(iTelExt);
			tCustTelNo.setTelChgRsnCode(iTelChgRsnCode);
			tCustTelNo.setRelationCode(iRelationCode);
			tCustTelNo.setLiaisonName(iLiaisonName);
			tCustTelNo.setRmk(iRmk);
			tCustTelNo.setStopReason(iStopReason);
			tCustTelNo.setEnable(iEnable);
//			tCustTelNo.setCreateEmpNo(tCustTelNo.getCreateEmpNo());
//			tCustTelNo.setLastUpdateEmpNo(titaVo.getTlrNo());
			try {
				sCustTelNoService.update(tCustTelNo, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "客戶聯絡電話檔");
			}
			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeCustTelNo, tCustTelNo);
			dataLog.exec();
		}

		this.totaVo.putParam("OResult", "Y");

		this.addList(this.totaVo);
		return this.sendList();
	}
	
	private String uniqueEloan(String iCustUKey,String iTelTypeCode,String iTelArea,String iTelNo,String iTelExt) {
		String telNoUKey = "";
		Slice<CustTelNo> sCustTelNo = sCustTelNoService.findCustUKey(iCustUKey, 0, Integer.MAX_VALUE);
		List<CustTelNo> lCustTelNo = sCustTelNo == null ? null : sCustTelNo.getContent();
		
		if (lCustTelNo != null) {
			for (CustTelNo custTelNo : lCustTelNo) {
				if (iTelTypeCode.equals(custTelNo.getTelTypeCode()) && iTelArea.equals(custTelNo.getTelArea()) && iTelNo.equals(custTelNo.getTelNo()) && iTelExt.equals(custTelNo.getTelExt())) {
					telNoUKey = custTelNo.getTelNoUKey(); 
					break;
				}
			}
		} 

		return telNoUKey; 
	}
}