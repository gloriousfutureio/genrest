package io.gloriousfuture.genrpc.openapi.v2.playmvc

import play.api.mvc.{ActionBuilder, AnyContent, EssentialAction}

import scala.language.higherKinds
import scala.reflect.ClassTag

class DocumentedActionBuilderWrapper[+R[_]](val underlying: ActionBuilder[R]) extends AnyVal {

  /**
    * Add the response model to the endpoint documentation.
    */
  def withoutSchema: DocumentedActionBuilderAwaitingBodyParser[AnyContent, R] = {
    DocumentedActionBuilderAwaitingBodyParser.response(underlying)
  }

  /**
    * Add the response model to the endpoint documentation.
    */
  def withRequestSchema[In: ClassTag]: DocumentedActionBuilderAwaitingResponse[In, R] = {
    DocumentedActionBuilderAwaitingResponse.request(underlying)
  }

  /**
    * Add the response model to the endpoint documentation.
    */
  def withResponseSchema[Out: ClassTag]: DocumentedActionBuilderAwaitingBodyParser[Out, R] = {
    DocumentedActionBuilderAwaitingBodyParser.response(underlying)
  }
}

final class DocumentEssentialActionWrapper(val underlying: EssentialAction) extends AnyVal {

  def withoutSchema: DocumentedEssentialAction[AnyContent, AnyContent] = DocumentedEssentialAction.full(underlying)

  def withResponseSchema[Out]: ResponseDocumentedAction[Out] = new ResponseDocumentedAction(underlying)

  def withRequestSchema[In]: RequestDocumentedAction[In] = new RequestDocumentedAction(underlying)
}
