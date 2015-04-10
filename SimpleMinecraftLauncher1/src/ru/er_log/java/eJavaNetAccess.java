/*
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package ru.er_log.java;

public interface eJavaNetAccess {
    /**
     * return the URLClassPath belonging to the given loader
     */
    eURLClassPath geteURLClassPath (eURLClassLoader u);
}
