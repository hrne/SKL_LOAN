package com.st1.itx.trade.L7;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.Ias39LGD;
import com.st1.itx.db.service.Ias39LGDService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita DateStart=9,7 DateEnd=9,7 Type=X,2 END=X,1
 */

@Service("L7022") // 違約損失率檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L7022 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public Ias39LGDService sIas39LGDService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7022 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iType = titaVo.getParam("Type");

		int iDateStart = this.parse.stringToInteger(titaVo.getParam("DateStart"));
		iDateStart = iDateStart + 19110000;
		int iDateEnd = this.parse.stringToInteger(titaVo.getParam("DateEnd"));
		if (iDateEnd == 0) {
			iDateEnd = 99991231;
		} else {
			iDateEnd = iDateEnd + 19110000;
		}

		this.info("*** L7022 Date : " + iDateStart + "~" + iDateEnd);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 37 * 200 = 7,400

		// 查詢違約損失率檔
		Slice<Ias39LGD> slIas39LGD;
		if (iType.equals("00")) {
			slIas39LGD = sIas39LGDService.findType(iDateStart, iDateEnd, "00", "99", this.index, this.limit, titaVo);
		} else {
			slIas39LGD = sIas39LGDService.findDate(iType, iDateStart, iDateEnd, this.index, this.limit, titaVo);
		}
		List<Ias39LGD> lIas39LGD = slIas39LGD == null ? null : slIas39LGD.getContent();

		if (lIas39LGD == null || lIas39LGD.size() == 0) {
			throw new LogicException(titaVo, "E0001", "違約損失率檔"); // 查無資料
		}
		// 如有找到資料
		for (Ias39LGD tIas39LGD : lIas39LGD) {
			OccursList occursList = new OccursList();
			occursList.putParam("OODate", tIas39LGD.getDate());
			occursList.putParam("OOType", tIas39LGD.getType());
			//occursList.putParam("OOTypeDesc", tIas39LGD.getTypeDesc()); 改使用下拉選單中文
			occursList.putParam("OOLGDPercent", tIas39LGD.getLGDPercent());
			//occursList.putParam("OOEnable", tIas39LGD.getEnable());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slIas39LGD != null && slIas39LGD.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}