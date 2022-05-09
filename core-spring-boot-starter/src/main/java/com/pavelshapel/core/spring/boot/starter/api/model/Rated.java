package com.pavelshapel.core.spring.boot.starter.api.model;

import java.time.LocalDate;

public interface Rated {
    String DATE_FIELD = "date";
    String ABBREVIATION_FIELD = "abbreviation";
    String VOLUME_FIELD = "volume";

    LocalDate getDate();

    void setDate(LocalDate date);

    String getAbbreviation();

    void setAbbreviation(String abbreviation);

    Double getVolume();

    void setVolume(Double volume);
}
