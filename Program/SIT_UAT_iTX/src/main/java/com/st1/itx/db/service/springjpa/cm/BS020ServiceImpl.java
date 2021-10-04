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

/**
 * 應繳期款戶號清單by繳款方式
 * 
 * @author Lai
 * @version 1.0.0
 */
@Service("BS020ServiceImpl")
@Repository
public class BS020ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		String sql = "SELECT ";
		sql += "a.\"CustNo\"             as F0 ";
		sql += ",a.\"FacmNo\"            as F1 ";
		sql += ",a.\"RvBal\"             as F2 ";
		sql += ",f.\"RepayCode\"         as F3 ";
		sql += "FROM   \"AcReceivable\" a ";
		sql += "LEFT JOIN \"FacMain\"  f ON f.\"CustNo\" = a.\"CustNo\" ";
		sql += "                        AND f.\"FacmNo\" = a.\"FacmNo\" ";
		sql += "WHERE a.\"AcctCode\" = 'TAV' ";
		sql += "  and a.\"ClsFlag\" = 0 ";
		sql += "  and nvl(f.\"RepayCode\",0) > 0 ";
		sql += "order BY a.\"CustNo\" ";
		sql += "        ,a.\"FacmNo\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setFirstResult(0);
		query.setMaxResults(Integer.MAX_VALUE);

		List<Object> result = query.getResultList();

		int size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(result);
	}

}