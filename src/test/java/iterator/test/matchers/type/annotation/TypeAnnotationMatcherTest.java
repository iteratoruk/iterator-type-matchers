package iterator.test.matchers.type.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.annotation.Annotation;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

@TestAnnotationWithValue("foo")
class TypeAnnotationMatcherTest {

  // class under test
  private static <A extends Annotation, T> Matcher<Class<T>> hasTypeAnnotation(
      AnnotationMap<A> expected) {
    return new TypeAnnotationMatcher<>(expected);
  }

  @Test
  void shouldMatchSimpleAnnotationWithValue() throws Exception {
    assertThat(
        TypeAnnotationMatcherTest.class,
        hasTypeAnnotation(AnnotationMap.from(TestAnnotationWithValue.class).set("value", "foo")));
  }

  @Test
  void shouldNotMatchSimpleAnnotationWithValue() throws Exception {
    assertThrows(
        AssertionError.class,
        () -> {
          assertThat(
              TypeAnnotationMatcherTest.class,
              hasTypeAnnotation(
                  AnnotationMap.from(TestAnnotationWithValue.class).set("value", "bar")));
        });
  }

  @Test
  public void shouldNotMatchUnannotatedField() throws Exception {
    assertThrows(
        AssertionError.class,
        () -> {
          assertThat(
              FieldAnnotationMatcherTest.class,
              hasTypeAnnotation(AnnotationMap.from(TestAnnotationWithValue.class)));
        });
  }
}
