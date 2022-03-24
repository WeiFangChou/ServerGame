package com.eric;

import com.eric.gui.J_Main;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class EricLogger extends Logger {
    private static String className = null;

    public EricLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
        className = name;
    }

    public static EricLogger getLogger2(String name) {
        className = name;
        String classResourceBundleName = Logger.getLogger(name).getResourceBundleName();
        return new EricLogger(className, classResourceBundleName);
    }

    @Override // java.util.logging.Logger
    public synchronized void addHandler(Handler handler) throws SecurityException {
        super.addHandler(handler);
    }

    @Override // java.util.logging.Logger
    public void config(String msg) {
        super.config(msg);
    }

    @Override // java.util.logging.Logger
    public void entering(String sourceClass, String sourceMethod, Object param1) {
        super.entering(sourceClass, sourceMethod, param1);
    }

    @Override // java.util.logging.Logger
    public void entering(String sourceClass, String sourceMethod, Object[] params) {
        super.entering(sourceClass, sourceMethod, params);
    }

    public void entering(String sourceClass, String sourceMethod) {
        super.entering(sourceClass, sourceMethod);
    }

    public void exiting(String sourceClass, String sourceMethod, Object result) {
        super.exiting(sourceClass, sourceMethod, result);
    }

    public void exiting(String sourceClass, String sourceMethod) {
        super.exiting(sourceClass, sourceMethod);
    }

    @Override // java.util.logging.Logger
    public void fine(String msg) {
        super.fine(msg);
    }

    @Override // java.util.logging.Logger
    public void finer(String msg) {
        super.finer(msg);
    }

    @Override // java.util.logging.Logger
    public void finest(String msg) {
        super.finest(msg);
    }

    public Filter getFilter() {
        return super.getFilter();
    }

    public synchronized Handler[] getHandlers() {
        return super.getHandlers();
    }

    public Level getLevel() {
        return super.getLevel();
    }

    public String getName() {
        return super.getName();
    }

    public Logger getParent() {
        return super.getParent();
    }

    public ResourceBundle getResourceBundle() {
        return super.getResourceBundle();
    }

    public String getResourceBundleName() {
        return super.getResourceBundleName();
    }

    public synchronized boolean getUseParentHandlers() {
        return super.getUseParentHandlers();
    }

    @Override // java.util.logging.Logger
    public void info(String msg) {
        J_Main.getInstance().addConsol(String.valueOf(Calendar.getInstance().getTime().toString()) + " " + className);
        J_Main.getInstance().addConsol(msg);
        super.info(msg);
    }

    public boolean isLoggable(Level level) {
        return super.isLoggable(level);
    }

    @Override // java.util.logging.Logger
    public void log(Level level, String msg, Object param1) {
        super.log(level, msg, param1);
    }

    @Override // java.util.logging.Logger
    public void log(Level level, String msg, Object[] params) {
        super.log(level, msg, params);
    }

    @Override // java.util.logging.Logger
    public void log(Level level, String msg, Throwable thrown) {
        J_Main.getInstance().addConsol(String.valueOf(Calendar.getInstance().getTime().toString()) + " " + className);
        J_Main.getInstance().addConsol(level + ": " + msg + "\n" + thrown);
        super.log(level, msg, thrown);
    }

    @Override // java.util.logging.Logger
    public void log(Level level, String msg) {
        super.log(level, msg);
    }

    public void log(LogRecord record) {
        super.log(record);
    }

    @Override // java.util.logging.Logger
    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Object param1) {
        super.logp(level, sourceClass, sourceMethod, msg, param1);
    }

    @Override // java.util.logging.Logger
    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Object[] params) {
        super.logp(level, sourceClass, sourceMethod, msg, params);
    }

    @Override // java.util.logging.Logger
    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Throwable thrown) {
        super.logp(level, sourceClass, sourceMethod, msg, thrown);
    }

    @Override // java.util.logging.Logger
    public void logp(Level level, String sourceClass, String sourceMethod, String msg) {
        super.logp(level, sourceClass, sourceMethod, msg);
    }

    @Override // java.util.logging.Logger
    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object param1) {
        super.logrb(level, sourceClass, sourceMethod, bundleName, msg, param1);
    }

    @Override // java.util.logging.Logger
    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object[] params) {
        super.logrb(level, sourceClass, sourceMethod, bundleName, msg, params);
    }

    @Override // java.util.logging.Logger
    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Throwable thrown) {
        super.logrb(level, sourceClass, sourceMethod, bundleName, msg, thrown);
    }

    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg) {
        super.logrb(level, sourceClass, sourceMethod, bundleName, msg);
    }

    @Override // java.util.logging.Logger
    public synchronized void removeHandler(Handler handler) throws SecurityException {
        super.removeHandler(handler);
    }

    @Override // java.util.logging.Logger
    public void setFilter(Filter newFilter) throws SecurityException {
        super.setFilter(newFilter);
    }

    @Override // java.util.logging.Logger
    public void setLevel(Level newLevel) throws SecurityException {
        super.setLevel(newLevel);
    }

    public void setParent(Logger parent) {
        super.setParent(parent);
    }

    public synchronized void setUseParentHandlers(boolean useParentHandlers) {
        super.setUseParentHandlers(useParentHandlers);
    }

    @Override // java.util.logging.Logger
    public void severe(String msg) {
        super.severe(msg);
    }

    public void throwing(String sourceClass, String sourceMethod, Throwable thrown) {
        super.throwing(sourceClass, sourceMethod, thrown);
    }

    @Override // java.util.logging.Logger
    public void warning(String msg) {
        J_Main.getInstance().addConsol(String.valueOf(Calendar.getInstance().getTime().toString()) + " " + className);
        J_Main.getInstance().addConsol("Warning: " + msg);
        super.warning(msg);
    }

    /* access modifiers changed from: protected */
    @Override // java.lang.Object
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object arg0) {
        return super.equals(arg0);
    }

    /* access modifiers changed from: protected */
    @Override // java.lang.Object
    public void finalize() throws Throwable {
        super.finalize();
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return super.toString();
    }
}
