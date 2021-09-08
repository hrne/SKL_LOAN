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

@Service("L8337ServiceImpl")
public class L8337ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

	public List<Map<String, String>> findData(int index, int limit, String CustId, String BankId, TitaVo titaVo) throws Exception {
		Query query;
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "select \"CustId\","
				+ " \"Bank\" from (";
				for (int i = 1; i<=30 ;i ++) {
					sql += "select \"CustId\", \"Bank"+i+"\" as \"Bank\" from \"JcicZ570\" where \"CustId\" = '"+CustId+"'";
					if (i < 30) {
						sql += " union ";
					}else {
						sql += " )";
					}
				}
				sql += "where \"Bank\" = '"+BankId+"'";
		        
		this.info("sql===="+sql);
		query = em.createNativeQuery(sql);

		this.info("L8337Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
