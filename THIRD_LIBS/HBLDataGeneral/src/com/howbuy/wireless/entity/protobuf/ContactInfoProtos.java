// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ContactInfo.proto

package com.howbuy.wireless.entity.protobuf;

public final class ContactInfoProtos {
  private ContactInfoProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class ContactInfo extends
      com.google.protobuf.GeneratedMessage {
    // Use ContactInfo.newBuilder() to construct.
    private ContactInfo() {
      initFields();
    }
    private ContactInfo(boolean noInit) {}
    
    private static final ContactInfo defaultInstance;
    public static ContactInfo getDefaultInstance() {
      return defaultInstance;
    }
    
    public ContactInfo getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.ContactInfoProtos.internal_static_ContactInfo_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.ContactInfoProtos.internal_static_ContactInfo_fieldAccessorTable;
    }
    
    // optional string name = 1;
    public static final int NAME_FIELD_NUMBER = 1;
    private boolean hasName;
    private java.lang.String name_ = "";
    public boolean hasName() { return hasName; }
    public java.lang.String getName() { return name_; }
    
    // optional string company = 2;
    public static final int COMPANY_FIELD_NUMBER = 2;
    private boolean hasCompany;
    private java.lang.String company_ = "";
    public boolean hasCompany() { return hasCompany; }
    public java.lang.String getCompany() { return company_; }
    
    // optional string title = 3;
    public static final int TITLE_FIELD_NUMBER = 3;
    private boolean hasTitle;
    private java.lang.String title_ = "";
    public boolean hasTitle() { return hasTitle; }
    public java.lang.String getTitle() { return title_; }
    
    // optional string email = 4;
    public static final int EMAIL_FIELD_NUMBER = 4;
    private boolean hasEmail;
    private java.lang.String email_ = "";
    public boolean hasEmail() { return hasEmail; }
    public java.lang.String getEmail() { return email_; }
    
    // optional string phoneNumber = 5;
    public static final int PHONENUMBER_FIELD_NUMBER = 5;
    private boolean hasPhoneNumber;
    private java.lang.String phoneNumber_ = "";
    public boolean hasPhoneNumber() { return hasPhoneNumber; }
    public java.lang.String getPhoneNumber() { return phoneNumber_; }
    
    // optional string note = 6;
    public static final int NOTE_FIELD_NUMBER = 6;
    private boolean hasNote;
    private java.lang.String note_ = "";
    public boolean hasNote() { return hasNote; }
    public java.lang.String getNote() { return note_; }
    
    // optional string id = 7;
    public static final int ID_FIELD_NUMBER = 7;
    private boolean hasId;
    private java.lang.String id_ = "";
    public boolean hasId() { return hasId; }
    public java.lang.String getId() { return id_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasName()) {
        output.writeString(1, getName());
      }
      if (hasCompany()) {
        output.writeString(2, getCompany());
      }
      if (hasTitle()) {
        output.writeString(3, getTitle());
      }
      if (hasEmail()) {
        output.writeString(4, getEmail());
      }
      if (hasPhoneNumber()) {
        output.writeString(5, getPhoneNumber());
      }
      if (hasNote()) {
        output.writeString(6, getNote());
      }
      if (hasId()) {
        output.writeString(7, getId());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasName()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getName());
      }
      if (hasCompany()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getCompany());
      }
      if (hasTitle()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getTitle());
      }
      if (hasEmail()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(4, getEmail());
      }
      if (hasPhoneNumber()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(5, getPhoneNumber());
      }
      if (hasNote()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(6, getNote());
      }
      if (hasId()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(7, getId());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo other) {
        if (other == com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo.getDefaultInstance()) return this;
        if (other.hasName()) {
          setName(other.getName());
        }
        if (other.hasCompany()) {
          setCompany(other.getCompany());
        }
        if (other.hasTitle()) {
          setTitle(other.getTitle());
        }
        if (other.hasEmail()) {
          setEmail(other.getEmail());
        }
        if (other.hasPhoneNumber()) {
          setPhoneNumber(other.getPhoneNumber());
        }
        if (other.hasNote()) {
          setNote(other.getNote());
        }
        if (other.hasId()) {
          setId(other.getId());
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
              setName(input.readString());
              break;
            }
            case 18: {
              setCompany(input.readString());
              break;
            }
            case 26: {
              setTitle(input.readString());
              break;
            }
            case 34: {
              setEmail(input.readString());
              break;
            }
            case 42: {
              setPhoneNumber(input.readString());
              break;
            }
            case 50: {
              setNote(input.readString());
              break;
            }
            case 58: {
              setId(input.readString());
              break;
            }
          }
        }
      }
      
      
      // optional string name = 1;
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
      
      // optional string company = 2;
      public boolean hasCompany() {
        return result.hasCompany();
      }
      public java.lang.String getCompany() {
        return result.getCompany();
      }
      public Builder setCompany(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasCompany = true;
        result.company_ = value;
        return this;
      }
      public Builder clearCompany() {
        result.hasCompany = false;
        result.company_ = getDefaultInstance().getCompany();
        return this;
      }
      
      // optional string title = 3;
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
      
      // optional string email = 4;
      public boolean hasEmail() {
        return result.hasEmail();
      }
      public java.lang.String getEmail() {
        return result.getEmail();
      }
      public Builder setEmail(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasEmail = true;
        result.email_ = value;
        return this;
      }
      public Builder clearEmail() {
        result.hasEmail = false;
        result.email_ = getDefaultInstance().getEmail();
        return this;
      }
      
      // optional string phoneNumber = 5;
      public boolean hasPhoneNumber() {
        return result.hasPhoneNumber();
      }
      public java.lang.String getPhoneNumber() {
        return result.getPhoneNumber();
      }
      public Builder setPhoneNumber(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasPhoneNumber = true;
        result.phoneNumber_ = value;
        return this;
      }
      public Builder clearPhoneNumber() {
        result.hasPhoneNumber = false;
        result.phoneNumber_ = getDefaultInstance().getPhoneNumber();
        return this;
      }
      
      // optional string note = 6;
      public boolean hasNote() {
        return result.hasNote();
      }
      public java.lang.String getNote() {
        return result.getNote();
      }
      public Builder setNote(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasNote = true;
        result.note_ = value;
        return this;
      }
      public Builder clearNote() {
        result.hasNote = false;
        result.note_ = getDefaultInstance().getNote();
        return this;
      }
      
      // optional string id = 7;
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
      
      // @@protoc_insertion_point(builder_scope:ContactInfo)
    }
    
    static {
      defaultInstance = new ContactInfo(true);
      com.howbuy.wireless.entity.protobuf.ContactInfoProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:ContactInfo)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ContactInfo_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ContactInfo_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021ContactInfo.proto\"y\n\013ContactInfo\022\014\n\004na" +
      "me\030\001 \001(\t\022\017\n\007company\030\002 \001(\t\022\r\n\005title\030\003 \001(\t" +
      "\022\r\n\005email\030\004 \001(\t\022\023\n\013phoneNumber\030\005 \001(\t\022\014\n\004" +
      "note\030\006 \001(\t\022\n\n\002id\030\007 \001(\tB8\n#com.howbuy.wir" +
      "eless.entity.protobufB\021ContactInfoProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_ContactInfo_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_ContactInfo_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ContactInfo_descriptor,
              new java.lang.String[] { "Name", "Company", "Title", "Email", "PhoneNumber", "Note", "Id", },
              com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo.class,
              com.howbuy.wireless.entity.protobuf.ContactInfoProtos.ContactInfo.Builder.class);
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