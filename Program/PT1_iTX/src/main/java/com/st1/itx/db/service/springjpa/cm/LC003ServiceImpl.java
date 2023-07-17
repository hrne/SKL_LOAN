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

public class LC003ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int index, int limit) throws Exception {
		// 放行查詢
		String iTlrNo = titaVo.get("TLRNO").trim();
		int iEntday = Integer.valueOf(titaVo.get("iEntdy").trim());
		int iEntdayS = 0;
		int iEntdayE = 99991231;
		if (iEntday > 0) {
			iEntday = iEntday + 19110000;
			iEntdayS = iEntday;
			iEntdayE = iEntday;
		}
		String iBrNo = titaVo.get("iBrNo").trim();
		String iTranNo = titaVo.get("iTranNo").trim();

		this.info("TLRNO = " + iTlrNo);
		this.info("iBrNo = " + iBrNo);
		this.info("iTranNo = " + iTranNo);
		this.info("index = " + index);
		this.info("limit = " + limit);

		String sql = "";
		sql += "SELECT";
		sql += " E.\"CalDate\" - 19110000 AS \"CalDate\" ";
		sql += ",E.\"CalTime\"";
		sql += ",E.\"Entdy\" - 19110000 AS \"Entdy\" ";
		sql += ",E.\"TxNo\"";
		sql += ",NVL(JSON_VALUE(E.\"TranData\", '$.iCode'),'') AS \"iCode\" ";
		sql += ",NVL(JSON_VALUE(E.\"TranData\", '$.FileNm'),'') AS \"FileNm\" ";
		sql += ",NVL(JSON_VALUE(E.\"TranData\", '$.iItem'),'') AS \"iItem\" ";
		sql += ",E.\"TranNo\"";
		sql += ",B.\"TranItem\"    AS \"TranNoX\" ";
		sql += ",E.\"MrKey\"";
		sql += ",E.\"CurName\"";
		sql += ",E.\"TxAmt\"";
		sql += ",A.\"BrNo\"";
		sql += ",C.\"BranchShort\" AS \"BrNoX\" ";
		sql += ",E.\"TlrNo\"";
		sql += ",D.\"Fullname\" AS \"TlrNoX\" ";
		sql += ",A.\"FlowType\"";
		sql += ",A.\"FlowStep\"";
		sql += ",A.\"RejectReason\"";
		sql += " from \"TxFlow\" A";
		sql += " left join \"TxTeller\" F on F.\"TlrNo\"=:tlrNo ";
		sql += " left join \"CdBranch\" C on C.\"BranchNo\"=A.\"BrNo\"";
		sql += " left join \"TxRecord\" E on decode(A.\"FlowStep\", 1, E.\"Entdy\", E.\"OrgEntdy\")=A.\"Entdy\" ";
		sql += "                         and E.\"TxNo\" = decode(A.\"FlowStep\", 1, A.\"TxNo1\", A.\"TxNo3\") ";
		sql += "                         and E.\"FlowNo\"= A.\"FlowNo\"";
		sql += " left join \"TxTranCode\" B on B.\"TranNo\"=E.\"TranNo\"";
		sql += " left join \"CdEmp\" D on D.\"EmployeeNo\"=E.\"TlrNo\"";
		sql += " where A.\"Entdy\">=:entdys";
		sql += "   and A.\"Entdy\"<=:entdye";
		sql += "   and A.\"BrNo\"=:brno";
		sql += "   and A.\"FlowMode\"= 1 "; // 流程模式 1.待放行 2.待審核 3.待提交 9.已結案
		sql += "   and A.\"FlowStep\" in (1, 3) "; // 流程步驟 1.登錄 2.放行 3.審核 4.審核放行
		sql += "   and A.\"FlowGroupNo\"=F.\"GroupNo\" "; // 流程科組別
		sql += "   and NVL(E.\"Entdy\", 0) > 0 "; // join到TxRecord
		if (!"".equals(iTranNo)) {
			sql += "   and A.\"TranNo\" like :itranNo";
		}

		sql += " order by A.\"CreateDate\" ASC ";

		sql += " OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

		this.info("LC003ServiceImpl sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("tlrNo", iTlrNo);
		query.setParameter("entdys", iEntdayS);
		query.setParameter("entdye", iEntdayE);
		query.setParameter("brno", iBrNo);
		if (!"".equals(iTranNo)) {
			query.setParameter("itranNo", iTranNo + "%");
		}
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		return this.convertToMap(query);
	}

}