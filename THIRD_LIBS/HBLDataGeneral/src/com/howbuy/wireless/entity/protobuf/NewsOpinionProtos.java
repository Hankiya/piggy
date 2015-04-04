// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: newsOpinion.proto

package com.howbuy.wireless.entity.protobuf;

public final class NewsOpinionProtos {
  private NewsOpinionProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class NewsOpinion extends
      com.google.protobuf.GeneratedMessage {
    // Use NewsOpinion.newBuilder() to construct.
    private NewsOpinion() {
      initFields();
    }
    private NewsOpinion(boolean noInit) {}
    
    private static final NewsOpinion defaultInstance;
    public static NewsOpinion getDefaultInstance() {
      return defaultInstance;
    }
    
    public NewsOpinion getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.internal_static_NewsOpinion_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.internal_static_NewsOpinion_fieldAccessorTable;
    }
    
    // optional string title = 1;
    public static final int TITLE_FIELD_NUMBER = 1;
    private boolean hasTitle;
    private java.lang.String title_ = "";
    public boolean hasTitle() { return hasTitle; }
    public java.lang.String getTitle() { return title_; }
    
    // optional string label = 2;
    public static final int LABEL_FIELD_NUMBER = 2;
    private boolean hasLabel;
    private java.lang.String label_ = "";
    public boolean hasLabel() { return hasLabel; }
    public java.lang.String getLabel() { return label_; }
    
    // optional sint64 publishTime = 3;
    public static final int PUBLISHTIME_FIELD_NUMBER = 3;
    private boolean hasPublishTime;
    private long publishTime_ = 0L;
    public boolean hasPublishTime() { return hasPublishTime; }
    public long getPublishTime() { return publishTime_; }
    
    // optional string contentType = 4;
    public static final int CONTENTTYPE_FIELD_NUMBER = 4;
    private boolean hasContentType;
    private java.lang.String contentType_ = "";
    public boolean hasContentType() { return hasContentType; }
    public java.lang.String getContentType() { return contentType_; }
    
    // optional sint64 id = 5;
    public static final int ID_FIELD_NUMBER = 5;
    private boolean hasId;
    private long id_ = 0L;
    public boolean hasId() { return hasId; }
    public long getId() { return id_; }
    
