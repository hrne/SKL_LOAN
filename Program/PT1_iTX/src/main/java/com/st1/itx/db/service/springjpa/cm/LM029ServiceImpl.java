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
public class LM029ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 執行報表輸出(明細表)
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("LM029ServiceImpl findAll ");

		int entdy = yearMonth;
		this.info("yearMonth=" + entdy);

		String sql = "";
		sql += " SELECT M.\"CustNo\"                              AS F0 ";
		sql += "       ,M.\"FacmNo\"                              AS F1 ";
		sql += "       ,M.\"BormNo\"                              AS F2 ";
		sql += "       ,C.\"CustId\"                              AS F3 ";
		sql += "       ,\"Fn_ParseEOL\"(C.\"CustName\", 0)        AS F4 ";
		sql += "       ,M.\"AcctCode\"                            AS F5 ";
		sql += "       ,L.\"DrawdownDate\"                        AS F6 ";
		sql += "       ,L.\"MaturityDate\"                        AS F7 ";
		sql += "       ,M.\"StoreRate\"                           AS F8 ";
		sql += "       ,L.\"PayIntFreq\"                          AS F9 ";
		sql += "       ,L.\"PrevPayIntDate\"                      AS F10 ";
		sql += "       ,NVL(O.\"OvduDate\", 0)                    AS F11 ";
		sql += "       ,L.\"UsageCode\"                           AS F12 ";
//		sql += "       ,CL.\"OwnerId\"                            AS F16 ";
//		sql += "       ,CL.\"OwnerName\"                          AS F17 ";
//		sql += "       ,NVL(CI.\"SettingSeq\",0)                  AS F18 ";
		sql += "       ,NVL(F.\"LineAmt\",0)                      AS F13 ";
		sql += "       ,NVL(L.\"DrawdownAmt\",0)                  AS F14 ";
		sql += "       ,M.\"LoanBalance\"                         AS F15 ";
