package me.exerosis.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public class ReflectField<T> {
    private static final Field FIELD_MODIFIERS;
    private static final Field FIELD_ACCESSOR;
    private static final Field FIELD_ACCESSOR_OVERRIDE;
    private static final Field FIELD_ROOT;

    static {
        Field fieldModifiers = null;
        Field fieldAccessor = null;
        Field fieldAccessorOverride = null;
        Field fieldRoot = null;
        try {
            fieldModifiers = Field.class.getDeclaredField("modifiers");
            fieldModifiers.setAccessible(true);
            fieldAccessor = Field.class.getDeclaredField("fieldAccessor");
            fieldAccessor.setAccessible(true);
            fieldAccessorOverride = Field.class.getDeclaredField("overrideFieldAccessor");
            fieldAccessorOverride.setAccessible(true);
            fieldRoot = Field.class.getDeclaredField("root");
            fieldRoot.setAccessible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            FIELD_MODIFIERS = fieldModifiers;
            FIELD_ACCESSOR = fieldAccessor;
            FIELD_ACCESSOR_OVERRIDE = fieldAccessorOverride;
            FIELD_ROOT = fieldRoot;
        }
    }

    private Field field;
    private Object instance;

    protected ReflectField(Field field) {
        this(field, null);
    }

    protected ReflectField(Field field, Object instance) {
        this.field = field;
        this.instance = instance;
        field.setAccessible(true);
    }

    public T value() {
        try {
            return (T) field.get(instance);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    //TODO probs needs some work here...
    public void setValue(T value) {
        try {
            if (Modifier.isFinal(field.getModifiers())) {
                FIELD_MODIFIERS.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                Field currentField = field;
                do {
                    FIELD_ACCESSOR.set(currentField, null);
                    FIELD_ACCESSOR_OVERRIDE.set(currentField, null);
                } while ((currentField = (Field) FIELD_ROOT.get(currentField)) != null);
            }
            field.set(instance, value);
        } catch (Exception ignored) {
        }
    }

    public ReflectClass<T> reflectValue() {
        return Reflect.Class(value());
    }

    public void setValueCast(Object value) {
        setValue((T) value);
    }

    //TODO Handle Exception
    public <K> K value(Class<K> type) {
        try {
            return type.cast(field.get(instance));
        } catch (Exception ignored) {
            return null;
        }
    }

    public <K> K value(ReflectClass<K> type) {
       return value(type.getClazz());
    }

    public ReflectClass<Object> getReflectType() {
        return new ReflectClass<>(field.getType()).setInstance(instance);
    }

    public ReflectClass<Object> getDeclaringClass() {
        return new ReflectClass<>(field.getDeclaringClass());
    }

    public Field getField() {
        return field;
    }

    public Object getInstance() {
        return instance;
    }

    public ReflectField<T> setInstance(Object instance) {
        this.instance = instance;
        return this;
    }

    //Field methods
    public Class<?> getType() {
        return field.getType();
    }

    public <K extends Annotation> K getAnnotation(Class<K> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    public String getName() {
        return field.getName();
    }
}