    // optional string tagName = 6;
    public static final int TAGNAME_FIELD_NUMBER = 6;
    private boolean hasTagName;
    private java.lang.String tagName_ = "";
    public boolean hasTagName() { return hasTagName; }
    public java.lang.String getTagName() { return tagName_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasTitle()) {
        output.writeString(1, getTitle());
      }
      if (hasLabel()) {
        output.writeString(2, getLabel());
      }
      if (hasPublishTime()) {
        output.writeSInt64(3, getPublishTime());
      }
      if (hasContentType()) {
        output.writeString(4, getContentType());
      }
      if (hasId()) {
        output.writeSInt64(5, getId());
      }
      if (hasTagName()) {
        output.writeString(6, getTagName());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasTitle()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getTitle());
      }
      if (hasLabel()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getLabel());
      }
      if (hasPublishTime()) {
        size += com.google.protobuf.CodedOutputStream
          .computeSInt64Size(3, getPublishTime());
      }
      if (hasContentType()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(4, getContentType());
      }
      if (hasId()) {
        size += com.google.protobuf.CodedOutputStream
          .computeSInt64Size(5, getId());
      }
      if (hasTagName()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(6, getTagName());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion other) {
        if (other == com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion.getDefaultInstance()) return this;
        if (other.hasTitle()) {
          setTitle(other.getTitle());
        }
        if (other.hasLabel()) {
          setLabel(other.getLabel());
        }
        if (other.hasPublishTime()) {
          setPublishTime(other.getPublishTime());
        }
        if (other.hasContentType()) {
          setContentType(other.getContentType());
        }
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasTagName()) {
          setTagName(other.getTagName());
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
              setTitle(input.readString());
              break;
            }
            case 18: {
              setLabel(input.readString());
              break;
            }
            case 24: {
              setPublishTime(input.readSInt64());
              break;
            }
            case 34: {
              setContentType(input.readString());
              break;
            }
            case 40: {
              setId(input.readSInt64());
              break;
            }
            case 50: {
              setTagName(input.readString());
              break;
            }
          }
        }
      }
      
      
      // optional string title = 1;
      public boolean hasTitle() {
        return result.hasTitle();
      }
      public java.lang.String getTitle() {
        return result.getTitle();
      }
      public Builder setTitle(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasTitle = true;
        result.title_ = value;
        return this;
      }
      public Builder clearTitle() {
        result.hasTitle = false;
        result.title_ = getDefaultInstance().getTitle();
        return this;
      }
      
      // optional string label = 2;
      public boolean hasLabel() {
        return result.hasLabel();
      }
      public java.lang.String getLabel() {
        return result.getLabel();
      }
      public Builder setLabel(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasLabel = true;
        result.label_ = value;
        return this;
      }
      public Builder clearLabel() {
        result.hasLabel = false;
        result.label_ = getDefaultInstance().getLabel();
        return this;
      }
      
      // optional sint64 publishTime = 3;
      public boolean hasPublishTime() {
        return result.hasPublishTime();
      }
      public long getPublishTime() {
        return result.getPublishTime();
      }
      public Builder setPublishTime(long value) {
        result.hasPublishTime = true;
        result.publishTime_ = value;
        return this;
      }
      public Builder clearPublishTime() {
        result.hasPublishTime = false;
        result.publishTime_ = 0L;
        return this;
      }
      
      // optional string contentType = 4;
      public boolean hasContentType() {
        return result.hasContentType();
      }
      public java.lang.String getContentType() {
        return result.getContentType();
      }
      public Builder setContentType(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasContentType = true;
        result.contentType_ = value;
        return this;
      }
      public Builder clearContentType() {
        result.hasContentType = false;
        result.contentType_ = getDefaultInstance().getContentType();
        return this;
      }
      
      // optional sint64 id = 5;
      public boolean hasId() {
        return result.hasId();
      }
      public long getId() {
        return result.getId();
      }
      public Builder setId(long value) {
        result.hasId = true;
        result.id_ = value;
        return this;
      }
      public Builder clearId() {
        result.hasId = false;
        result.id_ = 0L;
        return this;
      }
      
      // optional string tagName = 6;
      public boolean hasTagName() {
        return result.hasTagName();
      }
      public java.lang.String getTagName() {
        return result.getTagName();
      }
      public Builder setTagName(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasTagName = true;
        result.tagName_ = value;
        return this;
      }
      public Builder clearTagName() {
        result.hasTagName = false;
        result.tagName_ = getDefaultInstance().getTagName();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:NewsOpinion)
    }
    
    static {
      defaultInstance = new NewsOpinion(true);
      com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:NewsOpinion)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_NewsOpinion_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_NewsOpinion_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021newsOpinion.proto\"r\n\013NewsOpinion\022\r\n\005ti" +
      "tle\030\001 \001(\t\022\r\n\005label\030\002 \001(\t\022\023\n\013publishTime\030" +
      "\003 \001(\022\022\023\n\013contentType\030\004 \001(\t\022\n\n\002id\030\005 \001(\022\022\017" +
      "\n\007tagName\030\006 \001(\tB8\n#com.howbuy.wireless.e" +
      "ntity.protobufB\021NewsOpinionProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_NewsOpinion_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_NewsOpinion_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_NewsOpinion_descriptor,
              new java.lang.String[] { "Title", "Label", "PublishTime", "ContentType", "Id", "TagName", },
              com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion.class,
              com.howbuy.wireless.entity.protobuf.NewsOpinionProtos.NewsOpinion.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  public static void internalForceInit() {}
  
  // @@protoc_insertion_point(outer_class_scope)
}
