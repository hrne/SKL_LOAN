package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.date.DateUtil;

@Service("L4454R3ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4454R3ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	private int entryDate = 0;
	private int lastYearEntryDate = 0;

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

//		4-8-62_一年內新貸件扣款失敗表
		this.info("一年內新貸件扣款失敗表 findAll...");

		entryDate = Integer.parseInt(titaVo.getParam("EntryDate")) + 19110000;

		this.info("entryDate= " + entryDate);

		this.info("entryDate= " + entryDate);
		lastYearEntryDate = calDate(entryDate, -1, 0, 0);

		this.info("lastYearEntryDate = " + lastYearEntryDate);
		String sql = "	";
		// -- 因只會挑一筆 先找猜選後的戶號(唯一)
		sql += " WITH \"tmpBankDeductDtl\" AS (";
		sql += "	SELECT bd.\"RepayAcctNo\"";
		sql += "		  ,bd.\"CustNo\"";
		sql += "		  ,MAX(bd.\"FacmNo\") AS \"FacmNo\"";
		sql += "		  ,MAX(bd.\"IntStartDate\") AS \"IntStartDate\"";
		sql += "	FROM \"BankDeductDtl\" bd ";
		sql += "    WHERE bd.\"EntryDate\" = :entryDate ";
		sql += "      AND bd.\"MediaCode\" = 'Y'";
		sql += "      AND bd.\"RepayType\" = 1 ";
		sql += "      AND NVL(bd.\"ReturnCode\",'  ') not in ('  ','00')";
		sql += "	GROUP BY bd.\"RepayAcctNo\"";
		sql += "		    ,bd.\"CustNo\"";
		sql += " )";
		// -- 再去串額度跟金額
		sql += "  , \"tmpBankDeductDtl2\" AS (";
		sql += "	SELECT bd2.\"RepayAcctNo\"";
		sql += "		  ,bd2.\"CustNo\"";
		sql += "		  ,bd2.\"FacmNo\"";
		sql += "		  ,bd2.\"RepayAmt\"";
		sql += "		  ,bd2.\"RelCustName\"";
		sql += "		  ,bd2.\"IntStartDate\" ";
		sql += "	FROM \"tmpBankDeductDtl\" bd ";
		sql += "	LEFT JOIN \"BankDeductDtl\" bd2 on bd2.\"CustNo\" = bd.\"CustNo\"";
		sql += "                                   and bd2.\"FacmNo\" = bd.\"FacmNo\"";
		sql += "                                   and bd2.\"RepayAcctNo\" = bd.\"RepayAcctNo\"";
		sql += "                                   and bd2.\"IntStartDate\" = bd.\"IntStartDate\"";
		sql += " )";
		// -- 再去跟主檔串最小撥款、撥款日期
		sql += "  , \"tmpBankDeductDtl3\" AS (";
		sql += "	SELECT bd2.\"RepayAcctNo\"";
		sql += "		  ,bd2.\"CustNo\"";
		sql += "		  ,bd2.\"FacmNo\"";
		sql += "		  ,MIN(lbm.\"BormNo\") AS \"BormNo\"";
		sql += "		  ,bd2.\"RepayAmt\"";
		sql += "		  ,bd2.\"RelCustName\"";
		sql += "		  ,bd2.\"IntStartDate\" - 19110000 AS \"IntStartDate\"";
		sql += "		  ,lbm.\"PrevPayIntDate\" - 19110000 AS \"PrevPayIntDate\"";
		sql += "		  ,MIN(lbm.\"DrawdownDate\"  - 19110000) AS \"DrawdownDate\"";
		sql += "	FROM \"tmpBankDeductDtl2\" bd2 ";
		sql += "	LEFT JOIN \"LoanBorMain\" lbm on lbm.\"CustNo\" = bd2.\"CustNo\"";
		sql += "                                 and lbm.\"FacmNo\" = bd2.\"FacmNo\"";
		sql += "    WHERE lbm.\"Status\" = 0 ";
		sql += "      AND lbm.\"PrevPayIntDate\" <= bd2.\"IntStartDate\" ";
		sql += "	  AND lbm.\"DrawdownDate\" >= :lastYearEntryDate";
		sql += "      AND lbm.\"DrawdownDate\" <= :entryDate";
		sql += "	GROUP BY bd2.\"RepayAcctNo\"";
		sql += "		    ,bd2.\"CustNo\"";
		sql += "		    ,bd2.\"FacmNo\"";
		sql += "		    ,bd2.\"RepayAmt\"";
		sql += "		    ,bd2.\"RelCustName\"";
		sql += "		    ,bd2.\"IntStartDate\"";
		sql += "		    ,lbm.\"PrevPayIntDate\"";
		sql += " )";
		sql += "	SELECT bd.\"RepayAcctNo\" AS \"RepayAcctNo\"";
		sql += "   		  ,LPAD(bd.\"CustNo\", 7 , '0') AS \"CustNo\"";
		sql += "   		  ,LPAD(bd.\"FacmNo\", 3 , '0') AS \"FacmNo\"";
		sql += "   		  ,LPAD(bd.\"BormNo\",3,'0') AS \"BormNo\"";
		sql += "   	      ,cm.\"CustName\" AS \"CustName\"";
		sql += "   		  ,bd.\"RepayAmt\" AS \"RepayAmt\"";
		sql += "   		  ,case ";
		sql += "		     when LENGTH(TRIM(\"Fn_GetTelNo\"(CM.\"CustUKey\",'01',1))) >=7 then \"Fn_GetTelNo\"(CM.\"CustUKey\",'01',1)";
		sql += "		     when LENGTH(TRIM(\"Fn_GetTelNo\"(CM.\"CustUKey\",'02',1))) >=7 then \"Fn_GetTelNo\"(CM.\"CustUKey\",'02',1)";
		sql += "		     when LENGTH(TRIM(\"Fn_GetTelNo\"(CM.\"CustUKey\",'03',1))) >=7 then \"Fn_GetTelNo\"(CM.\"CustUKey\",'03',1)";
		sql += " 		     end AS \"PhoneNo\"";
		sql += "   		  ,NVL(bd.\"RelCustName\", cm.\"CustName\")  AS \"RelCustName\"";
		sql += "   		  ,bd.\"PrevPayIntDate\" AS \"PrevPayIntDate\"";
		sql += "   		  ,bd.\"DrawdownDate\"  AS \"DrawdownDate\"";
		sql += "   		  ,ce.\"Fullname\" AS \"Fullname\"";
		sql += "	from \"tmpBankDeductDtl3\" bd ";
		sql += "	left join \"FacMain\" fm on fm.\"CustNo\" = bd.\"CustNo\"";
		sql += "                            and fm.\"FacmNo\" = bd.\"FacmNo\"";
		sql += "	left join \"CustMain\" cm on cm.\"CustNo\" = bd.\"CustNo\"";
		sql += "	left join \"CdEmp\" ce on ce.\"EmployeeNo\" = fm.\"FireOfficer\" ";
		sql += "	order by \"RepayAcctNo\",\"CustNo\", \"FacmNo\" ";

		this.info("sql=" + sql);
		Query query;
//
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("entryDate", entryDate);
		query.setParameter("lastYearEntryDate", lastYearEntryDate);
		return this.convertToMap(query);
	}

	private int calDate(int today, int year, int month, int day) throws LogicException {
		dateUtil.init();
		dateUtil.setDate_1(today);
		dateUtil.setYears(year);
		dateUtil.setMons(month);
		dateUtil.setDays(day);
		today = dateUtil.getCalenderDay();
		return today;
	}
}