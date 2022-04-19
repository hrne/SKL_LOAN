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

@Service("l7005ServiceImpl")
@Repository
/* LNM34BP 清單2 */
public class L7005ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L7005ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll() throws Exception {
		logger.info("L7005.findAll ");

		String sql = "SELECT \"DataYM\", \"CustNo\", \"CustId\", \"FacmNo\", \"BormNo\", " + "\"AcCode\", \"Status\", \"IndustryCode\", \"ClKindCode\", \"AreaCode\", "
				+ "\"ProdRateCode\", \"CustKind\", \"DerFg\", \"ProdNo\" " + " FROM  \"Ias34Ep\" " + " WHERE \"DataYM\" = 202005" + " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\" ";

		logger.info("sql=" + sql);
		Query query;
//		query = em.createQuery(sql, L7005Vo.class);
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

		// 轉成 List<HashMap<String, String>> (for EXCEL)
		return this.convertToMap(query.getResultList());
	}
}