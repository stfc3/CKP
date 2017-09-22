/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Span;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.West;

import com.dvd.ckp.business.service.UserService;
import com.dvd.ckp.domain.User;
import com.dvd.ckp.utils.Constants;

/**
 *
 * @author dmin
 */
public class MainController extends SelectorComposer<Component> {

	@WireVariable
	protected UserService userService;
	@Wire
	Spreadsheet ss;
	@Wire
	Tabbox tabContent;
	@Wire
	Tabs tabs;
	@Wire
	Tabpanels tabpanels;
	@Wire
	Treeitem itemCustomer;
	@Wire
	Treeitem itemContract;
	@Wire
	Treeitem itemConstruction;

	@Wire
	Treeitem itemPumps;

	@Wire
	Treeitem itemLocation;

	@Wire
	Treeitem itemStaff;

	@Wire
	Treeitem itemUser;

	@Wire
	Treeitem itemRole;

	@Wire
	Treeitem itemBills;
	@Wire
	West westMenu;
	@Wire
	Div showHideMenue;
	@Wire
	Span userName;
	private Session session;
	private List<Tab> lstTabs;
	private int limitTabs = 100;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		session = Sessions.getCurrent();
		if (session.getAttribute(Constants.TOKEN) == null) {
			Executions.sendRedirect(Constants.PAGE_LOGIN);
		}
		User users = (User) session.getAttribute(Constants.SESSION_USER);
		userName.appendChild(new Label(users.getFullName()));
		lstTabs = new ArrayList<Tab>();
	}

	@Listen("onClick = #logout")
	public void logout() throws IOException {
		session.invalidate();
		Executions.sendRedirect(Constants.PAGE_LOGIN);
	}

	@Listen("onClick = #showHideMenue")
	public void showHideMenue() throws IOException {
		westMenu.setOpen(!westMenu.isOpen());
	}

	@Listen("onClick = #itemCustomer")
	public void itemCustomer() throws IOException {
		String vstrURL = itemCustomer.getValue();
		String vstrId = "tab" + itemCustomer.getId();
		String vstrTitle = itemCustomer.getLabel();
		addTab(vstrURL, vstrId, vstrTitle);
	}

	@Listen("onClick = #itemContract")
	public void itemContract() throws IOException {
		String vstrURL = itemContract.getValue();
		String vstrId = "tab" + itemContract.getId();
		String vstrTitle = itemContract.getLabel();
		addTab(vstrURL, vstrId, vstrTitle);
	}

	@Listen("onClick = #itemConstruction")
	public void itemConstruction() throws IOException {
		String vstrURL = itemConstruction.getValue();
		String vstrId = "tab" + itemConstruction.getId();
		String vstrTitle = itemConstruction.getLabel();
		addTab(vstrURL, vstrId, vstrTitle);
	}

	// Pumps
	@Listen("onClick = #itemPumps")
	public void itemPumps() throws IOException {
		String vstrURL = itemPumps.getValue();
		String vstrId = "tab" + itemPumps.getId();
		String vstrTitle = itemPumps.getLabel();
		addTab(vstrURL, vstrId, vstrTitle);
	}

	// location
	@Listen("onClick = #itemLocation")
	public void itemLocation() throws IOException {
		String vstrURL = itemLocation.getValue();
		String vstrId = "tab" + itemLocation.getId();
		String vstrTitle = itemLocation.getLabel();
		addTab(vstrURL, vstrId, vstrTitle);
	}

	// staff
	@Listen("onClick = #itemStaff")
	public void itemStaff() throws IOException {
		String vstrURL = itemStaff.getValue();
		String vstrId = "tab" + itemStaff.getId();
		String vstrTitle = itemStaff.getLabel();
		addTab(vstrURL, vstrId, vstrTitle);
	}

	// staff
	@Listen("onClick = #itemBills")
	public void itemStaffQuantity() throws IOException {
		String vstrURL = itemBills.getValue();
		String vstrId = "tab" + itemBills.getId();
		String vstrTitle = itemBills.getLabel();
		addTab(vstrURL, vstrId, vstrTitle);
	}

	@Listen("onClick = #itemUser")
	public void itemUser() throws IOException {
		String vstrURL = itemUser.getValue();
		String vstrId = "tab" + itemUser.getId();
		String vstrTitle = itemUser.getLabel();
		addTab(vstrURL, vstrId, vstrTitle);
	}

	@Listen("onClick = #itemRole")
	public void itemRole() throws IOException {
		String vstrURL = itemRole.getValue();
		String vstrId = "tab" + itemRole.getId();
		String vstrTitle = itemRole.getLabel();
		addTab(vstrURL, vstrId, vstrTitle);
	}

	private void addTab(String pstrURL, final String pstrId, String pstrTilte) {
		Include contentTabMenu;
		if (lstTabs.size() < limitTabs) {
			Tab newTab = getExistTab(pstrId, lstTabs);
			if (newTab == null) {
				newTab = new Tab(pstrTilte);
				newTab.setTooltiptext(pstrTilte);
				newTab.setId(pstrId);
				newTab.setClosable(true);
				newTab.setSelected(true);
				newTab.setParent(tabs);
				newTab.addEventListener("onClose", new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						removeTab(pstrId);
					}
				});
				lstTabs.add(newTab);
				Tabpanel tp = new Tabpanel();
				contentTabMenu = new Include();
				contentTabMenu.setSrc(pstrURL);
				contentTabMenu.setParent(tp);
				tp.setParent(tabpanels);
			}
			newTab.setSelected(true);
		}
	}

	private Tab getExistTab(String idNewTab, List<Tab> tabs) {

		if (tabs != null && tabs.size() > 0) {
			for (int i = 0; i < tabs.size(); i++) {
				String idTabi = tabs.get(i).getId();
				if (idNewTab.equalsIgnoreCase(idTabi)) {
					return tabs.get(i);
				}
			}

		}

		return null;

	}

	private void removeTab(String closeId) {
		if (lstTabs != null && lstTabs.size() > 0) {
			for (int i = 0; i < lstTabs.size(); i++) {
				String idTabi = lstTabs.get(i).getId();
				if (closeId.equalsIgnoreCase(idTabi)) {
					lstTabs.remove(lstTabs.get(i));
				}
			}

		}
	}

}
