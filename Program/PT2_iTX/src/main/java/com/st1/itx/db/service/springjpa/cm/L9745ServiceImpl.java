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
public class L9745ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		Boolean useWorkMonth = parse.stringToInteger(titaVo.getParam("workMonthStart")) > 0;
		Boolean useCustNo = parse.stringToInteger(titaVo.getParam("custNo")) > 0;
		Boolean useFacmNo = parse.stringToInteger(titaVo.getParam("custNo")) > 0 && parse.stringToInteger(titaVo.getParam("facmNo")) > 0;
		String bsOfficer = titaVo.getParam("bsOfficer");
		Boolean useBsOfficer = bsOfficer != null && !bsOfficer.trim().isEmpty();
		int entDy = titaVo.getEntDyI() + 19110000;
		this.info("entDy = " + entDy);

		this.info(String.format("L9745.findAll useWorkMonth:%s useCustNo:%s useFacmNo:%s useBfOfficer:%s", useWorkMonth, useCustNo, useFacmNo, useBsOfficer));

		String sql = "";
		sql += " SELECT E0.\"DepItem\" \"LoanEmpItem\" ";
		sql += "       ,E0.\"Fullname\" \"LoanEmpName\" ";
		sql += "       ,\"Fn_ParseEOL\"(C.\"CustName\", 0) AS \"CustName\" "; // 戶名
		sql += "       ,B.\"CustNo\" \"CustNo\" ";
		sql += "       ,B.\"FacmNo\" \"FacmNo\" ";
		sql += "       ,B.\"BormNo\" \"BormNo\" ";
		sql += "       ,B.\"DrawdownDate\" \"DrawdownDate\" ";
		sql += "       ,B.\"ProdCode\" \"ProdCode\" ";
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
		sql += " WHERE B.\"DrawdownAmt\" > 0 ";
		sql += "   AND B.\"PerfAmt\" > 0 ";
		sql += "   AND B.\"PerfDate\" <= :entDy ";
		if (useWorkMonth) {
			sql += "   AND B.\"WorkMonth\" BETWEEN :workMonthStart AND :workMonthEnd";
		} else {
			sql += "   AND B.\"DrawdownDate\" BETWEEN :drawdownDateStart AND :drawdownDateEnd";
		}
		if (useCustNo) {
			sql += "   AND B.\"CustNo\" = :custNo";
		}
		if (useFacmNo) {
			sql += "   AND B.\"FacmNo\" = :facmNo";
		}
		if (useBsOfficer) {
			sql += "   AND B.\"BsOfficer\" = :bsOfficer";
		}
		sql += " ORDER BY NLSSORT(B.\"BsOfficer\", 'NLS_SORT=EBCDIC') "; // 參考樣張，應是以此欄位排序；NLSSORT by EBCDIC 效果為英文先於數字
		// 2022-08-25大約下午三點,與User珮君電話中討論L9745出表時排序方式,最後決定:房貸專員>戶號>額度>撥款
		sql += "        , \"CustNo\" "; 
		sql += "        , \"FacmNo\" ";
		sql += "        , \"BormNo\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		if (useWorkMonth) {
			query.setParameter("workMonthStart", parse.stringToInteger(titaVo.getParam("workMonthStart")) + 191100);
			query.setParameter("workMonthEnd", parse.stringToInteger(titaVo.getParam("workMonthEnd")) + 191100);
		} else {
			query.setParameter("drawdownDateStart", parse.stringToInteger(titaVo.getParam("drawdownDateStart")) + 19110000);
			query.setParameter("drawdownDateEnd", parse.stringToInteger(titaVo.getParam("drawdownDateEnd")) + 19110000);
		}

		if (useCustNo) {
			query.setParameter("custNo", titaVo.getParam("custNo"));
		}

		if (useFacmNo) {
			query.setParameter("facmNo", titaVo.getParam("facmNo"));
		}

		if (useBsOfficer) {
			query.setParameter("bsOfficer", bsOfficer);
		}

		query.setParameter("entDy", entDy);
			
		return this.convertToMap(query);
	}

}