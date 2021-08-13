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

@Service("l5021ServiceImpl")
public class L5021ServiceImpl extends ASpringJpaParm implements InitializingBean{
	private static final Logger logger = LoggerFactory.getLogger(L5021ServiceImpl.class);

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
	
	public String FindL5021(String WorkYear,String WorkMonth) throws Exception{

		
		String sql = "select "+"i.\"WorkMonth\", "+ "i.\"EmpNo\", "+ "i.\"GoalAmt\", "+ "i.\"Fullname\", " + "sum(d.\"PerfAmt\") as \"TotalPerfAmt\" "
					+"from \"PfBsOfficer\" i left join \"PfBsDetail\" d on d.\"BsOfficer\" = i.\"EmpNo\" and d.\"WorkMonth\" = i.\"WorkMonth\"";
		if(!WorkMonth.equals("00")) {
			sql +=" where i.\"WorkMonth\" = "+String.valueOf(Integer.valueOf(WorkYear)+1911)+String.valueOf(WorkMonth);
		}else {
			sql +=" where i.\"WorkMonth\" between "+String.valueOf(Integer.valueOf(WorkYear)+1911)+String.valueOf("01")+" and "+ String.valueOf(Integer.valueOf(WorkYear)+1911)+String.valueOf("13");
		}
		sql += " group by i.\"WorkMonth\", i.\"EmpNo\", i.\"Fullname\", i.\"GoalAmt\" order by i.\"WorkMonth\" asc ";
		sql += sqlRow;
		logger.info("sql = "+sql);
		return sql;
	}
	
	public List<Map<String, String>> FindData(int index, int limit,String sql,TitaVo titaVo) throws Exception{
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);
		
		logger.info("L5021Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
