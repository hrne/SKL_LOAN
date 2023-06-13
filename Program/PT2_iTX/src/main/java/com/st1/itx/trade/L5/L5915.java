package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxControl;
import com.st1.itx.db.service.TxControlService;
import com.st1.itx.db.service.springjpa.cm.L5915ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.parse.Parse;

@Service("L5915")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5915 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	private Parse parse;

	@Autowired
	private L5915ServiceImpl l5915ServiceImpl;

	@Autowired
	public TxControlService txControlService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5915 ");
		this.totaVo.init(titaVo);
		String controlCode = "L5511." + (parse.stringToInteger(titaVo.getParam("Ym")) + 191100) + ".1";
		TxControl txControl = txControlService.holdById(controlCode, titaVo);
		if (txControl == null) {
			throw new LogicException(titaVo, "E0015", "尚未執行 L5511 功能 1.保費檢核及匯入介紹、協辦獎金發放資料 "); // 檢查錯誤
		}
		// 2022-08-19 智偉增加: SKL user 珮君要求照AS400把協辦人員件數、金額輸出為Excel
		MySpring.newTask("L5915Batch", this.txBuffer, titaVo);
		this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");
		this.addList(this.totaVo);
		return this.sendList();
	}

}