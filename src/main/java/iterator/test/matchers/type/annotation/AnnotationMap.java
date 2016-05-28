
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

@SuppressWarnings({ "unchecked", "rawtypes" })
public final class AnnotationMap<A extends Annotation> {

    public static abstract class MemberValue<T> {

        protected final T def;

        protected final Class<T> type;

        protected T value;

        private final boolean nullable;

        protected MemberValue(T value, T def, Class<T> type, boolean nullable) {
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

        public boolean isDefault() {
            return def == null && value == null || def != null && def.equals(value);
        }

        public boolean isNullable() {
            return nullable;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        void setValue(T value) {
            this.value = value;
        }

    }

    static abstract class ArrayMemberValue<T> extends MemberValue<T[]> {

        protected ArrayMemberValue(T[] value, T[] def, Class<T[]> type) {
            super(value, def, type, true);
        }

        @Override
        public boolean isDefault() {
            return def == null && value != null || Arrays.equals(def, value);
        }

        @Override
        public String toString() {
            if (value == null) return super.toString();
            StringJoiner joiner = new StringJoiner(COMMA, "{ ", " }");
            Arrays.stream(value).forEach(val -> joiner.add(format(val)));
            return joiner.toString();
        }

        protected String format(T val) {
            return String.valueOf(val);
        }

    }

    static final class AnnotationArrayMemberValue<A extends Annotation> extends ObjectArrayMemberValue<A> {

        private final List<AnnotationMap<A>> valueMaps;

        AnnotationArrayMemberValue(A[] value, A[] def, Class<A[]> type) {
            super(value, def, type);
            valueMaps = value == null ? null : Arrays.stream(value).map(anno -> of(anno)).collect(Collectors.toList());
        }

        @Override
        public String toString() {
            if (value == null) return super.toString();
            StringJoiner joiner = new StringJoiner(COMMA, "{ ", " }");
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

        BooleanMemberValue(boolean value, boolean def) {
            super(Boolean.valueOf(value), Boolean.valueOf(def), Boolean.class, false);
        }

    }

    static final class ByteArrayMemberValue extends ObjectArrayMemberValue<Byte> {

        ByteArrayMemberValue(byte[] value, byte[] def) {
            super(toObject(value), toObject(def), Byte[].class);
        }

    }

    static final class ByteMemberValue extends ObjectMemberValue<Byte> {

        ByteMemberValue(byte value, byte def) {
            super(Byte.valueOf(value), Byte.valueOf(def), Byte.class, false);
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

        CharMemberValue(char value, char def) {
            super(Character.valueOf(value), Character.valueOf(def), Character.class, false);
        }

        @Override
        public String toString() {
            return singleQuoted(value);
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

        DoubleMemberValue(double value, double def) {
            super(Double.valueOf(value), Double.valueOf(def), Double.class, false);
        }

    }

    static final class EnumArrayMemberValue<E extends Enum<E>> extends ObjectArrayMemberValue<E> {

        EnumArrayMemberValue(E[] value, E[] def, Class<E[]> type) {
            super(value, def, type);
        }

        @Override
        protected String format(E val) {
            return val != null ? String.format("%s.%s", val.getClass().getSimpleName(), val) : super.format(val);
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
            return suffixed(val, F);
        }

    }

    static final class FloatMemberValue extends ObjectMemberValue<Float> {

        FloatMemberValue(float value, float def) {
            super(Float.valueOf(value), Float.valueOf(def), Float.class, false);
        }

        @Override
        public String toString() {
            return suffixed(value, F);
        }

    }

    static final class IntArrayMemberValue extends ObjectArrayMemberValue<Integer> {

        IntArrayMemberValue(int[] value, int[] def) {
            super(toObject(value), toObject(def), Integer[].class);
        }

    }

    static final class IntMemberValue extends ObjectMemberValue<Integer> {

        IntMemberValue(int value, int def) {
            super(Integer.valueOf(value), Integer.valueOf(def), Integer.class, false);
        }

    }

    static final class LongArrayMemberValue extends ObjectArrayMemberValue<Long> {

        LongArrayMemberValue(long[] value, long[] def) {
            super(toObject(value), toObject(def), Long[].class);
        }

    }

    static final class LongMemberValue extends ObjectMemberValue<Long> {

        LongMemberValue(long value, long def) {
            super(Long.valueOf(value), Long.valueOf(def), Long.class, false);
        }

    }

    static class ObjectArrayMemberValue<T extends Object> extends ArrayMemberValue<T> {

        ObjectArrayMemberValue(T[] value, T[] def, Class<T[]> type) {
            super(value, def, type);
        }

    }

    static class ObjectMemberValue<T extends Object> extends MemberValue<T> {

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

        ShortMemberValue(short value, short def) {
            super(Short.valueOf(value), Short.valueOf(def), Short.class, false);
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
            return doubleQuoted(value);
        }

    }

    private static enum MemberValueFactory {
        ANNOTATION {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return type != null && type.isAnnotation();
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new AnnotationMemberValue<>((Annotation) value, (Annotation) def, (Class<Annotation>) type);
            }
        },
        ANNOTATION_ARRAY {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return type != null && type.isArray() && type.getComponentType().isAnnotation();
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new AnnotationArrayMemberValue<>((Annotation[]) value, (Annotation[]) def, (Class<Annotation[]>) type);
            }
        },
        BOOLEAN {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return boolean.class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new BooleanMemberValue((boolean) value, (boolean) def);
            }
        },
        BOOLEAN_ARRAY {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return boolean[].class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new BooleanArrayMemberValue((boolean[]) value, (boolean[]) def);
            }
        },
        BYTE {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return byte.class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new ByteMemberValue((byte) value, (byte) def);
            }
        },
        BYTE_ARRAY {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return byte[].class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new ByteArrayMemberValue((byte[]) value, (byte[]) def);
            }
        },
        CHAR {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return char.class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new CharMemberValue((char) value, (char) def);
            }
        },
        CHAR_ARRAY {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return char[].class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new CharArrayMemberValue((char[]) value, (char[]) def);
            }
        },
        CLASS {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return type != null && Class.class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new ClassMemberValue((Class<?>) value, (Class<?>) def);
            }
        },
        CLASS_ARRAY {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return type != null && type.isArray() && Class.class.equals(type.getComponentType());
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new ClassArrayMemberValue((Class<?>[]) value, (Class<?>[]) def);
            }
        },
        DOUBLE {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return double.class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new DoubleMemberValue((double) value, (double) def);
            }
        },
        DOUBLE_ARRAY {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return double[].class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new DoubleArrayMemberValue((double[]) value, (double[]) def);
            }
        },
        ENUM {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return type != null && type.isEnum();
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new EnumMemberValue<>((Enum) value, (Enum) def, (Class<Enum>) type);
            }
        },
        ENUM_ARRAY {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return type != null && type.isArray() && type.getComponentType().isEnum();
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new EnumArrayMemberValue<>((Enum[]) value, (Enum[]) def, (Class<Enum[]>) type);
            }
        },
        FLOAT {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return float.class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new FloatMemberValue((float) value, (float) def);
            }
        },
        FLOAT_ARRAY {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return float[].class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new FloatArrayMemberValue((float[]) value, (float[]) def);
            }
        },
        INT {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return int.class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new IntMemberValue((int) value, (int) def);
            }
        },
        INT_ARRAY {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return int[].class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new IntArrayMemberValue((int[]) value, (int[]) def);
            }
        },
        LONG {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return long.class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new LongMemberValue((long) value, (long) def);
            }
        },
        LONG_ARRAY {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return long[].class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new LongArrayMemberValue((long[]) value, (long[]) def);
            }
        },
        SHORT {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return short.class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new ShortMemberValue((short) value, (short) def);
            }
        },
        SHORT_ARRAY {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return short[].class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new ShortArrayMemberValue((short[]) value, (short[]) def);
            }
        },
        STRING {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return String.class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new StringMemberValue((String) value, (String) def);
            }
        },
        STRING_ARRAY {

            @Override
            boolean isValueType(Object value, Object def, Class<?> type) {
                return String[].class.equals(type);
            }

            @Override
            MemberValue<?> newMemberValue(Object value, Object def, Class<?> type) {
                return new StringArrayMemberValue((String[]) value, (String[]) def);
            }
        };

        static <A extends Annotation> MemberValue<?> forMember(String memberName, A annotation, Class<A> annotationType) {
            Object def = getAnnotationMemberDefault(annotationType, memberName);
            Object val = annotation != null ? getAnnotationMemberValue(annotation, memberName) : def;
            Class<?> type = getAnnotationMemberType(annotationType, memberName);
            for (MemberValueFactory fac : values()) {
                if (fac.isValueType(val, def, type)) return fac.newMemberValue(val, def, type);
            }
            throw new IllegalArgumentException(String.format("Unsupported annotation member type: '%s'", type));
        }

        abstract boolean isValueType(Object value, Object def, Class<?> type);

        abstract MemberValue<?> newMemberValue(Object value, Object def, Class<?> type);

    }

    static final String COMMA = ", ";

    static final String F = "f";

    public static <A extends Annotation> AnnotationMap<A> from(Class<A> annotationType) {
        return new AnnotationMap<>(annotationType, null);
    }

    public static <A extends Annotation> AnnotationMap<A> of(A annotation) {
        return new AnnotationMap<>(annotation);
    }

    static String doubleQuoted(String str) {
        return str != null ? String.format("\"%s\"", str) : String.valueOf(str);
    }

    static String singleQuoted(Character ch) {
        return ch != null ? String.format("'%s'", ch) : String.valueOf(ch);
    }

    static String suffixed(Object obj, String suffix) {
        return obj != null ? String.format("%s%s", obj, suffix) : String.valueOf(obj);
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
            if (!mv.isNullable()) throw new IllegalArgumentException(String.format("Cannot set member '%s' of type '%s' null", name, mv.getType().getSimpleName()));
            mv.setValue(null);
        }
        return this;
    }

    private void assertContains(String name) {
        if (!containsMember(name)) throw new IllegalArgumentException(String.format("No such member: '%s'", name));
    }

    private void assertType(String name, MemberValue<?> mv, Class<?> memberType) {
        if (!mv.getType().isAssignableFrom(memberType)) throw new IllegalArgumentException(String.format("Cannot get member '%s' of type '%s' as requested type: '%s'", name, mv.getType(), memberType));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("@");
        sb.append(getAnnotationClass().getSimpleName());
        StringJoiner joiner = new StringJoiner(COMMA, "(", ")");
        joiner.setEmptyValue(EMPTY);
        if (members.containsKey("value")) {
            MemberValue<?> mv = members.get("value");
            if (!mv.isDefault()) {
                if (members.size() > 1 && members.entrySet().stream().filter(entry -> !"value".equals(entry.getKey())).anyMatch(entry -> !entry.getValue().isDefault())) {
                    joiner.add(String.format("value = %s", mv));
                } else {
                    joiner.add(mv.toString());
                }
            }
        }
        members.entrySet().stream().filter(entry -> !"value".equals(entry.getKey()) && !entry.getValue().isDefault()).forEach(entry -> {
            joiner.add(String.format("%s = %s", entry.getKey(), entry.getValue()));
        });
        sb.append(joiner.toString());
        return sb.toString();
    }

}
