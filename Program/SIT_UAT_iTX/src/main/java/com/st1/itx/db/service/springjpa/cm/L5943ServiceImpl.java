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

@Service("l5943ServiceImpl")
public class L5943ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5943ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> FindData(int YyyMm, TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "select p.\"EmpNo\" ";
		sql += " ,c.\"Fullname\" ";
		sql += " ,p.\"DistItem\" ";
		sql += " ,p.\"DepItem\" ";
		sql += " ,p.\"GoalAmt\" ";
		sql += " ,sum(d.\"DrawdownAmt\") as \"TotalDrawdownAmt\" ";
		sql += ", sum(d.\"PerfCnt\") as \"TotalPerfCnt\" ";
		sql += ", case ";
		sql += "when p.\"GoalAmt\"=0 then 0 ";
		sql += "else round(sum(d.\"DrawdownAmt\")/p.\"GoalAmt\",4)*100 end as \"Count\" ";
		sql += " from \"PfBsOfficer\" p ";
		sql += "left join \"CdEmp\" c on c.\"EmployeeNo\" = p.\"EmpNo\" ";
		sql += "left join \"PfBsDetail\" d on p.\"EmpNo\" = d.\"BsOfficer\" and ";
		sql += "d.\"WorkMonth\" = p.\"WorkMonth\" where p.\"WorkMonth\" = " + YyyMm;
		sql += " group by p.\"EmpNo\",c.\"Fullname\",p.\"DistItem\",p.\"DepItem\",p.\"GoalAmt\"";
		sql += " order by \"Count\" desc";

		logger.info("sql = " + sql);

		query = em.createNativeQuery(sql);
		logger.info("L5943Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
