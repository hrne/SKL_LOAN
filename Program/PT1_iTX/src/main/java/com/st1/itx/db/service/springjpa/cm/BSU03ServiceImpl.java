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
public class BSU03ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	// 業績已調整清單
	public List<Map<String, String>> findAdjustList(int inputStartDate, TitaVo titaVo) {

		this.info(" findAdjustList = " + inputStartDate);

		String sql = " ";
		sql += " SELECT \"LMSACN\"               AS  \"CustNo\" ";
		sql += "      , \"LMSAPN\"               AS  \"FacmNo\" ";
		sql += "      , \"APLUAM\"               AS  \"DrawdownAmt\" ";
		sql += "      , \"PRZCNT\"               AS  \"CntingCode\" ";
		sql += "      , \"TACTAMT\"              AS  \"PerfAmt\" ";
		sql += "      , \"YAG3LV\"               AS  \"PerfEqAmt\" ";
		sql += "      , \"PAY3LV\"               AS  \"PerfReward\" ";
		sql += " FROM \"TMPQQQP\" ";
//		sql += " GROUP BY \"LMSACN\"  ";
//		sql += "        , \"LMSAPN\"  ";
//		sql += "        , \"APLUAM\"  ";
//		sql += "        , \"PRZCNT\"  ";
//		sql += "        , \"TACTAMT\" ";
//		sql += "        , \"YAG3LV\"  ";
//		sql += "        , \"PAY3LV\"  ";
		sql += " ORDER BY \"LMSACN\"  ";
		sql += "        , \"LMSAPN\"  ";
		sql += "        , \"SOURCE\"  ";
		sql += "        , CASE WHEN \"APLUAM\" < 600000 AND \"PRZCNT\" = 'N' THEN 1 ";
		sql += "               WHEN \"APLUAM\" >= 600000 AND \"PRZCNT\" = 'Y' THEN 2 ";
		sql += "               ELSE  3 ";
		sql += "          END          ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}