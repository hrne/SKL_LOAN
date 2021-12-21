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
public class LD003ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LD003ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		logger.info("LD003.findAll ");
		int entdy = titaVo.getEntDyI() + 19110000;

//		目前AcSubBookCode只有分一般帳戶(00A)和利變A(201)?
		String sql = " ";
		sql += " SELECT S.\"ColCount\"";
		sql += "       ,SUM(S.\"Counts\")      AS \"Counts\"";
		sql += "       ,SUM(S.\"LoanBalance\") AS \"Amt\"";
		sql += " FROM (  SELECT CASE";
		sql += "                  WHEN D.\"AcctCode\" = '310' AND A.\"AcSubBookCode\" = '00A' THEN 1";
		sql += "                  WHEN D.\"AcctCode\" = '310' AND A.\"AcSubBookCode\" = '201' THEN 2";
		sql += "                  WHEN D.\"AcctCode\" = '320' AND A.\"AcSubBookCode\" = '00A' THEN 5";
		sql += "                  WHEN D.\"AcctCode\" = '320' AND A.\"AcSubBookCode\" = '201' THEN 6";
		sql += "                  WHEN D.\"AcctCode\" = '330' AND A.\"AcSubBookCode\" = '00A' THEN 9";
		sql += "                  WHEN D.\"AcctCode\" = '330' AND A.\"AcSubBookCode\" = '201' THEN 10";
		sql += "                  WHEN D.\"AcctCode\" = '340' AND A.\"AcSubBookCode\" = '00A' THEN 13";
		sql += "                  WHEN D.\"AcctCode\" = '340' AND A.\"AcSubBookCode\" = '201' THEN 14";
		sql += "                ELSE 99 END AS \"ColCount\"";
		sql += "               ,A.\"Count\" AS \"Counts\"";
		sql += "               ,D.\"AcctCode\"";
		sql += "               ,A.\"AcSubBookCode\"";
		sql += "               ,D.\"LoanBalance\"";
		sql += "         FROM ( SELECT D.\"AcctCode\"";
		sql += "                      ,D.\"FacAcctCode\"";
		sql += "                      ,D.\"CustNo\"";
		sql += "                      ,D.\"FacmNo\"";
		sql += "                      ,SUM(D.\"LoanBalance\") AS \"LoanBalance\"";
		sql += "                FROM \"DailyLoanBal\" D ";
		sql += "                WHERE D.\"LoanBalance\" > 0";
		sql += "                  AND D.\"DataDate\" = :entdy";
		sql += "                GROUP BY D.\"AcctCode\"";
		sql += "                        ,D.\"FacAcctCode\"";
		sql += "                        ,D.\"CustNo\"";
		sql += "                        ,D.\"FacmNo\"";
		sql += "              ) D";
		sql += "         LEFT JOIN ( SELECT A0.\"AcctCode\"";
		sql += "                           ,A0.\"CustNo\"";
		sql += "                           ,A0.\"FacmNo\"";
		sql += "                           ,A0.\"AcSubBookCode\"";
		sql += "                           ,COUNT(*) AS \"Count\"";
		sql += "                     FROM \"DailyLoanBal\" D0";
		sql += "                     LEFT JOIN \"AcReceivable\" A0 ON A0.\"AcctCode\" = D0.\"AcctCode\"";
		sql += "                                                AND A0.\"CustNo\"   = D0.\"CustNo\"";
		sql += "                                                AND A0.\"FacmNo\"   = D0.\"FacmNo\"";
		sql += "                                                AND SUBSTR(A0.\"RvNo\",0,3) = LPAD(D0.\"BormNo\",3,'0')";
		sql += "                     WHERE D0.\"LoanBalance\" > 0";
		sql += "                       AND D0.\"DataDate\" = :entdy";
		sql += "                       AND A0.\"AcctCode\" IN ('310','320','330','340')";
		sql += "                     GROUP BY  A0.\"AcctCode\"";
		sql += "                              ,A0.\"CustNo\"";
		sql += "                              ,A0.\"FacmNo\"";
		sql += "                              ,A0.\"AcSubBookCode\"";
		sql += "                   ) A ON A.\"AcctCode\" = D.\"AcctCode\"";
		sql += "                      AND A.\"CustNo\"   = D.\"CustNo\"";
		sql += "                      AND A.\"FacmNo\"   = D.\"FacmNo\"";
		sql += "         WHERE D.\"LoanBalance\" > 0";
		for (int i = 1; i <= 16; i++) {
			sql += "        UNION ALL";
			sql += "        SELECT " + i + ",0,'0','0',0 FROM DUAL";
		}
		sql += ") S ";
		sql += " WHERE S.\"ColCount\" <> 99";
		sql += " GROUP BY S.\"ColCount\"";
		sql += " ORDER BY S.\"ColCount\"";

		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);

		query.setParameter("entdy", entdy);

		return this.convertToMap(query.getResultList());
	}
}

