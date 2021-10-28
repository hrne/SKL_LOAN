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
public class LM011ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Autowired
	Parse parse;

	public List<Map<String, String>> findAll(int bcYear, int drawdownFg, TitaVo titaVo) throws Exception {
		this.info("lM011.findAll ");

		String entdy = parse.IntegerToString(bcYear / 100, 1);

		String sql = "SELECT I.\"CustNo\"";
		sql += "            ,I.\"FacmNo\"";
		sql += "            ,I.\"ApplNo\"";
		sql += "            ,I.\"ApproveDate\"";
		sql += "            ,I.\"FirstDrawdownDate\"";
		sql += "            ,I.\"MaturityDate\"";
		sql += "            ,I.\"LoanTermYy\"";
		sql += "            ,I.\"LoanTermMm\"";
		sql += "            ,I.\"LoanTermDd\"";
		sql += "            ,I.\"UtilDeadline\"";
		sql += "            ,I.\"RecycleDeadline\"";
		sql += "            ,I.\"LineAmt\"";
		sql += "            ,I.\"UtilBal\"";
		sql += "            ,I.\"AvblBal\"";
		sql += "            ,I.\"RecycleCode\"";
		sql += "            ,I.\"IrrevocableFlag\"";
		sql += "            ,I.\"AcSubBookCode\"";
		sql += "            ,I.\"Ccf\"";
		sql += "            ,I.\"ExpLimitAmt\"";
		sql += "            ,I.\"DbAcNoCode\"";
		sql += "            ,I.\"CrAcNoCode\"";
		sql += "            ,1";
		sql += "      FROM \"Ias39LoanCommit\" I";
		sql += "      WHERE I.\"DataYm\" = :entdy";
		sql += "        AND I.\"DrawdownFg\" = :drawdownFg";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		query.setParameter("drawdownFg", drawdownFg);
		return this.convertToMap(query);
	}

}