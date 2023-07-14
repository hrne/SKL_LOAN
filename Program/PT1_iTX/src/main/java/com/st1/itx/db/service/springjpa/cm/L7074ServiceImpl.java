package com.st1.itx.db.service.springjpa.cm;

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
import com.st1.itx.util.parse.Parse;

@Service("L7074ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L7074ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAcDate(int iAccDateStart ,TitaVo titaVo) throws LogicException {

		this.info("L7074.findAll");
		
		String sql = " ";
		sql += "select ";
		sql += "    \"GroupId\"       as  \"GroupId\"       "; //ETL批號
		sql += "   ,\"TotalLines\"    as  \"TotalLines\"    "; //明細總行數
		sql += "   ,\"CurrencyCode\"  as  \"CurrencyCode\"  "; //幣別 借方總金額 
		sql += "   ,\"TotalAmount\"   as  \"TotalAmount\"   "; //借方總金額 
		sql += "   ,\"SendStatus\"    as  \"SendStatus\"    ";
		sql += "   ,CASE WHEN \"SendStatus\" = 0 THEN '上傳成功'      ";
		sql += "         WHEN \"SendStatus\" = 2 THEN '上傳失敗'      ";
		sql += "         ELSE                       '未上傳'             ";
		sql += "     END  AS \"SendStatusX\" "; //1.未上傳/0.上傳成功/2.上傳失敗
		sql += "from (     ";
		sql += "   select  ";
		sql += "     CONCAT(LPAD(\"AcDate\", 8,'0'),LPAD(\"MediaSeq\", 3,'0')) AS \"GroupId\"  ";
		sql += "   , SUM(1) AS  \"TotalLines\"   ";
		sql += "   , \"CurrencyCode\"  ";
		sql += "   , SUM(CASE WHEN \"DbCr\" = 'D' THEN \"TxAmt\" ELSE 0 END) AS \"TotalAmount\" ";
		sql += "   , MAX(CASE WHEN \"TransferFlag\" = 'Y' THEN 0 ";
		sql += "              WHEN NVL(\"ErrorCode\",' ') = ' ' THEN 2 ";
		sql += "              ELSE 1 ";
		sql += "         END) AS \"SendStatus\" ";
		sql += "    from \"SlipMedia2022\" ";
		sql += "    WHERE \"AcDate\" =:iAccDateStart ";
		sql += "    GROUP by   \"AcDate\", \"MediaSeq\" ,\"CurrencyCode\" ";
		sql += "    )";
		sql += "order by \"GroupId\"     ";
		
		
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("iAccDateStart", iAccDateStart);
		return this.convertToMap(query);
	}

}