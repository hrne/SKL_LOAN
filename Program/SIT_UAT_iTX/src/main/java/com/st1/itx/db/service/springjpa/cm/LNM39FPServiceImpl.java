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

@Service("lNM39FPServiceImpl")
@Repository

/*
 * LNM39FP 資料欄位清單6(放款與應收帳款-協商戶用)  LNFFP 放款協商戶資訊(撥款層)
 */

public class LNM39FPServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LNM39FP.findAll ---------------");
		this.info("-----LNM39FP TitaVo=" + titaVo);
		this.info("-----LNM39FP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202004;
//		}

		this.info("dataMonth= " + dateMonth);

		String sql = "";

		// 清單6
		sql = "SELECT " + "  \"CustNo\", \"CustId\", \"AgreeNo\", \"AgreeFg\", \"FacmNo\", \"BormNo\" "
				+ " FROM  \"LoanIfrs9Fp\" " + " WHERE \"DataYM\"= :dateMonth "
				+ " ORDER BY \"CustNo\", \"AgreeNo\", \"AgreeFg\", \"FacmNo\", \"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM39FP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dateMonth", dateMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
