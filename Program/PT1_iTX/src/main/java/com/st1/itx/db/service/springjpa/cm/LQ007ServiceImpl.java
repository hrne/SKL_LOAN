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
public class LQ007ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lQ007.findAll ");

		int entdy = (parse.stringToInteger(titaVo.getParam("ENTDY")) + 19110000);

//		int endY = entdy / 10000;
//		int endM = entdy / 100 % 100;

//		int begY = endY - 3;

		String sql = "";

		sql += " WITH \"SubjectProdNo\" AS ( ";
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
		sql += " SELECT int.\"ProdNoShow\" \"ProdNoShow\" ";
		sql += "       ,case ";
		sql += "	      when MOD(int.\"YearMonth\",100) IN (1,2,3) then trunc(int.\"YearMonth\" /100) * 100 + 3 ";
		sql += "	      when MOD(int.\"YearMonth\",100) IN (4,5,6) then trunc(int.\"YearMonth\" /100) * 100 + 6 ";
		sql += "	      when MOD(int.\"YearMonth\",100) IN (7,8,9) then trunc(int.\"YearMonth\" /100) * 100 + 9 ";
		sql += "	      when MOD(int.\"YearMonth\",100) IN (10,11,12) then trunc(int.\"YearMonth\" /100) * 100 + 12 ";
		sql += "	   end AS \"VisibleMonth\"";
		sql += "       ,SUM(nvl(bal.\"BalSum\",0)) \"BalSum\" "; // --餘額
		sql += "       ,SUM(nvl(int.\"IntSum\",0)) \"IntSum\" "; // --利收
		sql += " FROM (SELECT spn.\"ProdNoShow\" ";
		sql += "                  ,trunc(ad.\"RelDy\" / 100) AS \"YearMonth\" ";
		sql += "                  ,SUM(DECODE(ad.\"DbCr\", 'C', ad.\"TxAmt\", -ad.\"TxAmt\")) \"IntSum\" ";
		sql += "            FROM \"AcDetail\" ad ";
		sql += "            LEFT JOIN \"FacMain\" fm ON fm.\"CustNo\" = ad.\"CustNo\" ";
		sql += "                                    AND (fm.\"FacmNo\" = ad.\"FacmNo\" OR ad.\"AcctCode\" IN ('F15', 'F16'))";
		sql += "            LEFT JOIN \"SubjectProdNo\" spn ON (spn.\"ProdNo\" = fm.\"ProdNo\" OR spn.\"ProdNo\" = fm.\"AcctCode\" OR spn.\"ProdNo\" = ad.\"AcctCode\") ";
		sql += "                                           AND fm.\"AcctCode\" IN ('310','320','330','340') ";
		sql += "            WHERE trunc(ad.\"RelDy\"/100) BETWEEN (trunc(:entYearMonth/100)-3)*100+1 AND :entYearMonth ";
		sql += "            AND spn.\"ProdNo\" is not null ";
		sql += "            AND ad.\"AcctCode\" IN ('IC1','IC2','IC3','IC4','IOV','F15','F16') ";
		sql += "            GROUP BY spn.\"ProdNoShow\" ";
		sql += "                    ,trunc(ad.\"RelDy\" / 100)";
		sql += "           ) int ";
		sql += " LEFT JOIN (SELECT spn.\"ProdNoShow\" ";
		sql += "                  ,mlb.\"YearMonth\" ";
		sql += "                  ,SUM(mlb.\"LoanBalance\") \"BalSum\" ";
		sql += "            FROM \"MonthlyLoanBal\" mlb ";
		sql += "            LEFT JOIN \"SubjectProdNo\" spn ON (spn.\"ProdNo\" = mlb.\"ProdNo\" OR spn.\"ProdNo\" = mlb.\"AcctCode\") ";
		sql += "                                           AND mlb.\"AcctCode\" IN ('310','320','330','340') ";
		sql += "            WHERE mlb.\"YearMonth\" BETWEEN (trunc(:entYearMonth/100)-3) * 100 AND :entYearMonth ";
		sql += "            AND spn.\"ProdNo\" is not null ";
		sql += "            GROUP BY spn.\"ProdNoShow\" ";
		sql += "                    ,mlb.\"YearMonth\" ";
		sql += "           ) bal ON bal.\"YearMonth\" = int.\"YearMonth\" ";
		sql += "                AND bal.\"ProdNoShow\" = int.\"ProdNoShow\" ";
		sql += " WHERE int.\"ProdNoShow\" is not null ";
		sql += " GROUP BY case ";
		sql += "	        when MOD(int.\"YearMonth\",100) IN (1,2,3) then trunc(int.\"YearMonth\" /100) * 100 + 3 ";
		sql += "	        when MOD(int.\"YearMonth\",100) IN (4,5,6) then trunc(int.\"YearMonth\" /100) * 100 + 6 ";
		sql += "	        when MOD(int.\"YearMonth\",100) IN (7,8,9) then trunc(int.\"YearMonth\" /100) * 100 + 9 ";
		sql += "	        when MOD(int.\"YearMonth\",100) IN (10,11,12) then trunc(int.\"YearMonth\" /100) * 100 + 12 ";
		sql += "	     end ";
		sql += "         ,int.\"ProdNoShow\" ";
		sql += " ORDER BY \"VisibleMonth\" ";
		sql += "         ,\"ProdNoShow\" ";

