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
		sql += "               SELECT ";
		sql += "                      E0.\"DepItem\" F0 ";
		sql += "                     ,E0.\"Fullname\" F1 ";
		sql += "                     ,C.\"CustName\" F2 ";
		sql += "                     ,B.\"CustNo\" F3 ";
		sql += "                     ,B.\"FacmNo\" F4 ";
		sql += "                     ,B.\"BormNo\" F5 ";
		sql += "                     ,B.\"DrawdownDate\" F6 ";
		sql += "                     ,F.\"ProdNo\" F7 ";
		sql += "                     ,B.\"PieceCode\" F8 ";
		sql += "                     ,B.\"DrawdownAmt\" F9 ";
		sql += "                     ,B.\"WorkMonth\" F10 ";
		sql += "                     ,NVL(NVL(B1.\"DeptItem\", B1.\"UnitItem\"), ' ') AS F11 ";
		sql += "                     ,NVL(NVL(B2.\"DistItem\", B2.\"UnitItem\"), ' ') AS F12 ";
		sql += "                     ,NVL(B3.\"UnitItem\", ' ') AS F13 ";
		sql += "                     ,NVL(E1.\"Fullname\", ' ') AS F14 ";
		sql += "                     ,NVL(I.\"Introducer\", ' ') AS F15 ";
		sql += "               FROM \"PfBsDetail\" B  ";
		sql += "               LEFT JOIN \"PfItDetail\" I ";
		sql += "                     ON I.\"PerfDate\" = B.\"PerfDate\" ";
		sql += "                     AND I.\"CustNo\" = B.\"CustNo\" ";
		sql += "                     AND I.\"FacmNo\" = B.\"FacmNo\" ";
		sql += "                     AND I.\"BormNo\" = B.\"BormNo\" ";
		sql += "               LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = B.\"CustNo\" ";
		sql += "               LEFT JOIN \"PfBsOfficer\" E0 ON E0.\"EmpNo\" = B.\"BsOfficer\" ";
		sql += "                                           AND E0.\"DeptCode\" = B.\"DeptCode\" ";
		sql += "                                           AND E0.\"WorkMonth\" = B.\"WorkMonth\" ";
		sql += "               LEFT JOIN \"CdEmp\" E1 ON E1.\"EmployeeNo\" = I.\"Introducer\" ";
		sql += "               LEFT JOIN \"CdBcm\" B1 ON B1.\"UnitCode\" = I.\"DeptCode\" ";
		sql += "               LEFT JOIN \"CdBcm\" B2 ON B2.\"UnitCode\" = I.\"DistCode\"  ";
		sql += "               LEFT JOIN \"CdBcm\" B3 ON B3.\"UnitCode\" = I.\"UnitCode\" ";
		sql += "               LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = B.\"CustNo\" ";
		sql += "                                      AND F.\"FacmNo\" = B.\"FacmNo\" ";
		sql += "               WHERE B.\"WorkMonth\" >= (:inputYearStart || :inputMonthStart) ";
		sql += "                 AND B.\"WorkMonth\" <= (:inputYearEnd || :inputMonthEnd) ";
		sql += "                 AND B.\"DrawdownAmt\" > 0 ";
		sql += "                 AND B.\"PerfAmt\" > 0 ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYearStart", parse.stringToInteger(titaVo.getParam("inputYearStart")) + 1911);
		query.setParameter("inputYearEnd", parse.stringToInteger(titaVo.getParam("inputYearEnd")) + 1911);
		query.setParameter("inputMonthStart", titaVo.getParam("inputMonthStart"));
		query.setParameter("inputMonthEnd", titaVo.getParam("inputMonthEnd"));

		return this.convertToMap(query);
	}

}