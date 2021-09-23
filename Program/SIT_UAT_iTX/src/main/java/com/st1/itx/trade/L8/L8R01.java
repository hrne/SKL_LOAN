package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R01")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R01 extends TradeBuffer {

	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8R01 ");
		this.totaVo.init(titaVo);
		String jcicBankCode = titaVo.getParam("RimJcicBankCode");// Jcic聯徵代碼
		CdCode tCdCode = new CdCode();
		tCdCode = iCdCodeService.getItemFirst(8, "JcicBankCode", jcicBankCode, titaVo);
		String JcicBankName = "";// 80碼長度
		if (tCdCode != null) {
			JcicBankName = tCdCode.getItem();
		} else {
			throw new LogicException(titaVo, "E0001", "查無此代號");
		}
		this.totaVo.putParam("L8r01JcicBankName", JcicBankName);
		this.addList(this.totaVo);
		return this.sendList();
	}
}