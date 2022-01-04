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
public class L8923ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryfindbycustno(int index, int limit, TitaVo titaVo) throws Exception {

		this.info("L8923ServiceImpl.queryRecordDate");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		int iRecordDateStart = this.parse.stringToInteger(titaVo.getParam("RecordDateStart"));
		int iRecordDateEnd = this.parse.stringToInteger(titaVo.getParam("RecordDateEnd"));
		int iFRecordDateStart = iRecordDateStart + 19110000;
		int iFRecordDateEnd = iRecordDateEnd + 19110000;
		this.info("iFRecordDateStart=" + iFRecordDateStart + ",iFRecordDateEnd=" + iFRecordDateEnd);

		int iActualRepayDateStart = this.parse.stringToInteger(titaVo.getParam("ActualRepayDateStart"));
		int iActualRepayDateEnd = this.parse.stringToInteger(titaVo.getParam("ActualRepayDateEnd"));
		int iFActualRepayDateStart = iActualRepayDateStart + 19110000;
		int iFActualRepayDateEnd = iActualRepayDateEnd + 19110000;
		this.info("iFActualRepayDateStart=" + iFActualRepayDateStart + ",iFActualRepayDateEnd=" + iFActualRepayDateEnd);
		int iCustNo = Integer.parseInt(titaVo.getParam("CustNo"));

