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

@Service("l4606ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4606ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L4606ServiceImpl.class);

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
		logger.info("l4606.findAll ");
		String entdy = String.valueOf((Integer.valueOf(titaVo.get("InsuEndMonth").toString()) + 191100));
		String sql = "   SELECT                                                    ";
		sql += "     I.\"BatchNo\"                                                 ";
		sql += "   , I.\"InsuCate\"                                                ";
		sql += "   , I.\"InsuPrem\"                                                ";
		sql += "   , I.\"InsuStartDate\"                                           ";
		sql += "   , I.\"InsuEndDate\"                                             ";
		sql += "   , I.\"InsuredAddr\"                                             ";
		sql += "   , I.\"CustNo\"                                                  ";
		sql += "   , I.\"FacmNo\"                                                  ";
		sql += "   , I.\"FireOfficer\"                                             ";
		sql += "   , I.\"EmpId\"                                                   ";
		sql += "   , I.\"EmpName\"                                                 ";
		sql += "   , I.\"Commision\"                                               ";
		sql += "  FROM \"InsuComm\" I                                              ";
		sql += "  LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\"  = I.\"FireOfficer\"   ";
		sql += "  WHERE I.\"InsuYearMonth\" >= " + entdy;
		sql += "    and E.\"EmployeeNo\" is not null                               ";
		sql += "  ORDER BY I.\"BatchNo\", I.\"InsuCate\"                           ";

		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> find(TitaVo titaVo) throws Exception {
		logger.info("l4606.findAll ");
		String entdy = String.valueOf((Integer.valueOf(titaVo.get("InsuEndMonth").toString()) + 191100));
		String sql = "   SELECT                                                    ";
		sql += "     I.\"BatchNo\"                                                 ";
		sql += "   , I.\"InsuCate\"                                                ";
		sql += "   , I.\"InsuPrem\"                                                ";
		sql += "   , I.\"InsuStartDate\"                                           ";
		sql += "   , I.\"InsuEndDate\"                                             ";
		sql += "   , I.\"InsuredAddr\"                                             ";
		sql += "   , I.\"CustNo\"                                                  ";
		sql += "   , I.\"FacmNo\"                                                  ";
		sql += "   , I.\"FireOfficer\"                                             ";
		sql += "   , I.\"EmpId\"                                                   ";
		sql += "   , I.\"EmpName\"                                                 ";
		sql += "   , I.\"Commision\"                                               ";
		sql += "  FROM \"InsuComm\" I                                              ";
		sql += "  WHERE I.\"InsuYearMonth\" >= " + entdy;
		sql += "    and I.\"FireOfficer\" is null        ";
		sql += "  ORDER BY I.\"BatchNo\", I.\"InsuCate\"                           ";

		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query.getResultList());
	}
}