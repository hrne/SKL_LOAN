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
public class LD006ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int entDy, String workSeason, TitaVo titaVo) throws Exception {
		this.info("lD006.findAll entDy = " + entDy + " , workSeason = " + workSeason);

		String sql = "";
		sql += " SELECT B0.\"UnitItem\" AS \"BsDeptItem\""; // 部室中文(房貸專員)
		sql += "       ,E0.\"Fullname\" AS \"BsName\""; // 房貸專員姓名
		sql += "       ,C.\"CustName\""; // 戶名
		sql += "       ,I.\"CustNo\""; // 戶號
		sql += "       ,I.\"FacmNo\""; // 額度號碼
		sql += "       ,I.\"BormNo\""; // 撥款序號
		sql += "       ,I.\"DrawdownDate\""; // 撥款日
		sql += "       ,I.\"ProdCode\""; // 商品代碼
		sql += "       ,I.\"PieceCode\""; // 計件代碼
		sql += "       ,I.\"CntingCode\""; // 是否計件
		sql += "       ,I.\"DrawdownAmt\""; // 撥款金額
		sql += "       ,NVL(I.\"DeptCode\", ' ') AS \"DeptCode\""; // 部室代號(介紹人)
		sql += "       ,NVL(I.\"DistCode\", ' ') AS \"DistCode\""; // 區部代號(介紹人)
		sql += "       ,NVL(I.\"UnitCode\", ' ') AS \"UnitCode\""; // 單位代號(介紹人)
		sql += "       ,NVL(B2.\"UnitItem\", ' ') AS \"ItDeptItem\""; // 部室中文(介紹人)
		sql += "       ,NVL(B3.\"UnitItem\", ' ') AS \"ItDistItem\""; // 區部中文(介紹人)
		sql += "       ,NVL(B1.\"UnitItem\", ' ') AS \"ItUnitIem\""; // 單位中文(介紹人)
		sql += "       ,NVL(I.\"Introducer\", ' ') AS \"Introducer\""; // 員工代號(介紹人)
		sql += "       ,NVL(E1.\"Fullname\", ' ') AS \"ItName\""; // 介紹人姓名
		sql += "       ,NVL(E2.\"Fullname\", ' ') AS \"ItUnitManager\""; // 處經理姓名(介紹人)
		sql += "       ,NVL(E3.\"Fullname\", ' ') AS \"ItDistManager\""; // 區經理姓名(介紹人)
		sql += "       ,IGroup.\"PerfEqAmt\""; // 換算業績
		sql += "       ,IGroup.\"PerfReward\""; // 業務報酬
		sql += "       ,IGroup.\"PerfAmt\""; // 業績金額
		sql += " FROM \"PfItDetail\" I";
		sql += " LEFT JOIN \"PfBsDetail\" B ON B.\"PerfDate\" = I.\"PerfDate\""; // 取房貸專員
		sql += "                           AND B.\"CustNo\"   = I.\"CustNo\"";
		sql += "                           AND B.\"FacmNo\"   = I.\"FacmNo\"";
		sql += "                           AND B.\"BormNo\"   = I.\"BormNo\"";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = I.\"CustNo\"";
		sql += " LEFT JOIN \"CdEmp\" E0 ON E0.\"EmployeeNo\" = B.\"BsOfficer\""; // 取房貸專員姓名
		sql += " LEFT JOIN \"CdEmp\" E1 ON E1.\"EmployeeNo\" = I.\"Introducer\""; // 取介紹人姓名
		sql += " LEFT JOIN \"CdEmp\" E2 ON E2.\"EmployeeNo\" = I.\"UnitManager\""; // 取處經理姓名(介紹人)
		sql += " LEFT JOIN \"CdEmp\" E3 ON E3.\"EmployeeNo\" = I.\"DistManager\""; // 取區經理姓名(介紹人)
		sql += " LEFT JOIN \"CdBcm\" B0 ON B0.\"UnitCode\" = B.\"DeptCode\" "; // 取部室中文(房貸專員)
		sql += " LEFT JOIN \"CdBcm\" B1 ON B1.\"UnitCode\" = I.\"UnitCode\""; // 取單位中文(介紹人)
		sql += " LEFT JOIN \"CdBcm\" B2 ON B2.\"UnitCode\" = I.\"DeptCode\""; // 取部室中文(介紹人)
		sql += " LEFT JOIN \"CdBcm\" B3 ON B3.\"UnitCode\" = I.\"DistCode\""; // 取區部中文(介紹人)
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                   ,\"FacmNo\" ";
		sql += "                   ,\"WorkMonth\" ";
		sql += "                   ,SUM(\"PerfEqAmt\") \"PerfEqAmt\" ";
		sql += "                   ,SUM(\"PerfReward\") \"PerfReward\" ";
		sql += "                   ,SUM(\"PerfAmt\") \"PerfAmt\" ";
		sql += "             FROM \"PfItDetail\" ";
		sql += "             GROUP BY \"CustNo\" ";
		sql += "                     ,\"FacmNo\" ";
		sql += "                     ,\"WorkMonth\" ";
		sql += "           ) IGroup ON IGroup.\"CustNo\" = I.\"CustNo\" ";
		sql += "                   AND IGroup.\"FacmNo\" = I.\"FacmNo\" ";
		sql += "                   AND IGroup.\"WorkMonth\" = I.\"WorkMonth\" ";
		sql += " WHERE I.\"WorkMonth\" >= (:inputYearStart || :inputMonthStart)";
		sql += "   AND I.\"WorkMonth\" <= (:inputYearEnd || :inputMonthEnd)";
		sql += "   AND B0.\"UnitItem\" IS NOT NULL ";
		sql += "   AND I.\"DrawdownAmt\" >= 0 ";
		sql += "   AND (I.\"DeptCode\" IS NOT NULL OR I.\"Introducer\" IS NOT NULL) ";
		sql += " ORDER BY B.\"BsOfficer\", I.\"UnitManager\", I.\"CustNo\", I.\"FacmNo\", I.\"BormNo\"";
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