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

@Service("lNM39HPServiceImpl")
@Repository

/*
 * LNM39HP 清單8：(放款風險參數檔(額度層)(LNFHP))
 */

public class LNM39HPServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LNM39HPServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		logger.info("----------- LNM39HP.findAll ---------------");
		logger.info("-----LNM39HP TitaVo=" + titaVo);
		logger.info("-----LNM39HP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202004;
//		}

		logger.info("dataMonth= " + dateMonth);

		String sql = "";

		// 清單8
		sql = "SELECT " + "  \"CustNo\", \"CustId\", \"FacmNo\", \"ApplNo\", \"CustKind\", \"ApproveDate\", "
				+ " \"FirstDrawdownDate\", \"LineAmt\", \"IfrsProdCode\", \"AvblBal\", " 
				+ " \"RecycleCode\", \"IrrevocableFlag\", \"IndustryCode\", " 
				+ " \"OriRating\", \"OriModel\", \"Rating\", \"Model\", \"LGDModel\", \"LGD\", " 
				+ " \"LineAmtCurr\", \"AvblBalCurr\" " 
		        + " FROM  \"LoanIfrsHp\" "
		        + " WHERE \"DataYM\" = " + dateMonth
		        + " ORDER BY \"CustNo\", \"FacmNo\" ";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM39HP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
