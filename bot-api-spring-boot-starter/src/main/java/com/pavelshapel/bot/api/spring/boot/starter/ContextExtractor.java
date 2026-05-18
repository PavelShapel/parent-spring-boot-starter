package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.Context;
import com.pavelshapel.ordered.spring.boot.starter.OrderedComponent;

public abstract class ContextExtractor<P> extends OrderedComponent<P, Context> {}
