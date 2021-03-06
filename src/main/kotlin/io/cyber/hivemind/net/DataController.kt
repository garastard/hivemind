package io.cyber.hivemind.net

import io.cyber.hivemind.Command
import io.cyber.hivemind.Type
import io.cyber.hivemind.Verb
import io.cyber.hivemind.service.FileVerticle
import io.cyber.hivemind.util.downloadFile
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.Message
import io.vertx.ext.web.RoutingContext


class DataController(val vertx: Vertx) {

    fun getData(context: RoutingContext) {
        val dataId = context.request().getParam("dataId")
        val cmd = Command(Type.DATA, Verb.GET)
        val opts = DeliveryOptions()
        opts.addHeader("id", dataId)
        vertx.eventBus().send(FileVerticle::class.java.name, cmd, opts) { ar: AsyncResult<Message<String>>? ->
            if (ar!!.succeeded()) {
                downloadFile(context, ar.result().body())
            } else {
                ar.cause().printStackTrace()
                context.response().end(ar.toString())
            }
        }
    }

    fun postData(context: RoutingContext) {
            val cmd = Command(Type.DATA, Verb.POST, buffer = context.body)
            val opts = DeliveryOptions()
            val dataId = context.request().getParam("dataId")
            opts.addHeader("id", dataId)
            vertx.eventBus().send(FileVerticle::class.java.name, cmd, opts) { ar: AsyncResult<Message<String>>? ->
                if (ar!!.succeeded()) {
                    context.response().write(ar.result().toString())
                } else {
                    ar.cause().printStackTrace()
                }
                context.response().close()
            }
    }

    fun deleteData(context: RoutingContext) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun findData(context: RoutingContext) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
