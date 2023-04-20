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
public class L4926ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Autowired
	Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

//	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";


	public List<Map<String, String>> queryresult(int index, int limit, TitaVo titaVo) throws Exception {

		this.info("L4926ServiceImpl.queryresult");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		String iReconCode = titaVo.getParam("ReconCode");
		int iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		int iFEntryDate = 0;
		int iCustNo = Integer.parseInt(titaVo.getParam("CustNo"));
		String iTraderInfo = titaVo.getParam("TraderInfo");
		int iRepayAmt = Integer.parseInt(titaVo.getParam("RepayAmt"));

		if (iEntryDate > 0){
			iFEntryDate = iEntryDate + 19110000 ;
		}

		this.info("iReconCode=" + iReconCode + ",iFEntryDate=" + iFEntryDate);

		String sql = "";
		sql += " SELECT                         ";
		sql += "   \"AcDate\"     	"; // 資料日期(會計日)
		sql += " , \"BatchNo\"    	"; // 批號
		sql += " , \"DetailSeq\"  	"; // 明細序號
		sql += " , \"CustNo\"     	"; // 戶號
		sql += " , \"RepayType\"  	"; // 還款類別
		sql += " , \"RepayAmt\"   	"; // 還款金額
		sql += " , \"EntryDate\"  	"; // 入帳日期
		sql += " , \"DscptCode\"  	"; // 摘要代碼
		sql += " , \"RemintBank\" 	"; // 匯款銀行代碼
		sql += " , \"TraderInfo\" 	"; // 交易人資料
		sql += " , \"ReconCode\"  	"; // 對帳類別
		sql += " , \"TitaEntdy\"  	"; // 會計日期
		sql += " , \"TitaTlrNo\"  	"; // 經辦
		sql += " , \"TitaTxtNo\"  	"; // 交易序號

		sql += "FROM \"BankRmtf\" 		WHERE 1=1		\n";

		if (iCustNo > 0) {
			sql += " AND  \"CustNo\" = :custNo       \n";
		}
		if (iEntryDate > 0 ) {
			sql += " AND \"EntryDate\" = :entryDate  \n";
		}
		if (!iReconCode.isEmpty()) {
			sql += " AND  \"ReconCode\" = :reconCode      \n";
		}
		if (!iTraderInfo.isEmpty()) {
			sql += " AND  \"TraderInfo\" like :traderInfo      \n";
		}
		if (iRepayAmt > 0 ) {
			sql += " AND \"RepayAmt\" = :repayAmt  \n";
		}
		sql += " AND  NVL(\"AmlRsp\",'9') IN ('0','1','2')     \n";//排除已刪除資料
		sql += "ORDER BY \"EntryDate\" DESC , \"CustNo\" , \"ReconCode\" ";

//		sql += sqlRow;

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

//		query.setParameter("ThisIndex", index);
//		query.setParameter("ThisLimit", limit);

		if (!iReconCode.isEmpty()) {
			query.setParameter("reconCode", iReconCode);
		}
		if (iEntryDate > 0) {
			query.setParameter("entryDate", iFEntryDate);
		}
		if (iCustNo > 0) {
			query.setParameter("custNo", iCustNo);
		}
		if (!iTraderInfo.isEmpty()) {
			query.setParameter("traderInfo", "%" + iTraderInfo + "%");
		}
		if (iRepayAmt > 0 ) {
			query.setParameter("repayAmt", iRepayAmt);
		}
		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);
		
		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
//		query.setFirstResult(0);
		query.setFirstResult(this.index * this.limit);

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