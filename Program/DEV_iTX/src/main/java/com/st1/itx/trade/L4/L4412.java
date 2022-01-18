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
 * FunctionCode=9,1<br>
 * CustId=X,10<br>
 * AuthApplCode=X,1<br>
 * CustNo=9,7<br>
 * PostDepCode=X,1<br>
 * RepayAcct=9,14<br>
 * RepayAcctSeq=9,2<br>
 * AuthCode=9,1<br>
 * RelationCode=9,2<br>
 * RelAcctName=X,100<br>
 * RelAcctBirthday=9,7<br>
 * RelAcctGender=X,1<br>
 * AuthCreatedate=9,7<br>
 * END=X,1<br>
 */

@Service("L4412")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4412 extends TradeBuffer {

	@Autowired
	public BankAuthActCom bankAuthActCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4412 ");
		this.totaVo.init(titaVo);
		bankAuthActCom.setTxBuffer(this.getTxBuffer());

		// tita
		String iFunCode = titaVo.getParam("FuncCode");
		String iAuthApplCode = titaVo.getParam("AuthApplCode");

		this.info("iFunCode = " + iFunCode);
		this.info("iAuthApplCode = " + iAuthApplCode);

		// FunCode 1 新增
		if ("1".equals(iFunCode)) {
			if ("1".equals(iAuthApplCode)) {

				bankAuthActCom.add("A", titaVo);
			} else {
				bankAuthActCom.add("D", titaVo);
			}

			// FunCode 2 修改
		} else if ("2".equals(iFunCode)) {

			// 授權狀態未授權先刪除後新增
			if ("".equals(titaVo.getParam("AuthErrorCode").trim())) {
				bankAuthActCom.del("A", titaVo);
				bankAuthActCom.add("A", titaVo);
			} else {
				if ("1".equals(iAuthApplCode)) {
					bankAuthActCom.mntPostAuth("0", titaVo);
				} else {
					bankAuthActCom.mntPostAuth("1", titaVo);
				}
			}

			// FunCode 4 刪除
		} else if ("4".equals(iFunCode)) {
			if (iAuthApplCode.equals("2")) {
				bankAuthActCom.del("D", titaVo);
			} else {
				bankAuthActCom.del("A", titaVo);
			}
		} else {

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}