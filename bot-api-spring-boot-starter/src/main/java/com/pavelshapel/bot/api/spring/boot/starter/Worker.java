package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.ContextRegistry;
import java.util.function.Consumer;

public abstract class Worker implements Consumer<ContextRegistry> {}
