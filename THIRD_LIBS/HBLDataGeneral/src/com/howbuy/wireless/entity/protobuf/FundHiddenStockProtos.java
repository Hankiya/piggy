// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: fundyxzcg.proto

package com.howbuy.wireless.entity.protobuf;

public final class FundHiddenStockProtos {
  private FundHiddenStockProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class FundHiddenStockInfoProb extends
      com.google.protobuf.GeneratedMessage {
    // Use FundHiddenStockInfoProb.newBuilder() to construct.
    private FundHiddenStockInfoProb() {
      initFields();
    }
    private FundHiddenStockInfoProb(boolean noInit) {}
    
    private static final FundHiddenStockInfoProb defaultInstance;
    public static FundHiddenStockInfoProb getDefaultInstance() {
      return defaultInstance;
    }
    
    public FundHiddenStockInfoProb getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.internal_static_FundHiddenStockInfoProb_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.internal_static_FundHiddenStockInfoProb_fieldAccessorTable;
    }
    
    // optional string jjdm = 1;
    public static final int JJDM_FIELD_NUMBER = 1;
    private boolean hasJjdm;
    private java.lang.String jjdm_ = "";
    public boolean hasJjdm() { return hasJjdm; }
    public java.lang.String getJjdm() { return jjdm_; }
    
    // optional string jjjc = 2;
    public static final int JJJC_FIELD_NUMBER = 2;
    private boolean hasJjjc;
    private java.lang.String jjjc_ = "";
    public boolean hasJjjc() { return hasJjjc; }
    public java.lang.String getJjjc() { return jjjc_; }
    
