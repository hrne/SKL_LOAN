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
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.parse.Parse;

@Service("L420B")
@Scope("prototype")
/**
 * 整批入帳
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L420B extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public BatxHeadService batxHeadService;

	@Autowired
	SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L420B ");
		this.totaVo.init(titaVo);

		// 處理代碼 0:入帳 1:刪除 2:訂正
		int functionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));

		// 會計日期、批號
		int iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		String iBatchNo = titaVo.getParam("BatchNo");
		String iReconCode = "";
		if (titaVo.get("ReconCode") != null) {
			iReconCode = titaVo.getParam("ReconCode").trim();
		}

		// 檢查作業狀態
		checkHead(functionCode, iAcDate, iBatchNo, titaVo);

		// 啟動背景作業－整批入帳
		MySpring.newTask("BS401", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	/* 檢查作業狀態 */
	private void checkHead(int functionCode, int acDate, String batchNo, TitaVo titaVo) throws LogicException {
		// find 整批入帳總數檔
		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(acDate);
		tBatxHeadId.setBatchNo(batchNo);
		BatxHead tBatxHead = batxHeadService.findById(tBatxHeadId);
		if (tBatxHead == null) {
			throw new LogicException("E0014", tBatxHeadId + " not exist"); // E0014 檔案錯誤
		}

// BatxStsCode 整批作業狀態 0.正常 1.整批處理中
		if ("1".equals(tBatxHead.getBatxStsCode())) {
			if (!titaVo.getHsupCode().equals("1")) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "整批處理中");
			}
		}
// 處理代碼 0:入帳 1:刪除 2:訂正　4.刪除回復
// BatxExeCode 作業狀態 1.檢核有誤 2.檢核正常 3.入帳未完 4.入帳完成 8.已刪除	

		String batxExeCode = tBatxHead.getBatxExeCode();

		if (functionCode == 4) {
			if (!"8".equals(batxExeCode)) {
				throw new LogicException("E0010", "作業狀態不符"); // E0010 功能選擇錯誤
			}
		} else {
			if ("8".equals(batxExeCode)) {
				throw new LogicException("E0010", "作業狀態不符"); // E0010 功能選擇錯誤
			}
		}
	}
}