package com.st1.itx.trade.L4;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
//import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BankAuthActCom;

/**
 * Tita<br>
 * FUNCTIONCD=9,1<br>
 * CUSTNO=9,7<br>
 * FACMNO=9,3<br>
 * REPAYBANK=9,3<br>
 * REPAYACCTNO=9,14<br>
 * LIMAMT=9,14.2<br>
 * RELATIONIND=9,2<br>
 * RELACCTNAME=X,100<br>
 * RELACCTBIRTH=9,7<br>
 * RELACCTGENDER=X,1<br>
 * END=X,1<br>
 */

@Service("L4410")
@Scope("prototype")
public class L4410 extends TradeBuffer {

	@Autowired
	public BankAuthActCom bankAuthActCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4410 ");
		this.totaVo.init(titaVo);
		bankAuthActCom.setTxBuffer(this.getTxBuffer());

		// tita
		String iCreateFlag = titaVo.getParam("CreateFlag");
		// wk

		if ("1".equals(titaVo.getParam("FuncCode"))) {
			bankAuthActCom.add(iCreateFlag, titaVo);

			// 修改，新增授權A時授權成功，否則停止使用
		} else if ("2".equals(titaVo.getParam("FuncCode"))) {
			// 授權狀態未授權先刪除後新增
			if ("".equals(titaVo.getParam("AuthStatus").trim())) {
				bankAuthActCom.del("A", titaVo);
				bankAuthActCom.add("A", titaVo);
			} else {
				if ("A".equals(iCreateFlag)) {
					bankAuthActCom.mntAchAuth("0", titaVo);
				} else {
					bankAuthActCom.mntAchAuth("1", titaVo);
				}
			}

		} else if ("4".equals(titaVo.getParam("FuncCode"))) {
			bankAuthActCom.del(iCreateFlag, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}