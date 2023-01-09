// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: io/koosha/foobar/entity/dead_letter_error.proto

package io.koosha.foobar.entity;

public final class DeadLetterErrorProto {
  private DeadLetterErrorProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface DeadLetterErrorOrBuilder extends
      // @@protoc_insertion_point(interface_extends:io.koosha.foobar.entity.DeadLetterError)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>.io.koosha.foobar.Header header = 1;</code>
     * @return Whether the header field is set.
     */
    boolean hasHeader();
    /**
     * <code>.io.koosha.foobar.Header header = 1;</code>
     * @return The header.
     */
    io.koosha.foobar.HeaderProto.Header getHeader();
    /**
     * <code>.io.koosha.foobar.Header header = 1;</code>
     */
    io.koosha.foobar.HeaderProto.HeaderOrBuilder getHeaderOrBuilder();

    /**
     * <code>string error = 2;</code>
     * @return The error.
     */
    java.lang.String getError();
    /**
     * <code>string error = 2;</code>
     * @return The bytes for error.
     */
    com.google.protobuf.ByteString
        getErrorBytes();
  }
  /**
   * Protobuf type {@code io.koosha.foobar.entity.DeadLetterError}
   */
  public static final class DeadLetterError extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:io.koosha.foobar.entity.DeadLetterError)
      DeadLetterErrorOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use DeadLetterError.newBuilder() to construct.
    private DeadLetterError(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private DeadLetterError() {
      error_ = "";
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
        UnusedPrivateParameter unused) {
      return new DeadLetterError();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return io.koosha.foobar.entity.DeadLetterErrorProto.internal_static_io_koosha_foobar_entity_DeadLetterError_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return io.koosha.foobar.entity.DeadLetterErrorProto.internal_static_io_koosha_foobar_entity_DeadLetterError_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError.class, io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError.Builder.class);
    }

    public static final int HEADER_FIELD_NUMBER = 1;
    private io.koosha.foobar.HeaderProto.Header header_;
    /**
     * <code>.io.koosha.foobar.Header header = 1;</code>
     * @return Whether the header field is set.
     */
    @java.lang.Override
    public boolean hasHeader() {
      return header_ != null;
    }
    /**
     * <code>.io.koosha.foobar.Header header = 1;</code>
     * @return The header.
     */
    @java.lang.Override
    public io.koosha.foobar.HeaderProto.Header getHeader() {
      return header_ == null ? io.koosha.foobar.HeaderProto.Header.getDefaultInstance() : header_;
    }
    /**
     * <code>.io.koosha.foobar.Header header = 1;</code>
     */
    @java.lang.Override
    public io.koosha.foobar.HeaderProto.HeaderOrBuilder getHeaderOrBuilder() {
      return header_ == null ? io.koosha.foobar.HeaderProto.Header.getDefaultInstance() : header_;
    }

    public static final int ERROR_FIELD_NUMBER = 2;
    @SuppressWarnings("serial")
    private volatile java.lang.Object error_ = "";
    /**
     * <code>string error = 2;</code>
     * @return The error.
     */
    @java.lang.Override
    public java.lang.String getError() {
      java.lang.Object ref = error_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        error_ = s;
        return s;
      }
    }
    /**
     * <code>string error = 2;</code>
     * @return The bytes for error.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getErrorBytes() {
      java.lang.Object ref = error_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        error_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (header_ != null) {
        output.writeMessage(1, getHeader());
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(error_)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, error_);
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (header_ != null) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, getHeader());
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(error_)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, error_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError)) {
        return super.equals(obj);
      }
      io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError other = (io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError) obj;

      if (hasHeader() != other.hasHeader()) return false;
      if (hasHeader()) {
        if (!getHeader()
            .equals(other.getHeader())) return false;
      }
      if (!getError()
          .equals(other.getError())) return false;
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasHeader()) {
        hash = (37 * hash) + HEADER_FIELD_NUMBER;
        hash = (53 * hash) + getHeader().hashCode();
      }
      hash = (37 * hash) + ERROR_FIELD_NUMBER;
      hash = (53 * hash) + getError().hashCode();
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code io.koosha.foobar.entity.DeadLetterError}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:io.koosha.foobar.entity.DeadLetterError)
        io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterErrorOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return io.koosha.foobar.entity.DeadLetterErrorProto.internal_static_io_koosha_foobar_entity_DeadLetterError_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return io.koosha.foobar.entity.DeadLetterErrorProto.internal_static_io_koosha_foobar_entity_DeadLetterError_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError.class, io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError.Builder.class);
      }

      // Construct using io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError.newBuilder()
      private Builder() {

      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);

      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        header_ = null;
        if (headerBuilder_ != null) {
          headerBuilder_.dispose();
          headerBuilder_ = null;
        }
        error_ = "";
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return io.koosha.foobar.entity.DeadLetterErrorProto.internal_static_io_koosha_foobar_entity_DeadLetterError_descriptor;
      }

      @java.lang.Override
      public io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError getDefaultInstanceForType() {
        return io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError.getDefaultInstance();
      }

      @java.lang.Override
      public io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError build() {
        io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError buildPartial() {
        io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError result = new io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError(this);
        if (bitField0_ != 0) { buildPartial0(result); }
        onBuilt();
        return result;
      }

      private void buildPartial0(io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.header_ = headerBuilder_ == null
              ? header_
              : headerBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) != 0)) {
          result.error_ = error_;
        }
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError) {
          return mergeFrom((io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError other) {
        if (other == io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError.getDefaultInstance()) return this;
        if (other.hasHeader()) {
          mergeHeader(other.getHeader());
        }
        if (!other.getError().isEmpty()) {
          error_ = other.error_;
          bitField0_ |= 0x00000002;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10: {
                input.readMessage(
                    getHeaderFieldBuilder().getBuilder(),
                    extensionRegistry);
                bitField0_ |= 0x00000001;
                break;
              } // case 10
              case 18: {
                error_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000002;
                break;
              } // case 18
              default: {
                if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                  done = true; // was an endgroup tag
                }
                break;
              } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }
      private int bitField0_;

      private io.koosha.foobar.HeaderProto.Header header_;
      private com.google.protobuf.SingleFieldBuilderV3<
          io.koosha.foobar.HeaderProto.Header, io.koosha.foobar.HeaderProto.Header.Builder, io.koosha.foobar.HeaderProto.HeaderOrBuilder> headerBuilder_;
      /**
       * <code>.io.koosha.foobar.Header header = 1;</code>
       * @return Whether the header field is set.
       */
      public boolean hasHeader() {
        return ((bitField0_ & 0x00000001) != 0);
      }
      /**
       * <code>.io.koosha.foobar.Header header = 1;</code>
       * @return The header.
       */
      public io.koosha.foobar.HeaderProto.Header getHeader() {
        if (headerBuilder_ == null) {
          return header_ == null ? io.koosha.foobar.HeaderProto.Header.getDefaultInstance() : header_;
        } else {
          return headerBuilder_.getMessage();
        }
      }
      /**
       * <code>.io.koosha.foobar.Header header = 1;</code>
       */
      public Builder setHeader(io.koosha.foobar.HeaderProto.Header value) {
        if (headerBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          header_ = value;
        } else {
          headerBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <code>.io.koosha.foobar.Header header = 1;</code>
       */
      public Builder setHeader(
          io.koosha.foobar.HeaderProto.Header.Builder builderForValue) {
        if (headerBuilder_ == null) {
          header_ = builderForValue.build();
        } else {
          headerBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <code>.io.koosha.foobar.Header header = 1;</code>
       */
      public Builder mergeHeader(io.koosha.foobar.HeaderProto.Header value) {
        if (headerBuilder_ == null) {
          if (((bitField0_ & 0x00000001) != 0) &&
            header_ != null &&
            header_ != io.koosha.foobar.HeaderProto.Header.getDefaultInstance()) {
            getHeaderBuilder().mergeFrom(value);
          } else {
            header_ = value;
          }
        } else {
          headerBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <code>.io.koosha.foobar.Header header = 1;</code>
       */
      public Builder clearHeader() {
        bitField0_ = (bitField0_ & ~0x00000001);
        header_ = null;
        if (headerBuilder_ != null) {
          headerBuilder_.dispose();
          headerBuilder_ = null;
        }
        onChanged();
        return this;
      }
      /**
       * <code>.io.koosha.foobar.Header header = 1;</code>
       */
      public io.koosha.foobar.HeaderProto.Header.Builder getHeaderBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getHeaderFieldBuilder().getBuilder();
      }
      /**
       * <code>.io.koosha.foobar.Header header = 1;</code>
       */
      public io.koosha.foobar.HeaderProto.HeaderOrBuilder getHeaderOrBuilder() {
        if (headerBuilder_ != null) {
          return headerBuilder_.getMessageOrBuilder();
        } else {
          return header_ == null ?
              io.koosha.foobar.HeaderProto.Header.getDefaultInstance() : header_;
        }
      }
      /**
       * <code>.io.koosha.foobar.Header header = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilderV3<
          io.koosha.foobar.HeaderProto.Header, io.koosha.foobar.HeaderProto.Header.Builder, io.koosha.foobar.HeaderProto.HeaderOrBuilder> 
          getHeaderFieldBuilder() {
        if (headerBuilder_ == null) {
          headerBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
              io.koosha.foobar.HeaderProto.Header, io.koosha.foobar.HeaderProto.Header.Builder, io.koosha.foobar.HeaderProto.HeaderOrBuilder>(
                  getHeader(),
                  getParentForChildren(),
                  isClean());
          header_ = null;
        }
        return headerBuilder_;
      }

      private java.lang.Object error_ = "";
      /**
       * <code>string error = 2;</code>
       * @return The error.
       */
      public java.lang.String getError() {
        java.lang.Object ref = error_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          error_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string error = 2;</code>
       * @return The bytes for error.
       */
      public com.google.protobuf.ByteString
          getErrorBytes() {
        java.lang.Object ref = error_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          error_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string error = 2;</code>
       * @param value The error to set.
       * @return This builder for chaining.
       */
      public Builder setError(
          java.lang.String value) {
        if (value == null) { throw new NullPointerException(); }
        error_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }
      /**
       * <code>string error = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearError() {
        error_ = getDefaultInstance().getError();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      /**
       * <code>string error = 2;</code>
       * @param value The bytes for error to set.
       * @return This builder for chaining.
       */
      public Builder setErrorBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        checkByteStringIsUtf8(value);
        error_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:io.koosha.foobar.entity.DeadLetterError)
    }

    // @@protoc_insertion_point(class_scope:io.koosha.foobar.entity.DeadLetterError)
    private static final io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError();
    }

    public static io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<DeadLetterError>
        PARSER = new com.google.protobuf.AbstractParser<DeadLetterError>() {
      @java.lang.Override
      public DeadLetterError parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        Builder builder = newBuilder();
        try {
          builder.mergeFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(builder.buildPartial());
        } catch (com.google.protobuf.UninitializedMessageException e) {
          throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(e)
              .setUnfinishedMessage(builder.buildPartial());
        }
        return builder.buildPartial();
      }
    };

    public static com.google.protobuf.Parser<DeadLetterError> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<DeadLetterError> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public io.koosha.foobar.entity.DeadLetterErrorProto.DeadLetterError getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_io_koosha_foobar_entity_DeadLetterError_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_io_koosha_foobar_entity_DeadLetterError_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n/io/koosha/foobar/entity/dead_letter_er" +
      "ror.proto\022\027io.koosha.foobar.entity\032\035io/k" +
      "oosha/foobar/header.proto\"J\n\017DeadLetterE" +
      "rror\022(\n\006header\030\001 \001(\0132\030.io.koosha.foobar." +
      "Header\022\r\n\005error\030\002 \001(\tB/\n\027io.koosha.fooba" +
      "r.entityB\024DeadLetterErrorProtob\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          io.koosha.foobar.HeaderProto.getDescriptor(),
        });
    internal_static_io_koosha_foobar_entity_DeadLetterError_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_io_koosha_foobar_entity_DeadLetterError_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_io_koosha_foobar_entity_DeadLetterError_descriptor,
        new java.lang.String[] { "Header", "Error", });
    io.koosha.foobar.HeaderProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
