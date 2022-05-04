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

@Service("L9733ServiceImpl")
@Repository
public class L9733ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int inputStartDateNext, int inputEndDateNext, TitaVo titaVo) {

		this.info("L9733ServiceImpl findAll");
		this.info("inputStartDateNext = " + inputStartDateNext);
		this.info("inputEndDateNext = " + inputEndDateNext);

		String sql = "  ";
		sql += "SELECT LBM.\"CustNo\" ";
		sql += "     , LBM.\"FacmNo\" ";
		sql += "     , LBM.\"BormNo\" ";
		sql += "     , FAC.\"FirstDrawdownDate\" "; // -- 首次撥款日
		sql += "     , LBM.\"FirstAdjRateDate\" "; // -- 首次調整日期
		sql += "     , LBM.\"NextAdjRateDate\" "; // -- 下次調整日期
		sql += "     , LBM.\"RateAdjFreq\" "; // -- 利率調整周期
		sql += "     , SUBSTR(FAC.\"FirstDrawdownDate\",7,2) AS \"FirstDrawdownDateNEW\" "; // -- 首撥日
		sql += "     , SUBSTR(LBM.\"FirstAdjRateDate\",7,2) AS \"FirstAdjRateDateNEW\" "; // -- 首調日
		sql += "     , SUBSTR(LBM.\"NextAdjRateDate\",7,2) AS \"NextAdjRateDateNEW\" "; // -- 下調日
		sql += "     , FAC.\"MaturityDate\" "; // -- 額度到期日
		sql += "FROM \"LoanBorMain\" LBM ";
		sql += "LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                         AND FAC.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += "WHERE LBM.\"RateCode\" != 1 ";
		sql += "  AND LBM.\"Status\" = 0 "; // -- 戶況
		sql += "  AND \"NextAdjRateDate\" >= :inputStartDateNext "; // -- 下調日篩選起日
		sql += "  AND \"NextAdjRateDate\" <= :inputEndDateNext "; // -- 下調日篩選止日
		sql += "  AND SUBSTR(LBM.\"FirstAdjRateDate\",7,2) != SUBSTR(FAC.\"FirstDrawdownDate\",7,2) "; // -- 首調日與首撥日不同
		sql += "  AND SUBSTR(LBM.\"NextAdjRateDate\",7,2) IN ('30','31','28','29') "; // -- 下調日為月底日(?)
		sql += "  AND SUBSTR(LBM.\"NextAdjRateDate\",5,2) != SUBSTR(LBM.\"FirstAdjRateDate\",5,2) "; // -- 下調月與首調月不同
		sql += "  AND SUBSTR(FAC.\"FirstDrawdownDate\",7,2) != SUBSTR(LBM.\"NextAdjRateDate\",7,2) "; // -- 首撥日與下調日不同
		sql += "  AND LBM.\"RateAdjFreq\" = 6 ";
		sql += "ORDER BY \"FirstDrawdownDateNEW\" DESC ";
		sql += "       , LBM.\"CustNo\" ASC ";
		sql += "       , LBM.\"FacmNo\" ASC ";
		sql += "       , LBM.\"BormNo\" ASC ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputStartDateNext", inputStartDateNext);
		query.setParameter("inputEndDateNext", inputEndDateNext);
		return this.convertToMap(query);
	}

}