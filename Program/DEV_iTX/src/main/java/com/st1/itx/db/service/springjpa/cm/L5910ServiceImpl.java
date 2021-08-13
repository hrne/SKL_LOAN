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

@Service("l5910ServiceImpl")
public class L5910ServiceImpl extends ASpringJpaParm implements InitializingBean{
	private static final Logger logger = LoggerFactory.getLogger(L5910ServiceImpl.class);

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
		
		String  sql = 	"select a.\"WorkMonth\", "
						+ "b.\"DrawdownDate\", "
						+ "a.\"CustNo\", "
						+ "a.\"FacmNo\" , "
						+ "a.\"BormNo\", "
						+ "b.\"DrawdownAmt\", "
						+ "b.\"LoanBal\", "
						+ "b.\"StoreRate\", "
						+ "b.\"ApproveRate\", "
//						+ "b.\"ApproveRate\", "
						+ "c.\"ProdNo\", "
						+ "d.\"ProdName\", "
						+ "c.\"RateIncr\","  
						+ "c.\"PieceCode\","
						+ "e.\"RegCityCode\","
						+ "f.\"CityItem\","
						+ "c.\"BusinessOfficer\","
						+ "g.\"Fullname\"" 
						+"from \"PfBsDetail\" a "  
						+"left join \"LoanBorMain\" b on a.\"CustNo\" = b.\"CustNo\" and a.\"FacmNo\" = b.\"FacmNo\" and a.\"BormNo\" = b.\"BormNo\" " 
						+"left join \"FacMain\" c on a.\"CustNo\" = c.\"CustNo\" and a.\"FacmNo\" = c.\"FacmNo\" "
						+"left join \"FacProd\" d on c.\"ProdNo\" = d.\"ProdNo\" " 
						+"left join \"CustMain\" e on a.\"CustNo\" = e.\"CustNo\" "
						+"left join \"CdCity\" f on f.\"CityCode\" = e.\"RegCityCode\" "
						+"left join \"PfBsOfficer\" g on c.\"BusinessOfficer\" = g.\"EmpNo\" "
						+"where b.\"DrawdownDate\" between '"+dateSt+"' and '"+dateTo+"' "
						+"order by a.\"WorkMonth\" asc , b.\"DrawdownDate\" asc ,a.\"CustNo\" asc ,a.\"FacmNo\" asc ,a.\"BormNo\" asc " ;
		sql += sqlRow;

		logger.info("sql = "+sql);
		
		
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		
		query.setMaxResults(this.limit);
		
		logger.info("L5910Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
