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
public class LM018ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM018.findAll ");

		int entdy = (parse.stringToInteger(titaVo.getParam("ENTDY")) + 19110000);

		String sql = "";
		sql += " WITH \"OutputMonths\" AS ( ";
		sql += " SELECT UNIQUE \"YearMonth\" ";
		sql += "               ,CASE WHEN \"Fn_GetWorkSeason\"(\"YearMonth\", 3) > :entYearMonth ";
		sql += "                     THEN TO_NUMBER(:entYearMonth) ";
		sql += "                ELSE \"Fn_GetWorkSeason\"(\"YearMonth\", 3) END \"VisibleMonth\" ";
		sql += " FROM \"MonthlyLoanBal\" ";
		sql += " WHERE \"YearMonth\" BETWEEN TO_NUMBER(:entYear) * 100 + 1 AND :entYearMonth ";
		sql += " ), ";
		sql += " \"SubjectProdNo\" AS ( ";
		sql += "  ";
		sql += " SELECT 'IA' AS \"ProdNo\"  ";
		sql += "       ,'IA' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT 'IB' AS \"ProdNo\"  ";
		sql += "       ,'IB' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT 'IC' AS \"ProdNo\"  ";
		sql += "       ,'IC' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT 'ID' AS \"ProdNo\"  ";
		sql += "       ,'ID' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT 'IE' AS \"ProdNo\"  ";
		sql += "       ,'ID' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT 'IF' AS \"ProdNo\"  ";
		sql += "       ,'IF' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT 'IG' AS \"ProdNo\"  ";
		sql += "       ,'IF' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT 'IH' AS \"ProdNo\"  ";
		sql += "       ,'IH' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT 'II' AS \"ProdNo\"  ";
		sql += "       ,'IH' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT '81' AS \"ProdNo\"  ";
		sql += "       ,'ZZ' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT '82' AS \"ProdNo\"  ";
		sql += "       ,'ZZ' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT '83' AS \"ProdNo\"  ";
		sql += "       ,'ZZ' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT TO_CHAR('340') AS \"ProdNo\" ";
		sql += "       ,'AA' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT TO_CHAR('F15') AS \"ProdNo\" ";
		sql += "       ,'ZZ' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT TO_CHAR('F16') AS \"ProdNo\" ";
		sql += "       ,'ZZ' AS \"ProdNoShow\" ";
		sql += " FROM DUAL ";
		sql += " ) ";
		sql += " SELECT int.\"ProdNoShow\" F0";
		sql += "       ,om.\"VisibleMonth\" F1";
		sql += "       ,SUM(bal.\"BalSum\") F2";
		sql += "       ,SUM(int.\"IntSum\") F3";
		sql += " FROM \"OutputMonths\" om  ";
		sql += " LEFT JOIN (SELECT spn.\"ProdNoShow\" ";
		sql += "                  ,om.\"YearMonth\" ";
		sql += "                  ,SUM(DECODE(ad.\"DbCr\", 'C', ad.\"TxAmt\", -ad.\"TxAmt\")) \"IntSum\" ";
		sql += "            FROM \"AcDetail\" ad ";
		sql += "            LEFT JOIN \"FacMain\" fm ON fm.\"CustNo\" = ad.\"CustNo\" ";
		sql += "                                    AND (fm.\"FacmNo\" = ad.\"FacmNo\" OR ad.\"AcctCode\" IN ('F15', 'F16'))";
		sql += "            LEFT JOIN \"SubjectProdNo\" spn ON (spn.\"ProdNo\" = fm.\"ProdNo\" OR spn.\"ProdNo\" = fm.\"AcctCode\" OR spn.\"ProdNo\" = ad.\"AcctCode\") ";
		sql += "                                           AND fm.\"AcctCode\" IN ('310','320','330','340') ";
		sql += "            LEFT JOIN \"OutputMonths\" om ON om.\"YearMonth\" = FLOOR(ad.\"RelDy\"/100) ";
		sql += "            WHERE ad.\"RelDy\" BETWEEN TO_NUMBER(:entYear||'0101') AND TO_NUMBER(:entYear||'1231') ";
		sql += "            AND spn.\"ProdNo\" is not null ";
		sql += "            AND ad.\"AcctCode\" IN ('IC1','IC2','IC3','IC4','IOP','IOV','F15','F16') ";
		sql += "            GROUP BY spn.\"ProdNoShow\" ";
		sql += "                    ,om.\"YearMonth\" ";
		sql += "           ) int ON int.\"YearMonth\" = om.\"YearMonth\" ";
		sql += " LEFT JOIN (SELECT spn.\"ProdNoShow\" ";
		sql += "                  ,om.\"YearMonth\" ";
		sql += "                  ,SUM(mlb.\"LoanBalance\") \"BalSum\" ";
		sql += "            FROM \"MonthlyLoanBal\" mlb ";
		sql += "            LEFT JOIN \"SubjectProdNo\" spn ON (spn.\"ProdNo\" = mlb.\"ProdNo\" OR spn.\"ProdNo\" = mlb.\"AcctCode\") ";
		sql += "                                           AND mlb.\"AcctCode\" IN ('310','320','330','340') ";
		sql += "            LEFT JOIN \"OutputMonths\" om ON om.\"YearMonth\" = mlb.\"YearMonth\" ";
		sql += "            WHERE mlb.\"YearMonth\" BETWEEN :entYear * 100 AND :entYearMonth ";
		sql += "            AND spn.\"ProdNo\" is not null ";
		sql += "            GROUP BY spn.\"ProdNoShow\" ";
		sql += "                    ,om.\"YearMonth\" ";
		sql += "           ) bal ON bal.\"YearMonth\" = om.\"YearMonth\" ";
		sql += "                AND om.\"YearMonth\" = om.\"VisibleMonth\" ";
		sql += "                AND bal.\"ProdNoShow\" = int.\"ProdNoShow\" ";
		sql += " WHERE int.\"ProdNoShow\" is not null ";
		sql += " GROUP BY om.\"VisibleMonth\" ";
		sql += "         ,int.\"ProdNoShow\" ";
		sql += " ORDER BY om.\"VisibleMonth\" ";
		sql += "         ,int.\"ProdNoShow\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("entYearMonth", entdy / 100);
		query.setParameter("entYear", entdy / 10000);

		return this.convertToMap(query);
	}

}