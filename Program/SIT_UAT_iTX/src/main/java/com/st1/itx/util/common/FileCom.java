package com.st1.itx.util.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.filter.FilterUtils;
import com.st1.itx.util.filter.SafeClose;

/**
 * 讀取檔案共用工具
 * 
 * @author ST1-Chih Wei
 */
@Component("FileCom")
@Scope("prototype")
public class FileCom extends CommBuffer {

	/**
	 * 讀取文字檔，編碼限為UTF-8或big5
	 * 
	 * @param filePath 讀取檔案的路徑，須包含檔名及副檔名
	 * @param codeType 編碼("UTF-8"或"big5")
	 * @return ArrayList of String 以一行為一筆資料，按行數順序裝到ArrayList
	 * @throws IOException    交易程式需承接IOException
	 * @throws LogicException EC010
	 * @author ST1-Chih Wei
	 */
	public ArrayList<String> intputTxt(String filePath, String codeType) throws IOException, LogicException {

		// 檔案編碼串流
		if (codeType != null && (codeType.toUpperCase(Locale.TAIWAN).equals("BIG5") || codeType.toUpperCase(Locale.TAIWAN).equals("UTF-8")))
			;
		else
			throw new LogicException("EC010", "讀取文字檔，編碼限UTF-8或BIG5");

		// 宣告
		ArrayList<String> result = new ArrayList<>();

		// 檔案串流
		FileInputStream fis = null;

		// 編碼參數，設定為UTF-8 || big5
		InputStreamReader isr = null;

		// 開啟讀取工具
		BufferedReader br = null;

		try {
			// 檔案路徑
			File file = new File(FilterUtils.escape(filePath));
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, codeType);
			br = new BufferedReader(isr);

			// 逐行讀取裝到回傳資料容器
			while (br.ready()) {
				// 讀取一行
				String dataLine = br.readLine();
				// 裝到回傳資料容器
				result.add(dataLine);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		} finally {
			// 關閉讀取工具
			SafeClose.close(br);
			SafeClose.close(isr);
			SafeClose.close(fis);
		}

		// 回傳
		return result;
	}

	@Override
	public void exec() throws LogicException {
		// TODO Auto-generated method stub

	}

}
