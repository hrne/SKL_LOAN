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
public class LM013ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}
	
	public enum EntCodeCondition
	{
		Natural("0"),
		Enterprise("1"),
		All("%");
		
		private String value;
		
		EntCodeCondition(String value)
		{
			this.value = value;
		}
	}
	
	public enum IsRelsCondition
	{
		Yes("Y"),
		No("N"),
		All("%");
		
		private String value;
		
		IsRelsCondition(String value)
		{
			this.value = value;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, EntCodeCondition entCodeCondition, IsRelsCondition isRelsCondition) throws Exception {
		this.info("lM009.findAll ");

		String entdy = String.valueOf((Integer.valueOf(titaVo.getParam("inputDate")) + 19110000));

		String sql = " ";
		sql += "  WITH TotalData AS (  ";
		sql += "      SELECT *  ";
		sql += "      FROM ( SELECT DECODE(C.\"EntCode\",'1','1','0') AS \"EntCode\"  ";
		sql += "                   ,R.\"IsRels\" AS \"IsRels\"  ";
		sql += "                   ,TO_CHAR(D.\"CustNo\") AS \"CustNo\"  ";
		sql += "                   ,LPAD(D.\"FacmNo\", 3, '0') AS \"FacmNo\"  ";
		sql += "                   ,C.\"CustId\" AS \"CustId\"  ";
		sql += "                   ,C.\"CustName\" AS \"CustName\"  ";
		sql += "                   ,CASE WHEN D.\"ProdNo\" LIKE 'I%' OR D.\"ProdNo\" LIKE '8%' OR D.\"AcctCode\" = '340'  ";
		sql += "                         THEN 0  ";
		sql += "                    ELSE CASE CF.\"ClCode1\"  ";
		sql += "                           WHEN 1  ";
		sql += "                           THEN 2  ";
		sql += "                           WHEN 3  ";
		sql += "                           THEN 4  ";
		sql += "                         ELSE CF.\"ClCode1\" END   ";
		sql += "                    END AS \"ClCode1\"  ";
		sql += "                   ,F.\"LineAmt\" AS \"LineAmt\"  ";
		sql += "                   ,SUM(ROUND(NVL(IIM.\"BookValue\", D.\"LoanBalance\"))) AS \"BookValue\"  ";
		sql += "             FROM \"DailyLoanBal\" D  ";
		sql += "             LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\"  ";
		sql += "             LEFT JOIN ( SELECT cm.\"CustNo\"  ";
		sql += "                               ,CASE COUNT(brs.\"CustId\") + COUNT(brf.\"CustId\") + COUNT(brc.\"CustId\")  ";
		sql += "                                  WHEN 0  ";
		sql += "                                  THEN 'N'  ";
		sql += "                                ELSE 'Y' END AS \"IsRels\"  ";
		sql += "                         FROM \"CustMain\" cm  ";
		sql += "                         LEFT JOIN \"BankRelationSelf\" brs ON brs.\"CustId\" = cm.\"CustId\"  ";
		sql += "                         LEFT JOIN \"BankRelationFamily\" brf ON brf.\"CustId\" = cm.\"CustId\"  ";
		sql += "                                                              OR brf.\"RelationId\" = cm.\"CustId\"  ";
		sql += "                         LEFT JOIN \"BankRelationCompany\" brc ON brc.\"CompanyId\" = cm.\"CustId\"  ";
		sql += "                                                               OR brc.\"CustId\" = cm.\"CustId\"  ";
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
		sql += "                               ,ROUND(\"BookValue\") \"BookValue\"  ";
		sql += "                         FROM \"Ias39IntMethod\"  ";
		sql += "                       ) IIM ON IIM.\"CustNo\" = D.\"CustNo\"  ";
		sql += "                            AND IIM.\"FacmNo\" = D.\"FacmNo\"  ";
		sql += "                            AND IIM.\"BormNo\" = D.\"BormNo\"  ";
		sql += "                            AND NVL(IIM.\"Seq\", 0) = 1  ";
		sql += "             LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = D.\"CustNo\"  ";
		sql += "                                          AND LBM.\"FacmNo\" = D.\"FacmNo\"  ";
		sql += "                                          AND LBM.\"BormNo\" = D.\"BormNo\"  ";
		sql += "             WHERE D.\"MonthEndYm\" = :YearMonth  ";
		sql += "               AND F.\"FirstDrawdownDate\" <= :entdy  ";
		sql += "               AND LBM.\"Status\" IN (0,4)  ";
		sql += "               AND LBM.\"LoanBal\" > 0  ";
		sql += "             GROUP BY DECODE(C.\"EntCode\", '1', '1', '0')  ";
		sql += "                     ,R.\"IsRels\"  ";
		sql += "                     ,D.\"CustNo\"  ";
		sql += "                     ,LPAD(D.\"FacmNo\", 3, '0')  ";
		sql += "                     ,C.\"CustId\"  ";
		sql += "                     ,C.\"CustName\"  ";
		sql += "                     ,CASE WHEN D.\"ProdNo\" LIKE 'I%' OR D.\"ProdNo\" LIKE '8%' OR D.\"AcctCode\" = '340'  ";
		sql += "                           THEN 0  ";
		sql += "                      ELSE CASE CF.\"ClCode1\"  ";
		sql += "                             WHEN 1  ";
		sql += "                             THEN 2  ";
		sql += "                             WHEN 3  ";
		sql += "                             THEN 4  ";
		sql += "                           ELSE CF.\"ClCode1\" END  ";
		sql += "                      END  ";
		sql += "                     ,F.\"LineAmt\"  ";
		sql += "      )  ";
		sql += "      ORDER BY \"EntCode\" ";
		sql += "              ,\"IsRels\" ";
		sql += "              ,\"CustNo\" ";
		sql += "              ,\"FacmNo\" ";
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
		sql += "  SELECT *  ";
		sql += "  FROM TotalData_LineTotal  ";
		sql += "  WHERE \"LineTotal\" >= :LineAmtThreshold  ";
		sql += "    AND \"EntCode\" LIKE :EntCodeCondition  ";
		sql += "    AND \"IsRels\" LIKE :IsRelsCondition  ";
		sql += "    ";
		sql += "  UNION ALL  ";
		sql += "    ";
		sql += "  SELECT :EntCodeCondition AS \"EntCode\"  ";
		sql += "        ,:IsRelsCondition AS \"IsRels\"  ";
		sql += "        ,' ' AS \"CustNo\"  ";
		sql += "        ,' ' AS \"FacmNo\"  ";
		sql += "        ,'Everything_Above_Sum' AS \"CustId\"  ";
		sql += "        ,u'以上合計' AS \"CustName\"  ";
		sql += "        ,\"ClCode1\" AS \"ClCode1\"  ";
		sql += "        ,SUM(\"LineAmt\") AS \"LineAmt\"  ";
		sql += "        ,SUM(\"BookValue\") AS \"BookValue\"  ";
		sql += "        ,NULL AS \"LineTotal\"  ";
		sql += "  FROM TotalData_LineTotal  ";
		sql += "  WHERE \"LineTotal\" >= :LineAmtThreshold  ";
		sql += "    AND \"EntCode\" LIKE :EntCodeCondition  ";
		sql += "    AND \"IsRels\" LIKE :IsRelsCondition  ";
		sql += "  GROUP BY \"ClCode1\"  ";
		sql += "    ";
		sql += "  UNION ALL  ";
		sql += "    ";
		sql += "  SELECT :EntCodeCondition AS \"EntCode\"  ";
		sql += "        ,:IsRelsCondition AS \"IsRels\"  ";
		sql += "        ,' ' AS \"CustNo\"  ";
		sql += "        ,' ' AS \"FacmNo\"  ";
		sql += "        ,'Everything_Below_Sum' AS \"CustId\"  ";
		sql += "        ,u'以下合計' AS \"CustName\"  ";
		sql += "        ,\"ClCode1\" AS \"ClCode1\"  ";
		sql += "        ,SUM(\"LineAmt\") AS \"LineAmt\"  ";
		sql += "        ,SUM(\"BookValue\") AS \"BookValue\"  ";
		sql += "        ,NULL AS \"LineTotal\"  ";
		sql += "  FROM TotalData_LineTotal  ";
		sql += "  WHERE \"LineTotal\" < :LineAmtThreshold  ";
		sql += "    AND \"EntCode\" LIKE :EntCodeCondition  ";
		sql += "    AND \"IsRels\" LIKE :IsRelsCondition  ";
		sql += "  GROUP BY \"ClCode1\"  ";
		sql += "    ";
		sql += "  UNION ALL  ";
		sql += "    ";
		sql += "  SELECT :EntCodeCondition AS \"EntCode\"  ";
		sql += "        ,:IsRelsCondition AS \"IsRels\"  ";
		sql += "        ,' ' AS \"CustNo\"  ";
		sql += "        ,' ' AS \"FacmNo\"  ";
		sql += "        ,'Everything_Total' AS \"CustId\"  ";
		sql += "        ,u'總計' AS \"CustName\"  ";
		sql += "        ,\"ClCode1\" AS \"ClCode1\"  ";
		sql += "        ,SUM(\"LineAmt\") AS \"LineAmt\"  ";
		sql += "        ,SUM(\"BookValue\") AS \"BookValue\"  ";
		sql += "        ,NULL AS \"LineTotal\"  ";
		sql += "  FROM TotalData_LineTotal  ";
		sql += "  WHERE \"EntCode\" LIKE :EntCodeCondition  ";
		sql += "    AND \"IsRels\" LIKE :IsRelsCondition  ";
		sql += "  GROUP BY \"ClCode1\"  ";
		
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		// EntCodeCondition		企金別			0/1
		// entdy				製表日期		YYYYMMDD
		// IsRelsCondition      關係人別		Y/N
		// LineAmtThreshold		核貸總值分界
		// YearMonth			製表年月		YYYYMM

		query.setParameter("EntCodeCondition", entCodeCondition.value);
		query.setParameter("entdy", entdy);
		query.setParameter("IsRelsCondition", isRelsCondition.value);
		query.setParameter("LineAmtThreshold", titaVo.getParam("inputAmount"));
		query.setParameter("YearMonth", entdy.substring(0,6));

		return this.convertToMap(query.getResultList());
	}

}