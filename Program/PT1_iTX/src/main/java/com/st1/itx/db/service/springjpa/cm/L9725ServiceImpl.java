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
public class L9725ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("l9725.findAll ");

		String sql = "SELECT L.\"CustNo\" AS F0";
		sql += "            ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS F1";
		sql += "            ,L.\"LoanBal\" AS F2";
		sql += "            ,C.\"EntCode\" AS F3";
		sql += "            ,C.\"AMLJobCode\" AS F4";
		sql += "            ,C.\"AMLGroup\" AS F5";
		sql += "      FROM ( SELECT \"CustNo\"";
		sql += "                   ,SUM(\"LoanBal\") AS \"LoanBal\"";
		sql += "             FROM \"LoanBorMain\"";
		sql += "             WHERE \"Status\" IN (0,2,4,7)";
		sql += "             GROUP BY \"CustNo\"";
		sql += "           ) L";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = L.\"CustNo\"";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);

		return this.convertToMap(query.getResultList());
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll2(TitaVo titaVo) throws Exception {
		this.info("l9725.findAll2 ");
		int dateInputEnd = (Integer.valueOf(titaVo.getParam("DateInputEnd"))+19110000)/100;
		this.info("dateInputEnd =  "+ dateInputEnd);
		String sql = "SELECT ml.\"CustNo\" AS \"CustNo\"";
		sql += "            ,ml.\"FacmNo\" AS \"FacmNo\"";
		sql += "            ,ml.\"BormNo\" AS \"BormNo\"";
		sql += "            ,fm.\"ProdNo\" AS \"ProdNo\"";
		sql += "            ,lb.\"DrawdownDate\" AS \"DrawdownDate\"";
		sql += "            ,lb.\"MaturityDate\" AS \"MaturityDate\"";
		sql += "            ,fm.\"LineAmt\" AS \"LineAmt\"";
		sql += "            ,lb.\"LoanBal\" AS \"LoanBal\"";
		sql += "            ,lb.\"PieceCode\" AS \"PieceCode\"";
		sql += "            ,cm.\"CuscCd\" AS \"CuscCd\"";
		sql += "            ,cm.\"EntCode\" AS \"EntCode\"";	
		sql += "      FROM  \"MonthlyLoanBal\" ml";
		sql += "      LEFT JOIN \"LoanBorMain\" lb ON lb.\"CustNo\" = ml.\"CustNo\"  ";
		sql += "                                  AND lb.\"FacmNo\" = ml.\"FacmNo\"  ";
		sql += "                                  AND lb.\"BormNo\" = ml.\"BormNo\"  ";
		sql += "      LEFT JOIN \"FacMain\" fm ON fm.\"CustNo\" = ml.\"CustNo\"  ";
		sql += "                                  AND fm.\"FacmNo\" = ml.\"FacmNo\"  ";
		sql += "      LEFT JOIN \"CustMain\" cm ON cm.\"CustNo\" = ml.\"CustNo\"  ";
		sql += "      WHERE ml.\"YearMonth\"=:DateInputEnd";
		sql += "        AND ml.\"LoanBalance\">0";
		sql += "      ORDER BY \"CustNo\",\"FacmNo\",\"BormNo\"";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("DateInputEnd", dateInputEnd);

		return this.convertToMap(query.getResultList());
	}

}