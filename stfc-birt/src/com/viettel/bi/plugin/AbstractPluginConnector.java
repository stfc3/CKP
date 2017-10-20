/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bi.plugin;

/**
 *
 * @author longh
 */
public abstract class AbstractPluginConnector {
    public abstract void startup() throws Throwable;
    public abstract void shutdown() throws Throwable;
}
