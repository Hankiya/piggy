// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: commentInfoList.proto

package com.howbuy.wireless.entity.protobuf;

public final class CommentInfoListProto {
  private CommentInfoListProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class CommentInfoList extends
      com.google.protobuf.GeneratedMessage {
    // Use CommentInfoList.newBuilder() to construct.
    private CommentInfoList() {
      initFields();
    }
    private CommentInfoList(boolean noInit) {}
    
    private static final CommentInfoList defaultInstance;
    public static CommentInfoList getDefaultInstance() {
      return defaultInstance;
    }
    
    public CommentInfoList getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.CommentInfoListProto.internal_static_CommentInfoList_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.CommentInfoListProto.internal_static_CommentInfoList_fieldAccessorTable;
    }
    
    // optional .common.Common common = 1;
    public static final int COMMON_FIELD_NUMBER = 1;
    private boolean hasCommon;
    private com.howbuy.wireless.entity.protobuf.CommonProtos.Common common_;
    public boolean hasCommon() { return hasCommon; }
    public com.howbuy.wireless.entity.protobuf.CommonProtos.Common getCommon() { return common_; }
    
    // required sint32 totalNum = 2;
    public static final int TOTALNUM_FIELD_NUMBER = 2;
    private boolean hasTotalNum;
    private int totalNum_ = 0;
    public boolean hasTotalNum() { return hasTotalNum; }
    public int getTotalNum() { return totalNum_; }
    
    // repeated .CommentInfo commentInfo = 3;
    public static final int COMMENTINFO_FIELD_NUMBER = 3;
    private java.util.List<com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo> commentInfo_ =
      java.util.Collections.emptyList();
    public java.util.List<com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo> getCommentInfoList() {
      return commentInfo_;
    }
    public int getCommentInfoCount() { return commentInfo_.size(); }
    public com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo getCommentInfo(int index) {
      return commentInfo_.get(index);
    }
    
    private void initFields() {
      common_ = com.howbuy.wireless.entity.protobuf.CommonProtos.Common.getDefaultInstance();
    }
    public final boolean isInitialized() {
      if (!hasTotalNum) return false;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasCommon()) {
        output.writeMessage(1, getCommon());
      }
      if (hasTotalNum()) {
        output.writeSInt32(2, getTotalNum());
      }
      for (com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo element : getCommentInfoList()) {
        output.writeMessage(3, element);
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
      if (hasTotalNum()) {
        size += com.google.protobuf.CodedOutputStream
          .computeSInt32Size(2, getTotalNum());
      }
      for (com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo element : getCommentInfoList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, element);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.commentInfo_ != java.util.Collections.EMPTY_LIST) {
          result.commentInfo_ =
            java.util.Collections.unmodifiableList(result.commentInfo_);
        }
        com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList other) {
        if (other == com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList.getDefaultInstance()) return this;
        if (other.hasCommon()) {
          mergeCommon(other.getCommon());
        }
        if (other.hasTotalNum()) {
          setTotalNum(other.getTotalNum());
        }
        if (!other.commentInfo_.isEmpty()) {
          if (result.commentInfo_.isEmpty()) {
            result.commentInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo>();
          }
          result.commentInfo_.addAll(other.commentInfo_);
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
            case 16: {
              setTotalNum(input.readSInt32());
              break;
            }
            case 26: {
              com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo.Builder subBuilder = com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addCommentInfo(subBuilder.buildPartial());
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
      
      // required sint32 totalNum = 2;
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
      
      // repeated .CommentInfo commentInfo = 3;
      public java.util.List<com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo> getCommentInfoList() {
        return java.util.Collections.unmodifiableList(result.commentInfo_);
      }
      public int getCommentInfoCount() {
        return result.getCommentInfoCount();
      }
      public com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo getCommentInfo(int index) {
        return result.getCommentInfo(index);
      }
      public Builder setCommentInfo(int index, com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.commentInfo_.set(index, value);
        return this;
      }
      public Builder setCommentInfo(int index, com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo.Builder builderForValue) {
        result.commentInfo_.set(index, builderForValue.build());
        return this;
      }
      public Builder addCommentInfo(com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.commentInfo_.isEmpty()) {
          result.commentInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo>();
        }
        result.commentInfo_.add(value);
        return this;
      }
      public Builder addCommentInfo(com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo.Builder builderForValue) {
        if (result.commentInfo_.isEmpty()) {
          result.commentInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo>();
        }
        result.commentInfo_.add(builderForValue.build());
        return this;
      }
      public Builder addAllCommentInfo(
          java.lang.Iterable<? extends com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo> values) {
        if (result.commentInfo_.isEmpty()) {
          result.commentInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.CommentInfoProtos.CommentInfo>();
        }
        super.addAll(values, result.commentInfo_);
        return this;
      }
      public Builder clearCommentInfo() {
        result.commentInfo_ = java.util.Collections.emptyList();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:CommentInfoList)
    }
    
    static {
      defaultInstance = new CommentInfoList(true);
      com.howbuy.wireless.entity.protobuf.CommentInfoListProto.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:CommentInfoList)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_CommentInfoList_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_CommentInfoList_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\025commentInfoList.proto\032\021commentInfo.pro" +
      "to\032\014common.proto\"f\n\017CommentInfoList\022\036\n\006c" +
      "ommon\030\001 \001(\0132\016.common.Common\022\020\n\010totalNum\030" +
      "\002 \002(\021\022!\n\013commentInfo\030\003 \003(\0132\014.CommentInfo" +
      "B;\n#com.howbuy.wireless.entity.protobufB" +
      "\024CommentInfoListProto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_CommentInfoList_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_CommentInfoList_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_CommentInfoList_descriptor,
              new java.lang.String[] { "Common", "TotalNum", "CommentInfo", },
              com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList.class,
              com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.howbuy.wireless.entity.protobuf.CommentInfoProtos.getDescriptor(),
          com.howbuy.wireless.entity.protobuf.CommonProtos.getDescriptor(),
        }, assigner);
  }
  
  public static void internalForceInit() {}
  
  // @@protoc_insertion_point(outer_class_scope)
}