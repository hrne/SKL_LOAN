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

@Service("lNM39DPServiceImpl")
@Repository

/*
 * LNM39DP LNM39 欄位清單D
 */

public class LNM39DPServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LNM39DP.findAll ---------------");
		this.info("-----LNM39DP TitaVo=" + titaVo);
		this.info("-----LNM39DP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202003;
//		}

		this.info("dataMonth= " + dateMonth);

		String sql = "";

		// LNM39DP 欄位清單D
		sql = "SELECT " + "  \"CustNo\", \"CustId\", \"FacmNo\", \"BormNo\" " + ", \"AcCode\", \"Status\", \"FirstDrawdownDate\", \"DrawdownDate\", \"MaturityDate\" "
				+ ", \"LineAmt\", \"DrawdownAmt\", \"LoanBal\", \"IntAmt\", \"Fee\" " + ", \"OvduDays\", \"OvduDate\", \"BadDebtDate\", \"BadDebtAmt\" "
				+ ", \"DerDate\", \"DerRate\", \"DerLoanBal\", \"DerIntAmt\", \"DerFee\" " + ", \"DerY1Amt\", \"DerY2Amt\", \"DerY3Amt\", \"DerY4Amt\", \"DerY5Amt\" "
				+ ", \"DerY1Int\", \"DerY2Int\", \"DerY3Int\", \"DerY4Int\", \"DerY5Int\" " + ", \"DerY1Fee\", \"DerY2Fee\", \"DerY3Fee\", \"DerY4Fee\", \"DerY5Fee\" "
				+ ", \"IndustryCode\", \"ClTypeJCIC\", \"AreaCode\", \"ProdCode\", \"CustKind\", \"Ifrs9ProdCode\" " + " FROM  \"LoanIfrs9Dp\" " + " WHERE \"DataYM\" = " + dateMonth
				+ " ORDER BY \"DataFg\", \"CustNo\", \"FacmNo\", \"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM39DP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
