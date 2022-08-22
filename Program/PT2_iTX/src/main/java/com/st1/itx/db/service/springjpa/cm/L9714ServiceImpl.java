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

@Service
@Repository
/* 逾期放款明細 */
public class L9714ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L9714.findAll");

		String iCUSTNO = titaVo.get("CustNo");
		String iFACMNO = titaVo.get("FacmNo");
		String iUSEFG = titaVo.get("UsageCode");
		String iYEARMONTH = String.valueOf(Integer.valueOf(titaVo.get("eYearMonth")) + 191100);
		String sYEARMONTH = String.valueOf(Integer.valueOf(titaVo.get("sYearMonth"))+ 191100);
		boolean useUsageCode = !"00".equals(iUSEFG);
		boolean useFacmNo = !"000".equals(iFACMNO);

		String sql = "";

		sql += " SELECT \"Fn_ParseEOL\"(CM.\"CustName\", 0) AS \"CustName\" ";
		sql += "      , CM.\"CustId\" ";
		sql += "      , YHLI.\"CustNo\" ";
		sql += "      , YHLI.\"FacmNo\" ";
		sql += "      , YHLI.\"LoanAmt\" ";
		sql += "      , NVL(JSON_VALUE(YHLI.\"JsonFields\", '$.StartMonth'), :syearmonth) AS \"StartMonth\" ";
		sql += "      , NVL(JSON_VALUE(YHLI.\"JsonFields\", '$.EndMonth'), :iyearmonth)   AS \"EndMonth\" ";
		sql += "      , YHLI.\"LoanBal\" ";
		sql += "      , NVL(CC.\"Item\",CC2.\"Item\")                         AS \"Usage\" ";
		sql += "      , YHLI.\"YearlyInt\" ";
		sql += "      , YHLI.\"HouseBuyDate\" ";
		sql += "      , YHLI.\"FirstDrawdownDate\" ";
		sql += "      , YHLI.\"MaturityDate\" ";
		sql += " FROM \"YearlyHouseLoanInt\" YHLI ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = YHLI.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = YHLI.\"CustNo\" ";
		sql += "                        AND F.\"FacmNo\" = YHLI.\"FacmNo\" ";
		sql += " LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'UsageCode' ";
		sql += "                        AND CC.\"Code\" = YHLI.\"UsageCode\" ";
		sql += " LEFT JOIN \"CdCode\" CC2 ON CC2.\"DefCode\" = 'UsageCode' ";
		sql += "                         AND CC2.\"Code\" = F.\"UsageCode\" ";
		sql += " WHERE YHLI.\"YearlyInt\" > 0 ";
		sql += "   AND YHLI.\"YearMonth\" >= :syearmonth";
		sql += "   AND YHLI.\"YearMonth\" <= :iyearmonth";
		sql += "   AND YHLI.\"CustNo\" = :icustno ";
		if (useFacmNo)
			sql += "   AND YHLI.\"FacmNo\" = :ifacmno ";
		if (useUsageCode)
			sql += "   AND NVL(CC.\"Code\",CC2.\"Code\") = :iusefg ";
		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("icustno", iCUSTNO);
		query.setParameter("iyearmonth", iYEARMONTH);
		query.setParameter("syearmonth", sYEARMONTH);
		if (useFacmNo) {
			query.setParameter("ifacmno", iFACMNO);
		}
		if (useUsageCode) {
			query.setParameter("iusefg", iUSEFG);
		}
		return this.convertToMap(query);
	}

}