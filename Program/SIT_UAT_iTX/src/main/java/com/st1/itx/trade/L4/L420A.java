package com.st1.itx.trade.L4;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.util.parse.Parse;

@Service("L420A")
@Scope("prototype")
/**
 * 啟動整批檢核作業(BS400)
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L420A extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L420A.class);
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public BatxHeadService batxHeadService;

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
		if (tBatxHead == null)
			throw new LogicException("E0014", tBatxHeadId + " not exist"); // E0014 檔案錯誤

		// 檢查作業狀態
		checkHead(tBatxHead, titaVo);

		// 啟動背景作業－整批檢核
		MySpring.newTask("BS400", this.txBuffer, titaVo);

		// end
		this.addList(this.totaVo);
		return this.sendList();

	}

	/* 檢查作業狀態 */
	private void checkHead(BatxHead tBatxHead, TitaVo titaVo) throws LogicException {

// BatxStsCode 整批作業狀態 0.正常 1.整批處理中

		if ("1".equals(tBatxHead.getBatxStsCode()))
			throw new LogicException("E0010", "整批處理中，請稍後"); // E0010 功能選擇錯誤

// 處理代碼 0:入帳 1:刪除 2:訂正
// BatxExeCode 作業狀態 1.檢核有誤 2.檢核正常 3.入帳未完 4.入帳完成 8.已刪除		
		String batxExeCode = tBatxHead.getBatxExeCode();
		if ("8".equals(batxExeCode))
			throw new LogicException("E0010", "作業狀態不符"); // E0010 功能選擇錯誤

	}
}