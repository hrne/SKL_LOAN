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
import com.st1.itx.util.parse.Parse;

@Service("L8080ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L8080ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L8080.findAll");

		String iBrNo = titaVo.getParam("BrNo").trim();
		String iStatus = titaVo.getParam("Status").trim();
		int iAcDate1 = Integer.valueOf(titaVo.getParam("AcDate1")) + 19110000;
		int iAcDate2 = Integer.valueOf(titaVo.getParam("AcDate2")) + 19110000;
		int iTypeCode = parse.stringToInteger(titaVo.getParam("TypeCode"));
		String custId = titaVo.getParam("CustId").trim();
		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		String sql = "";
		sql += " select t.\"LogNo\" ";
		sql += "      , t.\"Entdy\" ";
		sql += "      , t.\"TransactionId\" ";
		sql += "      , t.\"AcctNo\" ";
		sql += "      , t.\"CaseNo\" ";
		sql += "      , t.\"MsgRg\" ";
		sql += "      , t.\"ConfirmStatus\" ";
		sql += "      , t.\"ConfirmCode\" ";
		sql += "      , t.\"ConfirmEmpNo\" "; 
		sql += "      , t.\"ConfirmTranCode\" ";
		sql += " from \"TxAmlLog\" t ";
		sql += " where \"BrNo\" = :iBrNo ";
		sql += "   and \"Entdy\" >= :iAcDate1 ";
		sql += "   and \"Entdy\" <= :iAcDate2 ";

		if (iTypeCode != 9) {
			switch (iTypeCode) {
			case 0:
				sql += " case ";
				sql += "   when SUBSTR(\"CaseNo\",0,2) in ('LN','RT') ";
				sql += "   then 1 ";
				sql += "   when \"CaseNo\" = 'L3110' ";
				sql += "   then 1 ";
				sql += " else 0 end > 0 ";
//				L3110
//				LNnnnn
				break;
			case 1:
				sql += " case ";
				sql += "   when (\"CaseNo\") = 'AUTH' ";
				sql += "   then 1 ";
				sql += " else 0 end > 0 ";
//				AUTH
				break;
			case 2:
				sql += " case ";
				sql += " when (\"CaseNo\") = 'DEDUCT' ";
				sql += " THEN 1 ";
				sql += " ELSE 0 END > 0 ";
//				DEDUCT
				break;
			case 3:
				sql += " case ";
				sql += "   when SUBSTR(\"CaseNo\",0,4) = 'BATX' ";
				sql += "   then 1 ";
				sql += " else 0 end > 0 ";
//				BATXnn
				break;
			}
		}
		if (!"9".equals(iStatus)) {
			sql += " and \"ConfirmStatus\" = " + iStatus;
		}
		if (custId != null && !custId.isEmpty()) {
			sql += " and \"CustId\" = :custId ";
		}
		if (custNo != 0) {
			sql += " and \"CustNo\" = :custNo ";
		}

		sql += " order by t.\"CreateDate\" Desc ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		
		query.setParameter("iBrNo", iBrNo);
		query.setParameter("iAcDate1", iAcDate1);
		query.setParameter("iAcDate2", iAcDate2);
		
		if (!"9".equals(iStatus)) {
			query.setParameter("iStatus", iStatus);
		}
		if (custId != null && !custId.isEmpty()) {
			query.setParameter("custId", custId);
		}
		if (custNo != 0) {
			query.setParameter("custNo", custNo);
		}

		return switchback(query);
	}

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(titaVo);
	}
}