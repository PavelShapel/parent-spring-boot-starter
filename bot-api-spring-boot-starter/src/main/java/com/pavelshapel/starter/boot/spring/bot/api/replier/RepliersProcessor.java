package com.pavelshapel.starter.boot.spring.bot.api.replier;

import com.pavelshapel.starter.boot.spring.ordered.OrderedComponentsProcessor;
import java.util.List;
import java.util.Objects;

public abstract class RepliersProcessor<PAYLOAD, REPLIER extends Replier<PAYLOAD, ?>>
    extends OrderedComponentsProcessor<PAYLOAD, String, REPLIER> {

  protected RepliersProcessor(List<REPLIER> repliers) {
    super(repliers);
  }

  public final String getApplicableReplierSimpleName(PAYLOAD payload) {
    return getSingle(payload).apply(payload);
  }

  public REPLIER getReplierByName(String simpleName) {
    return getSingle(
        /* payload= */ null,
        replier -> Objects.equals(simpleName, replier.getClass().getSimpleName()));
  }
}
