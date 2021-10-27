package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("LY005ServiceImpl")
@Repository
public class LY005ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> queryDetail(int inputYearMonth) throws Exception {
		this.info("LY005ServiceImpl queryDetail ");
		String sql = "";
		sql += " WITH \"LoanData\" AS ( ";
		sql += "     SELECT \"CustNo\" ";
		sql += "          , SUM(\"LoanBalance\") AS \"LoanTotal\" ";
		sql += "     FROM \"MonthlyLoanBal\" ";
		sql += "     WHERE \"YearMonth\" = :inputYearMonth ";
		sql += "     GROUP BY \"CustNo\" ";
		sql += " ) ";
		sql += " , \"FacData\" AS ( ";
		sql += "     SELECT FAC.\"CustNo\" ";
		sql += "          , FAC.\"FirstDrawdownDate\" ";
		sql += "          , CASE ";
		sql += "              WHEN \"ApprovedLevel\" = '9' ";
		sql += "              THEN N'B' ";
		sql += "            ELSE \"Fn_GetEmpName\"(FAC.\"Supervisor\",1) ";
		sql += "            END AS \"ApproveEmp\" ";
		sql += "          , ROW_NUMBER() OVER (PARTITION BY FAC.\"CustNo\" ";
		sql += "                               ORDER BY FAC.\"FirstDrawdownDate\" DESC ";
		sql += "                              ) AS \"Seq\"  ";
		sql += "     FROM \"FacMain\" FAC ";
		sql += " ) ";
		sql += " , \"ClData\" AS ( ";
		sql += "     SELECT CF.\"CustNo\" ";
		sql += "          , CASE ";
		sql += "              WHEN CF.\"ClCode1\" = 1 ";
		sql += "              THEN TO_NCHAR(NVL(CB.\"BdLocation\",N' ')) ";
		sql += "              WHEN CF.\"ClCode1\" = 2 ";
		sql += "              THEN TO_NCHAR(NVL(CL.\"LandLocation\",N' ')) ";
		sql += "            ELSE N' ' END  ";
		sql += "         || CASE ";
		sql += "              WHEN CF.\"ClCode1\" = 1  ";
		sql += "                   AND NVL(CB.\"BdLocation\",' ') != ' '  ";
		sql += "                   AND CFC.\"Count\" >= 2 ";
		sql += "              THEN N'等' ";
		sql += "              WHEN CF.\"ClCode1\" = 2  ";
		sql += "                   AND NVL(CL.\"LandLocation\",' ') != ' '  ";
		sql += "                   AND CFC.\"Count\" >= 2 ";
		sql += "              THEN N'等' ";
		sql += "            ELSE N' ' END  ";
		sql += "         || CASE ";
		sql += "              WHEN CF.\"ClCode1\" = 1  ";
		sql += "                   AND NVL(CB.\"BdLocation\",' ') != ' ' ";
		sql += "              THEN N'房地' ";
		sql += "              WHEN CF.\"ClCode1\" = 2  ";
		sql += "                   AND NVL(CL.\"LandLocation\",' ') != ' ' ";
		sql += "              THEN N'土地' ";
		sql += "            ELSE N' ' END AS \"Location\" ";
		sql += "     FROM ( ";
		sql += "         SELECT CF.\"CustNo\" ";
		sql += "              , CF.\"FacmNo\" ";
		sql += "              , CF.\"ClCode1\" ";
		sql += "              , CF.\"ClCode2\" ";
		sql += "              , CF.\"ClNo\" ";
		sql += "              , ROW_NUMBER() OVER (PARTITION BY CF.\"CustNo\" ";
		sql += "                                   ORDER BY CF.\"FacmNo\" DESC ";
		sql += "                                  ) AS \"Seq\"  ";
		sql += "         FROM \"ClFac\" CF ";
		sql += "         WHERE CF.\"MainFlag\" = 'Y' ";
		sql += "     ) CF ";
		sql += "     LEFT JOIN ( ";
		sql += "         SELECT CF.\"CustNo\" ";
		sql += "              , COUNT(*) AS \"Count\" ";
		sql += "         FROM \"ClFac\" CF ";
		sql += "         GROUP BY CF.\"CustNo\" ";
		sql += "     ) CFC ON CFC.\"CustNo\" = CF.\"CustNo\" ";
		sql += "     LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                              AND CB.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                              AND CB.\"ClNo\" = CF.\"ClNo\" ";
		sql += "     LEFT JOIN \"ClLand\" CL ON CL.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                          AND CL.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                          AND CL.\"ClNo\" = CF.\"ClNo\" ";
		sql += "     WHERE CF.\"Seq\" = 1 ";
		sql += " ) ";
		sql += " , R AS ( ";
		sql += "     SELECT 'A' AS \"RptACode\" ";
		sql += "          , CM.\"CustNo\" ";
		sql += "     FROM \"RptRelationSelf\" R ";
		sql += "     LEFT JOIN \"CustMain\" CM ON CM.\"CustId\" = R.\"CusId\" ";
		sql += "     LEFT JOIN \"LoanData\" LD ON LD.\"CustNo\" = CM.\"CustNo\" ";
		sql += "     WHERE R.\"LAW005\" = '1' ";
		sql += "       AND CM.\"CustNo\" > 0 ";
		sql += "       AND LD.\"LoanTotal\" > 0 ";
		sql += "     UNION ";
		sql += "     SELECT 'H' AS \"RptACode\" ";
		sql += "          , CM.\"CustNo\" ";
		sql += "     FROM \"RptRelationFamily\" R ";
		sql += "     LEFT JOIN \"CustMain\" CM ON CM.\"CustId\" = R.\"RlbID\" ";
		sql += "     LEFT JOIN \"LoanData\" LD ON LD.\"CustNo\" = CM.\"CustNo\" ";
		sql += "     WHERE R.\"LAW005\" = '1' ";
		sql += "       AND CM.\"CustNo\" > 0 ";
		sql += "       AND LD.\"LoanTotal\" > 0 ";
		sql += "     UNION ";
		sql += "     SELECT 'H' AS \"RptACode\" ";
		sql += "          , CM.\"CustNo\" ";
		sql += "     FROM \"RptRelationCompany\" R ";
		sql += "     LEFT JOIN \"CustMain\" CM ON CM.\"CustId\" = R.\"ComNo\" ";
		sql += "     LEFT JOIN \"LoanData\" LD ON LD.\"CustNo\" = CM.\"CustNo\" ";
		sql += "     WHERE R.\"LAW005\" = '1' ";
		sql += "       AND CM.\"CustNo\" > 0 ";
		sql += "       AND LD.\"LoanTotal\" > 0 ";
		sql += " ) ";
		sql += " , E AS ( ";
		sql += "     SELECT 'E' AS \"RptACode\" ";
		sql += "          , \"CustNo\" ";
		sql += "     FROM \"LoanData\" LD ";
		sql += "     WHERE LD.\"LoanTotal\" >= 100000000 "; // 一億元以上
		sql += " ) ";
		sql += " , G AS ( ";
		sql += "     SELECT 'G' AS \"RptACode\" ";
		sql += "          , CM.\"CustNo\" ";
		sql += "     FROM \"CustMain\" CM ";
		sql += "     LEFT JOIN \"LoanData\" LD ON LD.\"CustNo\" = CM.\"CustNo\" ";
		sql += "     WHERE CM.\"EmpNo\" IS NOT NULL ";
		sql += "       AND LD.\"LoanTotal\" > 0 ";
		sql += " ) ";
		sql += " , CDV AS ( ";
		sql += "     SELECT \"Totalequity\" ";
		sql += "          , ROW_NUMBER() OVER (ORDER BY \"YearMonth\" DESC ";
		sql += "                              ) AS \"Seq\"  ";
		sql += "     FROM \"CdVarValue\" ";
		sql += "     WHERE \"YearMonth\" <= :inputYearMonth ";
		sql += " ) ";
		sql += " SELECT S.\"RptACode\"    AS F0 "; // 與本公司關係
		sql += "      , CM.\"CustId\"     AS F1 "; // 交易對象代號(統編)
		sql += "      , CM.\"CustName\"   AS F2 "; // 交易對象名稱(姓名)
		sql += "      , 'A'               AS F3 "; // 交易種類
		sql += "      , 'C'               AS F4 "; // 交易型態
		sql += "      , CD.\"Location\"   AS F5 "; // 交易標的內容(擔保品地址+擔保品類別(房地、土地))
		sql += "      , FD.\"FirstDrawdownDate\" ";
		sql += "                          AS F6 "; // 交易日期(額度檔最新一筆初貸日)
		sql += "      , LD.\"LoanTotal\"  AS F7 "; // 交易金額(放款餘額)
		sql += "      , 0                 AS F8 "; // 最近交易日之市價（樣張空白）
		sql += "      , 0                 AS F9 "; // 已實現損益（樣張空白）
		sql += "      , 0                 AS F10 "; // 未實現損益（樣張空白）
		sql += "      , CASE ";
		sql += "          WHEN CDV.\"Totalequity\" > 0 ";
		sql += "          THEN LD.\"LoanTotal\" / CDV.\"Totalequity\" ";
		sql += "        ELSE 0 ";
		sql += "        END               AS F11 "; // 交易金額佔業主權益比率%(放款餘額/業主權益)
		sql += "      , FD.\"ApproveEmp\" AS F12 "; // 最後決定權人員(核決主管/核決層級)
		sql += "      , ''                AS F13 "; // 備註
		sql += " FROM ( ";
		sql += "     SELECT LISTAGG(S.\"RptACode\",';') AS \"RptACode\" ";
		sql += "         , S.\"CustNo\" ";
		sql += "     FROM ( ";
		sql += "         SELECT \"RptACode\" ";
		sql += "             , \"CustNo\" ";
		sql += "         FROM R ";
		sql += "         UNION  ";
		sql += "         SELECT \"RptACode\" ";
		sql += "             , \"CustNo\" ";
		sql += "         FROM E ";
		sql += "         UNION  ";
		sql += "         SELECT \"RptACode\" ";
		sql += "             , \"CustNo\" ";
		sql += "         FROM G ";
		sql += "     ) S ";
		sql += "     GROUP BY \"CustNo\" ";
		sql += " ) S ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = S.\"CustNo\" ";
		sql += " LEFT JOIN \"ClData\" CD ON CD.\"CustNo\" = S.\"CustNo\" ";
		sql += " LEFT JOIN \"LoanData\" LD ON LD.\"CustNo\" = S.\"CustNo\" ";
		sql += " LEFT JOIN \"FacData\" FD ON FD.\"CustNo\" = S.\"CustNo\" ";
		sql += "                       AND FD.\"Seq\" = 1 ";
		sql += " LEFT JOIN CDV ON CDV.\"Seq\" = 1 ";
		sql += " ORDER BY LD.\"LoanTotal\" DESC ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}

}