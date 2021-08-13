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

@Service("lNM39APServiceImpl")
@Repository

/*
 * IFRS9 資料欄位清單1(表內放款與應收帳款-資產基本資料與計算原始有效利率用)
 * LNFAP 放款與應收帳款(撥款層)
 */

public class LNM39APServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LNM39APServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		logger.info("----------- LNM39AP.findAll ---------------");
		logger.info("-----LNM39AP TitaVo=" + titaVo);
		logger.info("-----LNM39AP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202004;
//		}

		logger.info("dataMonth= " + dateMonth);

		String sql = "";

		// IFRS9 資料欄位清單1
		sql = "SELECT \"CustNo\", \"CustId\", \"FacmNo\", \"ApplNo\", \"BormNo\", " +
				" \"AcCode\", \"Status\", \"FirstDrawdownDate\", \"DrawdownDate\", \"FacLineDate\", \"MaturityDate\", " +
				" \"LineAmt\", \"DrawdownAmt\", \"AcctFee\", \"LoanBal\", \"IntAmt\", \"Fee\", \"Rate\", " +
				" \"OvduDays\", \"OvduDate\", \"BadDebtDate\", \"BadDebtAmt\", \"GracePeriod\", \"ApproveRate\", " +
				" \"AmortizedCode\", \"RateCode\", \"RepayFreq\", \"PayIntFreq\", " +
				" \"IndustryCode\", \"ClTypeJCIC\", \"CityCode\", \"ProdNo\", \"CustKind\", \"AssetClass\", \"IfrsProdCode\", " +
				" \"EvaAmt\", \"FirstDueDate\", \"TotalPeriod\", \"AvblBal\", \"RecycleCode\", \"IrrevocableFlag\", " +
				" \"TempAmt\", \"AcCurcd\", \"AcBookCode\", \"CurrencyCode\", \"ExchangeRate\", " +
				" \"LineAmtCurr\", \"DrawdownAmtCurr\", \"AcctFeeCurr\", \"LoanBalCurr\", " +
				" \"IntAmtCurr\", \"FeeCurr\", \"AvblBalCurr\", \"TempAmtCurr\" " +				
				" FROM  \"LoanIfrsAp\"" + 
	            " WHERE \"DataYM\" = " + dateMonth +
				" ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\" " ;

		logger.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM39AP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
