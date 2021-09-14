package com.st1.itx.db.service.springjpa.cm;

import java.time.LocalDate;
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

@Service
@Repository
public class LD009ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("ld009.findAll ");

		LocalDate today = LocalDate.of(titaVo.getEntDyI() / 10000 + 1911, titaVo.getEntDyI() / 100 % 100, titaVo.getEntDyI() % 100);
		LocalDate yesterday = today.minusDays(1);

		this.info(today.getYear() + "/" + today.getMonthValue() + "/" + today.getDayOfMonth());
		this.info(yesterday.getYear() + "/" + yesterday.getMonthValue() + "/" + yesterday.getDayOfMonth());

		String sql = " ";
		sql += " WITH TOTAL AS ( ";
		sql += " "; // 前日件數及金額
		sql += " SELECT DL.\"AcctCode\"              AS \"AcctCode\"           "; // 業務科目
		sql += "      , AR.\"AcSubBookCode\" AS \"AcSubBookCode\"         "; // 帳冊別
		sql += "      , SUM(1)                     AS \"LastDayCount\"       "; // 前日件數
		sql += "      , SUM(0)                     AS \"TodayAddCount\"      "; // 今日增加件數
		sql += "      , SUM(0)                     AS \"TodayDecCount\"      "; // 今日減少件數
		sql += "      , SUM(0)                     AS \"TodayRenewInCount\"  "; // 今日展入件數
		sql += "      , SUM(0)                     AS \"TodayRenewOutCount\" "; // 今日展出件數
		sql += "      , SUM(0)                     AS \"TodayCount\"         "; // 今日件數
		sql += "      , SUM(DL.\"LoanBalance\")      AS \"LastDayBal\"         "; // 前日金額
		sql += "      , SUM(0)                     AS \"TodayAddBal\"        "; // 今日增加金額
		sql += "      , SUM(0)                     AS \"TodayDecBal\"        "; // 今日減少金額
		sql += "      , SUM(0)                     AS \"TodayRenewInBal\"    "; // 今日展入金額
		sql += "      , SUM(0)                     AS \"TodayRenewOutBal\"   "; // 今日展出金額
		sql += "      , SUM(0)                     AS \"TodayBal\"           "; // 今日金額
		sql += " FROM \"DailyLoanBal\" DL ";
		sql += " LEFT JOIN \"AcReceivable\" AR ON AR.\"CustNo\" = DL.\"CustNo\" ";
		sql += "                              AND AR.\"FacmNo\" = DL.\"FacmNo\" ";
		sql += "                              AND AR.\"RvNo\"   = LPAD(DL.\"BormNo\", 3, '0') ";
		sql += "                              AND AR.\"AcctCode\" = DL.\"AcctCode\" ";
		sql += " WHERE DL.\"DataDate\" = :lastDay "; // 每日餘額檔的資料日期為前日
		sql += "   AND DL.\"LoanBalance\" > 0 ";
		sql += " GROUP BY DL.\"AcctCode\" ";
		sql += "        , AR.\"AcSubBookCode\" ";
		sql += " UNION ALL ";
		sql += " "; // 今日增加件數及金額
		sql += " SELECT FAC.\"AcctCode\"             AS \"AcctCode\"           "; // 業務科目
		sql += "      , AR.\"AcSubBookCode\" AS \"AcSubBookCode\"         "; // 帳冊別
		sql += "      , SUM(0)                     AS \"LastDayCount\"       "; // 前日件數
		sql += "      , SUM(1)                     AS \"TodayAddCount\"      "; // 今日增加件數
		sql += "      , SUM(0)                     AS \"TodayDecCount\"      "; // 今日減少件數
		sql += "      , SUM(0)                     AS \"TodayRenewInCount\"  "; // 今日展入件數
		sql += "      , SUM(0)                     AS \"TodayRenewOutCount\" "; // 今日展出件數
		sql += "      , SUM(0)                     AS \"TodayCount\"         "; // 今日件數
		sql += "      , SUM(0)                     AS \"LastDayBal\"         "; // 前日金額
		sql += "      , SUM(LBM.\"LoanBal\")         AS \"TodayAddBal\"        "; // 今日增加金額
		sql += "      , SUM(0)                     AS \"TodayDecBal\"        "; // 今日減少金額
		sql += "      , SUM(0)                     AS \"TodayRenewInBal\"    "; // 今日展入金額
		sql += "      , SUM(0)                     AS \"TodayRenewOutBal\"   "; // 今日展出金額
		sql += "      , SUM(0)                     AS \"TodayBal\"           "; // 今日金額
		sql += " FROM \"LoanBorMain\" LBM ";
		sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                        AND FAC.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += " LEFT JOIN \"AcReceivable\" AR ON AR.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                              AND AR.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += "                              AND AR.\"RvNo\"   = LPAD(LBM.\"BormNo\", 3, '0') ";
		sql += "                              AND AR.\"AcctCode\" = FAC.\"AcctCode\" ";
		sql += " WHERE LBM.\"DrawdownDate\" = :today "; // 放款主檔的撥款日為今日
		sql += "   AND LBM.\"LoanBal\" > 0 "; // ??? 待確認:當日撥款當日結清的要算一筆嗎?
		sql += "   AND LBM.\"RenewFlag\" = 'N' "; // 排除借新還舊案
		sql += " GROUP BY FAC.\"AcctCode\" ";
		sql += "        , AR.\"AcSubBookCode\" ";
		sql += " UNION ALL ";
		sql += " "; // 今日減少件數及金額
		sql += " SELECT TX.\"AcctCode\"              AS \"AcctCode\"           "; // 業務科目
		sql += "      , AR.\"AcSubBookCode\" AS \"AcSubBookCode\"         "; // 帳冊別
		sql += "      , SUM(0)                     AS \"LastDayCount\"       "; // 前日件數
		sql += "      , SUM(0)                     AS \"TodayAddCount\"      "; // 今日增加件數
		sql += "      , SUM(1)                     AS \"TodayDecCount\"      "; // 今日減少件數
		sql += "      , SUM(0)                     AS \"TodayRenewInCount\"  "; // 今日展入件數
		sql += "      , SUM(0)                     AS \"TodayRenewOutCount\" "; // 今日展出件數
		sql += "      , SUM(0)                     AS \"TodayCount\"         "; // 今日件數
		sql += "      , SUM(0)                     AS \"LastDayBal\"         "; // 前日金額
		sql += "      , SUM(0)                     AS \"TodayAddBal\"        "; // 今日增加金額
		sql += "      , SUM(TX.\"Principal\")        AS \"TodayDecBal\"        "; // 今日減少金額
		sql += "      , SUM(0)                     AS \"TodayRenewInBal\"    "; // 今日展入金額
		sql += "      , SUM(0)                     AS \"TodayRenewOutBal\"   "; // 今日展出金額
		sql += "      , SUM(0)                     AS \"TodayBal\"           "; // 今日金額
		sql += " FROM (SELECT LTX.\"CustNo\" ";
		sql += "            , LTX.\"FacmNo\" ";
		sql += "            , LTX.\"BormNo\" ";
		sql += "            , FAC.\"AcctCode\" ";
		sql += "            , SUM(\"Principal\" + \"ExtraRepay\") AS \"Principal\" "; // 實收本金
		sql += "       FROM \"LoanBorTx\" LTX ";
		sql += "       LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = LTX.\"CustNo\" ";
		sql += "                              AND FAC.\"FacmNo\" = LTX.\"FacmNo\" ";
		sql += "       WHERE LTX.\"AcDate\" = :today "; // 放款交易內容檔的會計日期為今日
		sql += "         AND LTX.\"TitaHCode\" = 0 ";
		sql += "         AND \"Principal\" + \"ExtraRepay\" > 0";
		sql += "       GROUP BY LTX.\"CustNo\" ";
		sql += "              , LTX.\"FacmNo\" ";
		sql += "              , LTX.\"BormNo\" ";
		sql += "              , FAC.\"AcctCode\" ";
		sql += "      ) TX ";
		sql += " LEFT JOIN \"AcReceivable\" AR ON AR.\"CustNo\" = TX.\"CustNo\" ";
		sql += "                              AND AR.\"FacmNo\" = TX.\"FacmNo\" ";
		sql += "                              AND AR.\"RvNo\"   = LPAD(TX.\"BormNo\", 3, '0')";
		sql += "                              AND AR.\"AcctCode\" = TX.\"AcctCode\" ";
		sql += " GROUP BY TX.\"AcctCode\" ";
		sql += "        , AR.\"AcSubBookCode\" ";
		sql += " UNION ALL ";
		sql += " "; // 今日展入件數及金額
		sql += " SELECT FAC.\"AcctCode\"             AS \"AcctCode\"           "; // 業務科目
		sql += "      , AR.\"AcSubBookCode\" AS \"AcSubBookCode\"         "; // 帳冊別
		sql += "      , SUM(0)                     AS \"LastDayCount\"       "; // 前日件數
		sql += "      , SUM(0)                     AS \"TodayAddCount\"      "; // 今日增加件數
		sql += "      , SUM(0)                     AS \"TodayDecCount\"      "; // 今日減少件數
		sql += "      , SUM(1)                     AS \"TodayRenewInCount\"  "; // 今日展入件數
		sql += "      , SUM(0)                     AS \"TodayRenewOutCount\" "; // 今日展出件數
		sql += "      , SUM(0)                     AS \"TodayCount\"         "; // 今日件數
		sql += "      , SUM(0)                     AS \"LastDayBal\"         "; // 前日金額
		sql += "      , SUM(0)                     AS \"TodayAddBal\"        "; // 今日增加金額
		sql += "      , SUM(0)                     AS \"TodayDecBal\"        "; // 今日減少金額
		sql += "      , SUM(LBM.\"DrawdownAmt\")     AS \"TodayRenewInBal\"    "; // 今日展入金額
		sql += "      , SUM(0)                     AS \"TodayRenewOutBal\"   "; // 今日展出金額
		sql += "      , SUM(0)                     AS \"TodayBal\"           "; // 今日金額
		sql += " FROM \"LoanBorMain\" LBM ";
		sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                        AND FAC.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += " LEFT JOIN \"AcReceivable\" AR ON AR.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                              AND AR.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += "                              AND AR.\"RvNo\"   = LPAD(LBM.\"BormNo\", 3, '0') ";
		sql += "                              AND AR.\"AcctCode\" = FAC.\"AcctCode\" ";
		sql += " WHERE LBM.\"DrawdownDate\" = :today "; // 放款主檔的撥款日期為今日
		sql += "   AND LBM.\"RenewFlag\" = 'Y' "; // 放款主檔的借新還舊記號為Y
		sql += " GROUP BY FAC.\"AcctCode\" ";
		sql += "        , AR.\"AcSubBookCode\" ";
		sql += " UNION ALL ";
		sql += " "; // 今日展出件數及金額
		sql += " SELECT TX.\"AcctCode\"              AS \"AcctCode\"           "; // 業務科目
		sql += "      , AR.\"AcSubBookCode\" AS \"AcSubBookCode\"         "; // 帳冊別
		sql += "      , SUM(0)                     AS \"LastDayCount\"       "; // 前日件數
		sql += "      , SUM(0)                     AS \"TodayAddCount\"      "; // 今日增加件數
		sql += "      , SUM(0)                     AS \"TodayDecCount\"      "; // 今日減少件數
		sql += "      , SUM(0)                     AS \"TodayRenewInCount\"  "; // 今日展入件數
		sql += "      , SUM(1)                     AS \"TodayRenewOutCount\" "; // 今日展出件數
		sql += "      , SUM(0)                     AS \"TodayCount\"         "; // 今日件數
		sql += "      , SUM(0)                     AS \"LastDayBal\"         "; // 前日金額
		sql += "      , SUM(0)                     AS \"TodayAddBal\"        "; // 今日增加金額
		sql += "      , SUM(0)                     AS \"TodayDecBal\"        "; // 今日減少金額
		sql += "      , SUM(0)                     AS \"TodayRenewInBal\"    "; // 今日展入金額
		sql += "      , SUM(TX.\"Principal\")        AS \"TodayRenewOutBal\"   "; // 今日展出金額
		sql += "      , SUM(0)                     AS \"TodayBal\"           "; // 今日金額
		sql += " FROM (SELECT LTX.\"CustNo\" ";
		sql += "            , LTX.\"FacmNo\" ";
		sql += "            , LTX.\"BormNo\" ";
		sql += "            , FAC.\"AcctCode\" ";
		sql += "            , SUM(\"Principal\") AS \"Principal\" "; // 實收本金
		sql += "       FROM \"LoanBorTx\" LTX ";
		sql += "       LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = LTX.\"CustNo\" ";
		sql += "                              AND FAC.\"FacmNo\" = LTX.\"FacmNo\" ";
		sql += "       WHERE LTX.\"AcDate\" = :today "; // 放款交易內容檔的會計日期為今日
		sql += "         AND NVL(JSON_VALUE(LTX.\"OtherFields\", '$.CaseCloseCode'), ' ') IN ('1','2') ";
		sql += "             "; // 結案區分為1,2
		sql += "       GROUP BY LTX.\"CustNo\" ";
		sql += "              , LTX.\"FacmNo\" ";
		sql += "              , LTX.\"BormNo\" ";
		sql += "              , FAC.\"AcctCode\" ";
		sql += "      ) TX ";
		sql += " LEFT JOIN \"AcReceivable\" AR ON AR.\"CustNo\" = TX.\"CustNo\" ";
		sql += "                              AND AR.\"FacmNo\" = TX.\"FacmNo\" ";
		sql += "                              AND AR.\"RvNo\"   = LPAD(TX.\"BormNo\", 3, '0') ";
		sql += "                              AND AR.\"AcctCode\" = TX.\"AcctCode\" ";
		sql += " GROUP BY TX.\"AcctCode\" ";
		sql += "        , AR.\"AcSubBookCode\" ";
		sql += " UNION ALL ";
		sql += " "; // 今日件數及金額
		sql += " SELECT DL.\"AcctCode\"              AS \"AcctCode\"           "; // 業務科目
		sql += "      , AR.\"AcSubBookCode\" AS \"AcSubBookCode\"         "; // 帳冊別
		sql += "      , SUM(0)                     AS \"LastDayCount\"       "; // 前日件數
		sql += "      , SUM(0)                     AS \"TodayAddCount\"      "; // 今日增加件數
		sql += "      , SUM(0)                     AS \"TodayDecCount\"      "; // 今日減少件數
		sql += "      , SUM(0)                     AS \"TodayRenewInCount\"  "; // 今日展入件數
		sql += "      , SUM(0)                     AS \"TodayRenewOutCount\" "; // 今日展出件數
		sql += "      , SUM(1)                     AS \"TodayCount\"         "; // 今日件數
		sql += "      , SUM(0)                     AS \"LastDayBal\"         "; // 前日金額
		sql += "      , SUM(0)                     AS \"TodayAddBal\"        "; // 今日增加金額
		sql += "      , SUM(0)                     AS \"TodayDecBal\"        "; // 今日減少金額
		sql += "      , SUM(0)                     AS \"TodayRenewInBal\"    "; // 今日展入金額
		sql += "      , SUM(0)                     AS \"TodayRenewOutBal\"   "; // 今日展出金額
		sql += "      , SUM(DL.\"LoanBalance\")      AS \"TodayBal\"           "; // 今日金額
		sql += " FROM \"DailyLoanBal\" DL ";
		sql += " LEFT JOIN \"AcReceivable\" AR ON AR.\"CustNo\" = DL.\"CustNo\" ";
		sql += "                              AND AR.\"FacmNo\" = DL.\"FacmNo\" ";
		sql += "                              AND AR.\"RvNo\"   = LPAD(DL.\"BormNo\", 3, '0') ";
		sql += "                              AND AR.\"AcctCode\" = DL.\"AcctCode\" ";
		sql += " WHERE DL.\"DataDate\" = :today "; // 每日餘額檔的資料日期為今日
		sql += "   AND DL.\"LoanBalance\" > 0 ";
		sql += " GROUP BY DL.\"AcctCode\" ";
		sql += "        , AR.\"AcSubBookCode\" ";
		sql += " 	 ";
		sql += " 	) ";
		sql += " 	 ";
		sql += " SELECT t.\"AcctCode\" AS F0 ";
		sql += "       ,CdC.\"Item\" AS F1 "; // 業務科目
		sql += "       ,t.\"AcSubBookCode\" AS F2 ";
		sql += " 	   ,CdCb.\"Item\" AS F3 "; // 帳冊別
		sql += " 	   ,SUM(t.\"LastDayCount\") AS F4 "; // 前日件數
		sql += " 	   ,SUM(t.\"TodayAddCount\") AS F5 "; // 今日件數增
		sql += " 	   ,SUM(t.\"TodayDecCount\") AS F6 "; // 今日件數減
		sql += " 	   ,SUM(t.\"TodayRenewInCount\") AS F7 "; // 今日展入件數
		sql += " 	   ,SUM(t.\"TodayRenewOutCount\") AS F8 "; // 今日展出件數
		sql += " 	   ,SUM(t.\"TodayAddCount\") - SUM(t.\"TodayDecCount\") AS F9  "; // 件數淨值
		sql += " 	   ,SUM(t.\"TodayCount\") AS F10 "; // 今日總件數
		sql += " 	   ,SUM(t.\"LastDayBal\") AS F11 "; // 前日金額
		sql += " 	   ,SUM(t.\"TodayAddBal\") AS F12 "; // 今日金額增
		sql += " 	   ,SUM(t.\"TodayDecBal\") AS F13 "; // 今日金額減
		sql += " 	   ,SUM(t.\"TodayAddBal\") -  SUM(t.\"TodayDecBal\") AS F14 "; // 今日金額淨增減
		sql += " 	   ,SUM(t.\"TodayBal\") AS F15 "; // 今日總金額
		sql += " 	   ,SUM(t.\"TodayRenewInBal\") - SUM(t.\"TodayRenewOutBal\") AS F16 "; // 展期
		sql += " FROM TOTAL t ";
		sql += " LEFT JOIN \"CdCode\" CdC ON CdC.\"DefCode\" = 'AcctCode' ";
		sql += "                         AND CdC.\"Code\" = t.\"AcctCode\" ";
		sql += " LEFT JOIN \"CdCode\" CdCb ON CdCb.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                          AND CdCb.\"Code\" = t.\"AcSubBookCode\" ";
		sql += " GROUP BY t.\"AcctCode\" ";
		sql += " 		 ,CdC.\"Item\" ";
		sql += "         ,t.\"AcSubBookCode\" ";
		sql += " 		 ,CdCb.\"Item\" ";
		sql += " ORDER BY t.\"AcctCode\" ";
		sql += "         ,t.\"AcSubBookCode\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("today", today.getYear() + String.format("%02d", today.getMonthValue()) + String.format("%02d", today.getDayOfMonth()));
		query.setParameter("lastDay", yesterday.getYear() + String.format("%02d", yesterday.getMonthValue()) + String.format("%02d", yesterday.getDayOfMonth()));

		return this.convertToMap(query);
	}

}