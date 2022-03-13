package com.pavelshapel.core.spring.boot.starter.bpp.annotation.autowired;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SelfAutowiredTester {
    @SelfAutowired
    private SelfAutowiredTester selfAutowiredTester;
}