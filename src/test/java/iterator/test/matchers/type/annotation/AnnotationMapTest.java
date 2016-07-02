
package iterator.test.matchers.type.annotation;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import iterator.Reflection;

import java.lang.annotation.Annotation;

import org.junit.Test;

public class AnnotationMapTest {

    @TestAnnotationWithoutValue
    private String annotated;

    @TestAnnotationWithoutValue(stringProperty = "BAZ")
    private String annotatedWithOverrides;

    @TestAnnotationWithoutValue(stringProperty = "foo")
    private String annotationWithDefaultStringProperty;

    @TestAnnotationWithoutValue(stringArrayProperty = { "foo", "bar", "baz" })
    private String stringArrayPropertySample;

    @TestAnnotationWithoutValue(booleanProperty = false)
    private String booleanPropertySample;

    @TestAnnotationWithoutValue(booleanArrayProperty = { false, true })
    private String booleanArrayPropertySample;

    @TestAnnotationWithoutValue(byteProperty = 42)
    private String bytePropertySample;

    @TestAnnotationWithoutValue(byteArrayProperty = { 3, 2, 1 })
    private String byteArrayPropertySample;

    @TestAnnotationWithoutValue(charProperty = 'a')
    private String charPropertySample;

    @TestAnnotationWithoutValue(charArrayProperty = { 'f', 'o', 'o' })
    private String charArrayPropertySample;

    @TestAnnotationWithoutValue(doubleProperty = 42.24)
    private String doublePropertySample;

    @TestAnnotationWithoutValue(doubleArrayProperty = { 42.24, 24.42 })
    private String doubleArrayPropertySample;

    @TestAnnotationWithoutValue(floatProperty = 24.42f)
    private String floatPropertySample;

    @TestAnnotationWithoutValue(floatArrayProperty = { 24.42f, 42.24f })
    private String floatArrayPropertySample;

    @TestAnnotationWithoutValue(intProperty = 24)
    private String intPropertySample;

    @TestAnnotationWithoutValue(intArrayProperty = { 3, 2, 1 })
    private String intArrayPropertySample;

    @TestAnnotationWithoutValue(longProperty = 42)
    private String longPropertySample;

    @TestAnnotationWithoutValue(longArrayProperty = { 3, 2, 1 })
    private String longArrayPropertySample;

    @TestAnnotationWithoutValue(shortProperty = 42)
    private String shortPropertySample;

    @TestAnnotationWithoutValue(shortArrayProperty = { 1, 2, 3 })
    private String shortArrayPropertySample;

    @TestAnnotationWithoutValue(enumProperty = MetasyntacticVariable.BAR)
    private String enumPropertySample;

    @TestAnnotationWithoutValue(enumArrayProperty = { MetasyntacticVariable.BAR, MetasyntacticVariable.BAZ })
    private String enumArrayPropertySample;

    @TestAnnotationWithoutValue(classProperty = String.class)
    private String classPropertySample;

    @TestAnnotationWithoutValue(classArrayProperty = { String.class, Integer.class })
    private String classArrayPropertySample;

    @TestAnnotationWithoutValue(annotationProperty = @TestAnnotationWithValue("bar"))
    private String annotationPropertySample;

    @TestAnnotationWithoutValue(annotationArrayProperty = { @TestAnnotationWithValue("bar"), @TestAnnotationWithValue(value = "baz", anotherProperty = "quix") })
    private String annotationArrayPropertySample;

    @TestAnnotationWithValue("bar")
    private String annotationWithValue;

    @TestAnnotationWithNullArrayDefault({ String.class, Integer.class, Boolean.class })
    private String annotationWithNullArrayDefault;

