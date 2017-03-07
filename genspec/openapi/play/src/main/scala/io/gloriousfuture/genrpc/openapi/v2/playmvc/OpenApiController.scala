package io.gloriousfuture.genrpc.openapi.v2.playmvc

import play.api.mvc._

import scala.language.{higherKinds, implicitConversions}

trait OpenApiController extends Controller with ImplicitDocumentedActionBuilder

trait ImplicitDocumentedActionBuilder {

  implicit def documenting[R[_]](builder: ActionBuilder[R]): DocumentedActionBuilderWrapper[R] = {
    new DocumentedActionBuilderWrapper(builder)
  }

//  implicit def documentWith[C](act: Action[C]): DocumentEssentialActionWrapper = {
//    new DocumentEssentialActionWrapper(act)
//  }

  implicit def documentWith(act: EssentialAction): DocumentEssentialActionWrapper = {
    new DocumentEssentialActionWrapper(act)
  }
}
