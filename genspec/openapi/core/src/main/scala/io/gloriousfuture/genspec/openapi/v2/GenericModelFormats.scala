package io.gloriousfuture.genspec.openapi.v2

import scala.language.higherKinds

trait GenericModelFormats[D[_], E[_]] {

  implicit def decodeApiPath: D[ApiPath]
  implicit def encodeApiPath: E[ApiPath]

  implicit def decodeApiPaths: D[Map[ApiPath, JsonRefOr[PathOps]]]
  implicit def encodeApiPaths: E[Map[ApiPath, JsonRefOr[PathOps]]]

  implicit def decodeContact: D[Contact]
  implicit def encodeContact: E[Contact]

  implicit def decodeDefinitions: D[Map[String, JsonRefOr[ModelDefinition]]]
  implicit def encodeDefinitions: E[Map[String, JsonRefOr[ModelDefinition]]]

  implicit def decodeExternalDocs: D[ExternalDocs]
  implicit def encodeExternalDocs: E[ExternalDocs]

  implicit def decodeInfo: D[Info]
  implicit def encodeInfo: E[Info]

  implicit def decodeJsonRef: D[JsonRef]
  implicit def encodeJsonRef: E[JsonRef]

  implicit def decodeLicense: D[License]
  implicit def encodeLicense: E[License]

  implicit def decodeModelDefinition: D[ModelDefinition]
  implicit def encodeModelDefinition: E[ModelDefinition]

  implicit def decodeJsonRefOrModelDefinition: D[JsonRefOr[ModelDefinition]]
  implicit def encodeJsonRefOrModelDefinition: E[JsonRefOr[ModelDefinition]]

  implicit def decodeModelPrimitive: D[ModelPrimitive]
  implicit def encodeModelPrimitive: E[ModelPrimitive]

  implicit def decodeJsonRefOrModelPrimitive: D[JsonRefOr[ModelPrimitive]]
  implicit def encodeJsonRefOrModelPrimitive: E[JsonRefOr[ModelPrimitive]]

  implicit def decodeRequestParam: D[ApiParam]
  implicit def encodeRequestParam: E[ApiParam]

  implicit def decodeJsonRefOrRequestParam: D[JsonRefOr[ApiParam]]
  implicit def encodeJsonRefOrRequestParam: E[JsonRefOr[ApiParam]]

  implicit def decodePathOperation: D[PathOperation]
  implicit def encodePathOperation: E[PathOperation]

  implicit def decodePathOps: D[PathOps]
  implicit def encodePathOps: E[PathOps]

  implicit def decodeJsonRefOrPathOps: D[JsonRefOr[PathOps]]
  implicit def encodeJsonRefOrPathOps: E[JsonRefOr[PathOps]]

  implicit def decodeResponses: D[Responses]
  implicit def encodeResponses: E[Responses]

  implicit def decodeRoot: D[Root]
  implicit def encodeRoot: E[Root]

  implicit def decodeSecurityDefinitions: D[SecurityDefinitions]
  implicit def encodeSecurityDefinitions: E[SecurityDefinitions]

  implicit def decodeSecurityRequirement: D[SecurityRequirement]
  implicit def encodeSecurityRequirement: E[SecurityRequirement]

  implicit def decodeTag: D[Tag]
  implicit def encodeTag: E[Tag]
}
