package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4920ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4920ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L4920ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int flag, TitaVo titaVo) throws Exception {

		logger.info("L4920.findAll");

//		會計日期    #AcDate
//		整批批號    #BatchNo
//		作業狀態    #StatusCode
//		還款來源    #RepayCode
//		檔名            #FileName
//		處理狀態    #ProcStsCode
//		戶號           #CustNo

		int iAcDate = parse.stringToInteger(titaVo.get("AcDate")) + 19110000;
		String iBatchNo = titaVo.get("BatchNo");
		String iStatusCode = titaVo.get("StatusCode");
		int iRepayCode = parse.stringToInteger(titaVo.get("RepayCode"));
		String iFileName = titaVo.get("FileName");
		String iProcStsCode = titaVo.get("ProcStsCode");
		int iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		String iReconCode = titaVo.get("ReconCode");

		logger.info("acDate = " + iAcDate);
		logger.info("iBatchNo = " + iBatchNo);
		logger.info("iStatusCode = " + iStatusCode);
		logger.info("iRepayCode = " + iRepayCode);
		logger.info("iFileName = " + iFileName);
		logger.info("iProcStsCode = " + iProcStsCode);
		logger.info("iCustNo = " + iCustNo);
		logger.info("iReconCode = " + iReconCode);

		String sql = "";
		if (flag == 0) {
			sql += " select                                       ";
			sql += "   bd.\"DetailSeq\"    AS F0                  ";
			sql += "  ,bd.\"EntryDate\"    AS F1                  ";
			sql += "  ,bd.\"CustNo\"       AS F2                  ";
			sql += "  ,bd.\"FacmNo\"       AS F3                  ";
			sql += "  ,bd.\"RepayType\"    AS F4                  ";
			sql += "  ,bd.\"RepayCode\"    AS F5                  ";
			sql += "  ,bd.\"ReconCode\"    AS F6                  ";
			sql += "  ,ca.\"AcctItem\"     AS F7                  ";
			sql += "  ,bd.\"RepayAmt\"     AS F8                  ";
			sql += "  ,bd.\"AcctAmt\"      AS F9                  ";
			sql += "  ,bd.\"ProcStsCode\"  AS F10                 ";
			sql += "  ,bd.\"ProcCode\"     AS F11                 ";
			sql += "  ,bd.\"ProcNote\"     AS F12                 ";
			sql += "  ,bd.\"TitaTlrNo\"    AS F13                 ";
			sql += "  ,bd.\"TitaTxtNo\"    AS F14                 ";
			sql += "  ,bd.\"DisacctAmt\"   AS F15                 ";
		} else {
			sql += " select                                       ";
			sql += "   SUM(bd.\"RepayAmt\")     AS F0             ";
			sql += "  ,SUM(bd.\"AcctAmt\")      AS F1             ";
			sql += "  ,SUM(bd.\"RepayAmt\") - SUM(bd.\"AcctAmt\")   AS F2 ";
		}
		sql += " from \"BatxDetail\" bd                       ";
		sql += " left join (                                  ";
		sql += "     select                                   ";
		sql += "      \"AcctCode\"                            ";
		sql += "     ,\"AcctItem\"                            ";
		sql += "     ,row_number() over (partition by \"AcctCode\" order by \"AcctCode\" Desc) as seq  ";
		sql += "     from \"CdAcCode\"                        ";
		sql += " )  ca on ca.\"AcctCode\" = bd.\"ReconCode\"  ";
		sql += "      and ca.seq = 1                          ";
		sql += " left join \"BatxHead\" bh on bh.\"AcDate\"  = bd.\"AcDate\"   ";
		sql += "                          and bh.\"BatchNo\" = bd.\"BatchNo\"  ";
		sql += " where bd.\"AcDate\" =       " + iAcDate;

		if (!"".equals(iBatchNo)) {
			sql += "   and bd.\"BatchNo\" = '" + iBatchNo + "'";
		}

//		sql += "   and bh.\"BatxExeCode\" = '" + iStatusCode + "'";

		if (iRepayCode != 0) {
			sql += "   and bd.\"RepayCode\" = " + iRepayCode;
		}

		if (!"".equals(iReconCode.trim())) {
			sql += "   and bd.\"ReconCode\" = '" + iReconCode + "'";
		}

		if (!"".equals(iFileName.trim())) {
			sql += "   and bd.\"FileName\" = '" + iFileName.trim() + "'";
		}

		switch (iProcStsCode) {
		case "A":
			sql += "   and bd.\"ProcStsCode\" in ('0','1','2','3','4','5','6','7') ";
			break;
		case "R":
			sql += "   and bd.\"ProcStsCode\" in ('0','2','3','4') ";
			break;
		case "S":
			sql += "   and bd.\"ProcStsCode\" in ('5','6','7') ";
			break;
		default:
			sql += "   and bd.\"ProcStsCode\" = '" + iProcStsCode + "'";
			break;
		}

		if (iCustNo != 0) {
			sql += "   and bd.\"CustNo\" =    " + iCustNo;
		}

		if (flag == 1) {
			sql += "   and bd.\"ProcStsCode\" != 1 ";
			sql += "   GROUP BY bd.\"AcDate\" ";
		}

		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		cnt = query.getResultList().size();
		logger.info("Total cnt ..." + cnt);

		if (flag == 0) {
			// *** 折返控制相關 ***
			// 設定從第幾筆開始抓,需在createNativeQuery後設定
			query.setFirstResult(this.index * this.limit);

			// *** 折返控制相關 ***
			// 設定每次撈幾筆,需在createNativeQuery後設定
			query.setMaxResults(this.limit);
		}

		List<Object> result = query.getResultList();

		size = result.size();
		logger.info("Total size ..." + size);

		return this.convertToMap(result);
	}

	public List<Map<String, String>> findAll(int flag, int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(flag, titaVo);
	}

	public int getSize() {
		return cnt;
	}
}