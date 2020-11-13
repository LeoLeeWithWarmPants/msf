package com.leolee.msf.config;

import lombok.Getter;

/**
 * @author HelloWoodes
 */

@Getter
public enum DataSourceKey {

    CLIENT,
    CLIENT2,
    /**
     * Order data source key.
     */
    ORDER,
    /**
     * Storage data source key.
     */
    STORAGE,
    /**
     * Pay data source key.
     */
    PAY,
}
