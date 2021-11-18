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
import com.st1.itx.util.parse.Parse;

@Service
@Repository
/* 逾期放款明細 */
public class LC001ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int index, int limit) throws Exception {

		int iEntday = Integer.valueOf(titaVo.get("iEntdy").trim()) + 19110000;
		String iBrNo = titaVo.get("iBrNo").trim();
		String iTlrNo = titaVo.get("iTlrNo").trim();
		String iTranNo = titaVo.get("iTranNo").trim();

		String sql = "SELECT  A.\"CalDate\"";
		sql += ",A.\"CalTime\"";
		sql += ",A.\"Entdy\"";
		sql += ",A.\"TxNo\"";
		sql += ",A.\"TranNo\"||' '||B.\"TranItem\" TranX";
		sql += ",A.\"MrKey\"";
		sql += ",A.\"CurName\"";
		sql += ",A.\"TxAmt\"";
		sql += ",A.\"BrNo\"||' '||C.\"BranchShort\" BrX";
		sql += ",A.\"TlrNo\"||' '||D.\"TlrItem\" TlrX";
		sql += ",A.\"FlowType\"";
		sql += ",A.\"FlowStep\"";
		sql += ",E.\"FlowStep\" FlowStep2";
		sql += ",E.\"SubmitFg\"";
		sql += ",E.\"FlowMode\"";
		sql += " from \"TxRecord\" A";
		sql += " left join \"TxTranCode\" B on B.\"TranNo\"=A.\"TranNo\"";
		sql += " left join \"CdBranch\" C on C.\"BranchNo\"=A.\"BrNo\"";
		sql += " left join \"TxTeller\" D on D.\"TlrNo\"=A.\"TlrNo\"";
		sql += " left join \"TxFlow\" E on E.\"Entdy\"=A.\"Entdy\" AND E.\"FlowNo\"=A.\"FlowNo\"";
		sql += " where A.\"Entdy\"=:entdy";
		sql += "   and A.\"BrNo\"=:brno";
		sql += "   and A.\"TxResult\"=:txresult";
		sql += "   and A.\"CanCancel\"=:cancancel";
		sql += "   and A.\"ActionFg\"=:actionfg";
		sql += "   and A.\"Hcode\"<>:hcode";

		if (!"".equals(iTlrNo)) {
			sql += "   and A.\"TlrNo\" like :tlrno";
		}
		if (!"".equals(iTranNo)) {
			sql += "   and A.\"TranNo\" like :tranNo";
		}

		sql += " order by A.\"CreateDate\" DESC";

		sql += " OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

		this.info("LC001ServiceImpl sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("entdy", iEntday);
		query.setParameter("brno", iBrNo);
		query.setParameter("txresult", 'S');
		query.setParameter("cancancel", 1);
		query.setParameter("actionfg", 0);
		query.setParameter("hcode", 1);
		if (!"".equals(iTlrNo)) {
			query.setParameter("tlrno", iTlrNo + "%");
		}
		if (!"".equals(iTranNo)) {
			query.setParameter("tranNo", iTranNo + "%");
		}

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		return this.convertToMap(query);
	}

}