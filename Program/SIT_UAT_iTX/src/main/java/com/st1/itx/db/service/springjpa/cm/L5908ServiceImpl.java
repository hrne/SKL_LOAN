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

@Service("l5908ServiceImpl")
public class L5908ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

	public List<Map<String, String>> FindData(int index, int limit, int workMonth1, int workMonth2, TitaVo titaVo) throws Exception {
				
		Query query;
		// *** 折返控制相關 ***
		this.limit = limit;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "select a.\"WorkMonth\",";
		sql += "a.\"DeptCode\","; 
		sql += "b.\"UnitItem\","; 
		sql += "a.\"BsOfficer\","; 
		sql += "c.\"Fullname\","; 
		sql += "count(*) as \"Total\" "; 
		sql += "from \"PfBsDetail\" a ";
//		sql += "left join \"PfBsOfficer\" b on a.\"WorkMonth\" = b.\"WorkMonth\" and a.\"BsOfficer\" = b.\"EmpNo\" " 
		sql += "LEFT JOIN \"CdBcm\" b ON b.\"UnitCode\"=a.\"DeptCode\" ";
		sql += "LEFT JOIN \"CdEmp\" c ON c.\"EmployeeNo\"=a.\"BsOfficer\" ";
		sql += "where a.\"WorkMonth\" between :WorkMonth1 and :WorkMonth2 ";
		sql += "and a.\"DrawdownAmt\" > 0 ";
		sql += "and a.\"BsOfficer\" is not null ";
		sql += "group by a.\"WorkMonth\",a.\"BsOfficer\",a.\"DeptCode\",c.\"Fullname\",b.\"UnitItem\" ";
		sql += "order by a.\"WorkMonth\",a.\"BsOfficer\",a.\"DeptCode\" ";
//		sql += sqlRow;
		this.info("sql = " + sql);
		
		query = em.createNativeQuery(sql);
		query.setParameter("WorkMonth1", workMonth1);
		query.setParameter("WorkMonth2", workMonth2);
		
//		query.setParameter("ThisIndex", index);
//		query.setParameter("ThisLimit", limit);

//		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
//
//		query.setMaxResults(this.limit);

		this.info("L5908Service FindData=" + query.toString());
		return this.convertToMap(query);
	}
}
