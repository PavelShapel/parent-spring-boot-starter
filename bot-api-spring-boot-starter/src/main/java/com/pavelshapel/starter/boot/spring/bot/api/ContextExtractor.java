package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.Context;
import com.pavelshapel.starter.boot.spring.ordered.OrderedComponent;

public abstract class ContextExtractor<P> extends OrderedComponent<P, Context> {}
