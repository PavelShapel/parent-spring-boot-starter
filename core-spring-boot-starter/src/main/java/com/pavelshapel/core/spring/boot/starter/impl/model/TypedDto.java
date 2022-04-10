package com.pavelshapel.core.spring.boot.starter.impl.model;

import com.pavelshapel.core.spring.boot.starter.api.model.Typed;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class TypedDto extends HashMap<String, Object> implements Typed<String> {
    @Override
    public String getType() {
        return get(TYPE_FIELD).toString();
    }

    @Override
    public void setType(String type) {
        put(TYPE_FIELD, type);
    }
}