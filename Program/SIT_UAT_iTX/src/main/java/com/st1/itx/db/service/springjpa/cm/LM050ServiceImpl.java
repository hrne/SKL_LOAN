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

@Service
@Repository
public class LM050ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> fnEquity(TitaVo titaVo) throws Exception {
		String entdy = String.valueOf(titaVo.getEntDyI() + 19110000);
		String yy = entdy.substring(0, 4);
		String mm = entdy.substring(4, 6);
		String yyqq = "";
		switch (mm) {
		case "01":
		case "02":
		case "03":
			yyqq = String.valueOf(Integer.valueOf(yy) - 1) + "12";
			break;
		case "04":
		case "05":
		case "06":
			yyqq = yy + "03";
			break;
		case "07":
		case "08":
		case "09":
			yyqq = yy + "06";
			break;
		case "10":
		case "11":
		case "12":
			yyqq = yy + "09";
			break;
		}
		this.info("lM050.Totalequity yyqq=" + yyqq);
		String sql = " ";
		sql += " SELECT V.\"Totalequity\" ";
		sql += "      , V.\"YearMonth\" ";
		sql += " FROM \"CdVarValue\" V ";
		sql += " WHERE V.\"YearMonth\" = :lyyqq ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("lyyqq", yyqq);
		return this.convertToMap(query);

	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		int inputYearMonth = (titaVo.getEntDyI() + 19110000) / 100;

		this.info("LM050ServiceImpl findAll inputYearMonth=" + inputYearMonth);

		String sql = " ";
		sql += " SELECT CASE ";
		sql += "          WHEN NVL(S1.\"RptId\",' ') != ' ' ";
		sql += "          THEN '1' "; // -- 保險業利害關係人放款管理辦法第3條利害關係人
		sql += "          WHEN CM.\"EmpNo\" IS NOT NULL ";
		sql += "          THEN '2' "; // -- 職員
		sql += "        ELSE '3' "; // -- 一般客戶
		sql += "        END               AS \"RptType\" "; // F0
		sql += "      , CASE ";
		sql += "          WHEN NVL(S1.\"RptId\",' ') != ' ' ";
		sql += "          THEN S0.\"CustNo\" ";
		sql += "        ELSE 0 END        AS \"CustNo\"  "; // F1
		sql += "      , CASE ";
		sql += "          WHEN NVL(S1.\"RptId\",' ') != ' ' ";
		sql += "          THEN CM.\"CustName\" ";
		sql += "        ELSE N' ' END      AS \"CustName\"  "; // F2
		sql += "      , SUM(S0.\"LoanBal\") AS \"LoanBal\" "; // F3
		sql += " FROM ( SELECT \"CustNo\" ";
		sql += "             , SUM(\"LoanBalance\") AS \"LoanBal\" ";
		sql += "        FROM \"MonthlyLoanBal\" ";
		sql += "        WHERE \"YearMonth\" = :inputYearMonth  ";
		sql += "          AND \"LoanBalance\" > 0 ";
		sql += "        GROUP BY \"CustNo\" ";
		sql += "      ) S0 ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = S0.\"CustNo\" ";
		sql += " LEFT JOIN ( SELECT TO_CHAR(\"CusId\") AS \"RptId\" ";
		sql += "             FROM \"RptRelationSelf\" ";
		sql += "             WHERE \"LAW005\" = '1' ";
		sql += "             UNION ";
		sql += "             SELECT TO_CHAR(\"RlbID\") AS \"RptId\" ";
		sql += "             FROM \"RptRelationFamily\" ";
		sql += "             WHERE \"LAW005\" = '1' ";
		sql += "             UNION ";
		sql += "             SELECT TO_CHAR(\"ComNo\") AS \"RptId\" ";
		sql += "             FROM \"RptRelationCompany\" ";
		sql += "             WHERE \"LAW005\" = '1' ";
		sql += "           ) S1 ON S1.\"RptId\" = CM.\"CustId\" ";
		sql += " GROUP BY CASE ";
		sql += "            WHEN NVL(S1.\"RptId\",' ') != ' ' ";
		sql += "            THEN '1' "; // -- 保險業利害關係人放款管理辦法第3條利害關係人
		sql += "            WHEN CM.\"EmpNo\" IS NOT NULL ";
		sql += "            THEN '2' "; // -- 職員
		sql += "          ELSE '3' "; // -- 一般客戶
		sql += "          END ";
		sql += "        , CASE ";
		sql += "            WHEN NVL(S1.\"RptId\",' ') != ' ' ";
		sql += "            THEN S0.\"CustNo\" ";
		sql += "          ELSE 0 END ";
		sql += "        , CASE ";
		sql += "            WHEN NVL(S1.\"RptId\",' ') != ' ' ";
		sql += "            THEN CM.\"CustName\" ";
		sql += "          ELSE N' ' END ";
		sql += " ORDER BY \"RptType\" ";
		sql += "        , \"LoanBal\" DESC  ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", inputYearMonth);
		return this.convertToMap(query);
	}

}