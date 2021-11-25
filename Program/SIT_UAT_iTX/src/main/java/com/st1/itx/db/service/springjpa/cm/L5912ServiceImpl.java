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

@Service("l5912ServiceImpl")
public class L5912ServiceImpl extends ASpringJpaParm implements InitializingBean {

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
		String sql = "select a.\"CustNo\"," + 
				" a.\"FacmNo\"," + 
				" a.\"BormNo\"," + 
				" a.\"DrawdownAmt\"," + 
				" a.\"DrawdownDate\"," + 
				" d.\"MaturityDate\"," + 
				" b.\"RepayBank\"," + 
				" a.\"PieceCode\"," + 
				" a.\"BsOfficer\"," + 
				" c.\"Fullname\"," + 
				" e.\"DepItem\"," + 
				" e.\"DistItem\"" + 
				" from \"PfBsDetail\" a  " + 
				" left join \"BankAuthAct\" b on a.\"CustNo\" = b.\"CustNo\" and a.\"FacmNo\" = b.\"FacmNo\" " + 
				" left join \"CdEmp\" c on c.\"EmployeeNo\" = a.\"BsOfficer\" " + 
				" left join \"LoanBorMain\" d on a.\"CustNo\" = d.\"CustNo\" and a.\"FacmNo\" = d.\"FacmNo\" and a.\"BormNo\" = d.\"BormNo\" " + 
				" left join \"PfBsOfficer\" e on a.\"WorkMonth\" = e.\"WorkMonth\" and a.\"BsOfficer\" = e.\"EmpNo\"" + 
				" where b.\"Status\" != '1' " + 
				"      and a.\"DrawdownDate\" between :StartDate and :EndDate" + 
				"      and a.\"BsOfficer\" is not null" + 
				" order by a.\"DrawdownDate\" ASC";
		this.info("sql = " + sql);

		query = em.createNativeQuery(sql);

		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		query.setParameter("StartDate", StartDate);
		query.setParameter("EndDate", EndDate);
		query.setMaxResults(this.limit);
		this.info("L5912Service FindData=" + query.toString());
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findData(int StartDate, int EndDate, TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "select * from (select y.\"BsOfficer\",y.\"Fullname\",y.\"DistItem\",y.\"DepItem\",count(y.\"BsOfficer\") as \"Total\" from (" + 
				"select " + 
				"a.\"CustNo\", " + 
				"a.\"FacmNo\", " + 
				"a.\"BormNo\", " + 
				"a.\"DrawdownAmt\", " + 
				"a.\"DrawdownDate\", " + 
				"d.\"MaturityDate\", " + 
				"b.\"RepayBank\"," + 
				"a.\"PieceCode\"," + 
				"a.\"BsOfficer\"," + 
				"c.\"Fullname\"," + 
				"e.\"DepItem\"," + 
				"e.\"DistItem\" " + 
				"from \"PfBsDetail\" a  " + 
				"left join \"BankAuthAct\" b on a.\"CustNo\" = b.\"CustNo\" and a.\"FacmNo\" = b.\"FacmNo\"  " + 
				"left join \"CdEmp\" c on c.\"EmployeeNo\" = a.\"BsOfficer\"  " + 
				"left join \"LoanBorMain\" d on a.\"CustNo\" = d.\"CustNo\" and a.\"FacmNo\" = d.\"FacmNo\" and a.\"BormNo\" = d.\"BormNo\"  " + 
				"left join \"PfBsOfficer\" e on a.\"WorkMonth\" = e.\"WorkMonth\" and a.\"BsOfficer\" = e.\"EmpNo\" where b.\"Status\" != '1'       " + 
				"and a.\"DrawdownDate\" between :StartDate and :EndDate      and a.\"BsOfficer\" is not null and e.\"DepItem\" is not null and e.\"DistItem\" is not null" + 
				" order by a.\"DrawdownDate\" ASC ) y" + 
				" group by y.\"BsOfficer\",y.\"Fullname\",y.\"DistItem\",y.\"DepItem\") k left join (select y.\"BsOfficer\" as \"BsOfficerA\",y.\"Fullname\" as \"FullnameA\",y.\"DistItem\" as \"DistItemA\",y.\"DepItem\" as \"DepItemA\",count(y.\"BsOfficer\") as \"103Total\" from (" + 
				"select " + 
				"a.\"CustNo\", " + 
				"a.\"FacmNo\", " + 
				"a.\"BormNo\", " + 
				"a.\"DrawdownAmt\", " + 
				"a.\"DrawdownDate\", " + 
				"d.\"MaturityDate\", " + 
				"b.\"RepayBank\"," + 
				"a.\"PieceCode\"," + 
				"a.\"BsOfficer\"," + 
				"c.\"Fullname\"," + 
				"e.\"DepItem\"," + 
				"e.\"DistItem\" " + 
				"from \"PfBsDetail\" a  " + 
				"left join \"BankAuthAct\" b on a.\"CustNo\" = b.\"CustNo\" and a.\"FacmNo\" = b.\"FacmNo\"  " + 
				"left join \"CdEmp\" c on c.\"EmployeeNo\" = a.\"BsOfficer\"  " + 
				"left join \"LoanBorMain\" d on a.\"CustNo\" = d.\"CustNo\" and a.\"FacmNo\" = d.\"FacmNo\" and a.\"BormNo\" = d.\"BormNo\"  " + 
				"left join \"PfBsOfficer\" e on a.\"WorkMonth\" = e.\"WorkMonth\" and a.\"BsOfficer\" = e.\"EmpNo\" where b.\"Status\" != '1'       " + 
				"and a.\"DrawdownDate\" between :StartDate and :EndDate      and a.\"BsOfficer\" is not null and e.\"DepItem\" is not null and e.\"DistItem\" is not null and b.\"RepayBank\" = '103'" + 
				" order by a.\"DrawdownDate\" ASC ) y" + 
				" group by y.\"BsOfficer\",y.\"Fullname\",y.\"DistItem\",y.\"DepItem\") j on j.\"BsOfficerA\" = k.\"BsOfficer\" and j.\"DistItemA\" = k.\"DistItem\" and j.\"DepItemA\" = k.\"DepItem\"";
		this.info("sql = " + sql);

		query = em.createNativeQuery(sql);


		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		query.setParameter("StartDate", StartDate);
		query.setParameter("EndDate", EndDate);
		query.setMaxResults(this.limit);
		this.info("L5912Service FindData=" + query.toString());
		return this.convertToMap(query);
	}
}