		String sql = "";
		sql += " SELECT                                 \n";
		sql += "\"RecordDate\" as F0					\n"; // 訪談日
		sql += ",\"ActualRepayDate\" as F1				\n"; // 實際還款日
		sql += ",\"CustNo\" as F2						\n"; // 戶號
		sql += ",\"FacmNo\" as F3						\n"; // 額度
		sql += ",\"BormNo\" as F4						\n"; // 撥款序號
		sql += ",\"RepayAmt\" as F5						\n"; // 還款金額
		sql += ",\"Career\" as F6						\n"; // 職業
		sql += ",\"Income\" as F7						\n"; // 年收入
		sql += ",\"RepaySource\" as F8					\n"; // 還款來源
		sql += ",\"RepayBank\" as F9					\n"; // 代償銀行
		sql += ",\"LastUpdateEmpNo\" as F10				\n"; // 更新人員
		sql += ",\"LastUpdate\" as F11					\n"; // 更新時間
		sql += ",\"RepayDate\" as F12					\n"; // 預定還款日期
		sql += ",\"Description\" as F13					\n"; // 其他說明
		sql += "from \"MlaundryRecord\" 				\n";
		if (iRecordDateStart > 0) {
			sql += "where \"RecordDate\" >= :recordDateStart and \"RecordDate\" <= :recordDateEnd  \n";
		}
		if (iActualRepayDateStart > 0) {
			sql += "where \"ActualRepayDate\" >= :actualRepayDateStart  and \"ActualRepayDate\" <= :actualRepayDateEnd  \n";
		}
		if (iCustNo > 0) {
			sql += " and  \"CustNo\" = :custNo       \n";
		}
		sql += "order by \"RecordDate\",\"ActualRepayDate\", \"CustNo\",\"FacmNo\",\"BormNo\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		if (iRecordDateStart > 0) {
			query.setParameter("recordDateStart", iFRecordDateStart);
			query.setParameter("recordDateEnd", iFRecordDateEnd);
		}
		if (iActualRepayDateStart > 0) {
			query.setParameter("actualRepayDateStart", iFActualRepayDateStart);
			query.setParameter("actualRepayDateEnd", iFActualRepayDateEnd);
		}
		if (iCustNo > 0) {
			query.setParameter("custNo", iCustNo);
		}

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(result);

	}

	public List<Map<String, String>> queryresult(int index, int limit, TitaVo titaVo) throws Exception {

		this.info("L8923ServiceImpl.queryRecordDate");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		int iRecordDateStart = this.parse.stringToInteger(titaVo.getParam("RecordDateStart"));
		int iRecordDateEnd = this.parse.stringToInteger(titaVo.getParam("RecordDateEnd"));
		int iFRecordDateStart = iRecordDateStart + 19110000;
		int iFRecordDateEnd = iRecordDateEnd + 19110000;
		this.info("iFRecordDateStart=" + iFRecordDateStart + ",iFRecordDateEnd=" + iFRecordDateEnd);

		int iActualRepayDateStart = this.parse.stringToInteger(titaVo.getParam("ActualRepayDateStart"));
		int iActualRepayDateEnd = this.parse.stringToInteger(titaVo.getParam("ActualRepayDateEnd"));
		int iFActualRepayDateStart = iActualRepayDateStart + 19110000;
		int iFActualRepayDateEnd = iActualRepayDateEnd + 19110000;
		this.info("iFActualRepayDateStart=" + iFActualRepayDateStart + ",iFActualRepayDateEnd=" + iFActualRepayDateEnd);
		int iCustNo = Integer.parseInt(titaVo.getParam("CustNo"));
		String iCustName = titaVo.getParam("CustName");

		String sql = "";
		sql += " SELECT                                 \n";
		sql += "M.\"RecordDate\" as F0					\n"; // 訪談日
		sql += ",M.\"ActualRepayDate\" as F1				\n"; // 實際還款日
		sql += ",M.\"CustNo\" as F2						\n"; // 戶號
		sql += ",M.\"FacmNo\" as F3						\n"; // 額度
		sql += ",M.\"BormNo\" as F4						\n"; // 撥款序號
		sql += ",M.\"RepayAmt\" as F5						\n"; // 還款金額
		sql += ",M.\"Career\" as F6						\n"; // 職業
		sql += ",M.\"Income\" as F7						\n"; // 年收入
		sql += ",M.\"RepaySource\" as F8					\n"; // 還款來源
		sql += ",M.\"RepayBank\" as F9					\n"; // 代償銀行
		sql += ",M.\"LastUpdateEmpNo\" as F10				\n"; // 更新人員
		sql += ",M.\"LastUpdate\" as F11					\n"; // 更新時間
		sql += ",M.\"RepayDate\" as F12					\n"; // 預定還款日期
		sql += ",M.\"Description\" as F13					\n"; // 其他說明
		sql += "from \"MlaundryRecord\" M				\n";
		if (!iCustName.isEmpty() && iCustNo == 0) {
			sql += "left join \"CustMain\" C on C.\"CustNo\" = M.\"CustNo\" \n";
		}
		if (iRecordDateStart > 0) {
			sql += "where M.\"RecordDate\" >= :recordDateStart and M.\"RecordDate\" <= :recordDateEnd  \n";
		}
		if (iActualRepayDateStart > 0) {
			sql += "where M.\"ActualRepayDate\" >= :actualRepayDateStart  and M.\"ActualRepayDate\" <= :actualRepayDateEnd  \n";
		}
		if (iCustNo > 0) {
			sql += " and  M.\"CustNo\" = :custNo       \n";
		}
		if (!iCustName.isEmpty() && iCustNo == 0) {
			sql += " and  C.\"CustName\" like :custName      \n";
		}
		sql += "order by M.\"RecordDate\",M.\"ActualRepayDate\", M.\"CustNo\",M.\"FacmNo\",M.\"BormNo\" ";

		sql += sqlRow;

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		if (iRecordDateStart > 0) {
			query.setParameter("recordDateStart", iFRecordDateStart);
			query.setParameter("recordDateEnd", iFRecordDateEnd);
		}
		if (iActualRepayDateStart > 0) {
			query.setParameter("actualRepayDateStart", iFActualRepayDateStart);
			query.setParameter("actualRepayDateEnd", iFActualRepayDateEnd);
		}
		if (iCustNo > 0) {
			query.setParameter("custNo", iCustNo);
		}
		if (!iCustName.isEmpty() && iCustNo == 0) {
			query.setParameter("custName", "%" + iCustName + "%");
		}

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(0);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);

	}

	public int getSize() {
		return cnt;
	}

}