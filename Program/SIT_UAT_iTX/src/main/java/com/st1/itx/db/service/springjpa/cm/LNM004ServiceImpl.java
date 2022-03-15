package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("lNM004ServiceImpl")
@Repository

/*
 * LNM004 清單4：放款與AR-估計回收率用
 */

public class LNM004ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LNM004.findAll ---------------");
		this.info("-----LNM004 TitaVo=" + titaVo);
		this.info("-----LNM004 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202004;
//		}

		this.info("dataMonth= " + dateMonth);

		String sql = "";

		// 清單4：放款與AR-估計回收率用
		sql = "SELECT A.\"CustNo\"" + "     , A.\"CustId\"" + "     , A.\"FacmNo\"" + "     , A.\"BormNo\"" + "     , A.\"AcCode\"" + "     , A.\"Status\"" + "     , A.\"FirstDrawdownDate\""
				+ "     , A.\"DrawdownDate\"" + "     , A.\"MaturityDate\"" + "     , A.\"LineAmt\"" + "     , A.\"DrawdownAmt\"" + "     , A.\"LoanBal\"" + "     , A.\"IntAmt\"" + "     , A.\"Fee\""
				+ "     , A.\"OvduDays\"" + "     , A.\"OvduDate\"" + "     , A.\"BadDebtDate\"" + "     , A.\"BadDebtAmt\"" + "     , A.\"DerDate\"" + "     , A.\"DerRate\""
				+ "     , A.\"DerLoanBal\"" + "     , A.\"DerIntAmt\"" + "     , A.\"DerFee\"" + "     , A.\"DerY1Amt\"" + "     , A.\"DerY2Amt\"" + "     , A.\"DerY3Amt\"" + "     , A.\"DerY4Amt\""
				+ "     , A.\"DerY5Amt\"" + "     , A.\"DerY1Int\"" + "     , A.\"DerY2Int\"" + "     , A.\"DerY3Int\"" + "     , A.\"DerY4Int\"" + "     , A.\"DerY5Int\"" + "     , A.\"DerY1Fee\""
				+ "     , A.\"DerY2Fee\"" + "     , A.\"DerY3Fee\"" + "     , A.\"DerY4Fee\"" + "     , A.\"DerY5Fee\"" + "     , A.\"IndustryCode\"" + "     , A.\"ClKindCode\"" + "     , CASE"
				+ "         WHEN \"CdArea\".\"CityCode\" = '05' THEN 'A'" + "         WHEN \"CdArea\".\"CityCode\" = '10' THEN 'B'" + "         WHEN \"CdArea\".\"CityCode\" = '15' THEN 'C'"
				+ "         WHEN \"CdArea\".\"CityCode\" = '35' THEN 'D'" + "         WHEN \"CdArea\".\"CityCode\" = '65' THEN 'E'" + "         WHEN \"CdArea\".\"CityCode\" = '70' THEN 'F'"
				+ "         ELSE 'G'" + "       END                   AS  \"AreaCode\"" + "     , A.\"ProdRateCode\"" + "     , A.\"CustKind\"" + "     , A.\"ProdNo\"" + " FROM \"Ias34Dp\" A"
				+ "   LEFT JOIN \"CdArea\"  ON \"CdArea\".\"Zip3\"  = A.\"AreaCode\"" + " WHERE A.\"DataYM\" = " + dateMonth + " ORDER BY A.\"CustNo\", A.\"FacmNo\", A.\"BormNo\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM004.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
