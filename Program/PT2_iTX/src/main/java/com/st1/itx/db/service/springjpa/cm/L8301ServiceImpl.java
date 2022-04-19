package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service("L8301ServiceImpl")
public class L8301ServiceImpl extends ASpringJpaParm implements InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	public List<String> findData(int index, int limit, TitaVo titaVo) throws Exception {
		Query query;
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "select \"FinCode\" from  \"NegFinAcct\" ";

		this.info("sql====" + sql);
		query = em.createNativeQuery(sql);

		this.info("L8301Service FindData=" + query.toString());
		return query.getResultList();
	}

}
