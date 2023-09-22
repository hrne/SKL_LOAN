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

@Service("l6903ServiceImpl")
@Repository
public class L6903ServiceImpl extends ASpringJpaParm implements InitializingBean {
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
		this.info("L6903FindData");

		String iAcBookCode = titaVo.getParam("AcBookCode").trim();
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode").trim();
		String iBranchNo = titaVo.getParam("BranchNo").trim();
		String iCurrencyCode = titaVo.getParam("CurrencyCode").trim();
		String iAcNoCode = titaVo.getParam("AcNoCode").trim();
		String iAcSubCode = titaVo.getParam("AcSubCode").trim();
		String iAcDtlCode = titaVo.getParam("AcDtlCode").trim();
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iEntAc = this.parse.stringToInteger(titaVo.getParam("EntAc"));
		String iMediaSlipNo = titaVo.getParam("MediaSlipNo").trim();
		String iRvNo = titaVo.getParam("RvNo").trim();
		int iAcDateS = this.parse.stringToInteger(titaVo.getParam("AcDateSt")) + 19110000;
		int iAcDateE = this.parse.stringToInteger(titaVo.getParam("AcDateEd")) + 19110000;

		String sql = "SELECT   ";
		sql += " A.*, ";
		sql += "  B.\"TranItem\" AS \"TranItem\" , ";
		sql += "  C.\"Fullname\" as \"TlrName\" , ";
		sql += "  D.\"Fullname\" as \"SupName\" , ";
		sql += " E.\\\"AcNoItem\\\" AS \\\"AcNoItem\\\" ";
		sql += " FROM \"AcDetail\" A ";
		sql += " LEFT JOIN \"TxTranCode\" B on B.\"TranNo\"= A.\"TitaTxCd\" ";
		sql += " LEFT JOIN \"CdEmp\" C on C.\"EmployeeNo\"= A.\"TitaTlrNo\" ";
		sql += " LEFT JOIN \"CdEmp\" D on D.\"EmployeeNo\"= A.\"TitaSupNo\" ";
		sql += " LEFT JOIN \"CdAcCode\" E on E.\"AcNoCode\"= A.\"AcNoCode\" and E.\"AcSubCode\"= A.\"AcSubCode\" and E.\"AcDtlCode\"= a.\"AcDtlCode\" ";
		sql += " WHERE A.\"AcDate\" BETWEEN :AcDateS AND :AcDateE ";
		if (iEntAc != 99) {
			sql += " AND A.\"EntAc\" = :entAc ";
		}
		if (!iAcBookCode.isEmpty()) {
			sql += "AND A.\"AcBookCode\" = :AcBookCode ";
		}
		if (!iAcSubBookCode.isEmpty()) {
			sql += "AND A.\"AcSubBookCode\" = :AcSubBookCode ";
		}
		if (!iBranchNo.isEmpty()) {
			sql += "AND A.\"BranchNo\" = :BranchNo ";
		}
		if (!iCurrencyCode.isEmpty()) {
			sql += "AND A.\"CurrencyCode\" = :CurrencyCode ";
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
		if (iCustNo > 0) {
			sql += "AND A.\"CustNo\" = :CustNo ";
		}
		if (!iMediaSlipNo.isEmpty()) {
			sql += "AND A.\"MediaSlipNo\" = :MediaSlipNo ";
		}
		if (!iRvNo.isEmpty()) {
			sql += "AND A.\"RvNo\" like :RvNo ";
		}
		sql += "ORDER BY A.\"AcDate\" ASC ";

		sql += sqlRow;

		this.info("FindL6903 sql=" + sql);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		query.setParameter("AcDateS", iAcDateS);
		query.setParameter("AcDateE", iAcDateE);
		if (iEntAc != 99) {
			query.setParameter("entAc", iEntAc);
		}
		if (!iAcBookCode.isEmpty()) {
			query.setParameter("AcBookCode", iAcBookCode);
		}
		if (!iAcSubBookCode.isEmpty()) {
			query.setParameter("AcSubBookCode", iAcSubBookCode);
		}
		if (!iBranchNo.isEmpty()) {
			query.setParameter("BranchNo", iBranchNo);
		}
		if (!iCurrencyCode.isEmpty()) {
			query.setParameter("CurrencyCode", iCurrencyCode);
		}
		if (!iAcNoCode.isEmpty()) {
			query.setParameter("AcNoCode", iAcNoCode);
		}
		if (!iAcSubCode.isEmpty()) {
			query.setParameter("AcSubCode", iAcSubCode);
		}
		if (!iAcDtlCode.isEmpty()) {
			query.setParameter("AcDtlCode", iAcDtlCode);
		}
		if (iCustNo > 0) {
			query.setParameter("CustNo", iCustNo);
		}
		if (!iMediaSlipNo.isEmpty()) {
			query.setParameter("MediaSlipNo", iMediaSlipNo);
		}
		if (!iRvNo.isEmpty()) {
			query.setParameter("RvNo", iRvNo);
		}

		this.info("L6903Service FindData=" + query);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> FindAmt(TitaVo titaVo) throws Exception {
		this.info("L6903FindData");

		String iAcBookCode = titaVo.getParam("AcBookCode").trim();
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode").trim();
		String iBranchNo = titaVo.getParam("BranchNo").trim();
		String iCurrencyCode = titaVo.getParam("CurrencyCode").trim();
		String iAcNoCode = titaVo.getParam("AcNoCode").trim();
		String iAcSubCode = titaVo.getParam("AcSubCode").trim();
		String iAcDtlCode = titaVo.getParam("AcDtlCode").trim();
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		String iMediaSlipNo = titaVo.getParam("MediaSlipNo").trim();
		String iRvNo = titaVo.getParam("RvNo").trim();
		int iAcDateS = this.parse.stringToInteger(titaVo.getParam("AcDateSt")) + 19110000;
		int iAcDateE = this.parse.stringToInteger(titaVo.getParam("AcDateEd")) + 19110000;
		this.info("AcBookCode   =" + iAcBookCode);
		this.info("CurrencyCode =" + iCurrencyCode);
		this.info("BranchNo     =" + iBranchNo);
		this.info("acdates      =" + iAcDateS);
		this.info("acdatee      =" + iAcDateE);

		String sql = "SELECT  sum(a.\"TxAmt\") AS \"TxAmt\", ";
		sql += "                  a.\"DbCr\"   AS  \"DbCr\" ";
		sql += " FROM \"AcDetail\" A ";
		sql += " LEFT JOIN \"TxTranCode\" B on B.\"TranNo\"= A.\"TitaTxCd\" ";
		sql += " LEFT JOIN \"CdEmp\" C on C.\"EmployeeNo\"= A.\"TitaTlrNo\" ";
		sql += " LEFT JOIN \"CdEmp\" D on D.\"EmployeeNo\"= A.\"TitaSupNo\" ";
		sql += " LEFT JOIN \"CdAcCode\" E on E.\"AcNoCode\"= A.\"AcNoCode\" and E.\"AcSubCode\"= A.\"AcSubCode\" and E.\"AcDtlCode\"= a.\"AcDtlCode\" ";
		sql += " WHERE A.\"AcDate\" BETWEEN :AcDateS AND :AcDateE ";
		sql += " AND A.\"EntAc\" > 0 ";

		if (!iAcBookCode.isEmpty()) {
			sql += "AND A.\"AcBookCode\" = :AcBookCode ";
		}
		if (!iAcSubBookCode.isEmpty()) {
			sql += "AND A.\"AcSubBookCode\" = :AcSubBookCode ";
		}
		if (!iBranchNo.isEmpty()) {
			sql += "AND A.\"BranchNo\" = :BranchNo ";
		}
		if (!iCurrencyCode.isEmpty()) {
			sql += "AND A.\"CurrencyCode\" = :CurrencyCode ";
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
		if (iCustNo > 0) {
			sql += "AND A.\"CustNo\" = :CustNo ";
		}
		if (!iMediaSlipNo.isEmpty()) {
			sql += "AND A.\"MediaSlipNo\" = :MediaSlipNo ";
		}
		if (!iRvNo.isEmpty()) {
			sql += "AND A.\"RvNo\" like :RvNo ";
		}
		sql += "GROUP BY A.\"DbCr\" ";
//		sql += "ORDER BY A.\"AcDate\" ASC ";

		this.info("FindL6903Amt sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("AcDateS", iAcDateS);
		query.setParameter("AcDateE", iAcDateE);

		if (!iAcBookCode.isEmpty()) {
			query.setParameter("AcBookCode", iAcBookCode);
		}
		if (!iAcSubBookCode.isEmpty()) {
			query.setParameter("AcSubBookCode", iAcSubBookCode);
		}
		if (!iBranchNo.isEmpty()) {
			query.setParameter("BranchNo", iBranchNo);
		}
		if (!iCurrencyCode.isEmpty()) {
			query.setParameter("CurrencyCode", iCurrencyCode);
		}
		if (!iAcNoCode.isEmpty()) {
			query.setParameter("AcNoCode", iAcNoCode);
		}
		if (!iAcSubCode.isEmpty()) {
			query.setParameter("AcSubCode", iAcSubCode);
		}
		if (!iAcDtlCode.isEmpty()) {
			query.setParameter("AcDtlCode", iAcDtlCode);
		}
		if (iCustNo > 0) {
			query.setParameter("CustNo", iCustNo);
		}
		if (!iMediaSlipNo.isEmpty()) {
			query.setParameter("MediaSlipNo", iMediaSlipNo);
		}
		if (!iRvNo.isEmpty()) {
			query.setParameter("RvNo", iRvNo);
		}

		this.info("L6903Service FindData=" + query);

		return this.convertToMap(query);
	}
}