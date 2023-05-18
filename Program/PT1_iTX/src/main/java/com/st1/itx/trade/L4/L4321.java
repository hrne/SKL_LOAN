package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L4321ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.parse.Parse;

@Service("L4321")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4321 extends TradeBuffer {

	@Autowired
	public Parse parse;
	@Autowired
	public L4321Report l4321Report;
	@Autowired
	public L4321ServiceImpl L4321ServiceImpl;

	private int iTxKind = 0;
	private int iCustType = 0;
	private int iAdjCode = 0;
	private String fileNm = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4321 ");
		this.totaVo.init(titaVo);
		this.info("titaVo.getTxcd()=" + titaVo.getTxcd() + ", titaVo.getTxCode()=" + titaVo.getTxCode());
		// 重產報表
		if ("LC899".equals(titaVo.getTxCode())) {
			this.info("LC899 exec l4321Report");
			String[] strAr = titaVo.getParam("Parm").split(",");
			this.iTxKind = parse.stringToInteger(strAr[0]);
			this.iCustType = parse.stringToInteger(strAr[1]);
			this.iAdjCode = parse.stringToInteger(strAr[2]);
			
			titaVo.putParam("TxKind", strAr[0]);
			titaVo.putParam("CustType", strAr[1]);
			titaVo.putParam("AdjCode", strAr[2]);
			titaVo.putParam("AdjDate", strAr[3]);
			putFileNm(titaVo);
			l4321Report.exec(2, titaVo);
			return null;
		}
		this.iTxKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		this.iCustType = parse.stringToInteger(titaVo.getParam("CustType"));
		this.iAdjCode = parse.stringToInteger(titaVo.get("AdjCode"));
		// 設定分頁、筆數
		this.index = titaVo.getReturnIndex();
		this.limit = Integer.MAX_VALUE;
		List<Map<String, String>> fnAllList = new ArrayList<>();
		Boolean flag = true;
		String sendMsg = "";
		if (titaVo.isHcodeNormal() && titaVo.isActfgEntry()) {
			try {
				fnAllList = L4321ServiceImpl.findAll(0, titaVo);
			} catch (Exception e) {
				flag = false;
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("L4321ServiceImpl.findAll error = " + errors.toString());
			}
			// 檢查未放行
			if (!flag) {
				if (fnAllList != null && fnAllList.size() != 0) {
					for (Map<String, String> s : fnAllList) {
						if ("1".equals(s.get("ActFg"))) {
							flag = false;
							sendMsg += " 戶號：" + s.get("CustNo");
						}
					}
				}
				if (!flag) {
					throw new LogicException("E0015", sendMsg); // 檢查錯誤
				}
			}
		}

//		titaVo titaVo
//		date 日期
//		brno 單位
//		filecode 檔案編號
//		fileitem 檔案說明
//		filename 輸出檔案名稱(不含副檔名,預設為.xlsx)
//		defaultExcel 預設excel底稿檔
//		defaultSheet 預設sheet,可指定 sheet index or sheet name
		// 執行交易
		putFileNm(titaVo);
		MySpring.newTask("L4321Batch", this.txBuffer, titaVo);
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void putFileNm(TitaVo titaVo) throws LogicException {
		String fileNm1 = "";
		if (this.iCustType == 1) {
			fileNm1 = "個金";
		} else {
			fileNm1 = "企金";
		}
		switch (this.iTxKind) {
		case 1:
			fileNm1 += "定期機動利率";
			break;
		case 2:
			fileNm1 += "機動指數利率";
			break;
		case 3:
			fileNm1 += "機動非指數利率";
			break;
		case 4:
			fileNm1 += "員工利率變動";
			break;
		case 5:
			fileNm1 += "按商品別利率";
			break;
		default:
			break;
		}
		switch (this.iAdjCode) {
		case 1:
			fileNm += fileNm1 + "-批次自動調整";
			break;
		case 2:
			fileNm += fileNm1 + "-按地區別調整";
			break;
		case 3:
			fileNm += fileNm1 + "-人工調整_按合約";
			break;
		case 4:
			fileNm += fileNm1 + "-人工調整_按地區別";
			break;
		default:
			break;
		}
		fileNm += titaVo.getCalTm().substring(0, 4);

		titaVo.putParam("FileNm", fileNm);
		titaVo.putParam("iCode", "L4321");
		titaVo.putParam("iItem", fileNm);

	}
}