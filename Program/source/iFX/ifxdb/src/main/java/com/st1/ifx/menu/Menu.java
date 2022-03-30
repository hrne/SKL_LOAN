package com.st1.ifx.menu;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	String name;
	List<Menu> submenus = new ArrayList<Menu>();
	List<TranItem> items = new ArrayList<TranItem>();

	// HashMap<String,TranItem> codeMap = new HashMap<String, TranItem>();
	private boolean enabled;

	public Menu(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TranItem> getItems() {
		return items;
	}

	public void setItems(List<TranItem> items) {
		this.items = items;
	}

	public void addItem(TranItem item) {
		this.items.add(item);
	}

	public List<Menu> getSubmenus() {
		return submenus;
	}

	public void setSubmenus(List<Menu> submenus) {
		this.submenus = submenus;
	}

	public void addMenu(Menu menu) {
		this.submenus.add(menu);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("menu:" + this.name + ", " + this.isEnabled() + "\n");
		for (int i = 0; i < this.submenus.size(); i++) {
			sb.append(this.submenus.get(i).getName() + "\n");
			sb.append(this.submenus.get(i).toString());
		}
		for (int i = 0; i < this.items.size(); i++) {
			sb.append("\t" + this.items.get(i).toString() + "\n");
		}
		return sb.toString();
	}
	// public HashMap<String, TranItem> getCodeMap() {
	// return codeMap;
	// }
	// public void setCodeMap(HashMap<String, TranItem> codeMap) {
	// this.codeMap = codeMap;
	// }
	//
	// public TranItem getByCode(String txcode) {
	// return this.codeMap.get(txcode);
	// }

}
