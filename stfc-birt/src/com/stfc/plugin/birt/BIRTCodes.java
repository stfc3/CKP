/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.plugin.birt;

/**
 *
 * @author minhca
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class BIRTCodes {
    //ControlType
    public static final int TEXT_BOX = 0;
    public static final int LIST_BOX = 1;
    public static final int RADIO_BUTTON = 2;
    public static final int CHECK_BOX = 3;
    public static final int AUTO_SUGGEST = 4;
    public static final int AUTO = 0;
    public static final int LEFT = 1;
    public static final int CENTER = 2;
    public static final int RIGHT = 3;
    //DataType
    public static final int TYPE_ANY = 0;
    public static final int TYPE_STRING = 1;
    public static final int TYPE_FLOAT = 2;
    public static final int TYPE_DECIMAL = 3;
    public static final int TYPE_DATE_TIME = 4;
    public static final int TYPE_BOOLEAN = 5;
    public static final int TYPE_INTEGER = 6;
    public static final int TYPE_DATE = 7;
    public static final int TYPE_TIME = 8;
    //SelectionListType
    public static final int SELECTION_LIST_NONE = 0;
    public static final int SELECTION_LIST_DYNAMIC = 1;
    public static final int SELECTION_LIST_STATIC = 2;
    //parameterType
    public static final int SCALAR_PARAMETER = 0;
    public static final int FILTER_PARAMETER = 1;
    public static final int LIST_PARAMETER = 2;
    public static final int TABLE_PARAMETER = 3;
    public static final int PARAMETER_GROUP = 4;
    public static final int CASCADING_PARAMETER_GROUP = 5;
    //Parameter Key
    public static final String KEY_PARAM_GROUP = "ParameterGroup";
    public static final String KEY_NAME = "Name";
    public static final String KEY_HELP_TEXT = "HelpText";
    public static final String KEY_DISPLAY_NAME = "DisplayName";
    public static final String KEY_DEFAULT_VALUE = "DefaultValue";
    public static final String KEY_DATA_TYPE = "DataType";
    public static final String KEY_DISPLAY_FORMAT = "DisplayFormat";
    public static final String KEY_TYPE_NAME = "TypeName";
    public static final String KEY_CONTROL_TYPE = "ControlType";
    public static final String KEY_PARAMETER_TYPE = "ParameterType";
    public static final String KEY_SELECTION_LIST_TYPE = "SelectionListType";
    public static final String KEY_SCALAR_PARAM_TYPE = "ScalarParamType";
    public static final String KEY_PROMPT_TEXT = "PromptText";
    public static final String KEY_HIDDEN = "Hidden";
    public static final String KEY_CONCEAL_ENTRY = "ConcealEntry";
    public static final String KEY_REQUIRED = "isRequired";
    public static final String KEY_SELECTION_LIST = "SelectionList";
    public static final String KEY_SCALAR_ORDER_IN_GROUP = "ScalarOrderInGroup";
    public static final String KEY_CACHED_LEVEL0_CAS_PARAM = "CachedLevel0CasParam";
}
