// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: moneys.proto

package com.howbuy.wireless.entity.protobuf;

public final class MoneysProtos {
  private MoneysProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class Moneys extends
      com.google.protobuf.GeneratedMessage {
    // Use Moneys.newBuilder() to construct.
    private Moneys() {
      initFields();
    }
    private Moneys(boolean noInit) {}
    
    private static final Moneys defaultInstance;
    public static Moneys getDefaultInstance() {
      return defaultInstance;
    }
    
    public Moneys getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.MoneysProtos.internal_static_Moneys_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.MoneysProtos.internal_static_Moneys_fieldAccessorTable;
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
    
    // optional string wfsy = 3;
    public static final int WFSY_FIELD_NUMBER = 3;
    private boolean hasWfsy;
    private java.lang.String wfsy_ = "";
    public boolean hasWfsy() { return hasWfsy; }
    public java.lang.String getWfsy() { return wfsy_; }
    
    // optional string qrsy = 4;
    public static final int QRSY_FIELD_NUMBER = 4;
    private boolean hasQrsy;
    private java.lang.String qrsy_ = "";
    public boolean hasQrsy() { return hasQrsy; }
    public java.lang.String getQrsy() { return qrsy_; }
    
    // optional string zfxz = 5;
    public static final int ZFXZ_FIELD_NUMBER = 5;
    private boolean hasZfxz;
    private java.lang.String zfxz_ = "";
    public boolean hasZfxz() { return hasZfxz; }
    public java.lang.String getZfxz() { return zfxz_; }
    
    // optional string hb1y = 6;
    public static final int HB1Y_FIELD_NUMBER = 6;
    private boolean hasHb1Y;
    private java.lang.String hb1Y_ = "";
    public boolean hasHb1Y() { return hasHb1Y; }
    public java.lang.String getHb1Y() { return hb1Y_; }
    
    // optional string hbjn = 7;
    public static final int HBJN_FIELD_NUMBER = 7;
    private boolean hasHbjn;
    private java.lang.String hbjn_ = "";
    public boolean hasHbjn() { return hasHbjn; }
    public java.lang.String getHbjn() { return hbjn_; }
    
    // optional string hb3y = 8;
    public static final int HB3Y_FIELD_NUMBER = 8;
    private boolean hasHb3Y;
    private java.lang.String hb3Y_ = "";
    public boolean hasHb3Y() { return hasHb3Y; }
    public java.lang.String getHb3Y() { return hb3Y_; }
    
    // optional string hb6y = 9;
    public static final int HB6Y_FIELD_NUMBER = 9;
    private boolean hasHb6Y;
    private java.lang.String hb6Y_ = "";
    public boolean hasHb6Y() { return hasHb6Y; }
    public java.lang.String getHb6Y() { return hb6Y_; }
    
    // optional string hb1n = 10;
    public static final int HB1N_FIELD_NUMBER = 10;
    private boolean hasHb1N;
    private java.lang.String hb1N_ = "";
    public boolean hasHb1N() { return hasHb1N; }
    public java.lang.String getHb1N() { return hb1N_; }
    
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
      if (hasWfsy()) {
        output.writeString(3, getWfsy());
      }
      if (hasQrsy()) {
        output.writeString(4, getQrsy());
      }
      if (hasZfxz()) {
        output.writeString(5, getZfxz());
      }
      if (hasHb1Y()) {
        output.writeString(6, getHb1Y());
      }
      if (hasHbjn()) {
        output.writeString(7, getHbjn());
      }
      if (hasHb3Y()) {
        output.writeString(8, getHb3Y());
      }
      if (hasHb6Y()) {
        output.writeString(9, getHb6Y());
      }
      if (hasHb1N()) {
        output.writeString(10, getHb1N());
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
      if (hasWfsy()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getWfsy());
      }
      if (hasQrsy()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(4, getQrsy());
      }
      if (hasZfxz()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(5, getZfxz());
      }
      if (hasHb1Y()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(6, getHb1Y());
      }
      if (hasHbjn()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(7, getHbjn());
      }
      if (hasHb3Y()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(8, getHb3Y());
      }
      if (hasHb6Y()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(9, getHb6Y());
      }
      if (hasHb1N()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(10, getHb1N());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys other) {
        if (other == com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys.getDefaultInstance()) return this;
        if (other.hasJjdm()) {
          setJjdm(other.getJjdm());
        }
        if (other.hasJzrq()) {
          setJzrq(other.getJzrq());
        }
        if (other.hasWfsy()) {
          setWfsy(other.getWfsy());
        }
        if (other.hasQrsy()) {
          setQrsy(other.getQrsy());
        }
        if (other.hasZfxz()) {
          setZfxz(other.getZfxz());
        }
        if (other.hasHb1Y()) {
          setHb1Y(other.getHb1Y());
        }
        if (other.hasHbjn()) {
          setHbjn(other.getHbjn());
        }
        if (other.hasHb3Y()) {
          setHb3Y(other.getHb3Y());
        }
        if (other.hasHb6Y()) {
          setHb6Y(other.getHb6Y());
        }
        if (other.hasHb1N()) {
          setHb1N(other.getHb1N());
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
              setWfsy(input.readString());
              break;
            }
            case 34: {
              setQrsy(input.readString());
              break;
            }
            case 42: {
              setZfxz(input.readString());
              break;
            }
            case 50: {
              setHb1Y(input.readString());
              break;
            }
            case 58: {
              setHbjn(input.readString());
              break;
            }
            case 66: {
              setHb3Y(input.readString());
              break;
            }
            case 74: {
              setHb6Y(input.readString());
              break;
            }
            case 82: {
              setHb1N(input.readString());
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
      
      // optional string wfsy = 3;
      public boolean hasWfsy() {
        return result.hasWfsy();
      }
      public java.lang.String getWfsy() {
        return result.getWfsy();
      }
      public Builder setWfsy(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasWfsy = true;
        result.wfsy_ = value;
        return this;
      }
      public Builder clearWfsy() {
        result.hasWfsy = false;
        result.wfsy_ = getDefaultInstance().getWfsy();
        return this;
      }
      
      // optional string qrsy = 4;
      public boolean hasQrsy() {
        return result.hasQrsy();
      }
      public java.lang.String getQrsy() {
        return result.getQrsy();
      }
      public Builder setQrsy(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasQrsy = true;
        result.qrsy_ = value;
        return this;
      }
      public Builder clearQrsy() {
        result.hasQrsy = false;
        result.qrsy_ = getDefaultInstance().getQrsy();
        return this;
      }
      
      // optional string zfxz = 5;
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
      
      // optional string hb1y = 6;
      public boolean hasHb1Y() {
        return result.hasHb1Y();
      }
      public java.lang.String getHb1Y() {
        return result.getHb1Y();
      }
      public Builder setHb1Y(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasHb1Y = true;
        result.hb1Y_ = value;
        return this;
      }
      public Builder clearHb1Y() {
        result.hasHb1Y = false;
        result.hb1Y_ = getDefaultInstance().getHb1Y();
        return this;
      }
      
      // optional string hbjn = 7;
      public boolean hasHbjn() {
        return result.hasHbjn();
      }
      public java.lang.String getHbjn() {
        return result.getHbjn();
      }
      public Builder setHbjn(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasHbjn = true;
        result.hbjn_ = value;
        return this;
      }
      public Builder clearHbjn() {
        result.hasHbjn = false;
        result.hbjn_ = getDefaultInstance().getHbjn();
        return this;
      }
      
      // optional string hb3y = 8;
      public boolean hasHb3Y() {
        return result.hasHb3Y();
      }
      public java.lang.String getHb3Y() {
        return result.getHb3Y();
      }
      public Builder setHb3Y(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasHb3Y = true;
        result.hb3Y_ = value;
        return this;
      }
      public Builder clearHb3Y() {
        result.hasHb3Y = false;
        result.hb3Y_ = getDefaultInstance().getHb3Y();
        return this;
      }
      
      // optional string hb6y = 9;
      public boolean hasHb6Y() {
        return result.hasHb6Y();
      }
      public java.lang.String getHb6Y() {
        return result.getHb6Y();
      }
      public Builder setHb6Y(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasHb6Y = true;
        result.hb6Y_ = value;
        return this;
      }
      public Builder clearHb6Y() {
        result.hasHb6Y = false;
        result.hb6Y_ = getDefaultInstance().getHb6Y();
        return this;
      }
      
      // optional string hb1n = 10;
      public boolean hasHb1N() {
        return result.hasHb1N();
      }
      public java.lang.String getHb1N() {
        return result.getHb1N();
      }
      public Builder setHb1N(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasHb1N = true;
        result.hb1N_ = value;
        return this;
      }
      public Builder clearHb1N() {
        result.hasHb1N = false;
        result.hb1N_ = getDefaultInstance().getHb1N();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:Moneys)
    }
    
    static {
      defaultInstance = new Moneys(true);
      com.howbuy.wireless.entity.protobuf.MoneysProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:Moneys)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_Moneys_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Moneys_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014moneys.proto\"\224\001\n\006Moneys\022\014\n\004jjdm\030\001 \001(\t\022" +
      "\014\n\004jzrq\030\002 \001(\t\022\014\n\004wfsy\030\003 \001(\t\022\014\n\004qrsy\030\004 \001(" +
      "\t\022\014\n\004zfxz\030\005 \001(\t\022\014\n\004hb1y\030\006 \001(\t\022\014\n\004hbjn\030\007 " +
      "\001(\t\022\014\n\004hb3y\030\010 \001(\t\022\014\n\004hb6y\030\t \001(\t\022\014\n\004hb1n\030" +
      "\n \001(\tB3\n#com.howbuy.wireless.entity.prot" +
      "obufB\014MoneysProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_Moneys_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_Moneys_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Moneys_descriptor,
              new java.lang.String[] { "Jjdm", "Jzrq", "Wfsy", "Qrsy", "Zfxz", "Hb1Y", "Hbjn", "Hb3Y", "Hb6Y", "Hb1N", },
              com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys.class,
              com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys.Builder.class);
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
