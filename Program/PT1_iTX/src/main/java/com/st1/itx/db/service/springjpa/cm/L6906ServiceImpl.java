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

@Service("l6906ServiceImpl")
@Repository
public class L6906ServiceImpl extends ASpringJpaParm implements InitializingBean {
	@Autowired
	Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int index, int limit) throws Exception {
		this.info("L6906findAll");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		String iBranchNo = titaVo.getParam("BranchNo");
		String iCurrencyCode = titaVo.getParam("CurrencyCode");
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		String iTitaTlrNo = titaVo.getParam("TitaTlrNo");
		String iTitaBatchNo = titaVo.getParam("TitaBatchNo");
		String iTitaTxCd = titaVo.getParam("TitaTxCd");
		int iTitaTxtNo = parse.stringToInteger(titaVo.getParam("TitaTxtNo"));

		this.info("BranchNo = " + iBranchNo);
		this.info("CurrencyCode = " + iCurrencyCode);
		this.info("AcDate = " + iAcDate);
		this.info("CustNo = " + iCustNo);
		this.info("TitaTlrNo = " + iTitaTlrNo);
		this.info("TitaBatchNo = " + iTitaBatchNo);
		this.info("TitaTxCd = " + iTitaTxCd);
		this.info("TitaTxtNo = " + iTitaTxtNo);

		String sql = "SELECT   ";
		sql += "  A.\"CustNo\"			AS \"CustNo\" ";
		sql += " ,A.\"FacmNo\"			AS \"FacmNo\" ";
		sql += " ,A.\"BormNo\"			AS \"BormNo\" ";
		sql += " ,A.\"AcNoCode\"		AS \"AcNoCode\" ";
		sql += " ,A.\"AcSubCode\"		AS \"AcSubCode\" ";
		sql += " ,A.\"AcDtlCode\"		AS \"AcDtlCode\" ";
		sql += " ,A.\"TitaBatchNo\"		AS \"TitaBatchNo\" ";
		sql += " ,A.\"SlipNote\"		AS \"SlipNote\" ";
		sql += " ,A.\"TitaTxCd\"		AS \"TitaTxCd\" ";
		sql += " ,NVL(B.\"TranItem\",'')		AS \"TranItem\" ";
		sql += " ,A.\"TitaTlrNo\"		AS \"TitaTlrNo\" ";
		sql += " ,NVL(C.\"Fullname\",'')		AS \"TlrName\" ";
		sql += " ,A.\"TitaTxtNo\"		AS \"TitaTxtNo\" ";
		sql += " ,A.\"TitaSupNo\"		AS \"TitaSupNo\" ";
		sql += " ,NVL(D.\"Fullname\",'')		AS \"SupName\"  ";
		sql += " ,CASE WHEN A.\"DbCr\" = 'D' THEN A.\"TxAmt\" ELSE 0 END		AS \"DbAmt\" ";
		sql += " ,CASE WHEN A.\"DbCr\" = 'C' THEN A.\"TxAmt\" ELSE 0 END		AS \"CrAmt\" ";
		sql += " ,NVL(E.\"AcNoItem\",'')		AS \"AcNoItem\" ";
		sql += " ,A.\"LastUpdate\"		AS \"LastUpdate\" ";
		sql += " FROM \"AcDetail\" A ";
		sql += " LEFT JOIN \"TxTranCode\" B on B.\"TranNo\"= A.\"TitaTxCd\" "; // 交易中文
		sql += " LEFT JOIN \"CdEmp\" C on C.\"EmployeeNo\"= A.\"TitaTlrNo\" "; // 經辦姓名
		sql += " LEFT JOIN \"CdEmp\" D on D.\"EmployeeNo\"= A.\"TitaSupNo\" "; // 主管姓名
		sql += " LEFT JOIN \"CdAcCode\" E on E.\"AcNoCode\"= A.\"AcNoCode\" ";
		sql += "						 AND E.\"AcSubCode\"= A.\"AcSubCode\" ";
		sql += "						 AND E.\"AcDtlCode\"= A.\"AcDtlCode\" ";
		sql += "   AND A.\"CurrencyCode\" = :currencyCode ";

		sql += " WHERE A.\"BranchNo\" = :branchNo ";
		sql += "   AND A.\"CurrencyCode\" = :currencyCode ";
		sql += "   AND A.\"AcDate\" = :acDate ";
		if (!(iCustNo == 0)) {
			sql += "   AND A.\"CustNo\" = :custNo ";
			sql += "   AND A.\"EntAc\" = 1 ";
		} else if (!(iTitaTlrNo.isEmpty())) {
			sql += "   AND A.\"TitaTlrNo\" = :titaTlrNo ";
			if (iTitaTxtNo > 0) {
				sql += "   AND A.\"TitaTxtNo\" = :titaTxtNo ";
			} else {
				sql += "   AND A.\"EntAc\" = 1 ";
			}
		} else if (!(iTitaBatchNo.isEmpty())) {
			sql += "   AND A.\"TitaBatchNo\" = :titaBatchNo ";
			sql += "   AND A.\"EntAc\" > 0 ";
		} else if (!(iTitaTxCd.isEmpty())) {
			sql += "   AND A.\"TitaTxCd\" = :titaTxCd ";
			sql += "   AND A.\"EntAc\" > 0 ";
		} else {
			sql += "   AND A.\"EntAc\" = 1 ";
		}
		sql += "ORDER BY A.\"RelTxseq\" ASC ";
		sql += "		,A.\"CustNo\" ASC ";
		sql += "		,A.\"FacmNo\" ASC ";
		sql += "		,A.\"BormNo\" ASC ";
		sql += "		,A.\"AcNoCode\" ASC ";
		sql += "		,A.\"AcSubCode\" ASC ";
		sql += "		,A.\"AcDtlCode\" ASC ";

		this.info("FindL6906 sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("branchNo", iBranchNo);
		query.setParameter("currencyCode", iCurrencyCode);
		query.setParameter("acDate", iAcDate);

		if (!(iCustNo == 0)) {
			query.setParameter("custNo", iCustNo);
		} else if (!(iTitaTlrNo.isEmpty())) {
			query.setParameter("titaTlrNo", iTitaTlrNo);
			if (iTitaTxtNo > 0) {
				query.setParameter("titaTxtNo", iTitaTxtNo);
			}
		} else if (!(iTitaBatchNo.isEmpty())) {
			query.setParameter("titaBatchNo", iTitaBatchNo);
		} else if (!(iTitaTxCd.isEmpty())) {
			query.setParameter("titaTxCd", iTitaTxCd);
		}

		this.info("sql = " + sql);

		return switchback(query);
	}

}