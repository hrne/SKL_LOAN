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
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.parse.Parse;

@Service("L4702ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4702ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L4702.findAll");

		int entDy = parse.stringToInteger(titaVo.get("ACCTDATE_ED"));
		this.info("entDy = " + entDy);

		String sql = " select                                            ";
		sql += "  b.\"CustNo\"                          AS \"CustNo\"    ";
		sql += " ,b.\"FacmNo\"                          AS \"FacmNo\"    ";
		sql += " ,c.\"CustName\"                        AS \"CustName\"  ";
		sql += " ,d.\"RepayCode\"                       AS \"RepayCode\" ";
		sql += " from \"BatxDetail\" d                                   ";
		sql += " left join \"CustMain\" c on c.\"CustNo\" = d.\"CustNo\" ";
		sql += " left join (                                             ";
		sql += "     select distinct                                     ";
		sql += "       \"CustNo\"                                        ";
		sql += "      ,\"FacmNo\"                                        ";
		sql += "      from \"LoanBorMain\"                               ";
		sql += "     where \"Status\" = 0                                ";
		sql += "       and \"NextPayIntDate\" <= " + entDy;
		sql += " ) b   on b.\"CustNo\" = d.\"CustNo\"                    ";
		sql += " where d.\"RepayCode\" = 1                               ";
		sql += "   and d.\"ProcStsCode\" <> 'D'                          ";
		sql += "   and d.\"CustNo\" <> 0                                 ";
		sql += "   and d.\"AcDate\" = " + entDy;
		sql += "   and b.\"CustNo\" is not null                          ";
		sql += " order by d.\"CustNo\", d.\"FacmNo\"                     ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}