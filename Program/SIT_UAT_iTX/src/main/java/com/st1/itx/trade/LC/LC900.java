package com.st1.itx.trade.LC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import com.st1.itx.db.service.springjpa.cm.LC900ServiceImpl;

@Service("LC900")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC900 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(LC900.class);

	// 檔案輸出路徑
	@Value("${iTXOutFolder}")
	private String OutFolder = "";

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public LC900ServiceImpl sLC900ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC900 ");
		this.totaVo.init(titaVo);

		String filename = OutFolder + "host_TXCD-" + dateUtil.getNowIntegerForBC() + "-01.txt";
		File file = new File(filename);// 建立檔案，準備寫檔
		try {
			BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "big5"));
			bufWriter.write(
					"TXCD    9                                                                                                                                                                                                                                       $");

			List<Map<String, String>> lc900List = null;
			try {
				lc900List = sLC900ServiceImpl.findAll();
			} catch (Exception e) {
				throw new LogicException(titaVo, "EC009", "LC900ServiceImpl.findAll");
			}

			if (lc900List != null) {
				for (Map<String, String> lc900Vo : lc900List) {
//					this.info("LC900 Menu = " + lc900Vo.get("F0").toString() + "/" + lc900Vo.get("F1").toString() + "/" + lc900Vo.get("F2").toString() + "/" + lc900Vo.get("F3").toString());
					bufWriter.write("\r\n");
					String s = "TXCD    0";
					s += String.format("%-5s", lc900Vo.get("F1").toString()) + dateUtil.getNowIntegerForBC() + lc900Vo.get("F4").toString() + String.format("%-5s", lc900Vo.get("F0").toString());
					s += String.format("00     %s", fillspaces(lc900Vo.get("F2").toString(), 120));
					s += lc900Vo.get("F3").toString();
					s += "002000000000000000000000000000000000000000000000000000000000000000000000011111000000$";
					bufWriter.write(s);
				}
			}

			bufWriter.close();
		} catch (IOException e) {
			throw new LogicException(titaVo, "EC009", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String fillspaces(String s, int len) {
		String rs = "";

		int rlen = clength(s);
		if (rlen < len) {
			rs = s + String.format("%" + (len - rlen) + "s", " ");
		}
		return rs;
	}

	// 中文以2位計算長度
	private int clength(String s) {
		int clen = 0;
		for (int i = 0; i < s.length(); i++) {
			String c = s.substring(i, i + 1);
			if (c.matches("[\\u0391-\\uFFE5]+")) {
				clen += 2;
			} else {
				clen += 1;
			}

//			this.info("XXR99 clength [" + c + "] = " + clen);
		}

		return clen;
	}

}