    // optional string jzrq = 3;
    public static final int JZRQ_FIELD_NUMBER = 3;
    private boolean hasJzrq;
    private java.lang.String jzrq_ = "";
    public boolean hasJzrq() { return hasJzrq; }
    public java.lang.String getJzrq() { return jzrq_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasJjdm()) {
        output.writeString(1, getJjdm());
      }
      if (hasJjjc()) {
        output.writeString(2, getJjjc());
      }
      if (hasJzrq()) {
        output.writeString(3, getJzrq());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasJjdm()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getJjdm());
      }
      if (hasJjjc()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getJjjc());
      }
      if (hasJzrq()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getJzrq());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb other) {
        if (other == com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb.getDefaultInstance()) return this;
        if (other.hasJjdm()) {
          setJjdm(other.getJjdm());
        }
        if (other.hasJjjc()) {
          setJjjc(other.getJjjc());
        }
        if (other.hasJzrq()) {
          setJzrq(other.getJzrq());
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
              setJjdm(input.readString());
              break;
            }
            case 18: {
              setJjjc(input.readString());
              break;
            }
            case 26: {
              setJzrq(input.readString());
              break;
            }
          }
        }
      }
      
      
      // optional string jjdm = 1;
      public boolean hasJjdm() {
        return result.hasJjdm();
      }
      public java.lang.String getJjdm() {
        return result.getJjdm();
      }
      public Builder setJjdm(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasJjdm = true;
        result.jjdm_ = value;
        return this;
      }
      public Builder clearJjdm() {
        result.hasJjdm = false;
        result.jjdm_ = getDefaultInstance().getJjdm();
        return this;
      }
      
      // optional string jjjc = 2;
      public boolean hasJjjc() {
        return result.hasJjjc();
      }
      public java.lang.String getJjjc() {
        return result.getJjjc();
      }
      public Builder setJjjc(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasJjjc = true;
        result.jjjc_ = value;
        return this;
      }
      public Builder clearJjjc() {
        result.hasJjjc = false;
        result.jjjc_ = getDefaultInstance().getJjjc();
        return this;
      }
      
      // optional string jzrq = 3;
      public boolean hasJzrq() {
        return result.hasJzrq();
      }
      public java.lang.String getJzrq() {
        return result.getJzrq();
      }
      public Builder setJzrq(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasJzrq = true;
        result.jzrq_ = value;
        return this;
      }
      public Builder clearJzrq() {
        result.hasJzrq = false;
        result.jzrq_ = getDefaultInstance().getJzrq();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:FundHiddenStockInfoProb)
    }
    
    static {
      defaultInstance = new FundHiddenStockInfoProb(true);
      com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:FundHiddenStockInfoProb)
  }
  
  public static final class FundYxzcgMain extends
      com.google.protobuf.GeneratedMessage {
    // Use FundYxzcgMain.newBuilder() to construct.
    private FundYxzcgMain() {
      initFields();
    }
    private FundYxzcgMain(boolean noInit) {}
    
    private static final FundYxzcgMain defaultInstance;
    public static FundYxzcgMain getDefaultInstance() {
      return defaultInstance;
    }
    
    public FundYxzcgMain getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.internal_static_FundYxzcgMain_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.internal_static_FundYxzcgMain_fieldAccessorTable;
    }
    
    // optional .common.Common common = 1;
    public static final int COMMON_FIELD_NUMBER = 1;
    private boolean hasCommon;
    private com.howbuy.wireless.entity.protobuf.CommonProtos.Common common_;
    public boolean hasCommon() { return hasCommon; }
    public com.howbuy.wireless.entity.protobuf.CommonProtos.Common getCommon() { return common_; }
    
    // repeated .FundHiddenStockInfoProb fundHiddenStockInfoProb = 2;
    public static final int FUNDHIDDENSTOCKINFOPROB_FIELD_NUMBER = 2;
    private java.util.List<com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb> fundHiddenStockInfoProb_ =
      java.util.Collections.emptyList();
    public java.util.List<com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb> getFundHiddenStockInfoProbList() {
      return fundHiddenStockInfoProb_;
    }
    public int getFundHiddenStockInfoProbCount() { return fundHiddenStockInfoProb_.size(); }
    public com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb getFundHiddenStockInfoProb(int index) {
      return fundHiddenStockInfoProb_.get(index);
    }
    
    // optional int32 totalNum = 3;
    public static final int TOTALNUM_FIELD_NUMBER = 3;
    private boolean hasTotalNum;
    private int totalNum_ = 0;
    public boolean hasTotalNum() { return hasTotalNum; }
    public int getTotalNum() { return totalNum_; }
    
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
      for (com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb element : getFundHiddenStockInfoProbList()) {
        output.writeMessage(2, element);
      }
      if (hasTotalNum()) {
        output.writeInt32(3, getTotalNum());
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
      for (com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb element : getFundHiddenStockInfoProbList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, element);
      }
      if (hasTotalNum()) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, getTotalNum());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.fundHiddenStockInfoProb_ != java.util.Collections.EMPTY_LIST) {
          result.fundHiddenStockInfoProb_ =
            java.util.Collections.unmodifiableList(result.fundHiddenStockInfoProb_);
        }
        com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain other) {
        if (other == com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain.getDefaultInstance()) return this;
        if (other.hasCommon()) {
          mergeCommon(other.getCommon());
        }
        if (!other.fundHiddenStockInfoProb_.isEmpty()) {
          if (result.fundHiddenStockInfoProb_.isEmpty()) {
            result.fundHiddenStockInfoProb_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb>();
          }
          result.fundHiddenStockInfoProb_.addAll(other.fundHiddenStockInfoProb_);
        }
        if (other.hasTotalNum()) {
          setTotalNum(other.getTotalNum());
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
              com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb.Builder subBuilder = com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addFundHiddenStockInfoProb(subBuilder.buildPartial());
              break;
            }
            case 24: {
              setTotalNum(input.readInt32());
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
      
      // repeated .FundHiddenStockInfoProb fundHiddenStockInfoProb = 2;
      public java.util.List<com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb> getFundHiddenStockInfoProbList() {
        return java.util.Collections.unmodifiableList(result.fundHiddenStockInfoProb_);
      }
      public int getFundHiddenStockInfoProbCount() {
        return result.getFundHiddenStockInfoProbCount();
      }
      public com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb getFundHiddenStockInfoProb(int index) {
        return result.getFundHiddenStockInfoProb(index);
      }
      public Builder setFundHiddenStockInfoProb(int index, com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.fundHiddenStockInfoProb_.set(index, value);
        return this;
      }
      public Builder setFundHiddenStockInfoProb(int index, com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb.Builder builderForValue) {
        result.fundHiddenStockInfoProb_.set(index, builderForValue.build());
        return this;
      }
      public Builder addFundHiddenStockInfoProb(com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.fundHiddenStockInfoProb_.isEmpty()) {
          result.fundHiddenStockInfoProb_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb>();
        }
        result.fundHiddenStockInfoProb_.add(value);
        return this;
      }
      public Builder addFundHiddenStockInfoProb(com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb.Builder builderForValue) {
        if (result.fundHiddenStockInfoProb_.isEmpty()) {
          result.fundHiddenStockInfoProb_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb>();
        }
        result.fundHiddenStockInfoProb_.add(builderForValue.build());
        return this;
      }
      public Builder addAllFundHiddenStockInfoProb(
          java.lang.Iterable<? extends com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb> values) {
        if (result.fundHiddenStockInfoProb_.isEmpty()) {
          result.fundHiddenStockInfoProb_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb>();
        }
        super.addAll(values, result.fundHiddenStockInfoProb_);
        return this;
      }
      public Builder clearFundHiddenStockInfoProb() {
        result.fundHiddenStockInfoProb_ = java.util.Collections.emptyList();
        return this;
      }
      
      // optional int32 totalNum = 3;
      public boolean hasTotalNum() {
        return result.hasTotalNum();
      }
      public int getTotalNum() {
        return result.getTotalNum();
      }
      public Builder setTotalNum(int value) {
        result.hasTotalNum = true;
        result.totalNum_ = value;
        return this;
      }
      public Builder clearTotalNum() {
        result.hasTotalNum = false;
        result.totalNum_ = 0;
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:FundYxzcgMain)
    }
    
    static {
      defaultInstance = new FundYxzcgMain(true);
      com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:FundYxzcgMain)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_FundHiddenStockInfoProb_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_FundHiddenStockInfoProb_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_FundYxzcgMain_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_FundYxzcgMain_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017fundyxzcg.proto\032\014common.proto\"C\n\027FundH" +
      "iddenStockInfoProb\022\014\n\004jjdm\030\001 \001(\t\022\014\n\004jjjc" +
      "\030\002 \001(\t\022\014\n\004jzrq\030\003 \001(\t\"|\n\rFundYxzcgMain\022\036\n" +
      "\006common\030\001 \001(\0132\016.common.Common\0229\n\027fundHid" +
      "denStockInfoProb\030\002 \003(\0132\030.FundHiddenStock" +
      "InfoProb\022\020\n\010totalNum\030\003 \001(\005B<\n#com.howbuy" +
      ".wireless.entity.protobufB\025FundHiddenSto" +
      "ckProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_FundHiddenStockInfoProb_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_FundHiddenStockInfoProb_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_FundHiddenStockInfoProb_descriptor,
              new java.lang.String[] { "Jjdm", "Jjjc", "Jzrq", },
              com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb.class,
              com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundHiddenStockInfoProb.Builder.class);
          internal_static_FundYxzcgMain_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_FundYxzcgMain_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_FundYxzcgMain_descriptor,
              new java.lang.String[] { "Common", "FundHiddenStockInfoProb", "TotalNum", },
              com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain.class,
              com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain.Builder.class);
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