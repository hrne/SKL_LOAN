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

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {		
		Boolean useWorkMonth = parse.stringToInteger(titaVo.getParam("workMonthStart")) > 0;
		Boolean useCustNo = parse.stringToInteger(titaVo.getParam("custNo")) > 0;
		Boolean useFacmNo = parse.stringToInteger(titaVo.getParam("custNo")) > 0 && parse.stringToInteger(titaVo.getParam("facmNo")) > 0;
		Boolean useIntroducer = !titaVo.getParam("Introducer").trim().isEmpty();
		
		this.info(String.format("lD006.findAll useWorkMonth:%s useCustNo:%s useFacmNo:%s useIntroducer:%s", useWorkMonth, useCustNo, useFacmNo, useIntroducer));
		// check titaVo for input values
		
		String sql = "";
		sql += " SELECT B0.\"UnitItem\" AS \"BsDeptItem\" "; // 部室中文(房貸專員)
		sql += "       ,E0.\"Fullname\" AS \"BsName\" "; // 房貸專員姓名
		sql += "       ,\"Fn_MaskName\"(C.\"CustName\") AS \"CustName\" "; // 戶名
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
		sql += "       ,NVL(PIDA.\"AdjPerfEqAmt\", IGroup.\"PerfEqAmt\") AS \"PerfEqAmt\" "; // 換算業績 - 參考樣張輸出, 同額度的三項業績金額為 Group By FacmNo
		sql += "       ,NVL(PIDA.\"AdjPerfReward\", IGroup.\"PerfReward\") AS \"PerfReward\" "; // 業務報酬 因此 IGroup 以 FacmNo 作 Group
		sql += "       ,NVL(PIDA.\"AdjPerfAmt\", IGroup.\"PerfAmt\") AS \"PerfAmt\" "; // 業績金額 PIDA 只到額度層
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
		sql += " LEFT JOIN ( SELECT PID.\"CustNo\" ";
		sql += "                   ,PID.\"FacmNo\" ";
		sql += "                   ,PID.\"WorkMonth\" ";
		sql += "                   ,SUM(PID.\"PerfEqAmt\") \"PerfEqAmt\" ";
		sql += "                   ,SUM(PID.\"PerfReward\") \"PerfReward\" ";
		sql += "                   ,SUM(PID.\"PerfAmt\") \"PerfAmt\" ";
		sql += "             FROM \"PfItDetail\" PID";
		sql += "             GROUP BY PID.\"CustNo\" ";
		sql += "                     ,PID.\"FacmNo\" ";
		sql += "                     ,PID.\"WorkMonth\" ";
		sql += "           ) IGroup ON IGroup.\"CustNo\" = I.\"CustNo\" ";
		sql += "                   AND IGroup.\"FacmNo\" = I.\"FacmNo\" ";
		sql += "                   AND IGroup.\"WorkMonth\" = I.\"WorkMonth\" ";
		sql += " LEFT JOIN \"PfItDetailAdjust\" PIDA ON PIDA.\"CustNo\"    = I.\"CustNo\"     ";
		sql += "                                    AND PIDA.\"FacmNo\"    = I.\"FacmNo\"     ";
		sql += "                                    AND PIDA.\"WorkMonth\" = I.\"WorkMonth\"  ";
		sql += " WHERE I.\"DrawdownAmt\" > 0 ";
		sql += "   AND I.\"Introducer\" IS NOT NULL"; // PfItDetail 存在介紹人為 Null 的資料
		if (useWorkMonth)
		{
			sql += "   AND I.\"WorkMonth\" BETWEEN :workMonthStart AND :workMonthEnd";
		} else
		{
			sql += "   AND I.\"DrawdownDate\" BETWEEN :perfDateStart AND :perfDateEnd";
		}
		if (useCustNo)
		{
			sql += "   AND I.\"CustNo\" = :custNo";
		}
		if (useFacmNo)
		{
			sql += "   AND I.\"CustNo\" = :facmNo";
		}
		if (useIntroducer)
		{
			sql += "   AND I.\"Introducer\" = :introducer";
		}
		sql += " ORDER BY NLSSORT(I.\"DeptCode\", 'NLS_SORT=FRENCH') ";
		sql += "         ,NLSSORT(I.\"DistCode\", 'NLS_SORT=FRENCH') ";
		sql += "         ,NLSSORT(I.\"UnitCode\", 'NLS_SORT=FRENCH') "; // 原表排序用到這三個欄位, 但原環境會將英文排在數字前面;
		sql += "         ,I.\"CustNo\" "; // 這裡利用 NLSSORT 取法語環境排序方式, 確保排序順序上英文先於數字
		sql += "         ,I.\"FacmNo\" ";
		sql += "         ,I.\"BormNo\" ";
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		if (useWorkMonth)
		{
		query.setParameter("workMonthStart", parse.stringToInteger(titaVo.getParam("workMonthStart")) + 191100);
		query.setParameter("workMonthEnd", parse.stringToInteger(titaVo.getParam("workMonthEnd")) + 191100);
		} else
		{
		query.setParameter("perfDateStart", parse.stringToInteger(titaVo.getParam("perfDateStart")) + 19110000);
		query.setParameter("perfDateEnd", parse.stringToInteger(titaVo.getParam("perfDateEnd")) + 19110000);
		}
		
		if (useCustNo)
		{
			query.setParameter("custNo", titaVo.getParam("custNo"));
		}
		
		if (useFacmNo)
		{
			query.setParameter("facmNo", titaVo.getParam("facmNo"));
		}
		
		if (useIntroducer)
		{
			query.setParameter("introducer", titaVo.getParam("Introducer"));
		}
		
		return this.convertToMap(query);
	}

}