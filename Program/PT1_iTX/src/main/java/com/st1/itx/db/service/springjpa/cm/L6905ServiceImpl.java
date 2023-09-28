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
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;;

@Service("l6905ServiceImpl")
@Repository
public class L6905ServiceImpl extends ASpringJpaParm implements InitializingBean {
	@Autowired
	Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

//	public String FindL5051(String FunctionCd, String PerfDateFm, String PerfDateTo, String CustNo, String FacmNo) throws Exception {
	public List<Map<String, String>> FindData(TitaVo titaVo, int index, int limit) throws Exception {
		this.info("L6905FindData");

		String iAcBookCode = titaVo.getParam("AcBookCode").trim();
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode").trim();
		String iBranchNo = titaVo.getParam("BranchNo").trim();
		String iCurrencyCode = titaVo.getParam("CurrencyCode").trim();
		String iAcNoCode = titaVo.getParam("AcNoCode").trim();
		String iAcSubCode = titaVo.getParam("AcSubCode").trim();
		String iAcDtlCode = titaVo.getParam("AcDtlCode").trim();
		String iRvNo = titaVo.getParam("RvNo").trim();
		String iDbCr = titaVo.getParam("DbCr");
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;

		int iInqType = this.parse.stringToInteger(titaVo.getParam("InqType"));
		int iSlipBatNo = this.parse.stringToInteger(titaVo.getParam("SlipBatNo"));
		int iInputTitaTxtNoStart = this.parse.stringToInteger(titaVo.getParam("InputTitaTxtNoStart"));
		int iInputTitaTxtNoEnd = this.parse.stringToInteger(titaVo.getParam("InputTitaTxtNoEnd"));
		if (iInputTitaTxtNoEnd == 0) {
			iInputTitaTxtNoEnd = 99999999;
		}

		String iInqData = titaVo.getParam("InqData").trim();

		String sql = "SELECT A.*,B.\"TranItem\",C.\"Fullname\" as TLRNAME,D.\"Fullname\" as SUPNAME,E.\"AcNoItem\" ";
		sql += "FROM \"AcDetail\" A ";
		sql += "LEFT JOIN \"TxTranCode\" B on B.\"TranNo\"=A.\"TitaTxCd\" ";
		sql += "LEFT JOIN \"CdEmp\" C on C.\"EmployeeNo\"=A.\"TitaTlrNo\" ";
		sql += "LEFT JOIN \"CdEmp\" D on D.\"EmployeeNo\"=A.\"TitaSupNo\" ";
		sql += "LEFT JOIN \"CdAcCode\" E on E.\"AcNoCode\"=A.\"AcNoCode\" and E.\"AcSubCode\"=A.\"AcSubCode\" and E.\"AcDtlCode\"=a.\"AcDtlCode\" ";
		sql += "WHERE A.\"AcDate\" = :AcDate ";
		sql += " AND A.\"EntAc\" > 0 ";

		if (!iBranchNo.isEmpty()) {
			sql += "AND A.\"BranchNo\" = :BranchNo ";
		}
		if (!iCurrencyCode.isEmpty()) {
			sql += "AND A.\"CurrencyCode\" = :CurrencyCode ";
		}
		if (!iAcBookCode.isEmpty()) {
			sql += "AND A.\"AcBookCode\" = :AcBookCode ";
		}
		if (!iAcSubBookCode.isEmpty()) {
			sql += "AND A.\"AcSubBookCode\" = :AcSubBookCode ";
		}
		if (!iAcNoCode.isEmpty()) {
			sql += "AND A.\"AcNoCode\" = :AcNoCode ";
		}
		if (!iAcSubCode.isEmpty()) {
			sql += "AND A.\"AcSubCode\" = :AcSubCode ";
		}
		if (!iAcDtlCode.isEmpty()) {
			sql += "AND A.\"AcDtlCode\" = :AcDtlCode ";
		}
		if (!iRvNo.isEmpty()) {
			sql += "AND A.\"RvNo\" like :RvNo ";
		}
		if ("1".equals(iDbCr)) {
			sql += "AND A.\"DbCr\"='D' ";
		} else if ("2".equals(iDbCr)) {
			sql += "AND A.\"DbCr\"='C' ";
		}
		if (iInqType == 0) {
		} else if (iInqType == 1) {
			sql += "AND \"SumNo\" = :SumNo ";
		} else if (iInqType == 2) {
			if (iInqData.length() != 0) {
				sql += "AND \"TitaTlrNo\" = :TitaTlrNo ";
			}
			sql += "AND \"TitaTxtNo\" >= :TitaTxtNoStart ";
			sql += "AND \"TitaTxtNo\" <= :TitaTxtNoEnd ";
		} else if (iInqType == 3) {
			sql += "AND CASE WHEN  NVL(\"TitaBatchNo\",' ') = ' '  AND :TitaBatchNo = ' '  THEN 1 ";
			sql += "         WHEN  SUBSTR(\"TitaBatchNo\",1,1) = ' ' AND :TitaBatchNo = ' ' THEN 1   ";
			sql += "         WHEN  \"TitaBatchNo\" = :TitaBatchNo THEN 1   ";
			sql += "        ELSE 0 END = 1   ";
		} else if (iInqType == 4) {
			sql += "AND CASE WHEN  NVL(\"DscptCode\",' ') = ' '  AND :DscptCode = ' '  THEN 1 ";
			sql += "         WHEN  SUBSTR(\"DscptCode\",1,1) = ' ' AND :DscptCode = ' ' THEN 1   ";
			sql += "         WHEN  \"DscptCode\" = :DscptCode THEN 1   ";
			sql += "        ELSE 0 END = 1   ";
		} else if (iInqType == 5) {
			sql += "AND \"SlipBatNo\" = :SlipBatNo ";
		} else if (iInqType == 6) {
			sql += "AND \"TitaSecNo\" = :TitaSecNo ";
		} else if (iInqType == 7) {
			sql += "AND \"SlipSumNo\" = :SlipSumNo ";
			sql += "AND \"SlipBatNo\" = :SlipBatNo ";
		} else if (iInqType == 8) {
			if (!iInqData.trim().isEmpty()) {
				sql += "AND \"MediaSlipNo\" = :MediaSlipNo ";
			} else {
				sql += "AND \"MediaSlipNo\" IS NULL ";
			}
			sql += "AND \"SlipBatNo\" = :SlipBatNo ";
		}
		sql += "ORDER BY A.\"SlipNo\" ASC  ";

		sql += sqlRow;

		this.info("FindL6905 sql=" + sql);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		query.setParameter("AcDate", iAcDate);

		if (!iAcBookCode.isEmpty()) {
			query.setParameter("AcBookCode", iAcBookCode);
			this.info("AcBookCode = " + iAcBookCode);
		}
		if (!iAcSubBookCode.isEmpty()) {
			query.setParameter("AcSubBookCode", iAcSubBookCode);
			this.info("AcSubBookCode = " + iAcSubBookCode);
		}
		if (!iBranchNo.isEmpty()) {
			query.setParameter("BranchNo", iBranchNo);
			this.info("BranchNo = " + iBranchNo);
		}
		if (!iCurrencyCode.isEmpty()) {
			query.setParameter("CurrencyCode", iCurrencyCode);
			this.info("CurrencyCode = " + iCurrencyCode);
		}
		if (!iAcNoCode.isEmpty()) {
			query.setParameter("AcNoCode", iAcNoCode);
			this.info("AcNoCode = " + iAcNoCode);
		}
		if (!iAcSubCode.isEmpty()) {
			query.setParameter("AcSubCode", iAcSubCode);
			this.info("AcSubCode = " + iAcSubCode);
		}
		if (!iAcDtlCode.isEmpty()) {
			query.setParameter("AcDtlCode", iAcDtlCode);
			this.info("AcDtlCode = " + iAcDtlCode);
		}
		if (!iRvNo.isEmpty()) {
			query.setParameter("RvNo", iRvNo);
			this.info("RvNo = " + iRvNo);
		}

		if (iInqType == 1) {
			query.setParameter("SumNo", FormatUtil.padX(iInqData, 3));
		} else if (iInqType == 2) {
			if (iInqData.length() != 0) {
				query.setParameter("TitaTlrNo", iInqData);
				this.info("TitaTlrNo = " + iInqData);
			}
			query.setParameter("TitaTxtNoStart", iInputTitaTxtNoStart);
			this.info("TitaTxtNoStart = " + iInputTitaTxtNoStart);
			query.setParameter("TitaTxtNoEnd", iInputTitaTxtNoEnd);
			this.info("TitaTxtNoEnd = " + iInputTitaTxtNoEnd);
		} else if (iInqType == 3) {
			query.setParameter("TitaBatchNo", iInqData.trim().isEmpty() ? " " : iInqData);
			this.info("TitaBatchNo = " + (iInqData.trim().isEmpty() ? " " : iInqData));
		} else if (iInqType == 4) {
			query.setParameter("DscptCode", iInqData.trim().isEmpty() ? " " : iInqData);
			this.info("DscptCode = " + (iInqData.trim().isEmpty() ? " " : iInqData));
		} else if (iInqType == 5) {
			query.setParameter("SlipBatNo", iInqData.trim().isEmpty() ? 0 : parse.stringToInteger(iInqData.trim()));
			this.info("SlipBatNo = " + (iInqData.trim().isEmpty() ? 0 : parse.stringToInteger(iInqData.trim())));
		} else if (iInqType == 6) {
			query.setParameter("TitaSecNo", FormatUtil.padX(iInqData, 2));
			this.info("TitaSecNo = " + (FormatUtil.padX(iInqData, 2)));
		} else if (iInqType == 7) {
			query.setParameter("SlipSumNo", iInqData.trim().isEmpty() ? 0 : parse.stringToInteger(iInqData.trim()));
			this.info("SlipSumNo = " + (iInqData.trim().isEmpty() ? 0 : parse.stringToInteger(iInqData.trim())));
			query.setParameter("SlipBatNo", iSlipBatNo);
			this.info("SlipBatNo = " + iSlipBatNo);
		} else if (iInqType == 8) {
			if (!iInqData.trim().isEmpty()) {
				query.setParameter("MediaSlipNo", iInqData);
				this.info("MediaSlipNo = " + (iInqData.trim().isEmpty() ? "" : iInqData));
			}
			query.setParameter("SlipBatNo", iSlipBatNo);
			this.info("SlipBatNo = " + iSlipBatNo);
		}

		this.info("L6905Service FindData=" + query);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);
	}

}