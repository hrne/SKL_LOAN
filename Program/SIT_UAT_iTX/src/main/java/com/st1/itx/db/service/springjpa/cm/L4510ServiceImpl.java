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


	private int intStartDate = 0;
	private int intEndDate = 0;
	private String flag = "";
	private String AgType1 = "";

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L4510.findAll");

		String sql = " select                                                   ";
		sql += "  l.\"CustNo\"            as \"CustNo\"                         ";
		sql += " ,l.\"FacmNo\"            as \"FacmNo\"                         ";
		sql += " ,f.\"AcctCode\"          as \"AcctCode\"                       ";
		
//		sql += " ,substr(d.\"Item\",0,1)  as \"Flag\"                           ";
		
		if ("1".equals(flag)) {
			sql += " , 1                  as \"Flag\" ";
		} else {
			sql += " , 2                  as \"Flag\" ";
		}
		
		sql += " ,l.\"BormNo\"            as \"BormNo\"                         ";
		
		if ("1".equals(flag)) {
			sql += " , 5                  as \"AgType1\" ";
		} else {
			sql += " , 1                  as \"AgType1\" ";
		}
		
		sql += " from \"LoanBorMain\" l                                         ";
		sql += " left join \"FacMain\"  f on f.\"CustNo\"     = l.\"CustNo\"    ";
		sql += "                       and f.\"FacmNo\"       = l.\"FacmNo\"    ";
		sql += " left join \"CustMain\" c on c.\"CustNo\"     = l.\"CustNo\"    ";
		sql += " left join \"CdEmp\"    e on e.\"EmployeeNo\" = c.\"EmpNo\"     ";
//		sql += " left join \"CdCode\"   d on d.\"DefType\"    = 4               ";
//		sql += "                         and d.\"DefCode\"    = 'EmpDeductType' ";
//		sql += "                         and d.\"Code\"       = :AgType1"  ;
		sql += " where l.\"NextPayIntDate\" >= :intStartDate";
		sql += "   and l.\"NextPayIntDate\" <= :intEndDate";
		sql += "   and l.\"Status\" = 0                                         ";
//		sql += "   and substr(d.\"Item\",0,1) = :flag"; // c."EmpNo" is not null && d.Item = flag
		sql += "   and nvl(e.\"EmployeeNo\", ' ') <> ' ' ";
		
//		15日薪-員工扣薪
		if ("1".equals(flag)) {
			sql += "   and f.\"RepayCode\" = 3                                  ";
		}

		// EmployeeCom
		sql += " AND CASE WHEN ( e.\"CommLineCode\" = 21 AND ( substr(e.\"AgLevel\", 0, 1) IN ( 'F','G','J','Z') )) "; 
		sql += "             OR (e.\"CommLineCode\" = 31 AND ( substr(e.\"AgLevel\", 0, 1) IN ('K','Z') )) "; 
		sql += "             OR (e.\"AgLevel\" NOT IN ('21','31','1C' ) AND e.\"AgPostIn\" NOT IN ('TU0036','TU0097') ) ";
		sql += "       THEN  5"; 
		sql += "       ELSE  1";
		sql += "       END = :AgType1";
		
    // 非員工扣款者判斷員工須在職  (AgStatusCode  任用狀況碼 =  1-在職)
		sql += " AND (f.\"RepayCode\" = 3 OR nvl(e.\"AgStatusCode\",'') = '1') ";
		
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("intStartDate", intStartDate);
		query.setParameter("intEndDate", intEndDate);
//		query.setParameter("flag", flag);
		query.setParameter("AgType1", AgType1);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll(int iIntStartDate, int iIntEndDate, int iFlag, TitaVo titaVo) throws Exception {
		intStartDate = iIntStartDate;
		intEndDate = iIntEndDate;
		flag = "" + iFlag;
		AgType1 = "1".equals(flag) ? "5" : "1" ;
		return findAll(titaVo);
	}
}