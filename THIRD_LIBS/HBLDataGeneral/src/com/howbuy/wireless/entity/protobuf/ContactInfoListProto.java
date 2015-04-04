// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ContactInfoList.proto

package com.howbuy.wireless.entity.protobuf;

public final class ContactInfoListProto {
  private ContactInfoListProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class ContactInfoList extends
      com.google.protobuf.GeneratedMessage {
    // Use ContactInfoList.newBuilder() to construct.
    private ContactInfoList() {
      initFields();
    }
    private ContactInfoList(boolean noInit) {}
    
    private static final ContactInfoList defaultInstance;
    public static ContactInfoList getDefaultInstance() {
      return defaultInstance;
    }
    
    public ContactInfoList getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.ContactInfoListProto.internal_static_ContactInfoList_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.ContactInfoListProto.internal_static_ContactInfoList_fieldAccessorTable;
    }
    
    // optional string version = 1;
    public static final int VERSION_FIELD_NUMBER = 1;
    private boolean hasVersion;
    private java.lang.String version_ = "";
    public boolean hasVersion() { return hasVersion; }
    public java.lang.String getVersion() { return version_; }
    
    // optional string channelId = 2;
    public static final int CHANNELID_FIELD_NUMBER = 2;
    private boolean hasChannelId;
    private java.lang.String channelId_ = "";
    public boolean hasChannelId() { return hasChannelId; }
    public java.lang.String getChannelId() { return channelId_; }
    
    // optional string productId = 3;
    public static final int PRODUCTID_FIELD_NUMBER = 3;
    private boolean hasProductId;
    private java.lang.String productId_ = "";
    public boolean hasProductId() { return hasProductId; }
    public java.lang.String getProductId() { return productId_; }
    
    // optional string parPhoneModel = 4;
    public static final int PARPHONEMODEL_FIELD_NUMBER = 4;
    private boolean hasParPhoneModel;
    private java.lang.String parPhoneModel_ = "";
    public boolean hasParPhoneModel() { return hasParPhoneModel; }
    public java.lang.String getParPhoneModel() { return parPhoneModel_; }
    
    // optional string subPhoneModel = 5;
    public static final int SUBPHONEMODEL_FIELD_NUMBER = 5;
    private boolean hasSubPhoneModel;
    private java.lang.String subPhoneModel_ = "";
    public boolean hasSubPhoneModel() { return hasSubPhoneModel; }
    public java.lang.String getSubPhoneModel() { return subPhoneModel_; }
    
    // optional string token = 6;
    public static final int TOKEN_FIELD_NUMBER = 6;
    private boolean hasToken;
    private java.lang.String token_ = "";
    public boolean hasToken() { return hasToken; }
    public java.lang.String getToken() { return token_; }
    
    // optional string iVer = 7;
    public static final int IVER_FIELD_NUMBER = 7;
    private boolean hasIVer;
    private java.lang.String iVer_ = "";
    public boolean hasIVer() { return hasIVer; }
    public java.lang.String getIVer() { return iVer_; }
    
