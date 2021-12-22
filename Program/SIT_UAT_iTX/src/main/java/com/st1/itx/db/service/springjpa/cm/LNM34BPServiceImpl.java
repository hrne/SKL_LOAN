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
import com.st1.itx.eum.ContentName;

@Service("lNM34BPServiceImpl")
@Repository

/*
 * LNM34BP LNM34 資料欄位清單B
 */

public class LNM34BPServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LNM34BP.findAll ---------------");
		this.info("-----LNM34BP TitaVo=" + titaVo);
		this.info("-----LNM34BP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202003;
//		}

		this.info("dataMonth= " + dateMonth);

		String sql = "";

		// LNM34BP 資料欄位清單B
		sql = "SELECT " + "  \"CustNo\", \"CustId\", \"FacmNo\", \"BormNo\", \"LoanRate\" "
				+ ", \"RateCode\", \"EffectDate\" " + " FROM  \"Ias34Bp\" " + " WHERE \"DataYM\" = :dateMonth "
				+ " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\", \"EffectDate\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM34BP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dateMonth", dateMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
