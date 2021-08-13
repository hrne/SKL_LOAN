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
import com.st1.itx.util.common.data.BS020Vo;

/**
 * 應繳期款戶號清單by繳款方式
 * 
 * @author Lai
 * @version 1.0.0
 */
@Service("BS020ServiceImpl")
@Repository
public class BS020ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(BS020ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<BS020Vo> find(int sDate, int eDate, int repayCode) throws Exception {
		String queryttext = "select NEW com.st1.itx.util.common.data.BS020Vo";
		queryttext += "(a.custNo, a.facmNo, a.bormNo) ";
		queryttext += "from LoanBorMain a ";
		queryttext += "left join FacMain f on f.custNo = a.custNo and f.facmNo = a.facmNo ";
		queryttext += "left join AcReceivable r on r.custNo = a.custNo and r.facmNo = a.facmNo and r.acctCode = 'TAV' and r.clsFlag = 0 ";
		queryttext += "where a.status in (0,4) "; // 戶況 0: 正常戶, 4: 逾期戶
		queryttext += "  and a.nextPayIntDate >= " + sDate;
		queryttext += "  and a.nextPayIntDate <= " + eDate ;
		if (repayCode > 0)
			queryttext += "  and f.repayCode = " + repayCode;
		queryttext += "  and r.custNo is not null ";
		queryttext += "order by a.custNo, a.facmNo, a.bormNo ";

		logger.info("queryttext=" + queryttext);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createQuery(queryttext, BS020Vo.class);
		return query.getResultList();
	}
}