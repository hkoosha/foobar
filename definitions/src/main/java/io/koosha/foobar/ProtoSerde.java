package io.koosha.foobar;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.lang.reflect.*;


public final class ProtoSerde {

    private ProtoSerde() {

        throw new UnsupportedOperationException("instantiation not supported");
    }

    // Stolen from Netty
    private static Class<?> find(final Object object,
                                 Class<?> parametrizedSuperclass,
                                 String typeParamName) {

        final Class<?> thisClass = object.getClass();

        Class<?> currentClass = thisClass;
        for (; ; ) {

            if (currentClass.getSuperclass() == parametrizedSuperclass) {
                int typeParamIndex = -1;
                final TypeVariable<?>[] typeParams = currentClass.getSuperclass().getTypeParameters();
                for (int i = 0; i < typeParams.length; i++)
                    if (typeParamName.equals(typeParams[i].getName())) {
                        typeParamIndex = i;
                        break;
                    }

                if (typeParamIndex < 0)
                    throw new IllegalStateException(
                            "unknown type parameter '" + typeParamName + "': " + parametrizedSuperclass);

                final Type genericSuperType = currentClass.getGenericSuperclass();
                if (!(genericSuperType instanceof ParameterizedType)) {
                    return Object.class;
                }

                final Type[] actualTypeParams = ((ParameterizedType) genericSuperType).getActualTypeArguments();

                Type actualTypeParam = actualTypeParams[typeParamIndex];
                if (actualTypeParam instanceof ParameterizedType) {
                    actualTypeParam = ((ParameterizedType) actualTypeParam).getRawType();
                }
                if (actualTypeParam instanceof Class) {
                    return (Class<?>) actualTypeParam;
                }
                if (actualTypeParam instanceof GenericArrayType) {
                    Type componentType = ((GenericArrayType) actualTypeParam).getGenericComponentType();
                    if (componentType instanceof ParameterizedType) {
                        componentType = ((ParameterizedType) componentType).getRawType();
                    }
                    if (componentType instanceof Class) {
                        return Array.newInstance((Class<?>) componentType, 0).getClass();
                    }
                }
                if (actualTypeParam instanceof final TypeVariable<?> v) {
                    // Resolved type parameter points to another type parameter.
                    if (!(v.getGenericDeclaration() instanceof Class)) {
                        return Object.class;
                    }

                    currentClass = thisClass;
                    parametrizedSuperclass = (Class<?>) v.getGenericDeclaration();
                    typeParamName = v.getName();
                    if (parametrizedSuperclass.isAssignableFrom(thisClass))
                        continue;
                    return Object.class;
                }

                throw new RuntimeException("could not find type parameter, class="
                        + thisClass.getName() + ", typeParameterName=" + typeParamName);
            }

            currentClass = currentClass.getSuperclass();
            if (currentClass == null) {
                throw new RuntimeException("could not find type parameter, class="
                        + thisClass.getName() + ", typeParameterName=" + typeParamName);
            }
        }
    }


    public static class Deser<T extends GeneratedMessageV3> implements Deserializer<T> {

        protected interface FromByteArray<U> {

            U fromBytes(byte[] data) throws InvalidProtocolBufferException;

        }

        private final FromByteArray<T> protoReader;

        public Deser(final FromByteArray<T> protoReader) {

            this.protoReader = protoReader;
        }

        public Deser(final Class<T> protoClass) {

            final Method parseFrom;
            try {
                parseFrom = protoClass.getDeclaredMethod("parseFrom", byte[].class);
            }
            catch (final NoSuchMethodException e) {
                throw new IllegalArgumentException("not a proto class: " + protoClass.getName(), e);
            }

            this.protoReader = data -> {
                try {
                    @SuppressWarnings({"unchecked", "RedundantCast"})
                    final T t = (T) parseFrom.invoke(null, (Object) data);
                    return t;
                }
                catch (final IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
            };
        }

        protected Deser() {

            final Class<?> protoClass;
            try {
                protoClass = find(this, Deser.class, "T");
            }
            catch (final Exception e) {
                throw new IllegalArgumentException("not a proto class: " + this.getClass().getName());
            }

            if (!GeneratedMessageV3.class.isAssignableFrom(protoClass))
                throw new IllegalArgumentException("not a proto class: " + this.getClass().getName());

            final Method parseFrom;
            try {
                parseFrom = protoClass.getDeclaredMethod("parseFrom", byte[].class);
            }
            catch (final NoSuchMethodException e) {
                throw new IllegalArgumentException("not a proto class: " + protoClass.getName(), e);
            }

            this.protoReader = data -> {
                try {
                    @SuppressWarnings({"unchecked", "RedundantCast"})
                    final T t = (T) parseFrom.invoke(null, (Object) data);
                    return t;
                }
                catch (final IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
            };
        }

        @Override
        public final T deserialize(final String topic,
                                   final byte[] data) {

            if (data == null)
                return null;

            try {
                return this.protoReader.fromBytes(data);
            }
            catch (final InvalidProtocolBufferException ex) {
                throw new SerializationException(ex);
            }

        }

    }


    public static class Ser<T extends GeneratedMessageV3> implements Serializer<T> {

        @Override
        public final byte[] serialize(final String topic,
                                      final T data) {

            return data.toByteArray();
        }

    }

}
