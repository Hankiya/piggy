// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: fundInfoByType.proto

package com.howbuy.wireless.entity.protobuf;

public final class FundInfoByTypeProtos {
  private FundInfoByTypeProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class FundInfo extends
      com.google.protobuf.GeneratedMessage {
    // Use FundInfo.newBuilder() to construct.
    private FundInfo() {
      initFields();
    }
    private FundInfo(boolean noInit) {}
    
    private static final FundInfo defaultInstance;
    public static FundInfo getDefaultInstance() {
      return defaultInstance;
    }
    
    public FundInfo getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.internal_static_FundInfo_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.internal_static_FundInfo_fieldAccessorTable;
    }
    
    // optional .common.Common common = 1;
    public static final int COMMON_FIELD_NUMBER = 1;
    private boolean hasCommon;
    private com.howbuy.wireless.entity.protobuf.CommonProtos.Common common_;
    public boolean hasCommon() { return hasCommon; }
    public com.howbuy.wireless.entity.protobuf.CommonProtos.Common getCommon() { return common_; }
    
    // repeated .FundList fundList = 2;
    public static final int FUNDLIST_FIELD_NUMBER = 2;
    private java.util.List<com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList> fundList_ =
      java.util.Collections.emptyList();
    public java.util.List<com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList> getFundListList() {
      return fundList_;
    }
    public int getFundListCount() { return fundList_.size(); }
    public com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList getFundList(int index) {
      return fundList_.get(index);
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
      for (com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList element : getFundListList()) {
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
      for (com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList element : getFundListList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, element);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.fundList_ != java.util.Collections.EMPTY_LIST) {
          result.fundList_ =
            java.util.Collections.unmodifiableList(result.fundList_);
        }
        com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo other) {
        if (other == com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo.getDefaultInstance()) return this;
        if (other.hasCommon()) {
          mergeCommon(other.getCommon());
        }
        if (!other.fundList_.isEmpty()) {
          if (result.fundList_.isEmpty()) {
            result.fundList_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList>();
          }
          result.fundList_.addAll(other.fundList_);
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
              com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList.Builder subBuilder = com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addFundList(subBuilder.buildPartial());
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
      
      // repeated .FundList fundList = 2;
      public java.util.List<com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList> getFundListList() {
        return java.util.Collections.unmodifiableList(result.fundList_);
      }
      public int getFundListCount() {
        return result.getFundListCount();
      }
      public com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList getFundList(int index) {
        return result.getFundList(index);
      }
      public Builder setFundList(int index, com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.fundList_.set(index, value);
        return this;
      }
      public Builder setFundList(int index, com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList.Builder builderForValue) {
        result.fundList_.set(index, builderForValue.build());
        return this;
      }
      public Builder addFundList(com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.fundList_.isEmpty()) {
          result.fundList_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList>();
        }
        result.fundList_.add(value);
        return this;
      }
      public Builder addFundList(com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList.Builder builderForValue) {
        if (result.fundList_.isEmpty()) {
          result.fundList_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList>();
        }
        result.fundList_.add(builderForValue.build());
        return this;
      }
      public Builder addAllFundList(
          java.lang.Iterable<? extends com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList> values) {
        if (result.fundList_.isEmpty()) {
          result.fundList_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList>();
        }
        super.addAll(values, result.fundList_);
        return this;
      }
      public Builder clearFundList() {
        result.fundList_ = java.util.Collections.emptyList();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:FundInfo)
    }
    
    static {
      defaultInstance = new FundInfo(true);
      com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:FundInfo)
  }
  
  public static final class FundList extends
      com.google.protobuf.GeneratedMessage {
    // Use FundList.newBuilder() to construct.
    private FundList() {
      initFields();
    }
    private FundList(boolean noInit) {}
    
    private static final FundList defaultInstance;
    public static FundList getDefaultInstance() {
      return defaultInstance;
    }
    
    public FundList getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.internal_static_FundList_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.internal_static_FundList_fieldAccessorTable;
    }
    
    // optional string fundCode = 1;
    public static final int FUNDCODE_FIELD_NUMBER = 1;
    private boolean hasFundCode;
    private java.lang.String fundCode_ = "";
    public boolean hasFundCode() { return hasFundCode; }
    public java.lang.String getFundCode() { return fundCode_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasFundCode()) {
        output.writeString(1, getFundCode());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasFundCode()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getFundCode());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList other) {
        if (other == com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList.getDefaultInstance()) return this;
        if (other.hasFundCode()) {
          setFundCode(other.getFundCode());
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
              setFundCode(input.readString());
              break;
            }
          }
        }
      }
      
      
      // optional string fundCode = 1;
      public boolean hasFundCode() {
        return result.hasFundCode();
      }
      public java.lang.String getFundCode() {
        return result.getFundCode();
      }
      public Builder setFundCode(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasFundCode = true;
        result.fundCode_ = value;
        return this;
      }
      public Builder clearFundCode() {
        result.hasFundCode = false;
        result.fundCode_ = getDefaultInstance().getFundCode();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:FundList)
    }
    
    static {
      defaultInstance = new FundList(true);
      com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:FundList)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_FundInfo_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_FundInfo_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_FundList_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_FundList_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\024fundInfoByType.proto\032\014common.proto\"G\n\010" +
      "FundInfo\022\036\n\006common\030\001 \001(\0132\016.common.Common" +
      "\022\033\n\010fundList\030\002 \003(\0132\t.FundList\"\034\n\010FundLis" +
      "t\022\020\n\010fundCode\030\001 \001(\tB;\n#com.howbuy.wirele" +
      "ss.entity.protobufB\024FundInfoByTypeProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_FundInfo_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_FundInfo_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_FundInfo_descriptor,
              new java.lang.String[] { "Common", "FundList", },
              com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo.class,
              com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo.Builder.class);
          internal_static_FundList_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_FundList_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_FundList_descriptor,
              new java.lang.String[] { "FundCode", },
              com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList.class,
              com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.howbuy.wireless.entity.protobuf.CommonProtos.getDescriptor(),
        }, assigner);
  }
  
  public static void internalForceInit() {}
  
  // @@protoc_insertion_point(outer_class_scope)
}