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

@Service("l5500ServiceImpl")
@Repository
public class L5500ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

	public List<Map<String, String>> findData(int index, int limit, String sql, Map<String, String> queryKey, TitaVo titaVo) throws LogicException {
		this.info("FindData 1");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		this.info("L5500ServiceImpl sql=[" + sql + "]");

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

//		this.info("JcicServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");
//		query.setParameter("ThisIndex", index);
//		query.setParameter("ThisLimit", limit);

		if (queryKey != null && queryKey.size() != 0) {
			for (String key : queryKey.keySet()) {
				this.info("L5500Service Find Key=[" + key + "],keyValue=[" + queryKey.get(key) + "]");
				query.setParameter(key, queryKey.get(key));
			}
		}

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
//		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
//		query.setMaxResults(this.limit);

		return this.convertToMap(query);

	}

	public List<String[]> FindData(int index, int limit, String sql, Map<String, String> queryKey, TitaVo titaVo) throws LogicException {
		this.info("FindData 2");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		this.info("L5500ServiceImpl sql=[" + sql + "]");

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		this.info("L5500ServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");
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

		return FindData(this.convertToMap(query));
	}

	public List<String[]> FindData(List<Map<String, String>> lObject) throws LogicException {
		List<String[]> data = new ArrayList<String[]>();
		if (lObject != null && lObject.size() != 0) {
			int col = lObject.get(0).keySet().size();
			for (Map<String, String> MapObj : lObject) {
				String row[] = new String[col];
				for (int i = 0; i < col; i++) {
					row[i] = MapObj.get("F" + String.valueOf(i));
					if (row[i] != null && row[i].length() != 0)
						;
					else
						row[i] = "";
				}
				data.add(row);
			}
		}
		return data;
	}

}