package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("checkAuthServiceImpl")
@Repository

public class CheckAuthServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(String tlrno, String tranno) throws Exception {
		this.info("CheckAuthServiceImpl.findAll tlrno = " + tlrno + ", TranNo = " + tranno);

		String sql = "select a.\"TlrNo\",a.\"LevelFg\",a.\"AuthNo\",b.\"AuthFg\",0 \"BeginDate\",0 \"BeginTime\",99991231 \"EndDate\",2400 \"EndTime\" from \"TxTeller\" a "
				+ "left join \"TxAuthority\" b on b.\"AuthNo\" = a.\"AuthNo\" and b.\"TranNo\" = :tranno "
				+ "where a.\"TlrNo\" = :tlrno " + "UNION ALL "
				+ "select c.\"TlrNo\",d.\"LevelFg\",d.\"AuthNo\",e.\"AuthFg\",c.\"BeginDate\",c.\"BeginTime\",c.\"EndDate\",c.\"EndTime\" from \"TxAgent\" c "
				+ "left join \"TxTeller\" d on d.\"TlrNo\" = c.\"TlrNo\" "
				+ "left join \"TxAuthority\" e on e.\"AuthNo\" = d.\"AuthNo\" and e.\"TranNo\" = :tranno "
				+ "where c.\"AgentTlrNo\" = :tlrno and c.\"Status\" = 0 ";
//		;
		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
//		this.info("CheckAuthServiceImpl.findAll 1");
		query = em.createNativeQuery(sql);
//		this.info("CheckAuthServiceImpl.findAll 2");
		query.setParameter("tlrno", tlrno);
		query.setParameter("tranno", tranno);

		this.info("CheckAuthServiceImpl.findAll 3");

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findCanDoList(String tranNo) throws Exception {
		this.info("CheckAuthServiceImpl.findCanDoList TranNo = " + tranNo);

		String sql = "select \"TlrNo\",MAX(\"AuthFg\") as \"AuthFg\" from ( ";
		sql += "select b.\"TlrNo\",b.\"AuthNo\",a.\"AuthFg\" from \"TxAuthority\" a ";
		sql += "left join \"TxTellerAuth\" b on b.\"AuthNo\" = a.\"AuthNo\" ";
		sql += "left join \"TxTeller\" c on c.\"TlrNo\" = b.\"TlrNo\" ";
		sql += "where \"TranNo\" = :TranNo and c.\"Status\" = '1' ";
		sql += "order by b.\"TlrNo\") z group by \"TlrNo\" order by \"TlrNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);

		query = em.createNativeQuery(sql);

		query.setParameter("TranNo", tranNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findCanDoPgms(String brno, String tlrno, String tranno) throws Exception {
		this.info("CheckAuthServiceImpl.findCanDoPgms tlrno = " + tlrno);

		String sql = "select A.\"BrNo\",E.\"BranchShort\",A.\"TlrNo\",F.\"Fullname\",B.\"AuthNo\",G.\"AuthItem\",C.\"TranNo\",D.\"TranItem\",C.\"AuthFg\",";
		sql += "case C.\"AuthFg\" when 1 then '限查詢' else '全部' end as \"AuthFgX\",C.\"LastUpdate\",C.\"LastUpdateEmpNo\",H.\"Fullname\" AS \"LastUpdateEmpName\" ";
		sql += "from \"TxTeller\" A ";
		sql += "left join \"TxTellerAuth\" B ON B.\"TlrNo\" = A.\"TlrNo\" ";
		sql += "left join \"TxAuthority\" C ON C.\"AuthNo\" = B.\"AuthNo\" ";
		sql += "left join \"TxTranCode\" D ON D.\"TranNo\" = C.\"TranNo\" ";
		sql += "left join \"CdBranch\" E ON E.\"BranchNo\" = A.\"BrNo\" ";
		sql += "left join \"CdEmp\" F ON F.\"EmployeeNo\" = A.\"TlrNo\" ";
		sql += "left join \"CdEmp\" H ON H.\"EmployeeNo\" = C.\"LastUpdateEmpNo\" ";
		sql += "left join \"TxAuthGroup\" G ON G.\"AuthNo\" = B.\"AuthNo\" ";
		sql += "where D.\"Status\" = 0 and C.\"AuthFg\" > 0 ";
		
		if (!"".equals(brno)) {
			sql += "and A.\"BrNo\" = :brno ";
		}
		if (!"".equals(tlrno)) {
			sql += "and A.\"TlrNo\" = :tlrno ";	
		}
		if (!"".equals(tranno)) {
			sql += "and C.\"TranNo\" = :tranno ";
		}
		
		sql += "order by A.\"BrNo\",A.\"TlrNo\",C.\"TranNo\",B.\"AuthNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);

		query = em.createNativeQuery(sql);

		if (!"".equals(brno)) {
			query.setParameter("brno", brno);
		}
		if (!"".equals(tlrno)) {
			query.setParameter("tlrno", tlrno);
		}
		if (!"".equals(tranno)) {
			query.setParameter("tranno", tranno);
		}

		return this.convertToMap(query);
	}

}
