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
import com.st1.itx.util.common.data.L6R25Vo;

@Service("l6r25ServiceImpl")
@Repository

public class L6R25ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L6R25ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<L6R25Vo> findAll(String authno, String aptype) throws Exception {
		logger.info("L6R25.findAll authno/aptype = " + authno + '/' + aptype);
//		int defno = 0;
//		if ("L1".equals(aptype)) {
//			defno = 9001;
//		} else if ("L2".equals(aptype)) {
//			defno = 9002;
//		} else if ("L3".equals(aptype)) {
//			defno = 9003;
//		} else if ("L4".equals(aptype)) {
//			defno = 9004;
//		} else if ("L5".equals(aptype)) {
//			defno = 9005;
//		} else if ("L6".equals(aptype)) {
//			defno = 9006;
//		} else if ("L7".equals(aptype)) {
//			defno = 9007;
//		} else if ("L8".equals(aptype)) {
//			defno = 9008;
//		} else if ("L9".equals(aptype)) {
//			defno = 9009;
//		}

		String defcode = "SubMenu" + aptype;

		String queryttext = "select NEW com.st1.itx.util.common.data.L6R25Vo";
		queryttext += "(a.tranNo,a.tranItem,a.menuNo,a.subMenuNo,b.item,a.menuFg,a.typeFg,d.authFg) ";
		queryttext += "from TxTranCode a ";
		queryttext += "left join CdCode b on b.defCode = :defcode and b.code = a.subMenuNo ";
		queryttext += "left join TxAuthGroup c on c.authNo = :authno ";
		queryttext += "left join TxAuthority d on d.authNo = c.authNo and d.tranNo = a.tranNo ";
		queryttext += "where a.menuNo = :menuno and a.status=0 ";
		queryttext += "order by a.menuNo,a.subMenuNo,a.tranNo ";
		logger.info("queryttext=" + queryttext);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createQuery(queryttext, L6R25Vo.class);
		query.setParameter("defcode", defcode);
		query.setParameter("menuno", aptype);
		query.setParameter("authno", authno);

		return query.getResultList();
	}

}