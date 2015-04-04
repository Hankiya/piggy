// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: fundInfoClosesNew.proto

package com.howbuy.wireless.entity.protobuf;

public final class FundInfoClosesNewProto {
  private FundInfoClosesNewProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class FundInfoClosesNew extends
      com.google.protobuf.GeneratedMessage {
    // Use FundInfoClosesNew.newBuilder() to construct.
    private FundInfoClosesNew() {
      initFields();
    }
    private FundInfoClosesNew(boolean noInit) {}
    
    private static final FundInfoClosesNew defaultInstance;
    public static FundInfoClosesNew getDefaultInstance() {
      return defaultInstance;
    }
    
    public FundInfoClosesNew getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.internal_static_FundInfoClosesNew_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.internal_static_FundInfoClosesNew_fieldAccessorTable;
    }
    
    // optional .common.Common common = 1;
    public static final int COMMON_FIELD_NUMBER = 1;
    private boolean hasCommon;
    private com.howbuy.wireless.entity.protobuf.CommonProtos.Common common_;
    public boolean hasCommon() { return hasCommon; }
    public com.howbuy.wireless.entity.protobuf.CommonProtos.Common getCommon() { return common_; }
    
    // optional fixed64 serverTime = 2;
    public static final int SERVERTIME_FIELD_NUMBER = 2;
    private boolean hasServerTime;
    private long serverTime_ = 0L;
    public boolean hasServerTime() { return hasServerTime; }
    public long getServerTime() { return serverTime_; }
    
    // repeated .ClosesNew closesNew = 3;
    public static final int CLOSESNEW_FIELD_NUMBER = 3;
    private java.util.List<com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew> closesNew_ =
      java.util.Collections.emptyList();
    public java.util.List<com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew> getClosesNewList() {
      return closesNew_;
    }
    public int getClosesNewCount() { return closesNew_.size(); }
    public com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew getClosesNew(int index) {
      return closesNew_.get(index);
    }
    
    // optional fixed64 dateVersion = 4;
    public static final int DATEVERSION_FIELD_NUMBER = 4;
    private boolean hasDateVersion;
    private long dateVersion_ = 0L;
    public boolean hasDateVersion() { return hasDateVersion; }
    public long getDateVersion() { return dateVersion_; }
    
