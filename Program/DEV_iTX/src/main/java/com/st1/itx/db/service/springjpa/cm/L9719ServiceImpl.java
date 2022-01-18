package com.st1.itx.db.service.springjpa.cm;

//import java.time.LocalDate;
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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L9719ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws LogicException {
		this.info("l9719.findAll ");

		int iYear = parse.stringToInteger(titaVo.getParam("inputYear")) + 1911;
		int iMonth = parse.stringToInteger(titaVo.getParam("inputMonth"));

		boolean isMonthZero = iMonth - 1 == 0;

		int ilYear = isMonthZero ? (iYear - 1) : iYear;
		int ilMonth = isMonthZero ? 12 : iMonth - 1;

		this.info("lastMonth:" + (iYear * 100 + ilMonth) + " thisMonth:" + (iYear * 100 + iMonth));

		String sql = "";
		sql += " WITH TotalData AS ( ";
		sql += " SELECT DECODE(NVL(MLB.\"AcctCode\", ' '), '990', 'OV', 'LN') AS \"Type\" ";
		sql += "       ,IIM.\"YearMonth\" AS \"YearMonth\" ";
		sql += "       ,ROUND(SUM(IIM.\"AccumDPAmortized\")) AS \"Amt\" ";
		sql += " FROM \"Ias39IntMethod\" IIM ";
		sql += " LEFT JOIN \"MonthlyLoanBal\" MLB ON IIM.\"YearMonth\" = MLB.\"YearMonth\" ";
		sql += "                                 AND IIM.\"CustNo\"    = MLB.\"CustNo\" ";
		sql += "                                 AND IIM.\"FacmNo\"    = MLB.\"FacmNo\" ";
		sql += "                                 AND IIM.\"BormNo\"    = MLB.\"BormNo\" ";
		sql += " WHERE IIM.\"YearMonth\" IN (:lastYearMonth, :thisYearMonth) ";
		sql += "   AND NVL(MLB.\"CurrencyCode\", ' ') = 'TWD' ";
		sql += " GROUP BY DECODE(NVL(MLB.\"AcctCode\", ' '), '990', 'OV', 'LN') ";
		sql += "         ,IIM.\"YearMonth\" ";
		sql += " ), ";
		sql += " ThisMonthData AS ( ";
		sql += " SELECT \"YearMonth\" ";
		sql += "       ,\"Type\" ";
		sql += "       ,SUM(\"AmtMonthly\") \"AmtMonthly\" ";
		sql += " FROM ( ";
		sql += " SELECT this.\"YearMonth\" ";
		sql += "       ,this.\"Type\" ";
		sql += "       ,this.\"Amt\" - NVL(last.\"Amt\", 0) AS \"AmtMonthly\" ";
		sql += " FROM TotalData this "; // "YearMonth" = :thisYearMonth in WHERE section
		sql += " LEFT JOIN TotalData last ON last.\"YearMonth\" = :lastYearMonth ";
		sql += "                         AND last.\"Type\"      = this.\"Type\" ";
		sql += " WHERE this.\"YearMonth\" = :thisYearMonth ";
		sql += " UNION ALL "; // UNION ALLs for making sure these always output 
		sql += " SELECT TO_NUMBER(:thisYearMonth) ";
		sql += "       ,'OV' ";
		sql += "       ,0 ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT TO_NUMBER(:thisYearMonth) ";
		sql += "       ,'LN' ";
		sql += "       ,0 ";
		sql += " FROM DUAL ";
		sql += " ) ";
		sql += " GROUP BY \"YearMonth\" ";
		sql += "         ,\"Type\" ";
		sql += " ) ";
		sql += " SELECT GREATEST(0, LoanThis.\"AmtMonthly\") AS \"LnAmt-Bor\" "; // only when > 0
		sql += "       ,-LEAST(0, LoanThis.\"AmtMonthly\") AS \"LnAmt-Loan\" "; // only when < 0, ABS
		sql += "       ,LoanCode.\"AcNoCode\" AS \"LnAmt-AcNoCode\" ";
		sql += "       ,LoanCode.\"AcNoItem\" AS \"LnAmt-AcNoItem\" ";
		sql += "       ,GREATEST(0, OvduThis.\"AmtMonthly\", 0) AS \"OvAmt-Bor\" "; // only when > 0
		sql += "       ,-LEAST(0, OvduThis.\"AmtMonthly\", 0) AS \"OvAmt-Loan\" "; // only when < 0, ABS
		sql += "       ,OvduCode.\"AcNoCode\" AS \"OvAmt-AcNoCode\" ";
		sql += "       ,OvduCode.\"AcNoItem\" AS \"OvAmt-AcNoItem\" ";
		sql += "       ,-LEAST(0, LoanThis.\"AmtMonthly\" + OvduThis.\"AmtMonthly\", 0) AS \"IntAmt-Bor\" "; // only when < 0, ABS
		sql += "       ,GREATEST(0, LoanThis.\"AmtMonthly\" + OvduThis.\"AmtMonthly\", 0) AS \"IntAmt-Loan\" "; // only when > 0
		sql += "       ,IntCode.\"AcNoCode\" AS \"IntAmt-AcNoCode\" ";
		sql += "       ,IntCode.\"AcNoItem\" AS \"IntAmt-AcNoItem\" ";
		sql += "       ,NVL(OvduTotal.\"Amt\", 0) AS \"OvduAccum\" "; // 'OV' is joined so it's not always present
		sql += "       ,LoanTotal.\"Amt\" AS \"LoanAccum\" ";
		sql += "       ,SUBSTR(:thisYearMonth, 1, 4) ";
		sql += "        || '年' ";
		sql += "        || SUBSTR(:thisYearMonth, 5, 2) ";
		sql += "        || '月折溢價攤銷' AS \"Description\" ";
		sql += " FROM ThisMonthData LoanThis "; // "Type" = 'LN' in WHERE section
		sql += " LEFT JOIN ThisMonthData OvduThis ON OvduThis.\"Type\" = 'OV' ";
		sql += " LEFT JOIN TotalData LoanTotal ON LoanTotal.\"Type\"      = LoanThis.\"Type\" ";
		sql += "                              AND LoanTotal.\"YearMonth\" = LoanThis.\"YearMonth\" ";
		sql += " LEFT JOIN TotalData OvduTotal ON OvduTotal.\"Type\"      = OvduThis.\"Type\" ";
		sql += "                              AND OvduTotal.\"YearMonth\" = OvduThis.\"YearMonth\" ";
		sql += " LEFT JOIN \"CdAcCode\" LoanCode ON LoanCode.\"AcctCode\" = 'AIL' ";
		sql += " LEFT JOIN \"CdAcCode\" OvduCode ON OvduCode.\"AcctCode\" = 'AIO' ";
		sql += " LEFT JOIN \"CdAcCode\" IntCode ON IntCode.\"AcctCode\"   = 'AII' ";
		sql += " WHERE LoanThis.\"Type\" = 'LN' ";


		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);

		query.setParameter("thisYearMonth", iYear * 100 + iMonth);
		query.setParameter("lastYearMonth", ilYear * 100 + ilMonth);
		return this.convertToMap(query);
	}

}