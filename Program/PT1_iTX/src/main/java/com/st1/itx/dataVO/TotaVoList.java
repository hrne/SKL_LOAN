package com.st1.itx.dataVO;

import java.util.ArrayList;

import com.st1.itx.eum.ContentName;

public class TotaVoList extends ArrayList<TotaVo> {

	private static final long serialVersionUID = 8948999343481205587L;

	/**
	 * 取得送往iFX的totaList
	 * 
	 * @return ArrayList TotaVo iFXList
	 */
	public ArrayList<TotaVo> getIfxList() {
		ArrayList<TotaVo> iFxList = new ArrayList<TotaVo>();
		for (TotaVo ifx : this)
			if (ifx.get(ContentName.sendTo).equals("iFX"))
				iFxList.add(ifx);

		return iFxList;
	}

	public ArrayList<TotaVo> getOtherList() {
		return null;
	}
}
