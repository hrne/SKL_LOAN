package com.st1.def.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Menu {
	String name;
	List<Menu> submenus = new ArrayList<Menu>();
	List<MenuItem> items = new ArrayList<MenuItem>();

	HashMap<String, MenuItem> codeMap = new HashMap<String, MenuItem>();

	public Menu(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MenuItem> getItems() {
		return items;
	}

	public void setItems(List<MenuItem> items) {
		this.items = items;
	}

	public void addItem(MenuItem item) {
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < this.submenus.size(); i++) {
			sb.append(this.submenus.get(i).getName() + "\n");
			sb.append(this.submenus.get(i).toString());
		}
		for (int i = 0; i < this.items.size(); i++) {
			sb.append("\t" + this.items.get(i).toString() + "\n");
		}
		return sb.toString();
	}

	public HashMap<String, MenuItem> getCodeMap() {
		return codeMap;
	}

	public void setCodeMap(HashMap<String, MenuItem> codeMap) {
		this.codeMap = codeMap;
	}

	public MenuItem getByCode(String txcode) {
		return this.codeMap.get(txcode);
	}

}
