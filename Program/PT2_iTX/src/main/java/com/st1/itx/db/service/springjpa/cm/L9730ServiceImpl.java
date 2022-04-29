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
		sql += " SELECT LRC.\"CustNo\"           AS \"CustNo\" ";
		sql += "       ,LRC.\"FacmNo\"           AS \"FacmNo\" ";
		sql += "       ,LRC.\"BormNo\"           AS \"BormNo\" ";
		sql += "       ,LBM.\"NextAdjRateDate\"  AS \"NextAdjRateDate\" ";
		sql += "       ,LBM.\"FirstAdjRateDate\" AS \"FirstAdjRateDate\" ";
		sql += "       ,LRC.\"ProdNo\"           AS \"ProdNo\" ";
		sql += "       ,LBM.\"RateIncr\"         AS \"LBMRateIncr\" ";
		sql += "       ,LRC.\"EffectDate\"       AS \"EffectDate\" ";
		sql += "       ,LRC.\"RateIncr\"         AS \"LRCRateIncr\" ";
		sql += "       ,LBM.\"LoanBal\"          AS \"LoanBal\" ";
		sql += " FROM \"LoanRateChange\" LRC ";
		sql += " LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = LRC.\"CustNo\" ";
		sql += "                              AND LBM.\"FacmNo\" = LRC.\"FacmNo\" ";
		sql += "                              AND LBM.\"BormNo\" = LRC.\"BormNo\" ";
		sql += " WHERE LRC.\"Status\" = 2 "; // 只抓加碼利率（定期機動）
		sql += "   AND ( (     NVL(LBM.\"LoanBal\", 0) != 0 ";
		sql += "           AND NVL(LBM.\"NextAdjRateDate\", 0) BETWEEN :inputStartDate AND :inputEndDate "; // A. 如果不餘額為0且下次調整日在此區間
		sql += "         ) ";
		sql += "         OR NVL(LBM.\"FirstAdjRateDate\", 0) BETWEEN :inputStartDate AND :inputEndDate ";   // B. 如果首次調整日在此區間（餘額可為0）
		sql += "       ) ";
		sql += " ORDER BY \"CustNo\" ASC ";
		sql += "         ,\"FacmNo\" ASC ";
		sql += "         ,\"BormNo\" ASC ";
		sql += "         ,\"EffectDate\" ASC ";


		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputStartDate", inputStartDate);
		query.setParameter("inputEndDate", inputEndDate);
		
		return this.convertToMap(query);
	}

}