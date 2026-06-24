package com.pavelshapel.starter.boot.spring.bot.api.service.extractor;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.Context;
import com.pavelshapel.starter.boot.spring.ordered.OrderedComponent;

public abstract class ContextExtractor<PAYLOAD> extends OrderedComponent<PAYLOAD, Context> {}
