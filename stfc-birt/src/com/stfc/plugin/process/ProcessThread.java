/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.plugin.process;

import org.apache.log4j.Logger;

/**
 *
 * @author trungtd3
 */
public abstract class ProcessThread implements Runnable {

    protected boolean running;
    protected String threadName;
    protected final Object lock = new Object();
    protected Logger logger;
    protected Thread t;

    public ProcessThread(String threadName) {
        this.threadName = threadName;
        this.logger = Logger.getLogger(threadName);
    }

    protected abstract void process();

    @Override
    public void run() {
        while (this.running) {
            try {
                process();
                Thread.yield();
                this.logger.info(this.threadName + "_" + Thread.currentThread().getName() + " is running");
            } catch (RuntimeException ex) {
                this.logger.error("catch runtime exception[" + ex.toString() + "]", ex);
            }
        }

    }

    public void start() {
        if (!this.running) {
            this.running = true;
            this.logger.info("starting " + this.threadName + " process...");
            this.t = new Thread(this);
            this.t.setName(this.threadName);
            this.t.start();
            this.logger.info(this.threadName + " process  is started");
        } else {
            this.logger.info(this.threadName + " process  is started");
        }
    }

    public void stop() {
        if (this.running) {


            this.logger.info("stop " + this.threadName + " process");
            this.running = false;
            synchronized (this.lock) {
                this.lock.notifyAll();
            }
            if (this.t != null) {
                this.t.interrupt();
            }
            try {
                if ((this.t != null) && (this.t.isAlive())) {
                    this.logger.info("waiting " + this.threadName + " process stop...");
                    this.t.join();
                }
            } catch (InterruptedException ex) {
                this.logger.error("stop process exception:" + ex);
            } finally {
                this.logger.info(this.threadName + " process is stopped");
            }
        }
    }

    public String getThreadName() {
        return this.threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
        this.logger = Logger.getLogger(threadName);
    }
}
