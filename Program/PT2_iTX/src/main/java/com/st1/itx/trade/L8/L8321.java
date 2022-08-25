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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.domain.JcicZ062Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ063;
import com.st1.itx.db.domain.JcicZ063Id;
import com.st1.itx.db.domain.JcicZ063Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ062Service;
import com.st1.itx.db.service.JcicZ063LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ063Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8321")
@Scope("prototype")
/**
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8321 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ062Service sJcicZ062Service;
	@Autowired
	public JcicZ063Service sJcicZ063Service;
	@Autowired
	public JcicZ063LogService sJcicZ063LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8321 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim();
		String iCustId = titaVo.getParam("CustId").trim();
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate").trim());
		int iChangePayDate = Integer.valueOf(titaVo.getParam("ChangePayDate").trim());
		int iClosedDate = Integer.valueOf(titaVo.getParam("ClosedDate").trim());
		String iClosedResult = titaVo.getParam("ClosedResult").trim();
		String iKey = "";

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);

		// JcicZ063, JcicZ062
		JcicZ063 iJcicZ063 = new JcicZ063();
		JcicZ063Id iJcicZ063Id = new JcicZ063Id();
		iJcicZ063Id.setSubmitKey(iSubmitKey);
		iJcicZ063Id.setCustId(iCustId);
		iJcicZ063Id.setRcDate(iRcDate);
		iJcicZ063Id.setChangePayDate(iChangePayDate);
		JcicZ063 chJcicZ063 = new JcicZ063();
		JcicZ062 iJcicZ062 = new JcicZ062();
		JcicZ062Id iJcicZ062Id = new JcicZ062Id();
		iJcicZ062Id.setSubmitKey(iSubmitKey);
		iJcicZ062Id.setCustId(iCustId);
		iJcicZ062Id.setRcDate(iRcDate);
		iJcicZ062Id.setChangePayDate(iChangePayDate);

		// 檢核項目(D-39)
		if (!"4".equals(iTranKey_Tmp)) {
			// 1.2 KEY值為IDN+報送單位wakg+dr前置協商申請日+申請變更還款條件日.***J

			// 1.3 同一KEY值資料變更還款條件結案日期不可早於申請變更還款條件日，也不可晚於本檔案資料報送日期--->(前端檢核)

			// 1.4 報送本結案檔案後，同一KEY值不可再報送相關檔案之異動.***J

			// 1.5 start 第9欄「結案原因」為'C'者，同一KEY值之「'62':金融機構無擔保債務變更還款條件協議資料」第17欄「簽約完成日」必須有值.
			iJcicZ062 = sJcicZ062Service.findById(iJcicZ062Id, titaVo);
			if (iJcicZ062 == null) {
				if ("C".equals(iClosedResult)) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "「結案原因」為'C'者，同一KEY值需先報送(62)金融機構無擔保債務變更還款條件協議資料.");
					} else {
						throw new LogicException("E0007", "「結案原因」為'C'者，同一KEY值需先報送(62)金融機構無擔保債務變更還款條件協議資料.");
					}
				}
			} else {
				if ("C".equals(iClosedResult) && (iJcicZ062.getChaRepayEndDate() == 0)) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "「結案原因」為'C'者，同一KEY值之(62)金融機構無擔保債務變更還款條件協議資料之「簽約完成日」必須有值.");
					} else {
						throw new LogicException("E0007", "「結案原因」為'C'者，同一KEY值之(62)金融機構無擔保債務變更還款條件協議資料之「簽約完成日」必須有值.");
					}
				} // 1.5 end

				// 1.6 start
				// 第9欄「結案原因」為'A'及'B'者，同一KEY值之「'62':金融機構無擔保債務變更還款條件協議資料」第17欄「簽約完成日」必須空白.
				if (("A".equals(iClosedResult) || "B".equals(iClosedResult)) && (iJcicZ062.getChaRepayEndDate() != 0)) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005",
								"「結案原因」為'A'及'B'者，同一KEY值之(62)金融機構無擔保債務變更還款條件協議資料之「簽約完成日」必須空白.");
					} else {
						throw new LogicException("E0007",
								"「結案原因」為'A'及'B'者，同一KEY值之(62)金融機構無擔保債務變更還款條件協議資料之「簽約完成日」必須空白.");
					}
				} // 1.6 end
			}

			// 2 各資料檔案格式(60-62)如有資料key值報送錯誤情形者，需以本檔案格式報送結案資料至本中心，並重新報送一筆原檔案資料.***J

			// 檢核條件 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ063
			chJcicZ063 = sJcicZ063Service.findById(iJcicZ063Id, titaVo);
			if (chJcicZ063 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ063.setJcicZ063Id(iJcicZ063Id);
			iJcicZ063.setTranKey(iTranKey);
			iJcicZ063.setClosedDate(iClosedDate);
			iJcicZ063.setClosedResult(iClosedResult);
			iJcicZ063.setUkey(iKey);
			try {
				sJcicZ063Service.insert(iJcicZ063, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ063 = sJcicZ063Service.ukeyFirst(iKey, titaVo);
			JcicZ063 uJcicZ063 = new JcicZ063();
			uJcicZ063 = sJcicZ063Service.holdById(iJcicZ063.getJcicZ063Id(), titaVo);
			if (uJcicZ063 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ063 oldJcicZ063 = (JcicZ063) iDataLog.clone(uJcicZ063);
			uJcicZ063.setClosedDate(iClosedDate);
			uJcicZ063.setClosedResult(iClosedResult);
			uJcicZ063.setTranKey(iTranKey);
			uJcicZ063.setOutJcicTxtDate(0);
			try {
				sJcicZ063Service.update(uJcicZ063, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ063, uJcicZ063);
			iDataLog.exec("L8321異動", uJcicZ063.getSubmitKey() + uJcicZ063.getCustId() + uJcicZ063.getRcDate()
					+ uJcicZ063.getChangePayDate());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ063 = sJcicZ063Service.ukeyFirst(iKey, titaVo);
			JcicZ063 uJcicZ0632 = new JcicZ063();
			uJcicZ0632 = sJcicZ063Service.holdById(iJcicZ063.getJcicZ063Id(), titaVo);
			iJcicZ063 = sJcicZ063Service.findById(iJcicZ063Id);
			if (iJcicZ063 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			JcicZ063 oldJcicZ0632 = (JcicZ063) iDataLog.clone(uJcicZ0632);
			uJcicZ0632.setClosedDate(iClosedDate);
			uJcicZ0632.setClosedResult(iClosedResult);
			uJcicZ0632.setTranKey(iTranKey);
			uJcicZ0632.setOutJcicTxtDate(0);

			Slice<JcicZ063Log> dJcicLogZ063 = null;
			dJcicLogZ063 = sJcicZ063LogService.ukeyEq(iJcicZ063.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ063 == null || ("A".equals(iTranKey) && dJcicLogZ063 == null)) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ063Service.delete(iJcicZ063, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ063Log iJcicZ063Log = dJcicLogZ063.getContent().get(0);
				iJcicZ063.setClosedDate(iJcicZ063Log.getClosedDate());
				iJcicZ063.setClosedResult(iJcicZ063Log.getClosedResult());
				iJcicZ063.setTranKey(iJcicZ063Log.getTranKey());
				iJcicZ063.setOutJcicTxtDate(iJcicZ063Log.getOutJcicTxtDate());
				try {
					sJcicZ063Service.update(iJcicZ063, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ0632, uJcicZ0632);
			iDataLog.exec("L8321刪除", uJcicZ0632.getSubmitKey() + uJcicZ0632.getCustId() + uJcicZ0632.getRcDate()
					+ uJcicZ0632.getChangePayDate());
			break;
		// 修改
		case "7":
			iKey = titaVo.getParam("Ukey");
			iJcicZ063 = sJcicZ063Service.ukeyFirst(iKey, titaVo);
			JcicZ063 uJcicZ0633 = new JcicZ063();
			uJcicZ0633 = sJcicZ063Service.holdById(iJcicZ063.getJcicZ063Id(), titaVo);
			if (uJcicZ0633 == null) {
				throw new LogicException("E0007", "更生債權金額異動通知資料");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate3 = iJcicZ063.getOutJcicTxtDate();
			this.info("JcicDate    = " + JcicDate3);
			if (JcicDate3 != 0) {
				throw new LogicException("E0007", "無此修改資料");
			}

			JcicZ063 oldJcicZ0633 = (JcicZ063) iDataLog.clone(uJcicZ0633);
			uJcicZ0633.setJcicZ063Id(iJcicZ063Id);
			uJcicZ0633.setTranKey(iTranKey);
			iJcicZ063.setClosedDate(iClosedDate);
			iJcicZ063.setClosedResult(iClosedResult);
			uJcicZ0633.setUkey(iKey);

			try {
				sJcicZ063Service.update(uJcicZ0633, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			iDataLog.setEnv(titaVo, oldJcicZ0633, uJcicZ0633);
			iDataLog.exec("L8321修改", uJcicZ0633.getSubmitKey() + uJcicZ0633.getCustId() + uJcicZ0633.getRcDate()
					+ uJcicZ0633.getChangePayDate());
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}