    private void initFields() {
      common_ = com.howbuy.wireless.entity.protobuf.CommonProtos.Common.getDefaultInstance();
    }
    public final boolean isInitialized() {
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasCommon()) {
        output.writeMessage(1, getCommon());
      }
      if (hasServerTime()) {
        output.writeFixed64(2, getServerTime());
      }
      for (com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew element : getClosesNewList()) {
        output.writeMessage(3, element);
      }
      if (hasDateVersion()) {
        output.writeFixed64(4, getDateVersion());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasCommon()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, getCommon());
      }
      if (hasServerTime()) {
        size += com.google.protobuf.CodedOutputStream
          .computeFixed64Size(2, getServerTime());
      }
      for (com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew element : getClosesNewList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, element);
      }
      if (hasDateVersion()) {
        size += com.google.protobuf.CodedOutputStream
          .computeFixed64Size(4, getDateVersion());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.closesNew_ != java.util.Collections.EMPTY_LIST) {
          result.closesNew_ =
            java.util.Collections.unmodifiableList(result.closesNew_);
        }
        com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew other) {
        if (other == com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew.getDefaultInstance()) return this;
        if (other.hasCommon()) {
          mergeCommon(other.getCommon());
        }
        if (other.hasServerTime()) {
          setServerTime(other.getServerTime());
        }
        if (!other.closesNew_.isEmpty()) {
          if (result.closesNew_.isEmpty()) {
            result.closesNew_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew>();
          }
          result.closesNew_.addAll(other.closesNew_);
        }
        if (other.hasDateVersion()) {
          setDateVersion(other.getDateVersion());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                return this;
              }
              break;
            }
            case 10: {
              com.howbuy.wireless.entity.protobuf.CommonProtos.Common.Builder subBuilder = com.howbuy.wireless.entity.protobuf.CommonProtos.Common.newBuilder();
              if (hasCommon()) {
                subBuilder.mergeFrom(getCommon());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setCommon(subBuilder.buildPartial());
              break;
            }
            case 17: {
              setServerTime(input.readFixed64());
              break;
            }
            case 26: {
              com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew.Builder subBuilder = com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addClosesNew(subBuilder.buildPartial());
              break;
            }
            case 33: {
              setDateVersion(input.readFixed64());
              break;
            }
          }
        }
      }
      
      
      // optional .common.Common common = 1;
      public boolean hasCommon() {
        return result.hasCommon();
      }
      public com.howbuy.wireless.entity.protobuf.CommonProtos.Common getCommon() {
        return result.getCommon();
      }
      public Builder setCommon(com.howbuy.wireless.entity.protobuf.CommonProtos.Common value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.hasCommon = true;
        result.common_ = value;
        return this;
      }
      public Builder setCommon(com.howbuy.wireless.entity.protobuf.CommonProtos.Common.Builder builderForValue) {
        result.hasCommon = true;
        result.common_ = builderForValue.build();
        return this;
      }
      public Builder mergeCommon(com.howbuy.wireless.entity.protobuf.CommonProtos.Common value) {
        if (result.hasCommon() &&
            result.common_ != com.howbuy.wireless.entity.protobuf.CommonProtos.Common.getDefaultInstance()) {
          result.common_ =
            com.howbuy.wireless.entity.protobuf.CommonProtos.Common.newBuilder(result.common_).mergeFrom(value).buildPartial();
        } else {
          result.common_ = value;
        }
        result.hasCommon = true;
        return this;
      }
      public Builder clearCommon() {
        result.hasCommon = false;
        result.common_ = com.howbuy.wireless.entity.protobuf.CommonProtos.Common.getDefaultInstance();
        return this;
      }
      
      // optional fixed64 serverTime = 2;
      public boolean hasServerTime() {
        return result.hasServerTime();
      }
      public long getServerTime() {
        return result.getServerTime();
      }
      public Builder setServerTime(long value) {
        result.hasServerTime = true;
        result.serverTime_ = value;
        return this;
      }
      public Builder clearServerTime() {
        result.hasServerTime = false;
        result.serverTime_ = 0L;
        return this;
      }
      
      // repeated .ClosesNew closesNew = 3;
      public java.util.List<com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew> getClosesNewList() {
        return java.util.Collections.unmodifiableList(result.closesNew_);
      }
      public int getClosesNewCount() {
        return result.getClosesNewCount();
      }
      public com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew getClosesNew(int index) {
        return result.getClosesNew(index);
      }
      public Builder setClosesNew(int index, com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.closesNew_.set(index, value);
        return this;
      }
      public Builder setClosesNew(int index, com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew.Builder builderForValue) {
        result.closesNew_.set(index, builderForValue.build());
        return this;
      }
      public Builder addClosesNew(com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.closesNew_.isEmpty()) {
          result.closesNew_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew>();
        }
        result.closesNew_.add(value);
        return this;
      }
      public Builder addClosesNew(com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew.Builder builderForValue) {
        if (result.closesNew_.isEmpty()) {
          result.closesNew_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew>();
        }
        result.closesNew_.add(builderForValue.build());
        return this;
      }
      public Builder addAllClosesNew(
          java.lang.Iterable<? extends com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew> values) {
        if (result.closesNew_.isEmpty()) {
          result.closesNew_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew>();
        }
        super.addAll(values, result.closesNew_);
        return this;
      }
      public Builder clearClosesNew() {
        result.closesNew_ = java.util.Collections.emptyList();
        return this;
      }
      
      // optional fixed64 dateVersion = 4;
      public boolean hasDateVersion() {
        return result.hasDateVersion();
      }
      public long getDateVersion() {
        return result.getDateVersion();
      }
      public Builder setDateVersion(long value) {
        result.hasDateVersion = true;
        result.dateVersion_ = value;
        return this;
      }
      public Builder clearDateVersion() {
        result.hasDateVersion = false;
        result.dateVersion_ = 0L;
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:FundInfoClosesNew)
    }
    
    static {
      defaultInstance = new FundInfoClosesNew(true);
      com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:FundInfoClosesNew)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_FundInfoClosesNew_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_FundInfoClosesNew_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\027fundInfoClosesNew.proto\032\017closesNew.pro" +
      "to\032\014common.proto\"{\n\021FundInfoClosesNew\022\036\n" +
      "\006common\030\001 \001(\0132\016.common.Common\022\022\n\nserverT" +
      "ime\030\002 \001(\006\022\035\n\tclosesNew\030\003 \003(\0132\n.ClosesNew" +
      "\022\023\n\013dateVersion\030\004 \001(\006B=\n#com.howbuy.wire" +
      "less.entity.protobufB\026FundInfoClosesNewP" +
      "roto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_FundInfoClosesNew_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_FundInfoClosesNew_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_FundInfoClosesNew_descriptor,
              new java.lang.String[] { "Common", "ServerTime", "ClosesNew", "DateVersion", },
              com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew.class,
              com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.howbuy.wireless.entity.protobuf.ClosesNewProtos.getDescriptor(),
          com.howbuy.wireless.entity.protobuf.CommonProtos.getDescriptor(),
        }, assigner);
  }
  
  public static void internalForceInit() {}
  
  // @@protoc_insertion_point(outer_class_scope)
}
