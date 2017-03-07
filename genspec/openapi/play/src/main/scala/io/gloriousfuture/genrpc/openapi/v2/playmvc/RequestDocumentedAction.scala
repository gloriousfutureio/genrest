package io.gloriousfuture.genrpc.openapi.v2.playmvc

import play.api.mvc.{AnyContent, EssentialAction}

import scala.reflect.ClassTag

final class RequestDocumentedAction[In](val underlying: EssentialAction) extends AnyVal {

  def withoutResponseSchema(implicit outTag: ClassTag[In]): DocumentedEssentialAction[In, AnyContent] = {
    DocumentedEssentialAction.full(underlying)
  }

  def withResponseSchema[Out](implicit inTag: ClassTag[In], outTag: ClassTag[Out]): DocumentedEssentialAction[In, Out] = {
    DocumentedEssentialAction.full(underlying)
  }
}
