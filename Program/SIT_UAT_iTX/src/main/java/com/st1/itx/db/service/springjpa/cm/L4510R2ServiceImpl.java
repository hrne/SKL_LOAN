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

@Service("l4510R2ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4510R2ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	private int entryDate = 0;
	private String procCode = "";

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L4510R2.findAll");

		String sql = " select                                                           ";
		sql += "  d.\"PerfMonth\"            AS F0                                      ";
		sql += " ,d.\"ProcCode\"             AS F1                                      ";
		sql += " ,d.\"EntryDate\"            AS F2                                      ";
		sql += " ,d.\"EmpNo\"                AS F3                                      ";
		sql += " ,d.\"CustId\"               AS F4                                      ";
		sql += " ,d.\"CustNo\"               AS F5                                      ";
		sql += " ,c.\"CustName\"             AS F6                                      ";
		sql += " ,d.\"FacmNo\"               AS F7                                      ";
		sql += " ,d.\"BormNo\"               AS F8                                      ";
		sql += " ,d.\"TxAmt\"                AS F9                                      ";
		sql += " from \"EmpDeductDtl\" d                                                ";
		sql += " left join \"CustMain\" c on c.\"CustNo\" = d.\"CustNo\"                ";
		sql += " where d.\"ErrMsg\" is null                                             ";
		sql += "   and d.\"EntryDate\" = " + entryDate;
		sql += "   and d.\"ProcCode\" in ( " + procCode + " ) ";
		sql += "   and d.\"AchRepayCode\" = 4                                           ";
		sql += " order by d.\"CustNo\",d.\"FacmNo\"                                     ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		List<Object> result = query.getResultList();

		return this.convertToMap(result);
	}

	public List<Map<String, String>> findAll(int iEntryDate, String iProcCode, TitaVo titaVo) throws Exception {
		entryDate = iEntryDate;
		procCode = iProcCode;

		return findAll(titaVo);
	}
}