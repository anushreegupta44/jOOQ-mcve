package org.jooq.mcve.controller;

import org.jooq.mcve.service.FeatureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeatureController {
  FeatureService featureService;

  public FeatureController(FeatureService featureService) {
    this.featureService = featureService;
  }

  @GetMapping("/feature")
  public void addFeature() {
    List<Integer> featureList = List.of(1, 2);
    featureService.addFeature(featureList);
  }
}
