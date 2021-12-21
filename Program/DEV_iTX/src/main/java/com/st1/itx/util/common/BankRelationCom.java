package com.st1.itx.util.common;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankRelationCompany;
import com.st1.itx.db.domain.BankRelationFamily;
import com.st1.itx.db.domain.BankRelationSelf;
import com.st1.itx.db.domain.BankRelationSuspected;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.BankRelationCompanyService;
import com.st1.itx.db.service.BankRelationFamilyService;
import com.st1.itx.db.service.BankRelationSelfService;
import com.st1.itx.db.service.BankRelationSuspectedService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.BankRelationVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 檢查是否為利害關係人 <BR>
 * 1.CheckBankRelationCom 是否為金控利害關係人 call by LXXXX <BR>
 * 1.1 LAW001 金控法第44條(1) <BR>
 * 1.2 LAW002 金控法第44條(列項)(2) <BR>
 * 1.3 LAW003 金控法第45條(3) <BR>
 * 1.4 LAW005 保險法(放款)(5) <BR>
 * 1.5 LAW008 準利害關係人(8) <BR>
 * 1.6 IsSalary 15日薪的員工(9) <BR>
 * 1.7 IsRelated 利害關係人(1,5) <BR>
 * 1.8 IsLnrelNear 準利害關係人(8) <BR>
 * 1.9 IsLimit授信限制對象(1,5,8,9) <BR>
 * 
 * @author st1
 *
 */
@Component("bankRelationCom")
@Scope("prototype")

