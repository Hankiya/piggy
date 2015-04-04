// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: managerInfo.proto

package com.howbuy.wireless.entity.protobuf;

public final class ManagerInfoProtos {
  private ManagerInfoProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class ManagerInfo extends
      com.google.protobuf.GeneratedMessage {
    // Use ManagerInfo.newBuilder() to construct.
    private ManagerInfo() {
      initFields();
    }
    private ManagerInfo(boolean noInit) {}
    
    private static final ManagerInfo defaultInstance;
    public static ManagerInfo getDefaultInstance() {
      return defaultInstance;
    }
    
    public ManagerInfo getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.internal_static_ManagerInfo_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.internal_static_ManagerInfo_fieldAccessorTable;
    }
    
    // optional string ryxm = 1;
    public static final int RYXM_FIELD_NUMBER = 1;
    private boolean hasRyxm;
    private java.lang.String ryxm_ = "";
    public boolean hasRyxm() { return hasRyxm; }
    public java.lang.String getRyxm() { return ryxm_; }
    
    // optional string rydm = 2;
    public static final int RYDM_FIELD_NUMBER = 2;
    private boolean hasRydm;
    private java.lang.String rydm_ = "";
    public boolean hasRydm() { return hasRydm; }
    public java.lang.String getRydm() { return rydm_; }
    
    // optional string type = 3;
    public static final int TYPE_FIELD_NUMBER = 3;
    private boolean hasType;
    private java.lang.String type_ = "";
    public boolean hasType() { return hasType; }
    public java.lang.String getType() { return type_; }
    
    // optional string rzsj = 4;
    public static final int RZSJ_FIELD_NUMBER = 4;
    private boolean hasRzsj;
    private java.lang.String rzsj_ = "";
    public boolean hasRzsj() { return hasRzsj; }
    public java.lang.String getRzsj() { return rzsj_; }
    
    // optional string rzqj = 5;
    public static final int RZQJ_FIELD_NUMBER = 5;
    private boolean hasRzqj;
    private java.lang.String rzqj_ = "";
    public boolean hasRzqj() { return hasRzqj; }
    public java.lang.String getRzqj() { return rzqj_; }
    
    // optional string tlpj = 6;
    public static final int TLPJ_FIELD_NUMBER = 6;
    private boolean hasTlpj;
    private java.lang.String tlpj_ = "";
    public boolean hasTlpj() { return hasTlpj; }
    public java.lang.String getTlpj() { return tlpj_; }
    
