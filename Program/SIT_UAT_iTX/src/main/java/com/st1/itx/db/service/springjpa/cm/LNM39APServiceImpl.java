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

@Service("lNM39APServiceImpl")
@Repository

/*
 * LNM39AP 資料欄位清單1(表內放款與應收帳款-資產基本資料與計算原始有效利率用) LNFAP 放款與應收帳款(撥款層)
 */

public class LNM39APServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LNM39AP.findAll ---------------");
		this.info("-----LNM39AP TitaVo=" + titaVo);
		this.info("-----LNM39AP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// IFRS9 資料欄位清單1
		sql = "SELECT \"CustNo\", \"CustId\", \"FacmNo\", \"ApplNo\", \"BormNo\", "
				+ " \"AcCode\", \"Status\", \"FirstDrawdownDate\", \"DrawdownDate\", \"FacLineDate\", \"MaturityDate\", "
				+ " \"LineAmt\", \"DrawdownAmt\", \"AcctFee\", \"LoanBal\", \"IntAmt\", \"Fee\", \"Rate\", "
				+ " \"OvduDays\", \"OvduDate\", \"BadDebtDate\", \"BadDebtAmt\", \"GracePeriod\", \"ApproveRate\", "
				+ " \"AmortizedCode\", \"RateCode\", \"RepayFreq\", \"PayIntFreq\", "
				+ " \"IndustryCode\", \"ClTypeJCIC\", \"CityCode\", \"ProdNo\", \"CustKind\", \"AssetClass\", \"Ifrs9ProdCode\", "
				+ " \"EvaAmt\", \"FirstDueDate\", \"TotalPeriod\", \"AvblBal\", \"RecycleCode\", \"IrrevocableFlag\", "
				+ " \"TempAmt\", \"AcCurcd\", \"AcBookCode\", \"CurrencyCode\", \"ExchangeRate\", "
				+ " \"LineAmtCurr\", \"DrawdownAmtCurr\", \"AcctFeeCurr\", \"LoanBalCurr\", "
				+ " \"IntAmtCurr\", \"FeeCurr\", \"AvblBalCurr\", \"TempAmtCurr\" " + " FROM  \"LoanIfrs9Ap\""
				+ " WHERE \"DataYM\"  = :dataMonth " + " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM39AP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
