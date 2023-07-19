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

	public List<Map<String, String>> findAcDate(int iAccDateStart, TitaVo titaVo) throws LogicException {

		this.info("L7074.findAll");

		String sql = " ";
		sql += "select ";
		sql += "    \"GroupId\"       as  \"GroupId\"       "; // ETL批號
		sql += "   ,\"TotalLines\"    as  \"TotalLines\"    "; // 明細總行數
		sql += "   ,\"CurrencyCode\"  as  \"CurrencyCode\"  "; // 幣別 借方總金額
		sql += "   ,\"TotalAmount\"   as  \"TotalAmount\"   "; // 借方總金額
		sql += "   ,\"SendStatus\"    as  \"SendStatus\"    ";
		sql += "   ,CASE WHEN \"SendStatus\" = 0 THEN '上傳成功'      ";
		sql += "         WHEN \"SendStatus\" = 2 THEN '上傳失敗'      ";
		sql += "         ELSE                       '未上傳'             ";
		sql += "     END  AS \"SendStatusX\" "; // 1.未上傳/0.上傳成功/2.上傳失敗
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

	public List<Map<String, String>> findAcDate2(int iAccDateStart, String iGroupId, int iSendStatus, TitaVo titaVo)
			throws LogicException {

		this.info("L79742.findAll2");
		int ixAccDateStart = parse.stringToInteger(titaVo.getParam("AccDateStart")) + 19110000;
		String ixGroupId = titaVo.getParam("GroupId");
		int ixSendStatus = parse.stringToInteger(titaVo.getParam("SendStatus"));

		this.info("ixAccDateStart   = " + ixAccDateStart);
		this.info("ixGroupId        = " + ixGroupId);
		this.info("ixSendStatus     = " + ixSendStatus);

		String sql = " ";
		sql += "select ";
		sql += "    \"AcBookCode\"       as \"AcBookCode\"      "; // 帳冊別
		sql += "   ,\"MediaSlipNo\"      as \"MediaSlipNo\"     "; // 傳票號碼
		sql += "   ,\"Seq\"              as \"Seq\"             "; // 傳票明細序號
		sql += "   ,\"AcDate\"           as \"AcDate\"          "; // 傳票日期
		sql += "   ,\"BatchNo\"          as \"BatchNo\"         "; // 傳票批號
		sql += "   ,\"MediaSeq\"         as \"MediaSeq\"        "; // 上傳核心序號
		sql += "   ,\"AcNoCode\"         as \"AcNoCode\"        "; // 科目代號
		sql += "   ,\"AcSubCode\"        as \"AcSubCode\"       "; // 子目代號
		sql += "   ,\"DeptCode\"         as \"DeptCode\"        "; // 部門代號
		sql += "   ,\"DbCr\"             as \"DbCr\"            "; // 借貸別
		sql += "   ,\"TxAmt\"            as \"TxAmt\"           "; // 金額
		sql += "   ,\"SlipRmk\"          as \"SlipRmk\"         "; // 傳票摘要
		sql += "   ,\"ReceiveCode\"      as \"ReceiveCode\"     "; // 會計科目銷帳碼
		sql += "   ,\"AcSubBookCode\"    as \"AcSubBookCode\"   "; // 區隔帳冊
		sql += "   ,\"CostMonth\"        as \"CostMonth\"       "; // 成本月份
		sql += "   ,\"Ifrs17Group\"      as \"Ifrs17Group\"     "; // IFRS17群組
		sql += "   ,\"LatestFlag\"       as \"LatestFlag\"      "; // 是否為最新
		sql += "   ,\"TransferFlag\"     as \"TransferFlag\"    "; // 是否已傳輸
		sql += "   ,\"CreateDate\"       as \"CreateDate\"      "; // 建檔日期時間
		sql += "   ,\"CreateEmpNo\"      as \"CreateEmpNo\"     "; // 建檔人員
		sql += "   ,\"LastUpdate\"       as \"LastUpdate\"      "; // 最後更新日期時間
		sql += "   ,\"LastUpdateEmpNo\"  as \"LastUpdateEmpNo\" "; // 最後更新人員
		sql += "   ,\"ErrorCode\"        as \"ErrorCode\"       "; // 回應錯誤代碼
		sql += "   ,\"ErrorMsg\"         as \"ErrorMsg\"        "; // 回應錯誤訊息
		sql += "   ,CASE WHEN \"SendStatus\" = 0 THEN '上傳成功'      ";
		sql += "         WHEN \"SendStatus\" = 2 THEN '上傳失敗'      ";
		sql += "         ELSE                       '未上傳'             ";
		sql += "     END  AS \"SendStatusX\" "; // 1.未上傳/0.上傳成功/2.上傳失敗
		sql += "   ,\"GroupId\"          as \"GroupId\"         ";
		sql += "from (     ";
		sql += "   select  ";
		sql += "    \"AcBookCode\"       as \"AcBookCode\"      "; // 帳冊別
		sql += "   ,\"MediaSlipNo\"      as \"MediaSlipNo\"     "; // 傳票號碼
		sql += "   ,\"Seq\"              as \"Seq\"             "; // 傳票明細序號
		sql += "   ,\"AcDate\"           as \"AcDate\"          "; // 傳票日期
		sql += "   ,\"BatchNo\"          as \"BatchNo\"         "; // 傳票批號
		sql += "   ,\"MediaSeq\"         as \"MediaSeq\"        "; // 上傳核心序號
		sql += "   ,\"AcNoCode\"         as \"AcNoCode\"        "; // 科目代號
		sql += "   ,\"AcSubCode\"        as \"AcSubCode\"       "; // 子目代號
		sql += "   ,\"DeptCode\"         as \"DeptCode\"        "; // 部門代號
		sql += "   ,\"DbCr\"             as \"DbCr\"            "; // 借貸別
		sql += "   ,\"TxAmt\"            as \"TxAmt\"           "; // 金額
		sql += "   ,\"SlipRmk\"          as \"SlipRmk\"         "; // 傳票摘要
		sql += "   ,\"ReceiveCode\"      as \"ReceiveCode\"     "; // 會計科目銷帳碼
		sql += "   ,\"AcSubBookCode\"    as \"AcSubBookCode\"   "; // 區隔帳冊
		sql += "   ,\"CostMonth\"        as \"CostMonth\"       "; // 成本月份
		sql += "   ,\"Ifrs17Group\"      as \"Ifrs17Group\"     "; // IFRS17群組
		sql += "   ,\"LatestFlag\"       as \"LatestFlag\"      "; // 是否為最新
		sql += "   ,\"TransferFlag\"     as \"TransferFlag\"    "; // 是否已傳輸
		sql += "   ,\"CreateDate\"       as \"CreateDate\"      "; // 建檔日期時間
		sql += "   ,\"CreateEmpNo\"      as \"CreateEmpNo\"     "; // 建檔人員
		sql += "   ,\"LastUpdate\"       as \"LastUpdate\"      "; // 最後更新日期時間
		sql += "   ,\"LastUpdateEmpNo\"  as \"LastUpdateEmpNo\" "; // 最後更新人員
		sql += "   ,\"ErrorCode\"        as \"ErrorCode\"       "; // 回應錯誤代碼
		sql += "   ,\"ErrorMsg\"         as \"ErrorMsg\"        "; // 回應錯誤訊息
		sql += "   , CONCAT(LPAD(\"AcDate\", 8,'0'),LPAD(\"MediaSeq\", 3,'0')) AS \"GroupId\"  ";
		sql += "   , SUM(CASE WHEN \"DbCr\" = 'D' THEN \"TxAmt\" ELSE 0 END) AS \"TotalAmount\" ";
		sql += "   , MAX(CASE WHEN \"TransferFlag\" = 'Y' THEN 0 ";
		sql += "              WHEN NVL(\"ErrorCode\",' ') = ' ' THEN 2 ";
		sql += "              ELSE 1 ";
		sql += "         END) AS \"SendStatus\" ";
		sql += "    from \"SlipMedia2022\" ";
		sql += "    WHERE 1 = 1 ";
		if (ixAccDateStart >= 19110000) {
			sql += "      AND \"AcDate\" =:iAccDateStart ";
		}

		sql += " GROUP BY  " + 
				"    \"AcDate\", \"MediaSeq\", \"CurrencyCode\", \"AcBookCode\", \"MediaSlipNo\", " + 
				"\"Seq\", \"BatchNo\", \"AcNoCode\", \"AcSubCode\", \"DeptCode\", " + 
				"\"DbCr\", \"TxAmt\", \"SlipRmk\", \"ReceiveCode\", \"CostMonth\", " + 
				"\"Ifrs17Group\", \"LatestFlag\", \"TransferFlag\", \"CreateDate\", \"CreateEmpNo\", " + 
				"\"LastUpdate\", \"LastUpdateEmpNo\", \"ErrorCode\", \"ErrorMsg\", \"AcSubBookCode\", " + 
				"concat(lpad(\"AcDate\", 8, '0'), lpad(\"MediaSeq\", 3, '0'))     )";
		sql += " WHERE 1 = 1 " ;
		if (!ixGroupId.equals("")) {
			sql += "      AND \"GroupId\" =:iGroupId ";
		}
		if (ixSendStatus != 9) {
			sql += "      AND \"SendStatus\" =:iSendStatus ";
		}
		sql += "  GROUP BY  " + 
				"    \"AcDate\", \"MediaSeq\", \"AcBookCode\", \"MediaSlipNo\", \"Seq\", " + 
				"\"BatchNo\", \"AcNoCode\", \"AcSubCode\", \"DeptCode\", \"DbCr\", " + 
				"\"TxAmt\", \"SlipRmk\", \"ReceiveCode\", \"CostMonth\", \"Ifrs17Group\", " + 
				"\"LatestFlag\", \"TransferFlag\", \"CreateDate\", \"CreateEmpNo\", \"LastUpdate\", " + 
				"\"LastUpdateEmpNo\", \"ErrorCode\", \"ErrorMsg\", \"SendStatus\", \"AcSubBookCode\", " + 
				"CASE WHEN \"SendStatus\" = 0 THEN '上傳成功' WHEN \"SendStatus\" = 2 THEN '上傳失敗' ELSE '未上傳' END, \"GroupId\"  ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		if (ixAccDateStart >= 19110000) {
			query.setParameter("iAccDateStart", ixAccDateStart);
		}
		if (!ixGroupId.equals("")) {
			query.setParameter("iGroupId", ixGroupId);
		}
		if (ixSendStatus != 9) {
			query.setParameter("iSendStatus", ixSendStatus);
		}
		
		return this.convertToMap(query);

	}
}