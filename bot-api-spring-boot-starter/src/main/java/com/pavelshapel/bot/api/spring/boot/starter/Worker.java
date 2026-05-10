package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.WorkerContext;
import com.pavelshapel.ordered.spring.boot.starter.OrderedComponent;

public abstract class Worker extends OrderedComponent<WorkerContext, Void> {}
