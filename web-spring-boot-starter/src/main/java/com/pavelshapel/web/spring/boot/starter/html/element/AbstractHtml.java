package com.pavelshapel.web.spring.boot.starter.html.element;

import com.pavelshapel.web.spring.boot.starter.html.factory.impl.HtmlFactories;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractHtml implements Html {
    public static final String DOC_TYPE_HTML = "<!doctype html>";
    public static final String BODY = "body";
    public static final String TITLE = "title";
    public static final String META = "meta";
    public static final String CHARSET = "charset";
    public static final String HEAD = "head";
    public static final String HTML = "html";
    public static final String H1 = "h1";

    @Autowired
    @Getter
    private HtmlFactories htmlFactories;

    @Value("${spring.application.name}")
    @Getter
    private String applicationName;
}
