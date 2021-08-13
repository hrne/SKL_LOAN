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

@Service("l5408ServiceImpl")
public class L5408ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5408ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> FindData(int startDate,int endDate,TitaVo titaVo) throws Exception{
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "select a.\"WorkMonth\",a.\"BsOfficer\", count(*) as \"Total\" , b.\"Fullname\"  , e.\"DeptCode\",e.\"DepItem\"" + 
				"from \"PfBsDetail\" a " + 
				"left join \"CdEmp\" b on b.\"EmployeeNo\" = a.\"BsOfficer\"  left join \"PfBsOfficer\" e on e.\"EmpNo\" = a.\"BsOfficer\"" + 
				"where a.\"WorkMonth\" between '"+startDate+"' and '"+endDate+"'" + 
				"group by a.\"WorkMonth\",a.\"BsOfficer\",b.\"Fullname\",e.\"DeptCode\",e.\"DepItem\"" + 
				"order by \"WorkMonth\" asc";
		
		logger.info("sql = "+sql); 

		query = em.createNativeQuery(sql);
		logger.info("L5408Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
