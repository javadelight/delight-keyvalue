package delight.keyvalue.tests;

import delight.keyvalue.utils.DecodeCaseInsensitiveKey;
import delight.keyvalue.utils.EncodeCaseInsensitiveKey;
import junit.framework.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class TestStringCaseFilters {
  @Test
  public void test() {
    final EncodeCaseInsensitiveKey filterIn = new EncodeCaseInsensitiveKey();
    final DecodeCaseInsensitiveKey filterOut = new DecodeCaseInsensitiveKey();
    final String lowerCase = filterIn.apply("Something_with_Uppercase");
    Assert.assertEquals("^Something_with_^Uppercase", lowerCase);
    Assert.assertEquals("Something_with_Uppercase", filterOut.apply(lowerCase));
    final String key2 = filterIn.apply("this/is/Nothing/but_a_very-common_KEY");
    Assert.assertEquals("this/is/^Nothing/but_a_very-common_^K^E^Y", key2);
    Assert.assertEquals("this/is/Nothing/but_a_very-common_KEY", filterOut.apply(key2));
  }
}
