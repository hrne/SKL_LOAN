package com.st1.itx.trade.L6;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdIndustry;
import com.st1.itx.db.service.CdIndustryService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.data.CdIndustryFileVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L660A")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L660A extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L660A.class);

	@Autowired
	public CdIndustryService CdIndustryService;
	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public FileCom fileCom;
	@Autowired
	public CdIndustryFileVo CdIndustryFileVo;
	@Autowired
	public DataLog dataLog;
	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L660A ");
		this.totaVo.init(titaVo);

		int updcount = 0;
		int inscount = 0;
		this.info("L660A starting ........");
//      吃檔                                                   
		String filename = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + titaVo.getParam("FILENA").trim();

		ArrayList<String> dataLineList = new ArrayList<>();

//       編碼參數，設定為UTF-8 || big5
		try {
			dataLineList = fileCom.intputTxt(filename, "big5");
		} catch (IOException e) {
			e.printStackTrace();
			throw new LogicException("E0014", "L660A(" + filename + ")");
		}

//       使用資料容器內定義的方法切資料
		CdIndustryFileVo.setValueFromFile(dataLineList);

		ArrayList<OccursList> uploadFile = CdIndustryFileVo.getOccursList();

		if (uploadFile != null && uploadFile.size() != 0) {
			for (OccursList tempOccursList : uploadFile) {

				this.info("L660A data : " + tempOccursList.get("IndustryCode") + "-" + tempOccursList.get("IndustryItem") + "-" + tempOccursList.get("MainType"));

				CdIndustry tCdIndustry = new CdIndustry();

				tCdIndustry = CdIndustryService.holdById(tempOccursList.get("IndustryCode"));
				if (tCdIndustry != null) {
					this.info("L660A update : " + tempOccursList.get("IndustryCode") + "-" + tempOccursList.get("IndustryItem") + "-" + tempOccursList.get("MainType"));
					CdIndustry tCdIndustry2 = (CdIndustry) dataLog.clone(tCdIndustry); //異動前資料
					
					tCdIndustry.setIndustryCode(tempOccursList.get("IndustryCode"));
					tCdIndustry.setIndustryItem(tempOccursList.get("IndustryItem"));
					tCdIndustry.setMainType(tempOccursList.get("MainType"));
					updcount = updcount + 1;
					try {
						tCdIndustry = CdIndustryService.update2(tCdIndustry, titaVo);
					} catch (DBException e) {
						e.printStackTrace();
						throw new LogicException("E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
					}
					dataLog.setEnv(titaVo, tCdIndustry2, tCdIndustry); ////
					dataLog.exec(); ////
				} else {
					this.info("L660A insert : " + tempOccursList.get("IndustryCode") + "-" + tempOccursList.get("IndustryItem") + "-" + tempOccursList.get("MainType"));
					CdIndustry iCdIndustry = new CdIndustry();
					iCdIndustry.setIndustryCode(tempOccursList.get("IndustryCode"));
					iCdIndustry.setIndustryItem(tempOccursList.get("IndustryItem"));
					iCdIndustry.setMainType(tempOccursList.get("MainType"));
					inscount = inscount + 1;
					try {
						CdIndustryService.insert(iCdIndustry, titaVo);
					} catch (DBException e) {
						e.printStackTrace();
						throw new LogicException("E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				}
			}

		}

		this.totaVo.putParam("InsCount", inscount);
		this.totaVo.putParam("UpdCount", updcount);

		this.addList(this.totaVo);
		return this.sendList();

	}
}