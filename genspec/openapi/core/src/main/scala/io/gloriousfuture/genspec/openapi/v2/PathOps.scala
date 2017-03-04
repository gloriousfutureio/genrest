package io.gloriousfuture.genspec.openapi.v2

case class PathOps(
  get: Option[PathOperation],
  put: Option[PathOperation],
  post: Option[PathOperation],
  delete: Option[PathOperation],
  options: Option[PathOperation],
  head: Option[PathOperation],
  patch: Option[PathOperation],
  parameters: OptionalSeq[JsonRefOr[ApiParam]]
)
