package io.gloriousfuture.genrpc.openapi.v2.playmvc

import play.api.mvc.{Action, BodyParser, Request, Result}

import scala.concurrent.Future
import scala.reflect.ClassTag

trait DocumentedAction[In, Out, C] extends Action[C] with DocumentedEssentialAction[In, Out]

object DocumentedAction {
  def full[In, Out, C](action: Action[C])(implicit inTag: ClassTag[In], outTag: ClassTag[Out]): DocumentedAction[In, Out, C] = {
    new DocumentedAction[In, Out, C] {
      override def requestBodyTag: ClassTag[In] = inTag
      override def responseBodyTag: ClassTag[Out] = outTag
      override def parser: BodyParser[C] = action.parser
      override def apply(request: Request[C]): Future[Result] = action(request)
    }
  }
}
