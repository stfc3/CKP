/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;

import com.dvd.ckp.business.service.ContractService;
import com.dvd.ckp.business.service.LocationServices;
import com.dvd.ckp.business.service.PumpServices;
import com.dvd.ckp.business.service.UtilsService;
import com.dvd.ckp.domain.Contract;
import com.dvd.ckp.domain.Location;
import com.dvd.ckp.domain.Param;
import com.dvd.ckp.domain.Price;
import com.dvd.ckp.domain.Pumps;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StyleUtils;
import java.util.Date;
import javax.servlet.ServletContext;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Longbox;

/**
 *
 * @author dmin
 */
public class PriceController extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(PriceController.class);

    ServletContext context;

    @WireVariable
    protected ContractService contractService;
    @WireVariable
    protected UtilsService utilsService;
    @WireVariable
    protected PumpServices pumpService;
    @WireVariable
    protected LocationServices locationService;
    @Wire
    private Grid lstPrice;
    @Wire
    private Intbox intAdd;
    @Wire
    private Longbox lgbContractId;

    ListModelList<Price> listDataModelPrice;

    Param defaultParam;
    List<Param> lstPumpType;

    List<Param> lstLocationType;

    List<Param> lstConvertType;

    Pumps defaultPump;
    List<Pumps> lstPumps;

    Location defaultLocation;
    List<Location> lstLocationMin;

    List<Location> lstLocationMax;

    /// index price
    private final int pumpTypeIndex = 1;
    private final int pumpIndex = 2;
    private final int m3Index = 3;
    private final int shiftIndex = 4;
    private final int waitIndex = 5;
    private final int locationTypeIndex = 6;
    private final int locationMinIndex = 7;
    private final int locationMaxIndex = 8;
    private final int locationIndex = 9;
    private final int convertTypeIndex = 10;
    private final int convertValueIndex = 11;
    
    
    private Long lngContractId;
    ///

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        context = Sessions.getCurrent().getWebApp().getServletContext();
        contractService = (ContractService) SpringUtil.getBean(SpringConstant.CONTRACT_SERVICES);
        utilsService = (UtilsService) SpringUtil.getBean(SpringConstant.UTILS_SERVICES);
        pumpService = (PumpServices) SpringUtil.getBean(SpringConstant.PUMPS_SERVICES);
        locationService = (LocationServices) SpringUtil.getBean(SpringConstant.LOCATION_SERVICES);

//        price
        defaultParam = new Param();
        defaultParam.setParamValue(Constants.DEFAULT_ID);
        defaultParam.setParamName(Labels.getLabel("option"));

        //list loai may bom
        lstPumpType = utilsService.getParamByKey(Constants.PRAM_PUMP_TYPE);
        if (lstPumpType == null) {
            lstPumpType = new ArrayList<>();
        }
        lstPumpType.add(Constants.FIRST_INDEX, defaultParam);

        //list loai vi tri
        lstLocationType = utilsService.getParamByKey(Constants.PRAM_LOCATION_TYPE);
        if (lstLocationType == null) {
            lstLocationType = new ArrayList<>();
        }
        lstLocationType.add(Constants.FIRST_INDEX, defaultParam);

        //list loai convert
        lstConvertType = utilsService.getParamByKey(Constants.PRAM_CONVERT_TYPE);
        if (lstConvertType == null) {
            lstConvertType = new ArrayList<>();
        }
        lstConvertType.add(Constants.FIRST_INDEX, defaultParam);

        //list bom
        defaultPump = new Pumps();
        defaultPump.setPumpsID(Constants.DEFAULT_ID);
        defaultPump.setPumpsName(Labels.getLabel("option"));
        lstPumps = pumpService.getAllListData();
        if (lstPumps == null) {
            lstPumps = new ArrayList<>();
        }
        lstPumps.add(Constants.FIRST_INDEX, defaultPump);

        //list vi tri
        defaultLocation = new Location();
        defaultLocation.setLocationID(Constants.SPECIAL_ID);
        defaultLocation.setLocationName(Labels.getLabel("option"));
        lstLocationMin = locationService.getListLocation();
        if (lstLocationMin == null) {
            lstLocationMin = new ArrayList<>();
        }
        lstLocationMin.add(Constants.FIRST_INDEX, defaultLocation);

        lstLocationMax = lstLocationMin;

        List<Price> vlstPrice = new ArrayList<>();
        if (intAdd.getValue() == 1) {
            listDataModelPrice = new ListModelList<>(vlstPrice);
        } else {
            lngContractId = lgbContractId.getValue();
            vlstPrice = contractService.getPriceByContract(lngContractId);
            listDataModelPrice = new ListModelList<>(vlstPrice);
        }

        lstPrice.setModel(listDataModelPrice);
        setDataDefaultInGrid();