public class BankRelationCom extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(BankRelationCom.class);

	@Autowired
	public BankRelationCompanyService bankRelationCompanyService;
	@Autowired
	public BankRelationSelfService bankRelationSelfService;
	@Autowired
	public BankRelationFamilyService bankRelationFamilyService;
	@Autowired
	public BankRelationSuspectedService bankRelationSuspectedService;
	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	EmployeeCom employeeCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("BankRelationCom ... " + titaVo);
		return null;
	}

	public BankRelationVo getBankRelation(String iCustId, TitaVo titaVo) throws LogicException {
		return getBankRelation(iCustId, "", titaVo);
	}

	public BankRelationVo getBankRelation(String iCustId, String iCustName, TitaVo titaVo) throws LogicException {
		logger.info("getBankRelation  ... ");
		BankRelationVo vo = new BankRelationVo();
		Slice<BankRelationCompany> slBankRelationCompany = bankRelationCompanyService.findCompanyIdEq(iCustId, 0, Integer.MAX_VALUE, titaVo);
		List<BankRelationCompany> lBankRelationCompany = slBankRelationCompany == null ? null : slBankRelationCompany.getContent();
		Slice<BankRelationSelf> slBankRelationSelf = bankRelationSelfService.findCustIdEq(iCustId, 0, Integer.MAX_VALUE, titaVo);
		List<BankRelationSelf> lBankRelationSelf = slBankRelationSelf == null ? null : slBankRelationSelf.getContent();
		Slice<BankRelationFamily> slBankRelationFamily = bankRelationFamilyService.findRelationIdEq(iCustId, 0, Integer.MAX_VALUE, titaVo);
		List<BankRelationFamily> lBankRelationFamily = slBankRelationFamily == null ? null : slBankRelationFamily.getContent();
		Slice<BankRelationSuspected> slBankRelationSuspected = bankRelationSuspectedService.RepCusNameEq(iCustName, 0, Integer.MAX_VALUE, titaVo);
		List<BankRelationSuspected> lBankRelationSuspected = slBankRelationSuspected == null ? null : slBankRelationSuspected.getContent();
		if (lBankRelationCompany != null) {
			for (BankRelationCompany t : lBankRelationCompany) {
				if ("".equals(vo.getDataDate())) {
					vo.setDataDate(this.parse.timeStampToString(t.getLastUpdate()));
				}
				if ("1".equals(t.getLAW001())) {
					vo.setLAW001("Y");
				}
				if ("1".equals(t.getLAW002())) {
					vo.setLAW002("Y");
				}
				if ("1".equals(t.getLAW003())) {
					vo.setLAW003("Y");
				}
				if ("1".equals(t.getLAW005())) {
					vo.setLAW005("Y");
				}
				if ("1".equals(t.getLAW008())) {
					vo.setLAW008("Y");
				}
			}
		}
		if (lBankRelationSelf != null) {
			for (BankRelationSelf t : lBankRelationSelf) {
				if ("".equals(vo.getDataDate())) {
					vo.setDataDate(this.parse.timeStampToString(t.getLastUpdate()));
				}
				if ("1".equals(t.getLAW001())) {
					vo.setLAW001("Y");
				}
				if ("1".equals(t.getLAW002())) {
					vo.setLAW002("Y");
				}
				if ("1".equals(t.getLAW003())) {
					vo.setLAW003("Y");
				}
				if ("1".equals(t.getLAW005())) {
					vo.setLAW005("Y");
				}
				if ("1".equals(t.getLAW008())) {
					vo.setLAW008("Y");
				}
			}

		}
		if (lBankRelationFamily != null) {
			for (BankRelationFamily t : lBankRelationFamily) {
				if ("".equals(vo.getDataDate())) {
					vo.setDataDate(this.parse.timeStampToString(t.getLastUpdate()));
				}
				if ("1".equals(t.getLAW001())) {
					vo.setLAW001("Y");
				}
				if ("1".equals(t.getLAW002())) {
					vo.setLAW002("Y");
				}
				if ("1".equals(t.getLAW003())) {
					vo.setLAW003("Y");
				}
				if ("1".equals(t.getLAW005())) {
					vo.setLAW005("Y");
				}
				if ("1".equals(t.getLAW008())) {
					vo.setLAW008("Y");
				}
			}
		}
		// 因為是姓名完全比對,有吻合資料,即預判為Y
		if (lBankRelationSuspected != null) {
			if ("".equals(vo.getDataDate())) {
				for (BankRelationSuspected t : lBankRelationSuspected) {
					vo.setDataDate(this.parse.timeStampToString(t.getLastUpdate()));
				}
			}
			vo.setIsSuspected("Y");
		}

		// 是否為15日薪員工
		CdEmp tCdEmp = cdEmpService.findAgentIdFirst(iCustId, titaVo);

		if (tCdEmp != null) {
			if (employeeCom.isDay15Salary(tCdEmp, titaVo)) {
				vo.setIsSalary("Y");
			}
		}

		//
		if ("Y".equals(vo.getLAW001()) || "Y".equals(vo.getLAW005())) {
			vo.setIsRelated("Y");
		}
		if ("Y".equals(vo.getLAW008())) {
			vo.setIsLnrelNear("Y");
		}
		if ("Y".equals(vo.getIsRelated()) || "Y".equals(vo.getIsLnrelNear()) || "Y".equals(vo.getIsSalary())) {
			vo.setIsLimit("Y");
		}

		// 2021.8.6 by eric : 資料日期是空的時,找首筆資料的第一筆顯示
		if ("".equals(vo.getDataDate())) {
			slBankRelationCompany = bankRelationCompanyService.findAll(0, 1, titaVo);
			lBankRelationCompany = slBankRelationCompany == null ? null : slBankRelationCompany.getContent();
			if (lBankRelationCompany != null) {
				for (BankRelationCompany t : lBankRelationCompany) {
					vo.setDataDate(this.parse.timeStampToString(t.getLastUpdate()));
				}
			}
		}

		if ("".equals(vo.getDataDate().trim())) {
			slBankRelationSelf = bankRelationSelfService.findAll(0, 1, titaVo);
			lBankRelationSelf = slBankRelationSelf == null ? null : slBankRelationSelf.getContent();
			if (lBankRelationSelf != null) {
				for (BankRelationSelf t : lBankRelationSelf) {
					vo.setDataDate(this.parse.timeStampToString(t.getLastUpdate()));
				}
			}
		}

		if ("".equals(vo.getDataDate().trim())) {
			slBankRelationFamily = bankRelationFamilyService.findAll(0, 1, titaVo);
			lBankRelationFamily = slBankRelationFamily == null ? null : slBankRelationFamily.getContent();
			if (lBankRelationFamily != null) {
				for (BankRelationFamily t : lBankRelationFamily) {
					vo.setDataDate(this.parse.timeStampToString(t.getLastUpdate()));
				}

			}
		}

		if ("".equals(vo.getDataDate().trim())) {
			slBankRelationSuspected = bankRelationSuspectedService.findAll(0, 1, titaVo);
			lBankRelationSuspected = slBankRelationSuspected == null ? null : slBankRelationSuspected.getContent();
			if (lBankRelationSuspected != null) {
				for (BankRelationSuspected t : lBankRelationSuspected) {
					vo.setDataDate(this.parse.timeStampToString(t.getLastUpdate()));
				}
			}
		}

		// if ("".equals(vo.getDataDate().trim())) {
		vo.setDataDate(String.valueOf(titaVo.getEntDyI()));
		// }

		logger.info(iCustId + " BankRelationVo=" + vo.toString());

		return vo;
	}

}
