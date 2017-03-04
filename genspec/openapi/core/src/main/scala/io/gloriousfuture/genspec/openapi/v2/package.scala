package io.gloriousfuture.genspec.openapi

import scala.language.implicitConversions

package object v2 {
  type JsonRefOr[Value] = Either[JsonRef, Value]

  type OptionalSeq[T] = Option[Seq[T]]
  object OptionalSeq {
    implicit def orEmptySeq[T](optional: OptionalSeq[T]): Seq[T] = optional getOrElse Nil
  }

  implicit class AsModelProperty(val either: JsonRefOr[ModelPrimitive]) extends AnyVal {
    def asPrimitive: Option[ModelPrimitive] = either.right.toOption
    def asRef: Option[JsonRef] = either.left.toOption
  }
}
