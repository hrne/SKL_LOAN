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

@Service("l5409ServiceImpl")
public class L5409ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5409ServiceImpl.class);

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
		String sql = "select x.\"Fullname\", sum(c.\"DrawdownAmt\") as \"TotalDrawdownAmt\" , sum(i.\"BadDebtBal\") as \"TotalBadDebtBal\" ";
			   sql += " ,case when sum(c.\"DrawdownAmt\") = 0 then 0";
			   sql += " else round(sum(i.\"BadDebtBal\")/sum(c.\"DrawdownAmt\"),4)*100 end as \"Percent\" ";
			   sql += " from \"PfBsDetail\" c " ;
			   sql += " left join \"CdEmp\" x on x.\"EmployeeNo\" = c.\"BsOfficer\"";
			   sql += " left join \"CollList\" i on i.\"CustNo\" = c.\"CustNo\" and i.\"FacmNo\" = c.\"FacmNo\" and i.\"OvduTerm\" >=4";
			   sql += " and c.\"PerfDate\" between '"+startDate+"' and '"+endDate+"' group by x.\"Fullname\"";
			   sql += " order by \"Percent\" desc , sum(c.\"DrawdownAmt\") desc , sum(i.\"BadDebtBal\") desc";
		logger.info("sql = "+sql); 

		query = em.createNativeQuery(sql);
		logger.info("L5409Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
