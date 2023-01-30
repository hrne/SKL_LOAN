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

@Service("l9135ServiceImpl2")
@Repository
public class L9135ServiceImpl2 extends ASpringJpaParm implements InitializingBean {
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
		this.info("L9135 ServiceImpl2");

		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;

		String sql = " "; 
		sql += "	SELECT A.\"AcNoCode\" AS \"AcNoCode\"";
		sql += "		  ,E2.\"AcNoItem\" AS \"AcNoItem\"";
		sql += "          ,A.\"DbCr\" AS \"DbCr\"";
//		sql += "		  ,'銀行存款－活期存款' AS \"AcNoItem\"";
		sql += "		  ,A.\"AcSubCode\" AS \"AcSubCode\"";
		sql += "		  ,CASE";
//		sql += "			 WHEN A.\"AcSubCode\" = '01004' THEN '郵局  18471256'";
//		sql += "			 WHEN A.\"AcSubCode\" = '04004' THEN '新光  0116-10-000108-8'";
//		sql += "			 WHEN A.\"AcSubCode\" = '04007' THEN '新光  0116-10-100100-6'";
//		sql += "			 WHEN A.\"AcSubCode\" = '12005' THEN '台新  00201000001800'";
		sql += "			 WHEN A.\"AcSubCode\" = '01004' THEN '18471256  '";
		sql += "			 WHEN A.\"AcSubCode\" = '04004' THEN '000108-8  '";
		sql += "			 WHEN A.\"AcSubCode\" = '04007' THEN '100100-6  '";
		sql += "			 WHEN A.\"AcSubCode\" = '12005' THEN '1000001800'";
		sql += "		   END AS \"AcctItem\"";
		sql += "		  ,LPAD(A.\"SlipNo\",5,0) AS \"SlipNo\"";
		sql += "		  ,DECODE(A.\"DbCr\",'D',A.\"TxAmt\",0) AS \"DbTxAmt\"";
		sql += "		  ,DECODE(A.\"DbCr\",'C',A.\"TxAmt\",0) AS \"CrTxAmt\"";
		sql += "	FROM \"AcDetail\" A ";
		sql += "	LEFT JOIN \"CdAcCode\" E ON E.\"AcNoCode\"=A.\"AcNoCode\"";
		sql += "							AND E.\"AcSubCode\"=A.\"AcSubCode\"";
		sql += "							AND E.\"AcDtlCode\"=A.\"AcDtlCode\"";
		sql += "	LEFT JOIN \"CdAcCode\" E2 ON E2.\"AcNoCode\"=A.\"AcNoCode\"";
		sql += "							 AND E.\"AcSubCode\" = '     ' ";
		sql += "	WHERE A.\"AcDate\" = :AcDate ";
		sql += "  	  AND A.\"AcNoCode\" = '10121100000'";// --銀行存款科目
		sql += "  	  AND A.\"AcSubCode\" NOT IN ('     ')";// --欄位五位
		sql += "  	  AND A.\"EntAc\" > 0 ";
		sql += "	ORDER BY A.\"AcNoCode\" ASC";
		sql += "			,A.\"AcSubCode\" DESC";
		sql += "			,A.\"AcDtlCode\" ASC";
		sql += "			,A.\"SlipNo\" ASC";


		this.info("L9135ServiceImpl sql=" + sql);

	
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("AcDate", iAcDate);
		return this.convertToMap(query);
	}



}