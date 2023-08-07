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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2078ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L2078ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;


	@Override
	public void afterPropertiesSet() throws Exception {
//		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int index, int limit) throws Exception {

		this.info("L2078.findAll");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		// tita
		int iReceiveDateStart = parse.stringToInteger(titaVo.getParam("ReceiveDateStart")) + 19110000;
		int iReceiveDateEnd = parse.stringToInteger(titaVo.getParam("ReceiveDateEnd")) + 19110000;
		int iCustNoStart = parse.stringToInteger(titaVo.getParam("CustNoStart"));
		int CustNoEnd = parse.stringToInteger(titaVo.getParam("CustNoEnd"));

		String sql = " SELECT  ";
		sql += "   ff.\"RecordNo\"  		AS \"RecordNo\"  ";
		sql += " , ff.\"ReceiveDate\"		AS \"ReceiveDate\"  ";
		sql += " , ff.\"CustNo\"			AS \"CustNo\"  ";
		sql += " , ff.\"FacmNo\"			AS \"FacmNo\"  ";
		sql += " , ff.\"Fee\"				AS \"Fee\"  ";
		sql += " , ff.\"FeeCode\"			AS \"FeeCode\"  ";
		sql += " , ff.\"CaseCode\"			AS \"CaseCode\"  ";
		sql += " , ff.\"RemitBranch\"		AS \"RemitBranch\"  ";
		sql += " , ff.\"CaseNo\"			AS \"CaseNo\"  ";
		sql += " , ff.\"CloseDate\"			AS \"CloseDate\"  ";
		sql += " , ff.\"CloseNo\"			AS \"CloseNo\"  ";
		sql += " , ff.\"DocDate\"			AS \"DocDate\"  ";
		sql += " , ff.\"LegalStaff\"		AS \"LegalStaff\"  ";
		sql += " , ff.\"Rmk\"				AS \"Rmk\"  ";
		sql += " , ff.\"OverdueDate\"		AS \"OverdueDate\"  ";
		sql += " , CASE WHEN ff.\"OverdueDate\" > 0 THEN 'Y' ";
		sql += "        ELSE ' ' END 	AS \"OverdueFg\" ";
		sql += " FROM \"ForeclosureFee\" ff ";
		sql += " WHERE  ff.\"ReceiveDate\" >= :receiveDateS  ";
		sql += " AND  ff.\"ReceiveDate\" <= :receiveDateE  ";
		sql += " AND  ff.\"CustNo\" >= :custNoS  ";
		sql += " AND  ff.\"CustNo\" <= :custNoE  ";
		sql += " ORDER BY   ";
		sql += "  ff.\"CloseNo\" ASC   ";
		sql += " ,ff.\"RecordNo\" ASC   ";
		sql += "     ";

		this.info("sql=" + sql);


		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		this.info("receiveDateS = " + iReceiveDateStart);
		this.info("receiveDateE = " + iReceiveDateEnd);
		this.info("custNoS = " + iCustNoStart);
		this.info("custNoE = " + CustNoEnd);
		query.setParameter("receiveDateS", iReceiveDateStart);
		query.setParameter("receiveDateE", iReceiveDateEnd);
		query.setParameter("custNoS", iCustNoStart);
		query.setParameter("custNoE", CustNoEnd);
		this.info("L2078Service FindData=" + query.toString());


		return switchback(query);
	}

}