package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service("l7912ServiceImpl")
public class L7912ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L7912ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> FindData(int CustNo, TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "select f.\"CustNo\",f.\"FacmNo\",f.\"ApplNo\",f.\"AcctCode\",f.\"LineAmt\",f.\"LoanTermYy\",f.\"LoanTermMm\",f.\"LoanTermDd\",f.\"UtilDeadline\""
				+ ",f.\"MaturityDate\" as \"FacMaturityDate\",f.\"BaseRateCode\",f.\"RecycleCode\",f.\"RecycleDeadline\",f.\"RateIncr\",f.\"ApproveRate\","
				+ "f.\"FirstDrawdownDate\",f.\"MaturityDate\",f.\"RecycleCode\" as \"RecycleCode1\",f.\"UtilBal\",f.\"CurrencyCode\",f.\"FireOfficer\","
				+ "c.\"ClCode1\",c.\"ClCode2\",c.\"ClNo\",f.\"UtilAmt\"" + " from \"FacMain\" f " + " left join \"ClFac\" c on c.\"CustNo\" = f.\"CustNo\" and c.\"FacmNo\" = f.\"FacmNo\""
				+ " where f.\"CustNo\" = '" + CustNo + "'";

		logger.info("sql = " + sql);

		query = em.createNativeQuery(sql);
		logger.info("L7912Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
