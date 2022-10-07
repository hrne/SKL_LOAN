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

		sql += "select \r\n";
		sql += "	\"CustNo\"\r\n";
		sql += "	,\"FacmNo\"\r\n";
		sql += "	,\"HouseBuyDate\"\r\n";
		sql += "	,\"LoanAmt\"\r\n";
		sql += "	,\"FirstDrawdownDate\"\r\n";
		sql += "	,\"MaturityDate\"\r\n";
		sql += "	,\"LoanBal\"\r\n";
		sql += "	,\"YearMonth\"\r\n";
		sql += "	,\"JsonFields\"\r\n";
		sql += "	,\"UsageCode\"\r\n";
		sql += "	,\"YearlyInt\"\r\n";
		sql += "	from  \"YearlyHouseLoanInt\"\r\n";
		sql += "	where \"YearMonth\"= :iYYYYMM ";
		sql += "	order by \"CustNo\",\"FacmNo\" ";

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