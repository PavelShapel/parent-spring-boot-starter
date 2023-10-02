package com.pavelshapel.retry.spring.boot.starter.transaction;


import com.pavelshapel.retry.spring.boot.starter.RetryStarterAutoConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        RetryStarterAutoConfiguration.class
})
class TransactionExecutorTest {
    @SpyBean
    private TransactionExecutor transactionExecutor;
    private RecoveredTransaction firstRecoveredTransaction;
    private RecoveredTransaction secondRecoveredTransaction;
    private RecoveredTransaction exceptionRecoveredTransaction;

    @BeforeEach
    void setUp() {
        Runnable firstSucceedRunnable = this::startFakeTransaction;
        Transaction firstSuccessTransaction = new Transaction(firstSucceedRunnable, "first success");
        Runnable firstRollBackRunnable = this::startFakeTransaction;
        Transaction firstRollBackTransaction = new Transaction(firstRollBackRunnable, "first rollback");
        firstRecoveredTransaction = new RecoveredTransaction(firstSuccessTransaction, firstRollBackTransaction);

        Runnable secondSucceedRunnable = this::startFakeTransaction;
        Transaction secondSuccessTransaction = new Transaction(secondSucceedRunnable, "second success");
        Runnable seconRollBackRunnable = this::startFakeTransaction;
        Transaction secondRollBackTransaction = new Transaction(seconRollBackRunnable, "second rollback");
        secondRecoveredTransaction = new RecoveredTransaction(secondSuccessTransaction, secondRollBackTransaction);

        Runnable exceptionRunnable = this::throwException;
        Transaction exceptionTransaction = new Transaction(exceptionRunnable, "exception success");
        Runnable rollBackExceptionRunnable = this::startFakeTransaction;
        Transaction rollBackExceptionTransaction = new Transaction(rollBackExceptionRunnable, "exception rollback");
        exceptionRecoveredTransaction = new RecoveredTransaction(exceptionTransaction, rollBackExceptionTransaction);
    }

    @Test
    void startTransactions_WithValidTransactions_ShouldSetIsSucceedTrue() {
        var transactions = List.of(firstRecoveredTransaction, secondRecoveredTransaction);

        transactionExecutor.startTransactions(transactions);

        transactions.stream()
                .map(RecoveredTransaction::succeedTransaction)
                .map(Transaction::isSucceed)
                .forEach(isSucceed -> assertThat(isSucceed).isTrue());
    }

    @Test
    void startTransactions_WithInvalidParameters_ShouldThrowException() {
        var transactions = List.of(firstRecoveredTransaction, exceptionRecoveredTransaction);

        assertThatThrownBy(() -> transactionExecutor.startTransactions(transactions))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void startTransactions_WithInvalidTransactions_ShouldSetIsSucceedFalse() {
        var transactions = List.of(firstRecoveredTransaction, exceptionRecoveredTransaction);

        assertThatThrownBy(() -> transactionExecutor.startTransactions(transactions))
                .isInstanceOf(RuntimeException.class);

        assertThat(exceptionRecoveredTransaction)
                .extracting(RecoveredTransaction::succeedTransaction)
                .extracting(Transaction::isSucceed)
                .isEqualTo(false);
    }

    @Test
    void startTransactions_WithFailedTransaction_ShouldSetIsSucceedTrueForRollBackSucceedTransaction() {
        var transactions = List.of(firstRecoveredTransaction, exceptionRecoveredTransaction);

        assertThatThrownBy(() -> transactionExecutor.startTransactions(transactions))
                .isInstanceOf(RuntimeException.class);

        assertThat(firstRecoveredTransaction)
                .extracting(RecoveredTransaction::rollBackTransaction)
                .extracting(Transaction::isSucceed)
                .isEqualTo(true);
    }

    private void throwException() {
        throw new RuntimeException("exception");
    }

    private void startFakeTransaction() {
        System.out.println("---transaction--->");
    }
}
