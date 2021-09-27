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
public class LM036ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> queryDelinquency(int startMonth, int endMonth, TitaVo titaVo) throws Exception {
		this.info("LM036ServiceImpl queryDelinquency ");

		this.info("LM036ServiceImpl startMonth = " + startMonth);
		this.info("LM036ServiceImpl endMonth = " + endMonth);

		String sql = "";
		sql += " SELECT \"YearMonth\" "; // F0 資料年月
		sql += "      , SUM(\"EntNormal\")        AS \"EntNormal\" "; // F1 法人正常
		sql += "      , SUM(\"EntOvdue1To2\")     AS \"EntOvdue1To2\" "; // F2 法人逾1~2期
		sql += "      , SUM(\"EntOvdue3To6\")     AS \"EntOvdue3To6\" "; // F3 法人逾3~6期
		sql += "      , SUM(\"EntColl\")          AS \"EntColl\" "; // F4 法人催收
		sql += "      , SUM(\"NatNormal\")        AS \"NatNormal\" "; // F5 自然人正常
		sql += "      , SUM(\"NatOvdue1To2\")     AS \"NatOvdue1To2\" "; // F6 自然人逾1~2期
		sql += "      , SUM(\"NatOvdue3To6\")     AS \"NatOvdue3To6\" "; // F7 自然人逾3~6期
		sql += "      , SUM(\"NatColl\")          AS \"NatColl\" "; // F8 自然人催收
		sql += "      , SUM(\"Normal\")           AS \"Normal\" "; // F9 總額正常
		sql += "      , SUM(\"Ovdue1To2\")        AS \"Ovdue1To2\" "; // F10 總額逾1~2期
		sql += "      , SUM(\"Ovdue3To6\")        AS \"Ovdue3To6\" "; // F11 總額逾3~6期
		sql += "      , SUM(\"Coll\")             AS \"Coll\" "; // F12 總額催收
		sql += "      , SUM(\"Total\")            AS \"Total\" "; // F13 放款總餘額
		sql += " FROM ( ";
		sql += "     SELECT \"YearMonth\" ";
		sql += "          , \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"OvduDays\" ";
		sql += "          , \"OvduTerm\" ";
		sql += "          , \"EntCode\" ";
		sql += "          , \"AcctCode\" ";
		sql += "          , \"PrinBalance\" ";
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') = '1' ";
		sql += "                   AND \"OvduTerm\" = 0 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END             AS \"EntNormal\" "; // --法人正常
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') = '1' ";
		sql += "                   AND \"OvduTerm\" >= 1 ";
		sql += "                   AND \"OvduTerm\" <= 2 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"EntOvdue1To2\" "; // --法人逾1~2期
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') = '1' ";
		sql += "                   AND \"OvduTerm\" >= 3 ";
//		sql += "                   AND \"OvduTerm\" <= 6 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"EntOvdue3To6\" "; // --法人逾3~6期
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') = '1' ";
		sql += "                   AND \"AcctCode\" = '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"EntColl\" "; // --法人轉催收
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') != '1' ";
		sql += "                   AND \"OvduTerm\" = 0 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"NatNormal\" "; // --自然人正常
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') != '1' ";
		sql += "                   AND \"OvduTerm\" >= 1 ";
		sql += "                   AND \"OvduTerm\" <= 2 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"NatOvdue1To2\" "; // --自然人逾1~2期
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') != '1' ";
		sql += "                   AND \"OvduTerm\" >= 3 ";
//		sql += "                   AND \"OvduTerm\" <= 6 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"NatOvdue3To6\" "; // --自然人逾3~6期
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') != '1' ";
		sql += "                   AND \"AcctCode\" = '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"NatColl\" "; // --自然人轉催收
		sql += "          , CASE ";
		sql += "              WHEN \"OvduTerm\" = 0 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"Normal\" "; // --總額正常
		sql += "          , CASE ";
		sql += "              WHEN \"OvduTerm\" >= 1 ";
		sql += "                   AND \"OvduTerm\" <= 2 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"Ovdue1To2\" "; // --總額逾1~2期
		sql += "          , CASE ";
		sql += "              WHEN \"OvduTerm\" >= 3 ";
//		sql += "                   AND \"OvduTerm\" <= 6 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"Ovdue3To6\" "; // --總額逾3~6期
		sql += "          , CASE ";
		sql += "              WHEN \"AcctCode\" = '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"Coll\" "; // --總額轉催收
		sql += "          , \"PrinBalance\"  AS \"Total\" "; // --放款總餘額
		sql += "     FROM \"MonthlyFacBal\" ";
		sql += "     WHERE \"PrinBalance\" > 0 ";
		sql += "       AND \"YearMonth\" >= :startMonth ";
		sql += "       AND \"YearMonth\" <= :endMonth ";
		sql += " ) ";
		sql += " GROUP BY \"YearMonth\" ";
		sql += " ORDER BY \"YearMonth\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startMonth", startMonth);
		query.setParameter("endMonth", endMonth);

		return this.convertToMap(query);
	}

}