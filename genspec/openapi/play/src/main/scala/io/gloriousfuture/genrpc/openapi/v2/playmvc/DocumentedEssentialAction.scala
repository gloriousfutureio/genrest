package io.gloriousfuture.genrpc.openapi.v2.playmvc

import akka.util.ByteString
import io.gloriousfuture.genspec.openapi.v2.PathOperation
import play.api.libs.streams.Accumulator
import play.api.mvc._

import scala.reflect.ClassTag

trait DocumentedEssentialAction[In, Out] extends EssentialAction {

  def requestBodyTag: ClassTag[In]

  def responseBodyTag: ClassTag[Out]

  def model: PathOperation = ???  // TODO: Figure out some implicit mechanism to build this thing from routes / controller
}

object DocumentedEssentialAction {
  def full[In, Out](action: EssentialAction)(implicit inTag: ClassTag[In], outTag: ClassTag[Out]): DocumentedEssentialAction[In, Out] = {
    new DocumentedEssentialAction[In, Out] {
      override def requestBodyTag: ClassTag[In] = inTag
      override def responseBodyTag: ClassTag[Out] = outTag
      override def apply(request: RequestHeader): Accumulator[ByteString, Result] = action(request)
    }
  }
}




