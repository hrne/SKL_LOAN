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

@Service("lNM39BPServiceImpl")
@Repository

/*
 * LNM39BP 資料欄位清單2(台幣放款-計算原始有效利率用) LNFBP
 */

public class LNM39BPServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LNM39BP.findAll ---------------");
		this.info("-----LNM39BP TitaVo=" + titaVo);
		this.info("-----LNM39BP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// 清單2：台幣放款-計算原始有效利率用
		sql = "SELECT \"CustNo\", \"CustId\", \"FacmNo\", \"BormNo\", \"LoanRate\", \"RateCode\", \"EffectDate\"" + " FROM  \"LoanIfrs9Bp\"" + " WHERE \"DataYM\"  = :dataMonth "
				+ " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\", \"EffectDate\" DESC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM39BP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
