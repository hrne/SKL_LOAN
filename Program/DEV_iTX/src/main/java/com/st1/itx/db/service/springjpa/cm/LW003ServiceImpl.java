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
public class LW003ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LW003ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> wkSsn(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		logger.info("lW003.wkSsn ENTDY=" + iENTDY);

		// 總表 f0:本日之工作年,f1:本日之工作月
		String sql = "SELECT W.\"Year\" AS F0";
		sql += "			,W.\"Month\" AS F1";
		sql += "	  FROM \"CdWorkMonth\" W";
		sql += "	  WHERE W.\"StartDate\" <= :iday";
		sql += "		AND W.\"EndDate\" >= :iday";
		logger.info("sql=" + sql);
		Query query;
		logger.info("titaVo=" + titaVo);
//		從titaVo tom??傳過來的資料?
//		顯示出Shared EntityManager proxy for target factory [org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean@4b174242]
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
//		logger.info("Impl0."+em);
		query = em.createNativeQuery(sql);
//		logger.info("Impl00."+query);
//		iENTDY=20200420
		query.setParameter("iday", iENTDY);
//		logger.info("Impl000."+query.setParameter("iday", iENTDY));
//		logger.info("Impl0000."+this.convertToMap(query.getResultList()));
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, Map<String, String> wkVo) throws Exception {
		logger.info("impl1." + titaVo.get("ENTDY") + "----" + Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
//		1090420+19110000=20200420
		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		logger.info("impl2." + wkVo.get("F0"));
//		2020
		String tWKYEAR = wkVo.get("F0");
//		20200420   2020
		logger.info("lW003.findAll ENTDY=" + iENTDY + ",WORKSEASON=" + tWKYEAR);

		String sql = "SELECT P.\"WorkMonth\" AS F0";// 營業日對應之工作年
		sql += "			,SUM(P.\"IntroducerAddBonus\") AS F1";// 介紹人獎勵金
		sql += "	  FROM \"PfReward\" P";
		sql += "	  LEFT JOIN \"CustMain\" C";
		sql += "  	  ON  C.\"CustNo\" =P.\"CustNo\"";
		sql += "	  WHERE TRUNC(P.\"WorkMonth\" / 100) = :wkyear";//
		sql += "  		AND P.\"PerfDate\" <= :iday";
		sql += "  		AND C.\"EntCode\" = 0";
		sql += " 	  GROUP BY P.\"WorkMonth\"";

		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		logger.info("impl3." + em);
		query = em.createNativeQuery(sql);
		logger.info("impl4." + query);
		query.setParameter("wkyear", tWKYEAR);
//		logger.info("impl5." + query.setParameter("wkyear", tWKYEAR));
		query.setParameter("iday", iENTDY);
//		logger.info("impl6." + query.setParameter("iday", iENTDY));
//		logger.info("impl7." +  this.convertToMap(query.getResultList()));
		return this.convertToMap(query.getResultList());
	}

}