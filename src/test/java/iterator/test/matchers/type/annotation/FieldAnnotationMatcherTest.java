package iterator.test.matchers.type.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.annotation.Annotation;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class FieldAnnotationMatcherTest {

  // class under test
  private static <A extends Annotation, T> Matcher<Class<T>> hasFieldAnnotation(
      String fieldName, AnnotationMap<A> expected) {
    return new FieldAnnotationMatcher<>(fieldName, expected);
  }

  @TestAnnotationWithValue("foo")
  private Object matchingSimpleAnnotation;

  String noTestAnnotation;

  @Test
  void shouldMatchSimpleAnnotationWithValue() {
    assertThat(
        FieldAnnotationMatcherTest.class,
        hasFieldAnnotation(
            "matchingSimpleAnnotation",
            AnnotationMap.from(TestAnnotationWithValue.class).set("value", "foo")));
  }

  @Test
  void shouldNotMatchSimpleAnnotationWithValue() {
    assertThrows(
        AssertionError.class,
        () -> {
          assertThat(
              FieldAnnotationMatcherTest.class,
              hasFieldAnnotation(
                  "matchingSimpleAnnotation",
                  AnnotationMap.from(TestAnnotationWithValue.class).set("value", "bar")));
        });
  }

  @Test
  void shouldNotMatchUnannotatedField() {
    assertThrows(
        AssertionError.class,
        () -> {
          assertThat(
              FieldAnnotationMatcherTest.class,
              hasFieldAnnotation(
                  "noTestAnnotation", AnnotationMap.from(TestAnnotationWithValue.class)));
        });
  }
}
