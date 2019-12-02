package iterator.test.matchers.type.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.annotation.Annotation;

import iterator.Reflection;
import org.junit.jupiter.api.Test;

class AnnotationMapTest {

  @TestAnnotationWithoutValue private String annotated;

  @TestAnnotationWithoutValue(stringProperty = "BAZ")
  private String annotatedWithOverrides;

  @TestAnnotationWithoutValue(stringProperty = "foo")
  private String annotationWithDefaultStringProperty;

  @TestAnnotationWithoutValue(stringArrayProperty = {"foo", "bar", "baz"})
  private String stringArrayPropertySample;

  @TestAnnotationWithoutValue(booleanProperty = false)
  private String booleanPropertySample;

  @TestAnnotationWithoutValue(booleanArrayProperty = {false, true})
  private String booleanArrayPropertySample;

  @TestAnnotationWithoutValue(byteProperty = 42)
  private String bytePropertySample;

  @TestAnnotationWithoutValue(byteArrayProperty = {3, 2, 1})
  private String byteArrayPropertySample;

  @TestAnnotationWithoutValue(charProperty = 'a')
  private String charPropertySample;

  @TestAnnotationWithoutValue(charArrayProperty = {'f', 'o', 'o'})
  private String charArrayPropertySample;

  @TestAnnotationWithoutValue(doubleProperty = 42.24)
  private String doublePropertySample;

  @TestAnnotationWithoutValue(doubleArrayProperty = {42.24, 24.42})
  private String doubleArrayPropertySample;

  @TestAnnotationWithoutValue(floatProperty = 24.42f)
  private String floatPropertySample;

  @TestAnnotationWithoutValue(floatArrayProperty = {24.42f, 42.24f})
  private String floatArrayPropertySample;

  @TestAnnotationWithoutValue(intProperty = 24)
  private String intPropertySample;

  @TestAnnotationWithoutValue(intArrayProperty = {3, 2, 1})
  private String intArrayPropertySample;

  @TestAnnotationWithoutValue(longProperty = 42)
  private String longPropertySample;

  @TestAnnotationWithoutValue(longArrayProperty = {3, 2, 1})
  private String longArrayPropertySample;

  @TestAnnotationWithoutValue(shortProperty = 42)
  private String shortPropertySample;

  @TestAnnotationWithoutValue(shortArrayProperty = {1, 2, 3})
  private String shortArrayPropertySample;

  @TestAnnotationWithoutValue(enumProperty = MetasyntacticVariable.BAR)
  private String enumPropertySample;

  @TestAnnotationWithoutValue(
      enumArrayProperty = {MetasyntacticVariable.BAR, MetasyntacticVariable.BAZ})
  private String enumArrayPropertySample;

  @TestAnnotationWithoutValue(classProperty = String.class)
  private String classPropertySample;

  @TestAnnotationWithoutValue(classArrayProperty = {String.class, Integer.class})
  private String classArrayPropertySample;

  @TestAnnotationWithoutValue(annotationProperty = @TestAnnotationWithValue("bar"))
  private String annotationPropertySample;

  @TestAnnotationWithoutValue(
      annotationArrayProperty = {
        @TestAnnotationWithValue("bar"),
        @TestAnnotationWithValue(value = "baz", anotherProperty = "quix")
      })
  private String annotationArrayPropertySample;

  @TestAnnotationWithValue("bar")
  private String annotationWithValue;

  @TestAnnotationWithNullArrayDefault({String.class, Integer.class, Boolean.class})
  private String annotationWithNullArrayDefault;

  @TestAnnotationWithoutDefaults(
      annotationArrayProperty = {},
      annotationProperty = @TestAnnotationWithValue("foo"),
      booleanArrayProperty = {true, false},
      booleanProperty = true,
      byteArrayProperty = {},
      byteProperty = 0,
      charArrayProperty = {'a', 'b', 'c'},
      charProperty = 'd',
      classArrayProperty = {String.class, Integer.class, Long.class},
      classProperty = Object.class,
      doubleArrayProperty = {0.0},
      doubleProperty = 42.24,
      enumArrayProperty = {MetasyntacticVariable.FOO},
      enumProperty = MetasyntacticVariable.BAR,
      floatArrayProperty = {24.42F},
      floatProperty = 42.24F,
      intArrayProperty = {1, 2, 3},
      intProperty = 42,
      longArrayProperty = {3L, 2L, 1L},
      longProperty = 4L,
      shortArrayProperty = {7, 8, 9},
      shortProperty = 10,
      stringArrayProperty = {"foo", "bar", "baz"},
      stringProperty = "quix")
  private String annotationWithoutDefaults;

  @Test
  void shouldReturnAnnotationClassWhenAnnotationMapOfAnnotationInstance() {
    // given
    TestAnnotationWithoutValue annotation = anno("annotated", TestAnnotationWithoutValue.class);
    // when
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // then
    assertThat(map.getAnnotationClass(), is((Object) TestAnnotationWithoutValue.class));
  }

