package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L9750ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	// 會計師查核種類:1.撥款案件-撥款日期 2.展期案件-撥款日期 3.契變案件-會計日期 4.清償案件-入帳日期
	public List<Map<String, String>> findType1(TitaVo titaVo) throws LogicException {
		this.info("L9750ServiceImpl.findType1");
		int startDate = parse.stringToInteger(titaVo.get("DateStart")) + 19110000;
		int endDate = parse.stringToInteger(titaVo.get("DateEnd")) + 19110000;

		this.info("startDate = " + startDate);
		this.info("endDate = " + endDate);

		String sql = "";
		sql += " select lbm.\"CustNo\"       ";// 戶號
		sql += "      , lbm.\"FacmNo\"       ";// 額度編號
		sql += "      , lbm.\"BormNo\"       ";// 撥款序號
		sql += "      , lbm.\"DrawdownDate\" ";// 撥款日期
		sql += "      , lbm.\"DrawdownAmt\"  ";// 撥款金額
		sql += "      , cdl.\"Item\"         ";// 企金別
		sql += "      , fm.\"CompensateFlag\" ";// 代償碼
		sql += " from \"LoanBorMain\" lbm  ";
		sql += " left join \"FacMain\" fm on fm.\"CustNo\" = lbm.\"CustNo\" and fm.\"FacmNo\" = lbm.\"FacmNo\" ";
		sql += " left join \"CdCode\" cdl on cdl.\"DefCode\" = 'DepartmentCode' and cdl.\"Code\" = fm.\"DepartmentCode\" ";
		sql += " where lbm.\"RenewFlag\" = 0 ";
		sql += "   and lbm.\"DrawdownDate\" BETWEEN :inputStartDate AND :inputEndDate ";
		sql += " order by     lbm.\"CustNo\" ,lbm.\"FacmNo\" ,lbm.\"BormNo\" ,lbm.\"DrawdownDate\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputStartDate", startDate);
		query.setParameter("inputEndDate", endDate);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findType2(TitaVo titaVo) throws LogicException {
		this.info("L9750ServiceImpl.findType2");
		int startDate = parse.stringToInteger(titaVo.get("DateStart")) + 19110000;
		int endDate = parse.stringToInteger(titaVo.get("DateEnd")) + 19110000;

		this.info("startDate = " + startDate);
		this.info("endDate = " + endDate);

		String sql = "";
		sql += " WITH rn AS (            ";
		sql += "     select \"CustNo\",\"NewFacmNo\",\"NewBormNo\"        ";
		sql += "     FROM \"AcLoanRenew\"        ";
		sql += "     WHERE \"NewFacmNo\" <> \"OldFacmNo\"        ";
		sql += "       AND \"AcDate\" BETWEEN :inputStartDate AND :inputEndDate      ";
		sql += "     GROUP BY \"CustNo\",\"NewFacmNo\",\"NewBormNo\"        ";
		sql += "     ORDER BY \"CustNo\",\"NewFacmNo\",\"NewBormNo\"        ";
		sql += "  )           ";

		sql += " select lbm.\"CustNo\"       ";// 戶號
		sql += "      , lbm.\"FacmNo\"       ";// 額度編號
		sql += "      , lbm.\"BormNo\"       ";// 撥款序號
		sql += "      , lbm.\"DrawdownDate\" ";// 撥款日期
		sql += "      , lbm.\"DrawdownAmt\"  ";// 撥款金額
		sql += "      , cdl.\"Item\"         ";// 企金別
		sql += "      , fm.\"CompensateFlag\" ";// 代償碼
		sql += " from rn  ";
		sql += " left join \"LoanBorMain\" lbm on lbm.\"CustNo\" = rn.\"CustNo\" ";
		sql += "                              AND lbm.\"FacmNo\" = rn.\"NewFacmNo\" ";
		sql += "                              AND lbm.\"BormNo\" = rn.\"NewBormNo\"";

		sql += " left join \"FacMain\" fm ";
		sql += "             on fm.\"CustNo\" = lbm.\"CustNo\" ";
		sql += "            and fm.\"FacmNo\" = lbm.\"FacmNo\"          ";

		sql += " left join \"CdCode\" cdl ";
		sql += "             on cdl.\"DefCode\" = 'DepartmentCode' ";
		sql += "            and cdl.\"Code\" = fm.\"DepartmentCode\"          ";

		sql += " order by     lbm.\"CustNo\" ,lbm.\"FacmNo\" ,lbm.\"BormNo\" ,lbm.\"DrawdownDate\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputStartDate", startDate);
		query.setParameter("inputEndDate", endDate);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findType3(TitaVo titaVo) throws LogicException {
		this.info("L9750ServiceImpl.findType3");
		int startDate = parse.stringToInteger(titaVo.get("DateStart")) + 19110000;
		int endDate = parse.stringToInteger(titaVo.get("DateEnd")) + 19110000;

		this.info("startDate = " + startDate);
		this.info("endDate = " + endDate);

		String sql = "";
		sql += " select ari.\"CustNo\"       ";// 戶號
		sql += "      , Json_VALUE(ari.\"JsonFields\",'$.ContractChgCode')  as \"ContractChgCode\" ";// 契約變更類型
		sql += "      , cdl.\"Item\"         ";// 契約變更中文
		sql += " from \"AcReceivable\" ari  ";
		sql += " left join \"CdCode\" cdl ";
		sql += "        on cdl.\"DefCode\" = 'ChangeItemCode' ";
		sql += "       and cdl.\"Code\" = Json_VALUE(ari.\"JsonFields\",'$.ContractChgCode') ";
		sql += " where ari.\"AcctCode\"= 'F29' ";
		sql += "   and ari.\"OpenAcDate\" BETWEEN :inputStartDate AND :inputEndDate ";
		sql += "   and Json_VALUE(ari.\"JsonFields\",'$.ContractChgCode') = '01' ";
		sql += " order by ari.\"CustNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputStartDate", startDate);
		query.setParameter("inputEndDate", endDate);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findType4(TitaVo titaVo) throws LogicException {
		this.info("L9750ServiceImpl.findType4");
		int startDate = parse.stringToInteger(titaVo.get("DateStart")) + 19110000;
		int endDate = parse.stringToInteger(titaVo.get("DateEnd")) + 19110000;

		this.info("startDate = " + startDate);
		this.info("endDate = " + endDate);

		String sql = "";
		sql += " select fac.\"CustNo\"          ";// 戶號
		sql += "      , fac.\"FacmNo\"          ";// 額度編號
		sql += "      , fac.\"EntryDate\"       ";// 入帳日
		sql += "      , fac.\"CloseReasonCode\" ";// 提前清償原因
		sql += "      , cdl.\"Item\"            ";// 提前清償原因中文
		sql += " from \"FacClose\" fac  ";
		sql += " left join \"CdCode\" cdl on cdl.\"DefCode\" = 'AdvanceCloseCode' and cdl.\"Code\" = fac.\"CloseReasonCode\" ";
		sql += " where fac.\"CloseDate\" > 0 ";
		sql += "    and fac.\"EntryDate\" BETWEEN :inputStartDate AND :inputEndDate ";
		sql += " order by fac.\"CustNo\" ,fac.\"FacmNo\" ,fac.\"EntryDate\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputStartDate", startDate);
		query.setParameter("inputEndDate", endDate);

		return this.convertToMap(query);
	}

}