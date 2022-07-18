package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ570Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.domain.JcicZ575Id;
import com.st1.itx.db.domain.JcicZ575Log;
import com.st1.itx.db.service.JcicZ570Service;
import com.st1.itx.db.service.JcicZ575LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ575Service;
import com.st1.itx.db.service.springjpa.cm.L8337ServiceImpl;
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

@Service("L8337")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8337 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ570Service sJcicZ570Service;
	@Autowired
	public L8337ServiceImpl sL8337ServiceImpl;
	@Autowired
	public JcicZ575Service sJcicZ575Service;
	@Autowired
	public JcicZ575LogService sJcicZ575LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8337 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iModifyType = titaVo.getParam("ModifyType");
		String iBankId = titaVo.getParam("BankId");
		String iKey = "";
		this.info("iApplyDate"   + iApplyDate);
		this.info("iBankId"     + iBankId);
		this.info("iCustId"    + iCustId);
		this.info("iSubmitKey"  +  iSubmitKey);
		// JcicZ575
		JcicZ575 iJcicZ575 = new JcicZ575();
		JcicZ575Id iJcicZ575Id = new JcicZ575Id();
		iJcicZ575Id.setApplyDate(iApplyDate);
		iJcicZ575Id.setBankId(iBankId);
		iJcicZ575Id.setCustId(iCustId);
		iJcicZ575Id.setSubmitKey(iSubmitKey);
		JcicZ575 chJcicZ575 = new JcicZ575();
		JcicZ570 iJcicZ570 = new JcicZ570();
		JcicZ570Id iJcicZ570Id = new JcicZ570Id();
		iJcicZ570Id.setApplyDate(iApplyDate);
		iJcicZ570Id.setCustId(iCustId);
		iJcicZ570Id.setSubmitKey(iSubmitKey);
		List<Map<String, String>> iL8337SqlReturn = new ArrayList<Map<String, String>>();
		this.info("iJcicZ570Id"   + iJcicZ570Id);
		// 檢核項目(D-78)
		if (!"4".equals(iTranKey_Tmp)) {

			// 二 key值為「債務人IDN+報送單位代號+申請日期+異動債權金機構代號」，不可重複，重複者予以剔退--->檢核在case "1"

			// 三 start 若異動債權金融機構代號不存在同一更生款項統一收付案件'570'檔案之債權金融機構代號者，予以剔退
			if ("A".equals(iTranKey)) {
				try {
					iL8337SqlReturn = sL8337ServiceImpl.findData(this.index, this.limit, iCustId, iBankId, titaVo);
				} catch (Exception e) {
					// E5004 讀取DB語法發生問題
					this.info("L5024 ErrorForSql=" + e);
					throw new LogicException(titaVo, "E5004", "");
				}
				// 三end
			}

		}
		// 檢核項目end

		switch (iTranKey_Tmp) {
		case "1":
			iJcicZ570 = sJcicZ570Service.findById(iJcicZ570Id, titaVo);
			this.info("iJcicZ570" + iJcicZ570);
			if (iJcicZ570 == null) {
				throw new LogicException(titaVo, "E0005", "異動債權金融機構代號不存在於同一更生款項統一收付案件'570'檔案之債權金融機構代號");
			}
			// 檢核是否重複，並寫入JcicZ575
			chJcicZ575 = sJcicZ575Service.findById(iJcicZ575Id, titaVo);
			if (chJcicZ575 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ575.setJcicZ575Id(iJcicZ575Id);
			iJcicZ575.setTranKey(iTranKey);
			iJcicZ575.setModifyType(iModifyType);
			iJcicZ575.setUkey(iKey);
			try {
				sJcicZ575Service.insert(iJcicZ575, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			break;
		case "2":
			iJcicZ570 = sJcicZ570Service.findById(iJcicZ570Id, titaVo);
			this.info("iJcicZ570" + iJcicZ570);
			if (iJcicZ570 == null) {
				throw new LogicException(titaVo, "E0005", "異動債權金融機構代號不存在於同一更生款項統一收付案件'570'檔案之債權金融機構代號");
			}
			iKey = titaVo.getParam("Ukey");
			iJcicZ575 = sJcicZ575Service.ukeyFirst(iKey, titaVo);
			JcicZ575 uJcicZ575 = new JcicZ575();
			uJcicZ575 = sJcicZ575Service.holdById(iJcicZ575.getJcicZ575Id(), titaVo);
			if (uJcicZ575 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ575 oldJcicZ575 = (JcicZ575) iDataLog.clone(uJcicZ575);
			uJcicZ575.setModifyType(iModifyType);
			uJcicZ575.setTranKey(iTranKey);
			uJcicZ575.setOutJcicTxtDate(0);
			try {
				sJcicZ575Service.update(uJcicZ575, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ575, uJcicZ575);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ575 = sJcicZ575Service.ukeyFirst(iKey, titaVo);
			JcicZ575 uJcicZ5752 = new JcicZ575();
			uJcicZ5752 = sJcicZ575Service.holdById(iJcicZ575.getJcicZ575Id(), titaVo);
			iJcicZ575 = sJcicZ575Service.findById(iJcicZ575Id);
			if (iJcicZ575 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			JcicZ575 oldJcicZ5752 = (JcicZ575) iDataLog.clone(uJcicZ5752);
			uJcicZ5752.setModifyType(iModifyType);
			uJcicZ5752.setTranKey(iTranKey);
			uJcicZ5752.setOutJcicTxtDate(0);
			
			Slice<JcicZ575Log> dJcicLogZ575 = null;
			dJcicLogZ575 = sJcicZ575LogService.ukeyEq(iJcicZ575.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ575 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ575Service.delete(iJcicZ575, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ575Log iJcicZ575Log = dJcicLogZ575.getContent().get(0);
				iJcicZ575.setModifyType(iJcicZ575Log.getModifyType());
				iJcicZ575.setTranKey(iJcicZ575Log.getTranKey());
				iJcicZ575.setOutJcicTxtDate(iJcicZ575Log.getOutJcicTxtDate());
				try {
					sJcicZ575Service.update(iJcicZ575, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ5752, uJcicZ5752);
			iDataLog.exec();
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}