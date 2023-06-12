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
public class LM013ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public enum EntCodeCondition {
		Natural("0"), Enterprise("1"), All("%");

		private String value;

		EntCodeCondition(String value) {
			this.value = value;
		}
	}

	public enum IsRelsCondition {
		Yes("Y"), No("N"), All("%");

		private String value;

		IsRelsCondition(String value) {
			this.value = value;
		}
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, EntCodeCondition entCodeCondition,
			IsRelsCondition isRelsCondition) throws Exception {
		this.info("lM013.findAll ");

		int entdy = (parse.stringToInteger(titaVo.getParam("inputDate")) + 19110000);

		String sql = " ";
		sql += "   WITH rawData AS (";
		sql += "        SELECT FSL.\"CustNo\"";
		sql += "             , FSL.\"FacmNo\"";
		sql += "             , FSL.\"MainApplNo\"";
		sql += "             , FSL.\"KeyinSeq\"";
		sql += "             , FSL.\"LineAmt\" AS \"LimitLineAmt\"";
		sql += "             , FM.\"UtilBal\"";
		sql += "        FROM \"FacShareLimit\" FSL";
		sql += "        LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = FSL.\"CustNo\"";
		sql += "                              AND FM.\"FacmNo\" = FSL.\"FacmNo\"";
		sql += "    )";
		sql += "    , maxSeqData AS (";
		sql += "        SELECT \"MainApplNo\"";
		sql += "             , MAX(\"KeyinSeq\") AS \"MaxSeq\"";
		sql += "        FROM rawData";
		sql += "        GROUP BY \"MainApplNo\"";
		sql += "    )";
		sql += "    , lastSeqData AS (";
		sql += "        SELECT r1.\"MainApplNo\"";
		sql += "             , r1.\"CustNo\"";
		sql += "             , r1.\"FacmNo\"";
		sql += "             , r1.\"LimitLineAmt\" - SUM(r2.\"UtilBal\") AS \"LimitLineAmt\"";
		sql += "        FROM rawData r1";
		sql += "        LEFT JOIN rawData r2 ON r2.\"MainApplNo\" = r1.\"MainApplNo\"";
		sql += "                            AND r2.\"KeyinSeq\" < r1.\"KeyinSeq\"";
		sql += "        LEFT JOIN maxSeqData m ON m.\"MainApplNo\" = r1.\"MainApplNo\"";
		sql += "        WHERE r1.\"KeyinSeq\" = m.\"MaxSeq\" ";
		sql += "        GROUP BY r1.\"MainApplNo\"";
		sql += "               , r1.\"CustNo\"";
		sql += "               , r1.\"FacmNo\"";
		sql += "               , r1.\"LimitLineAmt\"";
		sql += "    )";
		sql += "    , shareFacData AS (";
		sql += "        SELECT r.\"CustNo\"";
		sql += "             , r.\"FacmNo\"";
		sql += "             , MAX(NVL(l.\"LimitLineAmt\",r.\"UtilBal\")) AS \"ShareLineAmt\" ";
		sql += "        FROM rawData r";
		sql += "        LEFT JOIN lastSeqData l ON l.\"MainApplNo\" = r.\"MainApplNo\"";
		sql += "                               AND l.\"CustNo\" = r.\"CustNo\"";
		sql += "                               AND l.\"FacmNo\" = r.\"FacmNo\"";
		sql += "        GROUP BY r.\"CustNo\"";
		sql += "               , r.\"FacmNo\"";
		sql += "    )";
		sql += "    , ProjectLoan AS (";
		sql += "        SELECT JSON_VALUE(\"JsonFields\",'$.\"340LoanBal\"') +";
		sql += "        	   JSON_VALUE(\"JsonFields\",'$.\"921LoanBal\"') +";
		sql += "        	   JSON_VALUE(\"JsonFields\",'$.\"IALoanBal\"') +";
		sql += "        	   JSON_VALUE(\"JsonFields\",'$.\"IBLoanBal\"') +";
		sql += "        	   JSON_VALUE(\"JsonFields\",'$.\"ICLoanBal\"') +";
		sql += "        	   JSON_VALUE(\"JsonFields\",'$.\"IDLoanBal\"') +";
		sql += "        	   JSON_VALUE(\"JsonFields\",'$.\"IELoanBal\"') +";
		sql += "        	   JSON_VALUE(\"JsonFields\",'$.\"IFLoanBal\"') +";
		sql += "        	   JSON_VALUE(\"JsonFields\",'$.\"IGLoanBal\"') +";
		sql += "        	   JSON_VALUE(\"JsonFields\",'$.\"IHLoanBal\"') +";
		sql += "        	   JSON_VALUE(\"JsonFields\",'$.\"IILoanBal\"') AS \"LoanBal\"";
		sql += "        FROM \"CdComm\"";
		sql += "        WHERE \"CdType\" = '02' ";
		sql += "          AND \"CdItem\" = '01' ";
		sql += "          AND \"Enable\" = 'Y' ";
		sql += "          AND TRUNC(\"EffectDate\" / 100) = :YearMonth ";
		sql += "    )";
		sql += "  , TotalData AS (  ";
		sql += "      		SELECT DECODE(C.\"EntCode\",'1','1','0') AS \"EntCode\"  ";
		sql += "                   ,R.\"IsRels\" AS \"IsRels\"  ";
		sql += "                   ,LPAD(TO_CHAR(D.\"CustNo\"),7,'0') AS \"CustNo\"  ";
		sql += "                   ,LPAD(D.\"FacmNo\", 3, '0') AS \"FacmNo\"  ";
		sql += "                   ,C.\"CustId\" AS \"CustId\"  ";
		sql += "                   ,\"Fn_ParseEOL\"(C.\"CustName\", 0) AS \"CustName\"  ";
		sql += "                   ,CASE WHEN D.\"ProdNo\" LIKE 'I%' OR D.\"ProdNo\" IN ('81','82','83') OR D.\"AcctCode\" = '340'  ";
		sql += "                         THEN 0  ";
		sql += "                    ELSE CASE CF.\"ClCode1\"  ";
		sql += "                           WHEN 1  ";
		sql += "                           THEN 2  ";
		sql += "                           WHEN 3  ";
		sql += "                           THEN 4  ";
		sql += "                         ELSE CF.\"ClCode1\" END   ";
		sql += "                    END AS \"ClCode1\"  ";
		sql += "                   ,NVL(NVL(sfd.\"ShareLineAmt\",F.\"LineAmt\"),0) AS \"LineAmt\"  ";
		sql += "                   ,SUM(NVL(IIM.\"BookValue\", D.\"LoanBalance\")) AS \"BookValue\"  ";
		sql += "             FROM \"DailyLoanBal\" D  ";
		sql += "             LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\"  ";
		sql += "             LEFT JOIN ( SELECT cm.\"CustNo\"  ";
		sql += "                               ,CASE COUNT(s.\"StaffId\")";
		sql += "                                  WHEN 0  ";
		sql += "                                  THEN 'N'  ";
		sql += "                                ELSE 'Y' END AS \"IsRels\"  ";
		sql += "                         FROM \"CustMain\" cm  ";
		sql += "                         LEFT JOIN \"StakeholdersStaff\" s ON s.\"StaffId\" = cm.\"CustId\"  ";
		sql += "                         GROUP BY cm.\"CustNo\"  ";
		sql += "                       ) R ON R.\"CustNo\" = D.\"CustNo\"  ";
		sql += "             LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = D.\"CustNo\"  ";
		sql += "                                    AND F.\"FacmNo\" = D.\"FacmNo\"  ";
		sql += "             LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = D.\"CustNo\"  ";
		sql += "                                   AND CF.\"FacmNo\" = D.\"FacmNo\"  ";
		sql += "                                   AND CF.\"MainFlag\" = 'Y'  ";
		sql += "             LEFT JOIN ( SELECT \"CustNo\"  ";
		sql += "                               ,\"FacmNo\"  ";
		sql += "                               ,\"BormNo\"  ";
		sql += "                               ,ROW_NUMBER() OVER (PARTITION BY \"CustNo\"  ";
		sql += "                                                               ,\"FacmNo\"  ";
		sql += "                                                               ,\"BormNo\"  ";
		sql += "                                                   ORDER BY \"YearMonth\" DESC  ";
		sql += "                                                  ) \"Seq\"  ";
		sql += "                               ,\"BookValue\" AS \"BookValue\"  ";
		sql += "                         FROM \"Ias39IntMethod\"  ";
		sql += "                         WHERE \"YearMonth\" = :YearMonth ";
		sql += "                       ) IIM ON IIM.\"CustNo\" = D.\"CustNo\"  ";
		sql += "                            AND IIM.\"FacmNo\" = D.\"FacmNo\"  ";
		sql += "                            AND IIM.\"BormNo\" = D.\"BormNo\"  ";
		sql += "                            AND NVL(IIM.\"Seq\", 0) = 1  ";
		sql += "             LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = D.\"CustNo\"  ";
		sql += "                                          AND LBM.\"FacmNo\" = D.\"FacmNo\"  ";
		sql += "                                          AND LBM.\"BormNo\" = D.\"BormNo\"  ";
		sql += "             LEFT JOIN shareFacData sfd ON sfd.\"CustNo\" = D.\"CustNo\"  ";
		sql += "                                       AND sfd.\"FacmNo\" = D.\"FacmNo\"  ";
		sql += "             WHERE D.\"MonthEndYm\" = :YearMonth  ";
		sql += "               AND F.\"FirstDrawdownDate\" <= :entdy  ";
		sql += "               AND LBM.\"Status\" IN (0,4)  ";
		sql += "             GROUP BY DECODE(C.\"EntCode\", '1', '1', '0')  ";
		sql += "                     ,R.\"IsRels\"  ";
		sql += "                     ,D.\"CustNo\"  ";
		sql += "                     ,LPAD(D.\"FacmNo\", 3, '0')  ";
		sql += "                     ,C.\"CustId\"  ";
		sql += "                     ,C.\"CustName\"  ";
		sql += "                     ,CASE WHEN D.\"ProdNo\" LIKE 'I%' OR D.\"ProdNo\" IN ('81','82','83') OR D.\"AcctCode\" = '340'  ";
		sql += "                           THEN 0  ";
		sql += "                      ELSE CASE CF.\"ClCode1\"  ";
		sql += "                             WHEN 1  ";
		sql += "                             THEN 2  ";
		sql += "                             WHEN 3  ";
		sql += "                             THEN 4  ";
		sql += "                           ELSE CF.\"ClCode1\" END  ";
		sql += "                      END  ";
		sql += "                     ,NVL(NVL(sfd.\"ShareLineAmt\",F.\"LineAmt\"),0)  ";
		sql += "      		 ORDER BY TO_NUMBER(\"CustNo\") ASC ";
		sql += "              	 	 ,TO_NUMBER(\"FacmNo\") ASC ";
		sql += "              		 ,\"EntCode\" ";
		sql += "              		 ,\"IsRels\" ";
		sql += "  ),  ";
		sql += "    ";
		sql += "  TotalData_LineTotal AS (  ";
		sql += "      SELECT t1.*  ";
		sql += "            ,t2.\"LineTotal\"  ";
		sql += "      FROM TotalData t1  ";
		sql += "      LEFT JOIN (SELECT \"CustNo\"  ";
		sql += "                       ,SUM(\"LineAmt\") \"LineTotal\"  ";
		sql += "                 FROM TotalData  ";
		sql += "                 GROUP BY \"CustNo\"  ";
		sql += "                ) t2 ON t2.\"CustNo\" = t1.\"CustNo\"  ";
		sql += "  )  ";
		sql += "    ";
		sql += "  (  ";
		sql += "  SELECT \"EntCode\"";
		sql += "  	    ,\"IsRels\" ";
		sql += "  	    ,\"CustNo\" ";
		sql += "  	    ,\"FacmNo\" ";
		sql += "  	    ,\"CustId\" ";
		sql += "  	    ,\"CustName\" ";
		sql += "  	    ,\"ClCode1\" ";
		sql += "  	    ,ROUND(\"LineAmt\") AS \"LineAmt\"";
		sql += "  	    ,ROUND(\"BookValue\") AS \"BookValue\"";
		sql += "  	    ,ROUND(\"LineTotal\") AS \"LineTotal\"";
		sql += "  FROM TotalData_LineTotal  ";
		sql += "  WHERE \"LineTotal\" >= :LineAmtThreshold  ";
		sql += "    AND \"EntCode\" LIKE :EntCodeCondition  ";
		sql += "    AND \"IsRels\" LIKE :IsRelsCondition  ";
		sql += "  )";
		sql += "  UNION ALL  ";
		sql += "  (  ";
		sql += "  SELECT :EntCodeCondition AS \"EntCode\"  ";
		sql += "        ,:IsRelsCondition AS \"IsRels\"  ";
		sql += "        ,' ' AS \"CustNo\"  ";
		sql += "        ,' ' AS \"FacmNo\"  ";
		sql += "        ,'Everything_Above_Sum' AS \"CustId\"  ";
		sql += "        ,u'以上合計' AS \"CustName\"  ";
		sql += "        ,8 AS \"ClCode1\"  ";
		sql += "        ,0 AS \"LineAmt\"  ";
		sql += "        ,0 AS \"BookValue\"  ";
		sql += "        ,NULL AS \"LineTotal\"  ";
		sql += "  FROM DUAL  ";
		sql += "    ";
		sql += "  UNION ";
		sql += "    ";
		sql += "  SELECT :EntCodeCondition AS \"EntCode\"  ";
		sql += "        ,:IsRelsCondition AS \"IsRels\"  ";
		sql += "        ,' ' AS \"CustNo\"  ";
		sql += "        ,' ' AS \"FacmNo\"  ";
		sql += "        ,'Everything_Above_Sum' AS \"CustId\"  ";
		sql += "        ,u'以上合計' AS \"CustName\"  ";
		sql += "        ,\"ClCode1\" AS \"ClCode1\"  ";
		sql += "        ,ROUND(SUM(\"LineAmt\")) AS \"LineAmt\"  ";
		sql += "        ,ROUND( CASE ";
		sql += "				  WHEN \"ClCode1\" = 0 THEN (SELECT \"LoanBal\" FROM ProjectLoan ) ";
		sql += "				  ELSE SUM(\"BookValue\") END ) AS \"BookValue\"  ";
		sql += "        ,NULL AS \"LineTotal\"  ";
		sql += "  FROM TotalData_LineTotal  ";
		sql += "  WHERE \"LineTotal\" >= :LineAmtThreshold  ";
		sql += "    AND \"EntCode\" LIKE :EntCodeCondition  ";
		sql += "    AND \"IsRels\" LIKE :IsRelsCondition  ";
		sql += "  GROUP BY \"ClCode1\"  ";
		sql += "  ) ";
		sql += "  UNION ALL  ";
		sql += "  (  ";
		sql += "  SELECT :EntCodeCondition AS \"EntCode\"  ";
		sql += "        ,:IsRelsCondition AS \"IsRels\"  ";
		sql += "        ,' ' AS \"CustNo\"  ";
		sql += "        ,' ' AS \"FacmNo\"  ";
		sql += "        ,'Everything_Below_Sum' AS \"CustId\"  ";
		sql += "        ,u'以下合計' AS \"CustName\"  ";
		sql += "        ,8 AS \"ClCode1\"  ";
		sql += "        ,0 AS \"LineAmt\"  ";
		sql += "        ,0 AS \"BookValue\"  ";
		sql += "        ,NULL AS \"LineTotal\"  ";
		sql += "  FROM DUAL  ";
		sql += "    ";
		sql += "  UNION ";
		sql += "    ";
		sql += "  SELECT :EntCodeCondition AS \"EntCode\"  ";
		sql += "        ,:IsRelsCondition AS \"IsRels\"  ";
		sql += "        ,' ' AS \"CustNo\"  ";
		sql += "        ,' ' AS \"FacmNo\"  ";
		sql += "        ,'Everything_Below_Sum' AS \"CustId\"  ";
		sql += "        ,u'以下合計' AS \"CustName\"  ";
		sql += "        ,\"ClCode1\" AS \"ClCode1\"  ";
		sql += "        ,ROUND(SUM(\"LineAmt\")) AS \"LineAmt\"  ";
		sql += "        ,ROUND( CASE ";
		sql += "				  WHEN \"ClCode1\" = 0 THEN (SELECT \"LoanBal\" FROM ProjectLoan ) ";
		sql += "				  ELSE SUM(\"BookValue\") END ) AS \"BookValue\"  ";
		sql += "        ,NULL AS \"LineTotal\"  ";
		sql += "  FROM TotalData_LineTotal  ";
		sql += "  WHERE \"LineTotal\" < :LineAmtThreshold  ";
		sql += "    AND \"EntCode\" LIKE :EntCodeCondition  ";
		sql += "    AND \"IsRels\" LIKE :IsRelsCondition  ";
		sql += "  GROUP BY \"ClCode1\"  ";
		sql += "  )  ";
		sql += "  UNION ALL  ";
		sql += "  (  ";
		sql += "  SELECT :EntCodeCondition AS \"EntCode\"  ";
		sql += "        ,:IsRelsCondition AS \"IsRels\"  ";
		sql += "        ,' ' AS \"CustNo\"  ";
		sql += "        ,' ' AS \"FacmNo\"  ";
		sql += "        ,'Everything_Total' AS \"CustId\"  ";
		sql += "        ,u'總計' AS \"CustName\"  ";
		sql += "        ,8 AS \"ClCode1\"  ";
		sql += "        ,0 AS \"LineAmt\"  ";
		sql += "        ,0 AS \"BookValue\"  ";
		sql += "        ,NULL AS \"LineTotal\"  ";
		sql += "  FROM DUAL  ";
		sql += "    ";
		sql += "  UNION ";
		sql += "    ";
		sql += "  SELECT :EntCodeCondition AS \"EntCode\"  ";
		sql += "        ,:IsRelsCondition AS \"IsRels\"  ";
		sql += "        ,' ' AS \"CustNo\"  ";
		sql += "        ,' ' AS \"FacmNo\"  ";
		sql += "        ,'Everything_Total' AS \"CustId\"  ";
		sql += "        ,u'總計' AS \"CustName\"  ";
		sql += "        ,\"ClCode1\" AS \"ClCode1\"  ";
		sql += "        ,ROUND(SUM(\"LineAmt\")) AS \"LineAmt\"  ";
		sql += "        ,ROUND( CASE ";
		sql += "				  WHEN \"ClCode1\" = 0 THEN (SELECT \"LoanBal\" FROM ProjectLoan ) ";
		sql += "				  ELSE SUM(\"BookValue\") END ) AS \"BookValue\"  ";
		sql += "        ,NULL AS \"LineTotal\"  ";
		sql += "  FROM TotalData_LineTotal  ";
		sql += "  WHERE \"EntCode\" LIKE :EntCodeCondition  ";
		sql += "    AND \"IsRels\" LIKE :IsRelsCondition  ";
		sql += "  GROUP BY \"ClCode1\"  ";
		sql += "  )";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		// EntCodeCondition 企金別 0/1
		// entdy 製表日期 YYYYMMDD
		// IsRelsCondition 關係人別 Y/N
		// LineAmtThreshold 核貸總值分界
		// YearMonth 製表年月 YYYYMM

		query.setParameter("EntCodeCondition", entCodeCondition.value);
		query.setParameter("entdy", entdy);
		query.setParameter("IsRelsCondition", isRelsCondition.value);
		query.setParameter("LineAmtThreshold", titaVo.getParam("inputAmount"));
		query.setParameter("YearMonth", entdy / 100);

		return this.convertToMap(query);
	}

}