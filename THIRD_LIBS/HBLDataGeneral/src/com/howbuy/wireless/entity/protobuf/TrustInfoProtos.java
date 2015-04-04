// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: trustInfo.proto

package com.howbuy.wireless.entity.protobuf;

public final class TrustInfoProtos {
  private TrustInfoProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class TrustInfo extends
      com.google.protobuf.GeneratedMessage {
    // Use TrustInfo.newBuilder() to construct.
    private TrustInfo() {
      initFields();
    }
    private TrustInfo(boolean noInit) {}
    
    private static final TrustInfo defaultInstance;
    public static TrustInfo getDefaultInstance() {
      return defaultInstance;
    }
    
    public TrustInfo getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.TrustInfoProtos.internal_static_TrustInfo_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.TrustInfoProtos.internal_static_TrustInfo_fieldAccessorTable;
    }
    
    // optional string id = 1;
    public static final int ID_FIELD_NUMBER = 1;
    private boolean hasId;
    private java.lang.String id_ = "";
    public boolean hasId() { return hasId; }
    public java.lang.String getId() { return id_; }
    
    // optional string name = 2;
    public static final int NAME_FIELD_NUMBER = 2;
    private boolean hasName;
    private java.lang.String name_ = "";
    public boolean hasName() { return hasName; }
    public java.lang.String getName() { return name_; }
    
    // optional string timeline = 3;
    public static final int TIMELINE_FIELD_NUMBER = 3;
    private boolean hasTimeline;
    private java.lang.String timeline_ = "";
    public boolean hasTimeline() { return hasTimeline; }
    public java.lang.String getTimeline() { return timeline_; }
    
    // optional string yqnhsy = 4;
    public static final int YQNHSY_FIELD_NUMBER = 4;
    private boolean hasYqnhsy;
    private java.lang.String yqnhsy_ = "";
    public boolean hasYqnhsy() { return hasYqnhsy; }
    public java.lang.String getYqnhsy() { return yqnhsy_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasId()) {
        output.writeString(1, getId());
      }
      if (hasName()) {
        output.writeString(2, getName());
      }
      if (hasTimeline()) {
        output.writeString(3, getTimeline());
      }
      if (hasYqnhsy()) {
        output.writeString(4, getYqnhsy());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasId()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getId());
      }
      if (hasName()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getName());
      }
      if (hasTimeline()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getTimeline());
      }
      if (hasYqnhsy()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(4, getYqnhsy());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo other) {
        if (other == com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo.getDefaultInstance()) return this;
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasName()) {
          setName(other.getName());
        }
        if (other.hasTimeline()) {
          setTimeline(other.getTimeline());
        }
        if (other.hasYqnhsy()) {
          setYqnhsy(other.getYqnhsy());
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
              setId(input.readString());
              break;
            }
            case 18: {
              setName(input.readString());
              break;
            }
            case 26: {
              setTimeline(input.readString());
              break;
            }
            case 34: {
              setYqnhsy(input.readString());
              break;
            }
          }
        }
      }
      
      
      // optional string id = 1;
      public boolean hasId() {
        return result.hasId();
      }
      public java.lang.String getId() {
        return result.getId();
      }
      public Builder setId(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasId = true;
        result.id_ = value;
        return this;
      }
      public Builder clearId() {
        result.hasId = false;
        result.id_ = getDefaultInstance().getId();
        return this;
      }
      
      // optional string name = 2;
      public boolean hasName() {
        return result.hasName();
      }
      public java.lang.String getName() {
        return result.getName();
      }
      public Builder setName(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasName = true;
        result.name_ = value;
        return this;
      }
      public Builder clearName() {
        result.hasName = false;
        result.name_ = getDefaultInstance().getName();
        return this;
      }
      
      // optional string timeline = 3;
      public boolean hasTimeline() {
        return result.hasTimeline();
      }
      public java.lang.String getTimeline() {
        return result.getTimeline();
      }
      public Builder setTimeline(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasTimeline = true;
        result.timeline_ = value;
        return this;
      }
      public Builder clearTimeline() {
        result.hasTimeline = false;
        result.timeline_ = getDefaultInstance().getTimeline();
        return this;
      }
      
      // optional string yqnhsy = 4;
      public boolean hasYqnhsy() {
        return result.hasYqnhsy();
      }
      public java.lang.String getYqnhsy() {
        return result.getYqnhsy();
      }
      public Builder setYqnhsy(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasYqnhsy = true;
        result.yqnhsy_ = value;
        return this;
      }
      public Builder clearYqnhsy() {
        result.hasYqnhsy = false;
        result.yqnhsy_ = getDefaultInstance().getYqnhsy();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:TrustInfo)
    }
    
    static {
      defaultInstance = new TrustInfo(true);
      com.howbuy.wireless.entity.protobuf.TrustInfoProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:TrustInfo)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TrustInfo_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TrustInfo_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017trustInfo.proto\"G\n\tTrustInfo\022\n\n\002id\030\001 \001" +
      "(\t\022\014\n\004name\030\002 \001(\t\022\020\n\010timeline\030\003 \001(\t\022\016\n\006yq" +
      "nhsy\030\004 \001(\tB6\n#com.howbuy.wireless.entity" +
      ".protobufB\017TrustInfoProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_TrustInfo_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_TrustInfo_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TrustInfo_descriptor,
              new java.lang.String[] { "Id", "Name", "Timeline", "Yqnhsy", },
              com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo.class,
              com.howbuy.wireless.entity.protobuf.TrustInfoProtos.TrustInfo.Builder.class);
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