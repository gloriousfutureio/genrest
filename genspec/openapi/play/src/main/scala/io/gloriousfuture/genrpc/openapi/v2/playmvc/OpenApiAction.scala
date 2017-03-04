package io.gloriousfuture.genrpc.openapi.v2.playmvc

trait OpenApiAction[Rq, Rs] {

  def requestParams: String

  def rqClassName: String
}
