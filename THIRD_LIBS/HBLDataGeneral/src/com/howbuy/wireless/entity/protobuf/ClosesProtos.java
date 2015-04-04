// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: closes.proto

package com.howbuy.wireless.entity.protobuf;

public final class ClosesProtos {
  private ClosesProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class Closes extends
      com.google.protobuf.GeneratedMessage {
    // Use Closes.newBuilder() to construct.
    private Closes() {
      initFields();
    }
    private Closes(boolean noInit) {}
    
    private static final Closes defaultInstance;
    public static Closes getDefaultInstance() {
      return defaultInstance;
    }
    
    public Closes getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.ClosesProtos.internal_static_Closes_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.ClosesProtos.internal_static_Closes_fieldAccessorTable;
    }
    
    // optional string jjdm = 1;
    public static final int JJDM_FIELD_NUMBER = 1;
    private boolean hasJjdm;
    private java.lang.String jjdm_ = "";
    public boolean hasJjdm() { return hasJjdm; }
    public java.lang.String getJjdm() { return jjdm_; }
    
    // optional string jzrq = 2;
    public static final int JZRQ_FIELD_NUMBER = 2;
    private boolean hasJzrq;
    private java.lang.String jzrq_ = "";
    public boolean hasJzrq() { return hasJzrq; }
    public java.lang.String getJzrq() { return jzrq_; }
    
    // optional string jjjz = 3;
    public static final int JJJZ_FIELD_NUMBER = 3;
    private boolean hasJjjz;
    private java.lang.String jjjz_ = "";
    public boolean hasJjjz() { return hasJjjz; }
    public java.lang.String getJjjz() { return jjjz_; }
    
    // optional string ljjz = 4;
    public static final int LJJZ_FIELD_NUMBER = 4;
    private boolean hasLjjz;
    private java.lang.String ljjz_ = "";
    public boolean hasLjjz() { return hasLjjz; }
    public java.lang.String getLjjz() { return ljjz_; }
    
    // optional string zyjl = 5;
    public static final int ZYJL_FIELD_NUMBER = 5;
    private boolean hasZyjl;
    private java.lang.String zyjl_ = "";
    public boolean hasZyjl() { return hasZyjl; }
    public java.lang.String getZyjl() { return zyjl_; }
    
    // optional string zfxz = 6;
    public static final int ZFXZ_FIELD_NUMBER = 6;
    private boolean hasZfxz;
    private java.lang.String zfxz_ = "";
    public boolean hasZfxz() { return hasZfxz; }
    public java.lang.String getZfxz() { return zfxz_; }
    
