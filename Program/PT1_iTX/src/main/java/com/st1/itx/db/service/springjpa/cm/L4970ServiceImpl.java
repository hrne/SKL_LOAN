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
 * 暫收款可抵繳火顯查詢
 * 
 * @author Lai
 * @version 1.0.0
 */
@Service("L4970ServiceImpl")
@Repository
public class L4970ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

  // ref L4961 qc2627
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int startDate, int endDate, TitaVo titaVo) throws Exception {
		String sql = "";
		sql += " select                                                    ";
		sql += "  i.\"InsuYearMonth\" - 191100 as \"InsuYearMonth\"        "; // 火險到期年月
		sql += " ,i.\"PrevInsuNo\"                                         "; // 原保單號碼
		sql += " ,i.\"NowInsuNo\"                                          "; // 新保單號碼
		sql += " ,i.\"InsuStartDate\" - 19110000 as \"InsuStartDate\"      "; // 保險起日
		sql += " ,i.\"InsuEndDate\" - 19110000 as \"InsuEndDate\"          "; // 保險迄日
		sql += " ,i.\"CustNo\"                                             "; // 戶號+額度
		sql += " ,i.\"FacmNo\"                                             ";    
		sql += " ,c.\"CustName\"                                           "; // 戶名
		sql += " ,i.\"TotInsuPrem\"                                        "; // 總保費
		sql += " ,i.\"RepayCode\"                                          "; // 繳款方式
		sql += " ,i.\"StatusCode\"                                         ";
		sql += " ,a.\"TavBal\"                                             "; // 暫收可抵繳金額
		sql += " from \"InsuRenew\" i                                      ";
		sql += " left join \"CustMain\" c on c.\"CustNo\" = i.\"CustNo\"   ";
 		sql += " left join ( SELECT";
		sql += "              \"CustNo\"                                   ";
		sql += "             ,sum (\"RvBal\")  as \"TavBal\"               ";
		sql += "             FROM \"AcReceivable\"                         ";
		sql += "             WHERE \"ClsFlag\" = 0                         ";
		sql += "               and \"AcctCode\" = 'TAV'                    ";
		sql += "             GROUP BY \"CustNo\"                           ";
        sql += "           ) a on a.\"CustNo\" = i.\"CustNo\"              ";
   	    sql += " where i.\"AcDate\" = 0                                    ";
		sql += "   and i.\"RenewCode\" = 2                                 ";
		sql += "   and i.\"StatusCode\" in (0, 1, 2)                       ";
		sql += "   and i.\"InsuStartDate\" between " + startDate + " and " +  endDate ; // 保險起日區間
		sql += "   and i.\"TotInsuPrem\" > 0                                ";               
		sql += "   and i.\"TotInsuPrem\" >= a.\"TavBal\"                    ";       
		sql += " order by a.\"CustNo\"                                     ";
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