    @Test
    public void shouldReturnAnnotationClassWhenAnnotationMapOfAnnotationInstance() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotated", TestAnnotationWithoutValue.class);
        // when
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // then
        assertThat((Object) map.getAnnotationClass(), is((Object) TestAnnotationWithoutValue.class));
    }

    @Test
    public void shouldReturnMemberNamesWhenAnnotationMapOfAnnotationInstance() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotated", TestAnnotationWithoutValue.class);
        // when
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // then
        assertThat(map.getMemberNames(), containsInAnyOrder("stringProperty", "stringArrayProperty", "booleanProperty", "booleanArrayProperty", "byteProperty", "byteArrayProperty", "charProperty", "charArrayProperty", "shortProperty", "shortArrayProperty", "intProperty", "intArrayProperty", "longProperty", "longArrayProperty", "floatProperty", "floatArrayProperty", "doubleProperty", "doubleArrayProperty", "enumProperty", "enumArrayProperty", "classProperty", "classArrayProperty", "annotationProperty", "annotationArrayProperty"));
    }

    @Test
    public void shouldReturnStringMemberValueWhenAnnotationMapOfAnnotationInstance() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotated", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.get("stringProperty", String.class);
        // then
        String expected = Reflection.getAnnotationMemberDefault(TestAnnotationWithoutValue.class, "stringProperty");
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnStringMemberNonDefaultValueWhenAnnotationMapOfAnnotationInstance() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.get("stringProperty", String.class);
        // then
        String expected = Reflection.getAnnotationMemberValue(annotation, "stringProperty");
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnStringMemberStringArrayValueWhenAnnotationMapOfAnnotationInstance() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotated", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String[] actual = map.get("stringArrayProperty", String[].class);
        // then
        String[] expected = Reflection.getAnnotationMemberValue(annotation, "stringArrayProperty");
        assertThat(actual, is(expected));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowGivenNonExistentMemberNameWhenGet() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        map.get("non existent member", String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowGivenIncorrectTypeWhenGet() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        map.get("stringProperty", boolean.class);
    }

    @Test
    public void shouldUpdateMemberValueWhenSet() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        String original = map.get("stringProperty", String.class);
        String updated = original + original;
        // when
        AnnotationMap<TestAnnotationWithoutValue> actual = map.set("stringProperty", updated);
        // then
        assertThat(actual.get("stringProperty", String.class), is(updated));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowGivenNonExistentMemberNameWhenSet() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        map.set("non existent member", "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowGivenIncorrectTypeWhenSet() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        map.set("stringProperty", true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowGivenNullForNonNullableTypeWhenSet() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        map.set("booleanProperty", null);
    }

    @Test
    public void shouldNotThrowGivenNullForNullableTypeWhenSet() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        AnnotationMap<TestAnnotationWithoutValue> actual = map.set("stringProperty", null);
        // then
        assertThat(actual.get("stringProperty", String.class), nullValue());
    }

    @Test
    public void shouldCreateAnnotationMapFromClass() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotated", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> expected = AnnotationMap.of(annotation);
        // when
        AnnotationMap<TestAnnotationWithoutValue> actual = AnnotationMap.from(TestAnnotationWithoutValue.class);
        // then
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotated", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotatedWithOverrides", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(stringProperty = \"BAZ\")"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithStringArrayPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("stringArrayPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(stringArrayProperty = { \"foo\", \"bar\", \"baz\" })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithStringArrayPropertyValueGivenDefaultIsNullWhenToString() throws Exception {
        // given
        TestAnnotationWithNullArrayDefault annotation = anno("annotationWithNullArrayDefault", TestAnnotationWithNullArrayDefault.class);
        AnnotationMap<TestAnnotationWithNullArrayDefault> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithNullArrayDefault({ String.class, Integer.class, Boolean.class })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithBooleanPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("booleanPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(booleanProperty = false)"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithBooleanArrayPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("booleanArrayPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(booleanArrayProperty = { false, true })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithBytePropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("bytePropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(byteProperty = 42)"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithByteArrayPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("byteArrayPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(byteArrayProperty = { 3, 2, 1 })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithCharPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("charPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(charProperty = 'a')"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithCharArrayPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("charArrayPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(charArrayProperty = { 'f', 'o', 'o' })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithDoublePropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("doublePropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(doubleProperty = 42.24)"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithDoubleArrayPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("doubleArrayPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(doubleArrayProperty = { 42.24, 24.42 })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithFloatPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("floatPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(floatProperty = 24.42f)"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithFloatArrayPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("floatArrayPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(floatArrayProperty = { 24.42f, 42.24f })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithIntPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("intPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(intProperty = 24)"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWitIntArrayPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("intArrayPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(intArrayProperty = { 3, 2, 1 })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithLongPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("longPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(longProperty = 42)"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithLongArrayPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("longArrayPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(longArrayProperty = { 3, 2, 1 })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithShortPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("shortPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(shortProperty = 42)"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithShortArrayPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("shortArrayPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(shortArrayProperty = { 1, 2, 3 })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithEnumPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("enumPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(enumProperty = MetasyntacticVariable.BAR)"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithEnumArrayPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("enumArrayPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(enumArrayProperty = { MetasyntacticVariable.BAR, MetasyntacticVariable.BAZ })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithClassPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("classPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(classProperty = String.class)"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithClassArrayPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("classArrayPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(classArrayProperty = { String.class, Integer.class })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithAnnotationPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotationPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(annotationProperty = @TestAnnotationWithValue(\"bar\"))"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithAnnotationArrayPropertyValueWhenToString() throws Exception {
        // given
        TestAnnotationWithoutValue annotation = anno("annotationArrayPropertySample", TestAnnotationWithoutValue.class);
        AnnotationMap<TestAnnotationWithoutValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithoutValue(annotationArrayProperty = { @TestAnnotationWithValue(\"bar\"), @TestAnnotationWithValue(value = \"baz\", anotherProperty = \"quix\") })"));
    }

    @Test
    public void shouldReturnAnnotationSyntaxWithValueWhenToString() throws Exception {
        // given
        TestAnnotationWithValue annotation = anno("annotationWithValue", TestAnnotationWithValue.class);
        AnnotationMap<TestAnnotationWithValue> map = AnnotationMap.of(annotation);
        // when
        String actual = map.toString();
        // then
        assertThat(actual, is("@TestAnnotationWithValue(\"bar\")"));
    }

    private static <A extends Annotation> A anno(String fieldName, Class<A> annotationClass) {
        return Reflection.findFieldAnnotation(AnnotationMapTest.class, fieldName, annotationClass);
    }

}