//		sql += "       ,M.\"CityCode\"                            AS F22 ";
//		sql += "       ,NVL(CL.\"LandNo\",'00000000')             AS F23  "; // 地號格式為 4-4
//		sql += "       ,NVL(CL.\"BdNo\",'00000000')               AS F24  "; // 建號格式為 5-3
		sql += "       ,DECODE(M.\"AcSubBookCode\",'00A',' ','A') AS F16 ";
		sql += "       ,CASE ";
		sql += "       	  WHEN MF.\"OvduTerm\" IN (1,2,3,4,5) THEN MF.\"OvduTerm\" ";
		sql += "       	ELSE 0 END AS F17";
		sql += "       ,M.\"ClCode1\"                             AS F18 ";
		sql += "       ,M.\"ClCode2\"                             AS F19 ";
		sql += "       ,M.\"ClNo\"                                AS F20 ";
		sql += " FROM \"MonthlyLoanBal\" M ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\" ";
		sql += " LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = M.\"CustNo\" ";
		sql += "                            AND L.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "                            AND L.\"BormNo\" = M.\"BormNo\" ";
		sql += " LEFT JOIN \"LoanOverdue\" O ON O.\"CustNo\" = M.\"CustNo\" ";
		sql += "                            AND O.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "                            AND O.\"BormNo\" = M.\"BormNo\" ";
		sql += " LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\" ";
		sql += "                        AND F.\"FacmNo\" = M.\"FacmNo\" ";
		sql += " LEFT JOIN \"MonthlyFacBal\" MF ON MF.\"CustNo\" = F.\"CustNo\" ";
		sql += "                               AND MF.\"FacmNo\" = F.\"FacmNo\" ";
		sql += "                               AND MF.\"YearMonth\" = M.\"YearMonth\" ";

		sql += " WHERE M.\"YearMonth\" = :entdy ";
		sql += "   AND M.\"LoanBalance\" > 0 ";
		sql += " ORDER BY F0,F1,F2 ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("entdy", entdy);

		return this.convertToMap(query);
	}

	/**
	 * 執行報表輸出(Deliquency)
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findAll2(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("LM029ServiceImpl findAll2 ");

		this.info("yearMonth=" + yearMonth);

		int sYearMonth = (yearMonth / 100) * 100 + 1;
		int eYearMonth = yearMonth;

		String sql = "";
		sql += "	WITH dpAmt AS (";
		sql += "		SELECT I.\"YearMonth\" AS \"YearMonth\"";
		sql += "			  ,ROUND(SUM(NVL(I.\"AccumDPAmortized\",0)),0) AS \"Amt\"";
		sql += "		FROM \"Ias39IntMethod\" I";
		sql += "		LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"YearMonth\" = I.\"YearMonth\"";
		sql += "										AND MLB.\"CustNo\" = I.\"CustNo\"";
		sql += "										AND MLB.\"FacmNo\" = I.\"FacmNo\"";
		sql += "										AND MLB.\"BormNo\" = I.\"BormNo\"";
		sql += "		WHERE I.\"YearMonth\" BETWEEN :syearmonth AND :eyearmonth  ";
		sql += "		  AND MLB.\"AcctCode\" <> 990 ";
		sql += "		GROUP BY I.\"YearMonth\"";
		sql += "				,DECODE(NVL(MLB.\"AcctCode\",' '),'990','990','OTHER')";
		sql += "	),pAmt AS (";
		sql += "		SELECT \"YearMonth\"";
		sql += "			  ,SUM(\"PrinBalance\") AS \"Amt\"";
		sql += "		FROM \"MonthlyFacBal\"";
		sql += "		WHERE \"YearMonth\" BETWEEN :syearmonth AND :eyearmonth ";
		sql += "		  AND \"OvduTerm\" IN (1,2)";
		sql += "		  AND \"PrinBalance\" > 0";
		sql += "		GROUP BY \"YearMonth\"";
		sql += "	),aAmt AS (";
		sql += "		SELECT \"YearMonth\"";
		sql += "			  ,SUM(\"PrinBalance\") AS \"Amt\"";
		sql += "		FROM \"MonthlyFacBal\"";
		sql += "		WHERE \"YearMonth\" BETWEEN :syearmonth AND :eyearmonth ";
		sql += "		  AND \"PrinBalance\" > 0";
		sql += "		GROUP BY \"YearMonth\"";
		sql += "	),oAmt AS (";
		sql += "		SELECT \"YearMonth\"";
		sql += "			  ,SUM(\"Amt\") AS \"Amt\"";
		sql += "		FROM(";
		sql += "		SELECT \"YearMonth\"";
		sql += "			  ,SUM(\"PrinBalance\") AS \"Amt\"";
		sql += "		FROM \"MonthlyFacBal\"";
		sql += "		WHERE \"YearMonth\" BETWEEN :syearmonth AND :eyearmonth ";
		sql += "		  AND \"OvduTerm\" >= 3 ";
		sql += "		  AND \"AcctCode\" <> '990' ";
		sql += "		  AND \"PrinBalance\" > 0";
		sql += "		GROUP BY \"YearMonth\"";
		sql += "		UNION";
		sql += "		SELECT \"YearMonth\"";
		sql += "			  ,SUM(\"PrinBalance\") AS \"Amt\"";
		sql += "		FROM \"MonthlyFacBal\"";
		sql += "		WHERE \"YearMonth\" BETWEEN :syearmonth AND :eyearmonth ";
		sql += "		  AND \"AcctCode\" = '990' ";
		sql += "		  AND \"PrinBalance\" > 0";
		sql += "		GROUP BY \"YearMonth\"";
		sql += "		UNION";
		sql += "		SELECT \"YearMonth\"";
		sql += "			  ,\"LoanBal\" AS \"Amt\"";
		sql += "		FROM \"MonthlyLM052AssetClass\"";
		sql += "		WHERE \"YearMonth\" BETWEEN :syearmonth AND :eyearmonth ";
		sql += "		  AND \"AssetClassNo\" = 62 ";
		sql += "		)";
		sql += "		GROUP BY \"YearMonth\"";
		sql += "	),soAmt AS (";
		sql += "		SELECT \"YearMonth\"";
		sql += "			  ,\"LoanBal\" AS \"Amt\"";
		sql += "		FROM \"MonthlyLM052AssetClass\"";
		sql += "		WHERE \"YearMonth\" BETWEEN :syearmonth AND :eyearmonth ";
		sql += "		  AND \"AssetClassNo\" = 62 ";
		sql += "	)";
		sql += "	SELECT aAmt.\"YearMonth\"";
		sql += "		  ,pAmt.\"Amt\" AS \"12Amt\"";
		sql += "		  ,aAmt.\"Amt\" + dpAmt.\"Amt\" + NVL(soAmt.\"Amt\",0) AS \"totalAmt\"";
		sql += "		  ,ROUND(pAmt.\"Amt\" / (aAmt.\"Amt\" + dpAmt.\"Amt\"),15) AS \"12Rate\"";
		sql += "		  ,oAmt.\"Amt\" AS \"oAmt\"";
		sql += "		  ,ROUND(oAmt.\"Amt\" / (aAmt.\"Amt\" + dpAmt.\"Amt\"),15) AS \"oRate\"";
		sql += "	FROM pAmt";
		sql += "	LEFT JOIN aAmt ON aAmt.\"YearMonth\" = pAmt.\"YearMonth\"";
		sql += "	LEFT JOIN dpAmt ON dpAmt.\"YearMonth\" = pAmt.\"YearMonth\"";
		sql += "	LEFT JOIN oAmt ON oAmt.\"YearMonth\" = pAmt.\"YearMonth\"";
		sql += "	LEFT JOIN soAmt ON soAmt.\"YearMonth\" = pAmt.\"YearMonth\"";
		sql += " ORDER BY  aAmt.\"YearMonth\" ASC ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("syearmonth", sYearMonth);
		query.setParameter("eyearmonth", eYearMonth);

		return this.convertToMap(query);
	}

}