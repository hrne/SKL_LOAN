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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l5813ServiceImpl")
@Repository
public class L5813ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> checkAll(String iYear, TitaVo titaVo) throws Exception {

		String sql = "　";
		int iYYYYMM = Integer.parseInt(iYear + 12) + 191100;

		this.info("iYYYYMM==" + iYYYYMM);

		sql += "WITH  UsageData  AS (                \r\n ";
		sql += "      SELECT  LM.\"CustNo\"          \r\n ";
		sql += "            , LM.\"FacmNo\"          \r\n ";
		sql += "            , LM.\"UsageCode\"       \r\n ";
		sql += "            , ROW_NUMBER()           \r\n ";
		sql += "             OVER (                  \r\n ";
		sql += "   	               PARTITION BY  LM.\"CustNo\"  \r\n ";
		sql += "                            ,LM.\"FacmNo\"      \r\n ";
		sql += "               ORDER BY LM.\"BormNo\" DESC      \r\n ";
		sql += "             )                                  AS \"Seq\"    \r\n ";        
		sql += "      FROM \"LoanBorMain\"  LM       \r\n ";
		sql += "      LEFT JOIN \"YearlyHouseLoanInt\" Y ON Y.\"YearMonth\" = :iYYYYMM    \r\n ";
		sql += "                                      AND Y.\"CustNo\" = LM.\"CustNo\"  \r\n ";
		sql += "                                      AND Y.\"FacmNo\" = LM.\"FacmNo\"  \r\n ";
		sql += "    )  \r\n ";

		sql += "select \r\n";
		sql += "	 y.\"CustNo\"          \r\n";
		sql += "	,y.\"FacmNo\"          \r\n";
		sql += "	,y.\"HouseBuyDate\"    \r\n";
		sql += "	,y.\"LoanAmt\"         \r\n";
		sql += "	,y.\"FirstDrawdownDate\"     \r\n";
		sql += "	,y.\"MaturityDate\"    \r\n";
		sql += "	,y.\"LoanBal\"         \r\n";
		sql += "	,y.\"YearMonth\"       \r\n";
		sql += "	,y.\"JsonFields\"      \r\n";
		sql += "	,y.\"UsageCode\"       \r\n";
		sql += "	,y.\"YearlyInt\"       \r\n";
		sql += "	,cb.\"BdLocation\"     \r\n";
		sql += "    ,CC.\"Item\"           \r\n";
		sql += "	from  \"YearlyHouseLoanInt\"  y      \r\n";
		sql += "    LEFT JOIN \"CustMain\" C ON C.\"CustNo\" =  Y.\"CustNo\" ";
		sql += "	left join  \"ClFac\" cl on cl.\"CustNo\" = y.\"CustNo\"      \r\n";
		sql += "	                       and cl.\"FacmNo\" = y.\"FacmNo\"      \r\n";
		sql += "	                       and cl.\"MainFlag\" = 'Y'             \r\n";
		sql += "	                       and cl.\"ClCode1\" = 1                 \r\n";
		sql += "	left join  \"ClBuilding\" cb on cb.\"ClCode1\" = cl.\"ClCode1\"      \r\n";
		sql += "	                            and cb.\"ClCode2\" = cl.\"ClCode2\"      \r\n";
		sql += "	                            and cb.\"ClNo\" = cl.\"ClNo\"            \r\n";
		sql += "	left join UsageData UD ON UD.\"CustNo\" = y.\"CustNo\"     \r\n";
		sql += "                          AND UD.\"FacmNo\" = y.\"FacmNo\"     \r\n";
		sql += "                          AND UD.\"Seq\"    = 1                \r\n";
		sql += "    left join \"CdCode\" CC ON CC.\"DefCode\" = 'UsageCode'  \r\n";
		sql += "    		               AND CC.\"Code\" = UD.\"UsageCode\"  \r\n";
		
		sql += "	where \"YearMonth\"= :iYYYYMM ";
		sql += "      AND C.\"CuscCd\" = 1 "; // 自然人,2023/3/2調整
		sql += "      AND CASE "; //須有房地擔保品故建號需有值,2023/3/2調整
		sql += "            WHEN  cb.\"BdNo1\" > 0  THEN 1";
		sql += "            WHEN  cb.\"BdNo2\" > 0  THEN 1";
		sql += "          ELSE 0 END = 1";
		sql += "	order by \"CustNo\",\"FacmNo\" , \"UsageCode\" DESC ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iYYYYMM", iYYYYMM);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryBuildingOwner(int custNo, int facmNo, TitaVo titaVo) throws Exception {

		this.info("queryBuildingOwner , custNo = " + custNo + " , facmNo = " + facmNo);

		if (custNo == 0) {
			this.info("CustNo = 0 return.");
			return null;
		}

		String sql = "　";
		sql += " SELECT NVL(OwnerCM.\"CustId\",CM.\"CustId\")     AS \"CustId\" ";
		sql += "      , NVL(OwnerCM.\"CustName\",CM.\"CustName\") AS \"CustName\" ";
		sql += "      , ROW_NUMBER() ";
		sql += "        OVER ( ";
		sql += "          PARTITION BY ";
		sql += "            CF.\"CustNo\" ";
		sql += "            , CF.\"FacmNo\" ";
		sql += "          ORDER BY ";
		sql += "            CASE ";
		sql += "              WHEN NVL(CM.\"CustId\",' ') != ' ' ";
		sql += "                   AND NVL(OwnerCM.\"CustId\",' ') != ' ' ";
		sql += "                   AND CM.\"CustId\" = OwnerCM.\"CustId\" ";
	    sql += "              THEN 0 ";
	    sql += "            ELSE 1 END ";
	    sql += "            , NVL(OwnerCM.\"CustId\",CM.\"CustId\") ";
	    sql += "        ) AS \"Seq\" ";
		sql += " FROM  \"ClFac\" CF ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = CF.\"CustNo\" ";
		sql += " LEFT JOIN \"ClBuildingOwner\" CBO ON CBO.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                                  AND CBO.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                                  AND CBO.\"ClNo\" = CF.\"ClNo\" ";
		sql += " LEFT JOIN \"CustMain\" OwnerCM ON OwnerCM.\"CustUKey\" = CBO.\"OwnerCustUKey\" ";
		sql += " WHERE CF.\"CustNo\"= :custNo ";
		sql += "   AND CF.\"FacmNo\"= :facmNo ";
		sql += " ORDER BY \"Seq\" ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("custNo", custNo);
		query.setParameter("facmNo", facmNo);

		return this.convertToMap(query);
	}

}