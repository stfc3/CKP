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
import com.dvd.ckp.business.service.DistributeService;
import com.dvd.ckp.business.service.LocationServices;
import com.dvd.ckp.business.service.PumpServices;
import com.dvd.ckp.business.service.UtilsService;
import com.dvd.ckp.domain.Distribute;
import com.dvd.ckp.domain.Location;
import com.dvd.ckp.domain.Param;
import com.dvd.ckp.domain.Price;
import com.dvd.ckp.domain.Pumps;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StyleUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Window;

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
    @WireVariable
    protected DistributeService distributeService;
    @Wire
    private Grid lstPrice;
    @Wire
    private Grid lstPriceDistribute;
    @Wire
    private Longbox lgbContractId;
    @Wire
    private Window mainPrice;

    ListModelList<Price> listDataModelPrice;
    ListModelList<Price> listDataModelPriceDistribute;

    Param defaultParam;
    List<Param> lstPumpType;
    
    Distribute defaultDistribute;
    List<Distribute> lstDistributes;

    List<Param> lstLocationType;

    List<Param> lstConvertType;

    Pumps defaultPump;
    List<Pumps> lstPumps;

    Location defaultLocation;
    List<Location> lstLocationMin;

    List<Location> lstLocationMax;

    /// index price
    private final int pumpTypeIndex = 1;
//    private final int pumpIndex = 2;
    private final int m3Index = 2;
    private final int shiftIndex = 3;
    private final int waitIndex = 4;
    private final int switchIndex = 5;
    private final int convertTypeIndex = 6;
    private final int convertValueIndex = 7;

    private Long lngContractId;
    private boolean isAdd;
    
    private final Integer pricePump=1;
    private final Integer priceDistribute=2;
    ///

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        context = Sessions.getCurrent().getWebApp().getServletContext();
        contractService = (ContractService) SpringUtil.getBean(SpringConstant.CONTRACT_SERVICES);
        utilsService = (UtilsService) SpringUtil.getBean(SpringConstant.UTILS_SERVICES);
        pumpService = (PumpServices) SpringUtil.getBean(SpringConstant.PUMPS_SERVICES);
        locationService = (LocationServices) SpringUtil.getBean(SpringConstant.LOCATION_SERVICES);
        distributeService = (DistributeService) SpringUtil.getBean(SpringConstant.DISTRIBUTE_SERVICES);

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
        //list can phan phoi
        defaultDistribute = new Distribute();
        defaultDistribute.setDistributeId(Constants.DEFAULT_ID);
        defaultDistribute.setDistributeName(Labels.getLabel("option"));
        lstDistributes = distributeService.getDistributeActive();
        if (lstDistributes == null) {
            lstDistributes = new ArrayList<>();
        }
        lstDistributes.add(Constants.FIRST_INDEX, defaultDistribute);

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
        lngContractId = lgbContractId.getValue();
        if (lgbContractId != null) {
            vlstPrice = contractService.getPriceByContract(lngContractId, pricePump);
        }
        listDataModelPrice = new ListModelList<>(vlstPrice);
        List<Price> vlstPriceDistribute = new ArrayList<>();
        if (lgbContractId != null) {
            vlstPriceDistribute = contractService.getPriceByContract(lngContractId, priceDistribute);
        }
        listDataModelPriceDistribute = new ListModelList<>(vlstPriceDistribute);

        lstPrice.setModel(listDataModelPrice);
        lstPriceDistribute.setModel(listDataModelPriceDistribute);
        setDataDefaultInGrid();
        setDataDefaultInGridDistribute();
