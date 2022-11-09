package com.st1.itx.util.common;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 員工身分判讀 <BR>
 * isDay15Salary 是否為15日薪員工 call by PfDetailCom 業績明細處理 <BR>
 * isGAInsDepartent 是否為團體意外險部門員工 call by PfDetailCom 業績明細處理 <BR>
 * 
 * @author iza
 * @version 1.0.0
 */
@Component("employeeCom")
@Scope("prototype")

public class EmployeeCom extends TradeBuffer {

	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BankRelationCom ... " + titaVo);
		return null;
	}

	/**
	 * 是否為15日薪員工
	 * 
	 * @param tCdEmp tCdEmp
	 * @param titaVo TitaVo
	 * @return true/false
	 * @throws LogicException LogicException
	 */
	public boolean isDay15Salary(CdEmp tCdEmp, TitaVo titaVo) throws LogicException {
		this.info("isSalary  ... ");
		boolean isSalary = false;

		// 2022-11-09 Wei 修改
		// from 淳英 ask 核心系統-AG線-陳資宜
		// COMM_LINE_CODE = '35'
		// AND AG_STATUS_CODE = '1'
		// AND AG_CUR_IND = 'Y'

		String commLineCode = tCdEmp.getCommLineCode();
		String agStatusCode = tCdEmp.getAgStatusCode();
		String agCurInd = tCdEmp.getAgCurInd();

		if (commLineCode != null && !commLineCode.isEmpty() && commLineCode.equals("35")) {
			if (agStatusCode != null && !agStatusCode.isEmpty() && agStatusCode.equals("1")) {
				if (agCurInd != null && !agCurInd.isEmpty() && agCurInd.equals("Y")) {
					return true;
				}
			}
		}

		return isSalary;
	}

	/**
	 * 是否為團體意外險部門員工
	 * 
	 * @param tCdEmp tCdEmp
	 * @param titaVo TitaVo
	 * @return true/false
	 * @throws LogicException LogicException
	 */
	public boolean isGAInsDepartent(CdEmp tCdEmp, TitaVo titaVo) throws LogicException {
		this.info("isSalary  ... ");
		boolean isGAInsDepartent = false;
		// CenterCode2 部室代號 = 109000、A0Y000 is True
		if ("109000".equals(tCdEmp.getCenterCode2()) || "A0Y000".equals(tCdEmp.getCenterCode2()))
			isGAInsDepartent = true;
		return isGAInsDepartent;
	}

}
