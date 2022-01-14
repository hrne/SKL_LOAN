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


	private String flag = "";
	private String AgType1 = "";

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L4510.findAll");

		String sql = " select                                                   ";
		sql += "  f.\"CustNo\"              as \"CustNo\"                       ";
		sql += " ,f.\"FacmNo\"              as \"FacmNo\"                       ";
		sql += " ,f.\"AcctCode\"            as \"AcctCode\"                     ";						
		sql += " ,:AgType1                  as \"AgType1\"                      ";
		sql += " ,f.\"RepayCode\"           as \"RepayCode\"                    ";
		sql += " ,c.\"EmpNo\"               as \"EmpNo\"                        ";
		sql += " ,min(l.\"NextPayIntDate\") as \"NextPayIntDate\"               ";
		sql += " from \"LoanBorMain\" l                                         ";
		sql += " left join \"FacMain\"  f on f.\"CustNo\"     = l.\"CustNo\"    ";
		sql += "                       and f.\"FacmNo\"       = l.\"FacmNo\"    ";
		sql += " left join \"CustMain\" c on c.\"CustNo\"     = l.\"CustNo\"    ";
		sql += " left join \"CdEmp\"    e on e.\"EmployeeNo\" = c.\"EmpNo\"     ";
//		sql += " left join \"CdCode\"   d on d.\"DefType\"    = 4               ";
//		sql += "                         and d.\"DefCode\"    = 'EmpDeductType' ";
//		sql += "                         and d.\"Code\"       = :AgType1"  ;
		sql += " where l.\"Status\" = 0                                         ";
		sql += "   and nvl(e.\"EmployeeNo\", ' ') <> ' ' ";
		
//		15日薪-員工扣薪
		if ("1".equals(flag)) {
			sql += "   and f.\"RepayCode\" = 3                                  ";
		}

		// EmployeeCom
		// 2022-01-14 智偉修改: CommLineCode有非數值資料,比對時應以字串型態比較
		// ex : e.\"CommLineCode\" = 21 修改為 e.\"CommLineCode\" = '21'
		sql += "   AND CASE WHEN (    (e.\"CommLineCode\" = '21' AND substr(e.\"AgLevel\", 0, 1) NOT IN ( 'F','G','J','Z') ) "; 
		sql += "                   OR (e.\"CommLineCode\" = '31' AND substr(e.\"AgLevel\", 0, 1) NOT IN ('K','Z') ) "; 
		sql += "                   OR (e.\"CommLineCode\" NOT IN ('21','31','1C' )) )";
		sql += "             AND e.\"AgPostIn\" NOT IN ('TU0036','TU0097')  ";
		sql += "       THEN  5"; 
		sql += "       ELSE  1";
		sql += "       END = :AgType1";
		
    // 非員工扣款者判斷員工須在職  (AgStatusCode  任用狀況碼 =  1-在職)
		sql += "   AND (f.\"RepayCode\" = 3 OR nvl(e.\"AgStatusCode\",'') = '1') ";
		sql += " GROUP BY f.\"CustNo\" , f.\"FacmNo\"  ,f.\"AcctCode\", f.\"RepayCode\", c.\"EmpNo\"  ";
		
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
//		query.setParameter("flag", flag);
		query.setParameter("AgType1", AgType1);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll(int iFlag, TitaVo titaVo) throws Exception {
		flag = "" + iFlag;
		AgType1 = "1".equals(flag) ? "5" : "1" ;
		return findAll(titaVo);
	}
}