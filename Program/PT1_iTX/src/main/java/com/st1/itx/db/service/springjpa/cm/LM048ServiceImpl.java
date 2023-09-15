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

/**
 * 
 * 20230905 User(舜雯)已確認 企業放款餘額限額 不需要判斷，顯示所有資料即可
 * 
 * 
 */
@Service
@Repository
/* 逾期放款明細 */
public class LM048ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> queryDetail(int inputYearMonth, TitaVo titaVo) {

		this.info("LM048ServiceImpl queryDetail ");
		this.info("inputYearMonth = " + inputYearMonth);

		String sql = " ";
		sql += "    SELECT Ci.\"IndustryCode\" ";
		sql += "         , Ci.\"IndustryItem\" ";
		sql += "         , Ci.\"IndustryRating\" ";
		sql += "         , M.\"CustNo\" ";
		sql += "         , M.\"CustName\" ";
		sql += "         , M.\"LineAmt\" ";
		sql += "         , M.\"PrinBalance\" AS \"LoanBal\"";
		sql += "         , M.\"StoreRate\" ";
		sql += "         , M.\"MaturityDate\" ";
		sql += "         , M.\"PrevPayIntDate\" ";
		sql += "         , NVL(M.\"CustNoMain\",M.\"CustNo\") AS \"CustNoMain\" ";
		sql += "         , NVL(M.\"CustNameMain\",M.\"CustName\") AS \"CustNameMain\" ";
		sql += "    FROM ( ";
		sql += "        SELECT DISTINCT Substr( ";
		sql += "            \"IndustryCode\", 3 ";
		sql += "         , 4 ";
		sql += "        ) AS \"IndustryCode\" ";
		sql += "                      , \"IndustryItem\" ";
		sql += "                      , \"IndustryRating\" ";
		sql += "        FROM \"CdIndustry\" ";
		sql += "        WHERE \"IndustryRating\" IS NOT NULL ";
		sql += "              AND ";
		sql += "              \"MainType\" IS NOT NULL ";
		sql += "    ) Ci ";
		sql += "    left join ( ";
		sql += "    SELECT Cm.\"CustNo\" ";
		sql += "         , Cm.\"CustName\" ";
		sql += "         , Round( NVL(  ";
		sql += "         		CASE";
		sql += "         		  WHEN Fsa.\"CustNo\" IS NOT NULL ";
		sql += "         		  THEN Fsl.\"LineAmt\"";
		sql += "         		  ELSE Fm.\"LineAmt\"";
		sql += "         		END , 0 )";
		sql += "          / 1000 )      AS \"LineAmt\" ";
		sql += "         , Round(M.\"PrinBalance\" / 1000)   AS \"PrinBalance\" ";
		sql += "         , M2.\"StoreRate\" ";
		sql += "         , Fm.\"MaturityDate\" ";
		sql += "         , M2.\"PrevPayIntDate\" ";
		sql += "         , Cg.\"CustNo\"                   AS \"CustNoMain\" ";
		sql += "         , Decode( ";
		sql += "        Cg.\"CustName\", NULL ";
		sql += "         , '        ' ";
		sql += "         , To_Char(Cg.\"CustName\" || '關係企業') ";
		sql += "    )                   AS \"CustNameMain\" ";
		sql += "         , Cm.\"IndustryCode\" ";
		sql += "         , Substr( ";
		sql += "        Cm.\"IndustryCode\", 3 ";
		sql += "         , 4 ";
		sql += "    ) AS \"IndustryCode2\" ";
		sql += "    FROM \"MonthlyFacBal\"  M ";
		sql += "    LEFT JOIN \"FacMain\"     Fm ON Fm.\"CustNo\" = M.\"CustNo\" ";
		sql += "                                AND Fm.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "    LEFT JOIN \"FacShareAppl\" Fsa ON Fsa.\"CustNo\" = Fm.\"CustNo\" ";
		sql += "                                  AND Fsa.\"FacmNo\" = Fm.\"FacmNo\" ";
		sql += "                                  AND Fsa.\"ApplNo\" = Fm.\"ApplNo\" ";
		sql += "    LEFT JOIN \"FacShareLimit\" Fsl ON Fsa.\"KeyinSeq\" = 1 ";
		sql += "                                  AND Fsl.\"CustNo\" = Fsa.\"CustNo\" ";
		sql += "                                  AND Fsl.\"FacmNo\" = Fsa.\"FacmNo\" ";
		sql += "                                  AND Fsl.\"MainApplNo\" = Fsa.\"MainApplNo\" ";
		sql += "    LEFT JOIN \"CustMain\"       Cm ON Cm.\"CustNo\" = M.\"CustNo\" ";
		sql += "    LEFT JOIN \"ReltMain\"       Rm ON Rm.\"ReltUKey\" = Cm.\"CustUKey\" ";
		sql += "    LEFT JOIN \"CustMain\"       Cg ON Cg.\"CustNo\" = Rm.\"CustNo\" ";
		sql += "    LEFT join ( SELECT DISTINCT M.\"YearMonth\" ";
		sql += "                              , M.\"CustNo\" ";
		sql += "                              , M.\"FacmNo\" ";
		sql += "                              , M.\"StoreRate\" ";
		sql += "                              , M.\"PrevPayIntDate\"";
		sql += "                FROM \"MonthlyLoanBal\" M ";
		sql += "                LEFT JOIN (";
		sql += "   						SELECT  M.\"YearMonth\" ";
		sql += "                              , M.\"CustNo\" ";
		sql += "                              , M.\"FacmNo\" ";
		sql += "                              , MIN(M.\"PrevPayIntDate\") AS \"PrevPayIntDate\" ";
		sql += "               		    FROM \"MonthlyLoanBal\" M ";
		sql += "                		WHERE M.\"YearMonth\" = :yymm  ";
		sql += "                		GROUP BY M.\"YearMonth\"";
		sql += "                				,M.\"CustNo\"";
		sql += "                				,M.\"FacmNo\"";
		sql += "                ) M2 ON M2.\"CustNo\" = M.\"CustNo\"";
		sql += "                	AND M2.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                	AND M2.\"YearMonth\" = M.\"YearMonth\"";
		sql += "                WHERE M.\"YearMonth\" = :yymm  ";
		sql += "    ) m2 on m2.\"CustNo\"=m.\"CustNo\"  and  m2.\"FacmNo\" = m.\"FacmNo\" ";
		sql += "    where m.\"YearMonth\"= :yymm ";
		sql += "    and trunc(m2.\"PrevPayIntDate\"/100) >=  :yymm ";
		sql += "    and m.\"EntCode\" IN (1)  ";
		sql += "    ) m on m.\"IndustryCode2\" = ci.\"IndustryCode\" ";
		sql += "    Order By Ci.\"IndustryRating\" ";
		sql += "           , Ci.\"IndustryCode\" ";
		sql += "           , Nvl(M.\"CustNoMain\", 9999999) ";
		sql += "           , Nvl(M.\"CustNo\", 9999999) ";

//		sql += " SELECT CASE ";
//		sql += "          WHEN CDI.\"IndustryRating\" IS NOT NULL "; // -- 有評等
//		sql += "          THEN CDI.\"IndustryRating\" ";
//		sql += "          WHEN ML.\"LoanBal\" > :entLoanBalLimit "; // -- 企業放款餘額大於限額
//		sql += "          THEN 'B_Empty' ";
//		sql += "          WHEN GP.\"GroupName\" IS NOT NULL "; // -- 有同一關係企業或集團戶
//		sql += "          THEN 'B_Empty' ";
//		sql += "        ELSE 'B_Else' "; // -- B評等其他
//		sql += "        END                         AS Rating "; // -- F0 評等
//		sql += "      , CDI.\"IndustryItem\"        AS IndustryItem "; // -- F1 行業別名稱
//		sql += "      , LPAD(CM.\"CustNo\",7,'0')   AS CustNo "; // -- F2 戶號
//		sql += "      , CM.\"CustName\"             AS CustName "; // -- F3 戶名
//		sql += "      , FAC.\"LineAmt\"             AS LineAmt "; // -- F4 核貸金額
//		sql += "      , ML.\"LoanBal\"              AS LoanBal "; // -- F5 放款本金餘額
//		sql += "      , ML.\"StoreRate\"            AS StoreRate "; // -- F6 利率(%)
//		sql += "      , LBM.\"MaturityDate\"        AS MaturityDate "; // -- F7 到期日
//		sql += "      , LBM.\"PrevPayIntDate\"      AS PrevPayIntDate "; // -- F8 繳息迄日
//		sql += "      , GP.\"GroupName\"            AS GroupName "; // -- F9 同一關係企業或集團名稱
//		sql += "      , GP.\"GroupPercentage\"      AS GroupPercentage "; // -- F10 同一關係企業或集團核貸金額占淨值比例
//		sql += "      , ROUND(CUSTFAC.\"LineAmt\" / CDV.\"Totalequity\" , 8) ";
//		sql += "                                    AS CustPercentage "; // -- F11 單一授信對象核貸金額占淨值比例
//		sql += " FROM \"CustMain\" CM ";
//		sql += " LEFT JOIN \"CdIndustry\" CDI ON CDI.\"IndustryCode\" = CM.\"IndustryCode\" ";
//		sql += " LEFT JOIN ( ";
//		sql += "     SELECT \"StockHoldersEqt\" AS \"Totalequity\"";
//		sql += "           ,TRUNC(\"AcDate\" /100 ) AS \"YearMonth\"";
//		sql += "     FROM \"InnFundApl\" ";
//		sql += "     WHERE \"AcDate\" = (";
//		sql += "     	SELECT MAX(\"AcDate\") ";
//		sql += "     	FROM \"InnFundApl\" ";
//		sql += "     	WHERE TRUNC(\"AcDate\" / 100) = :inputYearMonth";
//		sql += "     )";
//		sql += " )  CDV ON CDV.\"YearMonth\" = :inputYearMonth ";
//		sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = CM.\"CustNo\" ";
//		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
//		sql += "                  , \"FacmNo\" ";
//		sql += "                  , SUM(\"LoanBalance\") AS \"LoanBal\" ";
//		sql += "                  , MAX(\"StoreRate\") AS \"StoreRate\" ";
//		sql += "             FROM \"MonthlyLoanBal\" ";
//		sql += "             WHERE \"LoanBalance\" > 0 ";
//		sql += "               AND \"YearMonth\" = :inputYearMonth ";
//		sql += "             GROUP BY \"CustNo\" ";
//		sql += "                    , \"FacmNo\" ";
//		sql += "           ) ML ON ML.\"CustNo\" = FAC.\"CustNo\" ";
//		sql += "               AND ML.\"FacmNo\" = FAC.\"FacmNo\" ";
//		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
//		sql += "                  , \"FacmNo\" ";
//		sql += "                  , MAX(\"MaturityDate\") AS \"MaturityDate\" ";
//		sql += "                  , MIN(\"PrevPayIntDate\") AS \"PrevPayIntDate\" ";
//		sql += "             FROM \"LoanBorMain\" ";
//		sql += "             WHERE \"LoanBal\" > 0 ";
//		sql += "               AND TRUNC(\"DrawdownDate\" / 100) <= :inputYearMonth ";
//		sql += "             GROUP BY \"CustNo\" ";
//		sql += "                    , \"FacmNo\" ";
//		sql += "           ) LBM ON LBM.\"CustNo\" = FAC.\"CustNo\" ";
//		sql += "                AND LBM.\"FacmNo\" = FAC.\"FacmNo\" ";
//		sql += " LEFT JOIN ( SELECT FAC.\"CustNo\" ";
//		sql += "                  , SUM(FAC.\"LineAmt\") AS \"LineAmt\"";
//		sql += "             FROM \"FacMain\" FAC ";
//		sql += "             LEFT JOIN ( SELECT \"CustNo\" ";
//		sql += "                              , \"FacmNo\" ";
//		sql += "                              , SUM(\"LoanBalance\") AS \"LoanBal\" ";
//		sql += "                         FROM \"MonthlyLoanBal\" ";
//		sql += "                         WHERE \"LoanBalance\" > 0 ";
//		sql += "                           AND \"YearMonth\" = :inputYearMonth ";
//		sql += "                         GROUP BY \"CustNo\" ";
//		sql += "                                , \"FacmNo\" ";
//		sql += "                       ) ML ON ML.\"CustNo\" = FAC.\"CustNo\" ";
//		sql += "                           AND ML.\"FacmNo\" = FAC.\"FacmNo\" ";
//		sql += "             WHERE ML.\"LoanBal\" > 0 ";
//		sql += "             GROUP BY FAC.\"CustNo\" ";
//		sql += "           ) CUSTFAC ON CUSTFAC.\"CustNo\" = CM.\"CustNo\" ";
//		sql += " LEFT JOIN ( SELECT RM.\"CustNo\" ";
//		sql += "                  , CRM.\"CustName\" || '關係企業' AS \"GroupName\" ";
//		sql += "                  , ROUND((FAC.\"LineAmt\" + GP.\"GroupLineAmt\") / CDV.\"Totalequity\" , 8) ";
//		sql += "                                                   AS \"GroupPercentage\" ";
//		sql += "             FROM \"ReltMain\" RM ";
//		sql += "             LEFT JOIN \"CustMain\" CRM ON CRM.\"CustNo\" = RM.\"CustNo\" ";
//		sql += "             LEFT JOIN \"CustMain\" CRD ON CRD.\"CustUKey\" = RM.\"ReltUKey\" ";
//		sql += "			 LEFT JOIN ( ";
//		sql += "			     SELECT \"StockHoldersEqt\" AS \"Totalequity\"";
//		sql += "			           ,TRUNC(\"AcDate\" /100 ) AS \"YearMonth\"";
//		sql += "			     FROM \"InnFundApl\" ";
//		sql += "			     WHERE \"AcDate\" = (";
//		sql += "			     	SELECT MAX(\"AcDate\") ";
//		sql += "			     	FROM \"InnFundApl\" ";
//		sql += "			     	WHERE TRUNC(\"AcDate\" / 100) = :inputYearMonth";
//		sql += "			     )";
//		sql += "			 )  CDV ON CDV.\"YearMonth\" = :inputYearMonth ";
//		sql += "             LEFT JOIN ( SELECT CM.\"CustId\" ";
//		sql += "                               , SUM(FAC.\"LineAmt\") AS \"LineAmt\" ";
//		sql += "                         FROM \"FacMain\" FAC  ";
//		sql += "                         LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = FAC.\"CustNo\" ";
//		sql += "                         LEFT JOIN ( SELECT \"CustNo\" ";
//		sql += "                                          , \"FacmNo\" ";
//		sql += "                                          , SUM(\"LoanBalance\") AS \"LoanBal\" ";
//		sql += "                                     FROM \"MonthlyLoanBal\" ";
//		sql += "                                     WHERE \"LoanBalance\" > 0 ";
//		sql += "                                       AND \"YearMonth\" = :inputYearMonth ";
//		sql += "                                     GROUP BY \"CustNo\" ";
//		sql += "                                            , \"FacmNo\" ";
//		sql += "                                   ) ML ON ML.\"CustNo\" = FAC.\"CustNo\" ";
//		sql += "                                       AND ML.\"FacmNo\" = FAC.\"FacmNo\" ";
//		sql += "                         WHERE TRUNC(FAC.\"FirstDrawdownDate\" / 100) <= :inputYearMonth ";
//		sql += "                           AND ML.\"LoanBal\" > 0 ";
//		sql += "                         GROUP BY CM.\"CustId\" ";
//		sql += "                       ) FAC ON FAC.\"CustId\" = CRM.\"CustId\" ";
//		sql += "             LEFT JOIN ( "; // -- 計算整組餘額
//		sql += "                         SELECT RM.\"CustNo\" ";
//		sql += "                              , SUM(NVL(FAC.\"LineAmt\",0)) AS \"GroupLineAmt\" "; // -- 放款餘額
//		sql += "                         FROM \"ReltMain\" RM ";
//		sql += "                         LEFT JOIN \"CustMain\" CRM ON CRM.\"CustNo\" = RM.\"CustNo\" ";
//		sql += "                         LEFT JOIN \"CustMain\" CRD ON CRD.\"CustUKey\" = RM.\"ReltUKey\" ";
//		sql += "                         LEFT JOIN ( SELECT CM.\"CustId\" ";
//		sql += "                                          , SUM(FAC.\"LineAmt\") AS \"LineAmt\" ";
//		sql += "                                     FROM \"FacMain\" FAC  ";
//		sql += "                                     LEFT JOIN ( SELECT \"CustNo\" ";
//		sql += "                                                      , \"FacmNo\" ";
//		sql += "                                                      , SUM(\"LoanBalance\") AS \"LoanBal\" ";
//		sql += "                                                 FROM \"MonthlyLoanBal\" ";
//		sql += "                                                 WHERE \"LoanBalance\" > 0 ";
//		sql += "                                                   AND \"YearMonth\" = :inputYearMonth ";
//		sql += "                                                 GROUP BY \"CustNo\" ";
//		sql += "                                                        , \"FacmNo\" ";
//		sql += "                                               ) ML ON ML.\"CustNo\" = FAC.\"CustNo\" ";
//		sql += "                                                   AND ML.\"FacmNo\" = FAC.\"FacmNo\" ";
//		sql += "                                     LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = FAC.\"CustNo\" ";
//		sql += "                                     WHERE TRUNC(FAC.\"FirstDrawdownDate\" / 100) <= :inputYearMonth ";
//		sql += "                                       AND ML.\"LoanBal\" > 0 ";
//		sql += "                                     GROUP BY CM.\"CustId\" ";
//		sql += "                                   ) FAC ON FAC.\"CustId\" = CRD.\"CustId\" ";
//		sql += "                         WHERE LENGTHB(CRM.\"CustId\") = 8 "; // -- 篩選關係企業
//		sql += "                           AND LENGTHB(CRD.\"CustId\") = 8 "; // -- 篩選關係企業
//		sql += "                         GROUP BY RM.\"CustNo\" ";
//		sql += "                       ) GP ON GP.\"CustNo\" = RM.\"CustNo\" ";
//		sql += "             WHERE LENGTHB(CRM.\"CustId\") = 8 "; // -- 篩選關係企業
//		sql += "               AND LENGTHB(CRD.\"CustId\") = 8 "; // -- 篩選關係企業
//		sql += "               AND NVL(FAC.\"LineAmt\",0) > 0 ";
//		sql += "               AND NVL(GP.\"GroupLineAmt\",0) + NVL(FAC.\"LineAmt\",0)> NVL(FAC.\"LineAmt\",0) "; // -- 兩筆以上才進表
//		sql += "           ) GP ON GP.\"CustNo\" = CM.\"CustNo\" ";
//		sql += " WHERE CM.\"EntCode\" = '1' "; // -- 企金別為1
//		sql += "   AND ML.\"LoanBal\" > 0 "; // -- 當月底有放款餘額
//		sql += " ORDER BY Rating ";
//		sql += "        , IndustryItem ";
//		sql += "        , GroupName";
//		sql += "        , CustNo ";
//		sql += "        , FAC.\"FacmNo\" ";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("yymm", inputYearMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> groupLineAmt(int inputYearMonth, int custNoMain, TitaVo titaVo) {

