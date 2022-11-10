package com.st1.itx.db.service.springjpa.cm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l5974ServiceImpl")
@Repository
public class L5974ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	L5051ServiceImpl l5051ServiceImpl;

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
	// 注意異動此邊欄位,或SQL語法 請檢查L5974

	public String sqlL5974(String FinCode) {

		String SqlNegFinAcct = "SELECT " + "FA.\"FinCode\" AS \"OOFinCode\"," + "FA.\"FinItem\" AS \"OOFinCodeX\","
				+ "FA.\"RemitBank\" AS \"OORemitBank\"," + "FA1.\"FinItem\" AS \"OORemitBankX1\","
				+ "FA2.\"BankItem\" AS \"OORemitBankX2\"," + "FA.\"RemitAcct\" AS \"OORemitAcct\", "
				+ "FA.\"DataSendSection\" AS \"OODataSendSection\", " + "FA3.\"FinItem\" AS \"OODataSendSectionX1\", "
				+ "FA4.\"BankItem\" AS \"OODataSendSectionX2\" " + "FROM \"NegFinAcct\" FA "
				+ "LEFT JOIN \"NegFinAcct\" FA1 " + "ON FA1.\"FinCode\"=FA.\"RemitBank\" " + "LEFT JOIN \"CdBank\" FA2 "
				+ "ON FA2.\"BranchCode\"= NVL(SUBSTR(FA.\"RemitBank\",4,4 ),'    ') " + "AND FA2.\"BankCode\" = SUBSTR(FA.\"RemitBank\",1,3 ) "
				+ "LEFT JOIN \"NegFinAcct\" FA3 " + "ON FA3.\"FinCode\" = FA.\"DataSendSection\" "
				+ "LEFT JOIN \"CdBank\" FA4 " + "ON FA4.\"BranchCode\" = NVL(SUBSTR(FA.\"DataSendSection\",4,4),'    ') "
				+ "AND FA4.\"BankCode\" = SUBSTR(FA.\"DataSendSection\",1,3) " + "WHERE 1=1 ";
		if (FinCode != null && FinCode.length() != 0) {
			SqlNegFinAcct += "AND FA.\"FinCode\"= :FinCode ";
		}
		String Order = "ORDER BY FA.\"FinCode\" ASC ";
		SqlNegFinAcct = SqlNegFinAcct + Order + sqlRow;
		return SqlNegFinAcct;
	}

	public List<String[]> FindL5974(int index, int limit, String sql, String FinCode, TitaVo titaVo) throws LogicException {
		this.info("FindData");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		this.info("L5974ServiceImpl sql=[" + sql + "]");

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		this.info("L5974ServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		if (FinCode != null && FinCode.length() != 0) {
			this.info("L5974ServiceImpl FinCode=[" + FinCode + "]");
			query.setParameter("FinCode", FinCode);
		}

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return FindData(this.convertToMap(query));
	}

	public List<String[]> FindData(List<Map<String, String>> lObject) throws LogicException {
		List<String[]> data = new ArrayList<String[]>();
		if (lObject != null && lObject.size() != 0) {
			int col = lObject.get(0).keySet().size();
			for (Map<String, String> MapObj : lObject) {

				String row[] = new String[col];
				for (int i = 0; i < col; i++) {
					row[i] = MapObj.get("F" + i);
				}
				data.add(row);
			}
		}
		return data;
	}
}