    // optional string dqrq = 7;
    public static final int DQRQ_FIELD_NUMBER = 7;
    private boolean hasDqrq;
    private java.lang.String dqrq_ = "";
    public boolean hasDqrq() { return hasDqrq; }
    public java.lang.String getDqrq() { return dqrq_; }
    
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
      if (hasJzrq()) {
        output.writeString(2, getJzrq());
      }
      if (hasJjjz()) {
        output.writeString(3, getJjjz());
      }
      if (hasLjjz()) {
        output.writeString(4, getLjjz());
      }
      if (hasZyjl()) {
        output.writeString(5, getZyjl());
      }
      if (hasZfxz()) {
        output.writeString(6, getZfxz());
      }
      if (hasDqrq()) {
        output.writeString(7, getDqrq());
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
      if (hasJzrq()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getJzrq());
      }
      if (hasJjjz()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getJjjz());
      }
      if (hasLjjz()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(4, getLjjz());
      }
      if (hasZyjl()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(5, getZyjl());
      }
      if (hasZfxz()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(6, getZfxz());
      }
      if (hasDqrq()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(7, getDqrq());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes other) {
        if (other == com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes.getDefaultInstance()) return this;
        if (other.hasJjdm()) {
          setJjdm(other.getJjdm());
        }
        if (other.hasJzrq()) {
          setJzrq(other.getJzrq());
        }
        if (other.hasJjjz()) {
          setJjjz(other.getJjjz());
        }
        if (other.hasLjjz()) {
          setLjjz(other.getLjjz());
        }
        if (other.hasZyjl()) {
          setZyjl(other.getZyjl());
        }
        if (other.hasZfxz()) {
          setZfxz(other.getZfxz());
        }
        if (other.hasDqrq()) {
          setDqrq(other.getDqrq());
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
              setJzrq(input.readString());
              break;
            }
            case 26: {
              setJjjz(input.readString());
              break;
            }
            case 34: {
              setLjjz(input.readString());
              break;
            }
            case 42: {
              setZyjl(input.readString());
              break;
            }
            case 50: {
              setZfxz(input.readString());
              break;
            }
            case 58: {
              setDqrq(input.readString());
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
      
      // optional string jzrq = 2;
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
      
      // optional string jjjz = 3;
      public boolean hasJjjz() {
        return result.hasJjjz();
      }
      public java.lang.String getJjjz() {
        return result.getJjjz();
      }
      public Builder setJjjz(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasJjjz = true;
        result.jjjz_ = value;
        return this;
      }
      public Builder clearJjjz() {
        result.hasJjjz = false;
        result.jjjz_ = getDefaultInstance().getJjjz();
        return this;
      }
      
      // optional string ljjz = 4;
      public boolean hasLjjz() {
        return result.hasLjjz();
      }
      public java.lang.String getLjjz() {
        return result.getLjjz();
      }
      public Builder setLjjz(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasLjjz = true;
        result.ljjz_ = value;
        return this;
      }
      public Builder clearLjjz() {
        result.hasLjjz = false;
        result.ljjz_ = getDefaultInstance().getLjjz();
        return this;
      }
      
      // optional string zyjl = 5;
      public boolean hasZyjl() {
        return result.hasZyjl();
      }
      public java.lang.String getZyjl() {
        return result.getZyjl();
      }
      public Builder setZyjl(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasZyjl = true;
        result.zyjl_ = value;
        return this;
      }
      public Builder clearZyjl() {
        result.hasZyjl = false;
        result.zyjl_ = getDefaultInstance().getZyjl();
        return this;
      }
      
      // optional string zfxz = 6;
      public boolean hasZfxz() {
        return result.hasZfxz();
      }
      public java.lang.String getZfxz() {
        return result.getZfxz();
      }
      public Builder setZfxz(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasZfxz = true;
        result.zfxz_ = value;
        return this;
      }
      public Builder clearZfxz() {
        result.hasZfxz = false;
        result.zfxz_ = getDefaultInstance().getZfxz();
        return this;
      }
      
      // optional string dqrq = 7;
      public boolean hasDqrq() {
        return result.hasDqrq();
      }
      public java.lang.String getDqrq() {
        return result.getDqrq();
      }
      public Builder setDqrq(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasDqrq = true;
        result.dqrq_ = value;
        return this;
      }
      public Builder clearDqrq() {
        result.hasDqrq = false;
        result.dqrq_ = getDefaultInstance().getDqrq();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:Closes)
    }
    
    static {
      defaultInstance = new Closes(true);
      com.howbuy.wireless.entity.protobuf.ClosesProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:Closes)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_Closes_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Closes_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014closes.proto\"j\n\006Closes\022\014\n\004jjdm\030\001 \001(\t\022\014" +
      "\n\004jzrq\030\002 \001(\t\022\014\n\004jjjz\030\003 \001(\t\022\014\n\004ljjz\030\004 \001(\t" +
      "\022\014\n\004zyjl\030\005 \001(\t\022\014\n\004zfxz\030\006 \001(\t\022\014\n\004dqrq\030\007 \001" +
      "(\tB3\n#com.howbuy.wireless.entity.protobu" +
      "fB\014ClosesProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_Closes_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_Closes_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Closes_descriptor,
              new java.lang.String[] { "Jjdm", "Jzrq", "Jjjz", "Ljjz", "Zyjl", "Zfxz", "Dqrq", },
              com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes.class,
              com.howbuy.wireless.entity.protobuf.ClosesProtos.Closes.Builder.class);
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
