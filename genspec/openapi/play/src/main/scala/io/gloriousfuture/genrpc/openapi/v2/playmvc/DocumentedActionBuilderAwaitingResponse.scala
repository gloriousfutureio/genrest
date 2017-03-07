package io.gloriousfuture.genrpc.openapi.v2.playmvc

import play.api.mvc.ActionBuilder
import play.mvc.BodyParser.AnyContent

import scala.language.higherKinds
import scala.reflect.ClassTag

final class DocumentedActionBuilderAwaitingResponse[In: ClassTag, +R[_]] private (builder: ActionBuilder[R]) {

  def withoutResponseSchema: DocumentedActionBuilder[In, AnyContent, R] = {
    DocumentedActionBuilder.full(builder)
  }

  def withResponseSchema[Out: ClassTag]: DocumentedActionBuilder[In, Out, R] = {
    DocumentedActionBuilder.full(builder)
  }
}

object DocumentedActionBuilderAwaitingResponse {
  private[playmvc] def request[In: ClassTag, R[_]](builder: ActionBuilder[R]): DocumentedActionBuilderAwaitingResponse[In, R] = {
    new DocumentedActionBuilderAwaitingResponse[In, R](builder)
  }
}
