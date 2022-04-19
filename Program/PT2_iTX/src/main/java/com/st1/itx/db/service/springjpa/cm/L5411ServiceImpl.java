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

@Service("l5411ServiceImpl")
public class L5411ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5411ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> FindData(int dateSt, int dateTo, TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		String sql = "select a.\"ApplNo\", " + "a.\"LineAmt\", " + "a.\"CustNo\", " + "a.\"FirstDrawdownDate\", " + "a.\"BusinessOfficer\", " + "a.\"Supervisor\""
				+ "count(a.\"CustNo\",a.\"FacmNo\") as \"TotalCount\"" + "from \"FacMain\" a where a.\"FirstDrawdownDate\" between " + dateSt + "and" + dateTo + "group by a.\"CustNo\",a.\"FacmNo\" ";
		logger.info("sql = " + sql);

		query = em.createNativeQuery(sql);
		logger.info("L5411Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
