package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R35")
@Scope("prototype")

public class L4R35 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R35 ");
		this.totaVo.init(titaVo);

		int iFuncCode = parse.stringToInteger(titaVo.getParam("RimFunCode"));
		int iAcDate = parse.stringToInteger(titaVo.getParam("RimAcDate"));
		String iBatchNo = titaVo.getParam("RimBatchNo");
		int iDetailSeq = parse.stringToInteger(titaVo.getParam("RimDetailSeq"));

		BatxDetail tBatxDetail = batxDetailService.findById(new BatxDetailId(iAcDate + 19110000, iBatchNo, iDetailSeq),
				titaVo);
		if (iFuncCode != 1 && tBatxDetail == null) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		this.totaVo.putParam("L4r35CustNo", tBatxDetail.getCustNo());
		this.totaVo.putParam("L4r35RepayType", tBatxDetail.getRepayType());
		this.totaVo.putParam("L4r35RepayAmt", tBatxDetail.getRepayAmt());
		this.totaVo.putParam("L4r35AcctAmt", tBatxDetail.getAcctAmt());
		this.totaVo.putParam("L4r35DisacctAmt", tBatxDetail.getDisacctAmt());
		this.totaVo.putParam("L4r35ProcStsCode", tBatxDetail.getProcStsCode());
		this.totaVo.putParam("L4r35ProcCode", tBatxDetail.getProcCode());

		String procNote = "";

		if (!tBatxDetail.getProcNote().isEmpty()) {

			TempVo tempVo = new TempVo();
			tempVo = tempVo.getVo(tBatxDetail.getProcNote());

			if (tempVo.get("ReturnMsg") != null && tempVo.get("ReturnMsg").length() > 0) {
				procNote += "回應訊息:" + tempVo.get("ReturnMsg") + " ";
			}
			if (tempVo.get("EraseCnt") != null) {
				procNote += "訂正" + tempVo.get("EraseCnt") + "次 ";
			}

			if (tempVo.get("CheckMsg") != null && tempVo.get("CheckMsg").length() > 0) {
				procNote += "檢核訊息:" + tempVo.get("CheckMsg") + " ";
			}

			if (tempVo.get("ErrorMsg") != null && tempVo.get("ErrorMsg").length() > 0) {
				procNote += "錯誤訊息:" + tempVo.get("ErrorMsg") + " ";
			}

			if (tBatxDetail.getRepayType() == 9) {
				if (tempVo.get("TempReasonCodeX") != null && tempVo.get("TempReasonCodeX").length() > 0) {
					procNote = procNote + "暫收原因:" + tempVo.get("TempReasonCodeX") + " ";
				}
				if (tempVo.get("CheckMsgOrg") != null && tempVo.get("CheckMsgOrg").length() > 0) {
					procNote = procNote + "原檢核訊息:" + tempVo.get("CheckMsgOrg") + " ";
				}
			}
			if (tBatxDetail.getRepayType() == 1) {
				if (tempVo.get("PrePaidTerms") != null && parse.stringToInteger(tempVo.get("PrePaidTerms")) > 0) {
					procNote = procNote + "預繳期數:" + tempVo.get("PrePaidTerms") + "期 ";
				}
			}
		}

		this.totaVo.putParam("L4r35ProcDscptCode", procNote);
		if (tBatxDetail.getTitaTxtNo().isEmpty()) {
			this.totaVo.putParam("L4r35TxSn", "");
		} else {
			this.totaVo.putParam("L4r35TxSn", tBatxDetail.getTitaTlrNo()
					+ parse.IntegerToString(parse.stringToInteger(tBatxDetail.getTitaTxtNo()), 8));
		}
		this.totaVo.putParam("L4r35FacmNoA", tBatxDetail.getFacmNo());

		this.addList(this.totaVo);
		return this.sendList();
	}
}