package io.gloriousfuture.genrpc.openapi.v2.playmvc

import play.api.mvc.{ActionBuilder, AnyContent, BodyParser, Result}

import scala.concurrent.Future
import scala.language.higherKinds
import scala.reflect.ClassTag

final class DocumentedActionBuilder[In: ClassTag, Out: ClassTag, +R[_]] private (builder: ActionBuilder[R]) {

  def apply[A](bodyParser: BodyParser[A])(block: R[A] => Result): DocumentedAction[In, Out, A] = {
    DocumentedAction.full(builder.apply(bodyParser)(block))
  }

  def apply(block: R[AnyContent] => Result): DocumentedAction[In, Out, AnyContent] = {
    DocumentedAction.full(builder.apply(block))
  }

  def apply(block: => Result): DocumentedAction[In, Out, AnyContent] = {
    DocumentedAction.full(builder.apply(block))
  }

  def async(block: => Future[Result]): DocumentedAction[In, Out, AnyContent] = {
    DocumentedAction.full(builder.async(block))
  }

  def async(block: R[AnyContent] => Future[Result]): DocumentedAction[In, Out, AnyContent] = {
    DocumentedAction.full(builder.async(block))
  }

  def async[A](bodyParser: BodyParser[A])(block: R[A] => Future[Result]): DocumentedAction[In, Out, A] = {
    DocumentedAction.full(builder.async(bodyParser)(block))
  }
}

object DocumentedActionBuilder {
  private[playmvc] def full[In: ClassTag, Out: ClassTag, R[_]](builder: ActionBuilder[R]): DocumentedActionBuilder[In, Out, R] = {
    new DocumentedActionBuilder[In, Out, R](builder)
  }
}