		this.info("LM048ServiceImpl groupLineAmt ");
		this.info("inputYearMonth = " + inputYearMonth);

		String sql = " ";
		sql += "    SELECT SUM(M.\"LineAmt\") AS \"ToTalLineAmt\" ";
		sql += "         , M.\"CustNo\" AS \"CustNoMain\" ";
		sql += "         , M.\"CustName\" AS \"CustNameMain\" ";
		sql += "    FROM ( ";
		sql += "        SELECT DISTINCT Substr( ";
		sql += "            \"IndustryCode\", 3 ";
		sql += "         , 4 ";
		sql += "        ) AS \"IndustryCode\" ";
		sql += "                      , \"IndustryItem\" ";
		sql += "                      , \"IndustryRating\" ";
		sql += "        FROM \"CdIndustry\" ";
		sql += "        WHERE \"IndustryRating\" IS NOT NULL ";
		sql += "              AND ";
		sql += "              \"MainType\" IS NOT NULL ";
		sql += "    ) Ci ";
		sql += "    left join ( ";
		sql += "    SELECT Cm.\"CustNo\" ";
		sql += "         , Cm.\"CustName\" ";
		sql += "         , Round( NVL(  ";
		sql += "         		CASE";
		sql += "         		  WHEN Fsa.\"CustNo\" IS NOT NULL ";
		sql += "         		  THEN Fsl.\"LineAmt\"";
		sql += "         		  ELSE Fm.\"LineAmt\"";
		sql += "         		END , 0 )";
		sql += "         )      AS \"LineAmt\" ";
		sql += "         , Round(M.\"PrinBalance\" / 1000)   AS \"PrinBalance\" ";
		sql += "         , M2.\"StoreRate\" ";
		sql += "         , Fm.\"MaturityDate\" ";
		sql += "         , M2.\"PrevPayIntDate\" ";
		sql += "         , Cg.\"CustNo\"                   AS \"CustNoMain\" ";
		sql += "         , Decode( ";
		sql += "        Cg.\"CustName\", NULL ";
		sql += "         , '        ' ";
		sql += "         , To_Char(Cg.\"CustName\" || '關係企業') ";
		sql += "    )                   AS \"CustNameMain\" ";
		sql += "         , Cm.\"IndustryCode\" ";
		sql += "         , Substr( ";
		sql += "        Cm.\"IndustryCode\", 3 ";
		sql += "         , 4 ";
		sql += "    ) AS \"IndustryCode2\" ";
		sql += "    FROM \"MonthlyFacBal\"  M ";
		sql += "    LEFT JOIN \"FacMain\"        Fm ON Fm.\"CustNo\" = M.\"CustNo\" ";
		sql += "                                 AND Fm.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "    LEFT JOIN \"FacShareAppl\" Fsa ON Fsa.\"CustNo\" = Fm.\"CustNo\" ";
		sql += "                                  AND Fsa.\"FacmNo\" = Fm.\"FacmNo\" ";
		sql += "                                  AND Fsa.\"ApplNo\" = Fm.\"ApplNo\" ";
		sql += "    LEFT JOIN \"FacShareLimit\" Fsl ON Fsa.\"KeyinSeq\" = 1 ";
		sql += "                                  AND Fsa.\"CustNo\" = Fsa.\"CustNo\" ";
		sql += "                                  AND Fsa.\"FacmNo\" = Fsa.\"FacmNo\" ";
		sql += "                                  AND Fsa.\"MainApplNo\" = Fsa.\"MainApplNo\" ";
		sql += "    LEFT JOIN \"CustMain\"       Cm ON Cm.\"CustNo\" = M.\"CustNo\" ";
		sql += "    LEFT JOIN \"ReltMain\"       Rm ON Rm.\"ReltUKey\" = Cm.\"CustUKey\" ";
		sql += "    LEFT JOIN \"CustMain\"       Cg ON Cg.\"CustNo\" = Rm.\"CustNo\" ";
		sql += "    LEFT join ( SELECT DISTINCT M.\"YearMonth\" ";
		sql += "                              , M.\"CustNo\" ";
		sql += "                              , M.\"FacmNo\" ";
		sql += "                              , M.\"StoreRate\" ";
		sql += "                              , M.\"PrevPayIntDate\"";
		sql += "                FROM \"MonthlyLoanBal\" M ";
		sql += "                LEFT JOIN (";
		sql += "   						SELECT  M.\"YearMonth\" ";
		sql += "                              , M.\"CustNo\" ";
		sql += "                              , M.\"FacmNo\" ";
		sql += "                              , MIN(M.\"PrevPayIntDate\") AS \"PrevPayIntDate\" ";
		sql += "               		    FROM \"MonthlyLoanBal\" M ";
		sql += "                		WHERE M.\"YearMonth\" = :yymm  ";
		sql += "                		GROUP BY M.\"YearMonth\"";
		sql += "                				,M.\"CustNo\"";
		sql += "                				,M.\"FacmNo\"";
		sql += "                ) M2 ON M2.\"CustNo\" = M.\"CustNo\"";
		sql += "                	AND M2.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                	AND M2.\"YearMonth\" = M.\"YearMonth\"";
		sql += "                WHERE M.\"YearMonth\" = :yymm  ";
		sql += "    ) m2 on m2.\"CustNo\"=m.\"CustNo\"  and  m2.\"FacmNo\" = m.\"FacmNo\" ";
		sql += "    where m.\"YearMonth\"= :yymm ";
		sql += "    and trunc(m2.\"PrevPayIntDate\"/100) >=  :yymm ";
		sql += "    and m.\"EntCode\" IN (1)  ";
		sql += "    ) m on m.\"IndustryCode2\" = ci.\"IndustryCode\" ";
		sql += "    Where  M.\"CustNoMain\" = " + custNoMain;
		sql += "    Group By M.\"CustNo\" ";
		sql += "           , M.\"CustName\" ";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("yymm", inputYearMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryLoanBal(int inputYearMonth, TitaVo titaVo) {
		this.info("LM048ServiceImpl queryLoanBal ");

		this.info("yearMonth = " + inputYearMonth);

		int lyy12 = (inputYearMonth / 100 - 1) * 100 + 12;

		this.info("lyy12 = " + lyy12);

		String sql = " ";
		sql += "    SELECT \"Name\" ";
		sql += "         , SUM(\"LoanBal\") AS \"LoanBal\" ";
		sql += "    FROM ( ";
		sql += "        SELECT 'LoanBal'              AS \"Name\" ";
		sql += "             , ROUND(SUM(\"PrinBalance\")/1000)     AS \"LoanBal\" ";
		sql += "        FROM \"MonthlyFacBal\" ";
		sql += "        WHERE \"YearMonth\" = :yymm ";
		sql += "              AND ";
		sql += "              \"PrinBalance\" > 0 ";
		sql += "        UNION ";
		sql += "        SELECT 'LoanBal'          AS \"Name\" ";
		sql += "             , ROUND(SUM(\"LoanBal\") / 1000)     AS \"LoanBal\" ";
		sql += "        FROM \"MonthlyLM052AssetClass\" ";
		sql += "        WHERE \"YearMonth\" = :yymm ";
		sql += "              AND ";
		sql += "              \"AssetClassNo\" IN ( ";
		sql += "                  '61' ";
		sql += "                , '62' ";
		sql += "              ) ";
		sql += "        UNION ";
		sql += "        SELECT 'NetWorth'                 AS \"Name\" ";
		sql += "             , ROUND(SUM(\"StockHoldersEqt\")/1000)     AS \"LoanBal\" ";
		sql += "        FROM \"InnFundApl\" ";
		sql += "        WHERE \"AcDate\" = ( ";
		sql += "            SELECT MAX(\"AcDate\") ";
		sql += "            FROM \"InnFundApl\" ";
		sql += "            WHERE Trunc(\"AcDate\" / 100) = :lyy12 ";
		sql += "                  AND ";
		sql += "                  \"StockHoldersEqt\" > 0 ";
		sql += "        ) ";
		sql += "    ) ";
		sql += "    GROUP BY \"Name\" ";

//		sql += " SELECT CASE ";
//		sql += "          WHEN CM.\"EntCode\" = '1' ";
//		sql += "          THEN '1' ";
//		sql += "        ELSE '0' END               AS EntCode "; // -- F0 企金別
//		sql += "      , SUM(NVL(ML.\"LoanBal\",0)) AS LoanBal "; // -- F1 企業放款總餘額
//		sql += " FROM \"CustMain\" CM ";
//		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
//		sql += "                  , SUM(\"LoanBalance\") AS \"LoanBal\" ";
//		sql += "             FROM \"MonthlyLoanBal\" ";
//		sql += "             WHERE \"LoanBalance\" > 0 ";
//		sql += "               AND \"YearMonth\" = :inputYearMonth ";
//		sql += "             GROUP BY \"CustNo\" ";
//		sql += "           ) ML ON ML.\"CustNo\" = CM.\"CustNo\" ";
//		sql += " WHERE ML.\"LoanBal\" > 0 "; // -- 當月底有放款餘額
//		sql += " GROUP BY CASE ";
//		sql += "          WHEN CM.\"EntCode\" = '1' ";
//		sql += "          THEN '1' ";
//		sql += "        ELSE '0' END ";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("yymm", inputYearMonth);
		query.setParameter("lyy12", lyy12);
		return this.convertToMap(query);
	}

}