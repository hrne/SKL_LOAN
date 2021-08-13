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

@Service("L4510ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4510ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L4510ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	private int intStartDate = 0;
	private int intEndDate = 0;
	private int flag = 0;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		logger.info("L4510.findAll");

		String sql = " select                                                   ";
		sql += "  l.\"CustNo\"            as F0                                 ";
		sql += " ,l.\"FacmNo\"            as F1                                 ";
		sql += " ,f.\"AcctCode\"          as F2                                 ";
		sql += " ,substr(d.\"Item\",0,1)  as F3                                 ";
		sql += " ,l.\"BormNo\"            as F4                                 ";
		sql += " from \"LoanBorMain\" l                                         ";
		sql += " left join \"FacMain\"  f on f.\"CustNo\"     = l.\"CustNo\"    ";
		sql += "                       and f.\"FacmNo\"       = l.\"FacmNo\"    ";
		sql += " left join \"CustMain\" c on c.\"CustNo\"     = l.\"CustNo\"    ";
		sql += " left join \"CdEmp\"    e on e.\"EmployeeNo\" = c.\"EmpNo\"     ";
		sql += " left join \"CdCode\"   d on d.\"DefType\"    = 4               ";
		sql += "                         and d.\"DefCode\"    = 'EmpDeductType' ";
		sql += "                         and d.\"Code\"       = e.\"AgType1\"   ";
		sql += " where l.\"NextPayIntDate\" >= " + intStartDate;
		sql += "   and l.\"NextPayIntDate\" <= " + intEndDate;
		sql += "   and l.\"Status\" = 0                                         ";
		sql += "   and l.\"AmortizedCode\" != 5 "; // --逆向房貸跳過
		sql += "   and substr(d.\"Item\",0,1) = " + flag; // c."EmpNo" is not null && d.Item = flag
//		15日薪-員工扣薪
		if (flag == 1) {
			sql += "   and f.\"RepayCode\" = 3                                  ";
		}
//		非15日-所有滯繳兩個月的
//		if (flag == 2) {
//
//		}
		// sql += " and e.\"AgType1\" in(0,2,3,5) ";

		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		List<Object> result = query.getResultList();

		return this.convertToMap(result);
	}

	public List<Map<String, String>> findAll(int iIntStartDate, int iIntEndDate, int iFlag, TitaVo titaVo)
			throws Exception {
		intStartDate = iIntStartDate;
		intEndDate = iIntEndDate;
		flag = iFlag;

		return findAll(titaVo);
	}
}