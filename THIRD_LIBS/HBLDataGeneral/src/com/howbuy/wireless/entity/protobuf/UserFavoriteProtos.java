// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: userFavorite.proto

package com.howbuy.wireless.entity.protobuf;

public final class UserFavoriteProtos {
  private UserFavoriteProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class UserFavorite extends
      com.google.protobuf.GeneratedMessage {
    // Use UserFavorite.newBuilder() to construct.
    private UserFavorite() {
      initFields();
    }
    private UserFavorite(boolean noInit) {}
    
    private static final UserFavorite defaultInstance;
    public static UserFavorite getDefaultInstance() {
      return defaultInstance;
    }
    
    public UserFavorite getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.internal_static_UserFavorite_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.internal_static_UserFavorite_fieldAccessorTable;
    }
    
    // optional string favoriteObject = 1;
    public static final int FAVORITEOBJECT_FIELD_NUMBER = 1;
    private boolean hasFavoriteObject;
    private java.lang.String favoriteObject_ = "";
    public boolean hasFavoriteObject() { return hasFavoriteObject; }
    public java.lang.String getFavoriteObject() { return favoriteObject_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasFavoriteObject()) {
        output.writeString(1, getFavoriteObject());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasFavoriteObject()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getFavoriteObject());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite parseDelimitedFrom(
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
    public static com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite result;
      
      // Construct using com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite();
        return builder;
      }
      
      protected com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite.getDescriptor();
      }
      
      public com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite getDefaultInstanceForType() {
        return com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite) {
          return mergeFrom((com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite other) {
        if (other == com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite.getDefaultInstance()) return this;
        if (other.hasFavoriteObject()) {
          setFavoriteObject(other.getFavoriteObject());
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
              setFavoriteObject(input.readString());
              break;
            }
          }
        }
      }
      
      
      // optional string favoriteObject = 1;
      public boolean hasFavoriteObject() {
        return result.hasFavoriteObject();
      }
      public java.lang.String getFavoriteObject() {
        return result.getFavoriteObject();
      }
      public Builder setFavoriteObject(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasFavoriteObject = true;
        result.favoriteObject_ = value;
        return this;
      }
      public Builder clearFavoriteObject() {
        result.hasFavoriteObject = false;
        result.favoriteObject_ = getDefaultInstance().getFavoriteObject();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:UserFavorite)
    }
    
    static {
      defaultInstance = new UserFavorite(true);
      com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:UserFavorite)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_UserFavorite_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_UserFavorite_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022userFavorite.proto\"&\n\014UserFavorite\022\026\n\016" +
      "favoriteObject\030\001 \001(\tB9\n#com.howbuy.wirel" +
      "ess.entity.protobufB\022UserFavoriteProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_UserFavorite_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_UserFavorite_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_UserFavorite_descriptor,
              new java.lang.String[] { "FavoriteObject", },
              com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite.class,
              com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite.Builder.class);
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
