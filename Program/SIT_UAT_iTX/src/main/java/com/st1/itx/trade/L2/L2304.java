package com.st1.itx.trade.L2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.data.AchAuthFileVo;
import com.st1.itx.util.common.data.L2304Vo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L2304")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2304 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2304.class);

	@Autowired
	public FileCom fileCom;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public RelsMainService sRelsMainService;

	@Autowired
	public AchAuthFileVo achAuthFileVo;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public L2304Vo l2304Vo;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	// 上傳檔案路徑
	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2304 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		deleRelsMain();

		this.info("flagA Start ...");

//		吃檔 利害關係人
		String filename = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		this.info("filename = " + filename);
		ArrayList<String> dataLineList = new ArrayList<>();

//		 編碼參數，設定為UTF-8 || big5

		try {
			dataLineList = fileCom.intputTxt(filename, "big5");
			this.info("dataLineList = " + dataLineList);
		} catch (IOException e) {
			e.printStackTrace();
			throw new LogicException("E0014", "L2304(" + filename + ")");
		}

//		 使用資料容器內定義的方法切資料
		l2304Vo.setValueFromFile(dataLineList);

		ArrayList<OccursList> upl2304Vo = l2304Vo.getOccursList();

		ArrayList<RelsMain> lRelsMain = new ArrayList<RelsMain>();
		CdEmp tCdEmp = new CdEmp();

		int seq = 0;
		if (upl2304Vo != null && upl2304Vo.size() != 0) {
			for (OccursList tempOccursList : upl2304Vo) {
				seq = seq + 1;
				// new OccursList
				OccursList OccursList = new OccursList();

				String RelsId = tempOccursList.get("RelsId").trim();

				this.info("ReltId = " + RelsId.toString());
				tCdEmp = cdEmpService.findAgentIdFirst(RelsId);
				if (tCdEmp != null) {
					RelsMain tRelsMain = new RelsMain();

					tRelsMain.setRelsUKey(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));

					tRelsMain.setRelsId(RelsId); // 關係人統編

					tRelsMain.setRelsName(tCdEmp.getFullname()); // 關係人姓名
					tRelsMain.setRelsCode("01"); // 關係人職稱
					tRelsMain.setRelsType(1); // 戶別

					try {
						sRelsMainService.insert(tRelsMain);
					} catch (DBException e) {
						throw new LogicException("E0005", "利害關係人主檔" + e.getErrorMsg());
					}

				} else {
					this.info("OORelsId = " + RelsId);
					OccursList.putParam("OORelsId", RelsId);

					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(OccursList);
				}
			}
		}

		this.info("totaVo = " + totaVo);
		this.addList(this.totaVo);
		return this.sendList();
	}

	// 刪除舊資料
	private void deleRelsMain() throws LogicException {
		List<RelsMain> deleRelsMain = new ArrayList<RelsMain>();
		Slice<RelsMain> sdeleRelsMain = sRelsMainService.findAll(this.index, this.limit);
		deleRelsMain = sdeleRelsMain == null ? null : sdeleRelsMain.getContent();
		if (deleRelsMain != null) {
			try {
				sRelsMainService.deleteAll(deleRelsMain);
			} catch (DBException e) {
				throw new LogicException("E0008", "利害關係人檔 : " + e.getErrorMsg());
			}
		}

	}
}