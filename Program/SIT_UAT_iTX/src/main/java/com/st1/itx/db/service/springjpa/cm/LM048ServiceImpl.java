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
public class LM048ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM048ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		String entdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 100);

		logger.info("lM048.findAll ");
		String sql = "SELECT I.\"IndustryItem\"";
		sql += "            ,M.\"CustNo\"";
		sql += "            ,C.\"CustName\"";
		sql += "            ,M.\"LineAmt\"";
		sql += "            ,M.\"LoanBalance\"";
		sql += "            ,M.\"StoreRate\"";
		sql += "            ,M.\"MaturityDate\"";
		sql += "            ,M.\"PrevIntDate\"";
		sql += "            ,NVL(R.\"CompanyName\", ' ') AS C11";
		sql += "      FROM (SELECT M.\"CustNo\"";
		sql += "                  ,SUM (F.\"LineAmt\") \"LineAmt\"";
		sql += "                  ,SUM(M.\"LoanBalance\") \"LoanBalance\"";
		sql += "                  ,MIN(M.\"StoreRate\") \"StoreRate\"";
		sql += "                  ,MAX(F.\"MaturityDate\") \"MaturityDate\"";
		sql += "                  ,MAX(B.\"PrevIntDate\") \"PrevIntDate\" ";
		sql += "            FROM (SELECT M.\"CustNo\"";
		sql += "                        ,M.\"FacmNo\"";
		sql += "                        ,SUM(M.\"LoanBalance\") \"LoanBalance\"";
		sql += "                        ,MAX(M.\"StoreRate\") \"StoreRate\"";
		sql += "                  FROM \"MonthlyLoanBal\" M";
		sql += "                  WHERE M.\"YearMonth\" = :entdy";
		sql += "                    AND M.\"EntCode\"   = '1'";
		sql += "                  GROUP BY M.\"CustNo\", M.\"FacmNo\") M";
		sql += "            LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                                   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "            LEFT JOIN \"MonthlyFacBal\" B ON B.\"YearMonth\" = :entdy";
		sql += "                                         AND B.\"CustNo\"    = M.\"CustNo\"";
		sql += "                                         AND B.\"FacmNo\"    = M.\"FacmNo\"";
		sql += "            WHERE B.\"Status\" IN (0, 2, 6)";
		sql += "            GROUP BY M.\"CustNo\") M";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "      LEFT JOIN \"CdIndustry\" I ON I.\"IndustryCode\" = C.\"IndustryCode\"";
		sql += "      LEFT JOIN \"RelsCompany\" R ON R.\"CompanyId\" = C.\"CustId\"";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		return this.convertToMap(query.getResultList());
	}

}