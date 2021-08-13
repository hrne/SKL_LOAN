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

@Service("lNM34APServiceImpl")
@Repository

/*
 * LNM34AP LNM34 資料欄位清單A
 */

public class LNM34APServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LNM34APServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		logger.info("----------- LNM34AP.findAll ---------------");
		logger.info("-----LNM34AP TitaVo=" + titaVo);
		logger.info("-----LNM34AP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202003;
//		}

		logger.info("dataMonth= " + dateMonth);

		String sql = "";

		// LNM34AP 資料欄位清單A
		sql = "SELECT " + "  \"CustNo\", \"CustId\", \"FacmNo\", \"ApplNo\", \"BormNo\" " + ", \"AcCode\", \"Status\", \"FirstDrawdownDate\", \"DrawdownDate\", \"FacLineDate\" "
				+ ", \"MaturityDate\", \"LineAmt\", \"DrawdownAmt\", \"AcctFee\", \"LoanBal\" " + ", \"IntAmt\", \"Fee\", \"Rate\", \"OvduDays\", \"OvduDate\" "
				+ ", \"BadDebtDate\", \"BadDebtAmt\", \"DerCode\", \"GracePeriod\", \"ApproveRate\" " + ", \"AmortizedCode\", \"RateCode\", \"RepayFreq\", \"PayIntFreq\", \"IndustryCode\" "
				+ ", \"ClTypeJCIC\", \"Zip3\" "
				+ ", \"ProdNo\", \"CustKind\", \"AssetClass\" " 
				+ ", \"IfrsProdCode\", \"EvaAmt\", \"FirstDueDate\", \"TotalPeriod\" " + ", \"AgreeBefFacmNo\", \"AgreeBefBormNo\" "
				+ " FROM  \"Ias34Ap\" " + " WHERE \"DataYM\" = " + dateMonth + " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\" ";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM34AP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
