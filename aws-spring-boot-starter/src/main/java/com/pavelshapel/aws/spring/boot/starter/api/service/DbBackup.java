package com.pavelshapel.aws.spring.boot.starter.api.service;

public interface DbBackup {
    String backup(String bucket, String object);
}
