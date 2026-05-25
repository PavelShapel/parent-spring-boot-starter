package com.pavelshapel.bot.telegram.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.PayloadConsumer;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionTemplate;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

final class TelegramPayloadConsumer
    extends PayloadConsumer<Update, TelegramContextExtractorsProcessor>
    implements LongPollingSingleThreadUpdateConsumer {

  TelegramPayloadConsumer(
      TransactionTemplate transactionTemplate,
      ApplicationEventPublisher events,
      TelegramContextExtractorsProcessor contextExtractorsProcessor,
      Logger logger) {
    super(transactionTemplate, events, contextExtractorsProcessor, logger);
  }

  @Override
  public void consume(Update update) {
    consumeRawPayload(update, update.getUpdateId().toString());
  }
}
