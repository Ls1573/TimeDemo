// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: mtMsg.proto

package com.time.memory.mt.nio.proto;

/**
 * Protobuf type {@code Busy_1}
 */
public  final class Busy_1 extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:Busy_1)
    Busy_1OrBuilder {
  // Use Busy_1.newBuilder() to construct.
  private Busy_1(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Busy_1() {
    value1_ = 0;
    stringValue_ = "";
    boolValue_ = false;
    vos_ = java.util.Collections.emptyList();
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private Busy_1(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    int mutable_bitField0_ = 0;
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!input.skipField(tag)) {
              done = true;
            }
            break;
          }
          case 8: {

            value1_ = input.readInt32();
            break;
          }
          case 18: {
            String s = input.readStringRequireUtf8();

            stringValue_ = s;
            break;
          }
          case 24: {

            boolValue_ = input.readBool();
            break;
          }
          case 34: {
            if (!((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
              vos_ = new java.util.ArrayList<Vo_1>();
              mutable_bitField0_ |= 0x00000008;
            }
            vos_.add(
                input.readMessage(Vo_1.parser(), extensionRegistry));
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
        vos_ = java.util.Collections.unmodifiableList(vos_);
      }
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return MsgBufProto.internal_static_Busy_1_descriptor;
  }

  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return MsgBufProto.internal_static_Busy_1_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            Busy_1.class, Builder.class);
  }

  private int bitField0_;
  public static final int VALUE1_FIELD_NUMBER = 1;
  private int value1_;
  /**
   * <code>optional int32 value1 = 1;</code>
   */
  public int getValue1() {
    return value1_;
  }

  public static final int STRING_VALUE_FIELD_NUMBER = 2;
  private volatile Object stringValue_;
  /**
   * <code>optional string string_value = 2;</code>
   */
  public String getStringValue() {
    Object ref = stringValue_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      stringValue_ = s;
      return s;
    }
  }
  /**
   * <code>optional string string_value = 2;</code>
   */
  public com.google.protobuf.ByteString
      getStringValueBytes() {
    Object ref = stringValue_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      stringValue_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int BOOL_VALUE_FIELD_NUMBER = 3;
  private boolean boolValue_;
  /**
   * <code>optional bool bool_value = 3;</code>
   */
  public boolean getBoolValue() {
    return boolValue_;
  }

  public static final int VOS_FIELD_NUMBER = 4;
  private java.util.List<Vo_1> vos_;
  /**
   * <code>repeated .Vo_1 vos = 4;</code>
   */
  public java.util.List<Vo_1> getVosList() {
    return vos_;
  }
  /**
   * <code>repeated .Vo_1 vos = 4;</code>
   */
  public java.util.List<? extends Vo_1OrBuilder>
      getVosOrBuilderList() {
    return vos_;
  }
  /**
   * <code>repeated .Vo_1 vos = 4;</code>
   */
  public int getVosCount() {
    return vos_.size();
  }
  /**
   * <code>repeated .Vo_1 vos = 4;</code>
   */
  public Vo_1 getVos(int index) {
    return vos_.get(index);
  }
  /**
   * <code>repeated .Vo_1 vos = 4;</code>
   */
  public Vo_1OrBuilder getVosOrBuilder(
      int index) {
    return vos_.get(index);
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (value1_ != 0) {
      output.writeInt32(1, value1_);
    }
    if (!getStringValueBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, stringValue_);
    }
    if (boolValue_ != false) {
      output.writeBool(3, boolValue_);
    }
    for (int i = 0; i < vos_.size(); i++) {
      output.writeMessage(4, vos_.get(i));
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (value1_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, value1_);
    }
    if (!getStringValueBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, stringValue_);
    }
    if (boolValue_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(3, boolValue_);
    }
    for (int i = 0; i < vos_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(4, vos_.get(i));
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof Busy_1)) {
      return super.equals(obj);
    }
    Busy_1 other = (Busy_1) obj;

    boolean result = true;
    result = result && (getValue1()
        == other.getValue1());
    result = result && getStringValue()
        .equals(other.getStringValue());
    result = result && (getBoolValue()
        == other.getBoolValue());
    result = result && getVosList()
        .equals(other.getVosList());
    return result;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptorForType().hashCode();
    hash = (37 * hash) + VALUE1_FIELD_NUMBER;
    hash = (53 * hash) + getValue1();
    hash = (37 * hash) + STRING_VALUE_FIELD_NUMBER;
    hash = (53 * hash) + getStringValue().hashCode();
    hash = (37 * hash) + BOOL_VALUE_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getBoolValue());
    if (getVosCount() > 0) {
      hash = (37 * hash) + VOS_FIELD_NUMBER;
      hash = (53 * hash) + getVosList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static Busy_1 parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Busy_1 parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Busy_1 parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Busy_1 parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Busy_1 parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static Busy_1 parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static Busy_1 parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static Busy_1 parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static Busy_1 parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static Busy_1 parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(Busy_1 prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code Busy_1}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:Busy_1)
          Busy_1OrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return MsgBufProto.internal_static_Busy_1_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return MsgBufProto.internal_static_Busy_1_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              Busy_1.class, Builder.class);
    }

    // Construct using Busy_1.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getVosFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      value1_ = 0;

      stringValue_ = "";

      boolValue_ = false;

      if (vosBuilder_ == null) {
        vos_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000008);
      } else {
        vosBuilder_.clear();
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return MsgBufProto.internal_static_Busy_1_descriptor;
    }

    public Busy_1 getDefaultInstanceForType() {
      return Busy_1.getDefaultInstance();
    }

    public Busy_1 build() {
      Busy_1 result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public Busy_1 buildPartial() {
      Busy_1 result = new Busy_1(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      result.value1_ = value1_;
      result.stringValue_ = stringValue_;
      result.boolValue_ = boolValue_;
      if (vosBuilder_ == null) {
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          vos_ = java.util.Collections.unmodifiableList(vos_);
          bitField0_ = (bitField0_ & ~0x00000008);
        }
        result.vos_ = vos_;
      } else {
        result.vos_ = vosBuilder_.build();
      }
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof Busy_1) {
        return mergeFrom((Busy_1)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(Busy_1 other) {
      if (other == Busy_1.getDefaultInstance()) return this;
      if (other.getValue1() != 0) {
        setValue1(other.getValue1());
      }
      if (!other.getStringValue().isEmpty()) {
        stringValue_ = other.stringValue_;
        onChanged();
      }
      if (other.getBoolValue() != false) {
        setBoolValue(other.getBoolValue());
      }
      if (vosBuilder_ == null) {
        if (!other.vos_.isEmpty()) {
          if (vos_.isEmpty()) {
            vos_ = other.vos_;
            bitField0_ = (bitField0_ & ~0x00000008);
          } else {
            ensureVosIsMutable();
            vos_.addAll(other.vos_);
          }
          onChanged();
        }
      } else {
        if (!other.vos_.isEmpty()) {
          if (vosBuilder_.isEmpty()) {
            vosBuilder_.dispose();
            vosBuilder_ = null;
            vos_ = other.vos_;
            bitField0_ = (bitField0_ & ~0x00000008);
            vosBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getVosFieldBuilder() : null;
          } else {
            vosBuilder_.addAllMessages(other.vos_);
          }
        }
      }
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Busy_1 parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (Busy_1) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private int value1_ ;
    /**
     * <code>optional int32 value1 = 1;</code>
     */
    public int getValue1() {
      return value1_;
    }
    /**
     * <code>optional int32 value1 = 1;</code>
     */
    public Builder setValue1(int value) {
      
      value1_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 value1 = 1;</code>
     */
    public Builder clearValue1() {
      
      value1_ = 0;
      onChanged();
      return this;
    }

    private Object stringValue_ = "";
    /**
     * <code>optional string string_value = 2;</code>
     */
    public String getStringValue() {
      Object ref = stringValue_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        stringValue_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>optional string string_value = 2;</code>
     */
    public com.google.protobuf.ByteString
        getStringValueBytes() {
      Object ref = stringValue_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        stringValue_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>optional string string_value = 2;</code>
     */
    public Builder setStringValue(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      stringValue_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional string string_value = 2;</code>
     */
    public Builder clearStringValue() {
      
      stringValue_ = getDefaultInstance().getStringValue();
      onChanged();
      return this;
    }
    /**
     * <code>optional string string_value = 2;</code>
     */
    public Builder setStringValueBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      stringValue_ = value;
      onChanged();
      return this;
    }

    private boolean boolValue_ ;
    /**
     * <code>optional bool bool_value = 3;</code>
     */
    public boolean getBoolValue() {
      return boolValue_;
    }
    /**
     * <code>optional bool bool_value = 3;</code>
     */
    public Builder setBoolValue(boolean value) {
      
      boolValue_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional bool bool_value = 3;</code>
     */
    public Builder clearBoolValue() {
      
      boolValue_ = false;
      onChanged();
      return this;
    }

    private java.util.List<Vo_1> vos_ =
      java.util.Collections.emptyList();
    private void ensureVosIsMutable() {
      if (!((bitField0_ & 0x00000008) == 0x00000008)) {
        vos_ = new java.util.ArrayList<Vo_1>(vos_);
        bitField0_ |= 0x00000008;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        Vo_1, Vo_1.Builder, Vo_1OrBuilder> vosBuilder_;

    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public java.util.List<Vo_1> getVosList() {
      if (vosBuilder_ == null) {
        return java.util.Collections.unmodifiableList(vos_);
      } else {
        return vosBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public int getVosCount() {
      if (vosBuilder_ == null) {
        return vos_.size();
      } else {
        return vosBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Vo_1 getVos(int index) {
      if (vosBuilder_ == null) {
        return vos_.get(index);
      } else {
        return vosBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Builder setVos(
        int index, Vo_1 value) {
      if (vosBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureVosIsMutable();
        vos_.set(index, value);
        onChanged();
      } else {
        vosBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Builder setVos(
        int index, Vo_1.Builder builderForValue) {
      if (vosBuilder_ == null) {
        ensureVosIsMutable();
        vos_.set(index, builderForValue.build());
        onChanged();
      } else {
        vosBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Builder addVos(Vo_1 value) {
      if (vosBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureVosIsMutable();
        vos_.add(value);
        onChanged();
      } else {
        vosBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Builder addVos(
        int index, Vo_1 value) {
      if (vosBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureVosIsMutable();
        vos_.add(index, value);
        onChanged();
      } else {
        vosBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Builder addVos(
        Vo_1.Builder builderForValue) {
      if (vosBuilder_ == null) {
        ensureVosIsMutable();
        vos_.add(builderForValue.build());
        onChanged();
      } else {
        vosBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Builder addVos(
        int index, Vo_1.Builder builderForValue) {
      if (vosBuilder_ == null) {
        ensureVosIsMutable();
        vos_.add(index, builderForValue.build());
        onChanged();
      } else {
        vosBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Builder addAllVos(
        Iterable<? extends Vo_1> values) {
      if (vosBuilder_ == null) {
        ensureVosIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, vos_);
        onChanged();
      } else {
        vosBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Builder clearVos() {
      if (vosBuilder_ == null) {
        vos_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000008);
        onChanged();
      } else {
        vosBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Builder removeVos(int index) {
      if (vosBuilder_ == null) {
        ensureVosIsMutable();
        vos_.remove(index);
        onChanged();
      } else {
        vosBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Vo_1.Builder getVosBuilder(
        int index) {
      return getVosFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Vo_1OrBuilder getVosOrBuilder(
        int index) {
      if (vosBuilder_ == null) {
        return vos_.get(index);  } else {
        return vosBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public java.util.List<? extends Vo_1OrBuilder>
         getVosOrBuilderList() {
      if (vosBuilder_ != null) {
        return vosBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(vos_);
      }
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Vo_1.Builder addVosBuilder() {
      return getVosFieldBuilder().addBuilder(
          Vo_1.getDefaultInstance());
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public Vo_1.Builder addVosBuilder(
        int index) {
      return getVosFieldBuilder().addBuilder(
          index, Vo_1.getDefaultInstance());
    }
    /**
     * <code>repeated .Vo_1 vos = 4;</code>
     */
    public java.util.List<Vo_1.Builder>
         getVosBuilderList() {
      return getVosFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        Vo_1, Vo_1.Builder, Vo_1OrBuilder>
        getVosFieldBuilder() {
      if (vosBuilder_ == null) {
        vosBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            Vo_1, Vo_1.Builder, Vo_1OrBuilder>(
                vos_,
                ((bitField0_ & 0x00000008) == 0x00000008),
                getParentForChildren(),
                isClean());
        vos_ = null;
      }
      return vosBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:Busy_1)
  }

  // @@protoc_insertion_point(class_scope:Busy_1)
  private static final Busy_1 DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new Busy_1();
  }

  public static Busy_1 getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Busy_1>
      PARSER = new com.google.protobuf.AbstractParser<Busy_1>() {
    public Busy_1 parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new Busy_1(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Busy_1> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<Busy_1> getParserForType() {
    return PARSER;
  }

  public Busy_1 getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

