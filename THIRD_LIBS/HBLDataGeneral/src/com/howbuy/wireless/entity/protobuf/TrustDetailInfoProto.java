// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: trustDetailInfo.proto

package com.howbuy.wireless.entity.protobuf;

public final class TrustDetailInfoProto {
  private TrustDetailInfoProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class TrustDetailInfo extends
      com.google.protobuf.GeneratedMessage {
    // Use TrustDetailInfo.newBuilder() to construct.
    private TrustDetailInfo() {
      initFields();
    }
    private TrustDetailInfo(boolean noInit) {}
    
    private static final TrustDetailInfo defaultInstance;
    public static TrustDetailInfo getDefaultInstance() {
      return defaultInstance;
    }
    
    public TrustDetailInfo getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.internal_static_TrustDetailInfo_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.internal_static_TrustDetailInfo_fieldAccessorTable;
    }
    
    // optional .common.Common common = 1;
    public static final int COMMON_FIELD_NUMBER = 1;
    private boolean hasCommon;
    private com.howbuy.wireless.entity.protobuf.CommonProtos.Common common_;
    public boolean hasCommon() { return hasCommon; }
    public com.howbuy.wireless.entity.protobuf.CommonProtos.Common getCommon() { return common_; }
    
    // optional string fullname = 2;
    public static final int FULLNAME_FIELD_NUMBER = 2;
    private boolean hasFullname;
    private java.lang.String fullname_ = "";
    public boolean hasFullname() { return hasFullname; }
    public java.lang.String getFullname() { return fullname_; }
    
    // optional string str = 3;
    public static final int STR_FIELD_NUMBER = 3;
    private boolean hasStr;
    private java.lang.String str_ = "";
    public boolean hasStr() { return hasStr; }
    public java.lang.String getStr() { return str_; }
    
    // optional string yqnhsysm = 4;
    public static final int YQNHSYSM_FIELD_NUMBER = 4;
    private boolean hasYqnhsysm;
    private java.lang.String yqnhsysm_ = "";
    public boolean hasYqnhsysm() { return hasYqnhsysm; }
    public java.lang.String getYqnhsysm() { return yqnhsysm_; }
    
    // optional string yjfxgm = 5;
    public static final int YJFXGM_FIELD_NUMBER = 5;
    private boolean hasYjfxgm;
    private java.lang.String yjfxgm_ = "";
    public boolean hasYjfxgm() { return hasYjfxgm; }
    public java.lang.String getYjfxgm() { return yjfxgm_; }
    
    // optional string zdrgzj = 6;
    public static final int ZDRGZJ_FIELD_NUMBER = 6;
    private boolean hasZdrgzj;
    private java.lang.String zdrgzj_ = "";
    public boolean hasZdrgzj() { return hasZdrgzj; }
    public java.lang.String getZdrgzj() { return zdrgzj_; }
    
