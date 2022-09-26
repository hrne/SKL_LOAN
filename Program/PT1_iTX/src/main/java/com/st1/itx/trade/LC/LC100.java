package com.st1.itx.trade.LC;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.domain.CdBranchGroup;
import com.st1.itx.db.domain.CdBranchGroupId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.CdBranchGroupService;
import com.st1.itx.db.service.CdBranchService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxRecordService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("LC100")
@Scope("prototype")
/**
 * 使用者登入
 *
 * @author AdamPan
 * @version 1.0.0
 */
public class LC100 extends TradeBuffer {

	@Autowired
	private TxTellerService txTellerService;

	@Autowired
	private TxRecordService txRecordService;

	@Autowired
	private CdEmpService tCdEmpService;

	@Autowired
	private CdBranchService sCdBranchService;

	@Autowired
	private CdBranchGroupService sCdBranchGroupService;

	@Autowired
	private Parse parse;

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC100 ");
		this.totaVo.init(titaVo);

		CdEmp tCdEmp = tCdEmpService.findById(titaVo.getTlrNo(), titaVo);

		if (tCdEmp != null) {
			if (tCdEmp.getFullname().trim().isEmpty()) {
				throw new LogicException("EC001", "員工資料檔員編姓名空白:" + titaVo.getTlrNo());
			}
		} else {
			throw new LogicException("EC001", "員工資料檔員編不存在:" + titaVo.getTlrNo());
		}

		TxTeller tTxTeller = txTellerService.holdById(titaVo.getTlrNo());

		if (tTxTeller != null) {
			try {
//				this.totaVo.putParam(ContentName.msgId, this.getClass().getSimpleName());
				this.totaVo.putParam(ContentName.brno, tTxTeller.getBrNo());
				CdBranch tCdBranch = sCdBranchService.findById(tTxTeller.getBrNo());
				if (tCdBranch == null) {
					throw new LogicException("EC001", "營業單位:" + tTxTeller.getBrNo());
				}

				CdBranchGroup tCdBranchGroup = sCdBranchGroupService.findById(new CdBranchGroupId(tTxTeller.getBrNo(), tTxTeller.getGroupNo()), titaVo);
				if (tCdBranchGroup == null) {
					throw new LogicException("EC001", "營業單位課組別檔:" + tTxTeller.getGroupNo());
				}

				TxRecord txRecord = txRecordService.findEntdyFirst(titaVo.getEntDyI(), titaVo.getTlrNo());

				String s = "";
				s = tCdBranchGroup.getGroupItem();

				s = s.trim();
				this.totaVo.putParam("BRNAME", tCdBranch.getBranchItem());
				this.totaVo.putParam("BANKNO", "");
				this.totaVo.putParam("FXLVL", "0");
				this.totaVo.putParam("BCURCD", "00");
				this.totaVo.putParam("FXBRNO", tTxTeller.getBrNo());
				this.totaVo.putParam("FINBRNO", tTxTeller.getBrNo());
				this.totaVo.putParam("OBUBRNO", tTxTeller.getBrNo());
				this.totaVo.putParam("LEVEL", tTxTeller.getLevelFg());
				this.totaVo.putParam("NAME", tCdEmp.getFullname());

				this.totaVo.putParam("EMPNO", "");
				this.totaVo.putParam("TXGRP", "1");
				if (tTxTeller.getTxtNo() >= 999500)
					tTxTeller.setTxtNo(1);

				this.totaVo.putParam("TXTNO", tTxTeller.getTxtNo());
				if (!Objects.isNull(txRecord))
					this.totaVo.putParam("TXTNO", (parse.stringToInteger(txRecord.getTxSeq()) + 1));

				// MODE 0.本日 1.次日
//				if (tTxTeller.getEntdy() == this.txBuffer.getMgBizDate().getNbsDy()) {
//				this.totaVo.putParam("MODE", "1");
//				this.totaVo.putParam("TXDATE", txBuffer.getMgBizDate().getNbsDy());
//				this.totaVo.putParam("NBSDY", txBuffer.getMgBizDate().getNnbsDy());
//				this.totaVo.putParam("NNBSDY", dDateUtil.getbussDate(txBuffer.getMgBizDate().getNnbsDy(), -1));
//				this.totaVo.putParam("LBSDY", txBuffer.getMgBizDate().getTbsDy());
//				} else {
				this.totaVo.putParam("MODE", "0");
				tTxTeller.setEntdy(this.txBuffer.getMgBizDate().getTbsDy());
				this.totaVo.putParam("TXDATE", txBuffer.getMgBizDate().getTbsDy());
				this.totaVo.putParam("NBSDY", txBuffer.getMgBizDate().getNbsDy());
				this.totaVo.putParam("NNBSDY", txBuffer.getMgBizDate().getNnbsDy());
				this.totaVo.putParam("LBSDY", txBuffer.getMgBizDate().getLbsDy());
				this.totaVo.putParam("MFBSDY", txBuffer.getMgBizDate().getMfbsDy());
//				}

				this.totaVo.putParam("DBTO", tTxTeller.getReportDb());
				this.totaVo.putParam("SWFCD", "");
				this.totaVo.putParam("LSNTAMT", "0");
				this.totaVo.putParam("LLSNTAMT", "0");

				this.totaVo.putParam("CLDEPT", tTxTeller.getGroupNo());
				this.totaVo.putParam("CLDEPTNA", s);
				this.totaVo.putParam("DAPKND", "11111111111111111111111111");
				this.totaVo.putParam("OAPKND", "11111111111111111111111111");
				this.totaVo.putParam("MBRNO", tTxTeller.getBrNo());
				if (!"1".equals(tTxTeller.getAdFg())) {
					this.totaVo.putParam("ADNO", tTxTeller.getTlrNo());
				} else {
					this.totaVo.putParam("ADNO", "");

				}
				this.totaVo.putParam("PSWD", titaVo.get("PSWD"));
				this.totaVo.putParam("DISFORM", "1");
				this.totaVo.putParam("LSLOGIN", new String("0" + tTxTeller.getLastDate() + "" + tTxTeller.getLastTime()));
//
				Date date = new Date();
//				SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat tt = new SimpleDateFormat("HHmmss");

				int loginTime = Integer.parseInt(FormatUtil.pad9(tt.format(date), 6));
//				int loginDate = Integer.parseInt(dt.format(date));

				tTxTeller.setLogonFg(1);
				tTxTeller.setLastDate(parse.stringToInteger(titaVo.getCalDy()));
				tTxTeller.setLastTime(loginTime);
//
				txTellerService.update(tTxTeller);
			} catch (DBException e) {
				this.totaVo.setTxrsutE();
				this.totaVo.setErrorMsgId(new String("" + e.getErrorId()));
				this.totaVo.setErrorMsg(e.getErrorMsg());
			}

		} else {
			throw new LogicException("EC001", "員編:" + titaVo.getTlrNo());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}