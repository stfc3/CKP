/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.utils;

import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;

/**
 *
 * @author dmin
 */
public class StyleUtils {
    
    /**
     * Set style enable edit
     *
     * @param lstCell
     */
    public static void setEnableComponent(List<Component> lstCell) {
        if (lstCell != null && !lstCell.isEmpty()) {
            for (Component c : lstCell) {
                if (c instanceof Cell) {
                    Component child = c.getFirstChild();
                    if (child instanceof Combobox) {
                        ((Combobox) child).setButtonVisible(true);
                        ((Combobox) child).setInplace(false);
                    } else if (child instanceof Datebox) {
                        ((Datebox) child).setButtonVisible(true);
                        ((Datebox) child).setInplace(false);
                    } else if (child instanceof Textbox) {
                        ((Textbox) child).setReadonly(false);
                        ((Textbox) child).setInplace(false);
                    }else if (child instanceof A && c.getChildren().size()==2) {
                        Button btn = (Button) c.getChildren().get(1);
                        btn.setDisabled(false);
                    } else if (child instanceof A && c.getChildren().size()>2) {
                        A edit = (A) child;
                        edit.setVisible(false);
                        A save = (A) c.getChildren().get(1);
                        A cancel = (A) c.getChildren().get(2);
                        save.setVisible(true);
                        cancel.setVisible(true);
                    }
                }
            }
        }
    }

    /**
     * Set style disable edit
     *
     * @param lstCell
     */
    public static void setDisableComponent(List<Component> lstCell) {
        if (lstCell != null && !lstCell.isEmpty()) {
            for (Component c : lstCell) {
                if (c instanceof Cell) {
                    Component child = c.getFirstChild();
                    if (child instanceof Combobox) {
                        ((Combobox) child).setButtonVisible(false);
                        ((Combobox) child).setInplace(true);
                    } else if (child instanceof Datebox) {
                        ((Datebox) child).setButtonVisible(false);
                        ((Datebox) child).setInplace(true);
                    } else if (child instanceof Textbox) {
                        ((Textbox) child).setReadonly(true);
                        ((Textbox) child).setInplace(true);
                    }else if (child instanceof A && c.getChildren().size()==2) {
                        Button btn = (Button) c.getChildren().get(1);
                        btn.setDisabled(true);
                    } else if (child instanceof A && c.getChildren().size()>2) {
                        A edit = (A) child;
                        edit.setVisible(true);
                        A save = (A) c.getChildren().get(1);
                        A cancel = (A) c.getChildren().get(2);
                        save.setVisible(false);
                        cancel.setVisible(false);
                    }
                }
            }
        }
    }
    
}