    // optional string dp = 7;
    public static final int DP_FIELD_NUMBER = 7;
    private boolean hasDp;
    private java.lang.String dp_ = "";
    public boolean hasDp() { return hasDp; }
    public java.lang.String getDp() { return dp_; }
    
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
      if (hasFullname()) {
        output.writeString(2, getFullname());
      }
      if (hasStr()) {
        output.writeString(3, getStr());
      }
      if (hasYqnhsysm()) {
        output.writeString(4, getYqnhsysm());
      }
      if (hasYjfxgm()) {
        output.writeString(5, getYjfxgm());
      }
      if (hasZdrgzj()) {
        output.writeString(6, getZdrgzj());
      }
      if (hasDp()) {
        output.writeString(7, getDp());
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
      if (hasFullname()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getFullname());
      }
      if (hasStr()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getStr());
      }
      if (hasYqnhsysm()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(4, getYqnhsysm());
      }
      if (hasYjfxgm()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(5, getYjfxgm());
      }
      if (hasZdrgzj()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(6, getZdrgzj());
      }
      if (hasDp()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(7, getDp());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo other) {
        if (other == com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo.getDefaultInstance()) return this;
        if (other.hasCommon()) {
          mergeCommon(other.getCommon());
        }
        if (other.hasFullname()) {
          setFullname(other.getFullname());
        }
        if (other.hasStr()) {
          setStr(other.getStr());
        }
        if (other.hasYqnhsysm()) {
          setYqnhsysm(other.getYqnhsysm());
        }
        if (other.hasYjfxgm()) {
          setYjfxgm(other.getYjfxgm());
        }
        if (other.hasZdrgzj()) {
          setZdrgzj(other.getZdrgzj());
        }
        if (other.hasDp()) {
          setDp(other.getDp());
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
              setFullname(input.readString());
              break;
            }
            case 26: {
              setStr(input.readString());
              break;
            }
            case 34: {
              setYqnhsysm(input.readString());
              break;
            }
            case 42: {
              setYjfxgm(input.readString());
              break;
            }
            case 50: {
              setZdrgzj(input.readString());
              break;
            }
            case 58: {
              setDp(input.readString());
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
      
      // optional string fullname = 2;
      public boolean hasFullname() {
        return result.hasFullname();
      }
      public java.lang.String getFullname() {
        return result.getFullname();
      }
      public Builder setFullname(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasFullname = true;
        result.fullname_ = value;
        return this;
      }
      public Builder clearFullname() {
        result.hasFullname = false;
        result.fullname_ = getDefaultInstance().getFullname();
        return this;
      }
      
      // optional string str = 3;
      public boolean hasStr() {
        return result.hasStr();
      }
      public java.lang.String getStr() {
        return result.getStr();
      }
      public Builder setStr(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasStr = true;
        result.str_ = value;
        return this;
      }
      public Builder clearStr() {
        result.hasStr = false;
        result.str_ = getDefaultInstance().getStr();
        return this;
      }
      
      // optional string yqnhsysm = 4;
      public boolean hasYqnhsysm() {
        return result.hasYqnhsysm();
      }
      public java.lang.String getYqnhsysm() {
        return result.getYqnhsysm();
      }
      public Builder setYqnhsysm(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasYqnhsysm = true;
        result.yqnhsysm_ = value;
        return this;
      }
      public Builder clearYqnhsysm() {
        result.hasYqnhsysm = false;
        result.yqnhsysm_ = getDefaultInstance().getYqnhsysm();
        return this;
      }
      
      // optional string yjfxgm = 5;
      public boolean hasYjfxgm() {
        return result.hasYjfxgm();
      }
      public java.lang.String getYjfxgm() {
        return result.getYjfxgm();
      }
      public Builder setYjfxgm(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasYjfxgm = true;
        result.yjfxgm_ = value;
        return this;
      }
      public Builder clearYjfxgm() {
        result.hasYjfxgm = false;
        result.yjfxgm_ = getDefaultInstance().getYjfxgm();
        return this;
      }
      
      // optional string zdrgzj = 6;
      public boolean hasZdrgzj() {
        return result.hasZdrgzj();
      }
      public java.lang.String getZdrgzj() {
        return result.getZdrgzj();
      }
      public Builder setZdrgzj(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasZdrgzj = true;
        result.zdrgzj_ = value;
        return this;
      }
      public Builder clearZdrgzj() {
        result.hasZdrgzj = false;
        result.zdrgzj_ = getDefaultInstance().getZdrgzj();
        return this;
      }
      
      // optional string dp = 7;
      public boolean hasDp() {
        return result.hasDp();
      }
      public java.lang.String getDp() {
        return result.getDp();
      }
      public Builder setDp(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasDp = true;
        result.dp_ = value;
        return this;
      }
      public Builder clearDp() {
        result.hasDp = false;
        result.dp_ = getDefaultInstance().getDp();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:TrustDetailInfo)
    }
    
    static {
      defaultInstance = new TrustDetailInfo(true);
      com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:TrustDetailInfo)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TrustDetailInfo_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TrustDetailInfo_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\025trustDetailInfo.proto\032\014common.proto\"\216\001" +
      "\n\017TrustDetailInfo\022\036\n\006common\030\001 \001(\0132\016.comm" +
      "on.Common\022\020\n\010fullname\030\002 \001(\t\022\013\n\003str\030\003 \001(\t" +
      "\022\020\n\010yqnhsysm\030\004 \001(\t\022\016\n\006yjfxgm\030\005 \001(\t\022\016\n\006zd" +
      "rgzj\030\006 \001(\t\022\n\n\002dp\030\007 \001(\tB;\n#com.howbuy.wir" +
      "eless.entity.protobufB\024TrustDetailInfoPr" +
      "oto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_TrustDetailInfo_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_TrustDetailInfo_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TrustDetailInfo_descriptor,
              new java.lang.String[] { "Common", "Fullname", "Str", "Yqnhsysm", "Yjfxgm", "Zdrgzj", "Dp", },
              com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo.class,
              com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo.Builder.class);
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