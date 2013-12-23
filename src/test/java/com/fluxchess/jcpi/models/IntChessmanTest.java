/*
 * Copyright 2007-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fluxchess.jcpi.models;

import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static com.fluxchess.test.AssertUtil.assertUtilityClassWellDefined;
import static org.junit.Assert.*;

public class IntChessmanTest {

  @Test
  public void testUtilityClass() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    assertUtilityClassWellDefined(IntChessman.class);
  }

  @Test
  public void testValues() {
    for (GenericChessman genericChessman : GenericChessman.values()) {
      assertEquals(genericChessman, IntChessman.toGenericChessman(IntChessman.valueOf(genericChessman)));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueOf() {
    IntChessman.valueOf(null);
  }

  @Test
  public void testPromotionValues() {
    for (GenericChessman genericChessman : GenericChessman.promotions) {
      assertEquals(genericChessman, IntChessman.toGenericChessman(IntChessman.valueOfPromotion(genericChessman)));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueOfPromotion() {
    IntChessman.valueOfPromotion(GenericChessman.PAWN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidToGenericChessman() {
    IntChessman.toGenericChessman(IntChessman.NOCHESSMAN);
  }

  @Test
  public void testOrdinal() {
    for (int chessman : IntChessman.values) {
      assertEquals(IntChessman.ordinal(chessman), IntChessman.toGenericChessman(chessman).ordinal());
      assertEquals(chessman, IntChessman.values[IntChessman.ordinal(chessman)]);
    }
  }

  @Test
  public void testIsValid() {
    for (int chessman : IntChessman.values) {
      assertTrue(IntChessman.isValid(chessman));
      assertEquals(chessman, chessman & IntChessman.MASK);
    }
  }

  @Test
  public void testInvalidIsValid() {
    assertFalse(IntChessman.isValid(IntChessman.NOCHESSMAN));
  }

  @Test
  public void testIsValidPromotion() {
    for (int chessman : IntChessman.promotions) {
      assertTrue(IntChessman.isValidPromotion(chessman));
      assertEquals(chessman, chessman & IntChessman.MASK);
    }
  }

  @Test
  public void testInvalidIsValidPromotion() {
    assertFalse(IntChessman.isValidPromotion(IntChessman.PAWN));
  }

  @Test
  public void testIsSliding() {
    assertTrue(IntChessman.isSliding(IntChessman.BISHOP));
    assertTrue(IntChessman.isSliding(IntChessman.ROOK));
    assertTrue(IntChessman.isSliding(IntChessman.QUEEN));
    assertFalse(IntChessman.isSliding(IntChessman.PAWN));
    assertFalse(IntChessman.isSliding(IntChessman.KNIGHT));
    assertFalse(IntChessman.isSliding(IntChessman.KING));
  }

}
