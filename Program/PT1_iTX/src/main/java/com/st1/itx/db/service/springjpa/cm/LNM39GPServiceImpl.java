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

@Service("lNM39GPServiceImpl")
@Repository

/*
 * LNM39GP 資料欄位清單7(放款與應收帳款-stage轉換用) LNFGP 放款stage轉換分類(撥款層)
 */

public class LNM39GPServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LNM39GP.findAll ---------------");
		this.info("-----LNM39GP TitaVo=" + titaVo);
		this.info("-----LNM39GP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// 清單7
		sql = "SELECT " + "  \"CustNo\", \"CustId\", \"FacmNo\", \"ApplNo\", \"BormNo\", "
				+ " \"CustKind\", \"Status\", \"OvduDate\", "
				+ " \"OriRating\", \"OriModel\", \"Rating\", \"Model\", \"OvduDays\", "
				+ " \"Stage1\", \"Stage2\", \"Stage3\", \"Stage4\", \"Stage5\", \"PdFlagToD\" "
				+ " FROM  \"LoanIfrs9Gp\" " + " WHERE \"DataYM\"   = :dataMonth "
				+ " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM39GP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
