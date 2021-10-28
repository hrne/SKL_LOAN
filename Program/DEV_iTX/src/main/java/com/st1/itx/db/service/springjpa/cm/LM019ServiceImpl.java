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
public class LM019ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("lM013.findAll ");
		String sql = "SELECT A.\"AcNoCode\"";
		sql += "            ,C.\"AcNoItem\"";
		sql += "            ,A.\"AcDate\"";
		sql += "            ,SUM(CASE WHEN A.\"DbCr\" = 'C' ";
		sql += "                      THEN A.\"TxAmt\" "; 
		sql += "                 ELSE - A.\"TxAmt\" END";
		sql += "                 + CASE WHEN A.\"DbCr\" = 'C' ";
		sql += "                        THEN NVL(-TO_NUMBER(JSON_VALUE(A.\"JsonFields\", '$.StampTaxFreeAmt')), 0) ";
		sql += "                   ELSE NVL(TO_NUMBER(JSON_VALUE(A.\"JsonFields\", '$.StampTaxFreeAmt')), 0) END "; 
		sql += "                ) AS \"TxAmt\" ";
		sql += "      FROM \"AcDetail\" A";
		sql += "      LEFT JOIN \"CdAcCode\" C ON C.\"AcNoCode\" = A.\"AcNoCode\"";
		sql += "      WHERE TRUNC(A.\"AcDate\" / 100) = :entdy";
		sql += "        AND A.\"AcNoCode\" IN ('40241100000'";
		sql += "                              ,'40241200000'";
		sql += "                              ,'40241300000'";
		sql += "                              ,'40241400000'";
		sql += "                              ,'40241500000'";
		sql += "                              ,'40241601000'";
		sql += "                              ,'40241602000'";
		sql += "                              ,'40241603000')";
		sql += "      GROUP BY A.\"AcNoCode\"";
		sql += "              ,C.\"AcNoItem\"";
		sql += "              ,A.\"AcDate\"";
		sql += "              ,A.\"CustNo\"";
		sql += "              ,A.\"SlipNo\"";
		sql += "              ,A.\"DbCr\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", yearMonth);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll_1(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("lM013.findAll ");
		String sql = "SELECT A.\"AcNoCode\"";
		sql += "            ,C.\"AcNoItem\"";
		sql += "            ,A.\"AcDate\"";
		sql += "            ,A.\"TxAmt\"";
		sql += "            ,TRUNC(A.\"TxAmt\" * 0.004) AS \"Tax\"";
		sql += "      FROM (SELECT A.\"AcNoCode\"";
		sql += "                  ,A.\"AcDate\"";
		sql += "                  ,SUM(CASE WHEN A.\"DbCr\" = 'C' ";
		sql += "                            THEN A.\"TxAmt\" "; 
		sql += "                       ELSE - A.\"TxAmt\" END";
		sql += "                       + CASE WHEN A.\"DbCr\" = 'C' ";
		sql += "                              THEN NVL(-TO_NUMBER(JSON_VALUE(A.\"JsonFields\", '$.StampTaxFreeAmt')), 0) ";
		sql += "                         ELSE NVL(TO_NUMBER(JSON_VALUE(A.\"JsonFields\", '$.StampTaxFreeAmt')), 0) END "; 
		sql += "                      ) AS \"TxAmt\" ";
		sql += "            FROM \"AcDetail\" A";
		sql += "            WHERE A.\"AcNoCode\" IN ('40241100000'";
		sql += "                                    ,'40241200000'";
		sql += "                                    ,'40241300000'";
		sql += "                                    ,'40241400000'";
		sql += "                                    ,'40241500000'";
		sql += "                                    ,'40241601000'";
		sql += "                                    ,'40241602000'";
		sql += "                                    ,'40241603000')";
		sql += "              AND TRUNC(A.\"AcDate\" / 100) = :entdy";
		sql += "              AND A.\"DbCr\" = 'C'";
		sql += "            GROUP BY A.\"AcNoCode\" ";
		sql += "                    ,A.\"AcDate\" ";
		sql += "                    ,A.\"CustNo\" ";
		sql += "                    ,A.\"DbCr\" ";
		sql += "                    ,A.\"SlipNo\") A ";
		sql += "      LEFT JOIN \"CdAcCode\" C ON C.\"AcNoCode\" = A.\"AcNoCode\"";
		sql += "      WHERE A.\"TxAmt\" >= 250";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", yearMonth);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll_2(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("lM013.findAll ");
		int lyentdy = yearMonth;
		int lmentdy = 0;
		if (yearMonth % 100 == 1) {
			lmentdy = yearMonth - 89;
		} else {
			lmentdy = yearMonth - 1;
		}
		this.info("yearMonth = " + yearMonth + "    lmemtdy = " + lmentdy + "    lyentdy = " + lyentdy);
		String sql = "SELECT A.\"F1\" \"F1\"";
		sql += "            ,A.\"F2\" \"F2\"";
		sql += "            ,SUM(A.\"F3\") \"F3\"";
		sql += "            ,SUM(A.\"F4\") \"F4\"";
		sql += "            ,SUM(A.\"F5\") \"F5\"";
		sql += "            ,SUM(A.\"F6\") \"F6\"";
		sql += "            ,SUM(A.\"F7\") \"F7\"";
		sql += "      FROM (SELECT A.\"AcNoCode\" AS \"F1\"";
		sql += "                  ,C.\"AcNoItem\" AS \"F2\"";
		sql += "                  ,SUM(CASE WHEN A.\"DbCr\" = 'C' ";
		sql += "                            THEN A.\"TxAmt\" "; 
		sql += "                       ELSE - A.\"TxAmt\" END";
		sql += "                       + CASE WHEN A.\"DbCr\" = 'C' ";
		sql += "                              THEN NVL(-TO_NUMBER(JSON_VALUE(A.\"JsonFields\", '$.StampTaxFreeAmt')), 0) ";
		sql += "                         ELSE NVL(TO_NUMBER(JSON_VALUE(A.\"JsonFields\", '$.StampTaxFreeAmt')), 0) END "; 
		sql += "                      ) AS \"F3\" ";
		sql += "                  ,0 \"F4\"";
		sql += "                  ,0 AS \"F5\"";
		sql += "                  ,0 AS \"F6\"";
		sql += "                  ,0 AS \"F7\"";
		sql += "            FROM \"AcDetail\" A";
		sql += "            LEFT JOIN \"CdAcCode\" C ON C.\"AcNoCode\" = A.\"AcNoCode\"";
		sql += "            WHERE TRUNC(A.\"AcDate\" / 100) = :entdy";
		sql += "              AND A.\"AcNoCode\" IN ('40241100000'";
		sql += "                                    ,'40241200000'";
		sql += "                                    ,'40241300000'";
		sql += "                                    ,'40241400000'";
		sql += "                                    ,'40241500000'";
		sql += "                                    ,'40241601000'";
		sql += "                                    ,'40241602000'";
		sql += "                                    ,'40241603000')";
		sql += "            GROUP BY A.\"AcNoCode\" ";
		sql += "                    ,C.\"AcNoItem\" ";
		sql += "                    ,A.\"AcDate\" ";
		sql += "                    ,A.\"CustNo\" ";
		sql += "                    ,A.\"DbCr\" ";
		sql += "                    ,A.\"SlipNo\" ";
		sql += "            UNION ALL";
		sql += "            SELECT A.\"AcNoCode\" \"F1\"";
		sql += "                  ,C.\"AcNoItem\" \"F2\"";
		sql += "                  ,0 \"F3\"";
		sql += "                  ,A.\"TxAmt\" \"F4\"";
		sql += "                  ,TRUNC(A.\"TxAmt\" * 0.004) \"F5\"";
		sql += "                  ,0 AS \"F6\"";
		sql += "                  ,0 AS \"F7\"";
		sql += "            FROM (SELECT A.\"AcNoCode\"";
		sql += "                        ,A.\"AcDate\"";
		sql += "                  ,SUM(CASE WHEN A.\"DbCr\" = 'C' ";
		sql += "                            THEN A.\"TxAmt\" "; 
		sql += "                       ELSE - A.\"TxAmt\" END";
		sql += "                       + CASE WHEN A.\"DbCr\" = 'C' ";
		sql += "                              THEN NVL(-TO_NUMBER(JSON_VALUE(A.\"JsonFields\", '$.StampTaxFreeAmt')), 0) ";
		sql += "                         ELSE NVL(TO_NUMBER(JSON_VALUE(A.\"JsonFields\", '$.StampTaxFreeAmt')), 0) END "; 
		sql += "                      ) AS \"TxAmt\" ";
		sql += "                  FROM \"AcDetail\" A";
		sql += "                  WHERE A.\"AcNoCode\" IN ('40241100000'";
		sql += "                                          ,'40241200000'";
		sql += "                                          ,'40241300000'";
		sql += "                                          ,'40241400000'";
		sql += "                                          ,'40241500000'";
		sql += "                                          ,'40241601000'";
		sql += "                                          ,'40241602000'";
		sql += "                                          ,'40241603000')";
		sql += "                    AND TRUNC(A.\"AcDate\" / 100) = :entdy";
		sql += "                    AND A.\"DbCr\" = 'C'";
		sql += "                  GROUP BY A.\"AcNoCode\" ";
		sql += "                          ,A.\"AcDate\" ";
		sql += "                          ,A.\"CustNo\" ";
		sql += "                          ,A.\"DbCr\"   ";
		sql += "                          ,A.\"SlipNo\" ) A ";
		sql += "            LEFT JOIN \"CdAcCode\" C ON C.\"AcNoCode\" = A.\"AcNoCode\"";
		sql += "            WHERE A.\"TxAmt\" >= 250";
		sql += "            UNION ALL";
		sql += "            SELECT A.\"AcNoCode\" AS \"F1\"";
		sql += "                  ,C.\"AcNoItem\" AS \"F2\"";
		sql += "                  ,0 AS \"F3\"";
		sql += "                  ,0 AS \"F4\"";
		sql += "                  ,0 AS \"F5\"";
		sql += "                  ,SUM(CASE WHEN A.\"DbCr\" = 'C' ";
		sql += "                            THEN A.\"TxAmt\" "; 
		sql += "                       ELSE - A.\"TxAmt\" END";
		sql += "                       + CASE WHEN A.\"DbCr\" = 'C' ";
		sql += "                              THEN NVL(-TO_NUMBER(JSON_VALUE(A.\"JsonFields\", '$.StampTaxFreeAmt')), 0) ";
		sql += "                         ELSE NVL(TO_NUMBER(JSON_VALUE(A.\"JsonFields\", '$.StampTaxFreeAmt')), 0) END "; 
		sql += "                      ) AS \"F6\" ";
		sql += "                  ,0 AS \"F7\"";
		sql += "            FROM \"AcDetail\" A";
		sql += "            LEFT JOIN \"CdAcCode\" C ON C.\"AcNoCode\" = A.\"AcNoCode\"";
		sql += "            WHERE TRUNC(A.\"AcDate\" / 100) = :lyentdy";
		sql += "              AND A.\"AcNoCode\" IN ('40241100000'";
		sql += "                                    ,'40241200000'";
		sql += "                                    ,'40241300000'";
		sql += "                                    ,'40241400000'";
		sql += "                                    ,'40241500000'";
		sql += "                                    ,'40241601000'";
		sql += "                                    ,'40241602000'";
		sql += "                                    ,'40241603000')";
		sql += "            GROUP BY A.\"AcNoCode\" ";
		sql += "                    ,C.\"AcNoItem\" ";
		sql += "                    ,A.\"CustNo\" ";
		sql += "                    ,A.\"DbCr\" ";
		sql += "                    ,A.\"SlipNo\" ";
		sql += "            UNION ALL";
		sql += "            SELECT A.\"AcNoCode\" AS \"F1\"";
		sql += "                  ,C.\"AcNoItem\" AS \"F2\"";
		sql += "                  ,0 AS \"F3\"";
		sql += "                  ,0 AS \"F4\"";
		sql += "                  ,0 AS \"F5\"";
		sql += "                  ,0 AS \"F6\"";
		sql += "                  ,SUM(CASE WHEN A.\"DbCr\" = 'C' ";
		sql += "                            THEN A.\"TxAmt\" "; 
		sql += "                       ELSE - A.\"TxAmt\" END";
		sql += "                       + CASE WHEN A.\"DbCr\" = 'C' ";
		sql += "                              THEN NVL(-TO_NUMBER(JSON_VALUE(A.\"JsonFields\", '$.StampTaxFreeAmt')), 0) ";
		sql += "                         ELSE NVL(TO_NUMBER(JSON_VALUE(A.\"JsonFields\", '$.StampTaxFreeAmt')), 0) END "; 
		sql += "                      ) AS \"F7\" ";
		sql += "            FROM \"AcDetail\" A";
		sql += "            LEFT JOIN \"CdAcCode\" C ON C.\"AcNoCode\" = A.\"AcNoCode\"";
		sql += "            WHERE TRUNC(A.\"AcDate\" / 100) = :lmentdy";
		sql += "              AND A.\"AcNoCode\" IN ('40241100000'";
		sql += "                                    ,'40241200000'";
		sql += "                                    ,'40241300000'";
		sql += "                                    ,'40241400000'";
		sql += "                                    ,'40241500000'";
		sql += "                                    ,'40241601000'";
		sql += "                                    ,'40241602000'";
		sql += "                                    ,'40241603000')";
		sql += "            GROUP BY A.\"AcNoCode\" ";
		sql += "                    ,C.\"AcNoItem\" ";
		sql += "                    ,A.\"CustNo\" ";
		sql += "                    ,A.\"DbCr\" ";
		sql += "                    ,A.\"SlipNo\" ) A ";
		sql += "      GROUP BY A.\"F1\", A.\"F2\"";
		sql += "      ORDER BY A.\"F1\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", yearMonth);
		query.setParameter("lmentdy", lmentdy);
		query.setParameter("lyentdy", lyentdy);
		return this.convertToMap(query);
	}
}