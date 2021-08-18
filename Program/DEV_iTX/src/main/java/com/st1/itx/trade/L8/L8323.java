package com.st1.itx.trade.L8;

import java.util.ArrayList;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.domain.JcicZ571Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ571Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

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

@Service("L8323")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8323 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ571Service sJcicZ571Service;
	@Autowired
	public JcicCom jcicCom;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8323 ");
		this.totaVo.init(titaVo);
		String FunctionCd = titaVo.getParam("FunctionCd").trim(); // 功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey = titaVo.getParam("TranKey").trim(); // 交易代碼
		String CustId = titaVo.getParam("CustId").trim();// 債務人IDN
		String SubmitKey = titaVo.getParam("SubmitKey").trim();// 報送單位代號
		String ApplyDate = titaVo.getParam("ApplyDate").trim();// 款項統一收復申請日
		String BankId = titaVo.getParam("BankId").trim();// 受理款項統一收付之債權金融機構代號
		String OutJcicTxtDate = titaVo.getParam("OutJcicTxtDate").trim();// 轉出Jcic文字檔日期

		this.info("L8323 Key ApplyDate=[" + ApplyDate + "],CustId=[" + CustId + "],BankId=[" + BankId + "],SubmitKey=[" + SubmitKey + "]");
		// int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		// JcicMAaster
		JcicZ571 tJcicZ571 = new JcicZ571();
		JcicZ571Id tJcicZ571Id = new JcicZ571Id();
		tJcicZ571Id.setCustId(CustId);// 債務人IDN
		tJcicZ571Id.setSubmitKey(SubmitKey);// 報送單位代號
		tJcicZ571Id.setApplyDate(parse.stringToInteger(jcicCom.RocTurnDc(ApplyDate, 0)));// 款項統一收復申請日
		tJcicZ571Id.setBankId(BankId);
		tJcicZ571.setJcicZ571Id(tJcicZ571Id);
		this.info("L8323BankID" + tJcicZ571Id.getBankId());

		tJcicZ571.setTranKey(TranKey);// 交易代碼
		tJcicZ571.setOwnerYn(titaVo.getParam("OwnerYn").trim());// 是否為更生債權人
		tJcicZ571.setPayYn(titaVo.getParam("PayYn").trim());// 債務人是否仍依更生方案正常還款予本金融機構
		tJcicZ571.setOwnerAmt(parse.stringToInteger(titaVo.getParam("OwnerAmt").trim()));// 本金融機構更生債權總金額
		tJcicZ571.setAllotAmt(parse.stringToInteger(titaVo.getParam("AllotAmt").trim()));// 參與分配債權金額
		tJcicZ571.setUnallotAmt(parse.stringToInteger(titaVo.getParam("UnallotAmt").trim()));// 未參與分配債權金額

		// OutJcicTxtDate 可以刪除不可異動
		if (jcicCom.JcicOutDateCanUpdByUser(titaVo) == true) {
			tJcicZ571.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate, 0)));
		} else {
			tJcicZ571.setOutJcicTxtDate(0);
		}
		JcicZ571 tJcicZ571VO = sJcicZ571Service.holdById(tJcicZ571Id, titaVo);
		JcicZ571 OrgJcicZ571 = null;
		if (tJcicZ571VO != null) {
			OrgJcicZ571 = (JcicZ571) dataLog.clone(tJcicZ571VO);// 資料異動前
		}

		this.info("tJcicZ571VO=[" + tJcicZ571.toString() + "]");

		if ((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF = jcicCom.DeleteLogic(titaVo, tJcicZ571VO, tJcicZ571VO.getOutJcicTxtDate());
			if (DeleteTF) {
				// 刷主管卡後始可刪除
				// 交易需主管核可
				if (("A").equals(OrgJcicZ571.getTranKey())) {

				} else {
					// 刷主管卡後始可刪除
					// 交易需主管核可
					if (!titaVo.getHsupCode().equals("1")) {
						// titaVo.getSupCode();
						sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
					}
				}
				// 刪除
				try {
					sJcicZ571Service.delete(tJcicZ571VO, titaVo);
				} catch (DBException e) {
					// E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		} else {
			if (tJcicZ571VO != null) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				// UPDATE
				// KeyValue
				tJcicZ571.setCreateDate(tJcicZ571VO.getCreateDate());
				tJcicZ571.setCreateEmpNo(tJcicZ571VO.getCreateEmpNo());
				if (OutJcicTxtDate != null && OutJcicTxtDate.length() != 0) {
					if (Integer.parseInt(OutJcicTxtDate) == 0) {
						tJcicZ571.setOutJcicTxtDate(0);
					}
				} else {
					tJcicZ571.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate, 0)));
				}

				tJcicZ571.setCreateDate(OrgJcicZ571.getCreateDate());
				tJcicZ571.setCreateEmpNo(OrgJcicZ571.getCreateEmpNo());
				try {
					tJcicZ571 = sJcicZ571Service.update2(tJcicZ571, titaVo);// 資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ571, tJcicZ571);// 資料異動後-2
					dataLog.exec();// 資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			} else {
				// INSERT
				TranKey = "A";
				tJcicZ571.setTranKey(TranKey);
				try {
					sJcicZ571Service.insert(tJcicZ571, titaVo);
				} catch (DBException e) {
					// E0005 新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "");
				}

			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}