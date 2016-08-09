package io.swagger.classy

case class Api(name: String)

object Api {

  // figure out how to register to the ApiRegistry implicitly

  def registered(api: Api)(implicit registry: ApiRegistry): Api = {
    registry.register(api)
    api
  }
}
