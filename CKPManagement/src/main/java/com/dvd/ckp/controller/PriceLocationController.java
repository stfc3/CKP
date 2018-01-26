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
import com.dvd.ckp.business.service.UtilsService;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.Location;
import com.dvd.ckp.domain.Param;
import com.dvd.ckp.domain.PriceLocation;
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
import org.zkoss.zul.Longbox;

/**
 *
 * @author dmin
 */
public class PriceLocationController extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(PriceLocationController.class);

    ServletContext context;

    @WireVariable
    protected ContractService contractService;
    @WireVariable
    protected UtilsService utilsService;
    @WireVariable
    protected LocationServices locationService;
    @Wire
    private Grid lstPriceLocation;
    @Wire
    private Longbox lgbPriceId;

    ListModelList<PriceLocation> listDataModelPriceLocation;

    Param defaultParam;

    List<Param> lstLocationType;
    Location defaultLocation;
    List<Location> lstLocationMin;

    List<Location> lstLocationMax;

    /// index price
    private final int locationTypeIndex = 1;
    private final int locationMinIndex = 2;
    private final int locationMaxIndex = 3;
    private final int locationIndex = 4;
    private final int locationShiftIndex = 5;

    private Long lngPriceId;
    ///

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        context = Sessions.getCurrent().getWebApp().getServletContext();
        contractService = (ContractService) SpringUtil.getBean(SpringConstant.CONTRACT_SERVICES);
        utilsService = (UtilsService) SpringUtil.getBean(SpringConstant.UTILS_SERVICES);
        locationService = (LocationServices) SpringUtil.getBean(SpringConstant.LOCATION_SERVICES);

