package io.gloriousfuture.genspec.openapi.v2

/**
  * A single definition, mapping a "name" to the schema it defines.
  */
case class ModelDefinition(
  `type`: String,
  properties: Map[String, JsonRefOr[ModelPrimitive]] = Map.empty
)
