// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: mtMsg.proto

package com.time.memory.mt.nio.proto;

public interface ResponseMsgOrBuilder extends
    // @@protoc_insertion_point(interface_extends:ResponseMsg)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional .ResponseMsg.Status status = 1;</code>
   */
  int getStatusValue();
  /**
   * <code>optional .ResponseMsg.Status status = 1;</code>
   */
  ResponseMsg.Status getStatus();

  /**
   * <code>optional string msg = 2;</code>
   */
  String getMsg();
  /**
   * <code>optional string msg = 2;</code>
   */
  com.google.protobuf.ByteString
      getMsgBytes();

  /**
   * <code>optional .ResponseMsg.Code code = 3;</code>
   */
  int getCodeValue();
  /**
   * <code>optional .ResponseMsg.Code code = 3;</code>
   */
  ResponseMsg.Code getCode();

  /**
   * <pre>
   *业务1的返回结构体
   * </pre>
   *
   * <code>optional .Busy_1 busy_1 = 4;</code>
   */
  boolean hasBusy1();
  /**
   * <pre>
   *业务1的返回结构体
   * </pre>
   *
   * <code>optional .Busy_1 busy_1 = 4;</code>
   */
  Busy_1 getBusy1();
  /**
   * <pre>
   *业务1的返回结构体
   * </pre>
   *
   * <code>optional .Busy_1 busy_1 = 4;</code>
   */
  Busy_1OrBuilder getBusy1OrBuilder();
}
