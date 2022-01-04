package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
/* 逾期放款明細 */
public class LQ006ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("lQ006.findAll ");

		int entdy = parse.stringToInteger(titaVo.getParam("ENTDY")) + 19110000;

		String sql = "";
		sql += " SELECT LPAD(TO_CHAR(A.\"CustNo\"),7,'0') ";
		sql += "        || LPAD(TO_CHAR(A.\"FacmNo\"),3,'0') ";
		sql += "        || LPAD(TO_CHAR(A.\"BormNo\"),3,'0') AS \"CustFacmBorm\" ";
		sql += "       ,A.\"LoanBal\" AS \"LoanBal\" ";
		sql += "       ,NVL(B.\"Interest\",0) AS \"Interest\" ";
		sql += "       ,ROUND((NVL(FE.\"Fee\", 0) + NVL(AR.\"RvBal\", 0)) * ROUND(A.\"LoanBal\" / M2.\"TotalBal\" , 8), 0)  AS \"Fee\" ";
		sql += "       ,A.\"LoanBal\" ";
		sql += "        + NVL(B.\"Interest\", 0) ";
		sql += "        + ROUND((NVL(FE.\"Fee\", 0) + NVL(AR.\"RvBal\", 0)) * ROUND(A.\"LoanBal\" / M2.\"TotalBal\" , 8), 0)  AS \"Total\" ";
		sql += "       ,A.\"Days\" AS \"Days\" ";
		sql += " FROM ( SELECT M.\"CustNo\" AS \"CustNo\" ";
		sql += "              ,M.\"FacmNo\" AS \"FacmNo\" ";
		sql += "              ,M.\"BormNo\" AS \"BormNo\" ";
		sql += "              ,SUM(M.\"LoanBalance\") AS \"LoanBal\" ";
		sql += "              ,MAX(NVL(C.\"OvduDays\", 0)) AS \"Days\" ";
		sql += "        FROM \"MonthlyLoanBal\" M ";
		sql += "        LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = M.\"CustNo\" ";
		sql += "                                   AND L.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "                                   AND L.\"BormNo\" = M.\"BormNo\" ";
		sql += "        LEFT JOIN \"CollList\" C ON C.\"CustNo\" = M.\"CustNo\" ";
		sql += "                                AND C.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "        WHERE M.\"YearMonth\" = TRUNC( :entdy / 100 ) ";
		sql += "          AND M.\"LoanBalance\" > 0 ";
		sql += "        GROUP BY M.\"CustNo\" ";
		sql += "                ,M.\"FacmNo\" ";
		sql += "                ,M.\"BormNo\" ) A ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                   ,SUM(DECODE(\"FacmNo\", 0, NVL(\"RvBal\", 0), 0)) AS \"RvBalF\" "; // RvBal if FacmNo is 0
		sql += "                   ,SUM(DECODE(\"FacmNo\", 0, 0, NVL(\"RvBal\", 0))) AS \"RvBal\" "; // RvBal if FacmNo is not 0
		sql += "             FROM \"AcReceivable\" ";
		sql += "             WHERE \"AcctCode\" IN ('F09','F25','TMI') ";
		sql += "               AND \"OpenAcDate\" <= :entdy ";
		sql += "               AND \"ClsFlag\" = 0 ";
		sql += "             GROUP BY \"CustNo\" ) AR ON AR.\"CustNo\" = A.\"CustNo\" ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" AS \"CustNo\" ";
		sql += "                   ,\"FacmNo\" AS \"FacmNo\" ";
		sql += "                   ,\"BormNo\" AS \"BormNo\" ";
		sql += "                   ,SUM(\"Interest\") AS \"Interest\" ";
		sql += "             FROM \"AcLoanInt\" ";
		sql += "             WHERE \"YearMonth\" = TRUNC( :entdy / 100 ) ";
		sql += "               AND \"IntStartDate\" > 0 ";
		sql += "             GROUP BY \"CustNo\" ";
		sql += "                     ,\"FacmNo\" ";
		sql += "                     ,\"BormNo\" ) B ON B.\"CustNo\" = A.\"CustNo\" ";
		sql += "                                    AND B.\"FacmNo\" = A.\"FacmNo\" ";
		sql += "                                    AND B.\"BormNo\" = A.\"BormNo\" ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                   ,SUM(\"LoanBalance\") AS \"TotalBal\" ";
		sql += "             FROM \"MonthlyLoanBal\" ";
		sql += "             WHERE \"YearMonth\" = TRUNC( :entdy / 100 ) ";
		sql += "             GROUP BY \"CustNo\" ) M2 ON M2.\"CustNo\" = A.\"CustNo\" ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                   ,SUM(\"Fee\") AS \"Fee\" ";
		sql += "             FROM \"ForeclosureFee\" ";
		sql += "             WHERE \"CloseDate\" = 0 ";
		sql += "             GROUP BY \"CustNo\" ) FE ON FE.\"CustNo\" = A.\"CustNo\" ";
		sql += " ORDER BY \"CustFacmBorm\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("entdy", entdy);
		
		return this.convertToMap(query);
	}
	
	public List<Map<String, String>> findTotal(TitaVo titaVo) throws Exception {
		this.info("lQ006.findTotal");

		int inputYearMonth = parse.stringToInteger(titaVo.getParam("ENTDY")) / 100 + 191100;

		String sql = "";
		sql += " WITH Data AS ( ";
		sql += "      SELECT TO_CHAR(ADD_MONTHS(TO_DATE(MFB.\"YearMonth\" - 191100, 'YYYMM'), 1) - 1, 'YYY.MM.DD') \"OutputDate\" "; // YYYYMMDD轉為YYY.MM.DD
		sql += "            ,MFB.\"OvduDays\" \"OvduDays\" ";
		sql += "            ,NVL(MFB.\"PrinBalance\", 0) ";
		sql += "             + ROUND(NVL(MFBGroup.\"TotalFee\", 0) * ROUND(NVL(MFB.\"PrinBalance\", 0) / NVL(MFBGroup.\"TotalBal\", 0), 8)) "; // 入表戶必有餘額
		sql += "             + NVL(ALI.\"Interest\", 0) AS \"Amount\" ";
		sql += "      FROM \"MonthlyFacBal\" MFB ";
		sql += "      LEFT JOIN ( SELECT \"YearMonth\" ";
		sql += "                        ,\"CustNo\" ";
		sql += "                        ,\"FacmNo\" ";
		sql += "                        ,SUM(\"Interest\") \"Interest\" ";
		sql += "                  FROM \"AcLoanInt\" ";
		sql += "                  WHERE \"YearMonth\" = :inputYearMonth ";
		sql += "                    AND \"IntStartDate\" > 0 ";
		sql += "                  GROUP BY \"YearMonth\" ";
		sql += "                          ,\"CustNo\" ";
		sql += "                          ,\"FacmNo\" ) ALI ON ALI.\"YearMonth\" = MFB.\"YearMonth\" ";
		sql += "                                           AND ALI.\"CustNo\"    = MFB.\"CustNo\" ";
		sql += "                                           AND ALI.\"FacmNo\"    = MFB.\"FacmNo\" ";
		sql += "      LEFT JOIN ( SELECT \"YearMonth\" ";
		sql += "                        ,\"CustNo\" ";
		sql += "                        ,SUM(\"PrinBalance\") \"TotalBal\" ";
		sql += "                        ,SUM(\"FireFee\") + SUM(\"LawFee\") \"TotalFee\" ";
		sql += "                  FROM \"MonthlyFacBal\" ";
		sql += "                  GROUP BY \"YearMonth\" ";
		sql += "                          ,\"CustNo\" ) MFBGroup ON MFBGroup.\"YearMonth\" = MFB.\"YearMonth\" ";
		sql += "                                                AND MFBGroup.\"CustNo\"    = MFB.\"CustNo\" ";
		sql += "      WHERE MFB.\"YearMonth\" IN (  :inputYearMonth ";       // 今年本月
		sql += "                                  , :inputYearMonth - 100 "; // 去年本月
		sql += "                                  , SUBSTR(:inputYearMonth - 100, 1, 4) || 12 "; // 去年年底
		sql += "                                  , SUBSTR(:inputYearMonth - 200, 1, 4) || 12 "; // 前年年底
		sql += "                                 ) ";
		sql += "        AND MFB.\"PrinBalance\" > 0 ";
		sql += "        AND MFB.\"OvduDays\" BETWEEN 31 AND 90 ";
		sql += " ) ";
		sql += " SELECT \"OutputDate\" ";
		sql += "       ,SUM(CASE WHEN \"OvduDays\" BETWEEN 31 AND 60 ";
		sql += "                 THEN \"Amount\" ";
		sql += "            ELSE 0 END) \"SecondMonth\" ";
		sql += "       ,SUM(CASE WHEN \"OvduDays\" BETWEEN 61 AND 90 ";
		sql += "                 THEN \"Amount\" ";
		sql += "            ELSE 0 END) \"ThirdMonth\" ";
		sql += "       ,SUM(CASE WHEN \"OvduDays\" BETWEEN 31 AND 90 ";
		sql += "                 THEN \"Amount\" ";
		sql += "            ELSE 0 END) \"BothMonth\" ";
		sql += " FROM Data ";
		sql += " GROUP BY \"OutputDate\" ";
		sql += " ORDER BY \"OutputDate\" ";


		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("inputYearMonth", inputYearMonth);
		
		return this.convertToMap(query);
	}
}