//        price
    }

    /**
     * Edit row
     *
     * @param event
     */
    public void onEdit(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Price price = rowSelected.getValue();
        setComboboxParam(lstCell, getParamDefault(price.getPumpType(), pumpTypeIndex), pumpTypeIndex);
        setComboboxParam(lstCell, getParamDefault(price.getLocationType(), locationTypeIndex), locationTypeIndex);
        setComboboxParam(lstCell, getParamDefault(price.getConvertType(), convertTypeIndex), convertTypeIndex);
        setComboboxLocation(lstCell, getLocatoionDefault(price.getLocationMin(), locationMinIndex), locationMinIndex);
        setComboboxLocation(lstCell, getLocatoionDefault(price.getLocationMax(), locationMaxIndex), locationMaxIndex);
        setComboboxPump(lstCell, getPumpDefault(price.getPumpId()), pumpIndex);
        StyleUtils.setEnableComponent(lstCell, 4);
    }

    /**
     * Cancel
     *
     * @param event
     */
    public void onCancel(ForwardEvent event) {

        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid();

    }

    /**
     * Save
     *
     * @param event
     */
    public void onSave(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Price p = rowSelected.getValue();
        Price price = getDataInRow(lstCell);
        price.setPriceId(p.getPriceId());
        price.setContractId(lngContractId);
        price.setStatus(Constants.STATUS_ACTIVE);
        price.setCreateDate(new Date());
        contractService.insertOrUpdatePrice(price);
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid();
    }

    public void onDelete(ForwardEvent event) {
        Messagebox.show(Labels.getLabel("message.confirm.delete.content"), Labels.getLabel("message.confirm.delete.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event e) {
                if (Messagebox.ON_YES.equals(e.getName())) {
                    Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
                    Price price = rowSelected.getValue();
                    price.setStatus(Constants.STATUS_INACTIVE);
                    price.setCreateDate(new Date());
                    contractService.insertOrUpdatePrice(price);
                    reloadGrid();
                }
            }
        });
    }

    /**
     * Add row
     */
    public void onClick$add() {
        Price price = new Price();
        listDataModelPrice.add(Constants.FIRST_INDEX, price);
        lstPrice.setModel(listDataModelPrice);
        lstPrice.renderAll();
        List<Component> lstCell = lstPrice.getRows().getFirstChild().getChildren();
        setDataDefaultInGrid();
        StyleUtils.setEnableComponent(lstCell, 4);
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private Price getDataInRow(List<Component> lstCell) {
        Price price = new Price();
        Component component;
        Combobox cbxPumpType = null;
        Combobox cbxPump = null;
        Doublebox dbbM3 = null;
        Doublebox dbbShift = null;
        Doublebox dbbWait = null;
        Combobox cbxLocationType = null;
        Combobox cbxLocationMin = null;
        Combobox cbxLocationMax = null;
        Doublebox dbbLocation = null;
        Combobox cbxConvertType = null;
        Doublebox dbbConvertValue = null;
        component = lstCell.get(pumpTypeIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxPumpType = (Combobox) component;
            price.setPumpType(cbxPumpType.getSelectedItem().getValue());
        }
        component = lstCell.get(pumpIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxPump = (Combobox) component;
            price.setPumpId(cbxPump.getSelectedItem().getValue());
        }
        component = lstCell.get(m3Index).getFirstChild();
        if (component != null && component instanceof Doublebox) {
            dbbM3 = (Doublebox) component;
            price.setPriceM3(dbbM3.getValue());
        }
        component = lstCell.get(shiftIndex).getFirstChild();
        if (component != null && component instanceof Doublebox) {
            dbbShift = (Doublebox) component;
            price.setPriceShift(dbbShift.getValue());
        }
        component = lstCell.get(waitIndex).getFirstChild();
        if (component != null && component instanceof Doublebox) {
            dbbWait = (Doublebox) component;
            price.setPriceWait(dbbWait.getValue());
        }
        component = lstCell.get(locationTypeIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxLocationType = (Combobox) component;
            price.setLocationType(cbxLocationType.getSelectedItem().getValue());
        }
        component = lstCell.get(locationMinIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxLocationMin = (Combobox) component;
            price.setLocationMin(cbxLocationMin.getSelectedItem().getValue());
        }
        component = lstCell.get(locationMaxIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxLocationMax = (Combobox) component;
            price.setLocationMax(cbxLocationMax.getSelectedItem().getValue());
        }
        component = lstCell.get(locationIndex).getFirstChild();
        if (component != null && component instanceof Doublebox) {
            dbbLocation = (Doublebox) component;
            price.setPriceLocation(dbbLocation.getValue());
        }
        component = lstCell.get(convertTypeIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxConvertType = (Combobox) component;
            price.setConvertType(cbxConvertType.getSelectedItem().getValue());
        }
        component = lstCell.get(convertValueIndex).getFirstChild();
        if (component != null && component instanceof Doublebox) {
            dbbConvertValue = (Doublebox) component;
            price.setConvertValue(dbbConvertValue.getValue());
        }
        return price;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        listDataModelPrice = new ListModelList(contractService.getPriceByContract(lngContractId));
        lstPrice.setModel(listDataModelPrice);
        setDataDefaultInGrid();
    }

    private void setDataDefaultInGrid() {
        lstPrice.renderAll();
        List<Component> lstRows = lstPrice.getRows().getChildren();
        if (lstRows != null && !lstRows.isEmpty()) {
            for (int i = 0; i < lstRows.size(); i++) {
                Price price = listDataModelPrice.get(i);
                Component row = lstRows.get(i);
                List<Component> lstCell = row.getChildren();
                setComboboxParam(lstCell, getParamDefault(price.getPumpType(), pumpTypeIndex), pumpTypeIndex);
                setComboboxParam(lstCell, getParamDefault(price.getLocationType(), locationTypeIndex), locationTypeIndex);
                setComboboxParam(lstCell, getParamDefault(price.getConvertType(), convertTypeIndex), convertTypeIndex);
                setComboboxLocation(lstCell, getLocatoionDefault(price.getLocationMin(), locationMinIndex), locationMinIndex);
                setComboboxLocation(lstCell, getLocatoionDefault(price.getLocationMax(), locationMaxIndex), locationMaxIndex);
                setComboboxPump(lstCell, getPumpDefault(price.getPumpId()), pumpIndex);
            }
        }
    }

    private List<Param> getParamDefault(Long paramValue, int type) {
        List<Param> paramSelected = new ArrayList<>();
        List<Param> lstParam = null;
        switch (type) {
            case pumpTypeIndex:
                lstParam = lstPumpType;
                break;
            case locationTypeIndex:
                lstParam = lstLocationType;
                break;
            case convertTypeIndex:
                lstParam = lstConvertType;
                break;
            default:
                break;
        }
        if (paramValue != null && lstParam != null && !lstParam.isEmpty()) {
            for (Param vParam : lstParam) {
                if (paramValue.equals(vParam.getParamValue())) {
                    paramSelected.add(vParam);
                    break;
                }
            }
        }
        if (paramSelected.isEmpty()) {
            paramSelected.add(defaultParam);
        }
        return paramSelected;
    }

    private List<Location> getLocatoionDefault(Long locationId, int type) {
        List<Location> locationSelected = new ArrayList<>();
        List<Location> lstLocation = null;
        switch (type) {
            case locationMinIndex:
                lstLocation = lstLocationMin;
                break;
            case locationMaxIndex:
                lstLocation = lstLocationMax;
                break;
            default:
                break;
        }
        if (locationId != null && lstLocation != null && !lstLocation.isEmpty()) {
            for (Location vLocation : lstLocation) {
                if (locationId.equals(vLocation.getLocationID())) {
                    locationSelected.add(vLocation);
                    break;
                }
            }
        }
        if (locationSelected.isEmpty()) {
            locationSelected.add(defaultLocation);
        }
        return locationSelected;
    }
    private List<Pumps> getPumpDefault(Long pumpId) {
        List<Pumps> pumpSelected = new ArrayList<>();
        if (pumpId != null && lstPumps != null && !lstPumps.isEmpty()) {
            for (Pumps vPump : lstPumps) {
                if (pumpId.equals(vPump.getPumpsID())) {
                    pumpSelected.add(vPump);
                    break;
                }
            }
        }
        if (pumpSelected.isEmpty()) {
            pumpSelected.add(defaultPump);
        }
        return pumpSelected;
    }

    private void setComboboxParam(List<Component> lstCell, List<Param> selectedIndex, int columnIndex) {
        Combobox cbxParam = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        List<Param> lstParam = null;
        switch (columnIndex) {
            case pumpTypeIndex:
                lstParam = lstPumpType;
                break;
            case locationTypeIndex:
                lstParam = lstLocationType;
                break;
            case convertTypeIndex:
                lstParam = lstConvertType;
                break;
            default:
                break;
        }
        if (component != null && component instanceof Combobox) {
            cbxParam = (Combobox) component;
            ListModelList listDataModelParam = new ListModelList(lstParam);
            listDataModelParam.setSelection(selectedIndex);
            cbxParam.setModel(listDataModelParam);
            cbxParam.setTooltiptext(selectedIndex.get(Constants.FIRST_INDEX).getParamName());
        }
    }

    private void setComboboxLocation(List<Component> lstCell, List<Location> selectedIndex, int columnIndex) {
        Combobox cbxLocation = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        List<Location> lstLocation = null;
        switch (columnIndex) {
            case locationMinIndex:
                lstLocation = lstLocationMin;
                break;
            case locationMaxIndex:
                lstLocation = lstLocationMax;
                break;
            default:
                break;
        }
        if (component != null && component instanceof Combobox) {
            cbxLocation = (Combobox) component;
            ListModelList listDataModelParam = new ListModelList(lstLocation);
            listDataModelParam.setSelection(selectedIndex);
            cbxLocation.setModel(listDataModelParam);
            cbxLocation.setTooltiptext(selectedIndex.get(Constants.FIRST_INDEX).getLocationName());
        }
    }

    private void setComboboxPump(List<Component> lstCell, List<Pumps> selectedIndex, int columnIndex) {
        Combobox cbxPump = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxPump = (Combobox) component;
            ListModelList listDataModelParam = new ListModelList(lstPumps);
            listDataModelParam.setSelection(selectedIndex);
            cbxPump.setModel(listDataModelParam);
            cbxPump.setTooltiptext(selectedIndex.get(Constants.FIRST_INDEX).getPumpsName());
        }
    }
}
