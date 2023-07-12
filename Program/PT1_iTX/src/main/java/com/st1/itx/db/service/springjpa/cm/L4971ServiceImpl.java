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

@Service("L4971ServiceImpl")
@Repository
public class L4971ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	private String inputReconCode;
	private int inputAcDate;
//	private int reportNo = 0;
//	private int functionCode = 0;

	public List<Map<String, String>> findAll( int inputAcDate,String iReconCode ,TitaVo titaVo) throws Exception {

// L4971 匯款轉帳短繳查詢
//
// 輸入 ref L4211
//  會計日期
//  存摺代號	help A!,A2,A3,查全部
//  輸出

		String sql = " ";
			sql += " WITH TX0 AS (";
			sql += "   SELECT \"OpenAcDate\", ";
			sql += "          \"OpenTlrNo\", ";
			sql += "          lpad(\"OpenTxtNo\", 8, '0') AS \"OpenTxtNo\", ";
			sql += "          \"OpenTxCd\", ";
			sql += "          \"CustNo\", ";
			sql += "          SUM(\"RvBal\") AS \"ShortAmt\"        ";
	  	    sql += "   FROM \"AcReceivable\"";
 	  	    sql += "   WHERE \"ClsFlag\" = 0                         ";
	   	    sql += "     AND \"ReceivableFlag\" = 4                  "; // 短繳
		    sql += "     AND \"OpenAcDate\" = :inputAcDate           ";
			sql += "   GROUP BY \"CustNo\"";
			sql += "          , \"OpenAcDate\"";
			sql += "          , \"OpenTlrNo\"";
			sql += "          , \"OpenTxtNo\"";
			sql += "          , \"OpenTxCd\" ";
			sql += " )";
			sql += ",TX1 AS (";
			sql += "   SELECT \"AcDate\"";
 	 	    sql += "        , \"CustNo\" ";
			sql += "   FROM \"BatxDetail\"";
			sql += "   WHERE \"AcDate\" = :inputAcDate";
			sql +=     " AND \"RepayCode\" = '01'"; // 匯款轉帳
			sql += "     AND \"ProcStsCode\" IN ( '5', '6', '7')   ";// 入帳成功
			sql += "     AND CASE";
			sql += "           WHEN NVL(TRIM( :inputReconCode ),' ') = ' ' ";// 輸入空白時查全部
			sql += "           THEN \"ReconCode\" ";
			sql += "           ELSE :inputReconCode";
			sql += "           END = \"ReconCode\" ";
			sql += "   GROUP BY \"AcDate\",";
			sql += "            \"CustNo\"";
			sql += " )";
			sql += "  SELECT  ";
			sql += "      TX0.\"CustNo\"                                 "; // 戶號
			sql += "    , CM.\"CustName\"                                "; // 戶名
			sql += "    , SUM(TX2.\"TxAmt\" )       AS \"TxAmt\"         "; // 匯款金額
			sql += "    , MIN(TX2.\"IntStartDate\") AS \"IntStartDate\"  "; // 計息起日
			sql += "    , MAX(TX2.\"IntEndDate\")   AS \"IntEndDate\"    "; // 計息迄日
			sql += "    , SUM(TX2.\"Principal\")    AS \"Principal\"     "; // 本金 
			sql += "    , SUM(TX2.\"Interest\")     AS \"Interest\"      "; // 利息  
			sql += "    , SUM(TX2.\"DelayInt\"";
			sql += "      + TX2.\"BreachAmt\"";
			sql += "      + TX2.\"CloseBreachAmt\") AS \"BreachAmt\"     "; // 違約金 
			sql += "    , SUM(TX2.\"FeeAmt\")       AS \"Fee\"           "; // 費用
			sql += "    , SUM(TX2.\"TempAmt\")      AS \"TempDr\"        "; // 暫收抵繳
			sql += "    , TX0.\"ShortAmt\"                               "; // 短繳金額
			sql += "    , TX0.\"OpenAcDate\"                                 "; // 連結L3005使用 ref L6909
			sql += "    , TX0.\"OpenTlrNo\"                               "; // 連結L3005使用
			sql += "    , TX0.\"OpenTxtNo\"                            "; // 連結L3005使用
            sql += " FROM TX0                                          ";
			sql += " LEFT JOIN TX1 ON TX1.\"AcDate\" = TX0.\"OpenAcDate\"  ";
			sql += "              AND TX1.\"CustNo\" = TX0.\"CustNo\"  ";
			sql += " LEFT JOIN \"LoanBorTx\" TX2 ";
			sql += "             ON TX2.\"AcDate\"    = TX0.\"OpenAcDate\"    ";
			sql += "            AND TX2.\"TitaTlrNo\" = TX0.\"OpenTlrNo\" ";
			sql += "            AND TX2.\"TitaTxtNo\" = TX0.\"OpenTxtNo\" ";
    	    sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = TX0.\"CustNo\"";
		    sql += " WHERE NVL(TX1.\"CustNo\",0) > 0                         ";
			sql += "   AND NVL(TX2.\"RepayCode\",0) IN (1, 90)            "; // 還款方式:1.匯款轉帳 90.暫收抵繳
			sql += " group by TX0.\"CustNo\", CM.\"CustName\", TX0.\"ShortAmt\", TX0.\"OpenAcDate\", TX0.\"OpenTlrNo\", TX0.\"OpenTxtNo\" ";
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputAcDate", inputAcDate);
		query.setParameter("inputReconCode", iReconCode);
		return this.convertToMap(query);
	}
}
