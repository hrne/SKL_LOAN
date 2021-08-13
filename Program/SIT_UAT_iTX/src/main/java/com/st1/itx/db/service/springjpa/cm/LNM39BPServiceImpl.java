package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
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
import com.st1.itx.eum.ContentName;

@Service("lNM39BPServiceImpl")
@Repository

/*
 * LNM39BP 清單2：台幣放款-計算原始有效利率用
 */

public class LNM39BPServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LNM39BPServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		logger.info("----------- LNM39BP.findAll ---------------");
		logger.info("-----LNM39BP TitaVo=" + titaVo);
		logger.info("-----LNM39BP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202004;
//		}

		logger.info("dataMonth= " + dateMonth);

		String sql = "";

		// 清單2：台幣放款-計算原始有效利率用
		sql = "SELECT \"CustNo\", \"CustId\", \"FacmNo\", \"BormNo\", \"LoanRate\", \"RateCode\", \"EffectDate\"" +
				" FROM  \"LoanIfrsBp\"" + 
	            " WHERE \"DataYM\" = " + dateMonth +
				" ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\", \"EffectDate\" DESC" ;

		logger.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM39BP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
