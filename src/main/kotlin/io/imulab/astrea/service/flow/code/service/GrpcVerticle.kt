package io.imulab.astrea.service.flow.code.service

import com.typesafe.config.Config
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.healthchecks.HealthCheckHandler
import io.vertx.ext.healthchecks.Status
import io.vertx.grpc.VertxServerBuilder
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class GrpcVerticle(
    private val flowService: AuthorizeCodeFlowService,
    private val appConfig: Config,
    private val healthCheckHandler: HealthCheckHandler
) : AbstractVerticle() {

    private val logger = LoggerFactory.getLogger(GrpcVerticle::class.java)

    override fun start(startFuture: Future<Void>?) {
        val server = VertxServerBuilder
            .forPort(vertx, appConfig.getInt("service.port"))
            .addService(flowService)
            .build()

        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            logger.info("Authorize code flow service gRPC shutting down...")
            server.shutdown()
            server.awaitTermination(10, TimeUnit.SECONDS)
        })

        server.start { ar ->
            if (ar.failed()) {
                logger.error("Authorize code flow service failed to start.", ar.cause())
            } else {
                startFuture?.complete()
                logger.info("Authorize code flow service started...")
            }
        }

        healthCheckHandler.register("authorize_code_flow_grpc_api") { h ->
            if (server.isTerminated)
                h.complete(Status.KO())
            else
                h.complete(Status.OK())
        }
    }
}