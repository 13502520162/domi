package com.domi.support.identification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * logger鍖呰鍣� 鍩虹logger
 * 
 * @author tgf(Jan 15, 2011)
 * 
 */
public class BzLogger {
    /** slf4j logger */
    protected Logger logger;

    public BzLogger(Class<?> clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    public BzLogger(String loggerName) {
        logger = LoggerFactory.getLogger(loggerName);
    }

    // =================debug==========================
    public void debug(String message, Object... args) {
        logger.debug(message, args);
        // hooker
        FormattingTuple tp = MessageFormatter.arrayFormat(message, args);
        handleDebug(tp.getMessage(), tp.getThrowable());
    }

    // =================info=============================
    public void info(String message, Object... args) {
        logger.info(message, args);
        // hooker
        FormattingTuple tp = MessageFormatter.arrayFormat(message, args);
        handleInfo(tp.getMessage(), tp.getThrowable());
    }

    // ==================warn===========================
    public void warn(String message, Object... args) {
        logger.warn(message, args);
        // hooker
        FormattingTuple tp = MessageFormatter.arrayFormat(message, args);
        handleWarn(tp.getMessage(), tp.getThrowable());
    }

    // ==================error============================
    public void error(String message, Object... args) {
        logger.error(message, args);
        // hooker
        FormattingTuple tp = MessageFormatter.arrayFormat(message, args);
        handleError(tp.getMessage(), tp.getThrowable());
    }

    // ========hooker, 鍚勭閿欒淇℃伅閽╁瓙, 鐢卞瓙绫诲疄鐜皁verride=============
    protected void handleWarn(String message, Throwable e) {
    }

    protected void handleDebug(String message, Throwable e) {
    }

    protected void handleError(String message, Throwable e) {
    }

    protected void handleInfo(String message, Throwable e) {
    }

    // =========isXXEnabled??
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }
}
