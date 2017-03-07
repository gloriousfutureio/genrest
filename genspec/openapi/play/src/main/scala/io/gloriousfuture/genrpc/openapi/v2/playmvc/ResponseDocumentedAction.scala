package io.gloriousfuture.genrpc.openapi.v2.playmvc

import play.api.mvc.{AnyContent, EssentialAction}

import scala.reflect.ClassTag

final class ResponseDocumentedAction[Out](val action: EssentialAction) extends AnyVal {

  def withoutRequestSchema(implicit outTag: ClassTag[Out]): DocumentedEssentialAction[AnyContent, Out] = {
    DocumentedEssentialAction.full(action)
  }

  def withRequestSchema[In](implicit inTag: ClassTag[In], outTag: ClassTag[Out]): DocumentedEssentialAction[In, Out] = {
    DocumentedEssentialAction.full(action)
  }
}
