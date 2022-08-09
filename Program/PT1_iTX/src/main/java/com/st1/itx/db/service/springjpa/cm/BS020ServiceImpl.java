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
	public List<Map<String, String>> findAll(TitaVo titaVo, int nextPayIntDate) throws Exception {
		String sql = "SELECT ";
		sql += " av.\"CustNo\"             as \"CustNo\" "; // 戶號
		sql += ",av.\"FacmNo\"             as \"FacmNo\" ";
		sql += ",av.TAV                    as TAV "; // 暫收可抵繳金額
		sql += ",NVL(ln.\"RepayType\",9)    as \"RepayType\" "; // 還款類別 1.期款 2.費用
		sql += ",av.\"RepayCode\"          as \"RepayCode\" "; // 還款來源 02.銀行扣款 00.其他
		sql += "FROM ";
		sql += "( SELECT";
		sql += "   a.\"CustNo\"             as \"CustNo\" ";
		sql += "  ,NVL(t.\"FacmNo\",0)      as \"FacmNo\" ";
 	    sql += "  ,max (case when NVL(f.\"RepayCode\",0) = 2 then 2  else 0 end)";
		sql += "                            as \"RepayCode\"  ";
		sql += "  ,sum (case when a.\"AcctCode\" = 'TAV' then a.\"RvBal\" else 0 end)  as TAV ";
		sql += "  ,sum (case when a.\"AcctCode\" <> 'TAV' then a.\"RvBal\" else 0 end) as FEE ";
		sql += "  FROM \"AcReceivable\" a ";
		sql += "  LEFT JOIN \"LoanFacTmp\" t ON t.\"CustNo\" = a.\"CustNo\" ";
		sql += "                            AND t.\"FacmNo\" = a.\"FacmNo\" ";
		sql += "  LEFT JOIN \"FacMain\"  f ON f.\"CustNo\" = a.\"CustNo\" ";
		sql += "                          AND f.\"FacmNo\" = a.\"FacmNo\" ";
		sql += "  WHERE a.\"AcctFlag\" = 0 ";
		sql += "    and a.\"ClsFlag\" = 0 ";
		sql += "  GROUP BY a.\"CustNo\"        ";
		sql += "          ,NVL(t.\"FacmNo\",0) ";
  	    sql += ") av ";
		sql += "Left join";
		sql += "( SELECT";
		sql += "   l.\"CustNo\"               as \"CustNo\" ";
		sql += "  ,NVL(t.\"FacmNo\",0)        as \"FacmNo\" ";
		sql += "  ,1                          as \"RepayType\" ";
		sql += "  FROM \"LoanBorMain\" l ";
		sql += "  LEFT JOIN \"LoanFacTmp\" t ON t.\"CustNo\" = l.\"CustNo\" ";
		sql += "                            AND t.\"FacmNo\" = l.\"FacmNo\" ";
		sql += "  WHERE l.\"Status\" = 0 ";
		sql += "    and ( l.\"NextPayIntDate\" <= " + nextPayIntDate + " or l.\"MaturityDate\" <= " + nextPayIntDate + ")";
		sql += "  GROUP BY l.\"CustNo\"     ";
		sql += "          ,NVL(t.\"FacmNo\",0) ";
		sql += " ) ln on ln.\"CustNo\" = av.\"CustNo\" ";
		sql += "     and ln.\"FacmNo\" = av.\"FacmNo\" ";
		sql += "WHERE av.TAV > 0 ";
		sql += " and (av.FEE > 0 or NVL(ln.\"RepayType\",0) > 0)";
		sql += "ORDER BY av.\"CustNo\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setFirstResult(0);
		query.setMaxResults(Integer.MAX_VALUE);

		List<Object> result = query.getResultList();

		int size = result.size();
		this.info("Total size ..." + size);
		return this.convertToMap(query);
	}

}