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

@Service("l5912ServiceImpl")
public class L5912ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5912ServiceImpl.class);

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

	public List<Map<String, String>> FindData(int index, int limit,int startDate,int endDate,TitaVo titaVo) throws Exception{
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
//		String sql = "select a.\"DeptCode\" , ";
		String sql = "select a.\"DepItem\" , ";
			   sql += "a.\"DistItem\" , ";
			   sql += "a.\"Fullname\" ,";
			   sql += "count(x.\"BsOfficer\") as totalBsOfficer , ";
			   sql += "count(i.\"RepayBank\") as totalRepayBank , ";
			   sql += " case when count(x.\"BsOfficer\") = 0 then 0";
			   sql += " else round(count(i.\"RepayBank\")/count(x.\"BsOfficer\"),1)*100 end as \"Count\" ";
			   sql += " from \"PfBsDetail\" x left join \"BankAuthAct\" i on i.\"FacmNo\" = x.\"FacmNo\" and i.\"CustNo\" = x.\"CustNo\"";
			   sql += " and i.\"RepayBank\" = '103'";
			   sql += " left join \"PfBsOfficer\" a on a.\"EmpNo\" = x.\"BsOfficer\"";
			   sql += " where x.\"PerfDate\" between '"+startDate+"' and '"+endDate +"' ";
			   sql += " and i.\"Status\" != '1' ";
			   sql += " group by a.\"DepItem\" , a.\"DistItem\" , a.\"Fullname\" ";
			   sql += sqlRow;
		logger.info("sql = "+sql); 

		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		
		query.setMaxResults(this.limit);
		logger.info("L5912Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
