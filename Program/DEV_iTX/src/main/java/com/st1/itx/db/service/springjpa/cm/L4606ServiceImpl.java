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

@Service("l4606ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4606ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("l4606.findAll ");
		String insuYearMonth = String.valueOf((Integer.valueOf(titaVo.get("InsuEndMonth").toString()) + 191100));
		String sql = "   SELECT                                                    ";
		sql += "     I.\"NowInsuNo\"                                         AS F0  ";
		sql += "   , I.\"InsuCate\"                                          AS F1  ";
		sql += "   , I.\"InsuPrem\"                                          AS F2  ";
		sql += "   , I.\"InsuStartDate\"                                     AS F3  ";
		sql += "   , I.\"InsuEndDate\"                                       AS F4  ";
		sql += "   , I.\"InsuredAddr\"                                       AS F5  ";
		sql += "   , I.\"CustNo\"                                            AS F6  ";
		sql += "   , I.\"FacmNo\"                                            AS F7  ";
		sql += "   , C.\"CustName\"                                          AS F8  ";
		sql += "   , I.\"EmpId\"                                             AS F9  ";
		sql += "   , I.\"EmpName\"                                           AS F10 ";
		sql += "   , I.\"DueAmt\"                                            AS F11 ";
		sql += "  FROM \"InsuComm\" I                                              ";
		sql += "  LEFT JOIN \"CustMain\" C ON c.\"CustNo\"  = I.\"CustNo\"          ";
		sql += "  WHERE I.\"InsuYearMonth\" = " + insuYearMonth;
		sql += "    and I.\"DueAmt\"  > 0                                         ";
		sql += "    and NVL(I.\"MediaCode\",'N') = 'Y'                             ";
		sql += "  ORDER BY I.\"EmpId\",I.\"NowInsuNo\", I.\"InsuCate\"                ";


		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> find(TitaVo titaVo) throws Exception {
		this.info("l4606.findAll ");
		String insuYearMonth = String.valueOf((Integer.valueOf(titaVo.get("InsuEndMonth").toString()) + 191100));
		String sql = "   SELECT                                                    ";
		sql += "     I.\"NowInsuNo\"                                           AS F0  ";
		sql += "   , I.\"InsuCate\"                                          AS F1  ";
		sql += "   , I.\"InsuPrem\"                                          AS F2  ";
		sql += "   , I.\"InsuStartDate\"                                     AS F3  ";
		sql += "   , I.\"InsuEndDate\"                                       AS F4  ";
		sql += "   , I.\"InsuredAddr\"                                       AS F5  ";
		sql += "   , I.\"CustNo\"                                            AS F6  ";
		sql += "   , I.\"FacmNo\"                                            AS F7  ";
		sql += "   , C.\"CustName\"                                          AS F8  ";
		sql += "   , I.\"EmpId\"                                             AS F9  ";
		sql += "   , I.\"EmpName\"                                           AS F10 ";
		sql += "   , I.\"DueAmt\"                                            AS F11 ";
		sql += "  FROM \"InsuComm\" I                                               ";
		sql += "  LEFT JOIN \"CustMain\" C ON c.\"CustNo\"  = I.\"CustNo\"          ";
		sql += "  WHERE I.\"InsuYearMonth\" = " + insuYearMonth;
		sql += "    and I.\"DueAmt\"  > 0                                           ";
		sql += "    and NVL(I.\"MediaCode\",'N') = 'N'                              ";
		sql += "  ORDER BY I.\"EmpId\",I.\"NowInsuNo\", I.\"InsuCate\"                ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}
}