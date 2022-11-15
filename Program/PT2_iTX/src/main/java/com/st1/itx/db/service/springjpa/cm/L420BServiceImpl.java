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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.parse.Parse;

@Service("L420BServiceImpl")
@Repository
/* 逾期放款明細 */
public class L420BServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;
	
	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {

		this.info("L4920.findAll");

//		會計日期    #AcDate
//		整批批號    #BatchNo
///		對帳類別    # ReconCode			

		int iAcDate = parse.stringToInteger(titaVo.get("AcDate")) + 19110000;
		String iBatchNo = titaVo.get("BatchNo").trim();
		String iReconCode = titaVo.get("ReconCode").trim();

		this.info("acDate = " + iAcDate);
		this.info("iBatchNo = " + iBatchNo);
		this.info("iReconCode = " + iReconCode);

		String sql = "";
		sql += " select                ";
		sql += "   bd.*                ";
		sql += " from \"BatxDetail\" bd                                                      ";
		sql += " left join \"LoanBorTx\" ln  											     ";
		sql += "   ON  ln.\"AcDate\" = bd.\"AcDate\"   							     		 ";
		sql += "  AND  ln.\"AcSeq\"  = 1 								                     ";
		sql += "  AND  ln.\"TitaTlrNo\" = bd.\"TitaTlrNo\"   								 ";
		sql += "  AND  ln.\"TitaTxtNo\" = bd.\"TitaTxtNo\"   								 ";
		sql += " where bd.\"AcDate\" =       " + iAcDate;
		sql += "   and bd.\"BatchNo\" = '" + iBatchNo + "'";
		sql += "   and bd.\"ProcStsCode\" in ('5','6','7')                                   ";
		if (!iReconCode.isEmpty()) {
			sql += "   and bd.\"ReconCode\" = '" + iReconCode + "'";
		}
		sql += " ORDER BY ln.\"TitaCalDy\" Desc	                    						 ";
		sql += "         ,ln.\"TitaCalTm\"	Desc                    					     ";
		sql += "         ,ln.\"TitaTxtNo\" Desc             	    					     ";
		sql += " " + sqlRow;

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);
		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
//		query.setFirstResult(this.index * this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);
		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(query);
	}

	public int getSize() {
		return cnt;
	}
}