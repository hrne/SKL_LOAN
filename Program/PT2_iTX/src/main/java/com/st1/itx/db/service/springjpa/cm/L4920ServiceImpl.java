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

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;
	
	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int flag, int index, int limit, TitaVo titaVo) throws Exception {

		this.info("L4920.findAll");

//		會計日期    #AcDate
//		整批批號    #BatchNo
//		作業狀態    #StatusCode
//		還款來源    #RepayCode
//		檔名           #FileName
//		對帳類別    # ReconCode			
//		處理狀態    #ProcStsCode
//		戶號           #CustNo

		int iAcDate = parse.stringToInteger(titaVo.get("AcDate")) + 19110000;
		String iBatchNo = titaVo.get("BatchNo").trim();
		String iStatusCode = titaVo.get("StatusCode");
		int iRepayCode = parse.stringToInteger(titaVo.get("RepayCode"));
		String iFileName = titaVo.get("FileName").trim();
		String iProcStsCode = titaVo.get("ProcStatusCode");
		int iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		String iReconCode = titaVo.get("ReconCode").trim();

		this.info("acDate = " + iAcDate);
		this.info("iBatchNo = " + iBatchNo);
		this.info("iStatusCode = " + iStatusCode);
		this.info("iRepayCode = " + iRepayCode);
		this.info("iFileName = " + iFileName);
		this.info("iProcStsCode = " + iProcStsCode);
		this.info("iCustNo = " + iCustNo);
		this.info("iReconCode = " + iReconCode);

		String sql = "";
		if (flag == 0) {
			sql += " select                                       ";
			sql += "   bd.\"DetailSeq\"                 ";
			sql += "  ,bd.\"EntryDate\"                 ";
			sql += "  ,bd.\"CustNo\"                    ";
			sql += "  ,bd.\"FacmNo\"                    ";
			sql += "  ,bd.\"RepayType\"                 ";
			sql += "  ,bd.\"RepayCode\"                 ";
			sql += "  ,bd.\"ReconCode\"                 ";
			sql += "  ,nvl(ca.\"AcctItem\", bd.\"ReconCode\") AS \"ReconCodeX\" ";
			sql += "  ,bd.\"RepayAmt\"                  ";
			sql += "  ,bd.\"AcctAmt\"                   ";
			sql += "  ,bd.\"ProcStsCode\"               ";
			sql += "  ,bd.\"ProcCode\"                  ";
			sql += "  ,bd.\"ProcNote\"                  ";
			sql += "  ,bd.\"TitaTlrNo\"                 ";
			sql += "  ,bd.\"TitaTxtNo\"                 ";
			sql += "  ,bd.\"DisacctAmt\"                ";
			sql += "  ,bd.\"FileName\"                  ";
			sql += " from \"BatxDetail\" bd                       ";
			sql += " left join (                                  ";
			sql += "     select                                   ";
			sql += "      \"AcctCode\"                            ";
			sql += "     ,\"AcctItem\"                            ";
			sql += "     ,row_number() over (partition by \"AcctCode\" order by \"AcctCode\" Desc) as seq  ";
			sql += "     from \"CdAcCode\"                        ";
			sql += " )  ca on ca.\"AcctCode\" = bd.\"ReconCode\"  ";
			sql += "      and ca.seq = 1                          ";
		} else {
			sql += " select                                       ";
			sql += "   SUM(bd.\"RepayAmt\")     AS F0             ";
			sql += "  ,SUM(bd.\"AcctAmt\")      AS F1             ";
			sql += "  ,SUM(bd.\"RepayAmt\") - SUM(bd.\"AcctAmt\")   AS F2 ";
			sql += " from \"BatxDetail\" bd                       ";
		}

		sql += " where bd.\"AcDate\" =       " + iAcDate;

		if (!iBatchNo.isEmpty()) {
			sql += "   and bd.\"BatchNo\" = '" + iBatchNo + "'";
		}

		// 刪除
		if ("8".equals(iStatusCode)) {
			sql += "   and bd.\"ProcStsCode\" in ('D') ";
		} else {
			sql += "   and bd.\"ProcStsCode\" not in ('D') ";
		}

		if (iRepayCode != 0) {
			sql += "   and bd.\"RepayCode\" = " + iRepayCode;
		}

		if (!iReconCode.isEmpty()) {
			sql += "   and bd.\"ReconCode\" = '" + iReconCode + "'";
		}

		if (!iFileName.isEmpty()) {
			sql += "   and bd.\"FileName\" = '" + iFileName + "'";
		}

		switch (iProcStsCode) {
		case "A":
			break;
		case "R":
			sql += "   and bd.\"ProcStsCode\" in ('0','2','3','4') ";
			break;
		case "S":
			sql += "   and bd.\"ProcStsCode\" in ('5','6','7') AND bd.\"RepayType\" <> 9 ";
			break;
		case "T":
			sql += "   and bd.\"ProcStsCode\" in ('5','6','7') AND bd.\"RepayType\" = 9 ";
			break;
		default:
			sql += "   and bd.\"ProcStsCode\" = '" + iProcStsCode + "'";
			break;
		}

		if (iCustNo != 0) {
			sql += "   and bd.\"CustNo\" =    " + iCustNo;
		}

		if (flag == 1) {
			sql += "   and bd.\"ProcStsCode\" != '1' ";
			sql += "   GROUP BY bd.\"AcDate\" ";
		}
		sql += " " + sqlRow;

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);
		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
//		query.setFirstResult(this.index * this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);
		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(query);
	}

	public int getSize() {
		return cnt;
	}
}