//        price
    }

    /**
     * Edit row
     *
     * @param event
     */
    public void onEdit(ForwardEvent event) {
        isAdd = false;
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Price price = rowSelected.getValue();
        setComboboxParam(lstCell, getParamDefault(price.getPumpType(), pumpTypeIndex), pumpTypeIndex);
        setComboboxParam(lstCell, getParamDefault(price.getConvertType(), convertTypeIndex), convertTypeIndex);
        StyleUtils.setEnableComponent(lstCell, 4);
    }
    /**
     * Edit row
     *
     * @param event
     */
    public void onEditDistribute(ForwardEvent event) {
        isAdd = false;
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Price price = rowSelected.getValue();
        setComboboxDistribute(lstCell, getDistributeDefault(price.getPumpType()), pumpTypeIndex);
        StyleUtils.setEnableComponent(lstCell, 4);
    }

    /**
     * Cancel
     *
     * @param event
     */
    public void onCancel(ForwardEvent event) {

        isAdd = false;
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid();

    }
    /**
     * Cancel
     *
     * @param event
     */
    public void onCancelDistribute(ForwardEvent event) {

        isAdd = false;
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGridDistribute();

    }

    /**
     * Save
     *
     * @param event
     */
    public void onSave(ForwardEvent event) {
        isAdd = false;
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Price price = rowSelected.getValue();
        getDataInRow(lstCell, price);
        price.setContractId(lngContractId);
        price.setPriceType(pricePump);
        price.setStatus(Constants.STATUS_ACTIVE);
        price.setCreateDate(new Date());
        contractService.insertOrUpdatePrice(price);
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid();
    }
    public void onSaveDistribute(ForwardEvent event) {
        isAdd = false;
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Price price = rowSelected.getValue();
        getDataInRowDistribute(lstCell, price);
        price.setContractId(lngContractId);
        price.setPriceType(priceDistribute);
        price.setStatus(Constants.STATUS_ACTIVE);
        price.setCreateDate(new Date());
        contractService.insertOrUpdatePrice(price);
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGridDistribute();
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
    public void onDeleteDistribute(ForwardEvent event) {
        Messagebox.show(Labels.getLabel("message.confirm.delete.content"), Labels.getLabel("message.confirm.delete.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event e) {
                if (Messagebox.ON_YES.equals(e.getName())) {
                    Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
                    Price price = rowSelected.getValue();
                    price.setStatus(Constants.STATUS_INACTIVE);
                    price.setCreateDate(new Date());
                    contractService.insertOrUpdatePrice(price);
                    reloadGridDistribute();
                }
            }
        });
    }

    /**
     * Add row
     */
    public void onClick$add() {
        if (!isAdd) {
            isAdd = true;
            Price price = new Price();
            listDataModelPrice.add(Constants.FIRST_INDEX, price);
            lstPrice.setModel(listDataModelPrice);
            lstPrice.renderAll();
            List<Component> lstCell = lstPrice.getRows().getFirstChild().getChildren();
            setDataDefaultInGrid();
            StyleUtils.setEnableComponent(lstCell, 4);
        }
    }
    /**
     * Add row
     */
    public void onClick$addDistribute() {
        if (!isAdd) {
            isAdd = true;
            Price price = new Price();
            listDataModelPriceDistribute.add(Constants.FIRST_INDEX, price);
            lstPriceDistribute.setModel(listDataModelPriceDistribute);
            lstPriceDistribute.renderAll();
            List<Component> lstCell = lstPriceDistribute.getRows().getFirstChild().getChildren();
            setDataDefaultInGridDistribute();
            StyleUtils.setEnableComponent(lstCell, 4);
        }
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private void getDataInRow(List<Component> lstCell, Price price) {
        Component component;
        Combobox cbxPumpType = null;
        Doublebox dbbM3 = null;
        Doublebox dbbShift = null;
        Doublebox dbbWait = null;
        Doublebox dbbSwitch = null;
        Combobox cbxConvertType = null;
        Doublebox dbbConvertValue = null;
        component = lstCell.get(pumpTypeIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxPumpType = (Combobox) component;
            price.setPumpType(cbxPumpType.getSelectedItem().getValue());
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
        component = lstCell.get(switchIndex).getFirstChild();
        if (component != null && component instanceof Doublebox) {
            dbbSwitch = (Doublebox) component;
            price.setPriceSwitch(dbbSwitch.getValue());
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
    }
    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private void getDataInRowDistribute(List<Component> lstCell, Price price) {
        Component component;
        Combobox cbxPumpType = null;
        Doublebox dbbRent = null;
        component = lstCell.get(pumpTypeIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxPumpType = (Combobox) component;
            price.setPumpType(cbxPumpType.getSelectedItem().getValue());
        }
        component = lstCell.get(m3Index).getFirstChild();
        if (component != null && component instanceof Doublebox) {
            dbbRent = (Doublebox) component;
            price.setPriceRent(dbbRent.getValue());
        }
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        listDataModelPrice = new ListModelList(contractService.getPriceByContract(lngContractId, pricePump));
        lstPrice.setModel(listDataModelPrice);
        setDataDefaultInGrid();
    }
    /**
     * Reload grid distribute
     */
    private void reloadGridDistribute() {
        listDataModelPriceDistribute = new ListModelList(contractService.getPriceByContract(lngContractId, priceDistribute));
        lstPriceDistribute.setModel(listDataModelPriceDistribute);
        setDataDefaultInGridDistribute();
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
                setComboboxParam(lstCell, getParamDefault(price.getConvertType(), convertTypeIndex), convertTypeIndex);
            }
        }
    }
    private void setDataDefaultInGridDistribute() {
        lstPriceDistribute.renderAll();
        List<Component> lstRows = lstPriceDistribute.getRows().getChildren();
        if (lstRows != null && !lstRows.isEmpty()) {
            for (int i = 0; i < lstRows.size(); i++) {
                Price price = listDataModelPriceDistribute.get(i);
                Component row = lstRows.get(i);
                List<Component> lstCell = row.getChildren();
                setComboboxDistribute(lstCell, getDistributeDefault(price.getPumpType()), pumpTypeIndex);
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

    private void setComboboxParam(List<Component> lstCell, List<Param> selectedIndex, int columnIndex) {
        Combobox cbxParam = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        List<Param> lstParam = null;
        switch (columnIndex) {
            case pumpTypeIndex:
                lstParam = lstPumpType;
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
    
    private void setComboboxDistribute(List<Component> lstCell, List<Distribute> selectedIndex, int columnIndex) {
        Combobox cbxDistribute = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxDistribute = (Combobox) component;
            ListModelList listDataModelDistribute = new ListModelList(lstDistributes);
            listDataModelDistribute.setSelection(selectedIndex);
            cbxDistribute.setModel(listDataModelDistribute);
            cbxDistribute.setTooltiptext(selectedIndex.get(Constants.FIRST_INDEX).getDistributeName());
        }

    }
    
    private List<Distribute> getDistributeDefault(Long distributeId) {
        List<Distribute> distributeSelected = new ArrayList<>();
        if (distributeId != null && lstDistributes != null && !lstDistributes.isEmpty()) {
            for (Distribute distribute : lstDistributes) {
                if (distributeId.equals(distribute.getDistributeId())) {
                    distributeSelected.add(distribute);
                    break;
                }
            }
        }
        if (distributeSelected.isEmpty()) {
            distributeSelected.add(defaultDistribute);
        }
        return distributeSelected;
    }


    public void onPriceLocation(ForwardEvent event) {
        Map<String, Object> arguments = new HashMap<>();
        Price price;
        Long vlngPriceId = null;
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        price = rowSelected.getValue();
        List<Component> lstCell = rowSelected.getChildren();
        getDataInRow(lstCell, price);
        price.setContractId(lngContractId);
        price.setStatus(Constants.STATUS_ACTIVE);
        price.setCreateDate(new Date());
        contractService.insertOrUpdatePrice(price);
        if (isAdd) {
            vlngPriceId = utilsService.getId().longValue();
        } else {
            vlngPriceId = price.getPriceId();

        }
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid();
        arguments.put("priceId", vlngPriceId);
        Window winAddUser = (Window) Executions.createComponents(
                "/manager/include/priceLocation.zul", mainPrice, arguments);

        winAddUser.setBorder(true);
        winAddUser.setBorder("normal");
        winAddUser.setClosable(true);

        winAddUser.doModal();
    }
}
