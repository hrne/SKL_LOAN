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

@Service("l5910ServiceImpl")
public class L5910ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

	public List<Map<String, String>> findDetail(int StartDate, int EndDate, TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

//		String sql = "select a.\"WorkMonth\", " + "b.\"DrawdownDate\", " + "a.\"CustNo\", " + "a.\"FacmNo\" , " + "a.\"BormNo\", " + "b.\"DrawdownAmt\", " + "b.\"LoanBal\", " + "b.\"StoreRate\", "
//				+ "b.\"ApproveRate\", "
////						+ "b.\"ApproveRate\", "
//				+ "c.\"ProdNo\", " + "d.\"ProdName\", " + "c.\"RateIncr\"," + "c.\"PieceCode\"," + "e.\"RegCityCode\"," + "f.\"CityItem\"," + "c.\"BusinessOfficer\"," + "g.\"Fullname\""
//				+ "from \"PfBsDetail\" a " 
//				+ "left join \"LoanBorMain\" b on a.\"CustNo\" = b.\"CustNo\" and a.\"FacmNo\" = b.\"FacmNo\" and a.\"BormNo\" = b.\"BormNo\" "
//				+ "left join \"FacMain\" c on a.\"CustNo\" = c.\"CustNo\" and a.\"FacmNo\" = c.\"FacmNo\" " 
//				+ "left join \"FacProd\" d on c.\"ProdNo\" = d.\"ProdNo\" "
//				+ "left join \"CustMain\" e on a.\"CustNo\" = e.\"CustNo\" " + "left join \"CdCity\" f on f.\"CityCode\" = e.\"RegCityCode\" "
//				+ "left join \"PfBsOfficer\" g on c.\"BusinessOfficer\" = g.\"EmpNo\" " 
//				+ "where b.\"DrawdownDate\" between '" + dateSt + "' and '" + dateTo + "' "
//				+ "order by a.\"WorkMonth\" asc , b.\"DrawdownDate\" asc ,a.\"CustNo\" asc ,a.\"FacmNo\" asc ,a.\"BormNo\" asc ";
		
		String sql = "select a.\"WorkMonth\","
				+ "a.\"DrawdownDate\","
				+ "a.\"CustNo\","
				+ "a.\"FacmNo\","
				+ "a.\"BormNo\","
				+ "a.\"DrawdownAmt\","
				+ "a.\"ProdCode\","
				+ "d.\"ProdName\","
				+ "a.\"PieceCode\","
				+ "a.\"BsOfficer\","
				+ "g.\"Fullname\","
				+ "b.\"LoanBal\" ,"
				+ "b.\"StoreRate\","
				+ "b.\"ApproveRate\" as \"LoanApproveRate\" ,"
				+ "b.\"RateIncr\" as \"LBMRateIncr\","
				+ "c.\"RegCityCode\","
				+ "e.\"ApproveRate\" as \"FacApproveRate\","
				+ "r2.\"FitRate\","
				+ "r2.\"IncrFlag\","
				+ "r2.\"RateIncr\" as \"LRCRateIncr\"," 
				+ "r2.\"IndividualIncr\","
				+ "g.\"DeptCode\""
				+" from \"PfBsDetail\" a"  
				+" left join \"LoanBorMain\" b on a.\"CustNo\" = b.\"CustNo\" and a.\"FacmNo\" = b.\"FacmNo\" and a.\"BormNo\" = b.\"BormNo\""  
				+" left join \"FacProd\" d on a.\"ProdCode\" = d.\"ProdNo\""  
				+" left join \"PfBsOfficer\" g on a.\"BsOfficer\" = g.\"EmpNo\" and a.\"WorkMonth\" = g.\"WorkMonth\"" 
				+" left join \"CustMain\" c on c.\"CustNo\" = a.\"CustNo\""
				+" left join \"FacMain\" e on e.\"CustNo\" = a.\"CustNo\" and e.\"FacmNo\" = a.\"FacmNo\""
				+ "left join ("
				+ "				select rb.\"CustNo\","
				+ "				rb.\"FacmNo\","
				+ "				rb.\"BormNo\","
				+ "				rb.\"FitRate\","
				+ "				rb.\"EffectDate\","
				+ "				rb.\"IncrFlag\","				
				+ "				rb.\"RateIncr\","
				+ "             rb.\"IndividualIncr\","
				+ "				row_number() over (partition by rb.\"CustNo\", rb.\"FacmNo\", rb.\"BormNo\" order by rb.\"EffectDate\" Desc) as seq"
				+ "             from \"LoanRateChange\" rb"
				+ "          ) r2 on r2.\"CustNo\" = a.\"CustNo\" and r2.\"FacmNo\" = a.\"FacmNo\" and r2.\"BormNo\" = a.\"BormNo\" and r2.seq = 1 "
				+" where a.\"DrawdownDate\" between :StartDate and :EndDate and \"RepayType\" = '0' "
				+" order by a.\"WorkMonth\" ,a.\"DrawdownDate\" asc ";
		

		this.info("sql = " + sql);

		query = em.createNativeQuery(sql);


		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		query.setParameter("StartDate", StartDate);
		query.setParameter("EndDate", EndDate);
		query.setMaxResults(this.limit);

		this.info("L5910Service FindData=" + query.toString());
		return this.convertToMap(query);
	}
	
	public List<Map<String, String>> findResult(int StartDate, int EndDate, TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		
		String sql = "select sum(a.\"DrawdownAmt\") as \"Total\", " + 
				"a.\"DeptCode\", " + 
				"g.\"Fullname\", " +
				"g.\"DepItem\", " +
				"sum(a.\"DrawdownAmt\"*b.\"ApproveRate\") as \"Dulp\" , " + 
				"sum(a.\"DrawdownAmt\"*b.\"ApproveRate\")/ sum(a.\"DrawdownAmt\") as \"Divid\", " + 
				"row_number () over (order by sum(a.\"DrawdownAmt\"*b.\"ApproveRate\")/ sum(a.\"DrawdownAmt\") asc ) as \"sort\" " + 
				" from \"PfBsDetail\" a " + 
				"left join \"PfBsOfficer\" g  on a.\"BsOfficer\" = g.\"EmpNo\" and a.\"WorkMonth\" = g.\"WorkMonth\" " + 
				"left join \"LoanBorMain\" b on  a.\"CustNo\" = b.\"CustNo\" and a.\"FacmNo\" = b.\"FacmNo\" and a.\"BormNo\" = b.\"BormNo\"   " + 
				"where a.\"DrawdownDate\" between :StartDate and :EndDate and \"RepayType\" = '0' and a.\"BsOfficer\" is not null and g.\"Fullname\" is not null "+
				"group by a.\"DeptCode\"  ,g.\"Fullname\",g.\"DepItem\"";
		

		this.info("sql = " + sql);

		query = em.createNativeQuery(sql);


		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		query.setParameter("StartDate", StartDate);
		query.setParameter("EndDate", EndDate);
		query.setMaxResults(this.limit);

		this.info("L5910Service FindData=" + query.toString());
		return this.convertToMap(query);
	}
	public List<Map<String, String>> findResultAdd(int StartDate, int EndDate, TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		
		String sql = "select  " + 
				"sum(a.\"DrawdownAmt\") as \"Total\", " + 
				"a.\"DeptCode\", " + 
				"g.\"DepItem\",  " + 
				"sum(a.\"DrawdownAmt\"*b.\"ApproveRate\") as \"Dulp\" , " + 
				"sum(a.\"DrawdownAmt\"*b.\"ApproveRate\")/ sum(a.\"DrawdownAmt\") as \"Divid\",  " + 
				"row_number () over (order by sum(a.\"DrawdownAmt\"*b.\"ApproveRate\")/ sum(a.\"DrawdownAmt\") asc ) as \"sort\"   " + 
				"from \"PfBsDetail\" a  " + 
				"left join \"PfBsOfficer\" g  on a.\"BsOfficer\" = g.\"EmpNo\" and a.\"WorkMonth\" = g.\"WorkMonth\"  " + 
				"left join \"LoanBorMain\" b on  a.\"CustNo\" = b.\"CustNo\" and a.\"FacmNo\" = b.\"FacmNo\" and a.\"BormNo\" = b.\"BormNo\"    " + 
				"where a.\"DrawdownDate\" between :StartDate and :EndDate and \"RepayType\" = '0' and a.\"BsOfficer\" is not null and g.\"Fullname\" is not null  " + 
				"group by a.\"DeptCode\"  ,g.\"DepItem\"";
		

		this.info("sql = " + sql);

		query = em.createNativeQuery(sql);


		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		query.setParameter("StartDate", StartDate);
		query.setParameter("EndDate", EndDate);
		query.setMaxResults(this.limit);

		this.info("L5910Service FindData=" + query.toString());
		return this.convertToMap(query);
	}
}
