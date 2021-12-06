package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service("l6087ServiceImpl")
public class L6087ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";
	
	public List<Map<String, String>> findDistinct(TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "select distinct(\"WorkMonth\") from \"CdBonusCo\" order by \"WorkMonth\" DESC";
		this.info("sql = " + sql);

		query = em.createNativeQuery(sql);

		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		query.setMaxResults(this.limit);
		this.info("L6087Service FindData=" + query.toString());
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAllData(int sWorkMonth, TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "select row_number() over (order by \"WorkMonth\" DESC) as \"Sort\" , "
				+ "\"WorkMonth\" from (select distinct(\"WorkMonth\") from \"CdBonusCo\" where \"WorkMonth\" <= :sWorkMonth)";
		this.info("sql = " + sql);

		query = em.createNativeQuery(sql);

		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		query.setParameter("sWorkMonth", sWorkMonth);
		query.setMaxResults(this.limit);
		this.info("L6087Service FindData=" + query.toString());
		return this.convertToMap(query);
	}
}
