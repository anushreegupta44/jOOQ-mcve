package org.jooq.mcve.service;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.mcve.tables.records.FeatureRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static org.jooq.mcve.Tables.BUG;
import static org.jooq.mcve.Tables.FEATURE;

@Service
public class FeatureService {
  public static final int UNIQUE_BUG_VALUE_TO_FAIL = 1;
  private final DSLContext dslContext;

  @Autowired
  public FeatureService(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public void addFeature(List<Integer> featureList) {
    List<CompletionStage<Void>> completionStages = new ArrayList<>();
    dslContext.transaction(
        configuration -> {
          for (Integer featureInt : featureList) {
            completionStages.add(saveFeatureAndBugs(configuration, featureInt));
          }
          for (CompletionStage<Void> completionStage : completionStages) {
            completionStage.toCompletableFuture().join();
          }
        });
  }

  private CompletionStage<Void> saveFeatureAndBugs(
      Configuration configuration, Integer featureValue) {
    return configuration
        .dsl()
        .transactionAsync(
            innerConfiguration -> {
              FeatureRecord featureRecord = this.saveFeature(featureValue, innerConfiguration);
              Integer featureRecordId = featureRecord.getId();
              this.sadlySavingBug(innerConfiguration, featureRecordId);
            });
  }

  private FeatureRecord saveFeature(Integer featureValue, Configuration configuration) {
    return configuration
        .dsl()
        .insertInto(FEATURE)
        .columns(FEATURE.VALUE)
        .values(featureValue)
        .returning(FEATURE.ID)
        .fetchOne();
  }

  private void sadlySavingBug(Configuration configuration, Integer featureId) {
    configuration
        .dsl()
        .insertInto(BUG)
        .columns(BUG.VALUE, BUG.FEATURE_ID)
        .values(
            UNIQUE_BUG_VALUE_TO_FAIL,
            featureId) // Insertion of 2nd record will fail due to unique key constraint
        .execute();
  }
}