  @Test
  void shouldReturnMemberNamesWhenAnnotationMapOfAnnotationInstance() {
    // given
    TestAnnotationWithoutValue annotation = anno("annotated", TestAnnotationWithoutValue.class);
    // when
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // then
    assertThat(
        map.getMemberNames(),
        containsInAnyOrder(
            "stringProperty",
            "stringArrayProperty",
            "booleanProperty",
            "booleanArrayProperty",
            "byteProperty",
            "byteArrayProperty",
            "charProperty",
            "charArrayProperty",
            "shortProperty",
            "shortArrayProperty",
            "intProperty",
            "intArrayProperty",
            "longProperty",
            "longArrayProperty",
            "floatProperty",
            "floatArrayProperty",
            "doubleProperty",
            "doubleArrayProperty",
            "enumProperty",
            "enumArrayProperty",
            "classProperty",
            "classArrayProperty",
            "annotationProperty",
            "annotationArrayProperty"));
  }

  @Test
  void shouldReturnStringMemberValueWhenAnnotationMapOfAnnotationInstance() {
    // given
    TestAnnotationWithoutValue annotation = anno("annotated", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.get("stringProperty", String.class);
    // then
    String expected =
        Reflection.getAnnotationMemberDefault(TestAnnotationWithoutValue.class, "stringProperty");
    assertThat(actual, is(expected));
  }

  @Test
  void shouldReturnStringMemberNonDefaultValueWhenAnnotationMapOfAnnotationInstance() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.get("stringProperty", String.class);
    // then
    String expected = Reflection.getAnnotationMemberValue(annotation, "stringProperty");
    assertThat(actual, is(expected));
  }

  @Test
  void shouldReturnStringMemberStringArrayValueWhenAnnotationMapOfAnnotationInstance() {
    // given
    TestAnnotationWithoutValue annotation = anno("annotated", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String[] actual = map.get("stringArrayProperty", String[].class);
    // then
    String[] expected = Reflection.getAnnotationMemberValue(annotation, "stringArrayProperty");
    assertThat(actual, is(expected));
  }

  @Test
  void shouldThrowGivenNonExistentMemberNameWhenGet() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    assertThrows(
        IllegalArgumentException.class, () -> map.get("non existent member", String.class));
  }

