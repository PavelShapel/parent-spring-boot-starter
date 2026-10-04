package com.pavelshapel.starter.boot.spring.ordered;

public record OrderedResult<RESULT>(String orderedComponentId, RESULT result) {}
