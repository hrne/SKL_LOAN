package com.st1.itx.trade.L4;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;
import com.st1.itx.db.domain.TxToDoMain;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.service.TxToDoMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.parse.Parse;

@Service("L420A")
@Scope("prototype")
/**
 * 啟動整批檢核作業
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L420A extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public BatxHeadService batxHeadService;

	@Autowired
	TxToDoMainService txToDoMainService;

	@Autowired
	SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L420A ");
		this.totaVo.init(titaVo);

		// 會計日期、批號
		int iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		String iBatchNo = titaVo.getParam("BatchNo");

		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(iAcDate);
		tBatxHeadId.setBatchNo(iBatchNo);
		BatxHead tBatxHead = batxHeadService.findById(tBatxHeadId);// find 整批入帳總數檔
		if (tBatxHead == null) {
			throw new LogicException("E0014", tBatxHeadId + " not exist"); // E0014 檔案錯誤
		}
		// BatxExeCode 作業狀態 1.檢核有誤 2.檢核正常 3.入帳未完 4.入帳完成 8.已刪除
		if ("8".equals(tBatxHead.getBatxExeCode())) {
			throw new LogicException("E0010", "作業狀態不符"); // E0010 功能選擇錯誤
		}

		// BatxStsCode 整批作業狀態 0.正常 1.整批處理中
		if ("1".equals(tBatxHead.getBatxStsCode()) && !titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0004", "整批處理中");
		} else {
			// 啟動背景作業－整批檢核
			MySpring.newTask("L420ABatch", this.txBuffer, titaVo);
		}

		/* 每月21日(遇假日順延)，火險保費未繳轉借支 */
		TxToDoMain tTxToDoMain = txToDoMainService.findById("L4604", titaVo);
		if (tTxToDoMain != null && tTxToDoMain.getUnProcessCnt() > 0) {
			this.totaVo.setWarnMsg("整批入帳前需先執行<L4604-火險保費未繳轉借支>作業(每月21日)");
		}

		// end
		this.addList(this.totaVo);
		return this.sendList();

	}

}