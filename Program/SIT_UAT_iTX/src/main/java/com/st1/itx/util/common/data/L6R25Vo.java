package com.st1.itx.util.common.data;

import javax.persistence.Entity;

@Entity
public class L6R25Vo {

	// 交易代號
	private String TranNo = "";

	// 交易名稱
	private String TranItem = "";

	// 主選單類別
	private String MenuNo = "";

	// 子選單類別
	private String SubMenuNo = "";

	// 子選單類別名稱
	private String SubMenuItem = "";

	// 掛入選單記號
	private int MenuFg = 0;

	// 交易類別
	private int TypeFg = 0;

	// 權限記號
	private int AuthFg = 0;

	public L6R25Vo(String tranNo, String tranItem, String menuNo, String subMenuNo, String item, Integer menuFg, Integer typeFg, Integer authFg) {
		super();
		this.TranNo = tranNo;
		this.TranItem = tranItem;
		this.MenuNo = menuNo;
		this.SubMenuNo = subMenuNo;
		this.SubMenuItem = item;
		if (menuFg == null) {
			this.MenuFg = 0;
		} else {
			this.MenuFg = menuFg;
		}
		if (typeFg == null) {
			this.TypeFg = 0;
		} else {
			this.TypeFg = typeFg;
		}
		if (authFg == null) {
			this.AuthFg = 0;
		} else {
			this.AuthFg = authFg;
		}
	}

	public String getTranNo() {
		return TranNo;
	}

	public void setTranNo(String TranNo) {
		this.TranNo = TranNo;
	}

	public String getTranItem() {
		return TranItem;
	}

	public void setTranItem(String TranItem) {
		this.TranItem = TranItem;
	}

	public String getMenuNo() {
		return MenuNo;
	}

	public void setMenuNo(String MenuNo) {
		this.MenuNo = MenuNo;
	}

	public String getSubMenuNo() {
		return SubMenuNo;
	}

	public void setSubMenuNo(String SubMenuNo) {
		this.SubMenuNo = SubMenuNo;
	}

	public String getSubMenuItem() {
		return SubMenuItem;
	}

	public void setSubMenuItem(String SubMenuItem) {
		this.SubMenuItem = SubMenuItem;
	}

	public int getMenuFg() {
		return MenuFg;
	}

	public void setMenuFg(int MenuFg) {
		this.MenuFg = MenuFg;
	}

	public int getTypeFg() {
		return TypeFg;
	}

	public void setTypeFg(int TypeFg) {
		this.TypeFg = TypeFg;
	}

	public int getAuthFg() {
		return AuthFg;
	}

	public void setAuthFg(int AuthFg) {
		this.AuthFg = AuthFg;
	}

	@Override
	public String toString() {
		return "L6R25Vo [TranNo=" + TranNo + ", TranItem=" + TranItem + ", MenuNo=" + MenuNo + ", SubMenuNo=" + SubMenuNo + ", SubMenuItem=" + SubMenuItem + ", MenuFg=" + MenuFg + ", AuthFg=" + AuthFg
				+ "]";
	}
}
