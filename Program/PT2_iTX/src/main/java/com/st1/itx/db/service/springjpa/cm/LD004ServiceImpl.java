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
public class LD004ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lD004.findAll ");

		String sql = "";
		sql += " SELECT ACD.\"SlipNo\" "; // 傳票號碼
		sql += "      , ACD.\"TitaTxtNo\" "; // 交易序號
		sql += "      , ACD.\"AcNoCode\" "; // 會計科目代碼
		sql += "      , CDAC.\"AcNoItem\" "; // 會計科目名稱
		sql += "      , ACD.\"SumNo\" "; // 彙總別
		sql += "      , TX.\"EntryDate\" "; // 入帳日期
		sql += "      , LPAD(TX.\"CustNo\", 7, '0') AS \"CustNo\" "; // 戶號
		sql += "      , LPAD(TX.\"FacmNo\", 3, '0') AS \"FacmNo\" "; // 額度
		sql += "      , LPAD(TX.\"BormNo\", 3, '0') AS \"BormNo\" "; // 撥款編號
		sql += "      , \"Fn_ParseEOL\"(CM.\"CustName\", 0) AS \"CustName\" "; // 戶名
		sql += "      , CASE WHEN :inputOption = 1 "; // 還本收據
		sql += "             THEN TX.\"Principal\" ";
		sql += "             WHEN :inputOption = 2 "; // 繳息收據
		sql += "             THEN TX.\"Interest\" ";
		sql += "        ELSE 0 END AS \"Amt\" ";
		sql += "      , TX.\"IntStartDate\" "; // 計算期間-起
		sql += "      , TX.\"IntEndDate\" "; // 計算期間-止
		sql += "      , CDE.\"Fullname\" ";
		sql += "      , TX.\"AcDate\" "; // 會計日期
		sql += "      , ACD.\"AcSubCode\" "; // 子目
		sql += "      , ACD.\"SlipBatNo\" "; // 傳票批號
		sql += "      , TX.\"RepayCode\" "; // 還款來源
		sql += " FROM \"LoanBorTx\" TX ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = TX.\"CustNo\" ";
		sql += " LEFT JOIN \"AcDetail\" ACD ON ACD.\"AcDate\" = TX.\"AcDate\" ";
		sql += "                           AND ACD.\"TitaTxtNo\" = TO_NUMBER(TX.\"TitaTxtNo\") ";
		sql += "                           AND ACD.\"TitaTxCd\" = TX.\"TitaTxCd\" ";
		sql += " LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = ACD.\"AcNoCode\" ";
		sql += "                            AND CDAC.\"AcSubCode\" = ACD.\"AcSubCode\" ";
		sql += "                            AND CDAC.\"AcDtlCode\" = ACD.\"AcDtlCode\" ";
		sql += " LEFT JOIN \"CdEmp\" CDE ON CDE.\"EmployeeNo\" = ACD.\"TitaTlrNo\" ";
		sql += " WHERE TX.\"TitaHCode\" = 0 "; // 訂正別為0:正常
		sql += "   AND CM.\"EntCode\" IN (1, 2) "; // 企金別為1:企金、2:企金自然人
		sql += "   AND TX.\"AcDate\" = :inputAcDate "; // 會計日期
		sql += "   AND ACD.\"SlipNo\" >= :inputSlipNoStart "; // 傳票號碼-起
		sql += "   AND ACD.\"SlipNo\" <= :inputSlipNoEnd "; // 傳票號碼-止
		sql += "   AND ACD.\"TitaTxtNo\" >= :inputTitaTxtNoStart "; // 交易序號-起
		sql += "   AND ACD.\"TitaTxtNo\" <= :inputTitaTxtNoEnd "; // 交易序號-止
//		sql += "   AND CASE WHEN :inputOption = 1 "; // 作業選項1:還本收據
//		sql += "            THEN TX.\"Principal\" ";
//		sql += "            WHEN :inputOption = 2 "; // 作業選項2:繳息收據
//		sql += "            THEN TX.\"Interest\" ";
//		sql += "       ELSE 0 END > 0 "; // 選項所對應的金額大於0時才入表

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);

		query.setParameter("inputAcDate", parse.stringToInteger(titaVo.getParam("inputAcDate")) + 19110000);
		query.setParameter("inputSlipNoStart", titaVo.getParam("inputSlipNoStart"));
		query.setParameter("inputSlipNoEnd", titaVo.getParam("inputSlipNoEnd"));
		query.setParameter("inputTitaTxtNoStart", titaVo.getParam("inputTitaTxtNoStart"));
		query.setParameter("inputTitaTxtNoEnd", titaVo.getParam("inputTitaTxtNoEnd"));
		query.setParameter("inputOption", titaVo.getParam("inputOption"));

		return this.convertToMap(query);
	}

}