package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.formatter.currency;

/**
 * Format provided currency;
 *
 * @author robertnorthard
 */
public interface CurrencyFormatter {

    /**
     * Return currency.
     *
     * @param currency to convert
     * @return currency formatted as currency.
     */
    public String format(double currency);

}
