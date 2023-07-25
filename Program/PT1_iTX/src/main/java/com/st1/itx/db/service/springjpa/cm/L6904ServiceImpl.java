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

@Service("l6904ServiceImpl")
@Repository
public class L6904ServiceImpl extends ASpringJpaParm implements InitializingBean {
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

	public List<Map<String, String>> FindData(TitaVo titaVo, int index, int limit) throws Exception {
		this.info("L6904FindData");

		// 取得輸入資料
		String iAcBookCode = titaVo.getParam("AcBookCode"); // 帳冊別
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode"); // 區隔帳冊
		String iBranchNo = titaVo.getParam("BranchNo"); // 單位別
		String iCurrencyCode = titaVo.getParam("CurrencyCode"); // 幣別
		String iAcNoCodeS = titaVo.getParam("AcNoCode").trim(); // 科子細目 科目
		String iAcNoCodeE = "";
		String iAcSubCodeS = titaVo.getParam("AcSubCode").trim(); // 科子細目 子目
		String iAcSubCodeE = "";
		String iAcDtlCodeS = titaVo.getParam("AcDtlCode").trim(); // 科子細目 細目
		String iAcDtlCodeE = "";
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate")); // 會計日期
		int iFAcDate = iAcDate + 19110000;
		int iInqType = this.parse.stringToInteger(titaVo.getParam("InqType")); // 彙計方式

		if (iAcNoCodeS.isEmpty()) {
			iAcNoCodeS = "           ";
			iAcNoCodeE = "ZZZZZZZZZZZ";
		} else {
			iAcNoCodeE = titaVo.getParam("AcNoCode").trim();
		}
		if (iAcSubCodeS.isEmpty()) {
			iAcSubCodeS = "     ";
			iAcSubCodeE = "ZZZZZ";
		} else {
			iAcSubCodeE = titaVo.getParam("AcDtlCode").trim();
		}
		if (iAcDtlCodeS.isEmpty()) {
			iAcDtlCodeS = "  ";
			iAcDtlCodeE = "ZZ";
		} else {
			iAcDtlCodeE = titaVo.getParam("AcDtlCode").trim();
		}
		this.info(" AcBookCode = " + iAcBookCode);
		this.info(" AcSubBookCode = " + iAcSubBookCode);
		this.info(" BranchNo = " + iBranchNo);
		this.info(" CurrencyCode = " + iCurrencyCode);
		this.info(" AcDate = " + iFAcDate);
		this.info(" AcNoCode BETWEEN = " + iAcNoCodeS + " " + iAcNoCodeE);
		this.info(" AcSubCode BETWEEN = " + iAcSubCodeS + " " + iAcSubCodeE);
		this.info(" AcDtlCode BETWEEN = " + iAcDtlCodeS + " " + iAcDtlCodeE);

		String sql = " WITH \"Data\" AS ( ";
		sql += "  select \"AcNoCode\"  AS \"AcNoCode\" ";
		sql += "        , \"AcSubCode\" AS \"AcSubCode\" ";
		sql += "        , \"AcDtlCode\" AS \"AcDtlCode\" ";
		sql += "        , SUM(CASE WHEN \"DbCr\" ='D' THEN 1 ELSE 0  END ) AS \"DCNT\" ";
		sql += "        , SUM(CASE WHEN \"DbCr\" ='C' THEN 1 ELSE 0  END ) AS \"CCNT\" ";
		sql += "        , SUM(CASE WHEN \"DbCr\" ='D' THEN \"TxAmt\"  ELSE 0  END ) AS \"DAMT\" ";
		sql += "        , SUM(CASE WHEN \"DbCr\" ='C' THEN \"TxAmt\"  ELSE 0  END ) AS \"CAMT\" ";

		switch (iInqType) {
		case 0: // 全抓
			sql += "        , ' '  AS \"DataInq\" ";
			break;
		case 1: // 彙總別
			sql += "        , \"SumNo\" AS \"DataInq\" ";
			break;
		case 2: // 經辦別
			sql += "        , \"TitaTlrNo\" AS \"DataInq\" ";
			break;
		case 3: // 整批批號
			sql += "        , \"TitaBatchNo\" AS \"DataInq\" ";
			break;
		case 4: // 摘要代號
			sql += "        , \"DscptCode\" AS \"DataInq\" ";
			break;
		case 5: // 傳票批號
			sql += "        , \"SlipBatNo\" AS \"DataInq\" ";
			break;
		case 6: // 業務類別
			sql += "        , \"TitaSecNo\" AS \"DataInq\" ";
			break;
		case 7: // 彙總傳票號碼
			sql += "        , \"SlipSumNo\" AS \"DataInq\"";
			break;
		}
		sql += "        from \"AcDetail\"  ";
		sql += "        WHERE  \"AcBookCode\" = :acBookCode  ";
		sql += "                AND \"AcSubBookCode\" LIKE :acSubBookCode||'%' ";
		sql += "                AND \"BranchNo\"   = :branchNo  ";
		sql += "                AND \"CurrencyCode\" = :currencyCode ";
		sql += "                AND \"AcDate\" = :acDate ";
		sql += "                AND \"AcNoCode\" BETWEEN :acNoCodeS AND :acNoCodeE ";
		sql += "                AND \"AcSubCode\" BETWEEN :acSubCodeS AND :acSubCodeE ";
		sql += "                AND \"AcDtlCode\" BETWEEN :acDtlCodeS AND :acDtlCodeE ";
		sql += "                AND \"EntAc\" > 0 ";
		sql += "        GROUP BY 	  ";
		sql += "          \"AcNoCode\" ";
		sql += "        , \"AcSubCode\" ";
		sql += "        , \"AcDtlCode\" ";
		switch (iInqType) {
		case 0: // 全抓
			break;
		case 1: // 彙總別
			sql += "        , \"SumNo\" ";
			break;
		case 2: // 經辦別
			sql += "        , \"TitaTlrNo\" ";
			break;
		case 3: // 整批批號
			sql += "        , \"TitaBatchNo\" ";
			break;
		case 4: // 摘要代號
			sql += "        , \"DscptCode\" ";
			break;
		case 5: // 傳票批號
			sql += "        , \"SlipBatNo\" ";
			break;
		case 6: // 業務類別
			sql += "        , \"TitaSecNo\" ";
			break;
		case 7: // 彙總傳票號碼
			sql += "        , \"SlipSumNo\" ";
			break;
		}
		sql += "        ORDER BY \"AcNoCode\"  ";
		sql += "        , \"AcSubCode\" ";
		sql += "        , \"AcDtlCode\") ";
		sql += "        SELECT  '' AS \"AcNoCode\"   ";
		sql += "        , '' AS \"AcSubCode\" ";
		sql += "        , '' AS \"AcDtlCode\" ";
		sql += "        ,SUM(\"DCNT\")  AS \"SumDCnt\" ";
		sql += "        ,SUM(\"CCNT\")  AS \"SumCCnt\" ";
		sql += "        ,SUM(\"DAMT\")  AS \"SumDAmt\" ";
		sql += "        ,SUM(\"CAMT\")  AS \"SumCAmt\" ";
		sql += "        ,TO_NCHAR('')  AS \"AcNoItem\" ";
		sql += "        ,TO_NCHAR('')  AS \"DataInq\" ";
		sql += "        ,TO_NCHAR('')  AS \"BankRmftItem\" ";
		sql += "        FROM \"Data\"  ";
		sql += "        UNION ALL  ";
		sql += "        SELECT  ";
		sql += "        d.\"AcNoCode\"  AS \"AcNoCode\" ";
		sql += "        , d.\"AcSubCode\" AS \"AcSubCode\" ";
		sql += "        , d.\"AcDtlCode\" AS \"AcDtlCode\" ";
		sql += "        ,d.\"DCNT\" AS \"SumDCnt\" ";
		sql += "        , d.\"CCNT\" AS \"SumCCnt\" ";
		sql += "        , d.\"DAMT\" AS \"SumDAmt\" ";
		sql += "        , d.\"CAMT\" AS \"SumCAmt\"  ";
		sql += "        , cac.\"AcNoItem\" AS \"AcNoItem\" ";
		sql += "        , d.\"DataInq\"  AS \"DataInq\" ";
		sql += "        , cd.\"Item\"  AS \"BankRmftItem\" ";
		sql += "        FROM \"Data\" d ";
		sql += "        LEFT JOIN \"CdAcCode\" cac ON  cac.\"AcNoCode\" = d.\"AcNoCode\" ";
		sql += "                        AND cac.\"AcSubCode\" = d.\"AcSubCode\" ";
		sql += "                        AND cac.\"AcDtlCode\" = d.\"AcDtlCode\" ";
		sql += "        LEFT JOIN \"CdCode\" cd ON cd.\"DefCode\" = 'BankRmftCode' ";
		sql += "                        AND  cd.\"Code\" = d.\"DataInq\" ";

		sql += sqlRow;

		this.info("FindL6904 sql=" + sql);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		query.setParameter("acBookCode", iAcBookCode);
		query.setParameter("acSubBookCode", iAcSubBookCode);
		query.setParameter("branchNo", iBranchNo);
		query.setParameter("currencyCode", iCurrencyCode);
		query.setParameter("acDate", iFAcDate);
		query.setParameter("acNoCodeS", iAcNoCodeS);
		query.setParameter("acNoCodeE", iAcNoCodeE);
		query.setParameter("acSubCodeS", iAcSubCodeS);
		query.setParameter("acSubCodeE", iAcSubCodeE);
		query.setParameter("acDtlCodeS", iAcDtlCodeS);
		query.setParameter("acDtlCodeE", iAcDtlCodeE);

		this.info("L6904Service FindData=" + query.toString());

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