//		// 副query: 所有需要輸出的工作月/月份 - 同年一月到當月
//		sql += " WITH \"OutputMonths\" AS ( ";
//		for (int y = begY; y <= endY; y++) {
//
//			for (int m = 1; m <= 12; m++) {
//				int visibleMonth = 0;
//				if (m <= 3) {
//					visibleMonth = y * 100 + 3;
//				} else if (m <= 6) {
//					visibleMonth = y * 100 + 6;
//				} else if (m <= 9) {
//					visibleMonth = y * 100 + 9;
//				} else {
//					visibleMonth = y * 100 + 12;
//				}
//				sql += "	SELECT " + (begY * 100 + m) + "AS \"YearMonth\"";
//				sql += "	       " + visibleMonth + " AS \"VisibleMonth\"";
//				sql += "	FROM DUAL";
//				sql += "	";
//
//				if (y == endY && m == endM) {
//					break;
//				}
//				sql += "	UNION";
//
//			}
//		}
//		sql += " ), ";
//
//		// 副query: ProdNo 對照表
//		sql += " \"SubjectProdNo\" AS ( ";
//		sql += "  ";
//		sql += " SELECT 'IA' AS \"ProdNo\"  ";
//		sql += "       ,'IA' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT 'IB' AS \"ProdNo\"  ";
//		sql += "       ,'IB' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT 'IC' AS \"ProdNo\"  ";
//		sql += "       ,'IC' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT 'ID' AS \"ProdNo\"  ";
//		sql += "       ,'ID' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT 'IE' AS \"ProdNo\"  ";
//		sql += "       ,'ID' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT 'IF' AS \"ProdNo\"  ";
//		sql += "       ,'IF' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT 'IG' AS \"ProdNo\"  ";
//		sql += "       ,'IF' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT 'IH' AS \"ProdNo\"  ";
//		sql += "       ,'IH' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT 'II' AS \"ProdNo\"  ";
//		sql += "       ,'IH' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT '81' AS \"ProdNo\"  ";
//		sql += "       ,'ZZ' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT '82' AS \"ProdNo\"  ";
//		sql += "       ,'ZZ' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT '83' AS \"ProdNo\"  ";
//		sql += "       ,'ZZ' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT TO_CHAR('340') AS \"ProdNo\" ";
//		sql += "       ,'AA' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT TO_CHAR('F15') AS \"ProdNo\" ";
//		sql += "       ,'ZZ' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " UNION ALL ";
//		sql += " SELECT TO_CHAR('F16') AS \"ProdNo\" ";
//		sql += "       ,'ZZ' AS \"ProdNoShow\" ";
//		sql += " FROM DUAL ";
//		sql += " ) ";
//
//		// 主query
//		sql += " SELECT int.\"ProdNoShow\" \"ProdNoShow\" "; // 輸出顯示的商品名稱
//		sql += "       ,om.\"VisibleMonth\" \"VisibleMonth\" "; // 輸出顯示的月份 (季末月 / 當月)
//		sql += "       ,SUM(bal.\"BalSum\") \"BalSum\" "; // 餘額
//		sql += "       ,SUM(int.\"IntSum\") \"IntSum\" "; // 利收
//		sql += " FROM \"OutputMonths\" om  ";
//
//		// 利息
//		// 串月份時用 YearMonth,
//		// 最後再以 VisibleMonth group 成季
//		sql += " LEFT JOIN (SELECT spn.\"ProdNoShow\" ";
//		sql += "                  ,om.\"YearMonth\" ";
//		sql += "                  ,SUM(DECODE(ad.\"DbCr\", 'C', ad.\"TxAmt\", -ad.\"TxAmt\")) \"IntSum\" ";
//		sql += "            FROM \"AcDetail\" ad ";
//		sql += "            LEFT JOIN \"FacMain\" fm ON fm.\"CustNo\" = ad.\"CustNo\" ";
//		sql += "                                    AND (fm.\"FacmNo\" = ad.\"FacmNo\" OR ad.\"AcctCode\" IN ('F15', 'F16'))";
//		sql += "            LEFT JOIN \"SubjectProdNo\" spn ON (spn.\"ProdNo\" = fm.\"ProdNo\" OR spn.\"ProdNo\" = fm.\"AcctCode\" OR spn.\"ProdNo\" = ad.\"AcctCode\") ";
//		sql += "                                           AND fm.\"AcctCode\" IN ('310','320','330','340') ";
//		sql += "            LEFT JOIN \"OutputMonths\" om ON om.\"YearMonth\" = FLOOR(ad.\"RelDy\"/100) ";
//		sql += "            WHERE trunc(ad.\"RelDy\"/100) BETWEEN (trunc(:entYearMonth/100)-3)*100+1 AND :entYearMonth ";
//		sql += "            AND spn.\"ProdNo\" is not null ";
//		sql += "            AND ad.\"AcctCode\" IN ('IC1','IC2','IC3','IC4','IOV','F15','F16') ";
//		sql += "            GROUP BY spn.\"ProdNoShow\" ";
//		sql += "                    ,om.\"YearMonth\" ";
//		sql += "           ) int ON int.\"YearMonth\" = om.\"YearMonth\" ";
//
//		// 餘額
//		// join時利用om.YearMonth = om.VisibleMonth
//		// 確保餘額是出單一個月而非多個月合起來的餘額
//		sql += " LEFT JOIN (SELECT spn.\"ProdNoShow\" ";
//		sql += "                  ,om.\"YearMonth\" ";
//		sql += "                  ,SUM(mlb.\"LoanBalance\") \"BalSum\" ";
//		sql += "            FROM \"MonthlyLoanBal\" mlb ";
//		sql += "            LEFT JOIN \"SubjectProdNo\" spn ON (spn.\"ProdNo\" = mlb.\"ProdNo\" OR spn.\"ProdNo\" = mlb.\"AcctCode\") ";
//		sql += "                                           AND mlb.\"AcctCode\" IN ('310','320','330','340') ";
//		sql += "            LEFT JOIN \"OutputMonths\" om ON om.\"YearMonth\" = mlb.\"YearMonth\" ";
//		sql += "            WHERE mlb.\"YearMonth\" BETWEEN (trunc(:entYearMonth/100)-3) * 100 AND :entYearMonth ";
//		sql += "            AND spn.\"ProdNo\" is not null ";
//		sql += "            GROUP BY spn.\"ProdNoShow\" ";
//		sql += "                    ,om.\"YearMonth\" ";
//		sql += "           ) bal ON bal.\"YearMonth\" = om.\"YearMonth\" ";
//		sql += "                AND om.\"YearMonth\" = om.\"VisibleMonth\" ";
//		sql += "                AND bal.\"ProdNoShow\" = int.\"ProdNoShow\" ";
//		sql += " WHERE int.\"ProdNoShow\" is not null ";
//		sql += " GROUP BY om.\"VisibleMonth\" ";
//		sql += "         ,int.\"ProdNoShow\" ";
//		sql += " ORDER BY \"VisibleMonth\" ";
//		sql += "         ,\"ProdNoShow\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("entYearMonth", entdy / 100);

		return this.convertToMap(query);
	}

}