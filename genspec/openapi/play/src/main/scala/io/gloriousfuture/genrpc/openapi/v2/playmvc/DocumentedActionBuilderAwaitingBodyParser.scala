package io.gloriousfuture.genrpc.openapi.v2.playmvc

import play.api.mvc.{ActionBuilder, AnyContent, BodyParser, Result}

import scala.concurrent.Future
import scala.language.higherKinds
import scala.reflect.ClassTag

final class DocumentedActionBuilderAwaitingBodyParser[Out: ClassTag, +R[_]] private (builder: ActionBuilder[R]) {

  def withRequestSchema[In: ClassTag]: DocumentedActionBuilder[In, Out, R] = DocumentedActionBuilder.full(builder)

  def apply[A: ClassTag](bodyParser: BodyParser[A])(block: R[A] => Result): DocumentedAction[A, Out, A] = {
    withRequestSchema[A].apply(bodyParser)(block)
  }

  def apply(block: R[AnyContent] => Result): DocumentedAction[AnyContent, Out, AnyContent] = {
    withRequestSchema[AnyContent].apply(block)
  }

  def apply(block: => Result): DocumentedAction[AnyContent, Out, AnyContent] = {
    withRequestSchema[AnyContent].apply(block)
  }

  def async(block: => Future[Result]): DocumentedAction[AnyContent, Out, AnyContent] = {
    withRequestSchema[AnyContent].async(block)
  }

  def async(block: R[AnyContent] => Future[Result]): DocumentedAction[AnyContent, Out, AnyContent] = {
    withRequestSchema[AnyContent].async(block)
  }

  def async[A: ClassTag](bodyParser: BodyParser[A])(block: R[A] => Future[Result]): DocumentedAction[A, Out, A] = {
    withRequestSchema[A].async(bodyParser)(block)
  }
}

object DocumentedActionBuilderAwaitingBodyParser {
  def response[Out: ClassTag, R[_]](builder: ActionBuilder[R]): DocumentedActionBuilderAwaitingBodyParser[Out, R] = {
    new DocumentedActionBuilderAwaitingBodyParser[Out, R](builder)
  }
}
