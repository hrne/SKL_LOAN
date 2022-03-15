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
import com.st1.itx.util.common.JcicCom;

@Service("jcicServiceImpl")
@Repository
public class JcicServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	public JcicCom jcicCom;

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
	// 注意異動此邊欄位,或SQL語法 請檢查Jcic

	public String sqlJcic(TitaVo titaVo, String chainCd) {
		// ChainCd 交易編號
		this.info("JcicService sqlJcic chainCd=[" + chainCd + "]");
		String sql = "";
		String order = "";
		if (("L8R10").equals(chainCd)) {
			String sqlL8R10 = SqlJcicZ040() + "AND Jcic.\"CustId\"=:CustId " + "AND Jcic.\"RcDate\"=:RcDate " + "AND Jcic.\"SubmitKey\"=:SubmitKey ";
			sql = sqlL8R10;
		}
		sql = sql + order + sqlRow;
		return sql;
	}

	private String SqlJcicZ040() {
		String sqlJcicZ040 = "SELECT  " + "Jcic.\"TranKey\", " + "Jcic.\"CustId\", " + "Jcic.\"RcDate\", " + "Jcic.\"SubmitKey\", " + "Jcic.\"RbDate\", " + "Jcic.\"ApplyType\", "
				+ "Jcic.\"RefBankId\", " + "Jcic.\"NotBankId1\", " + "Jcic.\"NotBankId2\", " + "Jcic.\"NotBankId3\", " + "Jcic.\"NotBankId4\", " + "Jcic.\"NotBankId5\", " + "Jcic.\"NotBankId6\", "
				+ "Jcic.\"OutJcicTxtDate\", " + "Cd0.\"Item\" AS \"SubmitKeyX\", " + "Cd7.\"Item\" AS \"RefBankIdX\", " + "Cd1.\"Item\" AS \"NotBankId1X\", " + "Cd2.\"Item\" AS \"NotBankId2X\", "
				+ "Cd3.\"Item\" AS \"NotBankId3X\", " + "Cd4.\"Item\" AS \"NotBankId4X\", " + "Cd5.\"Item\" AS \"NotBankId5X\", " + "Cd6.\"Item\" AS \"NotBankId6X\" " + "FROM \"JcicZ040\" Jcic "
				+ "LEFT JOIN \"CdCode\" Cd0 ON Cd0.\"DefCode\"='JcicBankCode' AND Cd0.\"Code\"=Jcic.\"SubmitKey\" "
				+ "LEFT JOIN \"CdCode\" Cd1 ON Cd1.\"DefCode\"='JcicBankCode' AND Cd1.\"Code\"=Jcic.\"NotBankId1\" "
				+ "LEFT JOIN \"CdCode\" Cd2 ON Cd2.\"DefCode\"='JcicBankCode' AND Cd2.\"Code\"=Jcic.\"NotBankId2\" "
				+ "LEFT JOIN \"CdCode\" Cd3 ON Cd3.\"DefCode\"='JcicBankCode' AND Cd3.\"Code\"=Jcic.\"NotBankId3\" "
				+ "LEFT JOIN \"CdCode\" Cd4 ON Cd4.\"DefCode\"='JcicBankCode' AND Cd4.\"Code\"=Jcic.\"NotBankId4\" "
				+ "LEFT JOIN \"CdCode\" Cd5 ON Cd5.\"DefCode\"='JcicBankCode' AND Cd5.\"Code\"=Jcic.\"NotBankId5\" "
				+ "LEFT JOIN \"CdCode\" Cd6 ON Cd6.\"DefCode\"='JcicBankCode' AND Cd6.\"Code\"=Jcic.\"NotBankId6\" "
				+ "LEFT JOIN \"CdCode\" Cd7 ON Cd7.\"DefCode\"='JcicBankCode' AND Cd7.\"Code\"=Jcic.\"RefBankId\" " + "WHERE 1=1 ";
		return sqlJcicZ040;
	}

	public List<String[]> FindJcic(int index, int limit, String sql, Map<String, String> queryKey, TitaVo titaVo) throws LogicException {
		this.info("FindData");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		this.info("JcicServiceImpl sql=[" + sql + "]");

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		this.info("JcicServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		if (queryKey != null && queryKey.size() != 0) {
			for (String key : queryKey.keySet()) {
				this.info("JcicService FindJcic Key=[" + key + "],keyValue=[" + queryKey.get(key) + "]");
				query.setParameter(key, queryKey.get(key));
			}
		}

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		@SuppressWarnings("unchecked")
		List<Object> lObject = this.convertToMap(query.getResultList());

		return FindData(lObject);
	}

	@SuppressWarnings("unchecked")
	public List<String[]> FindData(List<Object> lObject) throws LogicException {
		List<String[]> data = new ArrayList<String[]>();
		if (lObject != null && lObject.size() != 0) {
			int col = ((Map<String, String>) lObject.get(0)).keySet().size();
			for (Object obj : lObject) {
				Map<String, String> MapObj = (Map<String, String>) obj;
				String row[] = new String[col];
				for (int i = 0; i < col; i++) {
					row[i] = MapObj.get("F" + String.valueOf(i));
					if (row[i] != null && row[i].length() != 0) {

					} else {
						row[i] = "";
					}
				}
				data.add(row);
			}
		}
		return data;
	}
}