package io.gloriousfuture.genspec.openapi.v2

/**
  *
  * @param `type` the type
  * @param default Used to supply a default JSON value (serialized as a string) associated with this particular schema.
  *                There are no restrictions placed on the value of this keyword.
  *                It is RECOMMENDED that a default value be valid against the associated schema.
  * @param format
  * @param title A short name of this schema
  * @param description A longer description of this schema
  */
case class Schema(
  `type`: String,
  default: Option[String] = None,
  format: Option[String] = None, // TODO: Add format validation
  title: Option[String] = None,
  description: Option[String] = None
)

object Schema {

  object DataTypes {

    final val OBJECT = "object"
  }
}
