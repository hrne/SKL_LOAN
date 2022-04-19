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

@Service("lNM39IPServiceImpl")
@Repository

/*
 * LNM39IP 資料欄位清單9(表外放款與應收帳款-資產基本資料與計算原始有效利率用)) LNFIP 放款已核准未動撥額度(額度層)
 */

public class LNM39IPServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LNM39IP.findAll ---------------");
		this.info("-----LNM39IP TitaVo=" + titaVo);
		this.info("-----LNM39IP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// IFRS9 資料欄位清單9
		sql = "SELECT \"CustNo\", \"CustId\", \"FacmNo\", \"ApplNo\", "
				+ " \"ApproveDate\", \"FirstDrawdownDate\", \"LineAmt\", \"AcctFee\", \"Fee\", "
				+ " \"ApproveRate\", \"GracePeriod\", \"AmortizedCode\", \"RateCode\", \"RepayFreq\", \"PayIntFreq\", "
				+ " \"IndustryCode\", \"ClTypeJCIC\", \"CityCode\", \"ProdNo\", \"CustKind\", \"Ifrs9ProdCode\", "
				+ " \"EvaAmt\", \"AvblBal\", \"RecycleCode\", \"IrrevocableFlag\", \"LoanTerm\", "
				+ " \"AcCode\", \"AcCurcd\", \"AcBookCode\", \"CurrencyCode\", \"ExchangeRate\", "
				+ " \"LineAmtCurr\", \"AcctFeeCurr\", \"FeeCurr\" " + " FROM  \"LoanIfrs9Ip\" "
				+ " WHERE \"DataYM\"  = :dataMonth " + " ORDER BY \"DrawdownFg\", \"CustNo\", \"FacmNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM39IP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
