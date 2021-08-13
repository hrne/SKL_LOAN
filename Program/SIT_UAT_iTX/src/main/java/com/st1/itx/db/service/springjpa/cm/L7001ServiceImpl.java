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

import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("l7001ServiceImpl")
@Repository
/* LNM34AP 清單1 */
public class L7001ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L7001ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll() throws Exception {
		logger.info("L7001.findAll ");

		String sql = "SELECT \"CustNo\", \"CustId\", \"FacmNo\", \"ApplNo\", \"BormNo\", " + "\"AcCode\", \"Status\", \"FirstDrawdownDate\", \"DrawdownDate\", \"FacLineDate\", "
				+ "\"MaturityDate\", \"LineAmt\", \"DrawdownAmt\", \"AcctFee\", \"LoanBal\", " + "\"IntAmt\", \"Fee\", \"Rate\", \"OvduDays\", \"OvduDate\", "
				+ "\"BadDebtDate\", \"BadDebtAmt\", \"DerCode\", \"GracePeriod\", \"ApproveRate\", " + "\"AmortizedCode\", \"RateCode\", \"RepayFreq\", \"PayIntFreq\", \"IndustryCode\", "
				+ "\"ClTypeJCIC\", \"Zip3\", \"BaseRateCode\", \"CustKind\", \"AssetKind\", " + "\"ProdNo\", \"EvaAmt\", \"FirstDueDate\", \"TotalPeriod\", \"AgreeBefFacmNo\", "
				+ "\"AgreeBefBormNo\" " + " FROM  \"Ias34Ap\" " + " WHERE \"DataYM\" = 202005" + " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\"";

		logger.info("sql=" + sql);
		Query query;
//		query = em.createQuery(sql, L7001Vo.class);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		// 設定參數
//		query.setParameter("defno", defno);

//		List result = query.getResultList();
//		logger.info(">>>>> result size = " + result.size() );  
//		for (Object row : result) {  
//			Object[] cells = (Object[]) row;
//			// logger.info(">>>>> " + cells.length  );  		
//			for (int i=0; i < cells.length; i++) { 
//				logger.info(">>>>> " + cells[i] + ",");  
//			}
//		} 

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());

	}

//	private List convertToTranScanResponsetest(List list) {
//
//		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
//		
//		try {
//
//			for (Iterator iter = list.iterator(); iter.hasNext();) {
//
//				Object[] values = (Object[]) iter.next();
//
//				HashMap<String, String> m = new HashMap<String, String>();
//
//				for (int i = 0; i < values.length; i++) {
//
//					m.put("f"+Integer.toString(i+1), values[i].toString());
//
//				}
//
//				logger.info(" m : " + m);
//				result.add(m);
//
//			}
//
//		} catch (Exception e) {
//
//			this.error("Exception:" + e);
//
//		}
//
//		logger.info("result:" + result.size());
//
//		return result;
//	}
}