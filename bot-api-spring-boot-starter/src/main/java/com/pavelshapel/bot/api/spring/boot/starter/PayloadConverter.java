package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.WorkerContext;
import org.springframework.core.convert.converter.Converter;

public interface PayloadConverter<P> extends Converter<P, WorkerContext> {}
