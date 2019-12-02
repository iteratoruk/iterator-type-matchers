/**
 * Copyright © 2016 Iterator Ltd. (iteratoruk@gmail.com)
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
package iterator.test.matchers.type.annotation;

import static iterator.Reflection.getAnnotationMemberDefault;
import static iterator.Reflection.getAnnotationMemberType;
import static iterator.Reflection.getAnnotationMemberValue;
import static java.util.Collections.unmodifiableSet;
import static org.apache.commons.lang3.ArrayUtils.toObject;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class AnnotationMap<A extends Annotation> {

  private static final String VALUE = "value";

  public abstract static class MemberValue<T> {

    private static final String UNDEFINED = "UNDEFINED";

    final T def;

    protected final Class<T> type;

    T value;

    private final boolean nullable;

    MemberValue(Class<T> type, boolean nullable) {
      this(null, null, type, nullable);
    }

    MemberValue(T value, Class<T> type, boolean nullable) {
      this(value, null, type, nullable);
    }

    MemberValue(T value, T def, Class<T> type, boolean nullable) {
      this.value = value;
      this.def = def;
      this.type = type;
      this.nullable = nullable;
    }

    @Override
    public boolean equals(Object obj) {
      return reflectionEquals(this, obj);
    }

    public Class<T> getType() {
      return type;
    }

    public T getValue() {
      return value;
    }

    @Override
    public int hashCode() {
      return reflectionHashCode(this);
    }

    public boolean isNotDefault() {
      return def == null || !def.equals(value);
    }

    public boolean isUndefined() {
      return def == null && value == null;
    }

    public boolean isNullable() {
      return nullable;
    }

    @Override
    public String toString() {
      return isUndefined() ? UNDEFINED : String.valueOf(value);
    }

    void setValue(T value) {
      this.value = value;
    }
  }

  abstract static class ArrayMemberValue<T> extends MemberValue<T[]> {

    ArrayMemberValue(T[] value, T[] def, Class<T[]> type) {
      super(value, def, type, true);
    }

    @Override
    public boolean isNotDefault() {
      return (def != null || value != null) && !Arrays.equals(def, value);
    }

    @Override
    public String toString() {
      if (value == null) return super.toString();
      StringJoiner joiner = getNewStringJoiner();
      Arrays.stream(value).forEach(val -> joiner.add(format(val)));
      return joiner.toString();
    }

    protected String format(T val) {
      return String.valueOf(val);
    }

    StringJoiner getNewStringJoiner() {
      if (value == null) throw new IllegalStateException("No array value available to join");
      String prefix = value.length > 0 ? "{ " : "{";
      String suffix = value.length > 0 ? " }" : "}";
      return new StringJoiner(COMMA, prefix, suffix);
    }
  }

  static final class AnnotationArrayMemberValue<A extends Annotation>
      extends ObjectArrayMemberValue<A> {

    private final List<AnnotationMap<A>> valueMaps;

    AnnotationArrayMemberValue(A[] value, A[] def, Class<A[]> type) {
      super(value, def, type);
      valueMaps =
          value == null
              ? null
              : Arrays.stream(value).map(AnnotationMap::of).collect(Collectors.toList());
    }

    @Override
    public String toString() {
      if (value == null) return super.toString();
      StringJoiner joiner = getNewStringJoiner();
      valueMaps.forEach(val -> joiner.add(val.toString()));
      return joiner.toString();
    }
  }

  static final class AnnotationMemberValue<A extends Annotation> extends ObjectMemberValue<A> {

    private final AnnotationMap<A> valueMap;

    AnnotationMemberValue(A value, A def, Class<A> type) {
      super(value, def, type, true);
      valueMap = value != null ? of(value) : null;
    }

    @Override
    public String toString() {
      return valueMap != null ? valueMap.toString() : super.toString();
    }
  }

  static final class BooleanArrayMemberValue extends ObjectArrayMemberValue<Boolean> {

    BooleanArrayMemberValue(boolean[] value, boolean[] def) {
      super(toObject(value), toObject(def), Boolean[].class);
    }
  }

  static final class BooleanMemberValue extends ObjectMemberValue<Boolean> {

    BooleanMemberValue() {
      super(Boolean.class, false);
    }

    BooleanMemberValue(boolean value) {
      super(value, Boolean.class, false);
    }

    BooleanMemberValue(boolean value, boolean def) {
      super(value, def, Boolean.class, false);
    }
  }

  static final class ByteArrayMemberValue extends ObjectArrayMemberValue<Byte> {

    ByteArrayMemberValue(byte[] value, byte[] def) {
      super(toObject(value), toObject(def), Byte[].class);
    }
  }

  static final class ByteMemberValue extends ObjectMemberValue<Byte> {

    ByteMemberValue() {
      super(Byte.class, false);
    }

    ByteMemberValue(byte value) {
      super(value, Byte.class, false);
    }

    ByteMemberValue(byte value, byte def) {
      super(value, def, Byte.class, false);
    }
  }

  static final class CharArrayMemberValue extends ObjectArrayMemberValue<Character> {

    CharArrayMemberValue(char[] value, char[] def) {
      super(toObject(value), toObject(def), Character[].class);
    }

    @Override
    protected String format(Character val) {
      return singleQuoted(val);
    }
  }

  static final class CharMemberValue extends ObjectMemberValue<Character> {

    CharMemberValue() {
      super(Character.class, false);
    }

    CharMemberValue(char value) {
      super(value, Character.class, false);
    }

    CharMemberValue(char value, char def) {
      super(value, def, Character.class, false);
    }

    @Override
    public String toString() {
      return value != null ? singleQuoted(value) : super.toString();
    }
  }

  static final class ClassArrayMemberValue extends ObjectArrayMemberValue<Class> {

    ClassArrayMemberValue(Class<?>[] value, Class<?>[] def) {
      super(value, def, Class[].class);
    }

    @Override
    protected String format(Class val) {
      return val != null ? String.format("%s.class", val.getSimpleName()) : super.toString();
    }
  }

  static final class ClassMemberValue extends ObjectMemberValue<Class> {

    ClassMemberValue(Class<?> value, Class<?> def) {
      super(value, def, Class.class, true);
    }

    @Override
    public String toString() {
      return value != null ? String.format("%s.class", value.getSimpleName()) : super.toString();
    }
  }

  static final class DoubleArrayMemberValue extends ObjectArrayMemberValue<Double> {

    DoubleArrayMemberValue(double[] value, double[] def) {
      super(toObject(value), toObject(def), Double[].class);
    }
  }

  static final class DoubleMemberValue extends ObjectMemberValue<Double> {

    DoubleMemberValue() {
      super(Double.class, false);
    }

    DoubleMemberValue(double value) {
      super(value, Double.class, false);
    }

    DoubleMemberValue(double value, double def) {
      super(value, def, Double.class, false);
    }
  }

  static final class EnumArrayMemberValue<E extends Enum<E>> extends ObjectArrayMemberValue<E> {

    EnumArrayMemberValue(E[] value, E[] def, Class<E[]> type) {
      super(value, def, type);
    }

    @Override
    protected String format(E val) {
      return val != null
          ? String.format("%s.%s", val.getClass().getSimpleName(), val)
          : super.format(val);
    }
  }

  static final class EnumMemberValue<E extends Enum<E>> extends ObjectMemberValue<E> {

    EnumMemberValue(E value, E def, Class<E> type) {
      super(value, def, type, true);
    }

    @Override
    public String toString() {
      return value != null ? String.format("%s.%s", type.getSimpleName(), value) : super.toString();
    }
  }

  static final class FloatArrayMemberValue extends ObjectArrayMemberValue<Float> {

    FloatArrayMemberValue(float[] value, float[] def) {
      super(toObject(value), toObject(def), Float[].class);
    }

    @Override
    protected String format(Float val) {
      return suffixed(val);
    }
  }

  static final class FloatMemberValue extends ObjectMemberValue<Float> {

    FloatMemberValue() {
      super(Float.class, false);
    }

    FloatMemberValue(float value) {
      super(value, Float.class, false);
    }

    FloatMemberValue(float value, float def) {
      super(value, def, Float.class, false);
    }

    @Override
    public String toString() {
      return value != null ? suffixed(value) : super.toString();
    }
  }

  static final class IntArrayMemberValue extends ObjectArrayMemberValue<Integer> {

    IntArrayMemberValue(int[] value, int[] def) {
      super(toObject(value), toObject(def), Integer[].class);
    }
  }

  static final class IntMemberValue extends ObjectMemberValue<Integer> {

    IntMemberValue() {
      super(Integer.class, false);
    }

    IntMemberValue(int value) {
      super(value, Integer.class, false);
    }

    IntMemberValue(int value, int def) {
      super(value, def, Integer.class, false);
    }
  }

  static final class LongArrayMemberValue extends ObjectArrayMemberValue<Long> {

    LongArrayMemberValue(long[] value, long[] def) {
      super(toObject(value), toObject(def), Long[].class);
    }
  }

  static final class LongMemberValue extends ObjectMemberValue<Long> {

    LongMemberValue() {
      super(Long.class, false);
    }

    LongMemberValue(long value) {
      super(value, Long.class, false);
    }

    LongMemberValue(long value, long def) {
      super(value, def, Long.class, false);
    }
  }

  static class ObjectArrayMemberValue<T extends Object> extends ArrayMemberValue<T> {

    ObjectArrayMemberValue(T[] value, T[] def, Class<T[]> type) {
      super(value, def, type);
    }
  }

  static class ObjectMemberValue<T extends Object> extends MemberValue<T> {

    ObjectMemberValue(Class<T> type, boolean nullable) {
      super(type, nullable);
    }

    ObjectMemberValue(T value, Class<T> type, boolean nullable) {
      super(value, type, nullable);
    }

    ObjectMemberValue(T value, T def, Class<T> type, boolean nullable) {
      super(value, def, type, nullable);
    }
  }

  static final class ShortArrayMemberValue extends ObjectArrayMemberValue<Short> {

    ShortArrayMemberValue(short[] value, short[] def) {
      super(toObject(value), toObject(def), Short[].class);
    }
  }

  static final class ShortMemberValue extends ObjectMemberValue<Short> {

    ShortMemberValue() {
      super(Short.class, false);
    }

    ShortMemberValue(short value) {
      super(value, Short.class, false);
    }

    ShortMemberValue(short value, short def) {
      super(value, def, Short.class, false);
    }
  }

  static final class StringArrayMemberValue extends ObjectArrayMemberValue<String> {

    StringArrayMemberValue(String[] value, String[] def) {
      super(value, def, String[].class);
    }

    @Override
    protected String format(String val) {
      return doubleQuoted(val);
    }
  }

  static final class StringMemberValue extends ObjectMemberValue<String> {

    StringMemberValue(String value, String def) {
      super(value, def, String.class, true);
    }

    @Override
    public String toString() {
      return value != null ? doubleQuoted(value) : super.toString();
    }
  }

  private enum MemberValueFactory {
    ANNOTATION {

      @Override
      boolean isValueType(Class<?> type) {
        return type.isAnnotation();
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new AnnotationMemberValue<>(
            (Annotation) value, (Annotation) def, (Class<Annotation>) type);
      }
    },
    ANNOTATION_ARRAY {

      @Override
      boolean isValueType(Class<?> type) {
        return type.isArray() && type.getComponentType().isAnnotation();
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new AnnotationArrayMemberValue<>(
            (Annotation[]) value, (Annotation[]) def, (Class<Annotation[]>) type);
      }
    },
    BOOLEAN {

      @Override
      boolean isValueType(Class<?> type) {
        return boolean.class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new BooleanMemberValue((boolean) value, (boolean) def);
      }

      @Override
      MemberValue<?> newMemberValueWithValue(Object value, Class<?> type) {
        return new BooleanMemberValue((boolean) value);
      }

      @Override
      MemberValue<?> newMemberValueWithoutValueOrDefault(Class<?> type) {
        return new BooleanMemberValue();
      }
    },
    BOOLEAN_ARRAY {

      @Override
      boolean isValueType(Class<?> type) {
        return boolean[].class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new BooleanArrayMemberValue((boolean[]) value, (boolean[]) def);
      }
    },
    BYTE {

      @Override
      boolean isValueType(Class<?> type) {
        return byte.class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new ByteMemberValue((byte) value, (byte) def);
      }

      @Override
      MemberValue<?> newMemberValueWithValue(Object value, Class<?> type) {
        return new ByteMemberValue((byte) value);
      }

      @Override
      MemberValue<?> newMemberValueWithoutValueOrDefault(Class<?> type) {
        return new ByteMemberValue();
      }
    },
    BYTE_ARRAY {

      @Override
      boolean isValueType(Class<?> type) {
        return byte[].class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new ByteArrayMemberValue((byte[]) value, (byte[]) def);
      }
    },
    CHAR {

      @Override
      boolean isValueType(Class<?> type) {
        return char.class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new CharMemberValue((char) value, (char) def);
      }

      @Override
      MemberValue<?> newMemberValueWithValue(Object value, Class<?> type) {
        return new CharMemberValue((char) value);
      }

      @Override
      MemberValue<?> newMemberValueWithoutValueOrDefault(Class<?> type) {
        return new CharMemberValue();
      }
    },
    CHAR_ARRAY {

      @Override
      boolean isValueType(Class<?> type) {
        return char[].class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new CharArrayMemberValue((char[]) value, (char[]) def);
      }
    },
    CLASS {

      @Override
      boolean isValueType(Class<?> type) {
        return Class.class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new ClassMemberValue((Class<?>) value, (Class<?>) def);
      }
    },
    CLASS_ARRAY {

      @Override
      boolean isValueType(Class<?> type) {
        return type.isArray() && Class.class.equals(type.getComponentType());
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new ClassArrayMemberValue((Class<?>[]) value, (Class<?>[]) def);
      }
    },
    DOUBLE {

      @Override
      boolean isValueType(Class<?> type) {
        return double.class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new DoubleMemberValue((double) value, (double) def);
      }

      @Override
      MemberValue<?> newMemberValueWithValue(Object value, Class<?> type) {
        return new DoubleMemberValue((double) value);
      }

      @Override
      MemberValue<?> newMemberValueWithoutValueOrDefault(Class<?> type) {
        return new DoubleMemberValue();
      }
    },
    DOUBLE_ARRAY {

      @Override
      boolean isValueType(Class<?> type) {
        return double[].class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new DoubleArrayMemberValue((double[]) value, (double[]) def);
      }
    },
    ENUM {

      @Override
      boolean isValueType(Class<?> type) {
        return type.isEnum();
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new EnumMemberValue<>((Enum) value, (Enum) def, (Class<Enum>) type);
      }
    },
    ENUM_ARRAY {

      @Override
      boolean isValueType(Class<?> type) {
        return type.isArray() && type.getComponentType().isEnum();
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new EnumArrayMemberValue<>((Enum[]) value, (Enum[]) def, (Class<Enum[]>) type);
      }
    },
    FLOAT {

      @Override
      boolean isValueType(Class<?> type) {
        return float.class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new FloatMemberValue((float) value, (float) def);
      }

      @Override
      MemberValue<?> newMemberValueWithValue(Object value, Class<?> type) {
        return new FloatMemberValue((float) value);
      }

      @Override
      MemberValue<?> newMemberValueWithoutValueOrDefault(Class<?> type) {
        return new FloatMemberValue();
      }
    },
    FLOAT_ARRAY {

      @Override
      boolean isValueType(Class<?> type) {
        return float[].class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new FloatArrayMemberValue((float[]) value, (float[]) def);
      }
    },
    INT {

      @Override
      boolean isValueType(Class<?> type) {
        return int.class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new IntMemberValue((int) value, (int) def);
      }

      @Override
      MemberValue<?> newMemberValueWithValue(Object value, Class<?> type) {
        return new IntMemberValue((int) value);
      }

      @Override
      MemberValue<?> newMemberValueWithoutValueOrDefault(Class<?> type) {
        return new IntMemberValue();
      }
    },
    INT_ARRAY {

      @Override
      boolean isValueType(Class<?> type) {
        return int[].class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new IntArrayMemberValue((int[]) value, (int[]) def);
      }
    },
    LONG {

      @Override
      boolean isValueType(Class<?> type) {
        return long.class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new LongMemberValue((long) value, (long) def);
      }

      @Override
      MemberValue<?> newMemberValueWithValue(Object value, Class<?> type) {
        return new LongMemberValue((long) value);
      }

      @Override
      MemberValue<?> newMemberValueWithoutValueOrDefault(Class<?> type) {
        return new LongMemberValue();
      }
    },
    LONG_ARRAY {

      @Override
      boolean isValueType(Class<?> type) {
        return long[].class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new LongArrayMemberValue((long[]) value, (long[]) def);
      }
    },
    SHORT {

      @Override
      boolean isValueType(Class<?> type) {
        return short.class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new ShortMemberValue((short) value, (short) def);
      }

      @Override
      MemberValue<?> newMemberValueWithValue(Object value, Class<?> type) {
        return new ShortMemberValue((short) value);
      }

      @Override
      MemberValue<?> newMemberValueWithoutValueOrDefault(Class<?> type) {
        return new ShortMemberValue();
      }
    },
    SHORT_ARRAY {

      @Override
      boolean isValueType(Class<?> type) {
        return short[].class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new ShortArrayMemberValue((short[]) value, (short[]) def);
      }
    },
    STRING {

      @Override
      boolean isValueType(Class<?> type) {
        return String.class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new StringMemberValue((String) value, (String) def);
      }
    },
    STRING_ARRAY {

      @Override
      boolean isValueType(Class<?> type) {
        return String[].class.equals(type);
      }

      @Override
      MemberValue<?> newMemberValueWithValueAndDefault(Object value, Object def, Class<?> type) {
        return new StringArrayMemberValue((String[]) value, (String[]) def);
      }
    };

    static <A extends Annotation> MemberValue<?> forMember(
        String memberName, A annotation, Class<A> annotationType) {
      Object def = getAnnotationMemberDefault(annotationType, memberName);
      Object val = annotation != null ? getAnnotationMemberValue(annotation, memberName) : def;
      Class<?> type = getAnnotationMemberType(annotationType, memberName);
      if (type != null) {
        for (MemberValueFactory fac : values()) {
          if (fac.isValueType(type)) return fac.newMemberValue(val, def, type);
        }
      }
      throw new IllegalArgumentException(
          String.format("Unsupported annotation member type: '%s'", type));
    }

    abstract boolean isValueType(Class<?> type);

    abstract MemberValue<?> newMemberValueWithValueAndDefault(
        Object value, Object def, Class<?> type);

    MemberValue<?> newMemberValueWithValue(Object value, Class<?> type) {
      return newMemberValueWithValueAndDefault(value, null, type);
    }

    // not possible in actually annotation usage but necessary when creating map from annotation
    // class
    MemberValue<?> newMemberValueWithoutValueOrDefault(Class<?> type) {
      return newMemberValueWithValueAndDefault(null, null, type);
    }

    // you can either have: a value AND a default; a value but NO default; neither value NOR default
    // should never occur: a default but NO value
    private MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
      if (value != null && def != null) {
        return newMemberValueWithValueAndDefault(value, def, type);
      }
      return def == null && value != null
          ? newMemberValueWithValue(value, type)
          : newMemberValueWithoutValueOrDefault(type);
    }
  }

  private static final String COMMA = ", ";

  private static final String F = "f";

  public static <A extends Annotation> AnnotationMap<A> from(Class<A> annotationType) {
    return new AnnotationMap<>(annotationType, null);
  }

  public static <A extends Annotation> AnnotationMap<A> of(A annotation) {
    return new AnnotationMap<>(annotation);
  }

  private static String doubleQuoted(String str) {
    return String.format("\"%s\"", str);
  }

  private static String singleQuoted(Character ch) {
    return String.format("'%s'", ch);
  }

  private static String suffixed(Object obj) {
    return String.format("%s%s", obj, AnnotationMap.F);
  }

  private final Class<A> annotationClass;

  private final SortedMap<String, MemberValue<?>> members = new TreeMap<>();

  private final Set<String> memberNames;

  private AnnotationMap(A annotation) {
    this((Class<A>) annotation.annotationType(), annotation);
  }

  private AnnotationMap(Class<A> annotationClass, A annotation) {
    this.annotationClass = annotationClass;
    Method[] methods = annotationClass.getDeclaredMethods();
    for (Method m : methods) {
      String memberName = m.getName();
      MemberValue<?> mv = MemberValueFactory.forMember(memberName, annotation, annotationClass);
      members.put(memberName, mv);
    }
    memberNames = unmodifiableSet(members.keySet());
  }

  public boolean containsMember(String name) {
    return memberNames.contains(name);
  }

  @Override
  public boolean equals(Object obj) {
    return reflectionEquals(this, obj);
  }

  public Class<A> getAnnotationClass() {
    return annotationClass;
  }

  public Set<String> getMemberNames() {
    return memberNames;
  }

  public <T> T get(String name, Class<T> memberType) {
    assertContains(name);
    MemberValue<T> mv = (MemberValue<T>) members.get(name);
    assertType(name, mv, memberType);
    return mv.getValue();
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(this);
  }

  public <T> AnnotationMap<A> set(String name, T value) {
    assertContains(name);
    MemberValue<T> mv = (MemberValue<T>) members.get(name);
    if (value != null) {
      assertType(name, mv, value.getClass());
      mv.setValue(value);
    } else {
      if (!mv.isNullable())
        throw new IllegalArgumentException(
            String.format(
                "Cannot set member '%s' of type '%s' null", name, mv.getType().getSimpleName()));
      mv.setValue(null);
    }
    return this;
  }

  private void assertContains(String name) {
    if (!containsMember(name))
      throw new IllegalArgumentException(String.format("No such member: '%s'", name));
  }

  private void assertType(String name, MemberValue<?> mv, Class<?> memberType) {
    if (!mv.getType().isAssignableFrom(memberType))
      throw new IllegalArgumentException(
          String.format(
              "Cannot get member '%s' of type '%s' as requested type: '%s'",
              name, mv.getType(), memberType));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("@");
    sb.append(getAnnotationClass().getSimpleName());
    StringJoiner joiner = new StringJoiner(COMMA, "(", ")");
    joiner.setEmptyValue(EMPTY);
    if (members.containsKey(VALUE)) {
      MemberValue<?> mv = members.get(VALUE);
      if (mv.isUndefined() || mv.isNotDefault()) {
        if (members.size() > 1
            && members.entrySet().stream()
                .filter(entry -> !VALUE.equals(entry.getKey()))
                .anyMatch(entry -> entry.getValue().isNotDefault())) {
          joiner.add(String.format("value = %s", mv));
        } else {
          joiner.add(mv.toString());
        }
      }
    }
    members.entrySet().stream()
        .filter(
            entry ->
                !VALUE.equals(entry.getKey())
                    && (entry.getValue().isUndefined() || entry.getValue().isNotDefault()))
        .forEach(entry -> joiner.add(String.format("%s = %s", entry.getKey(), entry.getValue())));
    sb.append(joiner.toString());
    return sb.toString();
  }
}
