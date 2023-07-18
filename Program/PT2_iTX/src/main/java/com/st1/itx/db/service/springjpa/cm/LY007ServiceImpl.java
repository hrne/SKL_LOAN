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

@Service("LY007ServiceImpl")
@Repository
public class LY007ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> queryDetail(int inputYearMonth, TitaVo titaVo) throws Exception {
		this.info("LY007ServiceImpl queryDetail ");

		this.info("inputYearMonth=" + inputYearMonth);

		String sql = "";
		//--是否一億元
		sql += "    WITH \"isTmpE\" AS (";
		sql += "        SELECT M.\"CustNo\"";
		sql += "             , MIN(F.\"FirstDrawdownDate\")       AS \"DrawdownDate\"";
		sql += "             , SUM(F.\"LineAmt\")                 AS \"LineAmt\"";
		sql += "             , SUM(M.\"PrinBalance\")             AS \"LoanBal\"";
		sql += "        FROM \"MonthlyFacBal\"  M";
		sql += "        LEFT JOIN \"FacMain\"     F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                                 AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "        WHERE M.\"YearMonth\" = :Yymm";
		sql += "              AND";
		sql += "              M.\"PrinBalance\" > 0";
		sql += "        GROUP BY M.\"CustNo\"";
		sql += "    ), \"tmpRel\" AS (";
		sql += "        SELECT M.\"Rel\"";
		sql += "             , C.\"CustNo\"";
		sql += "        FROM (";
		sql += "            SELECT DISTINCT CASE";
		sql += "                WHEN \"BusId\" = '-'";
		sql += "                     AND";
		sql += "                     \"RelId\" = '-' THEN 'A:G'";
		sql += "                ELSE 'Z'";
		sql += "            END AS \"Rel\"";
		sql += "                          , CASE";
		sql += "                WHEN \"BusId\" <> '-'   THEN \"BusId\"";
		sql += "                WHEN \"RelId\" <> '-'   THEN \"RelId\"";
		sql += "                ELSE \"HeadId\"";
		sql += "            END AS \"Id\"";
		sql += "            FROM \"LifeRelHead\"";
		sql += "            WHERE \"LoanBalance\" > 0";
		sql += "                  AND";
		sql += "                  Trunc(\"AcDate\" / 100) = :Yymm";
		sql += "                  AND";
		sql += "                  \"RelWithCompany\" IN (";
		sql += "                      'A'";
		sql += "                    , 'G'";
		sql += "                  )";
		sql += "        ) M";
		sql += "        LEFT JOIN \"CustMain\" C ON C.\"CustId\" = M.\"Id\"";
		sql += "    ), \"tmpLocation\" AS (";
		sql += "        SELECT M.\"CustNo\"";
		sql += "             , M.\"FacmNo\"";
		sql += "             , CASE";
		sql += "            WHEN Cl.\"LandLocation\" IS NULL THEN Substr(";
		sql += "                Cb.\"BdLocation\", 1";
		sql += "         , Length(";
		sql += "                    Cb.\"BdLocation\"";
		sql += "                ) - 12";
		sql += "            )";
		sql += "            WHEN Cb.\"BdLocation\" IS NULL THEN Substr(";
		sql += "                Cl.\"LandLocation\", 1";
		sql += "         , Length(";
		sql += "                    Cl.\"LandLocation\"";
		sql += "                ) - 12";
		sql += "            )";
		sql += "            ELSE Nvl(";
		sql += "                Cl.\"LandLocation\", Cb.\"BdLocation\"";
		sql += "            )";
		sql += "        END AS \"BdLocation\"";
		sql += "             , ROW_NUMBER() OVER(PARTITION BY M.\"CustNo\"";
		sql += "            ORDER BY M.\"ClNo\" ASC";
		sql += "        ) AS \"Seq\"";
		sql += "             , M.\"ClCode1\"";
		sql += "             , M.\"ClCode2\"";
		sql += "             , M.\"ClNo\"";
		sql += "             , F.\"Supervisor\"";
		sql += "        FROM \"MonthlyFacBal\"  M";
		sql += "        LEFT JOIN \"ClBuilding\"     Cb ON Cb.\"ClCode1\" = M.\"ClCode1\"";
		sql += "                                     AND";
		sql += "                                     Cb.\"ClCode2\" = M.\"ClCode2\"";
		sql += "                                     AND";
		sql += "                                     Cb.\"ClNo\" = M.\"ClNo\"";
		sql += "        LEFT JOIN \"ClLand\"         Cl ON Cl.\"ClCode1\" = M.\"ClCode1\"";
		sql += "                                 AND";
		sql += "                                 Cl.\"ClCode2\" = M.\"ClCode2\"";
		sql += "                                 AND";
		sql += "                                 Cl.\"ClNo\" = M.\"ClNo\"";
		sql += "        LEFT JOIN \"FacMain\"        F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                                 AND";
		sql += "                                 F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "        WHERE M.\"YearMonth\" = :Yymm";
		sql += "              AND";
		sql += "              M.\"PrinBalance\" > 0";
		sql += "    )";
		sql += "    SELECT DISTINCT Nvl( R.\"Rel\", 'E' ) AS \"Rel\"";
		sql += "                  , M.\"CustNo\"                         AS \"CustNo\"";
		sql += "                  , Nvl(Cm1.\"CustId\", Cm2.\"CustId\")  AS \"CustId\"";
		sql += "                  , Nvl(Cm1.\"CustName\", Cm2.\"CustName\")        AS \"CustName\"";
		sql += "                  , Tl.\"BdLocation\"                              AS \"BdLocation\"";
		sql += "                  , Allm.\"DrawdownDate\"                          AS \"DrawdownDate\"";
		sql += "                  , Allm.\"LoanBal\"                               AS \"LoanBal\"";
		sql += "                  , CASE";
		sql += "        			  WHEN Fm.\"Supervisor\" = '999999'";
		sql += "             		    OR Fm.\"Supervisor\" IS NULL THEN 'B'";
		sql += "        			  ELSE To_Char( Ce.\"Fullname\" )";
		sql += "    				END AS \"Supervisor\"";
		sql += "    FROM \"MonthlyFacBal\"  M";
		sql += "    LEFT JOIN \"FacShareAppl\"   F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                                  AND";
		sql += "                                  F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "    LEFT JOIN \"tmpRel\"         R ON R.\"CustNo\" = M.\"CustNo\"";
		sql += "    LEFT JOIN (";
		sql += "        SELECT *";
		sql += "        FROM \"isTmpE\"";
		sql += "        WHERE \"LineAmt\" >= 100000000";
		sql += "    ) M2 ON M2.\"CustNo\" = M.\"CustNo\"";
		sql += "    LEFT JOIN (";
		sql += "        SELECT *";
		sql += "        FROM \"isTmpE\"";
		sql += "        WHERE \"LineAmt\" < 100000000";
		sql += "    ) M3 ON M3.\"CustNo\" = M.\"CustNo\"";
		sql += "    LEFT JOIN \"CustMain\"       Cm1 ON Cm1.\"CustNo\" = R.\"CustNo\"";
		sql += "    LEFT JOIN \"CustMain\"       Cm2 ON Cm2.\"CustNo\" = M2.\"CustNo\"";
		sql += "    LEFT JOIN \"FacMain\"        Fm ON Fm.\"CustNo\" = M.\"CustNo\"";
		sql += "                              AND";
		sql += "                              Fm.\"FacmNo\" = M.\"FacmNo\"";
		sql += "    LEFT JOIN \"tmpLocation\"    Tl ON Tl.\"CustNo\" = M.\"CustNo\"";
		sql += "                                  AND";
		sql += "                                  Tl.\"Seq\" = 1";
		sql += "    LEFT JOIN \"isTmpE\"         Allm ON Allm.\"CustNo\" = M.\"CustNo\"";
		sql += "    LEFT JOIN \"CdEmp\"          Ce ON Ce.\"EmployeeNo\" = Tl.\"Supervisor\"";
		sql += "    WHERE M.\"YearMonth\" = :Yymm";
		sql += "          AND";
		sql += "          M.\"PrinBalance\" > 0";
		sql += "          AND";
		sql += "          ( M2.\"CustNo\" IS NOT NULL";
		sql += "            OR";
		sql += "            R.\"CustNo\" IS NOT NULL )";
		sql += "          AND";
		sql += "          Nvl(";
		sql += "              F.\"KeyinSeq\", 1";
		sql += "          ) = 1";
		sql += "    ORDER BY  Nvl(Cm1.\"CustId\", Cm2.\"CustId\")  ASC ";
		this.info("sql=" + sql);

		
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("Yymm", inputYearMonth);

		return this.convertToMap(query);
	}

	/*
	 * 查前一季底的淨值(查核數)
	 */
	public List<Map<String, String>> findStockHoldersEqt(int yearMonth, TitaVo titaVo) throws Exception {

		this.info("LY007.findStockHoldersEqt ");

		this.info("yearMonth =" + yearMonth);
		String sql = "";
		sql += " select \"AcDate\" , \"StockHoldersEqt\" from \"InnFundApl\" ";
		sql += " where \"AcDate\" = (";
		sql += " 	select max(\"AcDate\") from \"InnFundApl\" ";
		sql += " 	where trunc(\"AcDate\"/100) <= :yymm ";
		sql += " 	  and \"PosbleBorPsn\" > 0 ";
		sql += " )";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		return this.convertToMap(query);
	}

}