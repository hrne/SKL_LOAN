package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxControl;
import com.st1.itx.db.service.TxControlService;
//import com.st1.itx.db.service.springjpa.cm.L5051ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.util.MySpring;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L5511")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5511 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5511.class);

	@Autowired
	public TxControlService txControlService;
	
	@Autowired
	public Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("Run L5511");

		this.totaVo.init(titaVo);

		String iFunCode = titaVo.getParam("FunCode").trim();// 使用功能
		int iWorkMonth = parse.stringToInteger(titaVo.getParam("WorkMonth")) + 191100;
		
		if ("1".equals(iFunCode)) {
			String controlCode = "L5511." + iWorkMonth + ".2";
			TxControl txControl = txControlService.findById(controlCode, titaVo);
			if (txControl != null) {
				throw new LogicException(titaVo, "E0010", "已產生媒體檔");
			}
			
		} else if ("2".equals(iFunCode)) {
			String controlCode = "L5511." + iWorkMonth + ".1";
			TxControl txControl = txControlService.findById(controlCode, titaVo);
			if (txControl == null) {
				throw new LogicException(titaVo, "E0010", "未執行 L5510 保費檢核");
			}
			
		} else if ("3".equals(iFunCode)) {
			String controlCode = "L5511." + iWorkMonth + ".2";
			TxControl txControl = txControlService.holdById(controlCode, titaVo);
			if (txControl == null) {
				throw new LogicException(titaVo, "E0010", "未產生媒體檔");
			}
			// 刪除交易控制檔

			try {
				txControlService.delete(txControl, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
		}

		if (!"3".equals(iFunCode)) {
			MySpring.newTask("L5511Batch", this.txBuffer, titaVo);
			
			this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");
			
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}