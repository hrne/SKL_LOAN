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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
public class L7501ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> getLoanData(String custId, TitaVo titaVo) {
		this.info("L7501ServiceImpl findAll ");

		this.info("custId = " + custId);

		String sql = "";
		sql += " SELECT CM.\"CustNo\" ";
		sql += "      , CM.\"CustName\" ";
		sql += "      , NVL(FAC.\"FacmNo\",0) AS \"FacmNo\"";
		sql += "      , MIN(NVL(LBM.\"DrawdownDate\",0)) AS \"DrawdownDate\" ";
		sql += "      , MAX(NVL(LBM.\"MaturityDate\",0)) AS \"MaturityDate\" ";
		sql += "      , MAX(NVL(LBM.\"PrevPayIntDate\",0)) AS \"PrevIntDate\" ";
		sql += "      , SUM(NVL(LBM.\"LoanBal\",0)) AS \"LoanBal\" ";
		sql += "      , MAX(NVL(LBM.\"LastEntDy\",0)) AS \"LbsDy\" ";
		sql += " FROM \"CustMain\" CM ";
		sql += " LEFT JOIN \"FacMain\" FAC  ";
		sql += " ON FAC.\"CustNo\" = CM.\"CustNo\" ";
		sql += " LEFT JOIN \"LoanBorMain\" LBM  ";
		sql += " ON LBM.\"CustNo\" = FAC.\"CustNo\" ";
		sql += " AND LBM.\"FacmNo\" = FAC.\"FacmNo\" ";
		sql += " WHERE NVL(CM.\"CustNo\",0) != 0 ";
		sql += "   AND NVL(LBM.\"LoanBal\",0) != 0 ";
		sql += "   AND NVL(CM.\"CuscCd\",0) = 1 ";
		sql += "   AND NVL(LBM.\"Status\",9) = 0 ";
		sql += "   AND CM.\"CustId\" = :inputCustId ";
		sql += " GROUP BY CM.\"CustNo\" ";
		sql += "        , CM.\"CustName\" ";
		sql += "        , FAC.\"FacmNo\" ";
		sql += " ORDER BY \"FacmNo\" ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("inputCustId", custId);

		return this.convertToMap(query);
	}
}