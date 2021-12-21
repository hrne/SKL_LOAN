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

@Service("l5R33ServiceImpl")
public class L5R33ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5R33ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> getSeq(int CustNo, TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = " ";
		sql += " select c.\"CustName\"                       ";
		sql += " , max(i.\"ApplSeq\") as \"MaxApplSeq\"      ";
		sql += " from \"CustMain\" c                         ";
		sql += " left join \"InnDocRecord\" i                ";
		sql += " on i.\"CustNo\" = c.\"CustNo\"              ";
		sql += " where c.\"CustNo\" = " + CustNo;
		sql += " group by c.\"CustName\", i.\"ApplSeq\"      ";
		sql += " order by c.\"CustName\", i.\"ApplSeq\" DESC ";

		logger.info("sql = " + sql);

		query = em.createNativeQuery(sql);
		logger.info("LL5R33Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
