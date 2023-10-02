package com.pavelshapel.retry.spring.boot.starter.transaction;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.function.Predicate.not;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

@Log
@Service
public class TransactionExecutor {
    private static final String LOG_START_TRANSACTIONS_PATTERN = "Start transactions";
    private static final String LOG_ROLLBACK_TRANSACTIONS_PATTERN = "Rollback transactions";
    private static final String LOG_TRYING_EXECUTE_TRANSACTION_PATTERN = "Trying execute transaction [{0}]";
    private static final String LOG_SUCCEED_PATTERN = "Transaction succeed [{0}]";
    private static final String LOG_FAILED_PATTERN = "Transaction failed [{0}]";

    @Retryable
    public void startTransactions(List<RecoveredTransaction> transactions) {
        log.info(LOG_START_TRANSACTIONS_PATTERN);
        transactions.stream()
                .filter(not(this::getSucceedTransactionStatus))
                .map(RecoveredTransaction::succeedTransaction)
                .map(this::log)
                .forEach(this::startTransaction);
    }

    @SneakyThrows
    @Recover
    protected void rollBackTransactions(Throwable throwable, List<RecoveredTransaction> transactions) {
        log.info(LOG_ROLLBACK_TRANSACTIONS_PATTERN);
        transactions.stream()
                .filter(this::getSucceedTransactionStatus)
                .map(RecoveredTransaction::rollBackTransaction)
                .map(this::log)
                .forEach(this::startTransaction);
        throw throwable;
    }

    private Transaction log(Transaction transaction) {
        log.log(INFO, LOG_TRYING_EXECUTE_TRANSACTION_PATTERN, transaction);
        return transaction;
    }

    private void startTransaction(Transaction transaction) {
        String description = transaction.getDescription();
        try {
            transaction.getTask().run();
            transaction.setSucceed(true);
            log.log(INFO, LOG_SUCCEED_PATTERN, description);
        } catch (Throwable throwable) {
            log.log(WARNING, LOG_FAILED_PATTERN, description);
            throw throwable;
        }
    }

    private boolean getSucceedTransactionStatus(RecoveredTransaction recoveredTransaction) {
        return recoveredTransaction.succeedTransaction().isSucceed();
    }
}
