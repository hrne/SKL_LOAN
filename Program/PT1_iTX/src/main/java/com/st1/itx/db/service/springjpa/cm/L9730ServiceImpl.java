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
public class L9730ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	
	public List<Map<String, String>> findAll(int inputStartDate, int inputEndDate, TitaVo titaVo) {
		this.info("L9730ServiceImpl findAll ");
		
		// 原 AS400 Query: X800

		this.info("L9730ServiceImpl inputStartDate = " + inputStartDate);
		this.info("L9730ServiceImpl inputEndDate = " + inputEndDate);
		
		String sql = "";
		sql += " SELECT LRC.\"CustNo\" AS \"LMSACN\" ";
		sql += "       ,LRC.\"FacmNo\" AS \"LMSAPN\" ";
		sql += "       ,LRC.\"BormNo\" AS \"LMSASQ\" ";
		sql += "       ,LBM.\"NextAdjRateDate\" AS \"LMSNSD\" ";
		sql += "       ,LBM.\"FirstAdjRateDate\" AS \"LMSFSD\" ";
		sql += "       ,LRC.\"ProdNo\" AS \"IRTBCD\" ";
		sql += "       ,LBM.\"RateIncr\" AS \"IRTASC\" ";
		sql += "       ,LRC.\"EffectDate\" AS \"ASCADT\" ";
		sql += "       ,LRC.\"RateIncr\" AS \"ASCRAT\" ";
		sql += "       ,LBM.\"LoanBal\" AS \"LMSLBL\" ";
		sql += " FROM \"LoanRateChange\" LRC ";
		sql += " LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = LRC.\"CustNo\" ";
		sql += "                              AND LBM.\"FacmNo\" = LRC.\"FacmNo\" ";
		sql += "                              AND LBM.\"BormNo\" = LRC.\"BormNo\" ";
		sql += " WHERE NVL(LBM.\"LoanBal\", 0) != 0 ";
		sql += "   AND (    NVL(LBM.\"NextAdjRateDate\" , 0) BETWEEN :inputStartDate AND :inputEndDate ";
		sql += "         OR NVL(LBM.\"FirstAdjRateDate\", 0) BETWEEN :inputStartDate AND :inputEndDate) ";
		sql += " ORDER BY \"LMSACN\" ASC ";
		sql += "         ,\"LMSAPN\" ASC ";
		sql += "         ,\"LMSASQ\" ASC ";
		sql += "         ,\"ASCADT\" ASC ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputStartDate", inputStartDate);
		query.setParameter("inputEndDate", inputEndDate);
		
		return this.convertToMap(query);
	}

}