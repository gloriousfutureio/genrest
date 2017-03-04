package io.gloriousfuture.genspec.openapi.v2

case class PathOperation(
  operationId: Option[String],
  consumes: Option[Seq[String]],
  produces: Option[Seq[String]],
  parameters: Seq[JsonRefOr[ApiParam]],
  description: Option[String] = None,
  summary: Option[String] = None,
  tags: Option[Seq[String]] = None,
  externalDocs: Option[ExternalDocs] = None
)
