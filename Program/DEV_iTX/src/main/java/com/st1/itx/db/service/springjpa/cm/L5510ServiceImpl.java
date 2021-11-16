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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l5510ServiceImpl")
@Repository
public class L5510ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

//	public String FindL5051(String FunctionCd, String PerfDateFm, String PerfDateTo, String CustNo, String FacmNo) throws Exception {
	public List<Map<String, String>> FindData(TitaVo titaVo, int workMonth, int index, int limit) throws Exception {
		this.info("L5051FindData");

		String sql = "SELECT A.*,B.\"AgentId\" ";
		sql += "FROM \"PfItDetail\" A ";
		sql += "LEFT JOIN \"CdEmp\" B ON B.\"EmployeeNo\"=A.\"Introducer\" ";
		sql += "WHERE A.\"WorkMonth\" = :WorkMonth ";
		sql += "AND A.\"DrawdownAmt\" != 0 AND (A.\"PerfReward\" != 0 OR A.\"PerfEqAmt\" != 0) AND A.\"MediaFg\" = 0 ";
		sql += "AND LENGTH(TRIM(A.\"Introducer\")) > 0 ";
		sql += "ORDER BY A.\"Introducer\",A.\"CustNo\",A.\"FacmNo\",A.\"DrawdownAmt\" desc ";

//		sql += sqlRow;

		this.info("FindL5051 sql=" + sql);

		// *** 折返控制相關 ***
//		this.index = index;
		// *** 折返控制相關 ***
//		this.limit = limit;
//		this.info("L5051ServiceImpl sql=[" + sql + "]");
//		this.info("L5051ServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");

		Query query;
//			query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
//		query.setParameter("ThisIndex", index);
//		query.setParameter("ThisLimit", limit);

		query.setParameter("WorkMonth", workMonth);

		this.info("L5510Service FindData=" + query);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
//		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
//		query.setMaxResults(this.limit);

		// List<L5051Vo> L5051VoList =this.convertToMap(query.getResultList());

		return this.convertToMap(query);
	}

}