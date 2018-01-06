package io.github.bfstats;

import com.jayway.restassured.response.Response;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import ro.pippo.core.HttpConstants;
import ro.pippo.test.PippoRule;
import ro.pippo.test.PippoTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AppTest extends PippoTest {

  @Rule
  public PippoRule pippoRule = new PippoRule(new BasicApplication());

  @Ignore
  @Test
  public void testHello() {
    Response response = get("/");

    response.then()
        .statusCode(200)
        .contentType(HttpConstants.ContentType.TEXT_HTML);

    assertThat(response.asString(), equalTo("Hello World"));
  }
}
