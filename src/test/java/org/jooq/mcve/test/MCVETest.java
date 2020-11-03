package org.jooq.mcve.test;

import org.jooq.DSLContext;
import org.jooq.Table;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.jooq.mcve.Tables.BUG;
import static org.jooq.mcve.Tables.FEATURE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MCVETest {
  @LocalServerPort private int port;
  @Autowired private TestRestTemplate restTemplate;
  @Autowired private DSLContext dslContext;

  @Test
  public void mcveTest() {
    String url = "http://localhost:" + port + "/feature";
    HttpStatus statusCode = this.restTemplate.getForEntity(url, String.class).getStatusCode();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode);
    assertEquals(0, getCount(FEATURE)); // Coming as 1 but should be 0 as it should be rollback
    assertEquals(0, getCount(BUG)); // Coming as 1 but should be 0 as it should be rollback
  }

  public int getCount(Table table) {
    return dslContext.fetchCount(table);
  }
}
