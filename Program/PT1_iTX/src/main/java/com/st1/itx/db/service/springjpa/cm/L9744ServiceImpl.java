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
public class L9744ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		// parse.stringToInteger handles null as 0
		Boolean useWorkMonth = parse.stringToInteger(titaVo.getParam("workMonthStart")) > 0;
		Boolean useCustNo = parse.stringToInteger(titaVo.getParam("custNo")) > 0;
		Boolean useFacmNo = parse.stringToInteger(titaVo.getParam("custNo")) > 0
				&& parse.stringToInteger(titaVo.getParam("facmNo")) > 0;
		String introducer = titaVo.getParam("Introducer");
		Boolean useIntroducer = introducer != null && !introducer.trim().isEmpty();
		int entDy = titaVo.getEntDyI() + 19110000;
		this.info("entDy = " + entDy);

		this.info(String.format("L9744.findAll useWorkMonth:%s useCustNo:%s useFacmNo:%s useIntroducer:%s",
				useWorkMonth, useCustNo, useFacmNo, useIntroducer));
		// check titaVo for input values

		String sql = "";
		sql += " SELECT B0.\"UnitItem\" AS \"BsDeptItem\" "; // F0 部室中文(房貸專員)
		sql += "       ,E0.\"Fullname\" AS \"BsName\" "; // F1 房貸專員姓名
		sql += "       ,\"Fn_ParseEOL\"(C.\"CustName\", 0) AS \"CustName\" "; // F2 戶名
		sql += "       ,I.\"CustNo\""; // F3 戶號
		sql += "       ,I.\"FacmNo\""; // F4 額度號碼
		sql += "       ,I.\"BormNo\""; // F5 撥款序號
		sql += "       ,I.\"DrawdownDate\""; // F6 撥款日
		sql += "       ,I.\"ProdCode\""; // F7 商品代碼
		sql += "       ,I.\"PieceCode\""; // F8 計件代碼
		sql += "       ,I.\"CntingCode\""; // F9 是否計件
		sql += "       ,I.\"DrawdownAmt\""; // F10 撥款金額
		sql += "       ,NVL(I.\"DeptCode\", ' ') AS \"DeptCode\""; // F11 部室代號(介紹人)
		sql += "       ,NVL(I.\"DistCode\", ' ') AS \"DistCode\""; // F12 區部代號(介紹人)
		sql += "       ,NVL(I.\"UnitCode\", ' ') AS \"UnitCode\""; // F13 單位代號(介紹人)
		sql += "       ,NVL(B2.\"UnitItem\", ' ') AS \"ItDeptItem\""; // F14 部室中文(介紹人)
		sql += "       ,NVL(B3.\"UnitItem\", ' ') AS \"ItDistItem\""; // F15 區部中文(介紹人)
		sql += "       ,NVL(B1.\"UnitItem\", ' ') AS \"ItUnitIem\""; // F16 單位中文(介紹人)
		sql += "       ,NVL(I.\"Introducer\", ' ') AS \"Introducer\""; // F17 員工代號(介紹人)
		sql += "       ,NVL(E1.\"Fullname\", ' ') AS \"ItName\""; // F18 介紹人姓名
		sql += "       ,NVL(E2.\"Fullname\", ' ') AS \"ItUnitManager\""; // F19 處經理姓名(介紹人)
		sql += "       ,NVL(E3.\"Fullname\", ' ') AS \"ItDistManager\""; // F20 區經理姓名(介紹人)
		sql += "       ,I.\"PerfEqAmt\" "; // F21 換算業績 -- 20211201 此三金額修改為撥款層而非額度層總計
		sql += "       ,I.\"PerfReward\" "; // F22 業務報酬 -- 參考L5051
		sql += "       ,I.\"PerfAmt\" "; // F23 業績金額
		sql += " FROM \"PfItDetail\" I";
		sql += " LEFT JOIN \"PfBsDetail\" B ON B.\"PerfDate\"    = I.\"PerfDate\""; // 取房貸專員
		sql += "                           AND B.\"CustNo\"      = I.\"CustNo\"";
		sql += "                           AND B.\"FacmNo\"      = I.\"FacmNo\"";
		sql += "                           AND B.\"BormNo\"      = I.\"BormNo\"";
		sql += "                           AND B.\"PieceCode\"   = I.\"PieceCode\"";
		sql += "                           AND B.\"DrawdownAmt\" = I.\"DrawdownAmt\"";
		sql += "                           AND B.\"RepayType\"   = I.\"RepayType\" ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = I.\"CustNo\"";
		sql += " LEFT JOIN \"CdEmp\" E0 ON E0.\"EmployeeNo\" = B.\"BsOfficer\""; // 取房貸專員姓名
		sql += " LEFT JOIN \"CdEmp\" E1 ON E1.\"EmployeeNo\" = I.\"Introducer\""; // 取介紹人姓名
		sql += " LEFT JOIN \"CdEmp\" E2 ON E2.\"EmployeeNo\" = I.\"UnitManager\""; // 取處經理姓名(介紹人)
		sql += " LEFT JOIN \"CdEmp\" E3 ON E3.\"EmployeeNo\" = I.\"DistManager\""; // 取區經理姓名(介紹人)
		sql += " LEFT JOIN \"CdBcm\" B0 ON B0.\"UnitCode\" = B.\"DeptCode\" "; // 取部室中文(房貸專員)
		sql += " LEFT JOIN \"CdBcm\" B1 ON B1.\"UnitCode\" = I.\"UnitCode\""; // 取單位中文(介紹人)
		sql += " LEFT JOIN \"CdBcm\" B2 ON B2.\"UnitCode\" = I.\"DeptCode\""; // 取部室中文(介紹人)
		sql += " LEFT JOIN \"CdBcm\" B3 ON B3.\"UnitCode\" = I.\"DistCode\""; // 取區部中文(介紹人)
		sql += " WHERE I.\"DrawdownAmt\" > 0 ";
		sql += "   AND I.\"PerfDate\" <= :entDy ";
		sql += "   AND I.\"Introducer\" IS NOT NULL ";
		sql += "   AND ABS(NVL(PIDA.\"AdjPerfEqAmt\", I.\"PerfEqAmt\")) ";
		sql += "       + ABS(NVL(PIDA.\"AdjPerfReward\", I.\"PerfReward\")) ";
		sql += "       + ABS(NVL(PIDA.\"AdjPerfAmt\", I.\"PerfAmt\")) > 0 ";
		if (useWorkMonth) {
			sql += "   AND I.\"WorkMonth\" BETWEEN :workMonthStart AND :workMonthEnd";
		} else {
			sql += "   AND I.\"DrawdownDate\" BETWEEN :drawdownDateStart AND :drawdownDateEnd";
		}
		if (useCustNo) {
			sql += "   AND I.\"CustNo\" = :custNo";
		}
		if (useFacmNo) {
			sql += "   AND I.\"FacmNo\" = :facmNo";
		}
		if (useIntroducer) {
			sql += "   AND I.\"Introducer\" = :introducer";
		}
		sql += " ORDER BY \"BsDeptItem\" ";
		sql += "        , \"BsName\" ";
		sql += "        , I.\"CustNo\" ";
		sql += "        , I.\"FacmNo\" ";
		sql += "        , I.\"BormNo\" ";
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		if (useWorkMonth) {
			query.setParameter("workMonthStart", parse.stringToInteger(titaVo.getParam("workMonthStart")) + 191100);
			query.setParameter("workMonthEnd", parse.stringToInteger(titaVo.getParam("workMonthEnd")) + 191100);
		} else {
			query.setParameter("drawdownDateStart",
					parse.stringToInteger(titaVo.getParam("drawdownDateStart")) + 19110000);
			query.setParameter("drawdownDateEnd", parse.stringToInteger(titaVo.getParam("drawdownDateEnd")) + 19110000);
		}

		if (useCustNo) {
			query.setParameter("custNo", titaVo.getParam("custNo"));
		}

		if (useFacmNo) {
			query.setParameter("facmNo", titaVo.getParam("facmNo"));
		}

		if (useIntroducer) {
			query.setParameter("introducer", introducer);
		}
		
		query.setParameter("entDy", entDy);

		return this.convertToMap(query);
	}

}