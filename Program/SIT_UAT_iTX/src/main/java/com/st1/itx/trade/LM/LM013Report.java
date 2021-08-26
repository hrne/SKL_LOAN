package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM013ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.LM013ServiceImpl.EntCodeCondition;
import com.st1.itx.db.service.springjpa.cm.LM013ServiceImpl.IsRelsCondition;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class LM013Report extends MakeReport {

	@Autowired
	public LM013ServiceImpl lM013ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	private BigDecimal marginAmount;
	private String validDate;

	private String newBorder = "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";

	@Override
	public void printHeader() {

		printHeaderL();

	}

	private void printHeaderL() {

		print(-3, 3, "程式ID：" + this.getParentTranCode());
		print(-4, 3, "報  表：" + this.getRptCode());

		print(-1, 192, "機密等級：密");
		print(-2, 192, "日  期：" + this.showBcDate(dDateUtil.getNowStringBc(), 1));
		print(-3, 192, "時  間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		print(-4, 192, "頁  數：" + this.getNowPage());

		print(-3, newBorder.length() / 2, "新光人壽保險股份有限公司", "C");
		print(-4, newBorder.length() / 2, "金檢報表(放款種類表)", "C");

		print(-6, 1, "核貸總值分界. " + formatAmt(marginAmount, 0));
		print(-7, 1, "金檢日期..... " + validDate);

		/**
		 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * ----------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		print(-9, 0,
 				          "          放款對象                                                核貸總值                                                                                  帳面總值");
		print(-10, 0, "");
		print(-11, 0,
				          "戶號　　額度  名稱　統編　　不動產抵押       動產抵押       有價證券       銀行保證       專案放款           合計      不動產抵押       動產抵押       有價證券       銀行保證       專案放款           合計");
		print(-12, 0, newBorder);

	}

	public Boolean exec(TitaVo titaVo) throws LogicException {

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM013", "金檢報表(放款種類表)", "密", "A4", "L");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(13);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(44);

		this.setCharSpaces(0);

		this.setFont(1, 6);

		marginAmount = new BigDecimal(titaVo.getParam("inputAmount"));
		validDate = showRocDate(Integer.valueOf(titaVo.getParam("inputDate")) + 19110000, 1);

		List<Map<String, String>> LM013List_All = null;
		List<Map<String, String>> LM013List_0N = null;
		List<Map<String, String>> LM013List_0Y = null;
		List<Map<String, String>> LM013List_1N = null;
		List<Map<String, String>> LM013List_1Y = null;
		
		ArrayList<List<Map<String, String>>> listLists = new ArrayList<List<Map<String, String>>>();
		listLists.add(LM013List_All);
		listLists.add(LM013List_0N);
		listLists.add(LM013List_0Y);
		listLists.add(LM013List_1N);
		listLists.add(LM013List_1Y);

		try {
			LM013List_All = lM013ServiceImpl.findAll(titaVo, EntCodeCondition.All, IsRelsCondition.All);
			LM013List_0N = lM013ServiceImpl.findAll(titaVo, EntCodeCondition.Natural, IsRelsCondition.No);
			LM013List_0Y = lM013ServiceImpl.findAll(titaVo, EntCodeCondition.Enterprise, IsRelsCondition.Yes);
			LM013List_1N = lM013ServiceImpl.findAll(titaVo, EntCodeCondition.Natural, IsRelsCondition.No);
			LM013List_1Y = lM013ServiceImpl.findAll(titaVo, EntCodeCondition.Enterprise, IsRelsCondition.Yes);
		} catch (Exception e) {
			this.info("lM013ServiceImpl.findAll error = " + e.toString());
		}
		
		for (List<Map<String, String>> thisList : listLists)
		{
			if (thisList != null && !thisList.isEmpty())
			{
				for (Map<String, String> tLDVo : thisList) {
					
					// F0		企金別		0,1
					// F1		關係人別	Y,N
					// F2		戶號			7
					// F3		額度		3
					// F4		身分證		10
					// F5		姓名		至少10
					// F6		擔保品代號1
					// F7		核貸總值
					// F8		帳面價值
					// F9		LineTotal
					
					// 輸出 A 第一行
					// 輸出 A 第二行
					// 輸出 A 最後一行 (合計) (包含姓名)
					// 輸出 B
					
					// 輸出 x座標
					
					// 戶號			1, L
					// 額度			9, L
					// 姓名			9, L
					// 身分證       21, L
					
					// (核貸總值類)
					// 不動產抵押   38, R
					// 動產抵押     53, R
					// 有價證券     68, R
					// 銀行保證     83, R
					// 專案放款     98, R
					// 合計         113, R
					
					// (帳面價值類)
					// 不動產抵押   129, R
					// 動產抵押		144, R
					// 有價證券		159, R
					// 銀行保證		184, R
					// 專案放款		199, R
					// 合計			204, R
				}
			}
		}

	long sno = this.close();this.toPdf(sno);

	return true;
	}

	private String PadStart(int size, String intfor) {
		for (int i = 0; i < size; i++) {
			if (intfor.length() < size) {
				intfor = "0" + intfor;
			}
		}
		return intfor;
	}
}
