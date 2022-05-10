package com.pavelshapel.core.spring.boot.starter.api.model;

import java.time.LocalDate;

public interface Rated {
    String DATE_FIELD = "date";
    String ABBREVIATION_FIELD = "abbreviation";
    String AMOUNT_FIELD = "amount";
    String PRECISION_FIELD = "precision";

    LocalDate getDate();

    void setDate(LocalDate date);

    String getAbbreviation();

    void setAbbreviation(String abbreviation);

    double getAmount();

    void setAmount(double amount);

    int getPrecision();

    void setPrecision(int precision);
}
