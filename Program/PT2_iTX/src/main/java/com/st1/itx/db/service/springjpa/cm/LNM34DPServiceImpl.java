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

@Service("lNM34DPServiceImpl")
@Repository

/*
 * LNM34DP LNM34 資料欄位清單D
 */

public class LNM34DPServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LNM34DP.findAll ---------------");
		this.info("-----LNM34DP TitaVo=" + titaVo);
		this.info("-----LNM34DP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// LNM34DP 資料欄位清單D
		sql = "SELECT " + "  \"CustNo\", \"CustId\", \"FacmNo\", \"BormNo\" " + ", \"AcCode\", \"Status\", \"FirstDrawdownDate\", \"DrawdownDate\", \"MaturityDate\" "
				+ ", \"LineAmt\", \"DrawdownAmt\", \"LoanBal\", \"IntAmt\", \"Fee\" " + ", \"OvduDays\", \"OvduDate\", \"BadDebtDate\", \"BadDebtAmt\" "
				+ ", \"DerDate\", \"DerRate\", \"DerLoanBal\", \"DerIntAmt\", \"DerFee\" " + ", \"DerY1Amt\", \"DerY2Amt\", \"DerY3Amt\", \"DerY4Amt\", \"DerY5Amt\" "
				+ ", \"DerY1Int\", \"DerY2Int\", \"DerY3Int\", \"DerY4Int\", \"DerY5Int\" " + ", \"DerY1Fee\", \"DerY2Fee\", \"DerY3Fee\", \"DerY4Fee\", \"DerY5Fee\" "
				+ ", \"IndustryCode\", \"ClTypeJCIC\", \"Zip3\", \"ProdCode\", \"CustKind\", \"Ifrs9ProdCode\" " + " FROM  \"Ias34Dp\" " + " WHERE \"DataYM\"  = :dataMonth "
				+ " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM34DP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