//String sql = "SELECT F1"; // 開頭查詢所有表格
//sql += "            ,F2";
//sql += "            ,F4";
//sql += "            ,SUM(F5) AS F5";
//sql += "            ,SUM(F6) AS F6";
///*
// * join表格 A = AcReceivable B = CdAcBook D = CdCode E = CdAcCode NVL =
// * 若是A不是null則回傳A，否則回傳B
// */
//sql += "      FROM ( SELECT A.\"AcctCode\" AS F1";
//sql += "                   ,E.\"AcctItem\" AS F2";
//sql += "                   ,NVL(D.\"Code\",0) AS F3";
//sql += "                   ,DECODE(D.\"Item\", '利變年金', D.\"Item\", '一般帳戶') AS F4";
//sql += "                   , 1 AS F5";
//sql += "                   , A.\"AcBal\" AS F6";
//sql += "             FROM \"AcReceivable\" A"; // 左邊
//sql += "             LEFT JOIN \"CdAcBook\" B ON B.\"AcSubBookCode\" =  A.\"AcSubBookCode\"";// B的AcSubBookCode連結A的AcSubBookCode
//sql += "             LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'AcSubBookCode' ";
//sql += "                                   AND D.\"Code\" = B.\"AcSubBookCode\"";
//sql += "             LEFT JOIN \"CdAcCode\" E ON E.\"AcctCode\" = A.\"AcctCode\"";// A、B、D、E 藉由
//																					// AcSubBookCode(PK)接連一起
//sql += "             WHERE A.\"AcctCode\" > '300'";
//sql += "               AND A.\"AcctCode\" < '390' ";
//sql += "               AND A.\"ClsFlag\" = 0 ";
//sql += "             UNION ALL";// 將sql所查詢到的結果合併起來
//sql += "             SELECT A.\"AcctCode\" AS F1";
//sql += "                    ,A.\"AcctItem\" AS F2";
//sql += "                    ,D.\"Code\" AS F3";
//sql += "                    ,DECODE(D.\"Item\", '利變年金', D.\"Item\", '一般帳戶') AS F4";
//sql += "                    ,0 AS F5";
//sql += "                    ,0 AS F6";
//sql += "	         FROM \"CdAcCode\" A ";
//sql += "             CROSS JOIN \"CdAcBook\" B";// CROSS不指定任何條件，將兩個資料表最有相關聯的組合排列出來
//sql += "             LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'AcSubBookCode' ";
//sql += "               AND D.\"Code\" = B.\"AcSubBookCode\" ";
//sql += "             WHERE A.\"AcctCode\" > '300'";
//sql += "               AND A.\"AcctCode\" < '390' ";
//sql += "             UNION ALL";
//sql += "             SELECT A.\"AcctCode\" AS F1";
//sql += "                   ,A.\"AcctItem\" AS F2";
//sql += "                   ,CAST ( 0 AS VARCHAR2(10)) AS F3";// CAST將這些資料型態轉換成另一種資料型態
//sql += "                   ,CAST ( '一般帳戶' AS NVARCHAR2(50)) AS F4";
//sql += "                   ,0 AS F5";
//sql += "                   ,0 AS F6";
//sql += "             FROM \"CdAcCode\" A ";
//sql += "             WHERE A.\"AcctCode\" > '300'";
//sql += "               AND A.\"AcctCode\" < '390')";	
//sql += "             GROUP BY F1,F2,F4";// 查詢某一列(或多列)進行分組，相同值為一組
//sql += "             ORDER BY F1";// 查詢某一列(或多列<從左到右，依次排序，前面的屬性優先度高>)ASC:空位元組(NULL)排序為最後顯示 DESC:空位元組(NULL)排序為最上頭