  @Test
  void shouldThrowGivenIncorrectTypeWhenGet() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    assertThrows(IllegalArgumentException.class, () -> map.get("stringProperty", boolean.class));
  }

  @Test
  void shouldUpdateMemberValueWhenSet() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    String original = map.get("stringProperty", String.class);
    String updated = original + original;
    // when
    AnnotationMap<TestAnnotationWithoutValue> actual = map.set("stringProperty", updated);
    // then
    assertThat(actual.get("stringProperty", String.class), is(updated));
  }

  @Test
  void shouldThrowGivenNonExistentMemberNameWhenSet() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    assertThrows(IllegalArgumentException.class, () -> map.set("non existent member", "foo"));
  }

  @Test
  void shouldThrowGivenIncorrectTypeWhenSet() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    assertThrows(IllegalArgumentException.class, () -> map.set("stringProperty", true));
  }

  @Test
  void shouldThrowGivenNullForNonNullableTypeWhenSet() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    assertThrows(IllegalArgumentException.class, () -> map.set("booleanProperty", null));
  }

  @Test
  void shouldNotThrowGivenNullForNullableTypeWhenSet() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    AnnotationMap<TestAnnotationWithoutValue> actual = map.set("stringProperty", null);
    // then
    assertThat(actual.get("stringProperty", String.class), nullValue());
  }

  @Test
  void shouldCreateAnnotationMapFromClass() {
    // given
    TestAnnotationWithoutValue annotation = anno("annotated", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> expected = AnnotationMap.of(annotation);
    // when
    AnnotationMap<TestAnnotationWithoutValue> actual =
        AnnotationMap.from(TestAnnotationWithoutValue.class);
    // then
    assertThat(actual, is(expected));
  }

  @Test
  void shouldReturnAnnotationSyntaxWhenToString() {
    // given
    TestAnnotationWithoutValue annotation = anno("annotated", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(stringProperty = \"BAZ\")"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithStringArrayPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("stringArrayPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(
        actual,
        is("@TestAnnotationWithoutValue(stringArrayProperty = { \"foo\", \"bar\", \"baz\" })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithStringArrayPropertyValueGivenDefaultIsNullWhenToString() {
    // given
    TestAnnotationWithNullArrayDefault annotation =
        anno("annotationWithNullArrayDefault", TestAnnotationWithNullArrayDefault.class);
    AnnotationMap<TestAnnotationWithNullArrayDefault> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(
        actual,
        is("@TestAnnotationWithNullArrayDefault({ String.class, Integer.class, Boolean.class })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithBooleanPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("booleanPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(booleanProperty = false)"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithBooleanArrayPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("booleanArrayPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(booleanArrayProperty = { false, true })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithBytePropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("bytePropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(byteProperty = 42)"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithByteArrayPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("byteArrayPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(byteArrayProperty = { 3, 2, 1 })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithCharPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("charPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(charProperty = 'a')"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithCharArrayPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("charArrayPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(charArrayProperty = { 'f', 'o', 'o' })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithDoublePropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("doublePropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(doubleProperty = 42.24)"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithDoubleArrayPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("doubleArrayPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(doubleArrayProperty = { 42.24, 24.42 })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithFloatPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("floatPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(floatProperty = 24.42f)"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithFloatArrayPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("floatArrayPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(floatArrayProperty = { 24.42f, 42.24f })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithIntPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("intPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(intProperty = 24)"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWitIntArrayPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("intArrayPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(intArrayProperty = { 3, 2, 1 })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithLongPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("longPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(longProperty = 42)"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithLongArrayPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("longArrayPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(longArrayProperty = { 3, 2, 1 })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithShortPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("shortPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(shortProperty = 42)"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithShortArrayPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("shortArrayPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(shortArrayProperty = { 1, 2, 3 })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithEnumPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("enumPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(enumProperty = MetasyntacticVariable.BAR)"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithEnumArrayPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("enumArrayPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(
        actual,
        is(
            "@TestAnnotationWithoutValue(enumArrayProperty = { MetasyntacticVariable.BAR, MetasyntacticVariable.BAZ })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithClassPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("classPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithoutValue(classProperty = String.class)"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithClassArrayPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("classArrayPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(
        actual,
        is("@TestAnnotationWithoutValue(classArrayProperty = { String.class, Integer.class })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithAnnotationPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("annotationPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(
        actual,
        is("@TestAnnotationWithoutValue(annotationProperty = @TestAnnotationWithValue(\"bar\"))"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithAnnotationArrayPropertyValueWhenToString() {
    // given
    TestAnnotationWithoutValue annotation =
        anno("annotationArrayPropertySample", TestAnnotationWithoutValue.class);
    AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(
        actual,
        is(
            "@TestAnnotationWithoutValue(annotationArrayProperty = { @TestAnnotationWithValue(\"bar\"), @TestAnnotationWithValue(value = \"baz\", anotherProperty = \"quix\") })"));
  }

  @Test
  void shouldReturnAnnotationSyntaxWithValueWhenToString() {
    // given
    TestAnnotationWithValue annotation = anno("annotationWithValue", TestAnnotationWithValue.class);
    AnnotationMap<TestAnnotationWithValue> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    assertThat(actual, is("@TestAnnotationWithValue(\"bar\")"));
  }

  @Test
  void shouldHandleAbsenceOfDefaultWhenToStringUsingFromAnnotationType() {
    // given
    AnnotationMap<TestAnnotationWithoutDefaults> map =
        AnnotationMap.from(TestAnnotationWithoutDefaults.class);
    // when
    String actual = map.toString();
    // then
    String expected =
        String.format(
            "@TestAnnotationWithoutDefaults(annotationArrayProperty = %1$s, annotationProperty = %1$s, booleanArrayProperty = %1$s, booleanProperty = %1$s, byteArrayProperty = %1$s, byteProperty = %1$s, charArrayProperty = %1$s, charProperty = %1$s, classArrayProperty = %1$s, classProperty = %1$s, doubleArrayProperty = %1$s, doubleProperty = %1$s, enumArrayProperty = %1$s, enumProperty = %1$s, floatArrayProperty = %1$s, floatProperty = %1$s, intArrayProperty = %1$s, intProperty = %1$s, longArrayProperty = %1$s, longProperty = %1$s, shortArrayProperty = %1$s, shortProperty = %1$s, stringArrayProperty = %1$s, stringProperty = %1$s)",
            "UNDEFINED");
    assertThat(actual, is(expected));
  }

  @Test
  void shouldHandleAbsenceOfDefaultsWhenToStringUsingOfAnnotationInstance() {
    // given
    TestAnnotationWithoutDefaults annotation =
        anno("annotationWithoutDefaults", TestAnnotationWithoutDefaults.class);
    AnnotationMap<TestAnnotationWithoutDefaults> map = AnnotationMap.of(annotation);
    // when
    String actual = map.toString();
    // then
    String expected =
        "@TestAnnotationWithoutDefaults(annotationArrayProperty = {}, annotationProperty = @TestAnnotationWithValue(\"foo\"), booleanArrayProperty = { true, false }, booleanProperty = true, byteArrayProperty = {}, byteProperty = 0, charArrayProperty = { 'a', 'b', 'c' }, charProperty = 'd', classArrayProperty = { String.class, Integer.class, Long.class }, classProperty = Object.class, doubleArrayProperty = { 0.0 }, doubleProperty = 42.24, enumArrayProperty = { MetasyntacticVariable.FOO }, enumProperty = MetasyntacticVariable.BAR, floatArrayProperty = { 24.42f }, floatProperty = 42.24f, intArrayProperty = { 1, 2, 3 }, intProperty = 42, longArrayProperty = { 3, 2, 1 }, longProperty = 4, shortArrayProperty = { 7, 8, 9 }, shortProperty = 10, stringArrayProperty = { \"foo\", \"bar\", \"baz\" }, stringProperty = \"quix\")";
    assertThat(actual, is(expected));
  }

  private static <A extends Annotation> A anno(String fieldName, Class<A> annotationClass) {
    return Reflection.findFieldAnnotation(AnnotationMapTest.class, fieldName, annotationClass);
  }
}
