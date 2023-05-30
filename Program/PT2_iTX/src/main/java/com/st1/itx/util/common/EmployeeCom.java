package com.st1.itx.util.common;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
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
	CdEmpService cdEmpService;

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
	public boolean isDay15Employee(CdEmp tCdEmp, TitaVo titaVo) throws LogicException {
		this.info("isSalary  ... ");
		boolean isSalary = false;
// eLoan  AgLineCode        AgLevel                 InPostNo
// CdEmp  CommLineCode      AgLevel                 AgPostIn
//         業務線代號        業務人員職等           內階職務
//         等於 21    &&     第一碼不等於 F,G,J,Z
//     or  等於  31       &&         第一碼不等於   K,Z
//     or  不等於 21,31,1C   
// and                                              不等於   TU0036,TU0097
//
//	     and( (AgLineCode = '21')
//	    	     and (AgLevel NOT LIKE 'F%')
//	    	     and (AgLevel NOT LIKE 'G%')
//	    	     and (AgLevel NOT LIKE 'J%')
//	    	     and (AgLevel NOT LIKE 'Z%')
//	    	     OR  (AgLineCode = '31')
//	    	     and (AgLevel NOT LIKE 'K%')
//	    	     and (AgLevel NOT LIKE 'Z%')
//	    	     OR  (AgLineCode NOT IN ('21', '31', '1C')) 
//	    	     and (InPostNo NOT IN ('TU0036','TU0097')) )

		String commLineCode = tCdEmp.getCommLineCode();
		String agLevel = " ";
		if (tCdEmp.getAgLevel() != null && tCdEmp.getAgLevel().length() > 0) {
			agLevel = tCdEmp.getAgLevel().substring(0, 1);
		}

		if ("21".equals(commLineCode)) {
			if (!("F".equals(agLevel) || "G".equals(agLevel) || "J".equals(agLevel) || "Z".equals(agLevel))) {
				isSalary = true;
			}
		}

		if ("31".equals(commLineCode)) {
			if (!("K".equals(agLevel) || "Z".equals(agLevel))) {
				isSalary = true;
			}
		}

		if (!("21".equals(commLineCode) || "31".equals(commLineCode) || "1C".equals(commLineCode))) {
			if (!("TU0036".equals(tCdEmp.getAgPostIn()) || "TU0097".equals(tCdEmp.getAgPostIn()))) {
				isSalary = true;
			}
		}

		return isSalary;
	}

	/**
	 * 是否為15日薪非業績人員
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

	/**
	 * 取得介紹人的區經理<BR>
	 * 區經理 : CdEmp(員工資料檔)的AgLevel(業務人員職等)第一碼為'H'為區經理<BR>
	 * 介紹人本身為區經理,否則依序找上層主管直到找到區經理 <BR>
	 * 
	 * @param introducerCdEmp
	 * @param titaVo
	 * @return 區經理員編
	 * @throws LogicException
	 */
	public String getDistManager(CdEmp introducerCdEmp, TitaVo titaVo) throws LogicException {
		if (introducerCdEmp.getAgLevel().length() >= 1 && "H".equals(introducerCdEmp.getAgLevel().substring(0, 1))) {
			return introducerCdEmp.getEmployeeNo();
		}
		String directorId = introducerCdEmp.getDirectorId();
		for (int i = 0; i <= 3; i++) {
			CdEmp tCdEmp = cdEmpService.findAgentCodeFirst(directorId, titaVo);
			if (tCdEmp == null) {
				break;
			}
			if (tCdEmp.getAgLevel().length() >= 1 && "H".equals(tCdEmp.getAgLevel().substring(0, 1))) {
				return tCdEmp.getEmployeeNo();
			}
			directorId = tCdEmp.getDirectorId();
		}
		return null;
	}
}
