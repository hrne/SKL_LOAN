package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service("l5915ServiceImpl")
public class L5915ServiceImpl extends ASpringJpaParm implements InitializingBean {
	@Autowired
	public Parse parse;
	
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

	public List<Map<String, String>> FindData(TitaVo titaVo) throws Exception {
		int workMonth = parse.stringToInteger(titaVo.getParam("Ym")) + 191100;
		
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		
		String sql = "select a.\"Coorgnizer\",b.\"Fullname\",a.\"CustNo\",a.\"FacmNo\",a.\"BormNo\",a.\"DrawdownAmt\",a.\"ComputeCoBonusAmt\",a.\"CoorgnizerBonus\" ";
		sql += "from \"PfDetail\" a ";
		sql += "left join \"CdEmp\" b on b.\"EmployeeNo\"=a.\"Coorgnizer\" ";
		sql += "where a.\"WorkMonth\"=:workmonth and a.\"Coorgnizer\" is not null and a.\"ComputeCoBonusAmt\">0 and a.\"RepayType\"=0 ";
		sql += "order by a.\"Coorgnizer\",a.\"CustNo\",a.\"FacmNo\",a.\"BormNo\" ";
		this.info("sql = " + sql);

		query = em.createNativeQuery(sql);
		
		query.setParameter("workmonth", workMonth);

		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		query.setMaxResults(this.limit);
//		this.info("L5915Service FindData=" + query.toString());
		return this.convertToMap(query);
	}

}
