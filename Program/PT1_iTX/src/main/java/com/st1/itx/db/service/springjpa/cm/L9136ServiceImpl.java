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
import com.st1.itx.util.parse.Parse;;

@Service("l9136ServiceImpl")
@Repository
public class L9136ServiceImpl extends ASpringJpaParm implements InitializingBean {
	@Autowired
	Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}



	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("L9136 ServiceImpl findAll");

		int acDateStart = 0;
		int acDateEnd = 0;
		if ("L9136".equals(titaVo.getTxcd().toString())) {
			acDateStart =  this.parse.stringToInteger(titaVo.getParam("sAcDate")) + 19110000;
			acDateEnd = this.parse.stringToInteger(titaVo.getParam("eAcDate")) + 19110000;
		} else {
			acDateStart = this.parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
			acDateEnd = this.parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		}


		String sql = " "; 
		sql += "	SELECT T.\"TxDate\" AS \"AcDate\"";
		sql += "		  ,T.\"TxSeq\" AS \"TxSeq\"";
		sql += "		  ,T.\"CustNo\" AS \"CustNo\"";
		sql += "		  ,T.\"FacmNo\" AS \"FacmNo\"";
		sql += "		  ,T.\"BormNo\" AS \"BormNo\"";
		sql += "		  ,SUBSTR(cm.\"CustName\",0,5) AS \"CustName\"";
		sql += "		  ,DECODE(T.\"FacmNo\",0,' ',Cl.\"ApproveNo\") AS \"ApproveNo\"";
		sql += "		  ,DECODE(T.\"FacmNo\",0,' ',Cl.\"ClCode1\") AS \"ClCode1\"";
		sql += "		  ,DECODE(T.\"FacmNo\",0,' ',Cl.\"ClCode2\") AS \"ClCode2\"";
		sql += "		  ,DECODE(T.\"FacmNo\",0,' ',CC.\"Item\") AS \"ClName\"";
		sql += "		  ,DECODE(T.\"FacmNo\",0,' ',Cl.\"ClNo\") AS \"ClNo\"";
		sql += "		  ,JSON_QUERY(T.\"Content\",'$[*].f' WITH WRAPPER) AS \"Item\"";
		sql += "		  ,JSON_QUERY(T.\"Content\",'$[*].o' WITH WRAPPER) AS \"Old\"";
		sql += "		  ,JSON_QUERY(T.\"Content\",'$[*].n' WITH WRAPPER) AS \"New\"";
		sql += "		  ,CE.\"Fullname\" AS \"Name\"";
		sql += "		  ,CE2.\"Fullname\" AS \"SupNoName\"";
		sql += "		  ,T.\"TranNo\" AS \"TranNo\"";
		sql += "		  ,TC.\"TranItem\" AS \"TranItem\"";
		sql += "	FROM \"TxDataLog\" T";
		sql += "	LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = T.\"CustNo\"";
		sql += "	LEFT JOIN \"ClFac\" Cl ON Cl.\"CustNo\" = T.\"CustNo\"";
		sql += "						  AND Cl.\"FacmNo\" = T.\"FacmNo\"";
		sql += "						  AND Cl.\"MainFlag\" = 'Y'";
		sql += "	LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'ClCode2' || Cl.\"ClCode1\"";
		sql += "						   AND CC.\"Code\" = LPAD(Cl.\"ClCode2\",2,0)";
		sql += "	LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = T.\"TlrNo\"";
		sql += "	LEFT JOIN \"TxRecord\" TR ON TR.\"TxNo\" = T.\"TxSeq\"";
		sql += "	LEFT JOIN \"CdEmp\" CE2 ON CE2.\"EmployeeNo\" = TR.\"SupNo\"";
		sql += "	LEFT JOIN \"TxTranCode\" TC ON TC.\"TranNo\" = T.\"TranNo\"";
		sql += "	WHERE T.\"TxDate\" BETWEEN :sAcDate AND :eAcDate";
		sql += "	  AND JSON_QUERY(T.\"Content\",'$[*].f' WITH WRAPPER) IS NOT NULL";
		sql += "	  AND T.\"TlrNo\" <> 'E-LOAN'";
		sql += "	  AND T.\"CustNo\" > 0";
		sql += "	ORDER BY T.\"TxDate\" ASC";
		sql += "			,T.\"TlrNo\" ASC";
		sql += "			,T.\"TxSeq\" ASC";


		this.info("L9136ServiceImpl sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("sAcDate", acDateStart);
		query.setParameter("eAcDate", acDateEnd);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll2(TitaVo titaVo) throws Exception {
		this.info("L9136 ServiceImpl findAll2");

		int acDateStart = 0;
		int acDateEnd = 0;
		if ("L9136".equals(titaVo.getTxcd().toString())) {
			acDateStart =  this.parse.stringToInteger(titaVo.getParam("sAcDate")) + 19110000;
			acDateEnd = this.parse.stringToInteger(titaVo.getParam("eAcDate")) + 19110000;
		} else {
			acDateStart = this.parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
			acDateEnd = this.parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		}
		String sql = " "; 
		sql += "	WITH \"tmpTxRecord\" AS(";
		sql += "		SELECT T.\"Entdy\" AS \"TxDate\"";
		sql += "			  ,T.\"TxSeq\"";
		sql += "			  ,TO_NUMBER( ";
		sql += "		      	  CASE";
		sql += "		      		WHEN LENGTH(TRIM(T.\"MrKey\")) IN (7,11,15)";
		sql += "		      		THEN SUBSTR(T.\"MrKey\",0,7)";
		sql += "		      		ELSE '0' END";
		sql += "		      ) AS \"CustNo\"";
		sql += "			  ,TO_NUMBER( ";
		sql += "		      	  CASE";
		sql += "		      		WHEN LENGTH(TRIM(T.\"MrKey\")) IN (11,15)";
		sql += "		      		THEN SUBSTR(T.\"MrKey\",9,3)";
		sql += "		      		ELSE '0' END";
		sql += "		      ) AS \"FacmNo\"";
		sql += "			  ,TO_NUMBER( ";
		sql += "		      	  CASE";
		sql += "		      		WHEN LENGTH(TRIM(T.\"MrKey\")) IN (15)";
		sql += "		      		THEN SUBSTR(T.\"MrKey\",13,3)";
		sql += "		      		ELSE '0' END";
		sql += "		      ) AS \"BormNo\"";
		sql += "		      ,T.\"TranNo\" AS \"Item\"";
		sql += "			  ,' ' AS \"Old\"";
		sql += "		      ,CASE";
		sql += "		      	 WHEN T.\"Hcode\" = 1 ";
		sql += "		      	  AND JSON_VALUE(T.\"TranData\",'$.SUPNO') IS NOT NULL";
		sql += "		      	 THEN '訂正'";
		sql += "		      	 WHEN T.\"Hcode\" <> 1 ";
		sql += "		      	  AND JSON_VALUE(T.\"TranData\",'$.SUPNO') IS NOT NULL";
		sql += "		      	 THEN NVL(JSON_VALUE(T.\"TranData\",'$.RQSP'),'授權')";
		sql += "		      	 WHEN JSON_VALUE(T.\"TranData\",'$.ACTFG') IN (2,4)";
		sql += "		      	 THEN ";
		sql += "				   CASE";
		sql += "				     WHEN T.\"TranNo\" = 'L3100' THEN '撥款金額：' ||   TO_CHAR(NVL(JSON_VALUE(T.\"TranData\",'$.TXAMT'),'FM999,999,999,999,999'))";
		sql += "				   	 WHEN T.\"TranNo\" = 'L5103' THEN '借閱原因：' ||   TO_CHAR(CC.\"Item\")";
		sql += "				   	 ELSE '放行' END";
		sql += "		      	 END AS \"New\"";
//		sql += "		      ,CASE";
//		sql += "		      	 WHEN T.\"Hcode\" = 1 ";
//		sql += "		      	  AND JSON_VALUE(\"TranData\",'$.SUPNO') IS NOT NULL";
//		sql += "		      	 THEN '訂正'";
//		sql += "		      	 WHEN T.\"Hcode\" <> 1 ";
//		sql += "		      	  AND JSON_VALUE(\"TranData\",'$.SUPNO') IS NOT NULL";
//		sql += "		      	 THEN NVL(JSON_VALUE(\"TranData\",'$.RQSP'),'授權')";
//		sql += "		      	 WHEN JSON_VALUE(\"TranData\",'$.ACTFG') IN (2,4)";
//		sql += "		      	 THEN '放行' ";
//		sql += "		      	 END AS \"New\"";
		sql += "			  ,T.\"TlrNo\"";
		sql += "			  ,JSON_VALUE(T.\"TranData\",'$.SUPNO') AS \"SupNo\"";
		sql += "			  ,T.\"TranNo\" AS \"TranNo\"";
		sql += "		      ,CASE";
		sql += "		      	 WHEN T.\"Hcode\" = 1 ";
		sql += "		      	  AND JSON_VALUE(T.\"TranData\",'$.SUPNO') IS NOT NULL";
		sql += "		      	 THEN 1 ";
		sql += "		      	 WHEN T.\"Hcode\" <> 1 ";
		sql += "		      	  AND JSON_VALUE(T.\"TranData\",'$.SUPNO') IS NOT NULL";
		sql += "		      	 THEN 1 ";
		sql += "		      	 WHEN JSON_VALUE(T.\"TranData\",'$.ACTFG') IN (2,4)";
		sql += "		      	 THEN 2";
		sql += "		      	 ELSE 1 END AS \"Seq\"";
		sql += "		FROM \"TxRecord\" T";
		sql += "	    LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'UsageCodeX'";
		sql += "						       AND CC.\"Code\" = JSON_VALUE(T.\"TranData\",'$.UsageCode')";
		sql += "		WHERE T.\"Entdy\" BETWEEN :sAcDate AND :eAcDate";
		sql += "		  AND ((T.\"Hcode\" = 1 AND JSON_VALUE(T.\"TranData\",'$.SUPNO') IS NOT NULL )";
		sql += "		   OR (T.\"Hcode\" <> 1 AND JSON_VALUE(T.\"TranData\",'$.SUPNO') IS NOT NULL )";
		sql += "		   OR ( JSON_VALUE(T.\"TranData\",'$.ACTFG') IN (2,4)))";
		sql += "	)";
		sql += "	SELECT T.\"TxDate\" AS \"AcDate\"";
		sql += "		  ,T.\"TxSeq\" AS \"TxSeq\"";
		sql += "		  ,T.\"CustNo\" AS \"CustNo\"";
		sql += "		  ,T.\"FacmNo\" AS \"FacmNo\"";
		sql += "		  ,T.\"BormNo\" AS \"BormNo\"";
		sql += "		  ,SUBSTR(cm.\"CustName\",0,5) AS \"CustName\"";
		sql += "		  ,DECODE(T.\"FacmNo\",0,' ','',' ',Cl.\"ApproveNo\") AS \"ApproveNo\"";
		sql += "		  ,DECODE(T.\"FacmNo\",0,' ','',' ',Cl.\"ClCode1\") AS \"ClCode1\"";
		sql += "		  ,DECODE(T.\"FacmNo\",0,' ','',' ',Cl.\"ClCode2\") AS \"ClCode2\"";
		sql += "		  ,DECODE(T.\"FacmNo\",0,' ','',' ',CC.\"Item\") AS \"ClName\"";
		sql += "		  ,DECODE(T.\"FacmNo\",0,' ','',' ',Cl.\"ClNo\") AS \"ClNo\"";
		sql += "		  ,T.\"Item\" AS \"Item\"";
		sql += "		  ,T.\"Old\" AS \"Old\"";
		sql += "		  ,T.\"New\" AS \"New\"";
		sql += "		  ,CE.\"Fullname\" AS \"Name\"";
		sql += "		  ,CE2.\"Fullname\" AS \"SupNoName\"";
		sql += "		  ,T.\"TranNo\" AS \"TranNo\"";
		sql += "		  ,TC.\"TranItem\" AS \"TranItem\"";
		sql += "		  ,T.\"Seq\" AS \"Seq\"";
		sql += "	FROM \"tmpTxRecord\" T";
		sql += "	LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = T.\"CustNo\"";
		sql += "						  	 AND CM.\"CustNo\" > 0 ";
		sql += "	LEFT JOIN \"ClFac\" Cl ON Cl.\"CustNo\" = T.\"CustNo\"";
		sql += "						  AND Cl.\"FacmNo\" = T.\"FacmNo\"";
		sql += "						  AND Cl.\"MainFlag\" = 'Y'";
		sql += "	LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'ClCode2' || Cl.\"ClCode1\"";
		sql += "						   AND CC.\"Code\" = LPAD(Cl.\"ClCode2\",2,0)";
		sql += "	LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = T.\"TlrNo\"";
		sql += "	LEFT JOIN \"CdEmp\" CE2 ON CE2.\"EmployeeNo\" = T.\"SupNo\"";
		sql += "	LEFT JOIN \"TxTranCode\" TC ON TC.\"TranNo\" = T.\"TranNo\"";
		sql += "	ORDER BY T.\"Seq\" ASC";
		sql += "			,T.\"New\" ASC";
		sql += "			,T.\"TxDate\" ASC";
		sql += "			,T.\"TlrNo\" ASC";
		sql += "			,T.\"TxSeq\" ASC";

		this.info("L9136ServiceImpl sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("sAcDate", acDateStart);
		query.setParameter("eAcDate", acDateEnd);
		return this.convertToMap(query);
	}
	
	
	public List<Map<String, String>> findSupNo(TitaVo titaVo,String TxNo) throws Exception {
	
		String sql = " "; 
		sql += " WITH \"count\" AS (";
		sql += " 	SELECT \"TxSeq\" AS \"TxSeq\" ";
		sql += "          ,COUNT(\"TxSeq\") AS \"Count\"";
		sql += " 	FROM \"TxRecord\"";
		sql += " 	WHERE \"TxNo\" = :TxNo ";
		sql += " 	GROUP BY \"TxSeq\"";
		sql += " ),\"con1\" AS (";
		sql += " 	SELECT \"SupNo\" AS \"EmpNo\"";
		sql += " 	FROM \"TxRecord\"";
		sql += " 	WHERE \"TxNo\" = :TxNo ";
		sql += " ),\"con2\" AS (";
		sql += " 	SELECT \"TlrNo\" AS \"EmpNo\"";
		sql += " 	FROM \"TxRecord\"";
		sql += " 	WHERE \"TxNo\" = :TxNo ";
		sql += " 	  AND \"FlowType\" > 1";
		sql += " 	  AND \"FlowType\" = \"FlowStep\"";
		sql += " ),\"con3\" AS (";
		sql += " 	SELECT (SELECT \"TxSeq\" FROM \"count\") AS \"TxSeq\"";
		sql += " 		  ,CASE";
		sql += " 			 WHEN (SELECT \"Count\" FROM \"count\") = 1 ";
		sql += " 			 THEN (SELECT \"EmpNo\" FROM \"con1\")";
		sql += " 			 ELSE (SELECT \"EmpNo\" FROM \"con2\")";
		sql += " 			 END AS \"EmpNo\"";
		sql += " 	FROM DUAL";
		sql += " )";
		sql += " SELECT NVL(CE.\"Fullname\",'      ') AS \"SupNoName\"";
		sql += " 	   ,C.\"TxSeq\" AS \"TxSeq\"";
		sql += " FROM \"con3\" C";
		sql += " LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = C.\"EmpNo\"";


		this.info("L9136ServiceImpl sql=" + sql);

		
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("TxNo", TxNo);
		return this.convertToMap(query);
	}


}