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

@Service
@Repository
/* 逾期放款明細 */
public class LD007ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("lD007.findAll ");

		String sql = "";
		sql += "               SELECT DISTINCT ";
		sql += "                      E0.\"DepItem\" ";
		sql += "                     ,E0.\"Fullname\" ";
		sql += "                     ,C.\"CustName\" ";
		sql += "                     ,B.\"CustNo\" ";
		sql += "                     ,B.\"FacmNo\" ";
		sql += "                     ,B.\"BormNo\" ";
		sql += "                     ,B.\"DrawdownDate\" ";
		sql += "                     ,F.\"ProdNo\" ";
		sql += "                     ,B.\"PieceCode\" ";
		sql += "                     ,B.\"DrawdownAmt\" ";
		sql += "                     ,B.\"WorkMonth\" ";
		sql += "                     ,NVL(NVL(B1.\"DeptItem\", B1.\"UnitItem\"), ' ') AS F1 ";
		sql += "                     ,NVL(NVL(B2.\"DistItem\", B2.\"UnitItem\"), ' ') AS F2 ";
		sql += "                     ,NVL(B3.\"UnitItem\", ' ') AS F3 ";
		sql += "                     ,NVL(E1.\"Fullname\", ' ') AS F4 ";
		sql += "                     ,NVL(I.\"Introducer\", ' ') AS F5 ";
		sql += "               FROM \"PfBsDetail\" B  ";
		sql += "               LEFT JOIN \"PfItDetail\" I ";
		sql += "                     ON I.\"PerfDate\" = B.\"PerfDate\" ";
		sql += "                     AND I.\"CustNo\" = B.\"CustNo\" ";
		sql += "                     AND I.\"FacmNo\" = B.\"FacmNo\" ";
		sql += "                     AND I.\"BormNo\" = B.\"BormNo\" ";
		sql += "               LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = B.\"CustNo\" ";
		sql += "               LEFT JOIN \"PfBsOfficer\" E0 ON E0.\"EmpNo\" = B.\"BsOfficer\" ";
		sql += "                                           AND E0.\"DeptCode\" = B.\"DeptCode\" ";
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

		query.setParameter("inputYearStart", Integer.toString(Integer.valueOf(titaVo.getParam("inputYearStart")) + 1911));
		query.setParameter("inputYearEnd", Integer.toString(Integer.valueOf(titaVo.getParam("inputYearEnd")) + 1911));
		query.setParameter("inputMonthStart", titaVo.getParam("inputMonthStart"));
		query.setParameter("inputMonthEnd", titaVo.getParam("inputMonthEnd"));

		return this.convertToMap(query.getResultList());
	}

}