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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
/* 逾期放款明細 */
public class LM031ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM031ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String entdy = String.valueOf((Integer.valueOf(titaVo.get("inputDate").toString()) + 19110000));

		logger.info("lM031.findAll ");
		String sql = "";
		sql += " SELECT   LB.\"CustNo\" ";
		sql += "         ,LB.\"FacmNo\" ";
		sql += "         ,C.\"CustName\" ";
		sql += "         ,LB.\"BormNo\" ";
		sql += "         ,F.\"LineAmt\" ";
		sql += "         ,LB.\"LoanBal\" ";
		sql += "         ,C.\"EntCode\" ";
		sql += "         ,F.\"RecycleCode\" ";
		sql += "         ,F.\"RecycleDeadline\" ";
		sql += "         ,LB.\"PrevPayIntDate\" ";
		sql += " FROM \"LoanBorMain\" LB ";
		sql += " LEFT JOIN \"FacMain\" F ON LB.\"CustNo\" = F.\"CustNo\" ";
		sql += "                      AND LB.\"FacmNo\" = F.\"FacmNo\" ";
		sql += " LEFT JOIN \"CollList\" L ON L.\"CustNo\" = F.\"CustNo\" ";
		sql += "                       AND L.\"FacmNo\" = F.\"FacmNo\" ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = F.\"CustNo\" ";
		sql += " WHERE C.\"EntCode\" = '1' ";
		sql += "   AND F.\"RecycleDeadline\" <= :entdy ";
		sql += "   AND F.\"RecycleCode\" = 1 ";
		sql += "   AND LB.\"DrawdownAmt\" > 0 ";
		sql += "   AND LB.\"LoanBal\" > 0 ";
                sql += "   AND LB.\"BormNo\" <= 900 ";
		sql += " ORDER BY LB.\"CustNo\" ";
		sql += "         ,LB.\"FacmNo\" ";
		sql += "         ,LB.\"BormNo\" ";
		sql += "  ";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		return this.convertToMap(query.getResultList());
	}

}