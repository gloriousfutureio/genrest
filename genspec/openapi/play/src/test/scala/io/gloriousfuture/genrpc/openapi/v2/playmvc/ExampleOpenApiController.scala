package io.gloriousfuture.genrpc.openapi.v2.playmvc

import org.scalatest.{FutureOutcome, fixture}
import play.api.http.Status._
import play.api.mvc.Action
import play.api.test.ops.AsyncResultExtractors
import play.api.test.{EssentialActionCaller, FakeApplication, FakeRequest, Writeables}

case class X(name: String)

object ExampleOpenApiController extends ExampleOpenApiController
class ExampleOpenApiController extends OpenApiController {

  def actionBuilderWithoutSchema = {
    Action.withoutSchema(Ok)
  }

  def actionBuilderWithOnlyRequestSchema = {
    Action.withRequestSchema[X].withoutResponseSchema(Ok)
  }

  // TODO: Create an interface for both styles and reuse tests

  def actionWithoutSchema = {
    Action(Ok).withoutSchema
  }

  def actionWithOnlyRequestSchema = {
    Action(Ok).withRequestSchema[X].withoutResponseSchema
  }
}

class ExampleOpenApiControllerSpec extends fixture.AsyncFreeSpec
  with EssentialActionCaller
  with AsyncResultExtractors
  with Writeables {

  implicit val app = FakeApplication()
  implicit val mat = app.materializer
  implicit val ec = mat.executionContext

  object FixtureParam extends FixtureParam
  class FixtureParam {
    def controller: ExampleOpenApiController = ExampleOpenApiController
  }

  override def withFixture(test: OneArgAsyncTest): FutureOutcome = test(FixtureParam)

  val it: String = classOf[ExampleOpenApiController].getSimpleName

  s"$it should take a request as an action without an explicit response schema" in { fixture =>
    import fixture._
    for {
      resp <- call(controller.actionBuilderWithOnlyRequestSchema, FakeRequest())
    } yield {
      assertResult(OK)(resp.header.status)
    }
  }
}
