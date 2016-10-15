package io.gloriousfuture.genrpc

import RenderScala._
import org.scalatest.WordSpec

class RenderExamplesSpec extends WordSpec {

  "Render[ModelFile]" should {

    "render the Shapes example" in {
      assertResult(RenderExamples.Shapes.rendered) {
        Render.toString(RenderExamples.Shapes.model)
      }
    }
  }
}
