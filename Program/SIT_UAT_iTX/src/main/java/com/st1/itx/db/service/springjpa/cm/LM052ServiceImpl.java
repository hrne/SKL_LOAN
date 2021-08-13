package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
/* 逾期放款明細 */
public class LM052ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM052ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String yymm = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 100);

		logger.info("lM052.findAll yymm=" + yymm);
		String sql = "SELECT M.\"CustNo\"            F0";
		sql += "            ,M.\"FacmNo\"            F1";
		sql += "            ,M.\"BormNo\"            F2";
		sql += "            ,NVL(A.\"AcctSource\",' ')  F3";
		sql += "            ,C.\"CustId\"            F4";
		sql += "            ,C.\"CustName\"          F5";
		sql += "            ,M.\"AcctCode\"          F6";
		sql += "            ,MF.\"PrevIntDate\"      F7";
		sql += "            ,MF.\"OvduDate\"         F8";
		sql += "            ,F.\"UsageCode\"         F9";
		sql += "            ,M.\"LoanBalance\"       F10";
		sql += "            ,M.\"CityCode\"          F11";
		sql += "            ,M.\"ProdNo\"            F12";
		sql += "            ,F.\"FirstDrawdownDate\" F13";
		sql += "            ,CASE WHEN M.\"AcctCode\" = '340' THEN '*'";
		sql += "                  WHEN M.\"ProdNo\" LIKE 'I%' THEN '*'";
		sql += "                  WHEN M.\"ProdNo\" LIKE '8%' THEN '*'";
		sql += "             ELSE ' ' END            F14";
		sql += "      FROM \"MonthlyLoanBal\" M";
		sql += "      LEFT JOIN \"CdAcBook\" A ON A.\"AcSubBookCode\" = M.\"AcBookCode\"";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\"     = M.\"CustNo\"";
		sql += "      LEFT JOIN \"MonthlyFacBal\" MF ON MF.\"YearMonth\" = :yymm";
		sql += "                                    AND MF.\"CustNo\"    =  M.\"CustNo\"";
		sql += "                                    AND MF.\"FacmNo\"    =  M.\"FacmNo\"";
		sql += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                             AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "      WHERE M.\"YearMonth\" = :yymm";
		sql += "        AND M.\"LoanBalance\" > 0";
		sql += "      ORDER BY M.\"CustNo\"";
		sql += "              ,M.\"FacmNo\"";
		sql += "              ,M.\"BormNo\"";
		
		logger.info("sql=" + sql);
		logger.info("本月放款資產品質分類"+
		"ELECT  M.\"AssetClass\", NVL(M.\"AcBookCode\", '000')\r\n" + 
		"        , SUM(M.\"PrinBalance\")\r\n" + 
		" FROM  \"MonthlyFacBal\" M\r\n" + 
		"WHERE  M.\"YearMonth\"   =  本月年月\r\n" + 
		" AND   M.\"Status\"      IN (0, 2, 6)  \r\n" + 
		"GROUP  BY M.\"AssetClass\", NVL(M.\"AcSubBookCode\", '000')\r\n" + 
		"ORDER  BY M.\"AssetClass\", NVL(M.\"AcSubBookCode\", '000')\r\n" + 
		"\r\n" + 
		"M.\"AssetClass\" = 11=一之1,   12=一之2,\r\n" + 
		"21=二之1,   22=二之2,   23=二之3,\r\n" + 
		"3=三  \r\n" + 
		"4=四   \r\n" + 
		"5=五  \r\n" + 
		"M.\"AcSubBookCode\" = 00A,201\r\n" + 
		""
);
		
		logger.info("上月放款資產品質分類合計"+
		"SELECT  M.\"AssetClass\", SUM(M.\"PrinBalance\")\r\n" + 
		" FROM  \"MonthlyFacBal\" M\r\n" + 
		"WHERE  M.\"YearMonth\"   =  上月年月\r\n" + 
		" AND   M.\"Status\"      IN (0, 2, 6)  \r\n" + 
		"GROUP  BY M.\"AssetClass\" \r\n" + 
		"ORDER  BY M.\"AssetClass\"\r\n" + 
		""
		);
		
		logger.info("本月二之2分析表"+
		"SELECT M.\"Term\", M.\"FacAcctCode\", SUM(M.\"PrinBalance\")  \r\n" + 
		" FROM (SELECT  CASE WHEN M.\"OvduTerm\" = 1 THEN 1\r\n" + 
		"                    WHEN M.\"OvduTerm\" IN (2, 3) THEN 2\r\n" + 
		"                    ELSE 2 END \"Term\"\r\n" + 
		"             , M.\"FacAcctCode\", M.\"PrinBalance\" \r\n" + 
		"        FROM  \"MonthlyFacBal\" M\r\n" + 
		"       WHERE  M.\"YearMonth\"   =  上月年月\r\n" + 
		"        AND   M.\"Status\"      IN (0, 2, 6)  \r\n" + 
		"        AND   M.\"AssetClass\"  =  '22'\r\n" + 
		"      ) M\r\n" + 
		" GROUP  BY M.\"Term\", M.\"FacAcctCode\"\r\n" + 
		" ORDER  BY M.\"Term\", M.\"FacAcctCode\"\r\n" + 
		"\r\n" + 
		"M.\"Term\"= 1. 逾1期\r\n" + 
		"          2. 逾2-3期\r\n" + 
		"          3. 逾4-6期\r\n" + 
		"\r\n" + 
		"M.\"FacAcctCode\" = 310: 短期擔保放款 \r\n" + 
		"320: 中期擔保放款\r\n" + 
		"330: 長期擔保放款\r\n" + 
		""
		);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yymm);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll_1(TitaVo titaVo) throws Exception {

		String yymm = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 100);

		logger.info("lM052.findAll_1 yymm=" + yymm);
		String sql = "SELECT M.\"CustNo\" F0";
		sql += "            ,C.\"CustId\" F1";
		sql += "            ,M.\"FacmNo\" F2";
		sql += "            ,F.\"ApplNo\" F3";
		sql += "            ,M.\"BormNo\" F4";
		sql += "            ,F.\"FirstDrawdownDate\" F5";
		sql += "            ,M.\"ProdNo\" F6";
		sql += "            ,M.\"ClCode2\" F7";
		sql += "      FROM  \"MonthlyLoanBal\" M";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "      LEFT JOIN \"FacMain\" F  ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                              AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                  WHERE M.\"YearMonth\" = :yymm";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yymm);
		return this.convertToMap(query.getResultList());
	}

}