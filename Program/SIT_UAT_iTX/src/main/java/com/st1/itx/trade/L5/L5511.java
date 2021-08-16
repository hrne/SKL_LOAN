package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfRewardMedia;
import com.st1.itx.db.service.PfRewardMediaService;
//import com.st1.itx.db.service.springjpa.cm.L5051ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.util.MySpring;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L5511")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5511 extends TradeBuffer {

	@Autowired
	public PfRewardMediaService pfRewardMediaService;
	@Autowired
	public Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5511");

		this.totaVo.init(titaVo);
		List<Integer> typeList = new ArrayList<>();
		typeList.add(1);
		typeList.add(5);
		typeList.add(6);
		int iWorkMonth = parse.stringToInteger(titaVo.getParam("WorkMonth")) + 191100;
		Slice<PfRewardMedia> slPfRewardMedia = pfRewardMediaService.findWorkMonth(iWorkMonth, typeList, 0, this.index,
				this.limit, titaVo);

        if (slPfRewardMedia !=null) {
			throw new LogicException(titaVo, "E0010", "已匯入發放資料"); // 功能選擇錯誤
    	
        }

        MySpring.newTask("L5511Batch", this.txBuffer, titaVo);
		
		this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");

		this.addList(this.totaVo);
		return this.sendList();
	}

}