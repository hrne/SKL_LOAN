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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2077ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L2077ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L2077.findAll");

		// tita
		int iEntryDate = parse.stringToInteger(titaVo.getParam("TranDate")) + 19110000;
		int iApplDate = parse.stringToInteger(titaVo.getParam("ApplDate")) + 19110000;
		int iCustNoS = parse.stringToInteger(titaVo.getParam("CustNoS"));
		int iCustNoE = parse.stringToInteger(titaVo.getParam("CustNoE"));

		String sql = " SELECT f.* ";
		sql += "FROM (SELECT \"CustNo\",\"FacmNo\",MAX(\"CloseNo\") AS MAXNO FROM \"FacClose\" GROUP BY \"CustNo\" ,\"FacmNo\" ) r  ";
		sql += "inner join \"FacClose\" f ";
		sql += "ON  r.\"CustNo\" = f.\"CustNo\"  ";
		sql += "AND r.\"FacmNo\" = f.\"FacmNo\"  ";
		sql += "AND r.\"MAXNO\" = f.\"CloseNo\"  ";
		if (iEntryDate > 19110000) {
			sql += "AND f.\"EntryDate\"= " + iEntryDate;
		} else if (iApplDate > 19110000) {
			sql += "AND f.\"ApplDate\"= " + iApplDate;
		} else if (iCustNoS > 0) {
			sql += "AND f.\"CustNo\">= " + iCustNoS;
			sql += "AND f.\"CustNo\"<= " + iCustNoE;
		}
		sql += "order by f.\"CustNo\" ASC,f.\"FacmNo\"					";

		this.info("sql=" + sql);
		Query query;

		this.info("iEntryDate=" + iEntryDate);
		this.info("iApplDate=" + iApplDate);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
//		query.setParameter("entryDate", iEntryDate);
//		query.setParameter("applDate", iApplDate);
//		query.setParameter("type", iType);
		this.info("L2077Service FindData=" + query);

		return switchback(query);
	}

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(titaVo);
	}
}