    // repeated .ContactInfo contactInfo = 8;
    public static final int CONTACTINFO_FIELD_NUMBER = 8;
    private java.util.List<com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo> contactInfo_ =
      java.util.Collections.emptyList();
    public java.util.List<com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo> getContactInfoList() {
      return contactInfo_;
    }
    public int getContactInfoCount() { return contactInfo_.size(); }
    public com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo getContactInfo(int index) {
      return contactInfo_.get(index);
    }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasVersion()) {
        output.writeString(1, getVersion());
      }
      if (hasChannelId()) {
        output.writeString(2, getChannelId());
      }
      if (hasProductId()) {
        output.writeString(3, getProductId());
      }
      if (hasParPhoneModel()) {
        output.writeString(4, getParPhoneModel());
      }
      if (hasSubPhoneModel()) {
        output.writeString(5, getSubPhoneModel());
      }
      if (hasToken()) {
        output.writeString(6, getToken());
      }
      if (hasIVer()) {
        output.writeString(7, getIVer());
      }
      for (com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo element : getContactInfoList()) {
        output.writeMessage(8, element);
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasVersion()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getVersion());
      }
      if (hasChannelId()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getChannelId());
      }
      if (hasProductId()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getProductId());
      }
      if (hasParPhoneModel()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(4, getParPhoneModel());
      }
      if (hasSubPhoneModel()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(5, getSubPhoneModel());
      }
      if (hasToken()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(6, getToken());
      }
      if (hasIVer()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(7, getIVer());
      }
      for (com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo element : getContactInfoList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(8, element);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.contactInfo_ != java.util.Collections.EMPTY_LIST) {
          result.contactInfo_ =
            java.util.Collections.unmodifiableList(result.contactInfo_);
        }
        com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList other) {
        if (other == com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList.getDefaultInstance()) return this;
        if (other.hasVersion()) {
          setVersion(other.getVersion());
        }
        if (other.hasChannelId()) {
          setChannelId(other.getChannelId());
        }
        if (other.hasProductId()) {
          setProductId(other.getProductId());
        }
        if (other.hasParPhoneModel()) {
          setParPhoneModel(other.getParPhoneModel());
        }
        if (other.hasSubPhoneModel()) {
          setSubPhoneModel(other.getSubPhoneModel());
        }
        if (other.hasToken()) {
          setToken(other.getToken());
        }
        if (other.hasIVer()) {
          setIVer(other.getIVer());
        }
        if (!other.contactInfo_.isEmpty()) {
          if (result.contactInfo_.isEmpty()) {
            result.contactInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo>();
          }
          result.contactInfo_.addAll(other.contactInfo_);
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
              setVersion(input.readString());
              break;
            }
            case 18: {
              setChannelId(input.readString());
              break;
            }
            case 26: {
              setProductId(input.readString());
              break;
            }
            case 34: {
              setParPhoneModel(input.readString());
              break;
            }
            case 42: {
              setSubPhoneModel(input.readString());
              break;
            }
            case 50: {
              setToken(input.readString());
              break;
            }
            case 58: {
              setIVer(input.readString());
              break;
            }
            case 66: {
              com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo.Builder subBuilder = com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addContactInfo(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      
      // optional string version = 1;
      public boolean hasVersion() {
        return result.hasVersion();
      }
      public java.lang.String getVersion() {
        return result.getVersion();
      }
      public Builder setVersion(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasVersion = true;
        result.version_ = value;
        return this;
      }
      public Builder clearVersion() {
        result.hasVersion = false;
        result.version_ = getDefaultInstance().getVersion();
        return this;
      }
      
      // optional string channelId = 2;
      public boolean hasChannelId() {
        return result.hasChannelId();
      }
      public java.lang.String getChannelId() {
        return result.getChannelId();
      }
      public Builder setChannelId(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasChannelId = true;
        result.channelId_ = value;
        return this;
      }
      public Builder clearChannelId() {
        result.hasChannelId = false;
        result.channelId_ = getDefaultInstance().getChannelId();
        return this;
      }
      
      // optional string productId = 3;
      public boolean hasProductId() {
        return result.hasProductId();
      }
      public java.lang.String getProductId() {
        return result.getProductId();
      }
      public Builder setProductId(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasProductId = true;
        result.productId_ = value;
        return this;
      }
      public Builder clearProductId() {
        result.hasProductId = false;
        result.productId_ = getDefaultInstance().getProductId();
        return this;
      }
      
      // optional string parPhoneModel = 4;
      public boolean hasParPhoneModel() {
        return result.hasParPhoneModel();
      }
      public java.lang.String getParPhoneModel() {
        return result.getParPhoneModel();
      }
      public Builder setParPhoneModel(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasParPhoneModel = true;
        result.parPhoneModel_ = value;
        return this;
      }
      public Builder clearParPhoneModel() {
        result.hasParPhoneModel = false;
        result.parPhoneModel_ = getDefaultInstance().getParPhoneModel();
        return this;
      }
      
      // optional string subPhoneModel = 5;
      public boolean hasSubPhoneModel() {
        return result.hasSubPhoneModel();
      }
      public java.lang.String getSubPhoneModel() {
        return result.getSubPhoneModel();
      }
      public Builder setSubPhoneModel(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasSubPhoneModel = true;
        result.subPhoneModel_ = value;
        return this;
      }
      public Builder clearSubPhoneModel() {
        result.hasSubPhoneModel = false;
        result.subPhoneModel_ = getDefaultInstance().getSubPhoneModel();
        return this;
      }
      
      // optional string token = 6;
      public boolean hasToken() {
        return result.hasToken();
      }
      public java.lang.String getToken() {
        return result.getToken();
      }
      public Builder setToken(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasToken = true;
        result.token_ = value;
        return this;
      }
      public Builder clearToken() {
        result.hasToken = false;
        result.token_ = getDefaultInstance().getToken();
        return this;
      }
      
      // optional string iVer = 7;
      public boolean hasIVer() {
        return result.hasIVer();
      }
      public java.lang.String getIVer() {
        return result.getIVer();
      }
      public Builder setIVer(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasIVer = true;
        result.iVer_ = value;
        return this;
      }
      public Builder clearIVer() {
        result.hasIVer = false;
        result.iVer_ = getDefaultInstance().getIVer();
        return this;
      }
      
      // repeated .ContactInfo contactInfo = 8;
      public java.util.List<com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo> getContactInfoList() {
        return java.util.Collections.unmodifiableList(result.contactInfo_);
      }
      public int getContactInfoCount() {
        return result.getContactInfoCount();
      }
      public com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo getContactInfo(int index) {
        return result.getContactInfo(index);
      }
      public Builder setContactInfo(int index, com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.contactInfo_.set(index, value);
        return this;
      }
      public Builder setContactInfo(int index, com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo.Builder builderForValue) {
        result.contactInfo_.set(index, builderForValue.build());
        return this;
      }
      public Builder addContactInfo(com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.contactInfo_.isEmpty()) {
          result.contactInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo>();
        }
        result.contactInfo_.add(value);
        return this;
      }
      public Builder addContactInfo(com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo.Builder builderForValue) {
        if (result.contactInfo_.isEmpty()) {
          result.contactInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo>();
        }
        result.contactInfo_.add(builderForValue.build());
        return this;
      }
      public Builder addAllContactInfo(
          java.lang.Iterable<? extends com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo> values) {
        if (result.contactInfo_.isEmpty()) {
          result.contactInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo>();
        }
        super.addAll(values, result.contactInfo_);
        return this;
      }
      public Builder clearContactInfo() {
        result.contactInfo_ = java.util.Collections.emptyList();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:ContactInfoList)
    }
    
    static {
      defaultInstance = new ContactInfoList(true);
      com.howbuy.wireless.entity.protobuf.ContactInfoListProto.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:ContactInfoList)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ContactInfoList_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ContactInfoList_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\025ContactInfoList.proto\032\021ContactInfo.pro" +
      "to\032\014common.proto\"\266\001\n\017ContactInfoList\022\017\n\007" +
      "version\030\001 \001(\t\022\021\n\tchannelId\030\002 \001(\t\022\021\n\tprod" +
      "uctId\030\003 \001(\t\022\025\n\rparPhoneModel\030\004 \001(\t\022\025\n\rsu" +
      "bPhoneModel\030\005 \001(\t\022\r\n\005token\030\006 \001(\t\022\014\n\004iVer" +
      "\030\007 \001(\t\022!\n\013contactInfo\030\010 \003(\0132\014.ContactInf" +
      "oB;\n#com.howbuy.wireless.entity.protobuf" +
      "B\024ContactInfoListProto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_ContactInfoList_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_ContactInfoList_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ContactInfoList_descriptor,
              new java.lang.String[] { "Version", "ChannelId", "ProductId", "ParPhoneModel", "SubPhoneModel", "Token", "IVer", "ContactInfo", },
              com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList.class,
              com.howbuy.wireless.entity.protobuf.ContactInfoListProto.ContactInfoList.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.howbuy.wireless.entity.protobuf.ContactInfoProtos.getDescriptor(),
          com.howbuy.wireless.entity.protobuf.CommonProtos.getDescriptor(),
        }, assigner);
  }
  
  public static void internalForceInit() {}
  
  // @@protoc_insertion_point(outer_class_scope)
}