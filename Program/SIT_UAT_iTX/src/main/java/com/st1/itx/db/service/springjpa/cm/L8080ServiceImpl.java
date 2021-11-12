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

@Service("L8080ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L8080ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L8080.findAll");

		String iBrNo = titaVo.getParam("BrNo").trim();
		String iStatus = titaVo.getParam("Status").trim();
		int iAcDate1 = Integer.valueOf(titaVo.getParam("AcDate1")) + 19110000;
		int iAcDate2 = Integer.valueOf(titaVo.getParam("AcDate2")) + 19110000;
		int iTypeCode = parse.stringToInteger(titaVo.getParam("TypeCode"));

		String sql = " select * from ( select                                    ";
		sql += "  t.\"LogNo\"                                                    ";
		sql += " ,t.\"Entdy\"                                                    ";
		sql += " ,t.\"TransactionId\"                                            ";
		sql += " ,t.\"AcctNo\"       	                                         ";
		sql += " ,t.\"CaseNo\"         	                                         ";
		sql += " ,t.\"MsgRg\"      		                                         ";
		sql += " ,t.\"ConfirmStatus\"                                            ";
		sql += " ,t.\"ConfirmCode\"                                              ";
		sql += " ,t.\"ConfirmEmpNo\"                                             ";
		sql += " ,t.\"ConfirmTranCode\"                                          ";
		sql += " from \"TxAmlLog\" t                                             ";
		sql += " where                                                           ";

		if (iTypeCode != 9) {
			switch (iTypeCode) {
			case 0:
				sql += " case                                                         ";
				sql += " when SUBSTR(\"CaseNo\",0,2) in ('LN','RT')                   ";
				sql += " THEN 1                                  				      ";
				sql += " when (\"CaseNo\") = 'L3110'                                  ";
				sql += " THEN 1                                  					  ";
				sql += " ELSE 0 END >0                                  			  ";
				sql += " AND 			                                  			  ";
//				L3110
//				LNnnnn
				break;
			case 1:
				sql += " case                                                         ";
				sql += " when (\"CaseNo\") = 'AUTH'                                   ";
				sql += " THEN 1                                  					  ";
				sql += " ELSE 0 END >0                                  			  ";
				sql += " AND 			                                  			  ";
//				AUTH
				break;
			case 2:
				sql += " case                                                         ";
				sql += " when (\"CaseNo\") = 'DEDUCT'                                 ";
				sql += " THEN 1                                  					  ";
				sql += " ELSE 0 END >0                                  			  ";
				sql += " AND 			                                  			  ";
//				DEDUCT
				break;
			case 3:
				sql += " case                                                         ";
				sql += " when SUBSTR(\"CaseNo\",0,4) = 'BATX'                         ";
				sql += " THEN 1                                  				      ";
				sql += " ELSE 0 END >0                                  			  ";
				sql += " AND 			                                  			  ";
//				BATXnn
				break;
			}
		}
		if ("9".equals(iStatus)) {
			sql += "            \"BrNo\" = " + iBrNo;
			sql += "        and \"Entdy\" >= " + iAcDate1;
			sql += "        and \"Entdy\" <= " + iAcDate2;
		} else {
			sql += "            \"ConfirmStatus\" = " + iStatus;
			sql += "        and \"BrNo\" = " + iBrNo;
			sql += "        and \"Entdy\" >= " + iAcDate1;
			sql += "        and \"Entdy\" <= " + iAcDate2;
		}

		sql += " order by t.\"CreateDate\" Desc ";
		sql += " )  ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(titaVo);
	}

	public int getSize() {
		return cnt;
	}
}