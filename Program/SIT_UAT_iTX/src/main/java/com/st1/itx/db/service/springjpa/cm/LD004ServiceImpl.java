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

@Service
@Repository
/* 逾期放款明細 */
public class LD004ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lD004.findAll ");

		String sql = "";
 		sql += " SELECT  A.\"SlipNo\"  ";
 		sql += "        ,A.\"TitaTxtNo\"  ";
 		sql += "        ,A.\"AcNoCode\"  ";
 		sql += "        ,CA.\"AcctItem\"  ";
 		sql += "        ,A.\"SumNo\"  ";
 		sql += "        ,T.\"EntryDate\"  ";
 		sql += "        ,A.\"CustNo\"  ";
 		sql += "        ,A.\"FacmNo\"  ";
 		sql += "        ,A.\"BormNo\"  ";
 		sql += "        ,C.\"CustName\"  ";
 		sql += "        ,AGroup.\"TxAmt\"  ";
 		sql += "        ,T.\"IntStartDate\"  ";
 		sql += "        ,T.\"IntEndDate\"  ";
 		sql += "        ,A.\"Fullname\"  ";
 		sql += "        ,A.\"AcDate\"  ";
 		sql += "        ,A.\"AcSubCode\"  ";
 		sql += "        ,A.\"SlipBatNo\" ";
 		sql += " FROM (SELECT  A.\"SlipNo\"  ";
 		sql += "              ,A.\"TitaTxtNo\"  ";
 		sql += "              ,A.\"AcNoCode\"  ";
 		sql += "              ,A.\"AcctCode\"  ";
 		sql += "              ,A.\"SumNo\"    ";
 		sql += "              ,A.\"CustNo\"  ";
 		sql += "              ,A.\"FacmNo\"  ";
 		sql += "              ,A.\"BormNo\"  ";
 		sql += "              ,DECODE ( A.\"DbCr\",'D', - A.\"TxAmt\",A.\"TxAmt\") \"TxAmt\"  ";
 		sql += "              ,A.\"RelDy\", A.\"TitaTlrNo\"   ";
 		sql += "              ,E.\"Fullname\"  ";
 		sql += "              ,A.\"RelTxseq\"  ";
 		sql += "              ,A.\"AcDate\"  ";
 		sql += "              ,A.\"AcSubCode\"  ";
 		sql += "              ,A.\"DbCr\"  ";
 		sql += "              ,A.\"SlipBatNo\" ";
 		sql += "       FROM  \"AcDetail\" A  ";
 		sql += "       LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = A.\"TitaTlrNo\"  ";
 		sql += "       WHERE (    A.\"TitaTxCd\" LIKE 'L32%'  ";
 		sql += "               OR A.\"TitaTxCd\" LIKE 'L34%'  ";
 		sql += "               OR A.\"TitaTxCd\" LIKE 'L36%' ";
 		sql += "             )  ";
 		sql += "         AND (    A.\"AcctCode\" LIKE '3%'  ";
 		sql += "               OR A.\"AcctCode\" LIKE '9%'  ";
 		sql += "               OR A.\"AcctCode\" LIKE 'I%'  ";
 		sql += "               OR A.\"AcctCode\" LIKE 'F%'  ";
 		sql += "               OR A.\"AcctCode\" = 'TMI'  ";
 		sql += "             )    ";
 		sql += "         AND A.\"EntAc\" = 1  ";
 		sql += "      ) A  ";
 		sql += " LEFT JOIN \"LoanBorTx\" T ON T.\"AcDate\" = A.\"AcDate\"   ";
 		sql += "                          AND TO_NUMBER(T.\"TitaTxtNo\") = A.\"TitaTxtNo\"  ";
 		sql += "                          AND T.\"CustNo\" = A.\"CustNo\"  ";
 		sql += "                          AND T.\"FacmNo\" = A.\"FacmNo\"  ";
 		sql += "                          AND T.\"TxAmt\"  = A.\"TxAmt\" * DECODE(A.\"DbCr\",'C',1,-1)  ";
 		sql += " LEFT JOIN \"CdAcCode\" CA ON CA.\"AcctCode\" = A.\"AcctCode\"    ";
 		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = A.\"CustNo\"  ";
 		sql += " LEFT JOIN ( SELECT \"AcDate\" ";
 		sql += "                   ,\"CustNo\" ";
 		sql += "                   ,\"SlipNo\" ";
 		sql += "                   ,SUM(\"TxAmt\" * DECODE(\"DbCr\", 'D', -1, 1)) \"TxAmt\" ";
 		sql += "             FROM \"AcDetail\" ";
 		sql += "             WHERE :inputTradeSeq = SUBSTR(\"RelTxseq\",5)  ";
 		sql += "             GROUP BY \"AcDate\", \"CustNo\", \"SlipNo\" ";
 		sql += "           ) AGroup ON AGroup.\"AcDate\" = A.\"AcDate\" ";
 		sql += "                   AND AGroup.\"CustNo\" = A.\"CustNo\" ";
 		sql += "                   AND AGroup.\"SlipNo\" = A.\"SlipNo\" ";
 		sql += " WHERE C.\"EntCode\" = 1   ";
 		sql += "   AND :inputTradeSeq = SUBSTR(A.\"RelTxseq\",5)  ";

		/*
		 * SELECT A."SlipNo", A."TitaTxtNo", A."AcNoCode" , CA."AcctItem", A."SumNo",
		 * T."EntryDate" , A."CustNo", A."FacmNo", A."BormNo" , C."CustName", A."TxAmt",
		 * T."IntStartDate" , T."IntEndDate" FROM (SELECT A."SlipNo", A."TitaTxtNo",
		 * A."AcNoCode" , A."AcctCode", A."SumNo" , A."CustNo", A."FacmNo", A."BormNo" ,
		 * DECODE(A."DbCr", 'D', - A."TxAmt", A."TxAmt") "TxAmt" , A."RelDy",
		 * A."TitaTlrNo" FROM "AcDetail" A WHERE A."AcDate" = 本營業日 AND (A."TitaTxCd"
		 * LIKE 'L32%' OR A."TitaTxCd" LIKE 'L34%') AND (A."AcctCode" LIKE '3%' OR
		 * A."AcctCode" LIKE '9%') AND A."EntAc" = 1 ) A LEFT JOIN "LoanBorTx" T ON
		 * T."AcDate" = A."RelDy" AND T."TitaTlrNo" = A."TitaTlrNo" AND T."TitaTxtNo" =
		 * A."TitaTxtNo" LEFT JOIN "CdAcCode" CA ON CA."AcctCode" = A."AcctCode" LEFT
		 * JOIN "CustMain" C ON C."CustNo" = A."CustNo" WHERE C."EntCode" = 1
		 */

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("inputTradeSeq", titaVo.getParam("inputTradeSeq"));

		return this.convertToMap(query.getResultList());
	}

}