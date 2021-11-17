package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class LD007ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("lD007.findAll ");

		String sql = "";
		sql += " SELECT E0.\"DepItem\" \"LoanEmpItem\" ";
		sql += "       ,E0.\"Fullname\" \"LoanEmpName\" ";
		sql += "       ,C.\"CustName\" \"CustName\" ";
		sql += "       ,B.\"CustNo\" \"CustNo\" ";
		sql += "       ,B.\"FacmNo\" \"FacmNo\" ";
		sql += "       ,B.\"BormNo\" \"BormNo\" ";
		sql += "       ,B.\"DrawdownDate\" \"DrawdownDate\" ";
		sql += "       ,F.\"ProdNo\" \"ProdNo\" ";
		sql += "       ,B.\"PieceCode\" \"PieceCode\" ";
		sql += "       ,B.\"DrawdownAmt\" \"DrawdownAmt\" ";
		sql += "       ,B.\"WorkMonth\" \"WorkMonth\" ";
		sql += "       ,NVL(NVL(B1.\"DeptItem\", B1.\"UnitItem\"), ' ') AS \"DeptItem\" ";
		sql += "       ,NVL(NVL(B2.\"DistItem\", B2.\"UnitItem\"), ' ') AS \"DistItem\" ";
		sql += "       ,NVL(B3.\"UnitItem\", ' ') AS \"UnitItem\" ";
		sql += "       ,NVL(E1.\"Fullname\", ' ') AS \"IntroducerName\" ";
		sql += "       ,NVL(I.\"Introducer\", ' ') AS \"IntroducerNo\" ";
		sql += " FROM \"PfBsDetail\" B ";
		sql += " LEFT JOIN \"PfItDetail\" I ON I.\"PerfDate\" = B.\"PerfDate\" ";
		sql += "                           AND I.\"CustNo\" = B.\"CustNo\" ";
		sql += "                           AND I.\"FacmNo\" = B.\"FacmNo\" ";
		sql += "                           AND I.\"BormNo\" = B.\"BormNo\" ";
		sql += "                           AND I.\"PieceCode\" = B.\"PieceCode\" ";
		sql += "                           AND I.\"DrawdownAmt\" = B.\"DrawdownAmt\" ";
		sql += "                           AND I.\"RepayType\" = B.\"RepayType\" ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = B.\"CustNo\" ";
		sql += " LEFT JOIN \"PfBsOfficer\" E0 ON E0.\"EmpNo\" = B.\"BsOfficer\" ";
		sql += "                             AND E0.\"DeptCode\" = B.\"DeptCode\" ";
		sql += "                             AND E0.\"WorkMonth\" = B.\"WorkMonth\" ";
		sql += " LEFT JOIN \"CdEmp\" E1 ON E1.\"EmployeeNo\" = I.\"Introducer\" ";
		sql += " LEFT JOIN \"CdBcm\" B1 ON B1.\"UnitCode\" = I.\"DeptCode\" ";
		sql += " LEFT JOIN \"CdBcm\" B2 ON B2.\"UnitCode\" = I.\"DistCode\" ";
		sql += " LEFT JOIN \"CdBcm\" B3 ON B3.\"UnitCode\" = I.\"UnitCode\" ";
		sql += " LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = B.\"CustNo\" ";
		sql += "                        AND F.\"FacmNo\" = B.\"FacmNo\" ";
		sql += " WHERE B.\"WorkMonth\" BETWEEN :workMonthStart AND :workMonthEnd ";
		sql += "   AND B.\"DrawdownAmt\" > 0 ";
		sql += "   AND B.\"PerfAmt\" > 0 ";
		

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("workMonthStart", parse.stringToInteger(titaVo.getParam("workMonthStart")) + 191100);
		query.setParameter("workMonthEnd", parse.stringToInteger(titaVo.getParam("workMonthEnd")) + 191100);

		return this.convertToMap(query);
	}

}