package com.pavelshapel.bot.api.spring.boot.starter.model.context.worker.nested;

public record CallBackData(String className, String payload) implements NestedWorkerContext {}
