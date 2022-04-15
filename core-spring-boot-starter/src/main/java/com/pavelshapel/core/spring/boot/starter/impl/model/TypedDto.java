package com.pavelshapel.core.spring.boot.starter.impl.model;

import com.pavelshapel.core.spring.boot.starter.api.model.Typed;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TypedDto extends LinkedHashMap<String, Object> implements Typed<String> {
    public TypedDto(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getType() {
        return get(TYPE_FIELD).toString();
    }

    @Override
    public void setType(String type) {
        put(TYPE_FIELD, type);
    }
}