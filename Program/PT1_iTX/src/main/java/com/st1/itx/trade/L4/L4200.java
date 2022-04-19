package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

/**
 * Tita<br>
 */

@Service("L4200")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4200 extends TradeBuffer {

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4200 ");
		this.totaVo.init(titaVo);

// L4200入檔流程：
//		A.寫入Head檔
//		  for(4種來源檔) {
//			B.(first check)  檢核資料與寫入檔是否相同  並回寫處理狀態(ProcStsCode)
//			C.(second check) 寫入Detail檔 (媒體檔)
//			D.寫VO入各個扣款明細檔 (Detail不為異常者) 
//	      }
//		E.回寫Head檔 總金額與總筆數(if與前兩天相同，則丟Warning)

		// 執行交易
		MySpring.newTask("L4200Batch", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}