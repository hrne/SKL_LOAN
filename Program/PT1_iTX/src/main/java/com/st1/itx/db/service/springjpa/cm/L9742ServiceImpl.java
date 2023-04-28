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
public class L9742ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int functionCode, int option) throws Exception {
		this.info("L9742.findAll ");

		int acdate = parse.stringToInteger(titaVo.getParam("inputAcDate")) + 19110000;

		int inputSlipNoStart = parse.stringToInteger(titaVo.getParam("inputSlipNoStart"));
		// 當inputSlipNoStart交易序號為00000000時表示找全部，inputSlipNoEnd必為99999999
		int inputSlipNoEnd = inputSlipNoStart == 0 ? 99999999
				: parse.stringToInteger(titaVo.getParam("inputSlipNoEnd"));
		int inputTitaTxtNoStart = parse.stringToInteger(titaVo.getParam("inputTitaTxtNoStart"));
		int inputTitaTxtNoEnd = parse.stringToInteger(titaVo.getParam("inputTitaTxtNoEnd"));

		// 整批
		if (functionCode == 2) {
			inputSlipNoStart = 0;
			inputSlipNoEnd = 999999;
			inputTitaTxtNoStart = 0;
			inputTitaTxtNoEnd = 99999999;
		}

		this.info("acdate=" + acdate);
		this.info("functionCode=" + functionCode);
		this.info("option=" + option);
		this.info("inputSlipNoStart=" + inputSlipNoStart);
		this.info("inputSlipNoEnd=" + inputSlipNoEnd);
		this.info("inputTitaTxtNoStart=" + inputTitaTxtNoStart);
		this.info("inputTitaTxtNoEnd=" + inputTitaTxtNoEnd);

		String sql = "";
		sql += "	WITH \"MainData\" AS (";
		sql += " 		SELECT DISTINCT ACD.\"SlipNo\" "; // 傳票號碼
		sql += "      		 , ACD.\"TitaTxtNo\" "; // 交易序號
		sql += "      		 , ACD.\"AcNoCode\" "; // 會計科目代碼
		sql += "      		 , CDAC.\"AcNoItem\" "; // 會計科目名稱
		sql += "      		 , ACD.\"SumNo\" "; // 彙總別
		sql += "      		 , TX.\"EntryDate\" "; // 入帳日期
		sql += "      		 , LPAD(TX.\"CustNo\", 7, '0') AS \"CustNo\" "; // 戶號
		sql += "      		 , LPAD(TX.\"FacmNo\", 3, '0') AS \"FacmNo\" "; // 額度
		sql += "      		 , LPAD(TX.\"BormNo\", 3, '0') AS \"BormNo\" "; // 撥款編號
		sql += "      		 , \"Fn_ParseEOL\"(CM.\"CustName\", 0) AS \"CustName\" "; // 戶名
		sql += "      		 , CASE WHEN :inputOption = 1 "; // 還本收據
		sql += "             	    THEN TX.\"Principal\" ";
		sql += "             		WHEN :inputOption = 2 AND ACD.\"AcNoCode\" LIKE '40241%'";// 繳息收據
		sql += "             		THEN TX.\"Interest\" ";
		sql += "             		WHEN :inputOption = 3 AND ACD.\"AcNoCode\" LIKE '409030%'"; // 手續費收據
		sql += "             		THEN TX.\"FeeAmt\" ";
		sql += "        		ELSE 0 END AS \"Amt\" ";
		sql += "      		 , TX.\"IntStartDate\" "; // 計算期間-起
		sql += "      		 , TX.\"IntEndDate\" "; // 計算期間-止
		sql += "      		 , CDE.\"Fullname\" ";
		sql += "      		 , TX.\"AcDate\" "; // 會計日期
		sql += "      		 , ACD.\"AcSubCode\" "; // 子目
		sql += "      		 , ACD.\"SlipBatNo\" "; // 傳票批號
		sql += "      		 , TX.\"RepayCode\" "; // 還款來源
		sql += "		FROM \"LoanBorTx\" TX ";
		sql += " 		LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = TX.\"CustNo\" ";
		sql += " 		LEFT JOIN \"AcDetail\" ACD ON ACD.\"AcDate\" = TX.\"AcDate\" ";
		sql += "                           		  AND ACD.\"TitaTxtNo\" = TO_NUMBER(TX.\"TitaTxtNo\") ";
//		sql += "                           		  AND ACD.\"TitaTxCd\" = TX.\"TitaTxCd\" ";
		sql += "                           		  AND ACD.\"TitaTlrNo\" = TX.\"TitaTlrNo\" ";
		sql += " 		LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = ACD.\"AcNoCode\" ";
		sql += "                            	   AND CDAC.\"AcSubCode\" = ACD.\"AcSubCode\" ";
		sql += "                            	   AND CDAC.\"AcDtlCode\" = ACD.\"AcDtlCode\" ";
		sql += " 		LEFT JOIN \"CdEmp\" CDE ON CDE.\"EmployeeNo\" = ACD.\"TitaTlrNo\" ";
		sql += " 		WHERE TX.\"TitaHCode\" = 0 "; // 訂正別為0:正常
		sql += "   		  AND CM.\"EntCode\" IN (1, 2) "; // 企金別為1:企金、2:企金自然人
		sql += "   		  AND TX.\"AcDate\" = :inputAcDate "; // 會計日期

