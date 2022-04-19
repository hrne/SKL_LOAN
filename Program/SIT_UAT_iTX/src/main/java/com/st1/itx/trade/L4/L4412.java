package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
//import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BankAuthActCom;
import com.st1.itx.util.parse.Parse;

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
	
	@Autowired
	CdEmpService sCdEmpService;
	
	@Autowired
	BankAuthActService sBankAuthActService;
	
	@Autowired
	Parse parse;

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
		
		CdEmp cdEmp = sCdEmpService.findById(titaVo.getTlrNo(), titaVo);

		// FunCode 1 新增
		if ("1".equals(iFunCode)) {
			if ("1".equals(iAuthApplCode)) {

				bankAuthActCom.add("A", titaVo);
			} else {
				bankAuthActCom.add("D", titaVo);
			}
			
			this.totaVo.putParam("CreateEmpNo", titaVo.getTlrNo() + " " + cdEmp.getFullname());
			this.totaVo.putParam("CreateDate", titaVo.getCalDy());
			this.totaVo.putParam("LastUpdateEmpNo", titaVo.getTlrNo() + " " + cdEmp.getFullname());
			this.totaVo.putParam("LastUpdate", titaVo.getCalDy());

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
			
			Slice<BankAuthAct> slBankAuthAct = sBankAuthActService.facmNoEq(parse.stringToInteger(titaVo.getParam("CustNo")), parse.stringToInteger(titaVo.getParam("FacmNo")), 0, Integer.MAX_VALUE, titaVo);
			
			if (slBankAuthAct == null || slBankAuthAct.isEmpty())
				throw new LogicException("E0003", "銀扣授權帳號檔");
			
			List<BankAuthAct> lBankAuthAct = slBankAuthAct.getContent();
			
			if (lBankAuthAct == null || lBankAuthAct.isEmpty())
				throw new LogicException("E0003", "銀扣授權帳號檔");
			
			BankAuthAct tBankAuthAct = lBankAuthAct.get(0);
			
			cdEmp = sCdEmpService.findById(tBankAuthAct.getCreateEmpNo(), titaVo);
			
			if (cdEmp == null)
				throw new LogicException("E0001", "員工資料檔 " + tBankAuthAct.getCreateEmpNo());
			
			this.totaVo.putParam("CreateEmpNo", tBankAuthAct.getCreateEmpNo() + " " + cdEmp.getFullname());
			this.totaVo.putParam("CreateDate", parse.timeStampToStringDate(tBankAuthAct.getCreateDate()).replace("/", ""));
			
			cdEmp = sCdEmpService.findById(tBankAuthAct.getLastUpdateEmpNo(), titaVo);
			
			if (cdEmp == null)
				throw new LogicException("E0001", "員工資料檔 " + tBankAuthAct.getLastUpdateEmpNo());
			
			this.totaVo.putParam("LastUpdateEmpNo", tBankAuthAct.getLastUpdateEmpNo() + " " + cdEmp.getFullname());
			this.totaVo.putParam("LastUpdate", parse.timeStampToStringDate(tBankAuthAct.getLastUpdate()).replace("/", ""));

			// FunCode 4 刪除
		} else if ("4".equals(iFunCode)) {
			if (iAuthApplCode.equals("2")) {
				bankAuthActCom.del("D", titaVo);
			} else {
				bankAuthActCom.del("A", titaVo);
			}
			
			// 原樣奉還
			
			this.totaVo.putParam("CreateEmpNo", titaVo.getParam("CreateEmpNo"));
			this.totaVo.putParam("CreateDate", titaVo.getParam("CreateDate"));
			this.totaVo.putParam("LastUpdateEmpNo", titaVo.getParam("LastUpdateEmpNo"));
			this.totaVo.putParam("LastUpdate", titaVo.getParam("LastUpdate"));
			
		} else {

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}