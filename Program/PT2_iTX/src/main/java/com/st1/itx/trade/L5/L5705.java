package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.parse.Parse;

@Service("L5705")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5705 extends TradeBuffer {
	@Autowired
	public MakeFile makeFile;
	@Autowired
	public Parse parse;

	private List<String> lCustId = new ArrayList<String>();

	String OccName[] = { "OOCustId" };

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5705 ");
		this.totaVo.init(titaVo);
		this.info("L5705 titaVo=[" + titaVo + "]");

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		int submitCnt = parse.stringToInteger(titaVo.getParam("loopcnt"));

		for (int i = 1; i <= submitCnt; i++) {
			String custId = titaVo.getParam("CustId" + i);
			if (!custId.isEmpty()) {
				lCustId.add(custId);
			}
		}
		if (lCustId.size() == 0) {
			throw new LogicException("E0019", "未輸入統編");// 輸入資料錯誤
		}
		long TxtSnoF = 0L;
		// 製檔
		String strToday = titaVo.getEntDy(); // titaVo.getCalDy() 改為會計日2022/11/14
		int Today = 0;
		if (String.valueOf(Integer.parseInt(strToday)).length() != 8) {
			Today = Integer.parseInt(strToday) + 19110000;
		}
		TxtSnoF = makeTxt(String.valueOf(Today), titaVo);

		this.info("L5705 TxtSnoF=" + TxtSnoF);

		totaVo.put("TxtSnoF", "" + TxtSnoF);
		this.addList(this.totaVo);

		return this.sendList();
	}

	public long makeTxt(String DcToday, TitaVo titaVo) throws LogicException {

		long sno = 0L;
		String yyyy = DcToday.substring(0, 4);
		String yyy = String.valueOf(Integer.parseInt(yyyy) - 1911);
		String mm = DcToday.substring(4, 6);
		String dd = DcToday.substring(6, 8);
		String RocToday = yyy + mm + dd;
		String fileitem = "債權比例分攤(產出)";

//		String filename = "458"+mm+dd+String.valueOf(ThisSeq)+".sta";   輸出時批號固定為1
		String filename = "458" + mm + dd + "1" + ".sta";
		// 458+會計日(月份)+會計日(日期)+批號.sta
		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), fileitem, filename, 2);
		// JCIC-INQ-BARE-V01-4580001(24)+民國年月日(7)+批號(2)
//		String Head="JCIC-INQ-BARE-V01-4580001"+RocToday+strThisSeq;   輸出時批號固定為1
		String Head = "JCIC-INQ-BARE-V01-4580001" + RocToday + "01";
		makeFile.put(Head);
		for (String CustId : lCustId) {
			// FB1(3)+身份證字號(10)+空白(20)+Z98(3)
			String Detail = "FB1" + CustId + "                    Z98";
			makeFile.put(Detail);
		}
		String hhmm = titaVo.getCalTm().substring(0, 4);
		// TRLR0000002(10)+民國年月日(7)+hhmm(4)
		String End = "TRLR0000002" + RocToday + hhmm;
		makeFile.put(End);
		sno = makeFile.close();
		// makeFile.toFile(sno);//及時輸出的才要加這行
		return sno;
	}
}