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

@Service("l5911ServiceImpl")
public class L5911ServiceImpl extends ASpringJpaParm implements InitializingBean{
	private static final Logger logger = LoggerFactory.getLogger(L5911ServiceImpl.class);

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
	
	public List<Map<String, String>> FindData(int index, int limit,int dateSt, int dateTo ,TitaVo titaVo) throws Exception{
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		
		String  sql = "select ";
				sql += "distinct a.\"CreditSysNo\" ," ;
				sql += "sum(b.\"LineAmt\") as \"TotalAmt\" ,"; 
				sql += "a.\"CustNo\", ";
				sql += "count(b.\"LineAmt\") as \"Count\" ,";
				sql += "a.\"FirstDrawdownDate\", ";
				sql += "a.\"BusinessOfficer\", ";
				sql += "a.\"Supervisor\", ";
				sql += "c.\"ClCode1\", ";
				sql += "c.\"ClCode2\", ";
				sql += "c.\"ClNo\", ";
				sql += "sum(d.\"EvaAmt\") as \"TotalEvaAmt\", ";
				sql += "sum(e.\"RentPrice\") as \"TotalRentPrice\", ";
				sql += "f.\"RegCityCode\", ";
				sql += "g.\"Fullname\" as \"BusinessOfficerName\", ";
				sql += "h.\"Fullname\" as \"SupervisorName\", ";
				sql += "i.\"CityItem\" ";
				sql += "from \"FacMain\" a ";
				sql += "left join \"FacMain\" b on b.\"CreditSysNo\" = a.\"CreditSysNo\" ";
				sql += "left join \"ClFac\" c on c.\"CustNo\" = a.\"CustNo\" and c.\"ApproveNo\" = a.\"ApplNo\" ";
				sql += "left join \"ClMain\" d on c.\"ClCode1\" = d.\"ClCode1\" and c.\"ClCode2\" = d.\"ClCode2\" and c.\"ClNo\" = d.\"ClNo\" ";
				sql += "left join \"ClImm\" e on e.\"ClCode1\" = d.\"ClCode1\" and e.\"ClCode2\" = d.\"ClCode2\" and e.\"ClNo\" = d.\"ClNo\" ";
				sql += "left join \"CustMain\" f on f.\"CustNo\" = a.\"CustNo\" ";
				sql += "left join \"PfBsOfficer\" g on g.\"EmpNo\" = a.\"BusinessOfficer\" ";
				sql += "left join \"CdEmp\" h on h.\"EmployeeNo\" = a.\"Supervisor\" ";
				sql += "left join \"CdCity\" i on i.\"CityCode\" = f.\"RegCityCode\" ";
				sql += "where a.\"FirstDrawdownDate\" between '"+dateSt+"' and '"+dateTo+"'  ";
				sql += "and a.\"CreditSysNo\" != '0' ";
				sql += "and a.\"PieceCode\" in ( '1'  ,'A') ";
				sql += "group by  ";
				sql += "a.\"CreditSysNo\", a.\"LineAmt\", a.\"CustNo\", a.\"FacmNo\", a.\"BusinessOfficer\", ";
				sql += "a.\"Supervisor\", a.\"FirstDrawdownDate\", d.\"EvaAmt\", d.\"ClCode1\", c.\"ClCode1\", ";
				sql += "c.\"ClCode2\", c.\"ClNo\", e.\"RentPrice\", f.\"RegCityCode\",g.\"Fullname\",h.\"Fullname\", ";
				sql += "i.\"CityItem\" ";
				sql += "order by a.\"CustNo\" asc ";
				sql += sqlRow;

		logger.info("sql = "+sql);
		
		
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		
		query.setMaxResults(this.limit);
		logger.info("L5911Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
