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

@Service("lNM34GPServiceImpl")
@Repository

/*
 * LNM34GP LNM34 資料欄位清單G
 */

public class LNM34GPServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LNM34GP.findAll ---------------");
		this.info("-----LNM34GP TitaVo=" + titaVo);
		this.info("-----LNM34GP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// LNM34GP 資料欄位清單G
		sql = "SELECT " + "  \"CustNo\", \"CustId\", \"AgreeNo\", \"AgreeFg\", \"FacmNo\", \"BormNo\" "
				+ " FROM  \"Ias34Gp\" " + " WHERE \"DataYM\"  = :dataMonth "
				+ " ORDER BY \"CustNo\", \"AgreeNo\", \"AgreeFg\", \"FacmNo\", \"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM34GP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
