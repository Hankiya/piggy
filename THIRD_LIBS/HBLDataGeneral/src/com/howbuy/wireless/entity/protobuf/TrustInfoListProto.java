// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: trustInfoList.proto

package com.howbuy.wireless.entity.protobuf;

public final class TrustInfoListProto {
  private TrustInfoListProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class TrustInfoList extends
      com.google.protobuf.GeneratedMessage {
    // Use TrustInfoList.newBuilder() to construct.
    private TrustInfoList() {
      initFields();
    }
    private TrustInfoList(boolean noInit) {}
    
    private static final TrustInfoList defaultInstance;
    public static TrustInfoList getDefaultInstance() {
      return defaultInstance;
    }
    
    public TrustInfoList getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.TrustInfoListProto.internal_static_TrustInfoList_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.TrustInfoListProto.internal_static_TrustInfoList_fieldAccessorTable;
    }
    
    // optional .common.Common common = 1;
    public static final int COMMON_FIELD_NUMBER = 1;
    private boolean hasCommon;
    private com.howbuy.wireless.entity.protobuf.CommonProtos.Common common_;
    public boolean hasCommon() { return hasCommon; }
    public com.howbuy.wireless.entity.protobuf.CommonProtos.Common getCommon() { return common_; }
    
    // repeated .TrustInfo trustInfo = 2;
    public static final int TRUSTINFO_FIELD_NUMBER = 2;
    private java.util.List<com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo> trustInfo_ =
      java.util.Collections.emptyList();
    public java.util.List<com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo> getTrustInfoList() {
      return trustInfo_;
    }
    public int getTrustInfoCount() { return trustInfo_.size(); }
    public com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo getTrustInfo(int index) {
      return trustInfo_.get(index);
    }
    
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
      for (com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo element : getTrustInfoList()) {
        output.writeMessage(2, element);
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
      for (com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo element : getTrustInfoList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, element);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.trustInfo_ != java.util.Collections.EMPTY_LIST) {
          result.trustInfo_ =
            java.util.Collections.unmodifiableList(result.trustInfo_);
        }
        com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList other) {
        if (other == com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList.getDefaultInstance()) return this;
        if (other.hasCommon()) {
          mergeCommon(other.getCommon());
        }
        if (!other.trustInfo_.isEmpty()) {
          if (result.trustInfo_.isEmpty()) {
            result.trustInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo>();
          }
          result.trustInfo_.addAll(other.trustInfo_);
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
            case 18: {
              com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo.Builder subBuilder = com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addTrustInfo(subBuilder.buildPartial());
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
      
      // repeated .TrustInfo trustInfo = 2;
      public java.util.List<com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo> getTrustInfoList() {
        return java.util.Collections.unmodifiableList(result.trustInfo_);
      }
      public int getTrustInfoCount() {
        return result.getTrustInfoCount();
      }
      public com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo getTrustInfo(int index) {
        return result.getTrustInfo(index);
      }
      public Builder setTrustInfo(int index, com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.trustInfo_.set(index, value);
        return this;
      }
      public Builder setTrustInfo(int index, com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo.Builder builderForValue) {
        result.trustInfo_.set(index, builderForValue.build());
        return this;
      }
      public Builder addTrustInfo(com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.trustInfo_.isEmpty()) {
          result.trustInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo>();
        }
        result.trustInfo_.add(value);
        return this;
      }
      public Builder addTrustInfo(com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo.Builder builderForValue) {
        if (result.trustInfo_.isEmpty()) {
          result.trustInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo>();
        }
        result.trustInfo_.add(builderForValue.build());
        return this;
      }
      public Builder addAllTrustInfo(
          java.lang.Iterable<? extends com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo> values) {
        if (result.trustInfo_.isEmpty()) {
          result.trustInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo>();
        }
        super.addAll(values, result.trustInfo_);
        return this;
      }
      public Builder clearTrustInfo() {
        result.trustInfo_ = java.util.Collections.emptyList();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:TrustInfoList)
    }
    
    static {
      defaultInstance = new TrustInfoList(true);
      com.howbuy.wireless.entity.protobuf.TrustInfoListProto.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:TrustInfoList)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TrustInfoList_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TrustInfoList_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\023trustInfoList.proto\032\017trustInfo.proto\032\014" +
      "common.proto\"N\n\rTrustInfoList\022\036\n\006common\030" +
      "\001 \001(\0132\016.common.Common\022\035\n\ttrustInfo\030\002 \003(\013" +
      "2\n.TrustInfoB9\n#com.howbuy.wireless.enti" +
      "ty.protobufB\022TrustInfoListProto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_TrustInfoList_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_TrustInfoList_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TrustInfoList_descriptor,
              new java.lang.String[] { "Common", "TrustInfo", },
              com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList.class,
              com.howbuy.wireless.entity.protobuf.TrustInfoListProto.TrustInfoList.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.howbuy.wireless.entity.protobuf.TrustInfoProtos.getDescriptor(),
          com.howbuy.wireless.entity.protobuf.CommonProtos.getDescriptor(),
        }, assigner);
  }
  
  public static void internalForceInit() {}
  
  // @@protoc_insertion_point(outer_class_scope)
}