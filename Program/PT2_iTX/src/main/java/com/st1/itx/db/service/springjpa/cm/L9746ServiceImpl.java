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
public class L9746ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("L9746ServiceImpl findAll");

		int inputWorkMonth = Integer.parseInt(titaVo.getParam("inputWorkMonth")) + 191100;

		this.info("inputWorkMonth = " + inputWorkMonth);

		String sql = "";
		sql += " WITH S0 AS ( ";
		sql += "   SELECT PI.\"Introducer\"    AS Introducer "; // -- 介紹人員編
		sql += "        , \"Fn_GetEmpName\"(PI.\"Introducer\",0) ";
		sql += "                               AS EmpName "; // -- 介紹人姓名
		sql += "        , IE.\"AgentId\"       AS AgentId "; // -- 介紹人ID
		sql += "        , PI.\"CustNo\"        AS CustNo "; // -- 戶號
		sql += "        , PI.\"FacmNo\"        AS FacmNo "; // -- 額度
		sql += "        , CM.\"CustName\"      AS CustName "; // -- 戶名
		sql += "        , PI.\"DrawdownAmt\"   AS DrawdownAmt "; // -- 撥款金額
		sql += "        , PI.\"PieceCode\"     AS PieceCode "; // -- 計件代碼
		sql += "        , PI.\"CntingCode\"    AS CntingCode "; // -- 是否計件
		sql += "        , CB1.\"UnitItem\"     AS DeptItem "; // -- 部室
		sql += "        , CB2.\"UnitItem\"     AS DistItem "; // -- 區部
		sql += "        , CB3.\"UnitItem\"     AS UnitItem "; // -- 單位
		sql += "        , PI.\"ProdCode\"      AS ProdCode "; // -- 商品代碼
		sql += "        , PI.\"PerfEqAmt\"     AS PerfEqAmt "; // -- 三階換算業績
		sql += "        , PI.\"PerfReward\"    AS PerfReward "; // -- 三階業務報酬
		sql += "        , IE.\"AgType1\"       AS AgType1 "; // -- 制度別
		sql += "        , IE.\"AgLevelSys\"    AS AgLevelSys "; // -- 業務制度別
		sql += "   FROM \"PfItDetail\" PI ";
		sql += "   LEFT JOIN \"CdEmp\" IE ON IE.\"EmployeeNo\" = PI.\"Introducer\" ";
		sql += "   LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = PI.\"CustNo\" ";
		sql += "   LEFT JOIN \"CdBcm\" CB1 ON CB1.\"UnitCode\" = PI.\"DeptCode\" ";
		sql += "   LEFT JOIN \"CdBcm\" CB2 ON CB2.\"UnitCode\" = PI.\"DistCode\" ";
		sql += "   LEFT JOIN \"CdBcm\" CB3 ON CB3.\"UnitCode\" = PI.\"UnitCode\" ";
		sql += "   WHERE PI.\"WorkMonth\" = :inputWorkMonth ";
		sql += "     AND PI.\"RepayType\" IN (0, 4) ";
		sql += "     AND (PI.\"PerfEqAmt\" != 0 OR PI.\"PerfReward\" != 0) ";
		sql += " ) ";
		sql += " , S1 AS ( ";
		sql += " SELECT MAX(Introducer)        AS Introducer "; // -- 介紹人員編
		sql += "      , MAX(EmpName)           AS EmpName "; // -- 介紹人姓名
		sql += "      , MAX(AgentId)           AS AgentId "; // -- 介紹人ID
		sql += "      , CustNo                 AS CustNo "; // -- 戶號
		sql += "      , FacmNo                 AS FacmNo "; // -- 額度
		sql += "      , MAX(CustName)          AS CustName "; // -- 戶名
		sql += "      , SUM(DrawdownAmt)       AS DrawdownAmt "; // -- 撥款金額
		sql += "      , MAX(PieceCode)         AS PieceCode "; // -- 計件代碼
		sql += "      , MAX(CntingCode)        AS CntingCode "; // -- 是否計件
		sql += "      , MAX(DeptItem)          AS DeptItem "; // -- 部室
		sql += "      , MAX(DistItem)          AS DistItem "; // -- 區部
		sql += "      , MAX(UnitItem)          AS UnitItem "; // -- 單位
		sql += "      , MAX(ProdCode)          AS ProdCode "; // -- 商品代碼
		sql += "      , SUM(PerfEqAmt)         AS PerfEqAmt "; // -- 三階換算業績
		sql += "      , SUM(PerfReward)        AS PerfReward "; // -- 三階業務報酬
		sql += "      , MAX(AgType1)           AS AgType1 "; // -- 制度別
		sql += "      , MAX(AgLevelSys)        AS AgLevelSys "; // -- 業務制度別
		sql += " FROM S0 ";
		sql += " GROUP BY S0.CustNo ";
		sql += "        , S0.FacmNo ";
		sql += " ) ";
		sql += " SELECT Introducer             AS F0 "; // -- 介紹人員編
		sql += "      , EmpName                AS F1 "; // -- 介紹人姓名
		sql += "      , AgentId                AS F2 "; // -- 介紹人ID
		sql += "      , LPAD(CustNo,7,'0') ";
		sql += "        || '-' ";
		sql += "        || LPAD(FacmNo,3,'0')  AS F3 "; // -- 戶號-額度
		sql += "      , CustName               AS F4 "; // -- 戶名
		sql += "      , DrawdownAmt            AS F5 "; // -- 撥款金額
		sql += "      , PieceCode              AS F6 "; // -- 計件代碼
		sql += "      , CntingCode             AS F7 "; // -- 是否計件
		sql += "      , DeptItem               AS F8 "; // -- 部室
		sql += "      , DistItem               AS F9 "; // -- 區部
		sql += "      , UnitItem               AS F10 "; // -- 單位
		sql += "      , ProdCode               AS F11 "; // -- 商品代碼
		sql += "      , PerfEqAmt              AS F12 "; // -- 三階換算業績
		sql += "      , PerfReward             AS F13 "; // -- 三階業務報酬
		sql += "      , AgType1                AS F14 "; // -- 制度別
		sql += "      , AgLevelSys             AS F15 "; // -- 業務制度別
		sql += " FROM S1 ";
		sql += " ORDER BY F0 ";
		sql += "        , F3 ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputWorkMonth", inputWorkMonth);

		return this.convertToMap(query);
	}

}