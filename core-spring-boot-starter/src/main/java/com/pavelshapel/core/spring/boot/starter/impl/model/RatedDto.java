package com.pavelshapel.core.spring.boot.starter.impl.model;

import com.pavelshapel.core.spring.boot.starter.api.model.Rated;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatedDto implements Rated {
    LocalDate date;
    String abbreviation;
    Double volume;
}