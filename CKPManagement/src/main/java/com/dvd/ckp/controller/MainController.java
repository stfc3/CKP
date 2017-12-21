/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.bean.UserToken;
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
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.domain.Object;
import com.dvd.ckp.utils.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Li;
import org.zkoss.zul.A;
import org.zkoss.zul.Image;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;

/**
 *
 * @author dmin
 */
public class MainController extends SelectorComposer<Component> {

    @WireVariable
    protected UserService userService;
    @Wire
    Tabbox tabContent;
    @Wire
    Tabs tabs;
    @Wire
    Tabpanels tabpanels;
    @Wire
    Tree treeMenu;
    
    @Wire
    Div divListFunction;
    
    @Wire
    West westMenu;
    @Wire
    Div showHideMenue;
    @Wire
    Span userName;
    private Session session;
    private List<Tab> lstTabs;
    private final int limitTabs = 100;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        session = Sessions.getCurrent();
        if (session.getAttribute(Constants.USER_TOKEN) == null) {
            Executions.sendRedirect(Constants.PAGE_LOGIN);
        } else {
            UserToken userToken = (UserToken) session.getAttribute(Constants.USER_TOKEN);
            if (userToken != null) {
                userName.appendChild(new Label(userToken.getFullName()));
                createMenu(userToken);
                createMenuHome(userToken);
            }
        }
        lstTabs = new ArrayList<>();
    }

    private void createMenu(UserToken userToken) {
        if (userToken != null) {
            List<Object> lstAllObjects = userToken.getListObject();
            List<Object> lstRootMenu = getListRootMenu(lstAllObjects);
            if (lstRootMenu != null && !lstRootMenu.isEmpty()) {
                Treechildren treeChildrenRoot = new Treechildren();
                treeChildrenRoot.setParent(treeMenu);
                for (Object rootMenu : lstRootMenu) {
                    if (rootMenu != null) {
                        Treeitem itemRoot = new Treeitem();
                        itemRoot.setOpen(false);
                        itemRoot.setId(rootMenu.getObjectCode());
                        itemRoot.setLabel(Labels.getLabel(rootMenu.getObjectName()));
                        itemRoot.setValue(rootMenu.getPath());
                        //Add click event
                        itemRoot.addEventListener("onClick", new TreeOnClickListener());
                        itemRoot.setParent(treeChildrenRoot);

                        List<Object> lstChilds = getListChild(lstAllObjects, rootMenu.getObjectId());
                        if (lstChilds != null && !lstChilds.isEmpty()) {
                            addMenuItem(lstAllObjects, lstChilds, itemRoot);
                        }

                    }
                }
            }
        }
    }

    private void addMenuItem(List<Object> lstAllObjects, List<Object> lstObjectChilds, Treeitem itemParent) {
        if (lstObjectChilds != null && !lstObjectChilds.isEmpty()) {
            Treechildren treeChildren = new Treechildren();
            treeChildren.setParent(itemParent);
            for (Object itemMenu : lstObjectChilds) {
                if (itemMenu != null) {
                    Treeitem item = new Treeitem();
                    item.setOpen(false);
                    item.setId(itemMenu.getObjectCode());
                    item.setLabel(Labels.getLabel(itemMenu.getObjectName()));
                    item.setValue(itemMenu.getPath());
                    //Add click event
                    item.addEventListener("onClick", new TreeOnClickListener());
                    item.setParent(treeChildren);

                    List<Object> lstChilds = getListChild(lstAllObjects, itemMenu.getObjectId());
                    if (lstChilds != null && !lstChilds.isEmpty()) {
                        addMenuItem(lstAllObjects, lstChilds, item);
                    }

                }
            }
        }
    }

    private List<Object> getListRootMenu(List<Object> lstObjects) {
        List<Object> lstRootMenu = new ArrayList<>();
        if (lstObjects != null && !lstObjects.isEmpty()) {
            for (Object object : lstObjects) {
                if (object.getParentId() == null && object.getObjectType()==1L) {
                    lstRootMenu.add(object);
                }
            }
        }
        return lstRootMenu;
    }

    private List<Object> getListChild(List<Object> lstObjects, Long parentId) {
        List<Object> lstChilds = new ArrayList<>();
        if (lstObjects != null && !lstObjects.isEmpty()) {
            for (Object object : lstObjects) {
                if (parentId.equals(object.getParentId()) && object.getObjectType()==1L) {
                    lstChilds.add(object);
                }
            }
        }
        return lstChilds;
    }
    private void createMenuHome(UserToken userToken){
        if(userToken!=null){
            List<Object> lstAllObjects = userToken.getListObject();
            List<Object> lstFunction = getListFunction(lstAllObjects);
            if(lstFunction!=null && !lstFunction.isEmpty()){
                for(Object function:lstFunction){
                    buildFunction(function);
                }
            }
        }
    }
    private void buildFunction(Object object){
        Li liItem=new Li();
        liItem.setSclass("collection__item");
        liItem.setParent(divListFunction);
        
        Div divCardType=new Div();
        divCardType.setClass("card card--types");
        divCardType.setParent(liItem);
        
        
        Div divCardFigure=new Div();
        divCardFigure.setClass("card__figure");
        divCardFigure.setParent(divCardType);
        
        A aCardLink=new A();
        aCardLink.setClass("card__link");
        aCardLink.setParent(divCardFigure);
        aCardLink.setAttribute("MENU_ID", object.getObjectCode());
        aCardLink.setAttribute("MENU_NAME", Labels.getLabel(object.getObjectName()));
        aCardLink.setAttribute("MENU_PATH", object.getPath());
        
        aCardLink.addEventListener("onClick", new MenuHomeOnClickListener());
        
        Image imgCardImage=new Image();
        imgCardImage.setClass("card__image");
        imgCardImage.setSrc(object.getIcon());
        imgCardImage.setParent(divCardFigure);
        Div divCardPanel=new Div();
        divCardPanel.setClass("card__panel");
        divCardPanel.setParent(divCardFigure);
        
        Span spanCardCta=new Span();
        spanCardCta.setClass("card__cta");
        spanCardCta.setParent(divCardPanel);
        Label lblFunctionName=new Label(Labels.getLabel(object.getObjectName()));
        lblFunctionName.setParent(spanCardCta);
        
        
        Div divCardHeading=new Div();
        divCardHeading.setClass("card__heading");
        divCardHeading.setParent(divCardType);
        
        Span spanCardCount=new Span();
        spanCardCount.setClass("card__count");
        spanCardCount.setParent(divCardHeading);
        Label lblFunctionShort=new Label(Labels.getLabel(object.getObjectName()));
        lblFunctionShort.setParent(divCardHeading);
        
        
    }
    private List<Object> getListFunction(List<Object> lstObjects) {
        List<Object> lstFunction = new ArrayList<>();
        if (lstObjects != null && !lstObjects.isEmpty()) {
            for (Object object : lstObjects) {
                if (StringUtils.isValidString(object.getPath()) && StringUtils.isValidString(object.getIcon()) && object.getObjectType()==1L) {
                    lstFunction.add(object);
                }
            }
        }
        return lstFunction;
    }

    class TreeOnClickListener implements EventListener {

        @Override
        public void onEvent(Event event) {
            try {
                Treeitem treeitem = (Treeitem) event.getTarget();
                String strUrl = treeitem.getValue();
                String strId = "tab_" + treeitem.getId();
                String strTabName = treeitem.getLabel();
                if (StringUtils.isValidString(strUrl)) {
                    addTab(strUrl, strId, strTabName);
                } else {
                    treeitem.setOpen(!treeitem.isOpen());
                }

            } catch (Exception ex) {
//                logger.error(ex.getMessage(), ex);
            }
        }
    }
    
    class MenuHomeOnClickListener implements EventListener {

        @Override
        public void onEvent(Event event) {
            try {
                A menu = (A) event.getTarget();
                String strUrl = String.valueOf(menu.getAttribute("MENU_PATH"));
                String strId = "tab_" + String.valueOf(menu.getAttribute("MENU_ID"));
                String strTabName = String.valueOf(menu.getAttribute("MENU_NAME"));
                if (StringUtils.isValidString(strUrl)) {
                    addTab(strUrl, strId, strTabName);
                } 

            } catch (Exception ex) {
//                logger.error(ex.getMessage(), ex);
            }
        }
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


    private void addTab(String pstrURL, final String pstrId, String pstrTablName) {
        Include contentTabMenu;
        if (lstTabs.size() < limitTabs) {
            Tab newTab = getExistTab(pstrId, lstTabs);
            if (newTab == null) {
                newTab = new Tab(pstrTablName);
                newTab.setTooltiptext(pstrTablName);
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