    // optional string rqhb = 7;
    public static final int RQHB_FIELD_NUMBER = 7;
    private boolean hasRqhb;
    private java.lang.String rqhb_ = "";
    public boolean hasRqhb() { return hasRqhb; }
    public java.lang.String getRqhb() { return rqhb_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasRyxm()) {
        output.writeString(1, getRyxm());
      }
      if (hasRydm()) {
        output.writeString(2, getRydm());
      }
      if (hasType()) {
        output.writeString(3, getType());
      }
      if (hasRzsj()) {
        output.writeString(4, getRzsj());
      }
      if (hasRzqj()) {
        output.writeString(5, getRzqj());
      }
      if (hasTlpj()) {
        output.writeString(6, getTlpj());
      }
      if (hasRqhb()) {
        output.writeString(7, getRqhb());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasRyxm()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getRyxm());
      }
      if (hasRydm()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getRydm());
      }
      if (hasType()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getType());
      }
      if (hasRzsj()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(4, getRzsj());
      }
      if (hasRzqj()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(5, getRzqj());
      }
      if (hasTlpj()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(6, getTlpj());
      }
      if (hasRqhb()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(7, getRqhb());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo other) {
        if (other == com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo.getDefaultInstance()) return this;
        if (other.hasRyxm()) {
          setRyxm(other.getRyxm());
        }
        if (other.hasRydm()) {
          setRydm(other.getRydm());
        }
        if (other.hasType()) {
          setType(other.getType());
        }
        if (other.hasRzsj()) {
          setRzsj(other.getRzsj());
        }
        if (other.hasRzqj()) {
          setRzqj(other.getRzqj());
        }
        if (other.hasTlpj()) {
          setTlpj(other.getTlpj());
        }
        if (other.hasRqhb()) {
          setRqhb(other.getRqhb());
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
              setRyxm(input.readString());
              break;
            }
            case 18: {
              setRydm(input.readString());
              break;
            }
            case 26: {
              setType(input.readString());
              break;
            }
            case 34: {
              setRzsj(input.readString());
              break;
            }
            case 42: {
              setRzqj(input.readString());
              break;
            }
            case 50: {
              setTlpj(input.readString());
              break;
            }
            case 58: {
              setRqhb(input.readString());
              break;
            }
          }
        }
      }
      
      
      // optional string ryxm = 1;
      public boolean hasRyxm() {
        return result.hasRyxm();
      }
      public java.lang.String getRyxm() {
        return result.getRyxm();
      }
      public Builder setRyxm(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasRyxm = true;
        result.ryxm_ = value;
        return this;
      }
      public Builder clearRyxm() {
        result.hasRyxm = false;
        result.ryxm_ = getDefaultInstance().getRyxm();
        return this;
      }
      
      // optional string rydm = 2;
      public boolean hasRydm() {
        return result.hasRydm();
      }
      public java.lang.String getRydm() {
        return result.getRydm();
      }
      public Builder setRydm(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasRydm = true;
        result.rydm_ = value;
        return this;
      }
      public Builder clearRydm() {
        result.hasRydm = false;
        result.rydm_ = getDefaultInstance().getRydm();
        return this;
      }
      
      // optional string type = 3;
      public boolean hasType() {
        return result.hasType();
      }
      public java.lang.String getType() {
        return result.getType();
      }
      public Builder setType(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasType = true;
        result.type_ = value;
        return this;
      }
      public Builder clearType() {
        result.hasType = false;
        result.type_ = getDefaultInstance().getType();
        return this;
      }
      
      // optional string rzsj = 4;
      public boolean hasRzsj() {
        return result.hasRzsj();
      }
      public java.lang.String getRzsj() {
        return result.getRzsj();
      }
      public Builder setRzsj(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasRzsj = true;
        result.rzsj_ = value;
        return this;
      }
      public Builder clearRzsj() {
        result.hasRzsj = false;
        result.rzsj_ = getDefaultInstance().getRzsj();
        return this;
      }
      
      // optional string rzqj = 5;
      public boolean hasRzqj() {
        return result.hasRzqj();
      }
      public java.lang.String getRzqj() {
        return result.getRzqj();
      }
      public Builder setRzqj(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasRzqj = true;
        result.rzqj_ = value;
        return this;
      }
      public Builder clearRzqj() {
        result.hasRzqj = false;
        result.rzqj_ = getDefaultInstance().getRzqj();
        return this;
      }
      
      // optional string tlpj = 6;
      public boolean hasTlpj() {
        return result.hasTlpj();
      }
      public java.lang.String getTlpj() {
        return result.getTlpj();
      }
      public Builder setTlpj(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasTlpj = true;
        result.tlpj_ = value;
        return this;
      }
      public Builder clearTlpj() {
        result.hasTlpj = false;
        result.tlpj_ = getDefaultInstance().getTlpj();
        return this;
      }
      
      // optional string rqhb = 7;
      public boolean hasRqhb() {
        return result.hasRqhb();
      }
      public java.lang.String getRqhb() {
        return result.getRqhb();
      }
      public Builder setRqhb(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasRqhb = true;
        result.rqhb_ = value;
        return this;
      }
      public Builder clearRqhb() {
        result.hasRqhb = false;
        result.rqhb_ = getDefaultInstance().getRqhb();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:ManagerInfo)
    }
    
    static {
      defaultInstance = new ManagerInfo(true);
      com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:ManagerInfo)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ManagerInfo_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ManagerInfo_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021managerInfo.proto\"o\n\013ManagerInfo\022\014\n\004ry" +
      "xm\030\001 \001(\t\022\014\n\004rydm\030\002 \001(\t\022\014\n\004type\030\003 \001(\t\022\014\n\004" +
      "rzsj\030\004 \001(\t\022\014\n\004rzqj\030\005 \001(\t\022\014\n\004tlpj\030\006 \001(\t\022\014" +
      "\n\004rqhb\030\007 \001(\tB8\n#com.howbuy.wireless.enti" +
      "ty.protobufB\021ManagerInfoProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_ManagerInfo_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_ManagerInfo_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ManagerInfo_descriptor,
              new java.lang.String[] { "Ryxm", "Rydm", "Type", "Rzsj", "Rzqj", "Tlpj", "Rqhb", },
              com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo.class,
              com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo.Builder.class);
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
