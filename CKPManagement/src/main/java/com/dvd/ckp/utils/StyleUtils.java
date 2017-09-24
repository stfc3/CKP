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
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Longbox;
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
     * @param numberAction
     */
    public static void setEnableComponent(List<Component> lstCell, int numberAction) {
        if (lstCell != null && !lstCell.isEmpty()) {
            for (Component c : lstCell) {
                if (c instanceof Cell) {
                    Component child = c.getFirstChild();
                    if (child instanceof Combobox) {
                        ((Combobox) child).setButtonVisible(true);
                        ((Combobox) child).setInplace(false);
                        ((Combobox) child).setReadonly(false);
                    } else if (child instanceof Datebox) {
                        ((Datebox) child).setButtonVisible(true);
                        ((Datebox) child).setInplace(false);
                        ((Datebox) child).setReadonly(false);
                    } else if (child instanceof Doublebox) {
                        ((Doublebox) child).setReadonly(false);
                        ((Doublebox) child).setInplace(false);
                    }else if (child instanceof Intbox) {
                        ((Intbox) child).setReadonly(false);
                        ((Intbox) child).setInplace(false);
                    }else if (child instanceof Longbox) {
                        ((Longbox) child).setReadonly(false);
                        ((Longbox) child).setInplace(false);
                    }else if (child instanceof Textbox) {
                        ((Textbox) child).setReadonly(false);
                        ((Textbox) child).setInplace(false);
                    }else if (child instanceof A && c.getChildren().size()==2) {
                        Button btn = (Button) c.getChildren().get(1);
                        btn.setDisabled(false);
                    } else if (child instanceof A && c.getChildren().size()>=4) {
                        A edit;
                        A delete;
                        A save;
                        A cancel;
                        A detail;
                        A view;
                        A reset;
                        // edit, delete, save, cancel
                        switch (numberAction) {
                            case 4:
                                edit = (A) child;
                                delete = (A) c.getChildren().get(1);
                                save = (A) c.getChildren().get(2);
                                cancel = (A) c.getChildren().get(3);
                                
                                edit.setVisible(false);
                                delete.setVisible(false);
                                
                                save.setVisible(true);
                                cancel.setVisible(true);
                                break;
                            case 5:
                                edit = (A) child;
                                delete = (A) c.getChildren().get(1);
                                save = (A) c.getChildren().get(2);
                                cancel = (A) c.getChildren().get(3);
                                reset = (A) c.getChildren().get(4);
                                
                                edit.setVisible(false);
                                delete.setVisible(false);
                                reset.setVisible(false);
                                
                                save.setVisible(true);
                                cancel.setVisible(true);
                                break;
                            case 6:
                                edit = (A) child;
                                delete = (A) c.getChildren().get(1);
                                save = (A) c.getChildren().get(2);
                                cancel = (A) c.getChildren().get(3);
                                detail = (A) c.getChildren().get(4);
                                view = (A) c.getChildren().get(5);
                                
                                edit.setVisible(false);
                                delete.setVisible(false);
                                view.setVisible(false);
                                
                                save.setVisible(true);
                                cancel.setVisible(true);
                                detail.setVisible(true);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Set style disable edit
     *
     * @param lstCell
     * @param numberAction
     */
    public static void setDisableComponent(List<Component> lstCell, int numberAction) {
        if (lstCell != null && !lstCell.isEmpty()) {
            for (Component c : lstCell) {
                if (c instanceof Cell) {
                    Component child = c.getFirstChild();
                    if (child instanceof Combobox) {
                        ((Combobox) child).setButtonVisible(false);
                        ((Combobox) child).setInplace(true);
                        ((Combobox) child).setReadonly(true);
                    } else if (child instanceof Datebox) {
                        ((Datebox) child).setButtonVisible(false);
                        ((Datebox) child).setInplace(true);
                        ((Datebox) child).setReadonly(true);
                    }else if (child instanceof Doublebox) {
                        ((Doublebox) child).setReadonly(true);
                        ((Doublebox) child).setInplace(true);
                    }else if (child instanceof Intbox) {
                        ((Intbox) child).setReadonly(true);
                        ((Intbox) child).setInplace(true);
                    }else if (child instanceof Longbox) {
                        ((Longbox) child).setReadonly(true);
                        ((Longbox) child).setInplace(true);
                    } else if (child instanceof Textbox) {
                        ((Textbox) child).setReadonly(true);
                        ((Textbox) child).setInplace(true);
                    }else if (child instanceof A && c.getChildren().size()==2) {
                        Button btn = (Button) c.getChildren().get(1);
                        btn.setDisabled(true);
                    } else if (child instanceof A && c.getChildren().size()>=4) {
                        A edit;
                        A delete;
                        A save;
                        A cancel;
                        A detail;
                        A view;
                        A reset;
                        // edit, delete, save, cancel
                        switch (numberAction) {
                            case 4:
                                edit = (A) child;
                                delete = (A) c.getChildren().get(1);
                                save = (A) c.getChildren().get(2);
                                cancel = (A) c.getChildren().get(3);
                                
                                edit.setVisible(false);
                                delete.setVisible(false);
                                
                                save.setVisible(true);
                                cancel.setVisible(true);
                                break;
                            case 5:
                                edit = (A) child;
                                delete = (A) c.getChildren().get(1);
                                save = (A) c.getChildren().get(2);
                                cancel = (A) c.getChildren().get(3);
                                reset = (A) c.getChildren().get(4);
                                
                                edit.setVisible(false);
                                delete.setVisible(false);
                                reset.setVisible(false);
                                
                                save.setVisible(true);
                                cancel.setVisible(true);
                                break;
                            case 6:
                                edit = (A) child;
                                delete = (A) c.getChildren().get(1);
                                save = (A) c.getChildren().get(2);
                                cancel = (A) c.getChildren().get(3);
                                detail = (A) c.getChildren().get(4);
                                view = (A) c.getChildren().get(5);
                                
                                edit.setVisible(false);
                                delete.setVisible(false);
                                view.setVisible(false);
                                
                                save.setVisible(true);
                                cancel.setVisible(true);
                                detail.setVisible(true);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }
    
}
