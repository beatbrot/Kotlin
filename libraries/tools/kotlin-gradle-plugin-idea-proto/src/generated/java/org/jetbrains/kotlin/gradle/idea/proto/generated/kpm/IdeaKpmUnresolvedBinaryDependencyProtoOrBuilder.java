// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto_kpm.proto

package org.jetbrains.kotlin.gradle.idea.proto.generated.kpm;

public interface IdeaKpmUnresolvedBinaryDependencyProtoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.jetbrains.kotlin.gradle.idea.proto.generated.kpm.IdeaKpmUnresolvedBinaryDependencyProto)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional .org.jetbrains.kotlin.gradle.idea.proto.generated.IdeaExtrasProto extras = 1;</code>
   * @return Whether the extras field is set.
   */
  boolean hasExtras();
  /**
   * <code>optional .org.jetbrains.kotlin.gradle.idea.proto.generated.IdeaExtrasProto extras = 1;</code>
   * @return The extras.
   */
  org.jetbrains.kotlin.gradle.idea.proto.generated.IdeaExtrasProto getExtras();
  /**
   * <code>optional .org.jetbrains.kotlin.gradle.idea.proto.generated.IdeaExtrasProto extras = 1;</code>
   */
  org.jetbrains.kotlin.gradle.idea.proto.generated.IdeaExtrasProtoOrBuilder getExtrasOrBuilder();

  /**
   * <code>optional .org.jetbrains.kotlin.gradle.idea.proto.generated.kpm.IdeaKpmBinaryCoordinatesProto coordinates = 2;</code>
   * @return Whether the coordinates field is set.
   */
  boolean hasCoordinates();
  /**
   * <code>optional .org.jetbrains.kotlin.gradle.idea.proto.generated.kpm.IdeaKpmBinaryCoordinatesProto coordinates = 2;</code>
   * @return The coordinates.
   */
  org.jetbrains.kotlin.gradle.idea.proto.generated.kpm.IdeaKpmBinaryCoordinatesProto getCoordinates();
  /**
   * <code>optional .org.jetbrains.kotlin.gradle.idea.proto.generated.kpm.IdeaKpmBinaryCoordinatesProto coordinates = 2;</code>
   */
  org.jetbrains.kotlin.gradle.idea.proto.generated.kpm.IdeaKpmBinaryCoordinatesProtoOrBuilder getCoordinatesOrBuilder();

  /**
   * <code>optional string cause = 3;</code>
   * @return Whether the cause field is set.
   */
  boolean hasCause();
  /**
   * <code>optional string cause = 3;</code>
   * @return The cause.
   */
  java.lang.String getCause();
  /**
   * <code>optional string cause = 3;</code>
   * @return The bytes for cause.
   */
  com.google.protobuf.ByteString
      getCauseBytes();
}