//        price
        defaultParam = new Param();
        defaultParam.setParamValue(Constants.DEFAULT_ID);
        defaultParam.setParamName(Labels.getLabel("option"));

        //list loai vi tri
        lstLocationType = utilsService.getParamByKey(Constants.PRAM_LOCATION_TYPE);
        if (lstLocationType == null) {
            lstLocationType = new ArrayList<>();
        }
        lstLocationType.add(Constants.FIRST_INDEX, defaultParam);

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

        List<PriceLocation> vlstPriceLocations = new ArrayList<>();
        lngPriceId = lgbPriceId.getValue();
        if (lngPriceId != null) {
            vlstPriceLocations = contractService.getPriceLocationByPrice(lngPriceId);
        }
        listDataModelPriceLocation = new ListModelList<>(vlstPriceLocations);

        lstPriceLocation.setModel(listDataModelPriceLocation);
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
        PriceLocation priceLocation = rowSelected.getValue();
        setComboboxParam(lstCell, getParamDefault(priceLocation.getLocationType(), locationTypeIndex), locationTypeIndex);
        setComboboxLocation(lstCell, getLocatoionDefault(priceLocation.getLocationMin(), locationMinIndex), locationMinIndex);
        setComboboxLocation(lstCell, getLocatoionDefault(priceLocation.getLocationMax(), locationMaxIndex), locationMaxIndex);
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
        PriceLocation p = rowSelected.getValue();
        PriceLocation priceLocation = getDataInRow(lstCell);
        priceLocation.setPriceLocationId(p.getPriceLocationId());
        priceLocation.setPriceId(lngPriceId);
        priceLocation.setStatus(Constants.STATUS_ACTIVE);
        priceLocation.setCreateDate(new Date());
        contractService.insertOrUpdatePriceLocation(priceLocation);
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid();
    }

    public void onDelete(ForwardEvent event) {
        Messagebox.show(Labels.getLabel("message.confirm.delete.content"), Labels.getLabel("message.confirm.delete.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event e) {
                if (Messagebox.ON_YES.equals(e.getName())) {
                    Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
                    PriceLocation priceLocation = rowSelected.getValue();
                    priceLocation.setStatus(Constants.STATUS_INACTIVE);
                    priceLocation.setCreateDate(new Date());
                    contractService.insertOrUpdatePriceLocation(priceLocation);
                    reloadGrid();
                }
            }
        });
    }

    /**
     * Add row
     */
    public void onClick$add() {
        PriceLocation priceLocation = new PriceLocation();
        listDataModelPriceLocation.add(Constants.FIRST_INDEX, priceLocation);
        lstPriceLocation.setActivePage(Constants.FIRST_INDEX);
        lstPriceLocation.setModel(listDataModelPriceLocation);
        lstPriceLocation.renderAll();
        List<Component> lstCell = lstPriceLocation.getRows().getFirstChild().getChildren();
        setDataDefaultInGrid();
        StyleUtils.setEnableComponent(lstCell, 4);
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private PriceLocation getDataInRow(List<Component> lstCell) {
        PriceLocation priceLocation = new PriceLocation();
        Component component;
        Combobox cbxLocationType = null;
        Combobox cbxLocationMin = null;
        Combobox cbxLocationMax = null;
        Doublebox dbbLocation = null;
        Doublebox dbbLocationShift = null;
        component = lstCell.get(locationTypeIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxLocationType = (Combobox) component;
            priceLocation.setLocationType(cbxLocationType.getSelectedItem().getValue());
        }
        component = lstCell.get(locationMinIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxLocationMin = (Combobox) component;
            priceLocation.setLocationMin(cbxLocationMin.getSelectedItem().getValue());
        }
        component = lstCell.get(locationMaxIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxLocationMax = (Combobox) component;
            priceLocation.setLocationMax(cbxLocationMax.getSelectedItem().getValue());
        }
        component = lstCell.get(locationIndex).getFirstChild();
        if (component != null && component instanceof Doublebox) {
            dbbLocation = (Doublebox) component;
            priceLocation.setPriceLocation(dbbLocation.getValue());
        }
        component = lstCell.get(locationShiftIndex).getFirstChild();
        if (component != null && component instanceof Doublebox) {
            dbbLocationShift = (Doublebox) component;
            priceLocation.setPriceLocationShift(dbbLocationShift.getValue());
        }
        return priceLocation;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        listDataModelPriceLocation = new ListModelList(contractService.getPriceLocationByPrice(lngPriceId));
        lstPriceLocation.setModel(listDataModelPriceLocation);
        setDataDefaultInGrid();
    }

    private void setDataDefaultInGrid() {
        lstPriceLocation.renderAll();
        List<Component> lstRows = lstPriceLocation.getRows().getChildren();
        if (lstRows != null && !lstRows.isEmpty()) {
            for (int i = 0; i < lstRows.size(); i++) {
                PriceLocation priceLocation = listDataModelPriceLocation.get(i);
                Component row = lstRows.get(i);
                List<Component> lstCell = row.getChildren();
                setComboboxParam(lstCell, getParamDefault(priceLocation.getLocationType(), locationTypeIndex), locationTypeIndex);
                setComboboxLocation(lstCell, getLocatoionDefault(priceLocation.getLocationMin(), locationMinIndex), locationMinIndex);
                setComboboxLocation(lstCell, getLocatoionDefault(priceLocation.getLocationMax(), locationMaxIndex), locationMaxIndex);
            }
        }
    }

    private List<Param> getParamDefault(Long paramValue, int type) {
        List<Param> paramSelected = new ArrayList<>();
        List<Param> lstParam = null;
        switch (type) {
            case locationTypeIndex:
                lstParam = lstLocationType;
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

    private void setComboboxParam(List<Component> lstCell, List<Param> selectedIndex, int columnIndex) {
        Combobox cbxParam = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        List<Param> lstParam = null;
        switch (columnIndex) {
            case locationTypeIndex:
                lstParam = lstLocationType;
                break;
            default:
                break;
        }
        if (component != null && component instanceof Combobox) {
            cbxParam = (Combobox) component;
            MyListModel listDataModelParam = new MyListModel(lstParam);
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
            MyListModel listDataModelParam = new MyListModel(lstLocation);
            listDataModelParam.setSelection(selectedIndex);
            cbxLocation.setModel(listDataModelParam);
            cbxLocation.setTooltiptext(selectedIndex.get(Constants.FIRST_INDEX).getLocationName());
        }
    }

}
