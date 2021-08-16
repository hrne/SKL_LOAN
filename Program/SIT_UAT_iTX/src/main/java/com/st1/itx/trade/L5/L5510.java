package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.parse.Parse;

@Service("L5510")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5510 extends TradeBuffer {

	@Autowired
	public PfItDetailService pfItDetailService;

	@Autowired
	public Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5510 ");
		this.totaVo.init(titaVo);
		String iFunCode = titaVo.getParam("FunCode").trim();// 使用功能
		int iWorkMonth = parse.stringToInteger(titaVo.getParam("WorkMonth")) + 191100;
		Slice<PfItDetail> slPfItDetail = pfItDetailService.findByWorkMonth(iWorkMonth, iWorkMonth, 0,
				Integer.MAX_VALUE, titaVo);
		if (slPfItDetail != null) {
			for (PfItDetail pfIt : slPfItDetail.getContent()) {
				if ("1".equals(iFunCode) && pfIt.getMediaFg() > 0) {
					throw new LogicException(titaVo, "E0010", "已產生媒體檔"); // 功能選擇錯誤
				}
				if ("2".equals(iFunCode) && pfIt.getRewardDate() == 0) {
					if (pfIt.getPerfEqAmt().compareTo(BigDecimal.ZERO) != 0
							|| pfIt.getPerfReward().compareTo(BigDecimal.ZERO) != 0) {
						throw new LogicException(titaVo, "E0010", "未執行保費檢核"); // 功能選擇錯誤
					}
				}
			}
		}
		MySpring.newTask("L5510Batch", this.txBuffer, titaVo);

		this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");

		this.addList(this.totaVo);
		return this.sendList();
	}
}