package io.gloriousfuture.genspec.openapi.v2

import scala.language.higherKinds
import scala.reflect.ClassTag

trait GenericModelFormatsAdaptor[D[_], E[_]] extends GenericModelFormats[D, E] {

  /**
    * @note Do not make these implicit!
    *
    * Instead use this method explicitly to derive serializers of specific [[JsonRefOr]] models.
    */
  def decodeJsonRefOr[X: D: ClassTag]: D[JsonRefOr[X]]
  def encodeJsonRefOr[X: E]: E[JsonRefOr[X]]

  override implicit lazy val decodeJsonRefOrModelDefinition: D[JsonRefOr[ModelDefinition]] = decodeJsonRefOr[ModelDefinition]
  override implicit lazy val encodeJsonRefOrModelDefinition: E[JsonRefOr[ModelDefinition]] = encodeJsonRefOr[ModelDefinition]

  override implicit lazy val decodeJsonRefOrModelPrimitive: D[JsonRefOr[ModelPrimitive]] = decodeJsonRefOr[ModelPrimitive]
  override implicit lazy val encodeJsonRefOrModelPrimitive: E[JsonRefOr[ModelPrimitive]] = encodeJsonRefOr[ModelPrimitive]

  override implicit lazy val decodeJsonRefOrRequestParam: D[JsonRefOr[ApiParam]] = decodeJsonRefOr[ApiParam]
  override implicit lazy val encodeJsonRefOrRequestParam: E[JsonRefOr[ApiParam]] = encodeJsonRefOr[ApiParam]

  override implicit lazy val decodeJsonRefOrPathOps: D[JsonRefOr[PathOps]] = decodeJsonRefOr[PathOps]
  override implicit lazy val encodeJsonRefOrPathOps: E[JsonRefOr[PathOps]] = encodeJsonRefOr[PathOps]
}
