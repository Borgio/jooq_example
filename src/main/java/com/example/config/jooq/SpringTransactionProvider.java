package com.example.config.jooq;

import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED;

/**
 * An example <code>TransactionProvider</code> implementing the {@link TransactionProvider} contract
 * for use with Spring.
 *
 * @author Lukas Eder
 */
public class SpringTransactionProvider implements TransactionProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(SpringTransactionProvider.class);
  private final DataSourceTransactionManager dataSourceTransactionManager;

  public SpringTransactionProvider(final DataSourceTransactionManager
                                         dataSourceTransactionManager) {
    this.dataSourceTransactionManager = dataSourceTransactionManager;
  }

  @Override
  public void begin(TransactionContext ctx) {
    LOGGER.info("Begin transaction");
    TransactionStatus tx = dataSourceTransactionManager.getTransaction(new
          DefaultTransactionDefinition(PROPAGATION_NESTED));
    ctx.transaction(new SpringTransaction(tx));
  }

  @Override
  public void commit(TransactionContext ctx) {
    LOGGER.info("commit transaction");
    dataSourceTransactionManager.commit(((SpringTransaction) ctx.transaction()).tx);
  }

  @Override
  public void rollback(TransactionContext ctx) {
    LOGGER.info("rollback transaction");
    dataSourceTransactionManager.rollback(((SpringTransaction) ctx.transaction()).tx);
  }
}
