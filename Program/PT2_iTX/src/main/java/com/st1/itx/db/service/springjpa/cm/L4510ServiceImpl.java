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

@Service("L4510ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4510ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(int iOpItem, TitaVo titaVo) throws Exception {

// 薪制           制度別                            流程別      
// 15日薪                                                  5
// 1非15日薪  3-三階                                1
//           2-二階                                2
//           5-三階新制(else)        3

// iOpItem  
// 1.15日薪 
// 2.非15日薪
		this.info("L4510.findAll");

		String sql = " select                                                   ";
		sql += "  f.\"CustNo\"              as \"CustNo\"                       ";
		sql += " ,f.\"FacmNo\"              as \"FacmNo\"                       ";
		sql += " ,f.\"AcctCode\"            as \"AcctCode\"                     ";
		sql += " ,f.\"RepayCode\"           as \"RepayCode\"                    ";
		sql += " ,c.\"EmpNo\"               as \"EmpNo\"                        ";
		sql += " ,max(e.\"AgType1\")        as \"AgType1\"                      ";
		sql += " ,max(e.\"AgLevel\")        as \"AgLevel\"                      ";
		sql += " ,max(e.\"AgStatusCode\")   as \"AgStatusCode\"                 ";
		if (iOpItem == 1) {
			sql += " ,'5'                   as \"ProcCode\"                     ";
		} else {
			sql += " ,max(case when \"AgType1\" in '3' then '1'        ";
			sql += "           when \"AgType1\" in '2' then '2'        ";
			sql += "           else '3'                                ";
			sql += "      end )             as \"ProcCode\"        ";
		}
		sql += " ,min(l.\"NextPayIntDate\") as \"NextPayIntDate\"               ";
		sql += " ,min(l.\"FirstDueDate\")   as \"FirstDueDate\"                 ";
		sql += " from \"LoanBorMain\" l                                         ";
		sql += " left join \"FacMain\"  f on f.\"CustNo\"     = l.\"CustNo\"    ";
		sql += "                       and f.\"FacmNo\"       = l.\"FacmNo\"    ";
		sql += " left join \"CustMain\" c on c.\"CustNo\"     = l.\"CustNo\"    ";
		sql += " left join \"CdEmp\"    e on e.\"EmployeeNo\" = c.\"EmpNo\"     ";
		sql += " where l.\"Status\" = 0                                         ";
		sql += "   and nvl(e.\"EmployeeNo\", ' ') <> ' ' ";
		// EmployeeCom
		// 2023/6/28 Lai 15日薪：AgLevel 業務人員職等 IN ('00') 15日薪 or LIKE 'E%' 2/3階 處經理
		sql += "   AND CASE WHEN e.\"AgLevel\" IN ('00') THEN  1 ";
		sql += "            WHEN e.\"AgLevel\" LIKE 'E%' THEN  1 ";
		sql += "            ELSE 2                               ";
		sql += "       end = " + iOpItem;
		// 2023/6/28 Lai AgStatusCode 業務人員任用狀況碼 IN (1.在職,9.未報聘/內勤)
		sql += "   AND e.\"AgStatusCode\" IN ('1', '9') ";
		sql += " GROUP BY f.\"CustNo\" , f.\"FacmNo\"  ,f.\"AcctCode\", f.\"RepayCode\", c.\"EmpNo\"  ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}