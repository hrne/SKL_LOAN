package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxErrCode;
import com.st1.itx.db.service.TxErrCodeService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L6R48")
@Scope("prototype")
/**
 * 錯誤代碼
 * 
 * @author ChihCheng
 * @version 1.0.0
 */
public class L6R48 extends TradeBuffer {
	@Autowired
	public TxErrCodeService tTxErrCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L6R48 ");
		this.totaVo.init(titaVo);

		int iFunCode = Integer.parseInt(titaVo.getParam("RimFunCode"));
		String iErrCode = titaVo.getParam("RimErrCode");

		TxErrCode iTxErrCode = tTxErrCodeService.findById(iErrCode, titaVo);

		if (iTxErrCode == null) {
			if (iFunCode != 1) {
				throw new LogicException(titaVo, "E0001", "查詢資料不存在,代碼" + iErrCode);
			}

		} else {
			if (iFunCode == 1) {// 新增資料已存在
				throw new LogicException(titaVo, "E0002", "新增資料已存在,代碼:" + iErrCode);
			}
			totaVo.putParam("L6R48ErrContent", iTxErrCode.getErrContent());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}