package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("l4510RServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4510RServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L4510RServiceImpl.class);

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

		logger.info("L4510R.findAll");

		String sql = " select                                                           ";
		sql += "  d.\"PerfMonth\"            AS F0                                      ";
		sql += " ,d.\"ProcCode\"             AS F1                                      ";
		sql += " ,d.\"EntryDate\"            AS F2                                      ";
		sql += " ,d.\"EmpNo\"                AS F3                                      ";
		sql += " ,d.\"CustId\"               AS F4                                      ";
		sql += " ,d.\"CustNo\"               AS F5                                      ";
		sql += " ,c.\"CustName\"             AS F6                                      ";
		sql += " ,d.\"FacmNo\"               AS F7                                      ";
		sql += " ,r.\"ClNo\"                 AS F8                                      ";
		sql += " ,r.\"FireInsuPrem\"         AS F9                                      ";
		sql += " ,r.\"EthqInsuPrem\"         AS F10                                     ";
		sql += " ,r.\"TotInsuPrem\"          AS F11                                     ";
		sql += " from \"EmpDeductDtl\" d                                                ";
		sql += " left join \"CustMain\" c on c.\"CustNo\" = d.\"CustNo\"                ";
		sql += " left join (                                                            ";
		sql += "     select                                                             ";
		sql += "      \"CustNo\"                                                        ";
		sql += "     ,\"FacmNo\"                                                        ";
		sql += "     ,\"ClCode1\" || ' ' || LPAD(\"ClCode2\", 2, '0') || ' ' ||  LPAD(\"ClNo\", 7, '0') as \"ClNo\" ";
		sql += "     ,\"FireInsuPrem\"                                                  ";
		sql += "     ,\"EthqInsuPrem\"                                                  ";
		sql += "     ,\"TotInsuPrem\"                                                   ";
		sql += "     ,row_number() over (partition by \"CustNo\", \"FacmNo\" order by \"InsuYearMonth\" Desc) as seq ";
		sql += "     from \"InsuRenew\"                                                 ";
		sql += " ) r on r.\"CustNo\" = d.\"CustNo\"                                     ";
		sql += "    and r.\"FacmNo\" = d.\"FacmNo\"                                     ";
		sql += "    and r.seq = 1                                                       ";
		sql += " where d.\"ErrMsg\" is null                                             ";
		sql += "   and d.\"EntryDate\" = " + entryDate;
		sql += "   and d.\"ProcCode\" in ( " + procCode + " ) ";
		sql += "   and d.\"AchRepayCode\" = 5                                           ";
		sql += " order by d.\"CustNo\",d.\"FacmNo\"                                     ";

		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
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