		// functionCode 1=手動輸入、2=整批
		if (functionCode == 1) {
			// 當交易序號為0表示找範圍 0~99999999
			if (inputSlipNoStart == 0) {
				sql += "   		  AND ACD.\"TitaTxtNo\" >= :inputTitaTxtNoStart "; // 交易序號-起
				sql += "   		  AND ACD.\"TitaTxtNo\" <= 99999999 "; // 交易序號-止
			} else {
				//交易序號大於0 ，只找畫面輸入的值
				sql += "   		  AND ACD.\"TitaTxtNo\" = :inputTitaTxtNoStart "; // 交易序號
			}
		} else {
			sql += "   		  AND ACD.\"TitaTxtNo\" >= :inputTitaTxtNoStart "; // 交易序號-起
			sql += "   		  AND ACD.\"TitaTxtNo\" <= :inputTitaTxtNoEnd "; // 交易序號-止
			sql += "      	  AND \"RepayCode\" = 2 "; // 付款方式 2=銀行扣款
		}
		sql += "   		  AND ACD.\"SlipNo\" >= :inputSlipNoStart "; // 傳票號碼-起
		sql += "   		  AND ACD.\"SlipNo\" <= :inputSlipNoEnd "; // 傳票號碼-止
		sql += "      	  AND  CASE WHEN :inputOption = 1 "; // 還本收據
		sql += "             	    THEN TX.\"Principal\" ";
		sql += "             		WHEN :inputOption = 2 AND ACD.\"AcNoCode\" LIKE '40241%'";// 繳息收據
		sql += "             		THEN TX.\"Interest\" ";
		sql += "             		WHEN :inputOption = 3 AND ACD.\"AcNoCode\" LIKE '409030%'"; // 手續費收據
		sql += "             		THEN TX.\"FeeAmt\" ";
		sql += "        		ELSE 0 END > 0 ";
		sql += "	)";
		// --檢查筆數
		// --QC2306 說 有傳票號碼一筆以上 計算到額度從，否則計算到撥款層
		sql += "	, \"CheckCount\" AS (";
		sql += "		SELECT \"SlipNo\"";
		sql += "			  ,\"TitaTxtNo\"";
		sql += "			  ,COUNT(1) AS \"Count\"";
		sql += "		FROM \"MainData\"";
		sql += "		GROUP BY \"SlipNo\"";
		sql += "				,\"TitaTxtNo\"";
		sql += "	)";
		sql += "	SELECT M.\"SlipNo\"";
		sql += "		  ,M.\"TitaTxtNo\"";
		sql += "		  ,M.\"AcNoCode\"";
		sql += "		  ,M.\"AcNoItem\"";
		sql += "		  ,M.\"SumNo\"";
		sql += "		  ,M.\"EntryDate\"";
		sql += "		  ,M.\"CustNo\"";
		if (option == 3) {
			sql += "		    ,'0' AS \"FacmNo\" ";
			sql += "		    ,'0' AS \"BormNo\" ";
		} else {
			sql += "		    ,M.\"FacmNo\" ";
			sql += "		    ,CASE WHEN CC.\"Count\" > 1 THEN '0' ";
			sql += "		  		  ELSE M.\"BormNo\" END AS \"BormNo\"";
		}
		sql += "		  ,M.\"CustName\"";
		sql += "		  ,SUM(M.\"Amt\") AS \"Amt\"";
		sql += "		  ,MIN(M.\"IntStartDate\") AS \"IntStartDate\"";
		sql += "		  ,MAX(M.\"IntEndDate\") AS \"IntEndDate\"";
		sql += "		  ,M.\"Fullname\"";
		sql += "		  ,M.\"AcDate\"";
		sql += "		  ,M.\"AcSubCode\"";
		sql += "		  ,M.\"SlipBatNo\"";
		sql += "		  ,M.\"RepayCode\"";
		sql += "	FROM \"MainData\" M";
		sql += "	LEFT JOIN \"CheckCount\" CC ON CC.\"SlipNo\" = M.\"SlipNo\"";
		sql += "							   AND CC.\"TitaTxtNo\" = M.\"TitaTxtNo\"";
		sql += "	GROUP BY M.\"SlipNo\"";
		sql += "		    ,M.\"TitaTxtNo\"";
		sql += "		    ,M.\"AcNoCode\"";
		sql += "		    ,M.\"AcNoItem\"";
		sql += "		    ,M.\"SumNo\"";
		sql += "		    ,M.\"EntryDate\"";
		sql += "		    ,M.\"CustNo\"";
		if (option != 3) {
			sql += "		    ,M.\"FacmNo\" ";
			sql += "		    ,CASE WHEN CC.\"Count\" > 1 THEN '0' ";
			sql += "		  		  ELSE M.\"BormNo\" END ";
		}
		sql += "		    ,M.\"CustName\"";
		sql += "		    ,M.\"Fullname\"";
		sql += "		    ,M.\"AcDate\"";
		sql += "		    ,M.\"AcSubCode\"";
		sql += "		    ,M.\"SlipBatNo\"";
		sql += "		    ,M.\"RepayCode\"";


		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);

		query.setParameter("inputAcDate", acdate);

		// functionCode 1=手動輸入、2=整批
		if (functionCode == 1) {
			query.setParameter("inputTitaTxtNoStart", inputTitaTxtNoStart);
		} else {
			query.setParameter("inputTitaTxtNoStart", inputTitaTxtNoStart);
			query.setParameter("inputTitaTxtNoEnd", inputTitaTxtNoEnd);
		}

		query.setParameter("inputSlipNoStart", inputSlipNoStart);
		query.setParameter("inputSlipNoEnd", inputSlipNoEnd);
		query.setParameter("inputOption", option);

		return this.convertToMap(query);
	}

}