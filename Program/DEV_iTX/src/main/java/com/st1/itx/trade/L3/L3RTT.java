package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L3RTT")
@Scope("prototype")
/**
 * Test
 * 
 * @author Adam Pan
 * @version 1.0.0
 */
public class L3RTT extends TradeBuffer {

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3RTT ");
		this.totaVo.init(titaVo);
		String text = "{red-s}{b-s}Google 雲端硬碟附加服務條款{b-e}{red-e}\n" + "生效日期：2020 年 3 月 31 日 (查看先前版本)\n" + "\n"
				+ "若要使用 Google 雲端硬碟，您必須接受 (1)《Google 服務條款》以及 (2) 這些 Google 雲端硬碟附加服務條款 (《Google 雲端硬碟附加條款》)。\n" + "\n" + "請仔細閱讀上述這些文件。這些文件統稱為「條款」，內容說明在您使用 Google 服務時，我們致力遵守的服務原則以及期許您遵守的行為準則。\n" + "\n"
				+ "雖然 Google 的隱私權政策並非構成本「條款」的一部分，但仍建議您詳閱，進一步瞭解如何更新、管理、匯出及刪除您的資訊。\n" + "一、您的內容\n" + "\n"
				+ "Google {b-s}雲端硬碟可讓您上傳、提交、儲存、傳送及接收內容{b-e}；如同《Google 服務條款》中所述，您的內容仍屬於您所有。對於您的任何內容 (包括您在雲端硬碟帳戶中上傳、共用或儲存的所有文字、資料、資訊和檔案)，我們都不會聲明擁有權。《Google 服務條款》提供 Google 限定用途的授權以經營及改善 Google 雲端硬碟服務；因此，如果您決定與他人共用文件，或是想要在其他裝置上開啟文件，我們可以提供這項功能。\n"
				+ "\n" + "{blue-s}Google 雲端硬碟也可以讓您和其他 Google 雲端硬碟使用者共同協作處理內容。內容的「擁有者」為控制內容和其用途的使用者。{blue-e}\n" + "\n"
				+ "Google 雲端硬碟中的共用設定可讓您控管其他人如何使用您在 Google 雲端硬碟中的內容。檔案的隱私權設定則是取決於檔案所在的資料夾或雲端硬碟。只要您還沒指定共用對象，您個人雲端硬碟中的檔案就只有您本人能夠存取。您可以與他人共用自己的內容，也可以將內容的管理權轉移給其他使用者。如果您在他人與您共用的資料夾或雲端硬碟中建立或放置檔案，這些檔案將會沿用所在資料夾或雲端硬碟的共用設定，也可能會沿用擁有權設定。除非 Google 隱私權政策另有規定，否則我們不會與他人共用您的檔案和資料。\n"
				+ "\n" + "我們不會將您的內容用於行銷或宣傳性廣告活動。\n" + "二、計畫政策\n" + "\n" + "我們可對內容進行審查，以判斷其是否違法或違反《計畫政策》，並可移除或拒絕顯示我們合理確信違反政策或法律的內容。不過，這不表示我們一定會對內容進行審查，因此請勿如此認定。\n";

		// 設為視窗顯示
		this.totaVo.setHtmlContent(text);

		this.addList(this.totaVo);
		return this.sendList();
	}
}